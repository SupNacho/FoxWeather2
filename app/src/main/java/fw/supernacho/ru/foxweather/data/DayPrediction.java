package fw.supernacho.ru.foxweather.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fw.supernacho.ru.foxweather.data.weather.Wind;

/**
 * Created by SuperNacho on 09.12.2017.
 */

public class DayPrediction {
    private List<HourWeather> hours;
    private int dayIcoId;
    private long dayDt;
    private double dayTemp;
    private int humidity;
    private double pressure;
    private List<Wind> winds;
//Добавить Влажность, Давление, ветры и температуру (утро, день, вечер, ночь)?
    public DayPrediction(int dayIcoId, long dayDt, double dayTemp) {
        this.dayIcoId = dayIcoId;
        this.dayDt = dayDt;
        this.dayTemp = dayTemp;
        hours = new ArrayList<>();
        winds = new ArrayList<>();
        humidity = 60;
        pressure = 755.0;
    }
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

    public double getDayTemp() {
        return dayTemp;
    }
    public String getStringDayTemp() {
        return String.format(Locale.ENGLISH,"%.1f", dayTemp);
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return String.format(Locale.ENGLISH,"%.1f", pressure);
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public List<Wind> getWinds() {
        return winds;
    }

    public void setWinds(List<Wind> winds) {
        this.winds = winds;
    }
}
