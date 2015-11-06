package ru.sidorovroman.week.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.sidorovroman.week.R;
import ru.sidorovroman.week.ViewPagerAdapter;
import ru.sidorovroman.week.activity.ActionActivity;
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
                startActivity(new Intent(getContext(), ActionActivity.class));
            }
        });

        return inflate;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (WeekDay day : WeekDay.values()) {
            adapter.addFragment(new DayFragment(day), day.getLabel());
        }
        viewPager.setAdapter(adapter);
    }

}
