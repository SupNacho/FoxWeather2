package fw.supernacho.ru.foxweather.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuperNacho on 09.12.2017.
 */

public class DayPrediction {
    private List<HourWeather> hours;
    private int dayIcoId;
    private long dayDt;
    private double dayTemp;

    public DayPrediction(int dayIcoId, long dayDt, double dayTemp) {
        this.dayIcoId = dayIcoId;
        this.dayDt = dayDt;
        this.dayTemp = dayTemp;
        hours = new ArrayList<>();
    }

    public DayPrediction() {
        hours = new ArrayList<>();
    }

    public List<HourWeather> getHours() {
        return hours;
    }

    public int getDayIcoId() {
        return dayIcoId;
    }

    public long getDayDt() {
        return dayDt;
    }

    public double getDayTemp() {
        return dayTemp;
    }
}
