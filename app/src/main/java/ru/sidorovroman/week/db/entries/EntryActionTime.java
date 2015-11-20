package ru.sidorovroman.week.db.entries;

import android.provider.BaseColumns;

/**
 * Created by sidorovroman on 07.11.15.
 */
public class EntryActionTime implements BaseColumns {

    public static final String TABLE_NAME = "table_action_time";

    public static final String COLUMN_WEEK_DAY_IDS = "column_week_day_ids";
    public static final String COLUMN_ACTION_ID = "column_action_id";
    public static final String COLUMN_TIME_FROM = "column_time_from"; //time in minutes!!!
    public static final String COLUMN_TIME_TO = "column_time_to"; //time in minutes!!!
}