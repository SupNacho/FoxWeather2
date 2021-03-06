package fw.supernacho.ru.foxweather.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SuperNacho on 12.02.2018.
 */

public class OpenWeatherWidgetRender implements WidgetRenderInterface {
    private int iconId;
    private double temp;
    private String city;

    @Override
    public void renderWeather(JSONObject json) {
        try {
            JSONArray daysOfWeek = json.getJSONArray("list");
            JSONObject hour = daysOfWeek.getJSONObject(0);
            JSONObject main = hour.getJSONObject("main");
            JSONObject details = hour.getJSONArray("weather").getJSONObject(0);
            iconId = details.getInt("id");
            temp = main.getDouble("temp");
            city = json.getJSONObject("city").getString("name");
        } catch (JSONException e){
            e.printStackTrace();
        }
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
