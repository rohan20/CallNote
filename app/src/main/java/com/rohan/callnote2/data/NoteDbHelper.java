package com.rohan.callnote2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rohan on 19-Jan-17.
 */

public class NoteDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "call_note_db";

    public NoteDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public NoteDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE "
                + NoteContract.NotesEntry.TABLE_NAME + " ("
                + NoteContract.NotesEntry._ID + " INTEGER PRIMARY KEY, "
                + NoteContract.NotesEntry.COLUMN_SERVER_ID + " INTEGER UNIQUE, "
                + NoteContract.NotesEntry.COLUMN_NUMBER + " TEXT, "
                + NoteContract.NotesEntry.COLUMN_NOTE_TEXT + " TEXT, "
                + NoteContract.NotesEntry.COLUMN_CALL_TYPE + " TEXT, "
                + NoteContract.NotesEntry.COLUMN_TIMESTAMP + " TEXT, "
                + NoteContract.NotesEntry.COLUMN_CURRENT_USER_EMAIL + " TEXT)";

        sqLiteDatabase.execSQL(SQL_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
