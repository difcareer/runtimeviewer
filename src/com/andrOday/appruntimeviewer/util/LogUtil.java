package com.andrOday.appruntimeviewer.util;

import com.andrOday.appruntimeviewer.RunTimeViewer;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;


public class LogUtil {

    public static final String event_log = "event.log";

    public static final String info_log = "info.log";

    public static PrintWriter eventWriter = null;
    public static PrintWriter infoWriter = null;

    private static final int MAX_LOGFILE_SIZE = 20 * 1024;

    public static void init() {
        try {
            if (RunTimeViewer.targetApplication != null && eventWriter == null) {
                checkFileSize(event_log);
                File logFile = new File(RunTimeViewer.targetApplication.getFilesDir(), event_log);
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
                eventWriter = new PrintWriter(new FileWriter(logFile, true));
                logFile.setReadable(true, false);
                logFile.setWritable(true, false);
            }
        } catch (Exception e) {
            LogUtil.event_log("init event file error:");
            e.printStackTrace(LogUtil.eventWriter);
        }

        try {
            if (RunTimeViewer.targetApplication != null && infoWriter == null) {
                checkFileSize(info_log);
                File logFile = new File(RunTimeViewer.targetApplication.getFilesDir(), info_log);
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
                infoWriter = new PrintWriter(new FileWriter(logFile, true));
                logFile.setReadable(true, false);
                logFile.setWritable(true, false);
            }
        } catch (Exception e) {
            LogUtil.event_log("init event file error:");
            e.printStackTrace(LogUtil.eventWriter);
        }
    }

    public static void log(String message, String type) {
        if (RunTimeViewer.targetApplication != null) {
            if ("event".equals(type)) {
                try {
                    if (eventWriter != null) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                        String dateStr = simpleDateFormat.format(System.currentTimeMillis());
                        eventWriter.println(dateStr + " " + message);
                        eventWriter.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace(LogUtil.eventWriter);
                }
            } else {
                try {
                    if (infoWriter != null) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                        String dateStr = simpleDateFormat.format(System.currentTimeMillis());
                        infoWriter.println(dateStr + " " + message);
                        infoWriter.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace(LogUtil.eventWriter);
                }
            }
        }
    }

    public static void event_log(String message) {
        log(message, "event");
    }

    public static void info_log(String message) {
        log(message, "info");
    }

    /**
     * 打印堆栈
     */
    public static void printStack() {
        new Throwable().printStackTrace(infoWriter);
    }


    private static void checkFileSize(String file) {
        File logFile = new File(RunTimeViewer.targetApplication.getFilesDir(), file);
        if (logFile.exists() && logFile.length() > MAX_LOGFILE_SIZE) {
            logFile.renameTo(new File(RunTimeViewer.targetApplication.getFilesDir(), file + "_" + System.currentTimeMillis()));
        }
    }
}
