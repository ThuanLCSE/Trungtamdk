package home.smart.thuans.centraldevice.artificialBot;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import home.smart.thuans.centraldevice.utils.SQLiteManager;

/**
 * Created by Thuans on 4/27/2017.
 */

public class IntentLearnedSQLite {
    public static final String TABLE_INTENT_LEARN = "intent_learned";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_REPLY = "isReply";
    private static final String KEY_SPEAK_INTENT = "speakIntent";
    private static final String KEY_SENTENCE = "sentence";

    public static String createTable(){
        return "CREATE TABLE " + TABLE_INTENT_LEARN  + "("
                + KEY_ID  + "  INTEGER PRIMARY KEY    ,"
                + KEY_REPLY + " INTEGER ," +
                KEY_SPEAK_INTENT + " TEXT  ,"+
                KEY_SENTENCE + " TEXT  "+
                ")";
    }


    public int insert(IntentLearned intentLearned) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, intentLearned.getId());
        values.put(KEY_REPLY, intentLearned.isReply()? 1 : 0);
        values.put(KEY_SPEAK_INTENT, intentLearned.getSpeakIntent());
        values.put(KEY_SENTENCE, intentLearned.getSentence());

        // Inserting Row
        int newId=(int)db.insert(TABLE_INTENT_LEARN, null, values);
        SQLiteManager.getInstance().closeDatabase();

        return newId;
    }

    public IntentLearned findReplyById(int id){
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        String selectQuery =  " SELECT * "
                + " FROM " + TABLE_INTENT_LEARN
                + " WHERE "+KEY_ID+" = ? AND "+KEY_REPLY+" = 1";
//
        Cursor cursor = db.rawQuery(selectQuery,  new String[]{String.valueOf(id)});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            IntentLearned intentLearned = new IntentLearned();
            intentLearned.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            intentLearned.setReply(cursor.getInt(cursor.getColumnIndex(KEY_REPLY))==1?true:false);
            intentLearned.setSentence(cursor.getString(cursor.getColumnIndex(KEY_SENTENCE)));
            intentLearned.setSpeakIntent(cursor.getString(cursor.getColumnIndex(KEY_SPEAK_INTENT)));
            cursor.close();
            SQLiteManager.getInstance().closeDatabase();

            return intentLearned;
        } else return null;

    }
    public IntentLearned findSpeakByName(String name){
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        String selectQuery =  " SELECT * "
                + " FROM " + TABLE_INTENT_LEARN
                + " WHERE "+KEY_SPEAK_INTENT+" = ? AND "+KEY_REPLY+" = 0";

        Cursor cursor = db.rawQuery(selectQuery,  new String[]{String.valueOf(name)});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            IntentLearned intentLearned = new IntentLearned();
            intentLearned.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            intentLearned.setReply(cursor.getInt(cursor.getColumnIndex(KEY_REPLY))==1?true:false);
            intentLearned.setSentence(cursor.getString(cursor.getColumnIndex(KEY_SENTENCE)));
            intentLearned.setSpeakIntent(cursor.getString(cursor.getColumnIndex(KEY_SPEAK_INTENT)));
            cursor.close();
            SQLiteManager.getInstance().closeDatabase();

            return intentLearned;
        } else return null;

    }


    public void delete( ) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        db.delete(TABLE_INTENT_LEARN,null,null);
        SQLiteManager.getInstance().closeDatabase();
    }
}
