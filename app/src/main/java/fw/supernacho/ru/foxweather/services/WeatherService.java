package fw.supernacho.ru.foxweather.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.data.DayPrediction;
import fw.supernacho.ru.foxweather.data.HourWeather;
import fw.supernacho.ru.foxweather.data.WeatherDataLoader;
import fw.supernacho.ru.foxweather.data.weather.WeatherData;
import fw.supernacho.ru.foxweather.data.weather.Wind;

public class WeatherService extends Service {
    public static final String DATA_READY = "fw.supernacho.ru.foxweather.action.DATA_READY";
    private String cityName;
    private Timer timer;
    private TimerTask timerTask;

    private WeatherBinder binder = new WeatherBinder();

    public WeatherService() {
        cityName = MainData.getInstance().getMain().getWeatherPreference().getCity();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Service binded", Toast.LENGTH_SHORT).show();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "Service unbinded", Toast.LENGTH_SHORT).show();
        timerTask.cancel();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        getWeatherPrediction();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show();
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void getWeatherPrediction(){
        if (timerTask != null) timerTask.cancel();
        long interval = 10_800_000;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                final JSONObject json = WeatherDataLoader.getJSONData(getBaseContext(), cityName);
                if (json == null){
                            Toast.makeText(getBaseContext(), "City not found",
                                    Toast.LENGTH_SHORT).show();
                } else {

                            renderWeather(json, cityName);

                }
            }
        };
        timer.schedule(timerTask, 0, interval);
    }
    private void renderWeather(JSONObject json, String cityName){
        Gson gson = new Gson();
        List<HourWeather> hours = MainData.getInstance().getDayPerdiction().getHours();
        List<DayPrediction> week = MainData.getInstance().getWeekPrediction().getDaysList();
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
        List<fw.supernacho.ru.foxweather.data.weather.List> weatherList = weatherData.getList();
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
                // TODO: 09.02.2018 пофиксить дублирование
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
        String cityNameWithCountry = weatherData.getCity().getName() + ", " + weatherData.getCity().getCountry();
        Intent intent = new Intent(DATA_READY);
        intent.putExtra("cityName", cityNameWithCountry);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
    }

    private void addWinds(List<fw.supernacho.ru.foxweather.data.weather.List> weatherList, List<Wind> winds, int i) {
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

    public class WeatherBinder extends Binder {
        public WeatherService getService(){
            return WeatherService.this;
        }
    }
}
