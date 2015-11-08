package ru.sidorovroman.week.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.List;

import ru.sidorovroman.week.MySimpleArrayAdapter;
import ru.sidorovroman.week.R;
import ru.sidorovroman.week.activity.ActionActivity;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.models.Action;

/**
 * Created by sidorovroman on 01.11.15.
 */
public class ActionsFragment extends Fragment {

    private List<Action> allActions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allActions = new WeekDbHelper(getActivity()).getAllActions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fr_actions, container, false);

        ListView actionsList = (ListView) inflate.findViewById(R.id.actionsList);

        MySimpleArrayAdapter mAdapter = new MySimpleArrayAdapter(getActivity(), allActions);
        actionsList.setAdapter(mAdapter);
        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Action action = allActions.get(position);

                Intent intent = new Intent(getContext(), ActionActivity.class);
                intent.putExtra(ActionActivity.ACTION_ID_KEY, action.getId());
                startActivity(intent);

            }
        });
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
