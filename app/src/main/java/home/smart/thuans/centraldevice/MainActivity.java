package home.smart.thuans.centraldevice;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.sonyericsson.extras.liveware.aef.notification.Notification;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.notification.NotificationUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import home.smart.thuans.centraldevice.artificialBot.DetectIntent;
import home.smart.thuans.centraldevice.artificialBot.IntentConstant;
import home.smart.thuans.centraldevice.artificialBot.IntentLearned;
import home.smart.thuans.centraldevice.artificialBot.Term;
import home.smart.thuans.centraldevice.artificialBot.TermSQLite;
import home.smart.thuans.centraldevice.artificialBot.TermTarget;
import home.smart.thuans.centraldevice.artificialBot.TermTargetSQLite;
import home.smart.thuans.centraldevice.device.DeviceListAdapter;
import home.smart.thuans.centraldevice.device.DeviceSQLite;
import home.smart.thuans.centraldevice.house.CurrentBotContext;
import home.smart.thuans.centraldevice.house.HouseConfig;
import home.smart.thuans.centraldevice.house.LoginActivity;
import home.smart.thuans.centraldevice.http.CloudApi;
import home.smart.thuans.centraldevice.http.RetroArduinoSigleton;
import home.smart.thuans.centraldevice.http.WebServer;
import home.smart.thuans.centraldevice.device.ConnectedDevice;
import home.smart.thuans.centraldevice.service.CameraService;
import home.smart.thuans.centraldevice.service.CheckSensorService;
import home.smart.thuans.centraldevice.utils.BotUtils;
import home.smart.thuans.centraldevice.utils.DeviceConstant;
import home.smart.thuans.centraldevice.utils.FaceView;
import home.smart.thuans.centraldevice.utils.FacedetectUtils;
import home.smart.thuans.centraldevice.utils.SharedPrefConstant;
import home.smart.thuans.centraldevice.utils.VoiceUtils;
import home.smart.thuans.centraldevice.watch.SonyActivity;
import home.smart.thuans.centraldevice.watch.WatchService;

public class MainActivity extends Activity {
    public static final String MAIN_FILTER_RECEIVER = "thuan.smart.house.main.APPLICATION_MOBILE";
    public static final String WATCH_STT_CONTENT = "receiving_speech_to_text_from_sony";
    public static final String MESSAGE_BROADCAST_SOURCE = "message recive from other service";
    private Intent serviceIntent;
    private SharedPreferences pre;
    private DeviceListAdapter sensorAdapter;
    private RecyclerView listDevice;
    private BroadcastReceiver receiver;
    private TextView txtResult;
    private Button btnConfig;
    private final String TAG="Main activity";
    private WebServer mWebServer;


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter(MAIN_FILTER_RECEIVER));
        Log.d(TAG,"start broadcast");
//        Intent tets = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(tets);
//        return;
        pre=getSharedPreferences(SharedPrefConstant.SMART_HOUSE_SHARED_PREF, MODE_PRIVATE);
        HouseConfig house = HouseConfig.getInstance();

        String tokenSaved = pre.getString(SharedPrefConstant.TOKEN_KEY,"");
        if (tokenSaved.equals("")){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        house.setTokenKey(tokenSaved);
        String botType = pre.getString(SharedPrefConstant.BOT_TYPE,"");
        if (botType.equals("")){
            Intent intent = new Intent(MainActivity.this, ConfigurationActivity.class);
            startActivity(intent);
            return;
        }
        house.setBotTypeName(botType);
        String botName = pre.getString(SharedPrefConstant.BOT_NAME,"");
        house.setBotName(botName);
        String botRole = pre.getString(SharedPrefConstant.BOT_ROLE,"");
        house.setBotRole(botRole);
        String ownerName = pre.getString(SharedPrefConstant.OWNER_NAME,"");
        house.setOwnerName(ownerName);
        String ownerRole = pre.getString(SharedPrefConstant.OWNER_ROLE,"");
        house.setOwnerRole(ownerRole);

    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        Log.d(TAG," on stop main");
        HouseConfig house = HouseConfig.getInstance();
        DeviceSQLite deviceSQLite= new DeviceSQLite();
        deviceSQLite.delete();
        for (ConnectedDevice connectedDevice : house.getDevices()){
            if (connectedDevice.getType().equals(DeviceConstant.DEVICE_TYPE)){
                connectedDevice.setState("off");
            }
            deviceSQLite.insert(connectedDevice);
        }
        SharedPreferences preferences = getSharedPreferences(SharedPrefConstant.SMART_HOUSE_SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

        edit.putString(SharedPrefConstant.BOT_NAME,house.getBotName());
        edit.putString(SharedPrefConstant.BOT_ROLE,house.getBotRole());
        edit.putString(SharedPrefConstant.OWNER_NAME,house.getOwnerName());
        edit.putString(SharedPrefConstant.OWNER_ROLE,house.getOwnerRole());
        edit.putString(SharedPrefConstant.BOT_TYPE,house.getBotTypeName());
        edit.putString(SharedPrefConstant.TOKEN_KEY,house.getTokenKey());
        edit.commit();
        Log.d(TAG,"Saved Token "+house.getTokenKey());
        super.onStop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStartServ = (Button) findViewById(R.id.btn_start_camera);
        btnStartServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        Button btnStartVoice = (Button) findViewById(R.id.btn_start_voice);
        btnStartVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(),CheckSensorService.class));
                Intent voiceIntent = new Intent(MainActivity.this,SonyActivity.class);
                startActivity(voiceIntent);
            }
        });
