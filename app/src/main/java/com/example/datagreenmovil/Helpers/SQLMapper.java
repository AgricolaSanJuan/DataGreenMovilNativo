package com.example.datagreenmovil.Helpers;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class SQLMapper {
    public static <T> List<T> mapResultSetToList(ResultSet rs, Class<T> clazz) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<T> resultList = new ArrayList<>();
        try {
            while (rs.next()) {
                T obj = clazz.getDeclaredConstructor().newInstance();
                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    String columnName = field.getName();

                    try {
                        Object value = rs.getObject(columnName);
                        if (value != null) {
                            Class<?> fieldType = field.getType();

                            if (fieldType == Double.class || fieldType == double.class) {
                                field.set(obj, ((Number) value).doubleValue());
                            } else if (fieldType == Integer.class || fieldType == int.class) {
                                field.set(obj, ((Number) value).intValue());
                            } else if (fieldType == Long.class || fieldType == long.class) {
                                field.set(obj, ((Number) value).longValue());
                            } else if (fieldType == Float.class || fieldType == float.class) {
                                field.set(obj, ((Number) value).floatValue());
                            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                                field.set(obj, (value instanceof Number) ? ((Number) value).intValue() != 0 : Boolean.parseBoolean(value.toString()));
                            } else if (value instanceof Timestamp) {
                                // Convertir Timestamp a String
                                field.set(obj, sdf.format((Timestamp) value));
                            } else {
                                field.set(obj, value); // Asignación directa para otros tipos (String, etc.)
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error al mapear campo: " + columnName + " (" + e.getMessage() + ")");
                    }
                }

                resultList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}