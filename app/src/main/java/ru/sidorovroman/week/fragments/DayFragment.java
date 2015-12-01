package ru.sidorovroman.week.fragments;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.sidorovroman.week.adapters.ActionTimeAdapter;
import ru.sidorovroman.week.TimeLineView;
import ru.sidorovroman.week.R;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.ActionTime;


public class DayFragment extends Fragment{

    private static final String LOG_TAG = DayFragment.class.getSimpleName();
    private WeekDbHelper db;
    private WeekDay day;
    private RelativeLayout timeLineContainer;
    private ListView scheduler;
    private ActionTimeAdapter actionTimeAdapter;
    private List<ActionTime> actionTimes;

    public DayFragment( ) {
    }

    public DayFragment(WeekDay day) {
        this.day = day;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG,day.getLabel());
        View inflate = inflater.inflate(R.layout.fr_day, container, false);

        db = new WeekDbHelper(getActivity());

        actionTimes = getActions();

        initTimeLine(inflate);

        scheduler = (ListView) inflate.findViewById(R.id.scheduler);


        for (ActionTime a : actionTimes) {
            Log.d(LOG_TAG, "ac:"+a);
        }


        actionTimeAdapter = new ActionTimeAdapter(getActivity(), actionTimes, day);
        scheduler.setAdapter(actionTimeAdapter);

        scheduler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ActionTime actionTime = actionTimes.get(position);
                final CharSequence[] items = {"Редактировать","Удалить"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            Toast.makeText(getActivity(),"Редактирование надо реализовать",Toast.LENGTH_LONG).show();
//                            openActionDetailActivity(action.getId());
                        } else  if(item == 1){
                            db.removeActionTime(actionTime.getId());
                            actionTimes.remove(actionTime);
                            updateActionList();
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return inflate;
    }

    private void initTimeLine(View inflate) {
        timeLineContainer = (RelativeLayout) inflate.findViewById(R.id.timeline);
        timeLineContainer.addView(new TimeLineView(getActivity(), actionTimes));
    }
    private void redrawTimeLine(){
        timeLineContainer.removeAllViews();
        timeLineContainer.addView(new TimeLineView(getActivity(), actionTimes));
    }

    private void updateActionList() {
        actionTimeAdapter.notifyDataSetChanged();
        redrawTimeLine();
    }

    private long getScreenWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private List<ActionTime> getActions() {
        WeekDbHelper weekDbHelper = new WeekDbHelper(getActivity());

        List<ActionTime> actionTimes = weekDbHelper.getActionTimesByWeekDay(day);
        Collections.sort(actionTimes, new Comparator<ActionTime>() {
            @Override
            public int compare(final ActionTime object1, final ActionTime object2) {
                return object1.getTimeFrom().compareTo(object2.getTimeFrom());
            }
        });

        return actionTimes;
    }
}
