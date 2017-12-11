package com.example.drakewin.androidlabs;

/**
 * Created by DrakeWin on 11/28/2017.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DrakeWin on 11/28/2017.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    final static String DATABASE_NAME = "Messages.db";
    final static int VERSION_NUM = 4;
    final static String KEY_ID = "id";
    final static String KEY_MESSAGES ="messages";
    final static String TABLE_NAME ="Messages2";
    static final String[] COLUMNS = {KEY_ID,KEY_MESSAGES};

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME,null,VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        String CREATE_MESSAGES_DATABASE ="CREATE TABLE " + TABLE_NAME + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + KEY_MESSAGES + " TEXT )";
        db.execSQL(CREATE_MESSAGES_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        // droping older table if message already exists
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        this.onCreate(db);
    }
}
