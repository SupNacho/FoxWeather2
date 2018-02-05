package fw.supernacho.ru.foxweather.data;

/**
 * Created by SuperNacho on 09.12.2017.
 */

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

    public double getTemp() {
        return temp;
    }
    public String getStringTemp() {
        return String.format("%.1f", temp);
    }
}
