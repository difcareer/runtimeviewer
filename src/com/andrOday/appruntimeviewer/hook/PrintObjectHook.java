package com.andrOday.appruntimeviewer.hook;

import com.andrOday.appruntimeviewer.util.JsonUtil;
import com.andrOday.appruntimeviewer.util.LogUtil;
import de.robv.android.xposed.XC_MethodHook;

/**
 * Created by andr0day on 2015/1/12.
 */
public class PrintObjectHook extends XC_MethodHook {

    protected void beforeHookedMethod(MethodHookParam param) {
        Object obj = param.thisObject;
        StringBuilder sb = new StringBuilder();
        sb.append("Before method execute,this object:\n");
        sb.append(JsonUtil.toJSONString(obj));
        sb.append("\n");
        LogUtil.info_log(sb.toString());
    }
}
