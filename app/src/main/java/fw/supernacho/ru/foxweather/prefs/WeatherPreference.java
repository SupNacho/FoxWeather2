package fw.supernacho.ru.foxweather.prefs;

import android.app.Activity;
import android.content.SharedPreferences;

public class WeatherPreference {
    private static final String APP_PREFFERENCES_CITY = "city";
    private static final String APP_PREFFERENCES_COUNTRY = "country";
    private static final String MOSCOW = "Moscow,RU";
    private SharedPreferences userPreferences;

    public WeatherPreference(Activity activity) {
        this.userPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return userPreferences.getString(APP_PREFFERENCES_CITY, MOSCOW);
    }

    public int getCountry() {
        return userPreferences.getInt(APP_PREFFERENCES_COUNTRY, 181);
    }

    public void setCity(String city){
        userPreferences.edit().putString(APP_PREFFERENCES_CITY, city).apply();
    }

    public void setCountry(int id){
        userPreferences.edit().putInt(APP_PREFFERENCES_COUNTRY, id).apply();
    }
}
