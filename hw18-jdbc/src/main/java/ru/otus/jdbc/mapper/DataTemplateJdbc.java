package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.annotation.Id;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
        DbExecutor dbExecutor,
        EntitySQLMetaData entitySQLMetaData
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
            connection,
            entitySQLMetaData.getSelectByIdSql(),
            List.of(id),
            rs -> {
                try {
                    if (rs.next()) {
                        entityClassMetaData = initMetadata();
                        Constructor<T> constructor = entityClassMetaData.getConstructor();

                        T object = constructor.newInstance();
                        setFieldsValues(entityClassMetaData, object, rs);
                        return object;
                    }
                    return null;
                } catch (Exception e) {
                    throw new RuntimeException("Не найдена запись по id=" + id);
                }
            }
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
            connection,
            entitySQLMetaData.getSelectAllSql(),
            Collections.emptyList(),
            rs -> {
                List<T> result = new ArrayList<>();
                entityClassMetaData = initMetadata();
                Constructor<T> constructor = entityClassMetaData.getConstructor();

                try {
                    while (rs.next()) {
                        T object = constructor.newInstance();
                        setFieldsValues(entityClassMetaData, object, rs);
                        result.add(object);
                    }
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
        .orElseThrow(() -> new RuntimeException("Ошибка в DataTemplateJdbc.findAll"));
    }

    @Override
    public long insert(Connection connection, T object) {
        return dbExecutor.executeStatement(
            connection,
            entitySQLMetaData.getInsertSql(),
            getDeclaredFieldsValues(object, field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public void update(Connection connection, T object) {
        dbExecutor.executeStatement(
            connection,
            entitySQLMetaData.getUpdateSql(),
            Stream.concat(
                getDeclaredFieldsValues(object, field -> !field.isAnnotationPresent(Id.class)),
                getDeclaredFieldsValues(object, field -> field.isAnnotationPresent(Id.class))
            ).collect(Collectors.toList())
        );
    }

    private Stream<Object> getDeclaredFieldsValues(
        T object,
        Predicate<? super Field> predicate
    ) {
        return Arrays.stream(object.getClass().getDeclaredFields())
            .filter(predicate)
            .map(field -> {
                try {
                    field.setAccessible(true);
                    return field.get(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Невозможно получить значение для поля " + field.getName());
                }
            });
    }

    private EntityClassMetaData<T> initMetadata() {
        if (entityClassMetaData != null) {
            return entityClassMetaData;
        }

        Field metadataField = Arrays.stream(entitySQLMetaData
            .getClass()
            .getDeclaredFields())
            .filter(field -> field.getType() == EntityClassMetaData.class)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Поле с типом EntityClassMetaData не найдено!"));

        metadataField.setAccessible(true);

        try {
            return (EntityClassMetaData<T>) metadataField.get(entitySQLMetaData);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении значения поля EntityClassMetaData!");
        }
    }

    private void setFieldsValues(EntityClassMetaData<T> metaData, T object, ResultSet rs) {
        List<Field> fields = metaData.getAllFields();
        Object value;
        for (Field field : fields) {
            String fieldName = field.getName();
            try {
                Field objField = object.getClass().getDeclaredField(fieldName);
                objField.setAccessible(true);
                Class<?> fieldType = field.getType();

                if (fieldType.equals(Long.class)) {
                    value = rs.getLong(fieldName);
                } else if (fieldType.equals(String.class)) {
                    value = rs.getString(fieldName);
                } else {
                    throw new RuntimeException("Неизвестный тип данных у поля!");
                }
                objField.set(object, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
