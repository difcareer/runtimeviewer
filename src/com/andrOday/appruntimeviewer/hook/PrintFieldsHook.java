package com.andrOday.appruntimeviewer.hook;

import com.alibaba.fastjson.JSON;
import com.andrOday.appruntimeviewer.util.LogUtil;
import de.robv.android.xposed.XC_MethodHook;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by andr0day on 2015/1/12.
 */
public class PrintFieldsHook extends XC_MethodHook {

    public List<String> fields = null;


    protected void beforeHookedMethod(MethodHookParam param) {
        Object object = param.thisObject;
        StringBuilder sb = new StringBuilder();
        sb.append("fields:\n");
        int count = 0;
        if (object != null) {
            Class clazz = object.getClass();
            while (clazz != null) {
                Field[] f_ds = clazz.getDeclaredFields();
                for (Field it : f_ds) {
                    it.setAccessible(true);
                    for (String target : fields) {
                        String f_name = it.getName();
                        if (target.equals(f_name)) {
                            try {
                                sb.append(target + "=" + JSON.toJSONString(it.get(object)) + "\n");
                            } catch (Exception e) {
                                //json exception
                            }
                            count++;
                        }
                    }
                    if (count == fields.size()) {
                        break;
                    }
                }
                //向上查找
                clazz = clazz.getSuperclass();
            }
        }
        sb.append("\n");
        LogUtil.log(sb.toString());
    }
}