//        Log.w(TAG, "starting server.....");
//        final int port = 8080;
//        mWebServer = new WebServer(port);
//
//        (new Thread(mWebServer)).start()

        btnConfig = (Button) findViewById(R.id.btn_config);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent configIntent = new Intent(MainActivity.this,ConfigurationActivity.class);
                startActivity(configIntent);
            }
        });

        Button btnStopServ = (Button) findViewById(R.id.btn_stop_camera);

        btnStopServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(),CameraService.class));
                stopService(new Intent(getApplicationContext(),CheckSensorService.class));
            }
        });
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String messageSource = intent.getStringExtra(MainActivity.MESSAGE_BROADCAST_SOURCE);
                Log.d(TAG,"onReceive broadCast:"+messageSource);
                if (messageSource.equals(CameraService.FACE_DETECT)){
                    int numberFaces = intent.getIntExtra(CameraService.FACE_DETECT,0);
                    HouseConfig house = HouseConfig.getInstance();
                    byte[] byteArray = intent.getByteArrayExtra(CameraService.FACE_BITMAP);
                    if (numberFaces != house.getFaceDetected()) {
                        house.setFaceDetected(numberFaces);
                        house.setGuestIsAquaintance(false);
                    }
                    IntentLearned guestComing = BotUtils.getIntentByName(IntentConstant.GUEST_COMING);
                    String guestAlert = BotUtils.completeSentence(guestComing.getSentence(), numberFaces + "", "");
                    showReply(guestAlert);
                    FacedetectUtils singleFace = FacedetectUtils.getInstance(MainActivity.this);
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    Frame frame = new Frame.Builder().setBitmap(bmp).build();
                    SparseArray<Face> faces = singleFace.getSafeDetector().detect(frame);
                    FaceView overlay = (FaceView) findViewById(R.id.faceView);
                    overlay.setContent(bmp, faces);

                } else if (messageSource.equals(CheckSensorService.SENSOR_RESULT_MESSAGE)){
                    HouseConfig houseConfig = HouseConfig.getInstance();
                    Log.d(TAG,(houseConfig.getFaceDetected() >0)+"---"+houseConfig.mainDoorOpen()+"---"+!houseConfig.guestIsAquaintance());
                    if (houseConfig.getFaceDetected() >0 &&
                            houseConfig.mainDoorOpen() &&
                            !houseConfig.guestIsAquaintance() &&
                            (houseConfig.getDetectThiefMoment() == null)){
                        IntentLearned detectThief = BotUtils.getIntentByName(IntentConstant.DETECT_THIEF);
                        String detectThiefSentence = BotUtils.completeSentence(detectThief.getSentence(), "", "");
                        pushWatchMessage("Cảnh báo",detectThiefSentence);
                        showReply(detectThiefSentence);
                        houseConfig.setDetectThiefMoment(new Date());
                    }

                    sensorAdapter.notifyDataSetChanged();
                } else if (messageSource.equals(RetroArduinoSigleton.COMMAND_RESULT_MESSAGE)){
                    String succOrFail = intent.getStringExtra(RetroArduinoSigleton.COMMAND_RESULT_TYPE);
                    String sensorValue = intent.getStringExtra(RetroArduinoSigleton.RESULT_VALUE);
                    botReplyCommandByResult(succOrFail,sensorValue);
                }
            }
        };

        showSmartDeviceList();
        Bundle b = getIntent().getExtras();
        if (b!=null) {
            if (b.getString(WATCH_STT_CONTENT) != null){
                botReplyToSentence(" " + b.getString(WATCH_STT_CONTENT) + " ");
            }
            if (b.getString(ConfigurationActivity.CONFIGURATION_DONE)!= null){
                showReply(b.getString(ConfigurationActivity.CONFIGURATION_DONE));
            }
        }
        launchCameraServ();
        launchCheckSensorService();
    }
    private void pushWatchMessage(String messageName, String messageContent)
    {
        long time = System.currentTimeMillis();
        long sourceId = NotificationUtil.getSourceId(this,
                WatchService.EXTENSION_SPECIFIC_ID);
        if (sourceId == NotificationUtil.INVALID_ID) {
            Log.e(TAG, "Failed to insert data");
            return;
        }
        String profileImage = ExtensionUtils.getUriString(this,
                R.drawable.icon);

        // Build the notification.
        ContentValues eventValues = new ContentValues();
        eventValues.put(Notification.EventColumns.EVENT_READ_STATUS, false);
        eventValues.put(Notification.EventColumns.DISPLAY_NAME, messageName);
        eventValues.put(Notification.EventColumns.MESSAGE, messageContent);
        eventValues.put(Notification.EventColumns.PERSONAL, 1);
        eventValues.put(Notification.EventColumns.PROFILE_IMAGE_URI, profileImage);
        eventValues.put(Notification.EventColumns.PUBLISHED_TIME, time);
        eventValues.put(Notification.EventColumns.SOURCE_ID, sourceId);

        NotificationUtil.addEvent(this, eventValues);
    }
    private void botReplyCommandByResult(String commandResult, String resultValue){
        CurrentBotContext current = CurrentBotContext.getInstance();
        DetectIntent currentDetect = current.getDetected();
        ConnectedDevice currentTarget = current.getDeviceTarget();
        String replyComplete;
        IntentLearned reply = null;
        Log.d(TAG, commandResult + " ---  " + resultValue);
        if (commandResult.equals(IntentConstant.SUCCESS_REPLY)) {
            HouseConfig houseConfig = HouseConfig.getInstance();
            if (currentTarget.getPort().equals(DeviceConstant.ALERT_BELL_PORT)){
                houseConfig.setGuestIsAquaintance(true);
                houseConfig.setDetectThiefMoment(null);
            }

            reply = BotUtils.getIntentById(currentDetect.getSuccessReplyId());
            if (currentTarget.getType().equals(DeviceConstant.DEVICE_TYPE)) {
                houseConfig.changeStateByPort(currentTarget.getPort(),resultValue);
            } else if (currentTarget.getType().equals(DeviceConstant.SENSOR_TYPE)){
                houseConfig.changeValueByPort(currentTarget.getPort(),resultValue);
            }

            ConnectedDevice device = houseConfig.getDeviceByPort(currentTarget.getPort());
            Log.d(TAG, "  : xong tim thay  " + device.getName()+" : " +device.getState());
            sensorAdapter.notifyDataSetChanged();
        } else if  (commandResult.equals(IntentConstant.FAIL_REPLY)) {
            reply= BotUtils.getIntentById(currentDetect.getFailReplyId());
        }
        replyComplete = BotUtils.completeSentence(reply.getSentence(), resultValue, currentTarget.getName());
        showReply(replyComplete);
//        launchCheckSensorService();
    }

    private void botReplyToSentence(String humanSay){

        TermSQLite termSQLite = new TermSQLite();
        TermTargetSQLite termTargetSQLite = new TermTargetSQLite();
        List<Term> terms = termSQLite.getAllInSentence(humanSay);
        DetectIntent result = BotUtils.findBestDetected(terms);
        if (result != null) {
            CurrentBotContext current = CurrentBotContext.getInstance();
            current.setDetected(result);
            if (result.getFunctionName() != null){
                List<TermTarget> termTargets = termTargetSQLite.getAllInSentence(humanSay);
                ConnectedDevice device = BotUtils.findBestDeviceName(termTargets);
                if (device!=null) {
                    RetroArduinoSigleton retroArduinoSigleton = RetroArduinoSigleton.getInstance();
                    HouseConfig house = HouseConfig.getInstance();
                    ConnectedDevice currDevice = house.getDeviceByPort(device.getPort());
                    current.setDeviceTarget(currDevice );
                    Log.d(TAG, result.getFunctionName() + "  : function tim thay  " + currDevice.getName()+" : " +currDevice.getState());
                    switch (result.getFunctionName()) {
                        case IntentConstant.TURN_OBJECT_ON:
                            retroArduinoSigleton.turnObjectOn(device, MainActivity.this);
                            break;
                        case IntentConstant.TURN_OBJECT_OFF:
                            if (device.getPort().equals(DeviceConstant.ALERT_BELL_PORT)){
                                house.setGuestIsAquaintance(true);
                                house.setDetectThiefMoment(null);
                            }
                            retroArduinoSigleton.turnObjectOff(device, MainActivity.this);
                            break;
                    }
                } else {
                    Log.d(TAG,"Khong tim thay device cho cau"+humanSay);
                    //find Function unknowdevice
                }
            } else if (result.getSpeakIntent() != null){
                String replyComplete = null;
                IntentLearned reply = BotUtils.getIntentById(result.getReplyId());
                if (reply == null) {
                    reply = BotUtils.getIntentByName(IntentConstant.NOT_UNDERSTD);
                }
                replyComplete = BotUtils.completeSentence(reply.getSentence(), "", "");
                showReply(replyComplete);
            }
        } else {
            IntentLearned notUnderReply = BotUtils.getIntentByName(IntentConstant.NOT_UNDERSTD);
            String replyComplete = BotUtils.completeSentence(notUnderReply.getSentence(), "", "");
            showReply(replyComplete);
        }

    }

    private void showSmartDeviceList(){
        HouseConfig houseConfig = HouseConfig.getInstance();
        DeviceSQLite deviceSQLite= new DeviceSQLite();
        List<ConnectedDevice> deviceModelList = deviceSQLite.getAll();
        houseConfig.setDevices(deviceModelList);
        listDevice = (RecyclerView) findViewById(R.id.lstDeviceStatus);
        listDevice.setHasFixedSize(true);

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        sensorAdapter = new DeviceListAdapter(deviceModelList);
        listDevice.setAdapter(sensorAdapter);
        listDevice.setLayoutManager(MyLayoutManager);
    }

    private void launchCheckSensorService() {
        Intent startIntent = new Intent(this, CheckSensorService.class);
        startService(startIntent);
        Log.d(TAG,"---------start check sensor ser");
    }

    public void launchCameraServ() {
        serviceIntent = new Intent(this, CameraService.class);
        startService(serviceIntent);
        HouseConfig houseConfig = HouseConfig.getInstance();
        houseConfig.changeStateByPort(DeviceConstant.CAMERA_PORT,"on");
        sensorAdapter.notifyDataSetChanged();
        Log.d(TAG,"start cameraaaaa ser");
    }
    private  void showReply(String sentenceReply){
        if (txtResult == null){
            txtResult = (TextView) findViewById(R.id.message_result);
        }
        txtResult.setText(sentenceReply);
        VoiceUtils.speak(sentenceReply);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        VoiceUtils.stopSpeakApi();
        HouseConfig houseConfig = HouseConfig.getInstance();
        houseConfig.changeStateByPort(DeviceConstant.CAMERA_PORT,"off");

        stopService(new Intent(getApplicationContext(),CameraService.class));
        stopService(new Intent(getApplicationContext(),CheckSensorService.class));
//        mWebServer.stop();
        Log.d(TAG,"Saved Token destroy");
    }

}
