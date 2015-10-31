package ru.sidorovroman.week.enums;

/**
 * Created by sidorovroman on 31.10.15.
 */
public enum WeekDay {

    MONDAY(0,"Понедельник"),
    TUESDAY(1,"Вторник"),
    WEDNESDAY(2,"Среда"),
    THURSDAY(3,"Четверг"),
    FRIDAY(4,"Пятница"),
    SATURDAY(5,"Суббота"),
    SUNDAY(6,"Воскресенье");


    private int index;
    private String label;

    WeekDay(int index, String label) {
        this.index = index;
        this.label = label;
    }

    public int getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    public static WeekDay getDayByIndex(Integer dayIndex) {
        for (WeekDay day : values()) {
            if(dayIndex.equals(day.getIndex())){
                return day;
            }
        }

        throw new NullPointerException("Нет такого дня? что-то напутал значит");
    }
}
