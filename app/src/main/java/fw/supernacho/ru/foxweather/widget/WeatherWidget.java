package fw.supernacho.ru.foxweather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.services.WidgetService;


public class WeatherWidget extends AppWidgetProvider {

    public static final String ACTION_GET_WEATHER = "fw.supernacho.ru.foxweather.services.action.GET_WEATHER";
    public static final String UPDATE_WIDGET_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    private static int weatherIcon = R.drawable.weather_icon_sun8001;
    private static double temp = 0.0;
    private static String cityName = "Moscow";
    private static int appWidgetIdS;
    private static AppWidgetManager appWidgetManagerS = null;


    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setImageViewResource(R.id.appwidget_icon, weatherIcon);
        views.setTextViewText(R.id.appwidget_city_name, cityName);
        views.setTextViewText(R.id.appwidget_temp_text, String.valueOf(temp));
        appWidgetIdS = appWidgetId;
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        appWidgetManagerS = appWidgetManager;
        WidgetService.startActionGetWeather(context, cityName);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_GET_WEATHER)) {
            Log.d(">>>>>", "GET WEATHER");
            String cityName = intent.getStringExtra("cityName");
            WidgetService.startActionGetWeather(context, cityName);
        }
        if (appWidgetManagerS != null) {
            if (intent.getAction().equals(UPDATE_WIDGET_ACTION)) {
                Log.d(">>>>>", "UPDATE WIDGET ACTON");
                cityName = intent.getStringExtra("cityName");
                temp = intent.getDoubleExtra("temp", 0.0);
                int icon = intent.getIntExtra("icon", 800);
                setIcon(icon);
                Log.d(">>>>>", String.format("City: %s, Temp: %f, Icon: %d", cityName, temp, icon));
                Log.d(">>>>>", "APPWIDGETID = "+ appWidgetIdS);
                updateAppWidget(context, appWidgetManagerS, appWidgetIdS);
            }
        }
        super.onReceive(context, intent);
    }

    private void setIcon(int iconId) {
        int id = iconId / 100;
        if (iconId == 800) {
            weatherIcon = (R.drawable.weather_icon_sun8001);
        } else {
            switch (id) {
                case 2:
                    weatherIcon = (R.drawable.weather_icon_thunder200);
                    break;
                case 3:
                    weatherIcon = (R.drawable.weather_icon_drizzle300);
                    break;
                case 5:
                    weatherIcon = (R.drawable.weather_icon_rain500);
                    break;
                case 6:
                    weatherIcon = (R.drawable.weather_icon_snow600);
                    break;
                case 7:
                    weatherIcon = (R.drawable.weather_icon_mist700);
                    break;
                case 8:
                    weatherIcon = (R.drawable.weather_icon_clouds801);
                    break;
            }
        }
    }
}

