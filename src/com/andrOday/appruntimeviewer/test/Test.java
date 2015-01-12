package com.andrOday.appruntimeviewer.test;

import java.lang.reflect.Method;

public class Test {

    public static void main(String[] args) throws Exception {
//        print(new Test());
//        printStack();
        testSplit();
        Child child = new Child();
        Method method = getMethod(child, "parent", null);
        method.invoke(child, null);

    }

    public static void print(Object obj) {
        System.out.println(obj.getClass().getSimpleName());
    }

    public static void printStack() {
        new Throwable().printStackTrace();
    }

    public static void testSplit() {
        String str = "xxx|xxx";
        String[] strs = str.split("\\|");
        for (String it : strs) {
            System.out.println(it);
        }
    }

    public static Method getMethod(Object obj, String methodName, Class<?>... parameterTypes) {
        for (Class clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                return clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (Exception e) {

            }
        }
        return null;
    }

}
