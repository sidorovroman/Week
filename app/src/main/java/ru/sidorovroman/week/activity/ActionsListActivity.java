package ru.sidorovroman.week.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import ru.sidorovroman.week.R;
import ru.sidorovroman.week.fragments.ActionsFragment;

/**
 * Created by sidorovroman on 26.10.15.
 */
public class ActionsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_action);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, ActionsFragment.newInstance(true))
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("Выберите деятельность");
        }
    }
}
