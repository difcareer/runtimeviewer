package com.andrOday.appruntimeviewer.hook;

import com.alibaba.fastjson.JSON;
import com.andrOday.appruntimeviewer.util.LogUtil;
import de.robv.android.xposed.XC_MethodHook;


public class PrintMethodParametersHook extends XC_MethodHook {

    protected void beforeHookedMethod(MethodHookParam param) {
        logMethod("Before", param);
    }

    protected void afterHookedMethod(MethodHookParam param) {
        logMethod("After", param);
    }

    public void logMethod(String type, MethodHookParam param) {
        Object[] args = param.args;
        Object thisObj = param.thisObject;
        String thisObjClazz = (thisObj != null ? (thisObj.getClass() != null ? thisObj.getClass().getCanonicalName() : "thisObj'class is null") : "thisObj is null");
        StringBuilder sb = new StringBuilder(type +" "+ thisObjClazz + "->" + param.method.getName() + "\n");
        if (args != null) {
            sb.append("args:\n");
            for (Object obj : args) {
                String clazzName = (obj != null ? (obj.getClass() != null ? obj.getClass().getCanonicalName() : "param'class is null") : "param is null");
                sb.append(clazzName + ":");
                sb.append(JSON.toJSONString(obj) + "\n");
            }
        } else {
            sb.append("args are empty");
        }
        sb.append("\n");
        LogUtil.log(sb.toString());
    }
}
