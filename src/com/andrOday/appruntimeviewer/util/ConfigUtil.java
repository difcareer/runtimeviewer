package com.andrOday.appruntimeviewer.util;

import android.os.Handler;
import android.os.HandlerThread;
import com.alibaba.fastjson.JSON;
import com.andrOday.appruntimeviewer.RunTimeViewer;
import com.andrOday.appruntimeviewer.data.ProClass;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ConfigUtil {

    public static final String CONFIG = "config.json";

    public static List<ProClass> proClasses = new ArrayList<ProClass>();

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
            proClasses.clear();
            LogUtil.event_log("~~~~parse config file start~~~~");
            try {
                String content = FileUtils.readFileToString(file);
                proClasses = JSON.parseArray(content, ProClass.class);
            } catch (Exception e) {
                LogUtil.event_log("parse config file error:");
                e.printStackTrace(LogUtil.eventWriter);
            }
            LogUtil.event_log("~~~~parse config file end~~~~");
        }
    }

    static class Checker implements Runnable {

        @Override
        public void run() {
            LogUtil.event_log("start loop checker");
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
