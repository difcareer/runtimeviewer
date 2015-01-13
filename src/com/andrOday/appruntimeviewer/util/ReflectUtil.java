package com.andrOday.appruntimeviewer.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ReflectUtil {

    public Method getMethod(String clazz, String method, String... parameters) {
        return null;

    }

    public static String toString(Object object) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (object != null) {
            Class clazz = object.getClass();
            while (clazz != null) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field it : fields) {
                    it.setAccessible(true);
                    String name = it.getName();
                    String value;
                    try {
                        value = it.get(object).toString();
                    } catch (Exception e) {
                        value = "reflect error";
                    }
                    sb.append("\"" + name + "\":\"" + value + "\"\n");
                }
                clazz = clazz.getSuperclass();
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
