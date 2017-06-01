package home.smart.thuans.centraldevice.artificialBot;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import home.smart.thuans.centraldevice.utils.SQLiteManager;

/**
 * Created by Thuans on 4/28/2017.
 */

public class TermTargetSQLite {
    public static final String TABLE_TERM_TARGET= "term_target";
    // Contacts Table Columns names
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TARGET_ID = "targetId";
    private static final String KEY_TARGET_TYPE = "targetType";
    private static final String KEY_TFIDF_POINT = "tfidfPoint";

    public static String createTable(){
        return "CREATE TABLE " + TABLE_TERM_TARGET + "("
                + KEY_CONTENT  + "  TEXT    ,"
                + KEY_TARGET_ID + " TEXT ," 
                + KEY_TARGET_TYPE+ " TEXT ," +
                KEY_TFIDF_POINT + " REAL  ,"+
                "PRIMARY KEY ("+KEY_CONTENT+","+ KEY_TARGET_ID+")" +
                ")";
    }


    public int insert(TermTarget termTarget) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT, termTarget.getContent());
        values.put(KEY_TARGET_ID, termTarget.getTargetId());
        values.put(KEY_TARGET_TYPE, termTarget.getTargetType());
        values.put(KEY_TFIDF_POINT, termTarget.getTfidfPoint());

        // Inserting Row
        int newId=(int)db.insert(TABLE_TERM_TARGET, null, values);
        SQLiteManager.getInstance().closeDatabase();

        return newId;
    }


    public List<TermTarget> getAllInSentence(String sentence){
        List<TermTarget> result = new ArrayList<>();

        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        String selectQuery =  " SELECT * "
                + " FROM " + TABLE_TERM_TARGET
                + " WHERE instr(?,"+KEY_CONTENT+") > 0";

        Log.d(TABLE_TERM_TARGET, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,  new String[]{sentence});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TermTarget termTarget= new TermTarget();
                termTarget.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                termTarget.setTargetId(cursor.getString(cursor.getColumnIndex(KEY_TARGET_ID)));
                termTarget.setTfidfPoint(cursor.getDouble(cursor.getColumnIndex(KEY_TFIDF_POINT)));
                termTarget.setTargetType(cursor.getString(cursor.getColumnIndex(KEY_TARGET_TYPE)));
//                Log.d(TABLE_TERM, term.getContent());
                result.add(termTarget);
            } while (cursor.moveToNext());
        }

        cursor.close();
        SQLiteManager.getInstance().closeDatabase();

        return result;

    }

    public void delete( ) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        db.delete(TABLE_TERM_TARGET,null,null);
        SQLiteManager.getInstance().closeDatabase();
    }
}
