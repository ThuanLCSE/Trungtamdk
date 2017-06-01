package home.smart.thuans.centraldevice.house;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import home.smart.thuans.centraldevice.MainActivity;
import home.smart.thuans.centraldevice.R;
import home.smart.thuans.centraldevice.artificialBot.DetectIntent;
import home.smart.thuans.centraldevice.artificialBot.Term;
import home.smart.thuans.centraldevice.artificialBot.TermSQLite;
import home.smart.thuans.centraldevice.artificialBot.TermTarget;
import home.smart.thuans.centraldevice.artificialBot.TermTargetSQLite;
import home.smart.thuans.centraldevice.device.ConnectedDevice;
import home.smart.thuans.centraldevice.device.DeviceSQLite;
import home.smart.thuans.centraldevice.http.CloudApi;
import home.smart.thuans.centraldevice.http.pojoDto.Customer;
import home.smart.thuans.centraldevice.http.pojoDto.SmartHouse;
import home.smart.thuans.centraldevice.utils.BotUtils;
import home.smart.thuans.centraldevice.utils.DeviceConstant;
import home.smart.thuans.centraldevice.utils.RetroFitSingleton;
import home.smart.thuans.centraldevice.utils.SharedPrefConstant;
import home.smart.thuans.centraldevice.utils.TFIDF;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    private static final String TAG = "MainActivity";
    TextView txtResult;
    EditText username;
    EditText password;
    ProgressDialog mDownloadDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layou);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        txtResult = (TextView) findViewById(R.id.txt_Result);
        username = (EditText) findViewById(R.id.edt_username);
        password = (EditText) findViewById(R.id.edt_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RetroFitSingleton retro = RetroFitSingleton.getInstance();
                CloudApi cloudApi = retro.getCloudApi();

                Customer customer = new Customer();
                customer.setUsername(username.getText().toString());
                customer.setPassword(password.getText().toString());
                Call loginApi = cloudApi.mobileLogin(customer);
                loginApi.enqueue(new Callback<SmartHouse>() {
                    @Override
                    public void onResponse(Call<SmartHouse> call, Response<SmartHouse> response) {

                        SmartHouse houseResult = response.body();
                        if (houseResult != null){
                            HouseConfig houseConfig = HouseConfig.getInstance();
                            houseConfig.setTokenKey(houseResult.getTokenKey());
                            Log.d(TAG,houseResult.getTokenKey());
                            //hardcode arduino for easy demo
                            houseConfig.setArduinoAddress("http://192.168.137.111:8080");

                            DeviceSQLite deviceSQLite= new DeviceSQLite();
                            deviceSQLite.delete();

                            for (ConnectedDevice connectedDevice : houseResult.getDevices()){
                                connectedDevice.setValue(ConnectedDevice.DEFAULT_VALUE);
                                connectedDevice.setState(ConnectedDevice.STATE_OFF);
                                connectedDevice.addSecondaryName(connectedDevice.getName());
                                deviceSQLite.insert(connectedDevice);
                            }
                            saveDeviceTFIDFTerm(houseResult.getDevices());

                            txtResult.setText("Tải dữ liệu thành công "+ houseConfig.getTokenKey());
                        } else{
                            txtResult.setText("Đăng nhập thất bại");
                        }

                        mDownloadDialog.dismiss();
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        Toast.makeText(getApplicationContext(),"House hang ve "+ houseResult.getId(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        call.cancel();
                        mDownloadDialog.dismiss();
                        txtResult.setText("Đăng nhập thất bại");
                    }
                });
                mDownloadDialog = new ProgressDialog(LoginActivity.this);
                mDownloadDialog.setMessage("Đăng nhập lần đầu....");
                mDownloadDialog.show();
            }
        });


    }

    private void saveDeviceTFIDFTerm(List<ConnectedDevice> listDevices) {
        Map<String,Map<String,Integer>> trainingSetMap = BotUtils.readDeviceNametoHashMap(listDevices);
        Map<String,Map<String,Integer>> cloneForCalculate = new HashMap<>(trainingSetMap);

        TermTargetSQLite termTargetSQLite= new TermTargetSQLite();
        termTargetSQLite.delete();

        Iterator it = trainingSetMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            String deviceId = (String) pair.getKey();
            Map<String, Integer> wordCount = (Map<String, Integer>) pair.getValue();

            Iterator wit = wordCount.entrySet().iterator();
            while (wit.hasNext()){
                Map.Entry termPair = (Map.Entry) wit.next();
                String term = (String) termPair.getKey();

                float termTfidf = TFIDF.createTfIdf(cloneForCalculate, term, deviceId);
                System.out.println(termTfidf+"   "+term+"   "+deviceId);

                TermTarget saveTerm = new TermTarget();
                saveTerm.setTargetId(deviceId);
                saveTerm.setTargetType(DeviceConstant.DEVICE_TYPE);
                saveTerm.setTfidfPoint(termTfidf);
                saveTerm.setContent(" "+term+" ");
                termTargetSQLite.insert(saveTerm);
            }
        }
    }

    @Override
    protected void onStop() {
        SharedPreferences preferences = getSharedPreferences(SharedPrefConstant.SMART_HOUSE_SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

        HouseConfig houseConfig = HouseConfig.getInstance();
        edit.putString(SharedPrefConstant.TOKEN_KEY,houseConfig.getTokenKey());
        edit.commit();
        Log.d(TAG,"Saved Token "+houseConfig.getTokenKey());
        super.onStop();
    }
}
