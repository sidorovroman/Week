package ru.sidorovroman.week.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.sidorovroman.week.adapters.ActionTimeAdapter;
import ru.sidorovroman.week.DrawView;
import ru.sidorovroman.week.R;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.ActionTime;


public class DayFragment extends Fragment{

    private static final String LOG_TAG = DayFragment.class.getSimpleName();
    private WeekDay day;
    private RelativeLayout timeLineContainer;
    private ListView scheduler;

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
        scheduler = (ListView) inflate.findViewById(R.id.scheduler);

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

        List<ActionTime> actionTimes = weekDbHelper.getActionTimesByWeekDay(day);
        Collections.sort(actionTimes, new Comparator<ActionTime>() {
            @Override
            public int compare(final ActionTime object1, final ActionTime object2) {
                return object1.getTimeFrom().compareTo(object2.getTimeFrom());
            }
        });
        ActionTimeAdapter actionTimeAdapter = new ActionTimeAdapter(getActivity(), actionTimes, day);
        scheduler.setAdapter(actionTimeAdapter);
    }
}
