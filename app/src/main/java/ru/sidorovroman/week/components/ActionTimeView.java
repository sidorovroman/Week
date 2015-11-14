package ru.sidorovroman.week.components;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.sidorovroman.week.R;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.ActionTime;

/**
 * Created by sidorovroman on 10.11.15.
 */
public class ActionTimeView extends LinearLayout {

    private final IActionTime mListener;
    private final ActionTime actionTime;

    public interface IActionTime{
        void onSetTimeTo(int timeToValue);
        void onSetTimeFrom(int timeFromValue);
        void onSetDays(List<Integer> selectedDays);
        void onDelete();
    }

    private final List<Integer> selectedDays = new ArrayList();
    private EditText days;
    private int timeFromValue;
    private int timeToValue;
    private AlertDialog dialog;
    private TimePickerDialog timePickerDialogFrom;
    private TimePickerDialog timePickerDialogTo;
    private TextView from;
    private TextView to;

    public ActionTimeView(Context context, ActionTime actionTime, final FragmentManager fragmentManager, IActionTime listener) {
        super(context);
        this.mListener = listener;
        this.actionTime = actionTime;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_action_time, this, true);
        days = (EditText) findViewById(R.id.days);
        from = (TextView) findViewById(R.id.from);
        to = (TextView) findViewById(R.id.to);
        ImageView btnClear = (ImageButton) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDelete();
                }
            }
        });
        String text = "";
        for (Integer dayIndex : actionTime.getWeekDayIds()) {
            WeekDay day = WeekDay.getDayByIndex(dayIndex);
            text += day.getLabel();
        }

        days.setText(text);
        from.setText(actionTime.getTimeFrom() / 60 + ":" + actionTime.getTimeFrom() % 60);
        to.setText(actionTime.getTimeTo()/60 + ":" + actionTime.getTimeTo()%60);

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogFrom.show(fragmentManager, "TimePickerDialogFrom");
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogTo.show(fragmentManager, "TimePickerDialogTo");
            }
        });

        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaysDialog();
            }
        });
        initTimePicker();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

                        if (mListener != null) {
                            mListener.onSetDays(selectedDays);
                        }

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
    private void initTimePicker() {
        Calendar now = Calendar.getInstance();

        timePickerDialogFrom = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        from.setText("" + hourOfDay + ": " + minute);
                        timeFromValue = hourOfDay * 60 + minute;
                        if (mListener != null) {
                            mListener.onSetTimeFrom(timeFromValue);
                        }
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
                        to.setText("" + hourOfDay + ": " + minute);
                        timeToValue = hourOfDay * 60 + minute;
                        if (mListener != null) {
                            mListener.onSetTimeTo(timeToValue);
                        }
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
    }
}
