package com.andrOday.appruntimeviewer.util;

import android.util.Log;
import com.andrOday.appruntimeviewer.RunTimeViewer;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;


public class LogUtil {

    public static final String TAG = "RuntimeViewer";

    public static final String LOG_FILE = "viewer.log";

    public static PrintWriter logWriter = null;

    private static final int MAX_LOGFILE_SIZE = 20 * 1024;

    public static void init() {
        try {
            if (RunTimeViewer.targetApplication != null && logWriter == null) {
                checkFileSize();
                File logFile = new File(RunTimeViewer.targetApplication.getFilesDir(), LOG_FILE);
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
                logWriter = new PrintWriter(new FileWriter(logFile, true));
                logFile.setReadable(true, false);
                logFile.setWritable(true, false);
            }
        } catch (Exception e) {
            LogUtil.log("init log file error:");
            e.printStackTrace(LogUtil.logWriter);
        }
    }

    public static void log(String message) {
        if (RunTimeViewer.targetApplication != null) {
            try {
                if (logWriter != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                    String dateStr = simpleDateFormat.format(System.currentTimeMillis());
                    logWriter.println(dateStr + " " + message);
                    logWriter.flush();
                }
            } catch (Exception e) {
                Log.e(TAG, "write to log file error", e);
            }
        }
    }

    /**
     * 打印堆栈
     */
    public static void printStack() {
        log("stack info:");
        new Throwable().printStackTrace(logWriter);
    }


    private static void checkFileSize() {
        File logFile = new File(RunTimeViewer.targetApplication.getFilesDir(), LOG_FILE);
        if (logFile.exists() && logFile.length() > MAX_LOGFILE_SIZE) {
            logFile.renameTo(new File(RunTimeViewer.targetApplication.getFilesDir(), LOG_FILE + "_" + System.currentTimeMillis()));
        }
    }
}
