package home.smart.thuans.centraldevice;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import home.smart.thuans.centraldevice.utils.FacedetectUtils;
import home.smart.thuans.centraldevice.utils.SQLiteManager;
import home.smart.thuans.centraldevice.utils.SqLiteHelper;
import home.smart.thuans.centraldevice.utils.VoiceUtils;

/**
 * Created by Thuans on 4/27/2017.
 */

public class App extends Application {
    private static Context context;
    private static SqLiteHelper dbHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        VoiceUtils.initializeInstance(context);
        dbHelper = new SqLiteHelper();
        SQLiteManager.initializeInstance(dbHelper);
        FacedetectUtils singleFace = FacedetectUtils.getInstance(context);
        Log.d("App context","initiating .....");

    }

    public static Context getContext(){
        return context;
    }
}
