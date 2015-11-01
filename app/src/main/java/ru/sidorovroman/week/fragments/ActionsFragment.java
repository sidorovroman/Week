package ru.sidorovroman.week.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    private WeekDbHelper weekDbHelper;
    private RecyclerView actionsList;
    private static final int DATASET_COUNT = 60;
    private CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fr_actions, container, false);

        mLayoutManager = new LinearLayoutManager(getActivity());

        actionsList = (RecyclerView) inflate.findViewById(R.id.actionsList);
        actionsList.setHasFixedSize(true);

        weekDbHelper = new WeekDbHelper(getActivity());
        List<Action> allActions = weekDbHelper.getAllActions();
        mAdapter = new CustomAdapter(allActions);

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
