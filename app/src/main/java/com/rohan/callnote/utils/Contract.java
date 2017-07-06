package com.rohan.callnote.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Rohan on 18-Jan-17.
 */

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.rohan.callnote";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NOTES = "notes";

    public static final class NotesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;

        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_NOTE_TEXT = "note_text";
        public static final String COLUMN_CALL_TYPE = "call_type";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_CURRENT_USER_EMAIL = "current_user_email";

        public static Uri buildNotesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

}
