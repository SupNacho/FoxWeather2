package fw.supernacho.ru.foxweather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.services.WidgetService;


public class WeatherWidget extends AppWidgetProvider {

    public static final String ACTION_GET_WEATHER = "fw.supernacho.ru.foxweather.services.action.GET_WEATHER";
    public static final String WIDGET_ID = "fw.supernacho.ru.foxweather.services.WDGET_ID";
    public static final String UPDATE_WIDGET_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    private static final String ID = "id";
    private static final String CITY_NAME = "cityName";
    private static final String TEMP = "temp";
    private static final String ICON = "icon";
    private static int weatherIcon = R.drawable.weather_icon_sun8001;
    private static double temp = 0.0;
    private static String cityName = "Moscow";
    private static int[] appWidgetIdArr;
    private static AppWidgetManager appWidgetManagerS = null;
    private static SharedPreferences sharedPreferences;


    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, SharedPreferences sp, boolean fromReceiver) {
        cityName = sp.getString(WidgetConfigActivity.WIDGET_CITY + appWidgetId, null);
        if (cityName == null) return;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setImageViewResource(R.id.appwidget_icon, weatherIcon);
        views.setTextViewText(R.id.appwidget_city_name, cityName);
        views.setTextViewText(R.id.appwidget_temp_text, String.valueOf(temp));
        if (!fromReceiver) {
            WidgetService.startActionGetWeather(context, cityName, appWidgetId);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences.Editor editor = context.getSharedPreferences(WidgetConfigActivity.WIDGET_PREF,
                Context.MODE_PRIVATE).edit();
        for (int appWidgetId : appWidgetIds) {
            editor.remove(WidgetConfigActivity.WIDGET_CITY + appWidgetId);
            editor.apply();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        appWidgetManagerS = appWidgetManager;
        appWidgetIdArr = appWidgetIds;
        sharedPreferences = context.getSharedPreferences(WidgetConfigActivity.WIDGET_PREF,
                Context.MODE_PRIVATE);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, sharedPreferences, false);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (appWidgetManagerS != null) {
            if (intent.getAction().equals(UPDATE_WIDGET_ACTION)) {
                int id = intent.getIntExtra(ID, 0);
                for (int iD : appWidgetIdArr) {
                    if (iD == id) {
                        cityName = intent.getStringExtra(CITY_NAME);
                        temp = intent.getDoubleExtra(TEMP, 0.0);
                        int icon = intent.getIntExtra(ICON, 800);
                        setIcon(icon);
                        updateAppWidget(context, appWidgetManagerS, iD, sharedPreferences, true);
                    }
                }
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

