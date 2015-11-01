package ru.sidorovroman.week.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.Action;
import ru.sidorovroman.week.models.Scheduler;

/**
 * Created by sidorovroman on 12.10.15.
 */
public class WeekDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Week.db";

    public WeekDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String LOG_TAG = WeekDbHelper.class.getSimpleName();
    private static final String DB_NAME = "db_actions";


    public static abstract class ActionEntry implements BaseColumns {

        public static final String TABLE_NAME = "table_action";

        public static final String COLUMN_NAME = "column_name";
        public static final String COLUMN_CATEGORY_IDS = "column_category_ids";
    }

    public static abstract class SchedulerEntry implements BaseColumns {

        public static final String TABLE_NAME = "table_sheduler";

        public static final String COLUMN_WEEK_DAY_IDS = "column_week_day_ids";
        public static final String COLUMN_ACTION_ID = "column_action_id";
        public static final String COLUMN_TIME_FROM = "column_time_from"; //time in minutes!!!
        public static final String COLUMN_TIME_TO = "column_time_to"; //time in minutes!!!
    }


    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ACTION_ENTRIES =
            "CREATE TABLE " + ActionEntry.TABLE_NAME + " (" +
                    ActionEntry._ID + " INTEGER PRIMARY KEY," +
                    ActionEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    ActionEntry.COLUMN_CATEGORY_IDS + TEXT_TYPE +
            " )";

    private static final String SQL_CREATE_SHEDULER_ENTRIES =
            "CREATE TABLE " + SchedulerEntry.TABLE_NAME + " (" +
                    SchedulerEntry._ID + " INTEGER PRIMARY KEY," +
                    SchedulerEntry.COLUMN_WEEK_DAY_IDS + TEXT_TYPE + COMMA_SEP +
                    SchedulerEntry.COLUMN_ACTION_ID + INTEGER_TYPE + COMMA_SEP +
                    SchedulerEntry.COLUMN_TIME_FROM + INTEGER_TYPE + COMMA_SEP +
                    SchedulerEntry.COLUMN_TIME_TO + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_ACTION_ENTRIES = "DROP TABLE IF EXISTS " + ActionEntry.TABLE_NAME;
    private static final String SQL_DELETE_SHEDULER_ENTRIES = "DROP TABLE IF EXISTS " + SchedulerEntry.TABLE_NAME;

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ACTION_ENTRIES);
        db.execSQL(SQL_CREATE_SHEDULER_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ACTION_ENTRIES);
        db.execSQL(SQL_DELETE_SHEDULER_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<Scheduler> getSchedulerByWeekDay(WeekDay day) {
        List<Scheduler> filteredScheduler = new ArrayList<>();
        List<Scheduler> allScheduler = null;
        allScheduler = getAllScheduler();
        for (Scheduler schedule : allScheduler) {
            List<Integer> weekDayIds = schedule.getWeekDayIds();
            for (Integer dayId : weekDayIds) {
                if(dayId.equals(day.getIndex())){
                    filteredScheduler.add(schedule);
                }
            }
        }

        return filteredScheduler;
    }

    public List<Scheduler> getAllScheduler() {
        List<Scheduler> schedulerList = new ArrayList<Scheduler>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SchedulerEntry.TABLE_NAME;

//        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Scheduler scheduler = new Scheduler();
                scheduler.setId(Long.parseLong(cursor.getString(0)));

                List<Integer> weekDaysList = new ArrayList<>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(cursor.getString(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        weekDaysList.add(jsonArray.getInt(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                scheduler.setWeekDayIds(weekDaysList);
                scheduler.setActionId(cursor.getLong(2));
                scheduler.setTimeFrom(cursor.getLong(3));
                scheduler.setTimeTo(cursor.getLong(4));
                // Adding contact to list
                schedulerList.add(scheduler);
            } while (cursor.moveToNext());
        }

        // return contact list
        return schedulerList;
    }
    public List<Action> getAllActions() {
        List<Action> actionList = new ArrayList<Action>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ActionEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Action action = new Action();
                action.setId(Integer.parseInt(cursor.getString(0)));
                action.setName(cursor.getString(1));

                List<Integer> categoryIdsList = new ArrayList<>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(cursor.getString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        categoryIdsList.add(jsonArray.getInt(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                actionList.add(action);
            } while (cursor.moveToNext());
        }

        // return contact list
        return actionList;
    }
}