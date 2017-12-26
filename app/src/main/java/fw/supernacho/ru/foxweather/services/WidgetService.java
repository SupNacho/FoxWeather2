package fw.supernacho.ru.foxweather.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fw.supernacho.ru.foxweather.data.WeatherDataLoader;
import fw.supernacho.ru.foxweather.widget.WeatherWidget;


public class WidgetService extends IntentService {

    private static final String ACTION_GET_WEATHER = "fw.supernacho.ru.foxweather.services.action.GET_WEATHER";
    private static final String UPDATE_WIDGET_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    private static final String CITY_NAME = "fw.supernacho.ru.foxweather.services.extra.CITY_NAME";
    private final Handler handler = new Handler();

    public WidgetService() {
        super("WidgetService");
    }

    public static void startActionGetWeather(Context context, String cityName, int id) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(UPDATE_WIDGET_ACTION);
        intent.putExtra(WeatherWidget.WIDGET_ID, id);
        intent.putExtra(CITY_NAME, cityName);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_WIDGET_ACTION.equals(action)) {
                final String cityName = intent.getStringExtra(CITY_NAME);
                final int widgetId = intent.getIntExtra(WeatherWidget.WIDGET_ID, 0);
                handleWeather(cityName, widgetId);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleWeather(final String city, final int id) {
        new Thread(){
            public void run(){
                final JSONObject json = WeatherDataLoader.getJSONData( getBaseContext(), city);
                if (json == null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getBaseContext(), "City not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json, city, id);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json, String cityName, int id){
        try {

            JSONArray daysOfWeek = json.getJSONArray("list");
            JSONObject hour = daysOfWeek.getJSONObject(0);
            JSONObject main = hour.getJSONObject("main");
            JSONObject details = hour.getJSONArray("weather").getJSONObject(0);
            int iconId =details.getInt("id");
            double temp = main.getDouble("temp");

            Intent weatherDataBroadcast = new Intent(UPDATE_WIDGET_ACTION);
            weatherDataBroadcast.putExtra("icon", iconId);
            weatherDataBroadcast.putExtra("temp", temp);
            weatherDataBroadcast.putExtra("cityName", cityName);
            weatherDataBroadcast.putExtra("id", id);
            sendBroadcast(weatherDataBroadcast);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
