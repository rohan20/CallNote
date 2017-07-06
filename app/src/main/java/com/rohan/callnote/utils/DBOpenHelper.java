package com.rohan.callnote.utils;

import com.rohan.callnote.utils.Contract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rohan on 19-Jan-17.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "call_note_db";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE "
                + Contract.NotesEntry.TABLE_NAME + " ("
                + Contract.NotesEntry._ID + " INTEGER PRIMARY KEY, "
                + Contract.NotesEntry.COLUMN_SERVER_ID + " INTEGER UNIQUE, "
                + Contract.NotesEntry.COLUMN_NUMBER + " TEXT, "
                + Contract.NotesEntry.COLUMN_NOTE_TEXT + " TEXT, "
                + Contract.NotesEntry.COLUMN_CALL_TYPE + " TEXT, "
                + Contract.NotesEntry.COLUMN_TIMESTAMP + " TEXT, "
                + Contract.NotesEntry.COLUMN_CURRENT_USER_EMAIL + " TEXT)";

        sqLiteDatabase.execSQL(SQL_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
