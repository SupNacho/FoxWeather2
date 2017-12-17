package fw.supernacho.ru.foxweather;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import fw.supernacho.ru.foxweather.data.City;
import fw.supernacho.ru.foxweather.data.CityDataSource;
import fw.supernacho.ru.foxweather.data.DayPrediction;
import fw.supernacho.ru.foxweather.data.WeekPrediction;

/**
 * Created by SuperNacho on 08.12.2017.
 */

public class MainData {
    private static final MainData ourInstance = new MainData();
    private WeekPrediction weekPrediction;
    private DayPrediction dayPerdiction;
    private CityDataSource cityDataSource;
    private MainActivity main;

    public static MainData getInstance() {
        return ourInstance;
    }

    private MainData() {
        weekPrediction = new WeekPrediction();
        dayPerdiction = new DayPrediction();
    }

    public boolean addCity(String city){
        if (cityDataSource.addCity(city)) {
            Toast.makeText(main.getApplicationContext(),
                    "City added.", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(main.getApplicationContext(), "City already in your list",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public List<City> getCities(){
        return cityDataSource.getAllCities();
    }

    public void removeCity(City removedCity){
        cityDataSource.deleteCity(removedCity);
        main.getCityAdapter().notifyDataSetChanged();
    }

    public void setContext(Context context){
        setDataBase(context);
    }

    public void saveCityStat(String cityName, long dateStamp, int temp, int iconCode, String jsonObj){
        cityDataSource.saveCityStat(cityName, dateStamp, temp, iconCode, jsonObj);
    }

    private void setDataBase(Context context) {
        if (cityDataSource == null) {
            cityDataSource = new CityDataSource(context);
            cityDataSource.open();
        } else {
            Toast.makeText(context, "Base already connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void closeDB(){
        cityDataSource.close();
    }

    public WeekPrediction getWeekPrediction() {
        return weekPrediction;
    }

    public DayPrediction getDayPerdiction() {
        return dayPerdiction;
    }

    public void setMainActivity(MainActivity main){
        this.main = main;
    }
}
