package tw.sean.devicedatainfo;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import tw.sean.devicedatainfo.adapter.AppInfoAdapter;
import tw.sean.devicedatainfo.model.AppInfo;
import tw.sean.devicedatainfo.repository.DeviceData;

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
        List<AppInfo> appInfoList = DeviceData.getAllApkData(this);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter(appInfoList);
        rvData.setAdapter(appInfoAdapter);
    }

}
