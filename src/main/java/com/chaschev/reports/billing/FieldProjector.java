package com.chaschev.reports.billing;

import com.google.common.base.Function;
import com.itextpdf.text.Chunk;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * User: chaschev
 * Date: 6/14/13
 */
public class FieldProjector<T> implements Function<T, List> {
    protected Field[] fields;

    public FieldProjector(Class<T> aClass, String... fieldNames) {
        try {
            fields = new Field[fieldNames.length];

            for (int i = 0, fieldNamesLength = fieldNames.length; i < fieldNamesLength; i++) {
                String fieldName = fieldNames[i];

                Field field = aClass.getField(fieldName);
                field.setAccessible(true);

                fields[i] = field;
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    public List apply(T obj) {
        try {
            List result = new ArrayList(fields.length);
            for (int i = 0; i < fields.length; i++) {
                result.add(new Object[]{new Chunk(String.valueOf(fields[i].get(obj)))});
            }
            return result;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
