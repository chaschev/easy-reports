package com.chaschev.reports.billing;

import com.google.common.base.Function;
import com.itextpdf.text.Chunk;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * User: chaschev
 * Date: 6/14/13
 */
public class MethodsProjector<T> implements Function<T, List> {
    protected Method[] methods;

    public MethodsProjector(Class<T> aClass, String... methodNames) {
        try {
            methods = new Method[methodNames.length];

            for (int i = 0, fieldNamesLength = methodNames.length; i < fieldNamesLength; i++) {
                String fieldName = methodNames[i];

                Method method = aClass.getMethod(fieldName);
                method.setAccessible(true);

                methods[i] = method;
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public List apply(T obj) {
        try {
            List result = new ArrayList(methods.length);
            for (int i = 0; i < methods.length; i++) {
                result.add(new Object[]{new Chunk(String.valueOf(methods[i].invoke(obj)))});
            }
            return result;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
