package ru.sidorovroman.week.models;

import java.util.List;

/**
 * Created by sidorovroman on 31.10.15.
 */
public class Scheduler extends Model{

    private List<Integer> weekDayIds;
    private Long actionId;
    private int timeFrom;
    private int timeTo;

    public Scheduler() {
    }

    public Scheduler(List<Integer> weekDayIds, Long actionId, int timeFrom, int timeTo) {
        this.weekDayIds = weekDayIds;
        this.actionId = actionId;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public List<Integer> getWeekDayIds() {
        return weekDayIds;
    }

    public void setWeekDayIds(List<Integer> weekDayIds) {
        this.weekDayIds = weekDayIds;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
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
        return "_id: " + getId() + " weekDayIds: " + weekDayIds + " actionId: " + actionId + " timeFrom: " + timeFrom + " timeTo: " + timeTo;
    }
}
