package ru.sidorovroman.week.activity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.R;
import ru.sidorovroman.week.enums.Category;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.Action;
import ru.sidorovroman.week.models.Scheduler;

/**
 * Created by sidorovroman on 26.10.15.
 */
public class ActionActivity extends AppCompatActivity {

    private static final String LOG_TAG = ActionActivity.class.getSimpleName();
    public static final String ACTION_ID_KEY = "action_id_key";
    private EditText multiSelectionSpinner;
    private EditText days;
    private final List<Integer> selectedCategories = new ArrayList();
    private final List<Integer> selectedDays = new ArrayList();
    private TimePickerDialog timePickerDialogFrom;
    private TimePickerDialog timePickerDialogTo;
    private TextView from;
    private TextView to;
    private WeekDbHelper weekDbHelper;
    private EditText nameField;
    private int timeFromValue;
    private int timeToValue;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        weekDbHelper = new WeekDbHelper(this);

        multiSelectionSpinner = (EditText) findViewById(R.id.mySpinner);
        days = (EditText) findViewById(R.id.days);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        nameField = (EditText) findViewById(R.id.name);
        from = (TextView) findViewById(R.id.from);
        to = (TextView) findViewById(R.id.to);

        initTimePicker();

        long actionId = getIntent().getLongExtra(ACTION_ID_KEY,0);
        if(actionId != 0){
            Toast.makeText(this,"Изменение ", Toast.LENGTH_SHORT).show();
            Action action = weekDbHelper.getActionById(actionId);
            nameField.setText(action.getName());
            List<Scheduler> schedulerList = weekDbHelper.getSchedulerByActionId(actionId);
            //todo scheduler
        }else{
            Toast.makeText(this,"Создание ", Toast.LENGTH_SHORT).show();
        }


        multiSelectionSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoriesDialog();
            }
        });

        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaysDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = weekDbHelper.getWritableDatabase();

                ContentValues cv = prepareActionEntries();
                long actionId = db.insert(WeekDbHelper.ActionEntry.TABLE_NAME, null, cv);

                cv = prepareSchedulerEntries(actionId);
                db.insert(WeekDbHelper.SchedulerEntry.TABLE_NAME, null, cv);
                db.close();
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogFrom.show(getFragmentManager(), "TimePickerDialogFrom");
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogTo.show(getFragmentManager(), "TimePickerDialogTo");
            }
        });
    }

    private void openDaysDialog() {
        WeekDay[] values = WeekDay.values();
        CharSequence[] items = new CharSequence[values.length];
        for (int i = 0; i < values.length; i++) {
            items[i] = values[i].getLabel();
        }
        boolean[] checkedItems = new boolean[values.length];
        for (Integer catIndex : selectedDays) {
            checkedItems[catIndex] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select The Difficulty Level");
        builder.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of item (of which checkbox checked)
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            selectedDays.add(indexSelected);
                        } else if (selectedDays.contains(indexSelected)) {
                            selectedDays.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String text = "";
                        for (Integer dayIndex : selectedDays) {
                            WeekDay day = WeekDay.getDayByIndex(dayIndex);
                            text += day.getLabel();
                        }

                        days.setText(text);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel

                    }
                });

        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();
    }

    private void openCategoriesDialog() {
        Category[] values = Category.values();
        CharSequence[] items = new CharSequence[values.length];
        for (int i = 0; i < values.length; i++) {
            items[i] = values[i].getLabel();
        }

        boolean[] checkedItems = new boolean[values.length];
        for (Integer catIndex : selectedCategories) {
            checkedItems[catIndex] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select The Difficulty Level");
        builder.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of item (of which checkbox checked)
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            selectedCategories.add(indexSelected);
                        } else if (selectedCategories.contains(indexSelected)) {
                            selectedCategories.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        String text = "";
                        for (Integer dayIndex : selectedCategories) {
                            Category category = Category.getCategoryByIndex(dayIndex);
                            text += category.getLabel();
                        }

                        multiSelectionSpinner.setText(text);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel

                    }
                });

        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();
    }

    private ContentValues prepareSchedulerEntries(long actionId) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        cv.put(WeekDbHelper.SchedulerEntry.COLUMN_WEEK_DAY_IDS, new JSONArray(selectedDays).toString());
        cv.put(WeekDbHelper.SchedulerEntry.COLUMN_ACTION_ID,actionId);
        cv.put(WeekDbHelper.SchedulerEntry.COLUMN_TIME_FROM,timeFromValue);
        cv.put(WeekDbHelper.SchedulerEntry.COLUMN_TIME_TO,timeToValue);

        return cv;
    }

    private ContentValues prepareActionEntries() {

        String name = nameField.getText().toString();
        JSONArray categoriesJsonArray = new JSONArray(selectedCategories);

        // создаем объект для данных
        ContentValues cv = new ContentValues();
        cv.put(WeekDbHelper.ActionEntry.COLUMN_NAME, name);
        cv.put(WeekDbHelper.ActionEntry.COLUMN_CATEGORY_IDS, categoriesJsonArray.toString());

        return cv;
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
