package ru.sidorovroman.week.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.sidorovroman.week.db.entries.EntryAction;
import ru.sidorovroman.week.db.entries.EntryActionTime;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.Action;
import ru.sidorovroman.week.models.ActionTime;

/**
 * Created by sidorovroman on 12.10.15.
 */
public class WeekDbHelper extends SQLiteOpenHelper implements Queries{

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "Week.db";

    private static final String LOG_TAG = WeekDbHelper.class.getSimpleName();
    private static final String DB_NAME = "db_actions";

    public WeekDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ACTION_ENTRIES);
        db.execSQL(SQL_CREATE_ACTION_TIMES_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ACTION_ENTRIES);
        db.execSQL(SQL_DELETE_ACTION_TIME_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public List<Action> getActions() {
        List<Action> actionList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + EntryAction.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                actionList.add(getAction(cursor));
            } while (cursor.moveToNext());
        }

        return actionList;
    }

    public List<ActionTime> getActionTimes() {
        List<ActionTime> actionTimes = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + EntryActionTime.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                actionTimes.add(getActionTime(cursor));
            } while (cursor.moveToNext());
        }

        return actionTimes;
    }

    public List<ActionTime> getActionTimes(Long actionId) {
        List<ActionTime> actionTimes = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + EntryActionTime.TABLE_NAME + " where " + EntryActionTime.COLUMN_ACTION_ID  + "='" + actionId + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                actionTimes.add(getActionTime(cursor));
            } while (cursor.moveToNext());
        }

        return actionTimes;
    }

    public Action getAction(Long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + EntryAction.TABLE_NAME + " where " + EntryAction._ID + "='" + id + "'", null);
        cursor.moveToFirst();
        return getAction(cursor);
    }

    public ActionTime getActionTime(Long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + EntryActionTime.TABLE_NAME + " where " + EntryActionTime._ID + "='" + id + "'", null);
        cursor.moveToFirst();
        return getActionTime(cursor);
    }

    public long addAction(Action action) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = actionToCV(action);
        long actionId = db.insert(EntryAction.TABLE_NAME, null, cv);
        db.close();

        return actionId;
    }

    public long addActionTime(ActionTime actionTime) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = actionTimeToCV(actionTime);
        long actionId = db.insert(EntryActionTime.TABLE_NAME, null, cv);
        db.close();

        return actionId;
    }

    public void updateAction(Action action) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = actionToCV(action);
        db.update(EntryAction.TABLE_NAME, cv, EntryAction._ID + "=" + action.getId(), null);
        db.close();
    }

    public void updateActionTime(ActionTime actionTime) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = actionTimeToCV(actionTime);
        db.update(EntryActionTime.TABLE_NAME, cv, EntryActionTime._ID + "=" + actionTime.getId(), null);
        db.close();
    }

    public void removeAction(Long id) {
        getWritableDatabase().delete(EntryAction.TABLE_NAME, EntryAction._ID + "=" + id, null);
        removeActionTimeByActionId(id);
    }

    public void removeActionTime(Long id) {
        getWritableDatabase().delete(EntryActionTime.TABLE_NAME, EntryActionTime._ID + "=" + id, null);
    }

    public void removeActionTimeByActionId(Long actionId) {
        getWritableDatabase().delete(EntryActionTime.TABLE_NAME, EntryActionTime.COLUMN_ACTION_ID + "=" + actionId, null);
    }

    public List<ActionTime> getActionTimesByWeekDay(WeekDay day) {
        List<ActionTime> actionTimes = new ArrayList<>();
        for (ActionTime actionTime : getActionTimes()) {
            if(actionTime.getWeekDayIds().contains(day.getIndex())){
                actionTimes.add(actionTime);
            }
        }

        return actionTimes;
    }


    /* helpers */

    private Action getAction(Cursor cursor) {
        Action action = new Action();
        action.setId(Integer.parseInt(cursor.getString(0)));
        action.setName(cursor.getString(1));

        String categoryIdsJsonString = cursor.getString(2);
        Type listType = new TypeToken<List<Integer>>(){}.getType();
        List<Integer> categoryIdsList = new Gson().fromJson(categoryIdsJsonString, listType);
        action.setCategoryIds(categoryIdsList);

//        String actionTimesJsonString = cursor.getString(3);
//        Type actionTimesListType = new TypeToken<List<ActionTime>>(){}.getType();
//        List<ActionTime> actionTimes = new Gson().fromJson(actionTimesJsonString, actionTimesListType);
//        action.setActionTimeList(actionTimes);

        return action;
    }

    private ActionTime getActionTime(Cursor cursor) {

        ActionTime actionTime = new ActionTime();
        actionTime.setId(Integer.parseInt(cursor.getString(0)));

        String weekIdsJsonString = cursor.getString(1);
        Type listType = new TypeToken<List<Integer>>(){}.getType();
        List<Integer> weekIds = new Gson().fromJson(weekIdsJsonString, listType);
        actionTime.setWeekDayIds(weekIds);

        actionTime.setActionId(cursor.getLong(2));
        actionTime.setTimeFrom(cursor.getInt(3));
        actionTime.setTimeTo(cursor.getInt(4));

        return actionTime;
    }

    private ContentValues actionToCV(Action action) {

        String categoryIdsJsonString = new Gson().toJson(action.getCategoryIds());

        // создаем объект для данных
        ContentValues cv = new ContentValues();
        cv.put(EntryAction.COLUMN_NAME, action.getName());
        cv.put(EntryAction.COLUMN_CATEGORY_IDS, categoryIdsJsonString);

        return cv;
    }

    private ContentValues actionTimeToCV(ActionTime actionTime) {

        String weekDays = new Gson().toJson(actionTime.getWeekDayIds());

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        cv.put(EntryActionTime.COLUMN_WEEK_DAY_IDS, weekDays);
        cv.put(EntryActionTime.COLUMN_ACTION_ID, actionTime.getActionId());
        cv.put(EntryActionTime.COLUMN_TIME_FROM, actionTime.getTimeFrom());
        cv.put(EntryActionTime.COLUMN_TIME_TO, actionTime.getTimeTo());

        return cv;
    }

}