package fw.supernacho.ru.foxweather.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fw.supernacho.ru.foxweather.MainActivity;
import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.data.DayPrediction;
import fw.supernacho.ru.foxweather.data.HourWeather;
import fw.supernacho.ru.foxweather.data.WeatherDataLoader;
import fw.supernacho.ru.foxweather.data.weather.WeatherData;

public class WeatherService extends Service {
    public static final String DATA_READY = "fw.supernacho.ru.foxweather.action.DATA_READY";
    private String cityName;
    private Timer timer;
    private TimerTask timerTask;

    private static final int HOURS_PERDICT_ELEMENTS = 8;

    private WeatherBinder binder = new WeatherBinder();

    public WeatherService() {
        cityName = MainData.getInstance().getMain().getWeatherPreference().getCity();
        Log.d(">>>> ", " construct WService");
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
        Log.d(">>>>", "Set Name mothod in service");
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

//    private void renderWeather(JSONObject json, String cityName){
//        try {
//
//            JSONArray daysOfWeek = json.getJSONArray("list");
//            List<DayPrediction> week = MainData.getInstance().getWeekPrediction().getDaysList();
//            List<HourWeather> hours = MainData.getInstance().getDayPerdiction().getHours();
//            week.clear();
//            hours.clear();
//            for (int i = 0; i < HOURS_PERDICT_ELEMENTS; i++) {
//                JSONObject hour = daysOfWeek.getJSONObject(i);
//                JSONObject main = hour.getJSONObject("main");
//                JSONObject details = hour.getJSONArray("weather").getJSONObject(0);
//                hours.add(new HourWeather(hour.getLong("dt"), details.getInt("id"),
//                        main.getDouble("temp")));
//            }
//            for (int i = 0; i < daysOfWeek.length(); i ++) {
//                JSONObject hour = daysOfWeek.getJSONObject(i);
//                JSONObject main = hour.getJSONObject("main");
//                JSONObject details = hour.getJSONArray("weather").getJSONObject(0);
//                if (hour.getString("dt_txt").contains("21")) {
//                    if (week.size() >= 5) break;
//                    week.add(new DayPrediction(details.getInt("id"), hour.getLong("dt"),
//                            main.getDouble("temp")));
//                }
//            }
//
//            HourWeather hourWeather = hours.get(0);
//            MainData.getInstance().saveCityStat(cityName, hourWeather.getDt(),
//                    (int) hourWeather.getTemp(), hourWeather.getId(), json.toString() );
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Intent intent = new Intent(DATA_READY);
//        intent.putExtra("cityName", cityName);
//        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
////    }
    private void renderWeather(JSONObject json, String cityName){
        Gson gson = new Gson();
        List<HourWeather> hours = MainData.getInstance().getDayPerdiction().getHours();
        List<DayPrediction> week = MainData.getInstance().getWeekPrediction().getDaysList();
        hours.clear();
        week.clear();
        WeatherData weatherData = gson.fromJson(json.toString(), WeatherData.class);
        Log.d("+++", weatherData.getCity().getName());
        Log.d("+++", String.valueOf(weatherData.getList().size()));
        Log.d("+++", String.valueOf(weatherData.getList().get(0).getWeather().get(0).getIcon()));
        String currentDay = "";
        float avgTemp = 0.0f;
        float tTemp = 0.0f;
        int avgHumidity = 0;
        int tHumidity = 0;
        double avgPressure = 0.0;
        double tPressure = 0.0;
        int count = 0;
        List<fw.supernacho.ru.foxweather.data.weather.List> weatherList = weatherData.getList();
        for (int i = 0; i < weatherList.size(); i++) {
            if (currentDay.equals("")) currentDay = weatherList.get(i).getDtTxt().substring(0,10);
            if (!currentDay.equals(weatherList.get(i).getDtTxt().substring(0,10))){
                currentDay = weatherList.get(i).getDtTxt().substring(0,10);
                avgTemp = tTemp / count;
                avgHumidity = tHumidity / count;
                avgPressure = tPressure / count;
                week.add(new DayPrediction(weatherList.get(i-1).getWeather().get(0).getId(),
                        weatherList.get(i-1).getDt(), avgTemp, null, avgHumidity, avgPressure));

                Log.d("+++-", weatherList.get(i).getDtTxt().substring(0,10));
                Log.d("+++-", String.valueOf(tTemp));
                Log.d("+++-", String.valueOf(count));
                Log.d("+++-", String.valueOf(avgTemp));
                count = 0;
                avgTemp = 0.0f;
                tTemp = 0.0f;
                avgHumidity = 0;
                avgPressure = 0.0;
                tHumidity = 0;
                tPressure = 0.0;
                count += 1;
                tTemp += weatherList.get(i).getMain().getTemp();
                tHumidity += weatherList.get(i).getMain().getHumidity();
                tPressure += weatherList.get(i).getMain().getPressure();
            } else {
                // TODO: 09.02.2018 пофиксить дублирование
                tTemp += weatherList.get(i).getMain().getTemp();
                tHumidity += weatherList.get(i).getMain().getHumidity();
                tPressure += weatherList.get(i).getMain().getPressure();
                count += 1;
                Log.d("+++", weatherList.get(i).getDtTxt().substring(0,10));
                Log.d("+++", String.valueOf(tTemp));
                Log.d("+++", String.valueOf(count));
                Log.d("+++", String.valueOf(avgTemp));
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

    public class WeatherBinder extends Binder {
        public WeatherService getService(){
            return WeatherService.this;
        }
    }
}
