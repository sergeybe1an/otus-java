package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import ru.otus.jdbc.annotation.Id;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor getConstructor() {
        try {
             return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Id.class))
            .findFirst()
            .orElseThrow(() -> {
                throw new RuntimeException("Полей, помеченных аннотацией " + Id.class.getSimpleName() + " не найдено!");
            });
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> !field.isAnnotationPresent(Id.class))
            .collect(Collectors.toList());
    }
}
