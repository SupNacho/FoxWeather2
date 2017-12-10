package fw.supernacho.ru.foxweather.prefs;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.ArraySet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by SuperNacho on 19.11.2017.
 */

public class WeatherPreference {
    private static final String APP_PREFFERENCES_CITY = "city";
    private static final String APP_PREFFERENCES_CITIES = "cities";
    private static final String APP_PREFFERENCES_ISPRESSURE = "checked_pressure";
    private static final String APP_PREFFERENCES_ISTOMORROW = "checked_tomorrow";
    private static final String APP_PREFFERENCES_ISWEEK = "checked_week";
    private static final String MOSCOW = "Moscow";
    private SharedPreferences userPreferences;

    public WeatherPreference(Activity activity) {
        this.userPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return userPreferences.getString(APP_PREFFERENCES_CITY, MOSCOW);
    }

    public Set<String> getCities(){
        Set<String> citySet = new TreeSet<>();
        return userPreferences.getStringSet(APP_PREFFERENCES_CITIES, citySet);
    }

    public boolean[] getSelectors(){
        return new boolean[] {
                userPreferences.getBoolean(APP_PREFFERENCES_ISPRESSURE, false),
                userPreferences.getBoolean(APP_PREFFERENCES_ISTOMORROW, false),
                userPreferences.getBoolean(APP_PREFFERENCES_ISWEEK, false)
        };
    }
    public void setCity(String city){
        userPreferences.edit().putString(APP_PREFFERENCES_CITY, city).apply();
    }
    public void setCities(List<String> cityList){
        Set<String> citySet = new TreeSet<>();
        for (String s : cityList) {
            citySet.add(s);
        }
            userPreferences.edit().putStringSet(APP_PREFFERENCES_CITIES, citySet).apply();
    }
    public void setPressure(boolean pressure){
        userPreferences.edit().putBoolean(APP_PREFFERENCES_ISPRESSURE, pressure).apply();
    }
    public void setTomorrow(boolean tomorrow){
        userPreferences.edit().putBoolean(APP_PREFFERENCES_ISTOMORROW, tomorrow).apply();
    }
    public void setWeek(boolean week){
        userPreferences.edit().putBoolean(APP_PREFFERENCES_ISWEEK, week).apply();
    }
}
