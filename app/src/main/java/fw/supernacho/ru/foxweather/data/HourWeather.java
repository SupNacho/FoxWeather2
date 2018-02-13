package fw.supernacho.ru.foxweather.data;

import java.util.Locale;

public class HourWeather {
    private long dt;
    private int id;
    private double temp;

    public HourWeather(long dt, int id, double temp) {
        this.dt = dt;
        this.id = id;
        this.temp = temp;
    }

    public long getDt() {
        return dt;
    }

    public int getId() {
        return id;
    }

    public String getStringTemp() {
        return String.format(Locale.ENGLISH,"%.1f", temp);
    }
}
