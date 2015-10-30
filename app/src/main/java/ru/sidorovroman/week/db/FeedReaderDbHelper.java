package ru.sidorovroman.week.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by sidorovroman on 12.10.15.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String LOG_TAG = FeedReaderDbHelper.class.getSimpleName();
    private static final String DB_NAME = "db_actions";


    public static abstract class FeedEntry implements BaseColumns {

        public static final String TABLE_NAME = "action";

        public static final String COLUMN_NAME_NAME = "action_name";
        public static final String COLUMN_NAME_CATEGORIES = "action_categories";
        public static final String COLUMN_NAME_FROM = "action_time_from";
        public static final String COLUMN_NAME_TO = "action_to";
    }


    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CATEGORIES + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_FROM + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TO + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}