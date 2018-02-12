package fw.supernacho.ru.foxweather.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SuperNacho on 12.02.2018.
 */

public class OpenWeatherWidgetRenderer implements RenderInterface {
    private int iconId;
    private double temp;
    private String city;

    @Override
    public String renderWeather(JSONObject json) {
        try {
            JSONArray daysOfWeek = json.getJSONArray("list");
            JSONObject hour = daysOfWeek.getJSONObject(0);
            JSONObject main = hour.getJSONObject("main");
            JSONObject details = hour.getJSONArray("weather").getJSONObject(0);
            iconId = details.getInt("id");
            temp = main.getDouble("temp");
            city = json.getJSONObject("city").getString("name");
            return city;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return "404";
    }

    public int getIconId() {
        return iconId;
    }

    public double getTemp() {
        return temp;
    }

    public String getCity() {
        return city;
    }
}
