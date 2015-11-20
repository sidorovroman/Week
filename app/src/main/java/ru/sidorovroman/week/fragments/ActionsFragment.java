package ru.sidorovroman.week.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.List;

import ru.sidorovroman.week.MySimpleArrayAdapter;
import ru.sidorovroman.week.R;
import ru.sidorovroman.week.activity.ActionDetailActivity;
import ru.sidorovroman.week.db.WeekDbHelper;
import ru.sidorovroman.week.models.Action;
/**
 * Created by sidorovroman on 01.11.15.
 */
public class ActionsFragment extends Fragment {

    private static final String FRAGMENT_EXTRA_KEY = "fragment_extra_key";
    public static final String ACTIVITY_ID_KEY = "activity_id_key";
    private static final String LOG_TAG = ActionsFragment.class.getSimpleName();
    private List<Action> allActions;

    private boolean listForReturnActionId = false;
    private ListView actionsList;
    private WeekDbHelper db;

    public static Fragment newInstance(boolean returnId){
        ActionsFragment actionsFragment = new ActionsFragment();

        Bundle args = new Bundle();
        args.putBoolean(FRAGMENT_EXTRA_KEY, returnId);
        actionsFragment.setArguments(args);

        return actionsFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle arguments = getArguments();
        if(arguments != null) {
            listForReturnActionId = arguments.getBoolean(FRAGMENT_EXTRA_KEY, false);
        }
        db = new WeekDbHelper(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        allActions = db.getActions();

        final MySimpleArrayAdapter mAdapter = new MySimpleArrayAdapter(getActivity(), allActions);
        actionsList.setAdapter(mAdapter);
        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Action action = allActions.get(position);

                if (listForReturnActionId) {
                    Intent intent = new Intent();
                    intent.putExtra(ACTIVITY_ID_KEY, action.getId());
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    openActionDetailActivity(action.getId());
                }
            }
        });
        actionsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Action action = allActions.get(position);

                final CharSequence[] items = {"Редактировать","Удалить"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            openActionDetailActivity(action.getId());
                        } else  if(item == 1){
                            db.removeAction(action.getId());
                            allActions.remove(action);
                            mAdapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });
    }

    private void openActionDetailActivity(long id) {

        Intent intent = new Intent(getContext(), ActionDetailActivity.class);
        intent.putExtra(ActionDetailActivity.ACTION_ID_KEY, id);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fr_actions, container, false);

        actionsList = (ListView) inflate.findViewById(R.id.actionsList);

        FloatingActionButton fab = (FloatingActionButton) inflate.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ActionDetailActivity.class));
            }
        });

        return inflate;
    }
}
