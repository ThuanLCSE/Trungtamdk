package home.smart.thuans.centraldevice.artificialBot;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import home.smart.thuans.centraldevice.utils.SQLiteManager;

/**
 * Created by Thuans on 4/27/2017.
 */

public class TermSQLite {
    public static final String TABLE_TERM = "term";
    // Contacts Table Columns names
    private static final String KEY_CONTENT = "content";
    private static final String KEY_INTENT_ID = "intentId";
    private static final String KEY_TFIDF_POINT = "tfidfPoint";

    public static String createTable(){
        return "CREATE TABLE " + TABLE_TERM  + "("
                + KEY_CONTENT  + "  TEXT    ,"
                + KEY_INTENT_ID + " INTEGER ," +
                KEY_TFIDF_POINT + " REAL  ,"+
                "PRIMARY KEY ("+KEY_CONTENT+","+ KEY_INTENT_ID+")" +
                ")";
    }

    public int insert(Term term) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT, term.getContent());
        values.put(KEY_INTENT_ID, term.getIntentId());
        values.put(KEY_TFIDF_POINT, term.getTfidfPoint());
//        Log.d(TABLE_TERM,"----"+term.getContent()+"----")

        // Inserting Row
        int newId=(int)db.insert(TABLE_TERM, null, values);
        SQLiteManager.getInstance().closeDatabase();

        return newId;
    }


    public List<Term> getAllInSentence(String sentence){
        List<Term> result = new ArrayList<>();

        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        String selectQuery =  " SELECT * "
                + " FROM " + TABLE_TERM
//                + " WHERE ? MATCH '*' + "+KEY_CONTENT+" + '*' ";
                + " WHERE instr(?,"+KEY_CONTENT+") > 0";

        Log.d(TABLE_TERM, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,  new String[]{sentence});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Term term = new Term();
                term.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                term.setIntentId(cursor.getInt(cursor.getColumnIndex(KEY_INTENT_ID)));
                term.setTfidfPoint(cursor.getDouble(cursor.getColumnIndex(KEY_TFIDF_POINT)));

//                Log.d(TABLE_TERM, term.getContent());
                result.add(term);
            } while (cursor.moveToNext());
        }

        cursor.close();
        SQLiteManager.getInstance().closeDatabase();

        return result;

    }

    public void delete( ) {
        SQLiteDatabase db = SQLiteManager.getInstance().openDatabase();
        db.delete(TABLE_TERM,null,null);
        SQLiteManager.getInstance().closeDatabase();
    }
}
