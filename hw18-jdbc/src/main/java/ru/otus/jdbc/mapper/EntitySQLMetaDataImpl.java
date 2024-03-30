package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return getSelectAllSql() + " where " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        return "insert into " + entityClassMetaData.getName() + " (" +
            entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> ((Field)field).getName().toLowerCase())
                .collect(Collectors.joining(", ")) + ") values (" +
            entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> "?")
                .collect(Collectors.joining(","))
        + ")";
    }

    @Override
    public String getUpdateSql() {
        return "update " + entityClassMetaData.getName() + " set " +
            entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> field.toString() + " = ?")
                .collect(Collectors.joining(",")) + " where " +
            entityClassMetaData.getAllFields() + " = ?";
    }
}
