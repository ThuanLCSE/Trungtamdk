package home.smart.thuans.centraldevice.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import home.smart.thuans.centraldevice.R;

/**
 * Created by Sam on 4/13/2017.
 */

public class DeviceListHolder extends RecyclerView.ViewHolder{
    private static final String TAG = "DeviceListHolder";
    public TextView name;
    public TextView value;

    public DeviceListHolder(View rowView) {
        super(rowView);
        name = (TextView) rowView.findViewById(R.id.device_name);
        value= (TextView) rowView.findViewById(R.id.device_value);

    }
}
