package home.smart.thuans.centraldevice.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import home.smart.thuans.centraldevice.MainActivity;
import home.smart.thuans.centraldevice.R;
import home.smart.thuans.centraldevice.device.ConnectedDevice;
import home.smart.thuans.centraldevice.device.DeviceListAdapter;
import home.smart.thuans.centraldevice.device.DeviceSQLite;
import home.smart.thuans.centraldevice.house.HouseConfig;
import home.smart.thuans.centraldevice.http.CloudApi;
import home.smart.thuans.centraldevice.http.RetroArduinoSigleton;
import home.smart.thuans.centraldevice.utils.DeviceConstant;
import home.smart.thuans.centraldevice.utils.RetroFitSingleton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thuans on 4/28/2017.
 */

public class CheckSensorService extends Service {
    private static final String TAG = "Check Sensor Service";
    public static final String SENSOR_RESULT_MESSAGE = "Check sensor result message";
    private static Timer delayShootTime;
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
        final HouseConfig houseConfig = HouseConfig.getInstance();
        if (delayShootTime != null){
            delayShootTime.cancel();
            delayShootTime.purge();
        }
        delayShootTime = new Timer();
        delayShootTime.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                RetroArduinoSigleton retro = RetroArduinoSigleton.getInstance();
                CloudApi cloudApi = retro.getCloudApi();

                Call<ResponseBody> result = cloudApi.cbeckArduinoConnect();
                result.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            String[] attrib = result.split(",");
                            for (int i=0; i < attrib.length; i++){
                                String[] element = attrib[i].split(":");
                                if (element.length>1) {
                                    houseConfig.changeValueByPort(element[0], element[1]);
                                }
                            }
                            sendBroadCastToMain();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(CheckSensorService.this.getApplicationContext(),"Khong ket noi duoc",Toast.LENGTH_SHORT);
                    }
                });
                Log.d(TAG,result.request().url().toString()+"  ----- send check");
            }
        }, 0, 4000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"on destroy");
        delayShootTime.cancel();
        delayShootTime.purge();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendBroadCastToMain(){
        Log.d(TAG,"sned data");
        Intent broadcastIntent = new Intent(MainActivity.MAIN_FILTER_RECEIVER);
        broadcastIntent.putExtra(MainActivity.MESSAGE_BROADCAST_SOURCE,SENSOR_RESULT_MESSAGE);
        broadcaster.sendBroadcast(broadcastIntent);
    }

}
