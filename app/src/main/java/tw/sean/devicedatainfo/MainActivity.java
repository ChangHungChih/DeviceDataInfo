package tw.sean.devicedatainfo;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import tw.sean.devicedatainfo.adapter.AppInfoAdapter;
import tw.sean.devicedatainfo.model.AppInfo;
import tw.sean.devicedatainfo.repository.DeviceData;
import tw.sean.devicedatainfo.util.FormatUtil;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvData;
    private ScrollView scrollView;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }

    private void findViews() {
        rvData = findViewById(R.id.rvData);
        scrollView = findViewById(R.id.scrollView);
        tvInfo = findViewById(R.id.tvInfo);
    }

    public void listApk(View view) {
        List<AppInfo> appInfoList = DeviceData.getAllApkData(this);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter(appInfoList);
        rvData.setAdapter(appInfoAdapter);
        rvData.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
    }

    public void showDeviceInfo(View view) {
        rvData.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        String information = getDeviceInfoString();
        tvInfo.setText(information);
    }

    private String getDeviceInfoString() {
        Map<String, Long> storageData = DeviceData.getStorageInfo();
        StringBuilder allInfo = new StringBuilder();
        long totalStorageByte = storageData.get("total");
        long availableStorageByte = storageData.get("available");
        allInfo.append("***** Storage Information *****").append("\n");
        allInfo.append("Total Storage:").append(FormatUtil.formatSize(totalStorageByte)).append("\n");
        allInfo.append("Available Storage:").append(FormatUtil.formatSize(availableStorageByte)).append("\n");

        Map<String, Long> memoryData = DeviceData.getMemoryInfo(this);
        long totalMemoryByte = memoryData.get("total");
        long availableMemoryByte = memoryData.get("available");
        allInfo.append("\n").append("***** Memory Information *****").append("\n");
        allInfo.append("Total Memory:").append(FormatUtil.formatSize(totalMemoryByte)).append("\n");
        allInfo.append("Available Memory:").append(FormatUtil.formatSize(availableMemoryByte)).append("\n");

        String cpuInfo = DeviceData.getCpuInfo();
        allInfo.append("\n").append("***** CPU Information *****").append("\n");
        allInfo.append(cpuInfo).append("\n");

        Map<String, String> wifiAndMobileData = DeviceData.getWifiAndMobileInfo(this);
        allInfo.append("\n").append("***** WiFi & Mobile data Information *****").append("\n");
        allInfo.append("WiFi:").append(wifiAndMobileData.get("wifi")).append("\n");
        allInfo.append("Mobile:").append(wifiAndMobileData.get("mobile")).append("\n");
        allInfo.append("\n").append("\n").append("\n");
        return allInfo.toString();
    }

}
