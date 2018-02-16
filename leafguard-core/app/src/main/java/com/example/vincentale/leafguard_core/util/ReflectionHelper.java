package com.example.vincentale.leafguard_core.util;

import android.util.Log;

import com.example.vincentale.leafguard_core.model.Oak;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.example.vincentale.leafguard_core.util.StringHelper.capitalize;

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
    public static Object invokeGetter(String field, Object subject, Class subjectClass) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        try {
            Method getter = buildGetter(field, subject, subjectClass);
            return getter.invoke(subject);
        } catch (NoSuchFieldException ex) {
            Method superGetter = buildGetter(field, subject, subjectClass.getSuperclass());
            return superGetter.invoke(subject);
        }

    }

    private static Method buildGetter(String field, Object subject, Class subjectClass) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String getterName;
            if (subjectClass.getDeclaredField(field).getType().equals(boolean.class)) {
            getterName = "is" + capitalize(field);
        } else {
            getterName = "get" + capitalize(field);
        }
        return subjectClass.getMethod(getterName);
    }
}
