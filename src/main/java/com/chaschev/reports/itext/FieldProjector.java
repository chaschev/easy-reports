package com.chaschev.reports.itext;

import com.itextpdf.text.Chunk;

import java.lang.reflect.Field;

/**
 * User: chaschev
 * Date: 6/14/13
 */
public class FieldProjector<T> extends CompositeColumnFall.Projector<T> {
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


    public RowData apply(T obj) {
        try {
            Object[] result = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                result[i] = new Object[]{new Chunk(String.valueOf(fields[i].get(obj)))};
            }
            return new RowData(result);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}