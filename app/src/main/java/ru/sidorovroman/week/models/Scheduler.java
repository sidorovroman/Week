package ru.sidorovroman.week.models;

import java.util.List;

/**
 * Created by sidorovroman on 31.10.15.
 */
public class Scheduler extends Model{

    private List<Integer> weekDayIds;
    private Long actionId;
    private Long timeFrom;
    private Long timeTo;

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

    public Long getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Long getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Long timeTo) {
        this.timeTo = timeTo;
    }

    @Override
    public String toString() {
        return "_id: " + getId() + " weekDayIds: " + weekDayIds + " actionId: " + actionId + " timeFrom: " + timeFrom + " timeTo: " + timeTo;
    }
}
