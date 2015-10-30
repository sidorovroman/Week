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

import java.util.Calendar;
import java.util.List;

import ru.sidorovroman.week.db.FeedReaderDbHelper;
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
    private FeedReaderDbHelper feedReaderDbHelper;
    private EditText nameField;

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
                String name = nameField.getText().toString();
                List<String> categories = multiSelectionSpinner.getSelectedStrings();
                String fromString = from.getText().toString();
                String toString = to.getText().toString();

                // создаем объект для данных
                ContentValues cv = new ContentValues();
                // подключаемся к БД
                SQLiteDatabase db = feedReaderDbHelper.getWritableDatabase();
                cv.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_NAME, name);
                cv.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_CATEGORIES, String.valueOf(categories));
                cv.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_FROM, fromString);
                cv.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TO, toString);

                long rowID = db.insert(FeedReaderDbHelper.FeedEntry.TABLE_NAME, null, cv);
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

        feedReaderDbHelper = new FeedReaderDbHelper(this);
    }
    private void readAll() {

        SQLiteDatabase db = feedReaderDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderDbHelper.FeedEntry._ID,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_NAME,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_CATEGORIES,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_FROM,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TO
        };
        Cursor c = db.query(
                FeedReaderDbHelper.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(FeedReaderDbHelper.FeedEntry._ID);
            int nameColIndex = c.getColumnIndex(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_NAME);
            int categoriesColIndex = c.getColumnIndex(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_CATEGORIES);
            int fromColIndex = c.getColumnIndex(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_FROM);
            int toColIndex = c.getColumnIndex(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TO);

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", categories = " + c.getString(categoriesColIndex) +
                                ", from = " + c.getString(fromColIndex) +
                                ", to = " + c.getString(toColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

    }
    //todo можем ли мы использовать один timePicker для разных полей, используя tag?
    private void initTimePicker() {
        Calendar now = Calendar.getInstance();

        timePickerDialogFrom = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        from.setText("" + hourOfDay + ": " + minute);
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
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
    }
}
