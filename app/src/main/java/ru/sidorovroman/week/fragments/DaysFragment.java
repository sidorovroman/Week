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

import java.util.Calendar;

import ru.sidorovroman.week.R;
import ru.sidorovroman.week.ViewPagerAdapter;
import ru.sidorovroman.week.activity.ActionActivity;
import ru.sidorovroman.week.activity.ActionsActivity;
import ru.sidorovroman.week.enums.WeekDay;

/**
 * Created by sidorovroman on 01.11.15.
 */
public class DaysFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fr_days, container, false);
        ViewPager viewPager = (ViewPager) inflate.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) inflate.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) inflate.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), ActionsActivity.class), 1);
            }
        });

        return inflate;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;

        Long actionId = data.getLongExtra(ActionsFragment.ACTIVITY_ID_KEY, 0);
        Toast.makeText(getActivity(), "Обратно:" + actionId, Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (WeekDay day : WeekDay.values()) {
            adapter.addFragment(new DayFragment(day), day.getLabel());
        }
        viewPager.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        // Sunday = 1. Saturday = 7; tabIndex monday = 0, sunday = 6
        // that's why -2

        int tabIndex = day - 2;

        if(tabIndex == -1) // this is sunday
            tabIndex = 6;  // this tab index for sunday

        viewPager.setCurrentItem(tabIndex);
    }

}
