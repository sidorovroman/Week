package ru.sidorovroman.week.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.sidorovroman.week.R;
import ru.sidorovroman.week.components.ActionTimeView;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.enums.Category;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.Action;
import ru.sidorovroman.week.models.ActionTime;

/**
 * Created by sidorovroman on 26.10.15.
 */
public class ActionDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = ActionDetailActivity.class.getSimpleName();
    public static final String ACTION_ID_KEY = "action_id_key";
    private EditText multiSelectionSpinner;
    private final List<Integer> selectedCategories = new ArrayList();
    private List<Integer> selectedDaysTemp;
    private TimePickerDialog timePickerDialogFrom;
    private TimePickerDialog timePickerDialogTo;

    private WeekDbHelper db;
    private EditText nameField;
    private LinearLayout actionsTimeContainer;


    private AlertDialog dialog;
    private Long actionId;
    private int timeFromTemp;
    private int timeToTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new WeekDbHelper(this);

        multiSelectionSpinner = (EditText) findViewById(R.id.mySpinner);
        nameField = (EditText) findViewById(R.id.name);
        actionsTimeContainer = (LinearLayout) findViewById(R.id.actionsTimeContainer);

        initTimePicker();

        actionId = getIntent().getLongExtra(ACTION_ID_KEY, 0);
        if (actionId != 0) {
            Toast.makeText(this, "Изменение ", Toast.LENGTH_SHORT).show();
            Action action = db.getAction(actionId);
            nameField.setText(action.getName());
            selectedCategories.addAll(action.getCategoryIds());
            String text = "";

            for (Integer dayIndex : selectedCategories) {
                Category category = Category.getCategoryByIndex(dayIndex);
                text += category.getLabel();
            }

            multiSelectionSpinner.setText(text);
            for (ActionTime actionTime : db.getActionTimes(actionId)) {
                addActionTimeComponentView(actionTime);
            }
        } else {
            Toast.makeText(this, "Создание ", Toast.LENGTH_SHORT).show();
        }

        nameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !nameField.getText().toString().isEmpty()){
                    Action action = new Action(nameField.getText().toString(), selectedCategories);

                    if (actionId == 0) {
                        //create action
                        actionId = db.addAction(action);
                    } else {
                        // update action
                        action.setId(actionId);
                        db.updateAction(action);
                    }
                }
            }
        });


        multiSelectionSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoriesDialog();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDaysDialogNew();
            }
        });
    }


    @Override
    protected void onPause() {
        String name = nameField.getText().toString();
        if(!name.isEmpty()){
            Action action = new Action(nameField.getText().toString(), selectedCategories);
            if (actionId == 0) {
                //create action
                actionId = db.addAction(action);
            } else {
                // update action
                action.setId(actionId);
                db.updateAction(action);
            }
        }
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDaysDialogNew() {
        final Context context = this;
        WeekDay[] values = WeekDay.values();
        CharSequence[] items = new CharSequence[values.length];
        for (int i = 0; i < values.length; i++) {
            items[i] = values[i].getLabel();
        }
        boolean[] checkedItems = new boolean[values.length];
//        for (Integer catIndex : selectedDays) {
//            checkedItems[catIndex] = true;
//        }

        selectedDaysTemp = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select The Difficulty Level");
        builder.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of item (of which checkbox checked)
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            selectedDaysTemp.add(indexSelected);
                        } else if (selectedDaysTemp.contains(indexSelected)) {
                            selectedDaysTemp.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(context,"days:" + selectedDaysTemp.toString(),Toast.LENGTH_SHORT).show();
                        timePickerDialogFrom.show(getFragmentManager(), "TimePickerDialogFrom");

//                        String text = "";
//                        for (Integer dayIndex : selectedDaysTemp) {
//                            WeekDay day = WeekDay.getDayByIndex(dayIndex);
//                            text += day.getLabel();
//                        }
//
//                        days.setText(text);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedDaysTemp.clear();
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

    //todo можем ли мы использовать один timePicker для разных полей, используя tag?
    private void initTimePicker() {
        final Context context = this;
        Calendar now = Calendar.getInstance();

        timePickerDialogFrom = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        timeFromTemp = hourOfDay * 60 + minute;
                        timePickerDialogTo.show(getFragmentManager(), "TimePickerDialogTo");
                        Toast.makeText(context, "from:" + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();

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
                        timeToTemp = hourOfDay * 60 + minute;
                        Toast.makeText(context,"To:" + hourOfDay + ":" + minute,Toast.LENGTH_SHORT).show();
                        addActionTime();
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
    }

    private void addActionTime() {
        ActionTime actionTime = new ActionTime(actionId,selectedDaysTemp,timeFromTemp,timeToTemp);
        db.addActionTime(actionTime);
        addActionTimeComponentView(actionTime);
    }

    private void addActionTimeComponentView(ActionTime actionTime) {
        ActionTimeView actionTimeView = new ActionTimeView(this, actionTime, getFragmentManager());
        actionsTimeContainer.addView(actionTimeView);
    }

}
