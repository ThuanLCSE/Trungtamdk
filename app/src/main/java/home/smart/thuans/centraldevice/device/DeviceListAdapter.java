package home.smart.thuans.centraldevice.device;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import home.smart.thuans.centraldevice.R;
import home.smart.thuans.centraldevice.utils.DeviceConstant;

/**
 * Created by Thuans on 4/7/2017.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListHolder> {
    private static final String TAG = "DeviceListAdapter";
    private List<ConnectedDevice> deviceModelList = new ArrayList<ConnectedDevice>();
    private TextView twValue;

    public DeviceListAdapter(List<ConnectedDevice> deviceModelList) {
        this.deviceModelList = deviceModelList;
    }

    @Override
    public DeviceListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.devicestatuslist, parent, false);
        DeviceListHolder holder = new DeviceListHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(DeviceListHolder holder, int position) {
        holder.name.setText(deviceModelList.get(position).getName());
        if (deviceModelList.get(position).getType().equals(DeviceConstant.SENSOR_TYPE)) {
            holder.value.setText(deviceModelList.get(position).getValue());
            holder.value.setBackgroundResource(R.drawable.ovaltextview);
        } else
        if (deviceModelList.get(position).getType().equals(DeviceConstant.DEVICE_TYPE)) {
            holder.value.setText(deviceModelList.get(position).getState());
            holder.value.setBackgroundResource(R.drawable.ovaltextview3);
            if (deviceModelList.get(position).getState().equals("off")) {
                holder.value.setBackgroundResource(R.drawable.ovaltextview4);
            }
        }

    }

    @Override
    public int getItemCount() {
        return deviceModelList.size();
    }

    public long getItemId(int position) {
        return position;
    }
}
