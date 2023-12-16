package io.github.amings.mingle.utils;


import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

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

    public static Type findGenericSuperclass(Class<?> clazz, Class<?> target) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            if (parameterizedType.getRawType().equals(target)) {
                return genericSuperclass;
            } else {
                genericSuperclass = parameterizedType.getRawType();
            }
        } else {
            if (!genericSuperclass.equals(target)) {
                genericSuperclass = findGenericSuperclass((Class<?>) genericSuperclass, target);
            }
        }
        return genericSuperclass;
    }

    public static String toHexString(Object object) {
        return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
    }

    public static <T> T newInstance(Class<T> tClass) {
        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            return null;
        }
    }

}
