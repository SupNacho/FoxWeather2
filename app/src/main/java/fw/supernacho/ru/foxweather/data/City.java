package fw.supernacho.ru.foxweather.data;

/**
 * Created by SuperNacho on 14.12.2017.
 */

public class City {
    private long id;
    private String cityName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
