package com.pooyesh.appmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pooyesh.appmanager.StarterApplication;


public class AppPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    public static final String KEY_SORT_MODE = "KEY_SORT_MODE";
    public static final String PREF_CUSTOM_FILENAME = "PREF_CUSTOM_FILENAME";
    public static final String PREF_CUSTOM_PATH = "PREF_CUSTOM_PATH";

    public AppPreferences(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(StarterApplication.context);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public String getSortModeOfApks() {

        return sharedPreferences.getString(KEY_SORT_MODE, "1");
    }

    public void setSortModeOfApks(String res) {
        editor.putString(KEY_SORT_MODE, res);
        editor.commit();
    }
    public String getCustomPathOfApks() {
        return sharedPreferences.getString(PREF_CUSTOM_PATH, UtilsApplication.getDefaultAppFolder().getPath());
    }

    public void setCustomPathOfApks(String path) {
        editor.putString(PREF_CUSTOM_PATH, path);
        editor.commit();
    }

    public String getCustomFilenameOfApks() {
        return sharedPreferences.getString(PREF_CUSTOM_FILENAME, "1");
    }

}
