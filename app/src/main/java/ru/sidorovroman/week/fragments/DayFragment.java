package ru.sidorovroman.week.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;

import ru.sidorovroman.week.DrawView;
import ru.sidorovroman.week.R;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.Scheduler;


public class DayFragment extends Fragment{

    private static final String LOG_TAG = DayFragment.class.getSimpleName();
    private WeekDay day;
    private RelativeLayout timeLineContainer;
    private LinearLayout scheduler;

    public DayFragment(WeekDay day) {
        this.day = day;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG,day.getLabel());
        View inflate = inflater.inflate(R.layout.fr_day, container, false);

        timeLineContainer = (RelativeLayout) inflate.findViewById(R.id.timeline);
        scheduler = (LinearLayout) inflate.findViewById(R.id.scheduler);

        DrawView drawView = new DrawView(getActivity(), getScreenWidth());
        timeLineContainer.addView(drawView);
        readAll();

        return inflate;
    }

    private long getScreenWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private void readAll() {
        WeekDbHelper weekDbHelper = new WeekDbHelper(getActivity());

        List<Scheduler> schedulerByWeekDay = weekDbHelper.getSchedulerByWeekDay(day);

        for (Scheduler s : schedulerByWeekDay) {
            Log.d(LOG_TAG,s.toString());

            TextView tv = new TextView(getActivity());
            tv.setText(s.toString());
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            scheduler.addView(tv);
        }

//
//        SQLiteDatabase db = weekDbHelper.getReadableDatabase();
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                WeekDbHelper.ActionEntry._ID,
//                WeekDbHelper.ActionEntry.COLUMN_NAME,
//                WeekDbHelper.ActionEntry.COLUMN_CATEGORY_IDS
//        };
//        Cursor actionCursor = db.query(
//                WeekDbHelper.ActionEntry.TABLE_NAME,  // The table to query
//                projection,                               // The columns to return
//                null,                                     // The columns for the WHERE clause
//                null,                                     // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                null                                      // The sort order
//        );
//
//        // ставим позицию курсора на первую строку выборки
//        // если в выборке нет строк, вернется false
//        if (actionCursor.moveToFirst()) {
//
//            // определяем номера столбцов по имени в выборке
//            int idColIndex = actionCursor.getColumnIndex(WeekDbHelper.ActionEntry._ID);
//            int nameColIndex = actionCursor.getColumnIndex(WeekDbHelper.ActionEntry.COLUMN_NAME);
//            int categoriesColIndex = actionCursor.getColumnIndex(WeekDbHelper.ActionEntry.COLUMN_CATEGORY_IDS);
//
//            do {
//                // получаем значения по номерам столбцов и пишем все в лог
//                Log.d(LOG_TAG,
//                        "ID = " + actionCursor.getInt(idColIndex) +
//                                ", name = " + actionCursor.getString(nameColIndex) +
//                                ", categories = " + actionCursor.getString(categoriesColIndex));
//                // переход на следующую строку
//                // а если следующей нет (текущая - последняя), то false - выходим из цикла
//            } while (actionCursor.moveToNext());
//        } else
//            Log.d(LOG_TAG, "0 rows");
//        actionCursor.close();
//
//
//        // Define a projection1 that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projectionScheduler = {
//                WeekDbHelper.SchedulerEntry._ID,
//                WeekDbHelper.SchedulerEntry.COLUMN_WEEK_DAY_IDS,
//                WeekDbHelper.SchedulerEntry.COLUMN_TIME_FROM,
//                WeekDbHelper.SchedulerEntry.COLUMN_TIME_TO
//        };
//        Cursor scedulerCursor = db.query(
//                WeekDbHelper.SchedulerEntry.TABLE_NAME,  // The table to query
//                projectionScheduler,                               // The columns to return
//                null,                                     // The columns for the WHERE clause
//                null,                                     // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                null                                      // The sort order
//        );
//
//        // ставим позицию курсора на первую строку выборки
//        // если в выборке нет строк, вернется false
//        if (scedulerCursor.moveToFirst()) {
//
//            // определяем номера столбцов по имени в выборке
//            int idColIndex = scedulerCursor.getColumnIndex(WeekDbHelper.SchedulerEntry._ID);
//            int weekDaysColIndex = scedulerCursor.getColumnIndex(WeekDbHelper.SchedulerEntry.COLUMN_WEEK_DAY_IDS);
//            int fromColIndex = scedulerCursor.getColumnIndex(WeekDbHelper.SchedulerEntry.COLUMN_TIME_FROM);
//            int toColIndex = scedulerCursor.getColumnIndex(WeekDbHelper.SchedulerEntry.COLUMN_TIME_TO);
//
//            do {
//                // получаем значения по номерам столбцов и пишем все в лог
//                Log.d(LOG_TAG,
//                        "ID = " + scedulerCursor.getInt(idColIndex) +
//                                ", weekDays = " + scedulerCursor.getString(weekDaysColIndex) +
//                                ", from = " + scedulerCursor.getString(fromColIndex) +
//                                ", to = " + scedulerCursor.getString(toColIndex));
//                // переход на следующую строку
//                // а если следующей нет (текущая - последняя), то false - выходим из цикла
//            } while (scedulerCursor.moveToNext());
//        } else
//            Log.d(LOG_TAG, "0 rows");
//        scedulerCursor.close();
    }
}
