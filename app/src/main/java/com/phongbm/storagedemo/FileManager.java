package com.phongbm.storagedemo;

import android.app.admin.DeviceAdminInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Telephony;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
    private static final String TAG = "FileManager";

    private String rootPath;

    public FileManager() {
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public void showAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        if (file.isFile()) {
            Log.d(TAG, "This is File! " + file.getName());
            return;
        }

        if (!file.isDirectory()) {
            Log.d(TAG, "This is not file and folder!");
            return;
        }

        File[] files = file.listFiles();
        for (File f : files) {
            Log.d(TAG, f.getName());
        }
    }

    public void writeData() {
        try {
            String path = rootPath + "/" + Environment.DIRECTORY_DOWNLOADS
                    + "/device_info.txt";
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);

            fos.write(String.valueOf(Build.VERSION.SDK_INT).getBytes());
            fos.write("\r\n".getBytes());
            fos.write(Build.DEVICE.getBytes());

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRootPath() {
        return rootPath;
    }

}