package com.andrOday.appruntimeviewer;

import android.app.Application;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.andrOday.appruntimeviewer.hook.PrintFieldsHook;
import com.andrOday.appruntimeviewer.hook.PrintMethodParametersHook;
import com.andrOday.appruntimeviewer.hook.PrintMethodStacksHook;
import com.andrOday.appruntimeviewer.hook.PrintObjectHook;
import com.andrOday.appruntimeviewer.util.CmdUtil;
import com.andrOday.appruntimeviewer.util.ConfigUtil;
import com.andrOday.appruntimeviewer.util.HookUtil;
import com.andrOday.appruntimeviewer.util.LogUtil;
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
                            LogUtil.log(appClassName + " loaded");
                            ConfigUtil.parse();
                            if (ConfigUtil.hasConfig()) {
                                Method[] methods = HookUtil.class.getDeclaredMethods();
                                for (Method it : methods) {
                                    it.setAccessible(true);
                                    //反射调用 findAndHookMethod
                                    if ("findAndHookMethod".equals(it.getName())) {
                                        Class<?>[] paraTypes = it.getParameterTypes();
                                        if ("java.lang.String".equals(paraTypes[0].getCanonicalName())) {
                                            for (List<String> hook : ConfigUtil.methods) {
                                                String type = hook.get(0);
                                                String clazz = hook.get(1);
                                                String method = hook.get(2);
                                                Object[] parameters = buildParameters(hook);
                                                String[] cmds = type.split("\\|");
                                                for (String cmd : cmds) {
                                                    if (CmdUtil.printParameters.equals(cmd)) {
                                                        parameters[parameters.length - 1] = new PrintMethodParametersHook();
                                                        doHook(cmd, it, clazz, lpparam.classLoader, method, hook, parameters);
                                                    } else if (CmdUtil.printStacks.equals(cmd)) {
                                                        parameters[parameters.length - 1] = new PrintMethodStacksHook();
                                                        doHook(cmd, it, clazz, lpparam.classLoader, method, hook, parameters);
                                                    } else if (CmdUtil.printObject.equals(cmd)) {
                                                        parameters[parameters.length - 1] = new PrintObjectHook();
                                                        doHook(cmd, it, clazz, lpparam.classLoader, method, hook, parameters);
                                                    } else if (CmdUtil.printFields.equals(cmd)) {
                                                        String raw = (String) parameters[parameters.length - 2];
                                                        String[] parts = raw.split("\\|");
                                                        PrintFieldsHook printFieldsHook = new PrintFieldsHook();
                                                        printFieldsHook.fields = Arrays.asList(parts);
                                                        Object[] newParameters = new Object[parameters.length - 1];
                                                        System.arraycopy(parameters, 0, newParameters, 0, newParameters.length);
                                                        doHook(cmd, it, clazz, lpparam.classLoader, method, hook, newParameters);
                                                    }
                                                }
                                            }
                                            break;
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

    public void doHook(String cmd, Method findAndHookMethod, String clazz, ClassLoader classLoader, String method, List<String> protoMethod, Object[] parameters) {
        try {
            findAndHookMethod.invoke(null, clazz, classLoader, method, parameters);
            LogUtil.log(cmd + " hooked:" + JSON.toJSONString(protoMethod));
        } catch (Exception e) {
            LogUtil.log("hook method error:");
            e.printStackTrace(LogUtil.logWriter);
        }
    }

    /**
     * 原始list的格式为：[type,class,method,paramter1,...parametern]    paramter1~paramtern可以没有
     * 多预留了一个位置，方便放置callback对象
     *
     * @param list
     * @return
     */
    public Object[] buildParameters(List<String> list) {
        Object[] dest = new Object[list.size() - 2];
        if (list.size() > 3) {
            System.arraycopy(list.toArray(), 3, dest, 0, list.size() - 3);
        }
        return dest;
    }
}
