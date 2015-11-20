package ru.sidorovroman.week.db;

import ru.sidorovroman.week.db.entries.EntryAction;
import ru.sidorovroman.week.db.entries.EntryActionTime;

/**
 * Created by sidorovroman on 07.11.15.
 */
public interface Queries {

    String TEXT_TYPE = " TEXT";
    String INTEGER_TYPE = " INTEGER";
    String COMMA_SEP = ",";

    String SQL_CREATE_ACTION_ENTRIES =
            "CREATE TABLE " + EntryAction.TABLE_NAME + " (" +
                    EntryAction._ID + " INTEGER PRIMARY KEY," +
                    EntryAction.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    EntryAction.COLUMN_CATEGORY_IDS + TEXT_TYPE +
                    " )";

    String SQL_CREATE_ACTION_TIMES_ENTRIES =
            "CREATE TABLE " + EntryActionTime.TABLE_NAME + " (" +
                    EntryActionTime._ID + " INTEGER PRIMARY KEY," +
                    EntryActionTime.COLUMN_WEEK_DAY_IDS + TEXT_TYPE + COMMA_SEP +
                    EntryActionTime.COLUMN_ACTION_ID + INTEGER_TYPE + COMMA_SEP +
                    EntryActionTime.COLUMN_TIME_FROM + INTEGER_TYPE + COMMA_SEP +
                    EntryActionTime.COLUMN_TIME_TO + INTEGER_TYPE +
                    " )";

    String SQL_DELETE_ACTION_ENTRIES = "DROP TABLE IF EXISTS " + EntryAction.TABLE_NAME;
    String SQL_DELETE_ACTION_TIME_ENTRIES = "DROP TABLE IF EXISTS " + EntryActionTime.TABLE_NAME;

}
