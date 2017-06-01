package home.smart.thuans.centraldevice.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import home.smart.thuans.centraldevice.App;
import home.smart.thuans.centraldevice.artificialBot.DetectIntentSQLite;
import home.smart.thuans.centraldevice.artificialBot.IntentLearnedSQLite;
import home.smart.thuans.centraldevice.artificialBot.TermSQLite;
import home.smart.thuans.centraldevice.artificialBot.TermTargetSQLite;
import home.smart.thuans.centraldevice.device.DeviceSQLite;

/**
 * Created by Thuans on 4/27/2017.
 */

public class SqLiteHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "SmartHouseSQL3";

    private static final String TAG = "SQLite Helper utils";

    public SqLiteHelper( ) {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"bat dau tao table");
                //All necessary tables you like to create will create here
        db.execSQL(DeviceSQLite.createTable());
        db.execSQL(TermSQLite.createTable());
        db.execSQL(DetectIntentSQLite.createTable());
        db.execSQL(TermTargetSQLite.createTable());
        db.execSQL(IntentLearnedSQLite.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        // Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + DeviceSQLite.TABLE_DEVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TermSQLite.TABLE_TERM);
        db.execSQL("DROP TABLE IF EXISTS " + DetectIntentSQLite.TABLE_DETECT_INTENT);
        db.execSQL("DROP TABLE IF EXISTS " + IntentLearnedSQLite.TABLE_INTENT_LEARN);
        db.execSQL("DROP TABLE IF EXISTS " + TermTargetSQLite.TABLE_TERM_TARGET);
        onCreate(db);
    }
}
