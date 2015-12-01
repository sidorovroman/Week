package ru.sidorovroman.week.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.sidorovroman.week.R;
import ru.sidorovroman.week.ViewPagerAdapter;
import ru.sidorovroman.week.activity.ActionsListActivity;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.enums.WeekDay;
import ru.sidorovroman.week.models.ActionTime;

/**
 * Created by sidorovroman on 01.11.15.
 */
public class DaysFragment extends Fragment {

    private TimePickerDialog timePickerDialogFrom;
    private TimePickerDialog timePickerDialogTo;
    private int timeFromTemp = 0;
    private int timeToTemp = 0;
    private WeekDbHelper weekDbHelper;
    private long tempActionId;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fr_days, container, false);
        weekDbHelper = new WeekDbHelper(getActivity());
        initTimePickers();
        viewPager = (ViewPager) inflate.findViewById(R.id.viewpager);
        setupViewPager();

        TabLayout tabLayout = (TabLayout) inflate.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) inflate.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), ActionsListActivity.class), 1);
            }
        });

        return inflate;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;

        tempActionId = data.getLongExtra(ActionsFragment.ACTIVITY_ID_KEY, 0);
        timePickerDialogFrom.show(getActivity().getFragmentManager(), "TimePickerDialogFrom");
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (WeekDay day : WeekDay.values()) {
            adapter.addFragment(new DayFragment(day), day.getLabel());
        }
        viewPager.setAdapter(adapter);

        int currentTabIndex = WeekDay.getCurrentDayTabIndex();
        viewPager.setCurrentItem(currentTabIndex);
    }

    private void initTimePickers() {
        Calendar now = Calendar.getInstance();

        timePickerDialogFrom = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        timeFromTemp = hourOfDay * 60 + minute;
                        timePickerDialogTo.show(getActivity().getFragmentManager(), "TimePickerDialogTo");
                        Toast.makeText(getActivity(), "from:" + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();

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
                        int time = hourOfDay * 60 + minute;
                        if (time >= timeFromTemp) {
                            timeToTemp = time;
                            addActionTimeToAction();
                        } else {
                            Toast.makeText(getActivity(), "Время окончания деятельности должно быть после времени начала", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
    }

    private void addActionTimeToAction() {
        List<Integer> weekDayIds = new ArrayList<>();
        weekDayIds.add(viewPager.getCurrentItem());
        ActionTime actionTime = new ActionTime(tempActionId, weekDayIds, timeFromTemp, timeToTemp);
        weekDbHelper.addActionTime(actionTime);

        updateTabsContent();
    }

    private void updateTabsContent() {
        int tempItem = viewPager.getCurrentItem();

        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(tempItem);
    }
}
