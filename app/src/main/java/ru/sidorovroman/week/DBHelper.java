package ru.sidorovroman.week;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sidorovroman on 12.10.15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DBHelper.class.getSimpleName();
    private static final String DB_NAME = "db_actions";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");

        // создаем таблицу с полями
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "email text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}