package com.rohan.callnote.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.rohan.callnote.R;
import com.rohan.callnote.models.Note;
import com.rohan.callnote.utils.Contract;
import com.rohan.callnote.utils.DBUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;

/**
 * Created by Rohan on 19-Jan-17.
 */

public class CallNoteWidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    public CallNoteWidgetListProvider(Context context, Intent intent) {
        mContext = context;

    }

    public void updateCursor() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        final long identityToken = Binder.clearCallingIdentity();

        mCursor = mContext.getContentResolver().query(Contract.NotesEntry.CONTENT_URI,
                new String[]{Contract.NotesEntry._ID, Contract.NotesEntry.COLUMN_SERVER_ID,
                        Contract.NotesEntry.COLUMN_NUMBER, Contract.NotesEntry.COLUMN_NOTE_TEXT,
                        Contract.NotesEntry.COLUMN_CALL_TYPE, Contract.NotesEntry
                        .COLUMN_TIMESTAMP}, null, null, null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        updateCursor();
    }

    @Override
    public void onDataSetChanged() {
        mCursor.close();
        updateCursor();
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_layout);
        mCursor.moveToPosition(i);

        Note note = DBUtils.getNoteFromCursor(mCursor);

        boolean contactIsUnknown = true;

        CallsProvider callsProvider = new CallsProvider(mContext);
        for (Call call : callsProvider.getCalls().getList()) {
            if (call.number.equals(note.getNumber())) {
                remoteViews.setTextViewText(R.id.widget_caller_name_text_view, call.name);
                contactIsUnknown = false;
                break;
            }
        }

        if (contactIsUnknown)
            remoteViews.setTextViewText(R.id.widget_caller_name_text_view, note.getNumber());

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, HH:mm");
        Date date = new Date(Long.parseLong(note.getTimestamp()) * 1000);
        String dateString = formatter.format(date);
        remoteViews.setTextViewText(R.id.widget_call_time_text_view, dateString);

        remoteViews.setTextViewText(R.id.widget_caller_note_text_text_view, note.getNoteText());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
