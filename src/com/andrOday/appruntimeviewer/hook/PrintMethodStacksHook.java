package com.andrOday.appruntimeviewer.hook;

import com.andrOday.appruntimeviewer.util.LogUtil;
import de.robv.android.xposed.XC_MethodHook;

/**
 * Created by andr0day on 2015/1/6.
 */
public class PrintMethodStacksHook extends XC_MethodHook {

    protected void beforeHookedMethod(MethodHookParam param) {
        LogUtil.info_log("Before method execute,stacks:");
        LogUtil.printStack();
    }
}
