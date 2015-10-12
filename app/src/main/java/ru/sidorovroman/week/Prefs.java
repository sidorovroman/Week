package ru.sidorovroman.week;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sidorovroman on 12.10.15.
 */
public class Prefs {

    public static final String PREFS_FILE = "mysettings";
    private static final String LAST_TASKS_CHANGES_ID = "last_tasks_changes_id";


    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_FILE, Context.MODE_MULTI_PROCESS);
    }

    public static Long getLastTasksChangesId(Context context) {
        return getPrefs(context).getLong(LAST_TASKS_CHANGES_ID, 0L);
    }

    public static void setLastTasksChangesId(Context context, Long changeId) {
        getPrefs(context).edit().putLong(LAST_TASKS_CHANGES_ID, changeId).apply();
    }

    private static void clearSharedPrefs(Context context) {
        getPrefs(context).edit().clear().apply();
    }

}