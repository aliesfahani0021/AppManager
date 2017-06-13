package com.pooyesh.appmanager.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import com.pooyesh.appmanager.AppInfo;
import com.pooyesh.appmanager.StarterApplication;

public class UtilsApplication {
    public static File getDefaultAppFolder() {
        return new File(Environment.getExternalStorageDirectory() + "/AppManager");
    }

    public static File getAppFolders() {

        AppPreferences appPreferences = new AppPreferences(StarterApplication.context);

        return new File(appPreferences.getCustomPathOfApks());


    }

    public static Boolean copyFilesToAplace(AppInfo appInfo) {
        Boolean res = false;

        File initialFile = new File(appInfo.getSource());
        File finalFile = getOutputFilenames(appInfo);

        try {
            FileUtils.copyFile(initialFile, finalFile);
            res = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String getAPKFilenames(AppInfo appInfo) {
        AppPreferences appPreferences = StarterApplication.getAppPreferences();
        String res;

        switch (appPreferences.getCustomFilenameOfApks()) {
            case "1":
                res = appInfo.getAPK() + "_" + appInfo.getVersion();
                break;
            case "2":
                res = appInfo.getName() + "_" + appInfo.getVersion();
                break;
            case "4":
                res = appInfo.getName();
                break;
            default:
                res = appInfo.getAPK();
                break;
        }

        return res;
    }

    public static File getOutputFilenames(AppInfo appInfo) {
        return new File(getAppFolders().getPath() + "/" + getAPKFilenames(appInfo) + ".apk");
    }

    public static Intent getShareIntent(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

}
