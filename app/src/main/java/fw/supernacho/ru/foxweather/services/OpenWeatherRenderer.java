package fw.supernacho.ru.foxweather.services;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.data.DayPrediction;
import fw.supernacho.ru.foxweather.data.HourWeather;
import fw.supernacho.ru.foxweather.data.openweather.WeatherData;
import fw.supernacho.ru.foxweather.data.openweather.Wind;

/**
 * Created by SuperNacho on 11.02.2018.
 */

public class OpenWeatherRenderer implements RenderInterface {
    private Gson gson;
    private List<HourWeather> hours;
    private List<DayPrediction> week;

    public OpenWeatherRenderer() {
        gson = new Gson();
        hours = MainData.getInstance().getDayPrediction().getHours();
        week = MainData.getInstance().getWeekPrediction().getDaysList();
    }

    @Override
    public String renderWeather(JSONObject json) {
        hours.clear();
        week.clear();
        WeatherData weatherData = gson.fromJson(json.toString(), WeatherData.class);
        String currentDay = "";
        float avgTemp;
        float tTemp = 0.0f;
        int avgHumidity;
        int tHumidity = 0;
        double avgPressure;
        double tPressure = 0.0;
        int count = 0;
        List<fw.supernacho.ru.foxweather.data.openweather.List> weatherList = weatherData.getList();
        List<Wind> winds = new ArrayList<>(4);
        winds.add(new Wind());
        winds.add(new Wind());
        winds.add(new Wind());
        winds.add(new Wind());
        for (int i = 0; i < weatherList.size(); i++) {
            if (currentDay.equals("")) currentDay = weatherList.get(i).getDtTxt().substring(0,10);
            if (!currentDay.equals(weatherList.get(i).getDtTxt().substring(0,10))){
                currentDay = weatherList.get(i).getDtTxt().substring(0,10);
                avgTemp = tTemp / count;
                avgHumidity = tHumidity / count;
                avgPressure = tPressure / count;
                week.add(new DayPrediction(weatherList.get(i-1).getWeather().get(0).getId(),
                        weatherList.get(i-1).getDt(), avgTemp, winds, avgHumidity, avgPressure));
                winds = new ArrayList<>(4);
                winds.add(new Wind());
                winds.add(new Wind());
                winds.add(new Wind());
                winds.add(new Wind());
                count = 0;
                tTemp = 0.0f;
                tHumidity = 0;
                tPressure = 0.0;
                count += 1;
                tTemp += weatherList.get(i).getMain().getTemp();
                tHumidity += weatherList.get(i).getMain().getHumidity();
                tPressure += weatherList.get(i).getMain().getPressure();
                addWinds(weatherList, winds, i);
            } else {
                tTemp += weatherList.get(i).getMain().getTemp();
                tHumidity += weatherList.get(i).getMain().getHumidity();
                tPressure += weatherList.get(i).getMain().getPressure();
                count += 1;
                addWinds(weatherList, winds, i);
            }
            if (i > 0 && i < 8) {
                hours.add(new HourWeather((long) weatherList.get(i).getDt(),
                        weatherList.get(i).getWeather().get(0).getId(),
                        weatherList.get(i).getMain().getTemp()));
            }
        }
        return weatherData.getCity().getName() + ", " + weatherData.getCity().getCountry();
    }

    private void addWinds(List<fw.supernacho.ru.foxweather.data.openweather.List> weatherList, List<Wind> winds, int i) {
        if ( weatherList.get(i).getDtTxt().contains("00:00:00") ||
                weatherList.get(i).getDtTxt().contains("03:00:00")){
            winds.add(0, weatherList.get(i).getWind());
        }
        if ( weatherList.get(i).getDtTxt().contains("06:00:00") ||
                weatherList.get(i).getDtTxt().contains("09:00:00")){
            winds.add(1, weatherList.get(i).getWind());
        }
        if ( weatherList.get(i).getDtTxt().contains("12:00:00") ||
                weatherList.get(i).getDtTxt().contains("15:00:00")){
            winds.add(2, weatherList.get(i).getWind());
        }
        if ( weatherList.get(i).getDtTxt().contains("18:00:00") ||
                weatherList.get(i).getDtTxt().contains("21:00:00")){
            winds.add(3, weatherList.get(i).getWind());
        }
    }
}
