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
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.Action;
import ru.sidorovroman.week.models.ActionTime;

/**
 * Created by sidorovroman on 12.10.15.
 */
public class WeekDbHelper extends SQLiteOpenHelper implements Queries{

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Week.db";


    private static final String LOG_TAG = WeekDbHelper.class.getSimpleName();
    private static final String DB_NAME = "db_actions";

    public WeekDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ACTION_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ACTION_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<ActionTime> getActionTimesByWeekDay(WeekDay day) {
        List<ActionTime> actionTimesList = new ArrayList<>();

        for (Action action : getAllActions()) {
            for (ActionTime actionTime : action.getActionTimeList()) {
                for (Integer weekDayId : actionTime.getWeekDayIds()) {
                    if(weekDayId.equals(day.getIndex())){
                        actionTimesList.add(actionTime);
                    }
                }
            }
        }

        return actionTimesList;
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

        String categoryIdsJsonString = cursor.getString(2);
        Type listType = new TypeToken<List<Integer>>(){}.getType();
        List<Integer> categoryIdsList = new Gson().fromJson(categoryIdsJsonString, listType);
        action.setCategoryIds(categoryIdsList);

        String actionTimesJsonString = cursor.getString(3);
        Type actionTimesListType = new TypeToken<List<ActionTime>>(){}.getType();
        List<ActionTime> actionTimes = new Gson().fromJson(actionTimesJsonString, actionTimesListType);
        action.setActionTimeList(actionTimes);

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
        ContentValues cv = actionToCV(action);
        long actionId = db.insert(EntryAction.TABLE_NAME, null, cv);
        db.close();

        return actionId;
    }

    public void updateAction(Action action) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = actionToCV(action);
        db.update(EntryAction.TABLE_NAME, cv, EntryAction._ID + "=" + action.getId(), null);
        db.close();
    }

    private ContentValues actionToCV(Action action) {

        String categoryIdsJsonString = new Gson().toJson(action.getCategoryIds());
        String acitonTimeListJsonString = new Gson().toJson(action.getActionTimeList());

        // создаем объект для данных
        ContentValues cv = new ContentValues();
        cv.put(EntryAction.COLUMN_NAME, action.getName());
        cv.put(EntryAction.COLUMN_CATEGORY_IDS, categoryIdsJsonString);
        cv.put(EntryAction.COLUMN_ACTION_TIMES, acitonTimeListJsonString);

        return cv;
    }
}