package ru.sidorovroman.week.models;

import java.util.List;

/**
 * Created by sidorovroman on 31.10.15.
 */
public class ActionTime extends Model{


    private List<Integer> weekDayIds;
    private Long actionId;
    private Integer timeFrom;
    private Integer timeTo;

    public ActionTime() {
    }

    public ActionTime(Long actionId,List<Integer> weekDayIds, Integer timeFrom, Integer timeTo) {
        this.actionId = actionId;
        this.weekDayIds = weekDayIds;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public List<Integer> getWeekDayIds() {
        return weekDayIds;
    }

    public void setWeekDayIds(List<Integer> weekDayIds) {
        this.weekDayIds = weekDayIds;
    }

    public Integer getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Integer timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Integer getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Integer timeTo) {
        this.timeTo = timeTo;
    }

}
