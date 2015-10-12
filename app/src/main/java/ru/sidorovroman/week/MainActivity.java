package ru.sidorovroman.week;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity{


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RelativeLayout timeLineContainer;
    private TimePickerDialog timePickerDialogFrom;
    private TimePickerDialog timePickerDialogTo;
    private Dialog addActionDialog;
    private TextView from;
    private TextView to;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initTimePicker();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        timeLineContainer = (RelativeLayout) findViewById(R.id.timeline);
        DrawView drawView = new DrawView(this);
        timeLineContainer.addView(drawView);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    private void showAlertDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.dialog_add_action, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Добавление деятельности")
                .setView(view)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })

                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        addActionDialog = builder.create();

        EditText name = (EditText) view.findViewById(R.id.name);
        from = (TextView) view.findViewById(R.id.from);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogFrom.show(getFragmentManager(), "TimePickerDialogFrom");
            }
        });
        to = (TextView) view.findViewById(R.id.to);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogTo.show(getFragmentManager(), "TimePickerDialogTo");
            }
        });
        addActionDialog.show();
    }
//todo можем ли мы использовать один timePicker для разных полей, используя tag?
    private void initTimePicker() {
        Calendar now = Calendar.getInstance();

        timePickerDialogFrom = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        from.setText(""+hourOfDay + ": " + minute);
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
                        to.setText(""+hourOfDay + ": " + minute);
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class DrawView extends View {

        private final String LOG_TAG = DrawView.class.getSimpleName();
        private final float timeLineWidth;
        private final float timeInterval;
        private Paint timeLinePaint;
        private Paint pointPaint;
        private Paint textPaint;
        private Paint busyPaint;

        private int heightOfAction = 100;
        private int startX = 30;

        private long minutesInHour = 60;
        private long hoursInDay = 24;
        private long minutesInday = hoursInDay * minutesInHour;

        public DrawView(Context context) {
            super(context);
            timeLinePaint = new Paint();
            textPaint = new Paint();
            pointPaint = new Paint();
            busyPaint = new Paint();

            timeLineWidth = getScreenWidth() * 0.8f;
            timeInterval = timeLineWidth / hoursInDay;

            Log.d(LOG_TAG, "getScreenWidth:" + getScreenWidth());
            Log.d(LOG_TAG, "timeLineWidth:" + timeLineWidth);
            Log.d(LOG_TAG, "timeInterval:" + timeInterval);

        }

        @Override
        protected void onDraw(Canvas canvas) {

            timeLinePaint.setColor(Color.LTGRAY);
            timeLinePaint.setStrokeWidth(3);
            canvas.drawLine(startX, heightOfAction, timeLineWidth + startX, heightOfAction, timeLinePaint);

            pointPaint.setColor(Color.LTGRAY);
            pointPaint.setStrokeWidth(6);

            textPaint.setTextSize(18);
            textPaint.setColor(Color.DKGRAY);
            float pointX;

            for(int i = 0; i <= hoursInDay; i++ ){
                pointX = i * timeInterval + startX;
                canvas.drawPoint(pointX, heightOfAction, pointPaint);
                canvas.drawText(String.valueOf(i), pointX, heightOfAction + 20, textPaint);
            }
            addTimeLine(canvas,0,7,Color.parseColor("#673AB7"));

        }

        private void addTimeLine(Canvas canvas, int from, int to, int color) {
            busyPaint.setColor(color);
            busyPaint.setAlpha(200);
            busyPaint.setStrokeWidth(40);
            canvas.drawLine(from * timeInterval + startX, heightOfAction - 20, to * timeInterval + startX, heightOfAction - 20, busyPaint);
        }
    }

    private long getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
