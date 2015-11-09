package ru.sidorovroman.week.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ru.sidorovroman.week.DrawView;
import ru.sidorovroman.week.R;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.ActionTime;


public class DayFragment extends Fragment{

    private static final String LOG_TAG = DayFragment.class.getSimpleName();
    private WeekDay day;
    private RelativeLayout timeLineContainer;
    private LinearLayout scheduler;

    public DayFragment( ) {
    }

    public DayFragment(WeekDay day) {
        this.day = day;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG,day.getLabel());
        View inflate = inflater.inflate(R.layout.fr_day, container, false);

        timeLineContainer = (RelativeLayout) inflate.findViewById(R.id.timeline);
        scheduler = (LinearLayout) inflate.findViewById(R.id.scheduler);

        DrawView drawView = new DrawView(getActivity(), getScreenWidth());
        timeLineContainer.addView(drawView);
        readAll();

        return inflate;
    }

    private long getScreenWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private void readAll() {
        WeekDbHelper weekDbHelper = new WeekDbHelper(getActivity());

        List<ActionTime> schedulerByWeekDay = weekDbHelper.getActionTimesByWeekDay(day);

        for (ActionTime s : schedulerByWeekDay) {
            Log.d(LOG_TAG,s.toString());

            TextView tv = new TextView(getActivity());
            tv.setText(s.toString());
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            scheduler.addView(tv);
        }
    }
}
