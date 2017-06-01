package home.smart.thuans.centraldevice.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import home.smart.thuans.centraldevice.MainActivity;
import home.smart.thuans.centraldevice.artificialBot.IntentConstant;
import home.smart.thuans.centraldevice.device.ConnectedDevice;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Thuans on 4/28/2017.
 */

public class RetroArduinoSigleton {
    public static final String COMMAND_RESULT_MESSAGE = "retro arduino send command result";
    public static final String COMMAND_RESULT_TYPE = "command result type";
    private static final String TAG = "RetroArduinoSigleton";
    public static final String RESULT_VALUE = "result value from device arduino";

    private Retrofit retrofit;
    private boolean isControlled;
    public static String hostBaseUrl = "http://192.168.137.111:8080";
    private CloudApi cloudApi;
    private static volatile RetroArduinoSigleton instance = null;
    private LocalBroadcastManager broadcaster;

    private RetroArduinoSigleton() { }
    private static OkHttpClient getRequestHeader() {
        ConnectionPool connectionPool = new ConnectionPool(5,30,TimeUnit.SECONDS);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectionPool(connectionPool)
                .build();
        return httpClient;
    }
    public static RetroArduinoSigleton getInstance() {
        if(instance == null) {
            synchronized(RetroArduinoSigleton.class) {
                if(instance == null) {
                    instance= new RetroArduinoSigleton();
                    instance.retrofit = new Retrofit.Builder()
                            .baseUrl(hostBaseUrl)
                            .client(getRequestHeader())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    instance.cloudApi = instance.retrofit.create(CloudApi.class);
                    instance.isControlled = false;
                }
            }

        }
        return instance;
    }

    public void setControlled(boolean controlled) {
        isControlled = controlled;
    }

    public boolean isControlled() {
        return isControlled;
    }

    public CloudApi getCloudApi() {
        return cloudApi;
    }

    public void turnObjectOn(ConnectedDevice device, final Context context) {
        cloudApi = retrofit.create(CloudApi.class);
        Call<ResponseBody> sendCmd = cloudApi.sendCommand(device.getPort(),"on");
        final ProgressDialog mWaitDialog =new ProgressDialog(context);
        mWaitDialog .setMessage("Thực hiện lệnh....");
        sendCmd.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mWaitDialog.dismiss();
                Log.d(TAG,"turn on bell success -----send command");
                sendCommandResult(context, IntentConstant.SUCCESS_REPLY,"on");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mWaitDialog.dismiss();
//                sendCommandResult(context, IntentConstant.FAIL_REPLY,null);
//                sendCommandResult(context, IntentConstant.FAIL_REPLY,"on");

                Log.d(TAG,"turn on bell fail " + t.getMessage() + " " + t.getCause() + " " + t.getLocalizedMessage());

            }
        });
        Log.d(TAG,sendCmd.request().url().toString()+"-----send command");
        mWaitDialog .show();

    }

    private void sendCommandResult(Context context, String result, String resultValue){

        instance.broadcaster = LocalBroadcastManager.getInstance(context);
        Intent broadcastIntent = new Intent(MainActivity.MAIN_FILTER_RECEIVER);
        broadcastIntent.putExtra(MainActivity.MESSAGE_BROADCAST_SOURCE,COMMAND_RESULT_MESSAGE);
        broadcastIntent.putExtra(COMMAND_RESULT_TYPE,result);
        broadcastIntent.putExtra(RESULT_VALUE,resultValue);
        Log.d(TAG, result);
        broadcaster.sendBroadcast(broadcastIntent);
    }

    public void turnObjectOff(ConnectedDevice device,final Context context) {
        Call<ResponseBody> sendCmd = cloudApi.sendCommand(device.getPort(),"off");
        final ProgressDialog mWaitDialog =new ProgressDialog(context);
        mWaitDialog .setMessage("Thực hiện lệnh....");
        sendCmd.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mWaitDialog.dismiss();
                sendCommandResult(context, IntentConstant.SUCCESS_REPLY,"off");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mWaitDialog.dismiss();
//                sendCommandResult(context, IntentConstant.FAIL_REPLY,null);
                sendCommandResult(context, IntentConstant.SUCCESS_REPLY,"off");
            }
        });
        Log.d(TAG,sendCmd.request().url().toString()+"-----send command");
        mWaitDialog .show();
    }
}
