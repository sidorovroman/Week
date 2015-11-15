package ru.sidorovroman.week.models;

import java.util.List;

/**
 * Created by sidorovroman on 31.10.15.
 */
public class ActionTime extends PseudoModel{


    private List<Integer> weekDayIds;
    private int timeFrom;
    private int timeTo;

    public ActionTime() {
    }

    public ActionTime(List<Integer> weekDayIds, int timeFrom, int timeTo) {
        this.weekDayIds = weekDayIds;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public List<Integer> getWeekDayIds() {
        return weekDayIds;
    }

    public void setWeekDayIds(List<Integer> weekDayIds) {
        this.weekDayIds = weekDayIds;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(int timeFrom) {
        this.timeFrom = timeFrom;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(int timeTo) {
        this.timeTo = timeTo;
    }

    @Override
    public String toString() {
        return "pseudoId : " + getPseudoId() + " weekDayIds: " + weekDayIds + " timeFrom: " + timeFrom + " timeTo: " + timeTo;
    }
}
