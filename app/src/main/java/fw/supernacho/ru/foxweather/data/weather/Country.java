package fw.supernacho.ru.foxweather.data.weather;

/**
 * Created by SuperNacho on 06.02.2018.
 */

public class Country {
    private long id;
    private String countryTitle;
    private String tag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountryTitle() {
        return countryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return countryTitle + ", " + tag;
    }
}
