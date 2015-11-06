package ru.sidorovroman.week.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.sidorovroman.week.CustomAdapter;
import ru.sidorovroman.week.R;
import ru.sidorovroman.week.activity.ActionActivity;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.models.Action;

/**
 * Created by sidorovroman on 01.11.15.
 */
public class ActionsFragment extends Fragment {

    protected RecyclerView.LayoutManager mLayoutManager;
    private List<Action> allActions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allActions = new WeekDbHelper(getActivity()).getAllActions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fr_actions, container, false);

        mLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView actionsList = (RecyclerView) inflate.findViewById(R.id.actionsList);
        actionsList.setHasFixedSize(true);
        CustomAdapter mAdapter = new CustomAdapter(allActions);
        actionsList.setLayoutManager(mLayoutManager);
        actionsList.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) inflate.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ActionActivity.class));
            }
        });

        return inflate;
    }
}
