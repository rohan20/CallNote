package com.rohan.callnote2.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.rohan.callnote2.R;

/**
 * Created by Rohan on 19-Jan-17.
 */

public class CallNoteWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            int layoutId = R.layout.callnote_widget;
            RemoteViews widget = new RemoteViews(context.getPackageName(), layoutId);

            Intent serviceIntent = new Intent(context, CallNoteWidgetService.class);
            widget.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
            widget.setEmptyView(R.id.widget_list_view, R.id.empty_view);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            appWidgetManager.updateAppWidget(appWidgetId, widget);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

}
