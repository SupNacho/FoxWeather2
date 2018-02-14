package fw.supernacho.ru.foxweather.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;

public class WeatherDataLoader {
    private static final String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric";
    private static final String KEY = "x-api-key";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int ALL_RIGHT = 200;
    private static String cityName;

    public static JSONObject getJSONData(Context context, String city){
        cityName = city;
        try{
            URL url = new URL(String.format(OPEN_WEATHER_API, city));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(8196);
            String tempVariable;
            while ((tempVariable = reader.readLine()) != null){
                rawData.append(tempVariable).append(NEW_LINE);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if (jsonObject.getInt(RESPONSE) != ALL_RIGHT){
                return null;
            }
            return jsonObject;
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (UnknownHostException e){
            try {
                return new JSONObject(getOfflineData());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getOfflineData(){
        return MainData.getInstance().getOfflineSrc(cityName);
    }
}
