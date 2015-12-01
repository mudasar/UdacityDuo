package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.widget.WidgetRemoteViewService;

/**
 * Implementation of App Widget functionality.
 */
public class ScoreWidget extends AppWidgetProvider {

    private static final String LOG_TAG = ScoreWidget.class.getSimpleName();
    public static final String EXTRA_ITEM = "position";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }

        Log.i(LOG_TAG,"Widget updated");
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, WidgetRemoteViewService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.score_widget_layout);
        remoteViews.setRemoteAdapter(appWidgetId, R.id.widget_match_list, intent);
        remoteViews.setEmptyView(R.id.widget_match_list, R.id.empty_view);

        //TODO: Pending intent

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}

