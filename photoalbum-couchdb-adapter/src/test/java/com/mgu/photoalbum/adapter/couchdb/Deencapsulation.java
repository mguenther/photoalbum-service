package com.mgu.photoalbum.adapter.couchdb;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Deencapsulation {

    @SuppressWarnings ("serial")
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = new HashMap<Class<?>, Class<?>>() {

        {
            put(Boolean.class,
                    boolean.class);
            put(Character.class,
                    char.class);
            put(Byte.class, byte.class);
            put(Short.class,
                    short.class);
            put(Integer.class,
                    int.class);
            put(Float.class,
                    float.class);
            put(Long.class, long.class);
            put(Double.class,
                    double.class);
        }
    };

    private static Field declaredField(final Class<?> clazz, final String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignore) {
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return declaredField(superClass, fieldName);
            }
            throw new IllegalArgumentException("No instance field of name \"" + fieldName
                    + "\" found in " + clazz);
        }
    }

    @SuppressWarnings ("unchecked")
    public static <T> T staticFieldValue(final Class<?> clazz, final String fieldName) {
        final Field field = declaredField(clazz, fieldName);
        field.setAccessible(true);
        try {
            return (T) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access field " + fieldName, e);
        }
    }

    @SuppressWarnings ("unchecked")
    public static <T> T getFieldValue(final Object targetObj, final String fieldName) {
        final Class<?> dynamicClass = targetObj.getClass();
        final Field field = declaredField(dynamicClass, fieldName);
        field.setAccessible(true);
        try {
            return (T) field.get(targetObj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access field " + fieldName, e);
        }
    }

    public static <T> void setFieldValue(final Object targetObj, final String fieldName, final T fieldValue) {
        final Class<?> dynamicClass = targetObj.getClass();
        final Field field = declaredField(dynamicClass, fieldName);
        field.setAccessible(true);
        try {
            field.set(targetObj, fieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access field " + fieldName, e);
        }
    }

    public static <T> void setFieldValue(final Class<?> dynamicClass, final Object targetObj,
                                         final String fieldName, final T fieldValue) {
        final Field field = declaredField(dynamicClass, fieldName);
        field.setAccessible(true);
        try {
            field.set(targetObj, fieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access field " + fieldName, e);
        }
    }

    private static Class<?>[] parameterTypes(final Object[] methodArgs) {
        final Class<?>[] paramTypes = new Class<?>[methodArgs.length];
        for (int i = 0; i < methodArgs.length; i++) {
            paramTypes[i] = methodArgs[i].getClass();
        }
        return paramTypes;
    }

    public static <T> T invokeStatic(Class<?> clazz, String methodName, Object... methodArgs) {
        final Class<?>[] paramTypes = parameterTypes(methodArgs);
        final Method method = findSpecifiedMethod(clazz, methodName, paramTypes);
        return invokeStatic(method, methodArgs);
    }

    @SuppressWarnings ("unchecked")
    public static <T> T invokeStatic(final Method method, final Object... methodArgs) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        try {
            return (T) method.invoke(null, methodArgs);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failure to invoke method: " + method, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Error) {
                throw (Error) cause;
            } else if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                return null;
            }
        }
    }

    public static <T> T invoke(Object targetInstance, String methodName, Object... methodArgs) {
        final Class<?> clazz = targetInstance.getClass();
        final Class<?>[] paramTypes = parameterTypes(methodArgs);
        final Method method = findSpecifiedMethod(clazz, methodName, paramTypes);
        return invoke(targetInstance, method, methodArgs);
    }

    @SuppressWarnings ("unchecked")
    public static <T> T invoke(Object targetInstance, Method method, Object... methodArgs) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        try {
            return (T) method.invoke(targetInstance, methodArgs);
        } catch (IllegalAccessException e) {
            assert false : "Not expected to happen because the method was made accessible";
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failure to invoke method: " + method, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            if (cause instanceof Error) {
                throw (Error) cause;
            } else if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throwCheckedException((Exception) cause);
                return null;
            }
        }
    }

    public static void throwCheckedException(Exception exceptionToThrow) {
        synchronized (ThrowOfCheckedException.class) {
            ThrowOfCheckedException.exceptionToThrow = exceptionToThrow;

            try {
                // noinspection ClassNewInstance
                ThrowOfCheckedException.class.newInstance();
            } catch (InstantiationException ignore) {} catch (IllegalAccessException ignored) {}
        }
    }

    private static final class ThrowOfCheckedException {

        static Exception exceptionToThrow;

        @SuppressWarnings ("unused")
        ThrowOfCheckedException() throws Exception {
            throw exceptionToThrow;
        }
    }

    private static Method findSpecifiedMethod(Class<?> theClass, String methodName,
                                              Class<?>[] paramTypes) {
        for (Method declaredMethod : theClass.getDeclaredMethods()) {
            if (declaredMethod.getName().equals(methodName)
                    && matchesParameterTypes(declaredMethod.getParameterTypes(), paramTypes)) {
                return declaredMethod;
            }
        }

        Class<?> superClass = theClass.getSuperclass();

        if (superClass != null && superClass != Object.class) {
            return findSpecifiedMethod(superClass, methodName, paramTypes);
        }

        throw new IllegalArgumentException("Specified method not found");
    }

    private static boolean matchesParameterTypes(Class<?>[] declaredTypes, Class<?>[] specifiedTypes) {
        int i0 = 0;

        for (int i = i0; i < declaredTypes.length; i++) {
            Class<?> declaredType = declaredTypes[i];
            Class<?> specifiedType = specifiedTypes[i - i0];
            if (declaredType.isAssignableFrom(specifiedType)) {
                // OK, move to the next parameter
            } else if (isSameTypeIgnoringAutoBoxing(declaredType, specifiedType)) {
                // OK, move to next parameter.
            } else {
                return false;
            }
        }

        return true;
    }

    private static boolean isSameTypeIgnoringAutoBoxing(Class<?> firstType, Class<?> secondType) {
        return firstType == secondType || firstType.isPrimitive()
                && isWrapperOfPrimitiveType(firstType, secondType) || secondType.isPrimitive()
                && isWrapperOfPrimitiveType(secondType, firstType);
    }

    private static boolean isWrapperOfPrimitiveType(Class<?> primitiveType, Class<?> otherType) {
        return primitiveType == WRAPPER_TO_PRIMITIVE.get(otherType);
    }

    public static boolean isWrapperOfPrimitiveType(Class<?> type) {
        return WRAPPER_TO_PRIMITIVE.containsKey(type);
    }
}