package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoreWidget;
import barqsoft.footballscores.ScoresProvider;
import barqsoft.footballscores.Utilities;

/**
 * Created by mudasar on 29/11/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = WidgetRemoteViewsFactory.class.getSimpleName();

    private static final String[] FOOTBALL_SCORES_COLUMNS = {
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.MATCH_DAY
    };

    private static final int MATCH_ID = 0;
    private static final int HOME_COL = 3;
    private static final int AWAY_COL = 4;
    private static final int HOME_GOALS_COL = 5;
    private static final int AWAY_GOALS_COL = 6;

    private Cursor data = null;
    private Context mContext;
    private final ContentResolver mContentResolver;
    private final int mAppWidgetId;

    public WidgetRemoteViewsFactory(Context mContext, Intent intent) {
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        mContentResolver = mContext.getContentResolver();
        this.mContext = mContext;
        Log.i(LOG_TAG, "Widget Remote Views ctor");
    }



    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "Widget Remote Views created");
    }

    @Override
    public void onDataSetChanged() {
        if (data != null) {
            data.close();
        }
        Log.i(LOG_TAG, "Widget Remote Views data changed");
        final long identityToken = Binder.clearCallingIdentity();
        Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
        data = mContext.getContentResolver().query(uri,
                FOOTBALL_SCORES_COLUMNS,
                null,
                new String[]{new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))},
                DatabaseContract.scores_table.HOME_GOALS_COL + " ASC");

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (data != null) {
            data.close();
            data = null;
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public RemoteViews getViewAt(int index) {

        Log.i(LOG_TAG, "Widget Remote Views Factory");
        if (index == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(index)) {
            return null;
        }
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.score_widget_item);

        String homeTeam = data.getString(HOME_COL);
        int homeGoals = data.getInt(HOME_GOALS_COL);
        String awayTeam = data.getString(AWAY_COL);
        int awayGoals = data.getInt(AWAY_GOALS_COL);


        //fill the RemoteViews
        views.setTextViewText(R.id.home_name, homeTeam);
        views.setTextViewText(R.id.away_name, awayTeam);
        views.setTextViewText(R.id.score_textview, Utilities.getScores(homeGoals, awayGoals));


        Bundle bundle = new Bundle();
        bundle.putInt(ScoreWidget.EXTRA_ITEM, data.getInt(MATCH_ID));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(bundle);
        views.setOnClickFillInIntent(R.id.widget, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.score_widget_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int index) {
        if (data.moveToPosition(index))
            return data.getLong(MATCH_ID);
        return index;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
