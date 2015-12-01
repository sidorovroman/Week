package ru.sidorovroman.week;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.sidorovroman.week.models.ActionTime;

/**
 * Created by sidorovroman on 25.10.15.
 */
public class TimeLineView extends View {

    private final String LOG_TAG = TimeLineView.class.getSimpleName();
    private float timeLineWidth;
    private float timeInterval;
    private final List<ActionTime> actionTimes;
    private Paint timeLinePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private Paint busyPaint;

    private int heightOfAction = 100;
    private int startX = 0;

    private long minutesInHour = 60;
    private long hoursInDay = 24;
    private long minutesInday = hoursInDay * minutesInHour;
    private int parentWidth;
    private List<Interval> timeOnLine; // интервалы времени над линией

    public TimeLineView(Context context, List<ActionTime> actionTimes) {
        super(context);
        Log.d(LOG_TAG, "constructor");

        this.actionTimes = actionTimes;
        timeLinePaint = new Paint();
        textPaint = new Paint();
        pointPaint = new Paint();
        busyPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(LOG_TAG, "onMeasure");

        parentWidth = MeasureSpec.getSize(widthMeasureSpec);

        timeLineWidth = parentWidth;// * 0.8f;
        timeInterval = timeLineWidth / hoursInDay;

        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(LOG_TAG, "onDraw");

        timeOnLine = new ArrayList<>();

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

        for (ActionTime a : actionTimes) {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            boolean timeIntervalOverLine = timeOverLine(a.getTimeFrom()) || timeOverLine(a.getTimeTo());
            if (!timeIntervalOverLine) {
                timeOnLine.add(new Interval(a.getTimeFrom(), a.getTimeTo()));
            }
            addTimeLine(canvas, a.getTimeFrom(), a.getTimeTo(), color, !timeIntervalOverLine);
        }
    }
    private boolean timeOverLine(int time){
        for (Interval interval : timeOnLine) {
            if(interval.getFrom() < time && time < interval.getTo()){
                return true;
            }
        }
        return false;
    }

    private void addTimeLine(Canvas canvas, int timeFrom, int timeTo, int color, boolean addOverLine) {

        busyPaint.setColor(color);
        busyPaint.setAlpha(200);
        busyPaint.setStrokeWidth(40);

        int yPos = addOverLine ? heightOfAction - 20 : heightOfAction + 20;
        canvas.drawLine(
                convertTimeToXPos(timeFrom) * timeInterval + startX,
                yPos,
                convertTimeToXPos(timeTo) * timeInterval + startX,
                yPos,
                busyPaint);
    }

    private float convertTimeToXPos(int time){
        int hoursFrom = time / 60;
        int minutesFrom = time % 60;
        return hoursFrom + minutesFrom / 60f;
    }

    static class Interval{

        private int from = 0;
        private int to = 0;

        public Interval(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }
    }

    static class ActionTimeIndexed{

        private int index = 0;

        private ActionTime actionTime;

        public ActionTimeIndexed(ActionTime actionTime) {
            this.actionTime = actionTime;

        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public ActionTime getActionTime() {
            return actionTime;
        }

        public void setActionTime(ActionTime actionTime) {
            this.actionTime = actionTime;
        }

    }
}
