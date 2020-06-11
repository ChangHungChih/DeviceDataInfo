package tw.sean.devicedatainfo;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tw.sean.devicedatainfo.adapter.AppInfoAdapter;
import tw.sean.devicedatainfo.model.AppInfo;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }

    private void findViews() {
        rvData = findViewById(R.id.rvData);
    }

    public void listApk(View view) {
        List<AppInfo> appInfoList = getAllApkData();
        rvData.setLayoutManager(new LinearLayoutManager(this));
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter(appInfoList);
        rvData.setAdapter(appInfoAdapter);
    }

    private List<AppInfo> getAllApkData() {
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        List<AppInfo> appInfoList = new ArrayList<>();
        for (PackageInfo p : list) {
            AppInfo appInfo = new AppInfo();
            appInfo.setAppIcon(p.applicationInfo.loadIcon(packageManager));
            appInfo.setAppName(packageManager.getApplicationLabel(p.applicationInfo).toString());
            appInfo.setAppPackageName(p.applicationInfo.packageName);
            appInfo.setApkPath(p.applicationInfo.sourceDir);
            File file = new File(p.applicationInfo.sourceDir);
            appInfo.setAppSize((double) file.length());
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
