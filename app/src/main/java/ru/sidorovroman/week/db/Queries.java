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
                    EntryAction.COLUMN_ACTION_TIMES + TEXT_TYPE +
                    " )";

    String SQL_DELETE_ACTION_ENTRIES = "DROP TABLE IF EXISTS " + EntryAction.TABLE_NAME;

}
