package tw.sean.devicedatainfo.repository;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.sean.devicedatainfo.model.AppInfo;

import static android.content.Context.ACTIVITY_SERVICE;

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

    public static Map<String, Long> getStorageInfo() {
        Map<String, Long> data = new HashMap<>();
        File path = Environment.getDataDirectory();
        StatFs statFs = new StatFs(path.getPath());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            data.put("total", statFs.getTotalBytes());
            data.put("available", statFs.getAvailableBytes());
        } else {
            int blockSize = statFs.getBlockSize();
            long blockCount = statFs.getBlockCount();
            long availBlockCount = statFs.getAvailableBlocks();
            data.put("total", blockSize * blockCount);
            data.put("available", blockSize * availBlockCount);
        }

        return data;

    }

    public static Map<String, Long> getMemoryInfo(Context context) {
        ActivityManager actManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        Map<String, Long> data = new HashMap<>();
        if (actManager != null) {
            actManager.getMemoryInfo(memInfo);
        }
        data.put("total", memInfo.totalMem);
        data.put("available", memInfo.availMem);

        return data;
    }

    public static String getMemoryDetailInfo() {
        ProcessBuilder cmd;
        StringBuilder result = new StringBuilder();

        try {
            String[] args = {"/system/bin/cat", "/proc/meminfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while (in.read(re) != -1) {
                result.append(new String(re));
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String getCpuInfo() {
        ProcessBuilder cmd;
        StringBuilder result = new StringBuilder();

        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while (in.read(re) != -1) {
                result.append(new String(re));
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }

    public static Map<String, String> getWifiAndMobileInfo(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfoArray;
        HashMap<String, String> map = new HashMap<>();
        if (connectivityManager != null) {
            networkInfoArray = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo networkInfo : networkInfoArray) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    map.put("wifi", networkInfo.toString());
                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    map.put("mobile", networkInfo.toString());
                }
            }
        }
        return map;
    }

    public static String[] getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = null;
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
        }
        String wifiString = null;
        if (wifiInfo != null) {
            wifiString = wifiInfo.toString();
        } else {
            wifiString = "";
        }
        return wifiString.split(",");
    }

}
