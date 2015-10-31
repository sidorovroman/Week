package ru.sidorovroman.week.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by sidorovroman on 12.10.15.
 */
public class WeekDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
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

    public static abstract class ShedulerEntry implements BaseColumns {

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
            "CREATE TABLE " + ShedulerEntry.TABLE_NAME + " (" +
                    ShedulerEntry._ID + " INTEGER PRIMARY KEY," +
                    ShedulerEntry.COLUMN_WEEK_DAY_IDS + TEXT_TYPE + COMMA_SEP +
                    ShedulerEntry.COLUMN_ACTION_ID + INTEGER_TYPE + COMMA_SEP +
                    ShedulerEntry.COLUMN_TIME_FROM + INTEGER_TYPE + COMMA_SEP +
                    ShedulerEntry.COLUMN_TIME_TO + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_ACTION_ENTRIES = "DROP TABLE IF EXISTS " + ActionEntry.TABLE_NAME;
    private static final String SQL_DELETE_SHEDULER_ENTRIES = "DROP TABLE IF EXISTS " + ShedulerEntry.TABLE_NAME;


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
}