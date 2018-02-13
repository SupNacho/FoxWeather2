package fw.supernacho.ru.foxweather.services;

import org.json.JSONObject;

/**
 * Created by SuperNacho on 11.02.2018.
 */

public interface RenderInterface {
    String renderWeather(JSONObject json);
}
