package com.chaschev.reports.itext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: chaschev
 * Date: 6/14/13
 */
public class MethodsProjector<T> extends Projector<T> {
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

    @Override
    protected RowData apply(T obj) {
        try {
            Object[] r = new Object[methods.length];
            for (int i = 0; i < methods.length; i++) {
                r[i] = chunks(methods[i].invoke(obj));
            }
            return new RowData(r);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
