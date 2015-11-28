package ru.sidorovroman.week;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Random;

import ru.sidorovroman.week.models.ActionTime;

/**
 * Created by sidorovroman on 25.10.15.
 */
public class TimeLineView extends View {

    private final String LOG_TAG = TimeLineView.class.getSimpleName();
    private final float timeLineWidth;
    private final float timeInterval;
    private final long screenWidth;
    private final List<ActionTime> actionTimes;
    private Paint timeLinePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private Paint busyPaint;

    private int heightOfAction = 100;
    private int startX = 30;

    private long minutesInHour = 60;
    private long hoursInDay = 24;
    private long minutesInday = hoursInDay * minutesInHour;

    public TimeLineView(Context context, List<ActionTime> actionTimes, long screenWidth) {
        super(context);
        this.screenWidth = screenWidth;
        this.actionTimes = actionTimes;
        timeLinePaint = new Paint();
        textPaint = new Paint();
        pointPaint = new Paint();
        busyPaint = new Paint();

        timeLineWidth = screenWidth * 0.8f;
        timeInterval = timeLineWidth / hoursInDay;

        Log.d(LOG_TAG, "getScreenWidth:" + screenWidth);
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

        Random rnd = new Random();

        for (ActionTime actionTime : actionTimes) {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            addTimeLine(canvas,actionTime.getTimeFrom(),actionTime.getTimeTo(),color);
        }
    }

    private void addTimeLine(Canvas canvas, int timeFrom, int timeTo, int color) {
        int hoursFrom = timeFrom / 60;
        int minutesFrom = timeFrom % 60;
        float timeFromRes = hoursFrom + minutesFrom / 60f;

        int hoursTo = timeTo / 60;
        int minutesTo = timeTo % 60;
        float timeToRes = hoursTo + minutesTo / 60f;

        busyPaint.setColor(color);
        busyPaint.setAlpha(200);
        busyPaint.setStrokeWidth(40);



        canvas.drawLine(
                timeFromRes * timeInterval + startX,
                heightOfAction - 20,
                timeToRes * timeInterval + startX,
                heightOfAction - 20,
                busyPaint);
    }
}
