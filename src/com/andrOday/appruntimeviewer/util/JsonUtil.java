package com.andrOday.appruntimeviewer.util;

import com.alibaba.fastjson.JSON;

/**
 * Created by andr0day on 2015/1/12.
 */
public class JsonUtil {

    public static String toJSONString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            return "json error";
        }
    }
}
