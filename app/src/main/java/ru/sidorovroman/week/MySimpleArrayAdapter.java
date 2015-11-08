package ru.sidorovroman.week;

/**
 * Created by sidorovroman on 07.11.15.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.sidorovroman.week.models.Action;

public class MySimpleArrayAdapter extends ArrayAdapter<Action> {

    private  Context context;
    private  List<Action> values;

    public MySimpleArrayAdapter(Context context, List<Action> values) {
        super(context, R.layout.text_row_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.text_row_item, parent, false);

        TextView nameField = (TextView) rowView.findViewById(R.id.name);
        nameField.setText(values.get(position).getName());

        return rowView;
    }
}
