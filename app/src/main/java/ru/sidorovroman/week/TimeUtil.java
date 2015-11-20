package ru.sidorovroman.week;

/**
 * Created by sidorovroman on 20.11.15.
 */
public class TimeUtil {
    /**
     * на вход время в минутах
     * на выход в формате 00:00
     *
     * @return
     */
    public static String convertTime(int time) {

        int hours = time / 60;
        int minutes = time % 60;
        String sHours = hours < 10 ? "0" + hours : String.valueOf(hours);
        String sMinutes = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        return sHours + ":" + sMinutes;
    }
}
