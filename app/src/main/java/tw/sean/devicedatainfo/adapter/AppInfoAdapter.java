package tw.sean.devicedatainfo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.sean.devicedatainfo.R;
import tw.sean.devicedatainfo.model.AppInfo;
import tw.sean.devicedatainfo.util.FormatUtil;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.ViewHolder> {
    private List<AppInfo> appInfoList;

    public AppInfoAdapter(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }

    @NonNull
    @Override
    public AppInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_apps, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppInfoAdapter.ViewHolder holder, int position) {
        AppInfo appInfo = appInfoList.get(position);
        holder.ivAppIcon.setImageDrawable(appInfo.getAppIcon());
        holder.tvAppName.setText(appInfo.getAppName());
        holder.tvAppSize.setText(FormatUtil.formatSize(appInfo.getAppSize()));
        holder.tvAppPackage.setText(appInfo.getAppPackageName());
    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAppIcon;
        TextView tvAppName;
        TextView tvAppPackage;
        TextView tvAppSize;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAppIcon = itemView.findViewById(R.id.ivAppIcon);
            tvAppName = itemView.findViewById(R.id.tvAppName);
            tvAppPackage = itemView.findViewById(R.id.tvAppPackage);
            tvAppSize = itemView.findViewById(R.id.tvAppSize);
        }
    }
}
