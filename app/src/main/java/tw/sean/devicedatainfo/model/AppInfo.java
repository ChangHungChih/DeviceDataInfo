package tw.sean.devicedatainfo.model;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private Drawable appIcon;
    private String appName;
    private double appSize;
    private boolean isSystem = false;
    private String appPackageName;
    private String sourceDir;
    private String apkPath;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public double getAppSize() {
        return appSize;
    }

    public void setAppSize(double appSize) {
        this.appSize = appSize;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }
}
