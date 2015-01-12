package com.andrOday.appruntimeviewer.data;

import java.util.List;

/**
 * Created by andr0day on 15/1/12.
 */
public class ProClass {

    private String clazz;

    private List<ProMethod> methods;


    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<ProMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ProMethod> methods) {
        this.methods = methods;
    }


}
