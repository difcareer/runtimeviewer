package com.andrOday.appruntimeviewer.util;

import android.os.Handler;
import android.os.HandlerThread;
import com.andrOday.appruntimeviewer.RunTimeViewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConfigUtil {

    public static final String CONFIG = "runtime.cfg";

    public static List<List<String>> methods = new ArrayList<List<String>>();

    public static long LAST_PARSED_TIME = 0;

    static {
        HandlerThread handlerThread = new HandlerThread("checker");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(new Checker(), 10 * 1000);
    }

    public static boolean hasConfig() {
        File configFile = getConfigFile();
        return configFile != null && configFile.exists();
    }

    /**
     * may be null
     *
     * @return
     */
    public static File getConfigFile() {
        if (RunTimeViewer.targetApplication != null) {
            File dir = RunTimeViewer.targetApplication.getFilesDir();
            return new File(dir, CONFIG);
        }
        return null;
    }

    public static void parse() {
        if (hasConfig()) {
            File file = getConfigFile();
            LAST_PARSED_TIME = file.lastModified();
            methods.clear();
            LogUtil.log("~~~~parse config file start~~~~");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line = br.readLine();
                while (line != null && !line.trim().equals("")) {
                    String[] parts = line.trim().split(" ");
                    if (parts.length < 3) {
                        LogUtil.log("config error,at least 3 parts:" + line);
                    } else {
                        putIfNotExist(methods, Arrays.asList(parts));
                        LogUtil.log("parse method:" + Arrays.asList(parts));
                        line = br.readLine();
                    }
                }
            } catch (Exception e) {
                LogUtil.log("parse config file error:");
                e.printStackTrace(LogUtil.logWriter);
            }
            LogUtil.log("~~~~parse config file end~~~~");
        }
    }

    public static void putIfNotExist(List<List<String>> lists, List<String> target) {
        boolean exist = false;
        String joinTarget = CollectionUtil.join(target, ",");
        for (List<String> it : lists) {
            String joinIt = CollectionUtil.join(it, ",");
            if (joinIt.equals(joinTarget)) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            lists.add(target);
        }
    }

    static class Checker implements Runnable {

        @Override
        public void run() {
            LogUtil.log("start loop checker");
            while (true) {
                File configFile = getConfigFile();
                if (configFile != null) {
                    long modified = configFile.lastModified();
                    if (LAST_PARSED_TIME == 0L || LAST_PARSED_TIME != modified) {
                        parse();
                    }
                }
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {

                }
            }
        }
    }

}
