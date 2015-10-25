package ru.sidorovroman.week;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Created by sidorovroman on 25.10.15.
 */
public class DrawView extends View {

    private final String LOG_TAG = DrawView.class.getSimpleName();
    private final float timeLineWidth;
    private final float timeInterval;
    private final long screenWidth;
    private Paint timeLinePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private Paint busyPaint;

    private int heightOfAction = 100;
    private int startX = 30;

    private long minutesInHour = 60;
    private long hoursInDay = 24;
    private long minutesInday = hoursInDay * minutesInHour;

    public DrawView(Context context, long screenWidth) {
        super(context);
        this.screenWidth = screenWidth;
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
        addTimeLine(canvas,0,7,Color.parseColor("#673AB7"));

    }

    private void addTimeLine(Canvas canvas, int from, int to, int color) {
        busyPaint.setColor(color);
        busyPaint.setAlpha(200);
        busyPaint.setStrokeWidth(40);
        canvas.drawLine(from * timeInterval + startX, heightOfAction - 20, to * timeInterval + startX, heightOfAction - 20, busyPaint);
    }
}
