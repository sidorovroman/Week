package ru.sidorovroman.week.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ru.sidorovroman.week.R;
import ru.sidorovroman.week.TimeUtil;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.Action;
import ru.sidorovroman.week.models.ActionTime;

/**
 * Created by romansidorov on 18.11.15.
 */
public class ActionTimeAdapter extends BaseAdapter {
    private static final String LOG_TAG = ActionTimeAdapter.class.getSimpleName();
    private final WeekDay day;
    Context ctx;
    LayoutInflater lInflater;
    List<ActionTime> objects;
    WeekDbHelper db;

    public ActionTimeAdapter(Context context, List<ActionTime> products, WeekDay day) {
        ctx = context;
        this.day = day;
        objects = products;
        db = new WeekDbHelper(context);
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.row_action, parent, false);
        }

        TextView nameField = (TextView) view.findViewById(R.id.actionName);
        TextView timeFromField = (TextView) view.findViewById(R.id.timeFrom);
        TextView timeToField = (TextView) view.findViewById(R.id.timeTo);
        ActionTime actionTime = (ActionTime) getItem(position);
        Action action = db.getAction(actionTime.getActionId());
        if(action != null){
            nameField.setText(action.getName());
            timeFromField.setText(TimeUtil.convertTime(actionTime.getTimeFrom()));
            timeToField.setText(TimeUtil.convertTime(actionTime.getTimeTo()));
        }

        ImageView indicatorView = (ImageView) view.findViewById(R.id.task_indicator);

        Calendar c = Calendar.getInstance();

        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int timeInMinutes = hours * 60 + minutes;
        if(WeekDay.getCurrentDayTabIndex() == day.getIndex() && timeInMinutes >= actionTime.getTimeFrom() && timeInMinutes <= actionTime.getTimeTo()){
            indicatorView.setImageResource(R.drawable.action_current);
//            nameField.setTypeface(nameField.getTypeface(), Typeface.BOLD);
            nameField.setTextColor(Color.parseColor("#333333"));
            timeFromField.setTextColor(Color.parseColor("#333333"));
            timeToField.setTextColor(Color.parseColor("#333333"));
        }
        return view;
    }
}