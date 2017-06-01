package home.smart.thuans.centraldevice.device;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import home.smart.thuans.centraldevice.utils.SQLiteManager;

/**
 * Created by Thuans on 4/27/2017.
 */

public class DeviceSQLite{

    public static final String TABLE_DEVICE = "connected_device_update";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PORT = "port";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_FEATURE = "feature";
    private static final String KEY_VALUE = "value";
    private static final String KEY_STATE = "state";

    public static String createTable(){
        return "CREATE TABLE " + TABLE_DEVICE  + "("
                + KEY_ID  + " TEXT PRIMARY KEY    ,"
                +  KEY_NAME+ "  TEXT ," +
                KEY_FEATURE + "  TEXT  ,"+
                KEY_PORT + "  TEXT  ,"+
                KEY_VALUE + "  TEXT  ,"+
                KEY_STATE + "  TEXT  ,"+
                KEY_TYPE+ "  TEXT  "+ ")";
    }


    public int insert(ConnectedDevice device) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, device.getId());
        values.put(KEY_NAME, device.getName());
        values.put(KEY_FEATURE, device.getFeature());
        values.put(KEY_VALUE, device.getValue());
        values.put(KEY_STATE, device.getState());
        values.put(KEY_PORT, device.getPort());
        values.put(KEY_TYPE, device.getType());

        // Inserting Row
        int newId  = (int) db.insert(TABLE_DEVICE, null, values);
        SQLiteManager.getInstance().closeDatabase();

        return newId;
    }

    public List<ConnectedDevice> getAll(){
        List<ConnectedDevice> result = new ArrayList<ConnectedDevice>();

        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        String selectQuery =  " SELECT * "
                + " FROM " + TABLE_DEVICE
                ;

        Log.d(TABLE_DEVICE, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,  new String[]{});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ConnectedDevice connectedDevice = new ConnectedDevice();
                connectedDevice.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                connectedDevice.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                connectedDevice.setFeature(cursor.getString(cursor.getColumnIndex(KEY_FEATURE)));
                connectedDevice.setPort(cursor.getString(cursor.getColumnIndex(KEY_PORT)));
                connectedDevice.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
                connectedDevice.setValue(cursor.getString(cursor.getColumnIndex(KEY_VALUE)));
                connectedDevice.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));

                result.add(connectedDevice);
            } while (cursor.moveToNext());
        }

        cursor.close();
        SQLiteManager.getInstance().closeDatabase();

        return result;
    }
    public ConnectedDevice findById(String id){

        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        String selectQuery =  " SELECT * "
                + " FROM " + TABLE_DEVICE
                + " WHERE "+KEY_ID+" = ? ";

        Cursor cursor = db.rawQuery(selectQuery,  new String[]{String.valueOf(id)});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            ConnectedDevice connectedDevice = new ConnectedDevice();
            connectedDevice.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            connectedDevice.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            connectedDevice.setFeature(cursor.getString(cursor.getColumnIndex(KEY_FEATURE)));
            connectedDevice.setPort(cursor.getString(cursor.getColumnIndex(KEY_PORT)));
            connectedDevice.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            connectedDevice.setValue(cursor.getString(cursor.getColumnIndex(KEY_VALUE)));
            connectedDevice.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
            cursor.close();
            SQLiteManager.getInstance().closeDatabase();

            return connectedDevice;
        } else return null;

    }

    public void delete( ) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        db.delete(TABLE_DEVICE,null,null);
        SQLiteManager.getInstance().closeDatabase();
    }
}