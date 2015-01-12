package com.andrOday.appruntimeviewer.hook;

import com.alibaba.fastjson.JSON;
import com.andrOday.appruntimeviewer.util.LogUtil;
import de.robv.android.xposed.XC_MethodHook;

/**
 * Created by andr0day on 2015/1/12.
 */
public class PrintObjectHook extends XC_MethodHook {

    protected void beforeHookedMethod(MethodHookParam param) {
        Object obj = param.thisObject;
        StringBuilder sb = new StringBuilder();
        sb.append("this object:\n");
        sb.append(JSON.toJSONString(obj));
        sb.append("\n");
        LogUtil.log(sb.toString());
    }
}
