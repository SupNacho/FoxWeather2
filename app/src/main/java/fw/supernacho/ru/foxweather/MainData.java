package fw.supernacho.ru.foxweather;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import fw.supernacho.ru.foxweather.data.City;
import fw.supernacho.ru.foxweather.data.CityDataSource;
import fw.supernacho.ru.foxweather.data.DayPrediction;
import fw.supernacho.ru.foxweather.data.WeekPrediction;
import fw.supernacho.ru.foxweather.data.weather.Country;

/**
 * Created by SuperNacho on 08.12.2017.
 */

public class MainData {
    private static final MainData ourInstance = new MainData();
    private WeekPrediction weekPrediction;
    private DayPrediction dayPrediction;
    private CityDataSource cityDataSource;
    private MainActivity main;

    public static MainData getInstance() {
        return ourInstance;
    }

    private MainData() {
        weekPrediction = new WeekPrediction();
        dayPrediction = new DayPrediction();
    }

    public boolean addCity(String city) {
        if (cityDataSource.addCity(city)) {
            Toast.makeText(main.getApplicationContext(),
                    main.getResources().getString(R.string.main_data_city_added),
                    Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(main.getApplicationContext(), main.getResources()
                            .getString(R.string.main_data_city_already_in_list),
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public List<City> getCities() {
        return cityDataSource.getAllCities();
    }

    public List<Country> getCountries() {
        return cityDataSource.getCountries();
    }

    public void removeCity(City removedCity) {
        cityDataSource.deleteCity(removedCity);
        main.getCityAdapter().notifyDataSetChanged();
    }

    public void setContext(Context context) {
        setDataBase(context);
    }

    private void setDataBase(Context context) {
        if (cityDataSource == null) {
            cityDataSource = new CityDataSource(context);
            cityDataSource.open();
        } else {
            Toast.makeText(context, main.getResources()
                    .getString(R.string.main_data_base_already_connected), Toast.LENGTH_SHORT).show();
        }
    }

    public void closeDB() {
        cityDataSource.close();
    }

    public WeekPrediction getWeekPrediction() {
        return weekPrediction;
    }

    public DayPrediction getDayPrediction() {
        return dayPrediction;
    }

    public void setMainActivity(MainActivity main) {
        this.main = main;
    }

    public MainActivity getMain() {
        return main;
    }
}
