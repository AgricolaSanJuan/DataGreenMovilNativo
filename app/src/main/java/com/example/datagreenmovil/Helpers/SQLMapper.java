package com.example.datagreenmovil.Helpers;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class SQLMapper {
    public static <T> List<T> mapResultSetToList(ResultSet rs, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        try {
            while (rs.next()) {
                T obj = clazz.getDeclaredConstructor().newInstance();
                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    String columnName = field.getName();  // Suponiendo que los nombres de campo coinciden con las columnas

                    try {
                        Object value = rs.getObject(columnName);
                        if (value != null) {
                            field.set(obj, value);
                        }
                    } catch (Exception e) {
                        // Si hay un error, simplemente lo ignoramos para evitar crashes
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
