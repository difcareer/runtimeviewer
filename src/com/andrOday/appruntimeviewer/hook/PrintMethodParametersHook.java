package com.andrOday.appruntimeviewer.hook;

import com.andrOday.appruntimeviewer.util.JsonUtil;
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
        StringBuilder sb = new StringBuilder();
        sb.append(type + " method execute,parameters:\n");
        if (thisObj == null) {
            sb.append("thisObj is null\n");
        } else if (thisObj.getClass() == null) {
            sb.append("thisObj.getClass() is null\n");
        } else {
            sb.append(thisObj.getClass().getCanonicalName() + " -> " + param.method.getName() + "\n");
        }
        if (args != null) {
            int i = 0;
            for (Object obj : args) {
                if (obj == null) {
                    sb.append("args[" + i + "] is null\n");
                } else if (obj.getClass() == null) {
                    sb.append("args[" + i + "].getClass() is null\n");
                } else {
                    sb.append(obj.getClass().getCanonicalName() + " -> " + JsonUtil.toJSONString(obj) + "\n");
                }
            }
        } else {
            sb.append("args are empty");
        }
        sb.append("\n");
        LogUtil.info_log(sb.toString());
    }
}
