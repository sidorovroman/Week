package ru.sidorovroman.week.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.MultiSelectionSpinner;
import ru.sidorovroman.week.R;

/**
 * Created by sidorovroman on 26.10.15.
 */
public class ActionActivity extends AppCompatActivity {

    private static final String LOG_TAG = ActionActivity.class.getSimpleName();
    private MultiSelectionSpinner multiSelectionSpinner;

    private TimePickerDialog timePickerDialogFrom;
    private TimePickerDialog timePickerDialogTo;
    private TextView from;
    private TextView to;
    private Button btnCancel;
    private Button btnSave;
    private WeekDbHelper weekDbHelper;
    private EditText nameField;
    private int timeFromValue;
    private int timeToValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        String[] array = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(array);
        multiSelectionSpinner.setSelection(new int[]{2, 6});

        initTimePicker();

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = weekDbHelper.getWritableDatabase();

                ContentValues cv = prepareActionEntries();
                db.insert(WeekDbHelper.ActionEntry.TABLE_NAME, null, cv);

                cv = prepareSchedulerEntries();
                db.insert(WeekDbHelper.ShedulerEntry.TABLE_NAME, null, cv);
                readAll();
                db.close();

            }


        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        nameField = (EditText) findViewById(R.id.name);
        from = (TextView) findViewById(R.id.from);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogFrom.show(getFragmentManager(), "TimePickerDialogFrom");
            }
        });
        to = (TextView) findViewById(R.id.to);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogTo.show(getFragmentManager(), "TimePickerDialogTo");
            }
        });

        weekDbHelper = new WeekDbHelper(this);
    }

    private ContentValues prepareSchedulerEntries() {

        List<Integer> weekDays = new ArrayList<>();
        weekDays.add(1);
        weekDays.add(5);
        JSONArray weekDaysJsonArray = new JSONArray(weekDays);

        // создаем объект для данных
        ContentValues cv = new ContentValues();
        //todo
        cv.put(WeekDbHelper.ShedulerEntry.COLUMN_WEEK_DAY_IDS, weekDaysJsonArray.toString());
        //todo
        cv.put(WeekDbHelper.ShedulerEntry.COLUMN_ACTION_ID,12333);
        cv.put(WeekDbHelper.ShedulerEntry.COLUMN_TIME_FROM,timeFromValue);
        cv.put(WeekDbHelper.ShedulerEntry.COLUMN_TIME_TO,timeToValue);

        return cv;
    }

    private ContentValues prepareActionEntries() {

        String name = nameField.getText().toString();
        List<String> categories = multiSelectionSpinner.getSelectedStrings();
        JSONArray categoriesJsonArray = new JSONArray(categories);

        // создаем объект для данных
        ContentValues cv = new ContentValues();
        cv.put(WeekDbHelper.ActionEntry.COLUMN_NAME, name);
        cv.put(WeekDbHelper.ActionEntry.COLUMN_CATEGORY_IDS, categoriesJsonArray.toString());

        return cv;
    }

    private void readAll() {

        SQLiteDatabase db = weekDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                WeekDbHelper.ActionEntry._ID,
                WeekDbHelper.ActionEntry.COLUMN_NAME,
                WeekDbHelper.ActionEntry.COLUMN_CATEGORY_IDS
        };
        Cursor actionCursor = db.query(
                WeekDbHelper.ActionEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (actionCursor.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = actionCursor.getColumnIndex(WeekDbHelper.ActionEntry._ID);
            int nameColIndex = actionCursor.getColumnIndex(WeekDbHelper.ActionEntry.COLUMN_NAME);
            int categoriesColIndex = actionCursor.getColumnIndex(WeekDbHelper.ActionEntry.COLUMN_CATEGORY_IDS);

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + actionCursor.getInt(idColIndex) +
                                ", name = " + actionCursor.getString(nameColIndex) +
                                ", categories = " + actionCursor.getString(categoriesColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (actionCursor.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        actionCursor.close();


        // Define a projection1 that specifies which columns from the database
        // you will actually use after this query.
        String[] projectionScheduler = {
                WeekDbHelper.ShedulerEntry._ID,
                WeekDbHelper.ShedulerEntry.COLUMN_WEEK_DAY_IDS,
                WeekDbHelper.ShedulerEntry.COLUMN_TIME_FROM,
                WeekDbHelper.ShedulerEntry.COLUMN_TIME_TO
        };
        Cursor scedulerCursor = db.query(
                WeekDbHelper.ShedulerEntry.TABLE_NAME,  // The table to query
                projectionScheduler,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (scedulerCursor.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = scedulerCursor.getColumnIndex(WeekDbHelper.ShedulerEntry._ID);
            int weekDaysColIndex = scedulerCursor.getColumnIndex(WeekDbHelper.ShedulerEntry.COLUMN_WEEK_DAY_IDS);
            int fromColIndex = scedulerCursor.getColumnIndex(WeekDbHelper.ShedulerEntry.COLUMN_TIME_FROM);
            int toColIndex = scedulerCursor.getColumnIndex(WeekDbHelper.ShedulerEntry.COLUMN_TIME_TO);

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + scedulerCursor.getInt(idColIndex) +
                                ", weekDays = " + scedulerCursor.getString(weekDaysColIndex) +
                                ", from = " + scedulerCursor.getString(fromColIndex) +
                                ", to = " + scedulerCursor.getString(toColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (scedulerCursor.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        scedulerCursor.close();

    }
    //todo можем ли мы использовать один timePicker для разных полей, используя tag?
    private void initTimePicker() {
        Calendar now = Calendar.getInstance();

        timePickerDialogFrom = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        from.setText("" + hourOfDay + ": " + minute);
                        timeFromValue = hourOfDay * 60 + minute;
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );

        timePickerDialogTo = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        to.setText(""+hourOfDay + ": " + minute);
                        timeToValue = hourOfDay * 60 + minute;
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
    }
}
