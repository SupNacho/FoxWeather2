package fw.supernacho.ru.foxweather;

import java.util.ArrayList;
import java.util.List;

import fw.supernacho.ru.foxweather.data.DayPrediction;
import fw.supernacho.ru.foxweather.data.WeekPrediction;

/**
 * Created by SuperNacho on 08.12.2017.
 */

public class MainData {
    private static final MainData ourInstance = new MainData();
    private List<String> cities;
    private WeekPrediction weekPrediction;
    private DayPrediction dayPerdiction;

    public static MainData getInstance() {
        return ourInstance;
    }

    private MainData() {
        cities = new ArrayList<>();
        weekPrediction = new WeekPrediction();
        dayPerdiction = new DayPrediction();
    }

    public void addCity(String city){
        cities.add(city);
    }

    public List<String> getCities(){
        return cities;
    }

    public WeekPrediction getWeekPrediction() {
        return weekPrediction;
    }

    public DayPrediction getDayPerdiction() {
        return dayPerdiction;
    }

    public void addCities(ArrayList cities){
        this.cities = cities;
    }
}
