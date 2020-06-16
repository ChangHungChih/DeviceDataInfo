package tw.sean.devicedatainfo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import tw.sean.devicedatainfo.adapter.AppInfoAdapter;
import tw.sean.devicedatainfo.model.AppInfo;
import tw.sean.devicedatainfo.repository.DeviceData;
import tw.sean.devicedatainfo.util.FormatUtil;
import tw.sean.devicedatainfo.webrtc.ConnectActivity;

public class MainActivity extends AppCompatActivity implements LocationListener, SensorEventListener {
    private RecyclerView rvData;
    private ScrollView scrollView;
    private TextView tvInfo;
    private LocationManager locationManager;
    private SensorManager sensorManager;

    // List of mandatory application permissions.
    private static final String[] MANDATORY_PERMISSIONS = {
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        checkPermission();
    }

    private void findViews() {
        rvData = findViewById(R.id.rvData);
        scrollView = findViewById(R.id.scrollView);
        tvInfo = findViewById(R.id.tvInfo);
    }

    private void resetToDefault() {
        rvData.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        tvInfo.setText("");
        locationManager.removeUpdates(this);
        sensorManager.unregisterListener(this);
    }

    private void checkPermission() {
        for (String permission : MANDATORY_PERMISSIONS) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, MANDATORY_PERMISSIONS, 200);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 200 && grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    String msg = permissions[i] + " not granted";
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void listApk(View view) {
        resetToDefault();
        List<AppInfo> appInfoList = DeviceData.getAllApkData(this);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter(appInfoList);
        rvData.setAdapter(appInfoAdapter);
        rvData.setVisibility(View.VISIBLE);
    }

    public void showDeviceInfo(View view) {
        resetToDefault();
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

    public void getGpsInfo(View view) {
        resetToDefault();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates
                    (LocationManager.GPS_PROVIDER, 5000, 0, this);
        } else {
            Toast.makeText(this, "No Location Permission", Toast.LENGTH_LONG).show();
        }
        scrollView.setVisibility(View.VISIBLE);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onLocationChanged(Location location) {
        StringBuilder info = new StringBuilder();
        info.append("Location Provider:").append(location.getProvider()).append("\n");
        info.append(String.format("Latitude:%.6f\nLongitude:%.6f\nAltitude:%.2fMeters\n\n",
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude()));
        tvInfo.append(info);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void getGyroscopeInfo(View view) {
        resetToDefault();
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder info = new StringBuilder();
        info.append("GYROSCOPE Info\n");
        info.append("X-axis:").append(event.values[0]).append("\n");
        info.append("Y-axis:").append(event.values[1]).append("\n");
        info.append("Z-axis:").append(event.values[2]).append("\n");
        tvInfo.setText(info);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void toWebRtcSample(View view) {
        Intent intent = new Intent(this, ConnectActivity.class);
        startActivity(intent);
    }
}
