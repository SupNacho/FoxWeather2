package fw.supernacho.ru.foxweather.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fw.supernacho.ru.foxweather.data.openweather.Wind;

public class DayPrediction {
    private List<HourWeather> hours;
    private int dayIcoId;
    private long dayDt;
    private double dayTemp;
    private int humidity;
    private double pressure;
    private List<Wind> winds;

    public DayPrediction(int dayIcoId, long dayDt, double dayTemp, List<Wind> winds, int humidity, double pressure) {
        this.dayIcoId = dayIcoId;
        this.dayDt = dayDt;
        this.dayTemp = dayTemp;
        this.winds = winds;
        this.humidity = humidity;
        this.pressure = pressure;
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

    public String getStringDayTemp() {
        return String.format(Locale.ENGLISH,"%.1f", dayTemp);
    }

    public int getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return String.format(Locale.ENGLISH,"%.1f", pressure);
    }

    public List<Wind> getWinds() {
        return winds;
    }
}
