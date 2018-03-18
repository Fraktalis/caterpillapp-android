package com.fraktalis.caterpillapp.leafguard_core.util;

import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.fraktalis.caterpillapp.leafguard_core.util.StringHelper.capitalize;

/**
 * Utility class to handle dynamic invocation of getter for Object
 *
 */

public class ReflectionHelper {

    /**
     * Return the result of the getter method of the given field. In order to avoid metadata parsing,
     * this function assume that you follow naming conventions for getter : getField or isField for boolean
     * @param field : the field name in lowercase
     * @param subject : The considered object
     * @param subjectClass : The expected class where to find the getters
     * @return
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object invokeGetter(String field, @NonNull Object subject, Class subjectClass) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        try {
            Method getter = buildGetter(field, subjectClass);
            return getter.invoke(subject);
        } catch (NoSuchFieldException ex) {
            if (subjectClass.getSuperclass() != Object.class) {
                Method superGetter = buildGetter(field, subjectClass.getSuperclass());
                return superGetter.invoke(subject);
            }
            return null;
        }

    }

    private static Method buildGetter(String field, Class subjectClass) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String getterName;
            if (subjectClass.getDeclaredField(field).getType().equals(boolean.class)) {
            getterName = "is" + capitalize(field);
        } else {
            getterName = "get" + capitalize(field);
        }
        return subjectClass.getMethod(getterName);
    }
}
