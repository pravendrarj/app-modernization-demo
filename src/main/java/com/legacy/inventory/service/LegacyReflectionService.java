package com.legacy.inventory.service;

import java.lang.reflect.*;
import java.util.*;

/**
 * LEGACY REFLECTION AND SERIALIZATION
 * Java upgrade issue: Uses reflection without generics or type safety
 * Java upgrade issue: Uses ObjectInputStream/ObjectOutputStream (security risk)
 * Java upgrade issue: Manual bean copying instead of MapStruct or ModelMapper
 */
public class LegacyReflectionService {
    
    // JAVA UPGRADE ISSUE: Using reflection with raw types (no generics)
    public static Object instantiateClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        return clazz.newInstance(); // Deprecated: use getConstructor().newInstance()
    }
    
    // JAVA UPGRADE ISSUE: Using reflection to access private fields
    public static void setFieldValue(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
    
    // JAVA UPGRADE ISSUE: Using reflection to invoke private methods
    public static Object invokePrivateMethod(Object object, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        Method method = object.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(object, args);
    }
    
    // JAVA UPGRADE ISSUE: Using Serializable with ObjectInputStream (security vulnerability)
    // Should use JSON serialization with Jackson or other modern frameworks
    public static byte[] serializeObject(Object obj) throws Exception {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }
    
    // JAVA UPGRADE ISSUE: Using ObjectInputStream (deserialization vulnerability)
    public static Object deserializeObject(byte[] data) throws Exception {
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(data);
        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais);
        return ois.readObject();
    }
    
    // JAVA UPGRADE ISSUE: Manual bean copying (verbose, error-prone)
    public static class LegacyBeanCopier {
        public static <T> T copyProperties(Object source, Class<T> targetClass) throws Exception {
            T target = targetClass.newInstance();
            for (Field sourceField : source.getClass().getDeclaredFields()) {
                sourceField.setAccessible(true);
                try {
                    Field targetField = targetClass.getDeclaredField(sourceField.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, sourceField.get(source));
                } catch (NoSuchFieldException e) {
                    // Ignore fields that don't exist in target
                }
            }
            return target;
        }
    }
    
    // JAVA UPGRADE ISSUE: Using reflection for dependency injection (Spring alternative)
    public static void autowireFields(Object object, Map<String, Object> beans) throws Exception {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                String beanName = field.getName();
                if (beans.containsKey(beanName)) {
                    field.set(object, beans.get(beanName));
                }
            }
        }
    }
    
    // Placeholder for @Autowired annotation
    @interface Autowired {}
}
