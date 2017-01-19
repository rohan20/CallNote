package com.rohan.callnote.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Rohan on 19-Jan-17.
 */

public class CallNoteWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CallNoteWidgetListProvider(this.getApplicationContext(), intent);
    }
}
