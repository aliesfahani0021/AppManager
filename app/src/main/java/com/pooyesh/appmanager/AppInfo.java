package com.pooyesh.appmanager;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private String name;
    private String apk;
    private String version;
    private String source;
    private String data;
    private String size;
    private String InstallationDate;
    private Drawable icon;
    private Boolean system;

    public AppInfo(String name, String apk, String version, String source, String data, String size, String InstallationDate, Drawable icon, Boolean isSystem) {
        this.name = name;
        this.apk = apk;
        this.version = version;
        this.source = source;
        this.data = data;
        this.size = size;
        this.InstallationDate = InstallationDate;
        this.icon = icon;
        this.system = isSystem;
    }

    public String getName() {
        return name;
    }

    public String getAPK() {
        return apk;
    }

    public String getVersion() {
        return version;
    }

    public String getSource() {
        return source;
    }

    public String getData() {
        return data;
    }

    public String getSize() {
        return size;
    }

    public String getInstallationDate() {
        return InstallationDate;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Boolean isSystem() {
        return system;
    }

    public String toString() {
        return getName() + "##" + getAPK() + "##" + getVersion() + "##" + getSource() + "##" + getData() + "##" + getSize() + "##" + getInstallationDate() +"##" + isSystem();
    }

}
