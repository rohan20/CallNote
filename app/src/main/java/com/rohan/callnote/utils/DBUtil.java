package com.rohan.callnote.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.rohan.callnote.models.Note;

/**
 * Created by Rohan on 19-Jan-17.
 */

public class DBUtil {

    public static ContentValues cvFromNotes(@NonNull Note note) {

        ContentValues noteValues = new ContentValues();
        noteValues.put(Contract.NotesEntry.COLUMN_SERVER_ID, note.getServerID());
        noteValues.put(Contract.NotesEntry.COLUMN_NUMBER, note.getNumber());
        noteValues.put(Contract.NotesEntry.COLUMN_NOTE_TEXT, note.getNoteText());
        noteValues.put(Contract.NotesEntry.COLUMN_CALL_TYPE, note.getCallType());
        noteValues.put(Contract.NotesEntry.COLUMN_TIMESTAMP, note.getTimestamp());

        return noteValues;
    }

    public static Note getNoteFromCursor(@NonNull Cursor cursor) {

        Note note = new Note();

        note.setServerID(cursor.getInt(cursor.getColumnIndex(Contract.NotesEntry
                .COLUMN_SERVER_ID)));
        note.setNumber(cursor.getString(cursor.getColumnIndex(Contract.NotesEntry
                .COLUMN_NUMBER)));
        note.setNoteText(cursor.getString(cursor.getColumnIndex(Contract.NotesEntry
                .COLUMN_NOTE_TEXT)));
        note.setCallType(cursor.getString(cursor.getColumnIndex(Contract.NotesEntry
                .COLUMN_CALL_TYPE)));
        note.setTimestamp(cursor.getString(cursor.getColumnIndex(Contract.NotesEntry
                .COLUMN_TIMESTAMP)));

        return note;
    }

}
