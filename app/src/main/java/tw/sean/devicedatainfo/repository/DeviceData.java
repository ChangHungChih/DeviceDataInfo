package tw.sean.devicedatainfo.repository;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tw.sean.devicedatainfo.model.AppInfo;

public class DeviceData {

    public static List<AppInfo> getAllApkData(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        List<AppInfo> appInfoList = new ArrayList<>();
        for (PackageInfo p : list) {
            AppInfo appInfo = new AppInfo();
            appInfo.setAppIcon(p.applicationInfo.loadIcon(packageManager));
            appInfo.setAppName(packageManager.getApplicationLabel(p.applicationInfo).toString());
            appInfo.setAppPackageName(p.applicationInfo.packageName);
            appInfo.setApkPath(p.applicationInfo.sourceDir);
            File file = new File(p.applicationInfo.sourceDir);
            appInfo.setAppSize(file.length());
            appInfo.setSourceDir(p.applicationInfo.sourceDir);
            int flags = p.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                appInfo.setSystem(true);
            }
            appInfoList.add(appInfo);
        }

        return appInfoList;
    }

}
