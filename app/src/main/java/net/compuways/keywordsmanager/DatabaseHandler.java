package net.compuways.keywordsmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benton on 17/04/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "keywordsManager";

    // Contacts table name
    private static final String TABLE_KEYWORDS = "keywords";

    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_GROUPID = "_groupid";
    private static final String KEY_KEYWORD = "_keyword";
    private static final String KEY_TYPE = "_type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_KEYWORDS_TABLE = "CREATE TABLE " + TABLE_KEYWORDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GROUPID + " INTEGER,"
                + KEY_KEYWORD + " TEXT," + KEY_TYPE + " INTEGER )";
        System.out.println(CREATE_KEYWORDS_TABLE);
        db.execSQL(CREATE_KEYWORDS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEYWORDS);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public long addKeyword(Keyword keyword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUPID, keyword.get_groupid()); // Keyword _groupid
        values.put(KEY_KEYWORD, keyword.get_keyword());
        values.put(KEY_TYPE, keyword.get_type());

        // Inserting Row
        long n = db.insert(TABLE_KEYWORDS, null, values);
        db.close(); // Closing database connection

        return n;

    }

    // Getting single keyword
    public Keyword getKeyword(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_KEYWORDS, new String[]{KEY_ID,
                        KEY_GROUPID, KEY_KEYWORD}, KEY_ID + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Keyword keyword = new Keyword(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        cursor.close();
        db.close();
        return keyword;
    }

    // Getting All Keywords
    public List<Keyword> getAllKeywords() {
        List<Keyword> keywordList = new ArrayList<Keyword>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_KEYWORDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Keyword keyword = new Keyword();
                keyword.set_id(Integer.parseInt(cursor.getString(0)));
                keyword.set_groupid(Integer.parseInt(cursor.getString(1)));
                keyword.set_keyword(cursor.getString(2));
                // Adding keyword to list
                keywordList.add(keyword);
            } while (cursor.moveToNext());
        }

        // return keyword list
        cursor.close();
        db.close();
        return keywordList;
    }

    // Getting keywords Count
    public int getKeywordsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_KEYWORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // Updating single keyword
    public int updateKeyword(Keyword keyword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUPID, keyword.get_groupid());
        values.put(KEY_KEYWORD, keyword.get_keyword());
        values.put(KEY_TYPE, keyword.get_type());
        int rows = db.update(TABLE_KEYWORDS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(keyword.get_id())});
        db.close();
        return rows;
    }

    // Deleting single keyword
    public int deleteKeyword(Keyword keyword) {
        SQLiteDatabase db = this.getWritableDatabase();
        int n = db.delete(TABLE_KEYWORDS, KEY_ID + " = ?",
                new String[]{String.valueOf(keyword.get_id())});
        db.close();
        return n;
    }
    // Deleting single keyword
    public int deleteKeywordByGroup(String groupid) {
        SQLiteDatabase db = this.getWritableDatabase();
        int n = db.delete(TABLE_KEYWORDS, KEY_GROUPID + " = ?",
                new String[]{groupid});
        db.close();
        return n;
    }
    public int deleteKeywordByType(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        int n = db.delete(TABLE_KEYWORDS, KEY_TYPE + " = ?",
                new String[]{type});
        db.close();
        return n;
    }
}