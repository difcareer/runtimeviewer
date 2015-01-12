package com.andrOday.appruntimeviewer.test;

import com.alibaba.fastjson.JSON;
import com.andrOday.appruntimeviewer.data.ProClass;

import java.lang.reflect.Method;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
//        print(new Test());
//        printStack();
//        testSplit();
//        Child child = new Child();
//        Method method = getMethod(child, "parent", null);
//        method.invoke(child, null);
//        testArray();
        testParse();

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

    public static void testArray(){
        String[] str1 = new String[3];
        String[] str2 = new String[2];
        str1 = str2;
        System.out.println(str1.length);
    }

    public static void testParse(){
        String json = "[{\n" +
                "\t\"clazz\":\"com.gurkedev.wifiprotector.WifiProtector\",\n" +
                "\t\"methods\":[{\n" +
                "\t                \"method\":\"xxx\",\n" +
                "\t\t\tparameters:[\"11\",\"22\"],\n" +
                "\t\t\t\"cmds\":[\"33\",\"44\"],\n" +
                "\t\t\t\"fields\":[\"55\",\"66\"]\n" +
                "\t\t}]\n" +
                "\t\n" +
                "\n" +
                "}]";
        List<ProClass> proClasses =JSON.parseArray(json, ProClass.class);
        System.out.println(proClasses.get(0).getMethods().get(0).getCmds());


    }

}
