package ru.sidorovroman.week.models;

import java.util.List;

/**
 * Created by sidorovroman on 31.10.15.
 */
public class Action extends Model{

    private String name;
    private List<Integer> categoryIds;
    private List<ActionTime> actionTimeList;

    public Action() {
    }

    public Action(String name, List<Integer> categoryIds,List<ActionTime> actionTimes) {
        this.name = name;
        this.categoryIds = categoryIds;
        this.actionTimeList = actionTimes;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ActionTime> getActionTimeList() {
        return actionTimeList;
    }

    public void setActionTimeList(List<ActionTime> actionTimeList) {
        this.actionTimeList = actionTimeList;
    }

    @Override
    public String toString() {
        return "_id: " + getId() + " name: " + name + " categoryIds: " + categoryIds + " actionTimes: " + actionTimeList.toString();
    }
}
