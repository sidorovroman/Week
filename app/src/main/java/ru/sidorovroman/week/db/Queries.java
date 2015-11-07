package ru.sidorovroman.week.db;

import ru.sidorovroman.week.db.entries.EntryAction;
import ru.sidorovroman.week.db.entries.EntryScheduler;

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

    String SQL_CREATE_SHEDULER_ENTRIES =
            "CREATE TABLE " + EntryScheduler.TABLE_NAME + " (" +
                    EntryScheduler._ID + " INTEGER PRIMARY KEY," +
                    EntryScheduler.COLUMN_WEEK_DAY_IDS + TEXT_TYPE + COMMA_SEP +
                    EntryScheduler.COLUMN_ACTION_ID + INTEGER_TYPE + COMMA_SEP +
                    EntryScheduler.COLUMN_TIME_FROM + INTEGER_TYPE + COMMA_SEP +
                    EntryScheduler.COLUMN_TIME_TO + INTEGER_TYPE +
                    " )";

    String SQL_DELETE_ACTION_ENTRIES = "DROP TABLE IF EXISTS " + EntryAction.TABLE_NAME;
    String SQL_DELETE_SHEDULER_ENTRIES = "DROP TABLE IF EXISTS " + EntryScheduler.TABLE_NAME;

}
