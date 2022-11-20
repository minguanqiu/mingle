package io.github.amings.mingle.utils;


import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Reflection Utils
 *
 * @author Ming
 */

public class ReflectionUtils {

    /**
     * Get Reflection
     * @param builder - Configuration builder for Reflection
     * @return Reflections
     */
    public static Reflections getReflections(ConfigurationBuilder builder) {
        return new Reflections(builder);
    }

    /**
     * Find Field using Annotation
     * @param clazz - Class
     * @param ann - Annotation
     * @return Field
     */
    public static Field findFieldAnnotation(Class<?> clazz, Class<? extends Annotation> ann) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ann)) {
                return field;
            }
        }
        return null;
    }

    public static String findAnnotationByFieldStringValue(Object obj, Class<? extends Annotation> ann) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ann)) {
                try {
                    return (String) field.get(obj);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    return null;
                }
            }
        }
        return null;
    }

    public static boolean isMethodAnnotation(Method method, Class<? extends Annotation> ann) {
        return method.getAnnotation(ann) != null;
    }

    public static Class<?> getGenericClassSuper(Type type, int index) {
        ParameterizedType parameterized = (ParameterizedType) type;
        return (Class<?>) parameterized.getActualTypeArguments()[index];
    }

    public static Class<?> getGenericClass(Class<?> clazz, int index) {
        Type superclass = clazz.getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return (Class<?>) parameterized.getActualTypeArguments()[index];
    }

    public static Class<?> getGenericClassWithGeneric(Class<?> clazz, int index) {
        Type superclass = clazz.getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return (Class<?>) ((ParameterizedType) parameterized.getActualTypeArguments()[index]).getRawType();
    }

    public static String toHexString(Object object) {
        return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
    }

    public static <T> T newInstance(Class<T> tClass) {
        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }

}
