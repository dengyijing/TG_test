package com.example.testnet;

import android.os.Environment;

import java.io.File;

public class Util {

    public static String getDownloadCachePath() {

        String appCachePath = null;

        if (checkSDCard()) {
            appCachePath = Environment.getExternalStorageDirectory() + "/PikaDownload";
        } else {
            appCachePath = Environment.getDataDirectory().getPath() + "/PikaDownload";
        }
        String path = appCachePath.replaceFirst("/", "");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static boolean checkSDCard() {
        return  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
