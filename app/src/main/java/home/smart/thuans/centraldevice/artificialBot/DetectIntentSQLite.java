package home.smart.thuans.centraldevice.artificialBot;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import home.smart.thuans.centraldevice.utils.SQLiteManager;

/**
 * Created by Thuans on 4/27/2017.
 */

public class DetectIntentSQLite {
    public static final String TABLE_DETECT_INTENT = "detect_intent";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FAIL_ID = "failReplyId";
    private static final String KEY_SUCCESS_ID = "successReplyId";
    private static final String KEY_REPLY_ID = "replyId";
    private static final String KEY_FUNCTION_NAME = "functionName";
    private static final String KEY_SPEAK_INTENT= "speakIntent";

    public static String createTable(){
        return "CREATE TABLE " + TABLE_DETECT_INTENT  + "("
                + KEY_ID  + "  INTEGER  PRIMARY KEY  ,"+
                 KEY_FAIL_ID + " INTEGER ," +
                KEY_SUCCESS_ID + " INTEGER ," +
                KEY_REPLY_ID + " INTEGER ," +
                KEY_FUNCTION_NAME + " TEXT ," +
                KEY_SPEAK_INTENT + " TEXT  "+
                ")";
    }


    public int insert(DetectIntent detectIntent) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, detectIntent.getId());
        values.put(KEY_FAIL_ID, detectIntent.getFailReplyId());
        values.put(KEY_SUCCESS_ID, detectIntent.getSuccessReplyId());
        values.put(KEY_REPLY_ID, detectIntent.getReplyId());
        values.put(KEY_FUNCTION_NAME, detectIntent.getFunctionName());
        values.put(KEY_SPEAK_INTENT, detectIntent.getSpeakIntent());

        // Inserting Row
        int newId=(int)db.insert(TABLE_DETECT_INTENT, null, values);
        SQLiteManager.getInstance().closeDatabase();

        return newId;
    }


    public DetectIntent  findById(int id){

        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        String selectQuery =  " SELECT * "
                + " FROM " + TABLE_DETECT_INTENT
                + " WHERE "+KEY_ID+" = ? ";

        Cursor cursor = db.rawQuery(selectQuery,  new String[]{String.valueOf(id)});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            DetectIntent detectIntent = new DetectIntent();
            detectIntent.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            detectIntent.setFailReplyId(cursor.getInt(cursor.getColumnIndex(KEY_FAIL_ID)));
            detectIntent.setReplyId(cursor.getInt(cursor.getColumnIndex(KEY_REPLY_ID)));
            detectIntent.setSuccessReplyId(cursor.getInt(cursor.getColumnIndex(KEY_SUCCESS_ID)));
            detectIntent.setSpeakIntent(cursor.getString(cursor.getColumnIndex(KEY_SPEAK_INTENT)));
            detectIntent.setFunctionName(cursor.getString(cursor.getColumnIndex(KEY_FUNCTION_NAME)));

            cursor.close();
            SQLiteManager.getInstance().closeDatabase();

            return detectIntent;
        } else return null;

    }

    public DetectIntent  findByName(String functionName){

        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        String selectQuery =  " SELECT * "
                + " FROM " + TABLE_DETECT_INTENT
                + " WHERE "+KEY_FUNCTION_NAME+" = ? ";

        Cursor cursor = db.rawQuery(selectQuery,  new String[]{String.valueOf(functionName)});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            DetectIntent detectIntent = new DetectIntent();
            detectIntent.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            detectIntent.setFailReplyId(cursor.getInt(cursor.getColumnIndex(KEY_FAIL_ID)));
            detectIntent.setReplyId(cursor.getInt(cursor.getColumnIndex(KEY_REPLY_ID)));
            detectIntent.setSuccessReplyId(cursor.getInt(cursor.getColumnIndex(KEY_SUCCESS_ID)));
            detectIntent.setSpeakIntent(cursor.getString(cursor.getColumnIndex(KEY_SPEAK_INTENT)));
            detectIntent.setFunctionName(cursor.getString(cursor.getColumnIndex(KEY_FUNCTION_NAME)));

            cursor.close();
            SQLiteManager.getInstance().closeDatabase();

            return detectIntent;
        } else return null;

    }

    public void delete( ) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        db.delete(TABLE_DETECT_INTENT,null,null);
        SQLiteManager.getInstance().closeDatabase();
    }
}
