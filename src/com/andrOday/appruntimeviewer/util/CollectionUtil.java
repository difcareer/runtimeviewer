package com.andrOday.appruntimeviewer.util;

import java.util.Collection;

/**
 * Created by andr0day on 2015/1/6.
 */
public class CollectionUtil {

    public static String join(Collection<String> c, String sep) {
        StringBuilder sb = new StringBuilder();
        for (String str : c) {
            sb.append(str + sep);
        }
        return sb.toString();
    }
}
