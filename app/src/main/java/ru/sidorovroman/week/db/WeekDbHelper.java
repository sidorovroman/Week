package ru.sidorovroman.week.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ru.sidorovroman.week.db.entries.EntryAction;
import ru.sidorovroman.week.db.entries.EntryScheduler;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.Action;
import ru.sidorovroman.week.models.Scheduler;

/**
 * Created by sidorovroman on 12.10.15.
 */
public class WeekDbHelper extends SQLiteOpenHelper implements Queries{

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Week.db";


    private static final String LOG_TAG = WeekDbHelper.class.getSimpleName();
    private static final String DB_NAME = "db_actions";

    public WeekDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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
        for (Scheduler schedule : getAllScheduler()) {
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
        List<Scheduler> schedulerList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + EntryScheduler.TABLE_NAME;

//        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Scheduler scheduler = getSchedulerFromCursor(cursor);
                schedulerList.add(scheduler);
            } while (cursor.moveToNext());
        }

        // return contact list
        return schedulerList;
    }

    private Scheduler getSchedulerFromCursor(Cursor cursor) {
        Scheduler scheduler = new Scheduler();
        scheduler.setId(Long.parseLong(cursor.getString(0)));

        List<Integer> weekDaysList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(cursor.getString(1));

            for (int i = 0; i < jsonArray.length(); i++) {
                weekDaysList.add(jsonArray.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        scheduler.setWeekDayIds(weekDaysList);
        scheduler.setActionId(cursor.getLong(2));
        scheduler.setTimeFrom(cursor.getInt(3));
        scheduler.setTimeTo(cursor.getInt(4));

        return scheduler;
    }

    public List<Scheduler> getSchedulerByActionId( Long id){
        List<Scheduler> schedulerList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + EntryScheduler.TABLE_NAME + " where " + EntryScheduler.COLUMN_ACTION_ID + "='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Scheduler scheduler = getSchedulerFromCursor(cursor);
                schedulerList.add(scheduler);
            } while (cursor.moveToNext());
        }

        return schedulerList;
    }

    public List<Action> getAllActions() {
        List<Action> actionList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + EntryAction.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                actionList.add(getActionFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return actionList;
    }

    private Action getActionFromCursor(Cursor cursor) {
        Action action = new Action();
        action.setId(Integer.parseInt(cursor.getString(0)));
        action.setName(cursor.getString(1));

        List<Integer> categoryIdsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(cursor.getString(2));
            for (int i = 0; i < jsonArray.length(); i++) {
                categoryIdsList.add(jsonArray.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        action.setCategoryIds(categoryIdsList);


        return action;
    }

    public Action getActionById(Long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + EntryAction.TABLE_NAME + " where " + EntryAction._ID + "='" + id + "'", null);
        cursor.moveToFirst();
        return getActionFromCursor(cursor);
    }

    public long addAction(Action action) {
        SQLiteDatabase db = getWritableDatabase();

        JSONArray categoriesJsonArray = new JSONArray(action.getCategoryIds());

        // создаем объект для данных
        ContentValues cv = new ContentValues();
        cv.put(EntryAction.COLUMN_NAME, action.getName());
        cv.put(EntryAction.COLUMN_CATEGORY_IDS, categoriesJsonArray.toString());

        long actionId = db.insert(EntryAction.TABLE_NAME, null, cv);

        db.close();

        return actionId;
    }


    public void updateAction(Action action) {
        SQLiteDatabase db = getWritableDatabase();

        JSONArray categoriesJsonArray = new JSONArray(action.getCategoryIds());

        // создаем объект для данных
        ContentValues cv = new ContentValues();
        cv.put(EntryAction.COLUMN_NAME, action.getName());
        cv.put(EntryAction.COLUMN_CATEGORY_IDS, categoriesJsonArray.toString());

        int update = db.update(EntryAction.TABLE_NAME, cv, EntryAction._ID + "=" + action.getId(), null);

        db.close();
    }
    
    public long addScheduler(Scheduler scheduler) {

        SQLiteDatabase db = getWritableDatabase();

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        cv.put(EntryScheduler.COLUMN_WEEK_DAY_IDS, new JSONArray(scheduler.getWeekDayIds()).toString());
        cv.put(EntryScheduler.COLUMN_ACTION_ID, scheduler.getActionId());
        cv.put(EntryScheduler.COLUMN_TIME_FROM, scheduler.getTimeFrom());
        cv.put(EntryScheduler.COLUMN_TIME_TO, scheduler.getTimeTo());

        long schedulerId = db.insert(EntryScheduler.TABLE_NAME, null, cv);

        db.close();

        return schedulerId;
    }

}