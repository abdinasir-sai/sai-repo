package com.nouros.payrollmanagement.service.impl;

import java.lang.reflect.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ReflectionUtils {
    // Private constructor to prevent instantiation

    private static final Logger log = LogManager.getLogger(ReflectionUtils.class);

    private ReflectionUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Object getFieldValue(Object obj, String fieldName) throws ReflectiveOperationException {
        log.debug("inside getFieldValue fieldName: {}", fieldName);
        try {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (!field.canAccess(obj)) {
            field.trySetAccessible();
        }
        return field.get(obj);
        }catch(NoSuchFieldException | IllegalAccessException e) {
        	throw new ReflectiveOperationException("Failed to access field: " + fieldName, e);
        }
    }

    public static String getNestedFieldValue(Object obj, String fieldName) throws ReflectiveOperationException {
        String[] parts = fieldName.split("\\.");
        Object value = obj;
        for (String part : parts) {
            value = getFieldValue(value, part);
        }
        return String.valueOf(value);
    }

    public static void setFieldValue(Object obj, Object value, String fieldName) throws ReflectiveOperationException {
        log.debug("inside setFieldValue value: {} fieldName: {}", value, fieldName);
        try {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (!field.canAccess(obj)) {
            field.trySetAccessible();
        }
        }catch(NoSuchFieldException e) {
        	throw new ReflectiveOperationException("Failed to set field: " + fieldName, e);
        }
       
    }
}
