package ru.sidorovroman.week.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ru.sidorovroman.week.DrawView;
import ru.sidorovroman.week.R;


public class DayFragment extends Fragment{

    private RelativeLayout timeLineContainer;

    public DayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fr_day, container, false);


        timeLineContainer = (RelativeLayout) inflate.findViewById(R.id.timeline);

        DrawView drawView = new DrawView(getActivity(), getScreenWidth());
        timeLineContainer.addView(drawView);

        return inflate;
    }

    private long getScreenWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

}
