package com.rohan.callnote2.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.rohan.callnote2.data.NoteContract;
import com.rohan.callnote2.models.Note;

/**
 * Created by Rohan on 19-Jan-17.
 */

public class DBUtil {

    public static ContentValues cvFromNotes(@NonNull Note note) {

        ContentValues noteValues = new ContentValues();
        noteValues.put(NoteContract.NotesEntry.COLUMN_SERVER_ID, note.getServerID());
        noteValues.put(NoteContract.NotesEntry.COLUMN_NUMBER, note.getNumber());
        noteValues.put(NoteContract.NotesEntry.COLUMN_NOTE_TEXT, note.getNoteText());
        noteValues.put(NoteContract.NotesEntry.COLUMN_CALL_TYPE, note.getCallType());
        noteValues.put(NoteContract.NotesEntry.COLUMN_TIMESTAMP, note.getTimestamp());
        noteValues.put(NoteContract.NotesEntry.COLUMN_CURRENT_USER_EMAIL, note.getEmail());

        return noteValues;
    }

    public static Note getNoteFromCursor(@NonNull Cursor cursor) {

        Note note = new Note();

        note.setServerID(cursor.getInt(cursor.getColumnIndex(NoteContract.NotesEntry
                .COLUMN_SERVER_ID)));
        note.setNumber(cursor.getString(cursor.getColumnIndex(NoteContract.NotesEntry
                .COLUMN_NUMBER)));
        note.setNoteText(cursor.getString(cursor.getColumnIndex(NoteContract.NotesEntry
                .COLUMN_NOTE_TEXT)));
        note.setCallType(cursor.getString(cursor.getColumnIndex(NoteContract.NotesEntry
                .COLUMN_CALL_TYPE)));
        note.setTimestamp(cursor.getString(cursor.getColumnIndex(NoteContract.NotesEntry
                .COLUMN_TIMESTAMP)));
        note.setEmail(cursor.getString(cursor.getColumnIndex(NoteContract.NotesEntry
                .COLUMN_CURRENT_USER_EMAIL)));
        return note;
    }

}
