package com.andrOday.appruntimeviewer;

import android.app.Application;
import android.util.Log;
import com.andrOday.appruntimeviewer.data.ProClass;
import com.andrOday.appruntimeviewer.data.ProMethod;
import com.andrOday.appruntimeviewer.hook.PrintFieldsHook;
import com.andrOday.appruntimeviewer.hook.PrintMethodParametersHook;
import com.andrOday.appruntimeviewer.hook.PrintMethodStacksHook;
import com.andrOday.appruntimeviewer.hook.PrintObjectHook;
import com.andrOday.appruntimeviewer.util.*;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;


public class RunTimeViewer implements IXposedHookLoadPackage {

    public static Application targetApplication;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.isFirstApplication) {
            final String appClassName = lpparam.appInfo != null ? lpparam.appInfo.className : null;
            Log.e(LogUtil.TAG, "load appClassName:" + appClassName);
            if (appClassName != null) {
                try {
                    HookUtil.findAndHookMethod(appClassName, lpparam.classLoader, "onCreate", new XC_MethodHook() {
                        @Override
                        public void afterHookedMethod(MethodHookParam param) {
                            targetApplication = (Application) param.thisObject;
                            LogUtil.init();
                            LogUtil.event_log(appClassName + " loaded");
                            ConfigUtil.parse();
                            if (ConfigUtil.hasConfig()) {
                                Method[] methods = HookUtil.class.getDeclaredMethods();
                                for (Method it : methods) {
                                    it.setAccessible(true);
                                    //反射调用 findAndHookMethod
                                    if ("findAndHookMethod".equals(it.getName())) {
                                        Class<?>[] paraTypes = it.getParameterTypes();
                                        if ("java.lang.String".equals(paraTypes[0].getCanonicalName())) {
                                            for (ProClass proClass : ConfigUtil.proClasses) {
                                                if (proClass.getMethods() != null) {
                                                    for (ProMethod proMethod : proClass.getMethods()) {
                                                        List<String> parameters = proMethod.getParameters();
                                                        List<String> fields = proMethod.getFields();
                                                        List<String> cmds = proMethod.getCmds();
                                                        Object[] tmp = parameters.toArray();
                                                        Object[] pass_param = new Object[tmp.length + 1];
                                                        System.arraycopy(tmp, 0, pass_param, 0, tmp.length);
                                                        for (String cmd : cmds) {
                                                            if (CmdUtil.printParameters.equals(cmd)) {
                                                                pass_param[pass_param.length - 1] = new PrintMethodParametersHook();
                                                            } else if (CmdUtil.printStacks.equals(cmd)) {
                                                                pass_param[pass_param.length - 1] = new PrintMethodStacksHook();
                                                            } else if (CmdUtil.printObject.equals(cmd)) {
                                                                pass_param[pass_param.length - 1] = new PrintObjectHook();
                                                            } else if (CmdUtil.printFields.equals(cmd)) {
                                                                PrintFieldsHook printFieldsHook = new PrintFieldsHook();
                                                                printFieldsHook.fields = fields;
                                                                pass_param[pass_param.length - 1] = printFieldsHook;
                                                            } else {
                                                                pass_param = null;
                                                            }
                                                            doHook(cmd, it, proClass.getClazz(), lpparam.classLoader, proMethod, pass_param);
                                                        }
                                                    }

                                                }

                                            }
                                        }
                                    }
                                }
                            }

                        }
                    });

                } catch (NoSuchMethodError e) {
//                    XposedBridge.log(e);
                    // reflect onCreate error
                }
            }
        }
    }

    public void doHook(String cmd, Method findAndHookMethod, String clazz, ClassLoader classLoader, ProMethod proMethod, Object[] parameters) {
        if (parameters != null) {
            try {
                findAndHookMethod.invoke(null, clazz, classLoader, proMethod.getMethod(), parameters);
                LogUtil.event_log(cmd + " already hooked -> class=" + clazz + " method=" + JsonUtil.toJSONString(proMethod));
            } catch (Exception e) {
                LogUtil.event_log("hook method error:");
                e.printStackTrace(LogUtil.eventWriter);
            }
        }
    }
}
