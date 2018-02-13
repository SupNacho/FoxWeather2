package fw.supernacho.ru.foxweather.services;

import org.json.JSONObject;

/**
 * Created by SuperNacho on 12.02.2018.
 */

public interface WidgetRenderInterface {
    void renderWeather(JSONObject json);
    int getIconId();
    double getTemp();
    String getCity();
}
