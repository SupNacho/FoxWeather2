package fw.supernacho.ru.foxweather.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.data.WeatherDataLoader;

public class WeatherService extends Service {
    public static final String DATA_READY = "fw.supernacho.ru.foxweather.action.DATA_READY";
    private String cityName;
    private Timer timer;
    private TimerTask timerTask;
    private RenderInterface weatherRenderer;

    private WeatherBinder binder = new WeatherBinder();

    public WeatherService() {
        cityName = MainData.getInstance().getMain().getWeatherPreference().getCity();
        weatherRenderer = new OpenWeatherRenderer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
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
                            Toast.makeText(getBaseContext(), getBaseContext().getResources()
                                            .getString(R.string.service_toast_city_not_found),
                                    Toast.LENGTH_SHORT).show();
                } else {
                    String cityNameWithCountry = weatherRenderer.renderWeather(json);
                    MainData.getInstance().addOfflineSrc(cityName, json);
                    Intent intent = new Intent(DATA_READY);
                    intent.putExtra("cityName", cityNameWithCountry);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                }
            }
        };
        timer.schedule(timerTask, 0, interval);
    }

    public class WeatherBinder extends Binder {
        public WeatherService getService(){
            return WeatherService.this;
        }
    }
}
