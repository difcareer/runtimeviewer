package com.andrOday.appruntimeviewer.data;

import java.util.List;

/**
 * Created by andr0day on 15/1/12.
 */
public class ProMethod {

    private String method;

    private List<String> parameters;

    private List<String> cmds;

    private List<String> fields;

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getCmds() {
        return cmds;
    }

    public void setCmds(List<String> cmds) {
        this.cmds = cmds;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
