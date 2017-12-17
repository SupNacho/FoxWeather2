package fw.supernacho.ru.foxweather.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuperNacho on 14.12.2017.
 */

public class CityDataSource {
    private DataBaseHelper dbHelper;
    private SQLiteDatabase dataBase;

    private String[] citiesAllNames = {
            DataBaseHelper.COLUMN_ID,
            DataBaseHelper.COLUMN_CITY
    };

    public CityDataSource(Context context){
        dbHelper = new DataBaseHelper(context);
    }

    public void open(){
        dataBase = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public boolean addCity(String cityName){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_CITY, cityName);
        long cityCount = checkCityInBase(DataBaseHelper.TABLE_CITIES, cityName);
        if (cityCount == 0) {
            long insertId = dataBase.insert(DataBaseHelper.TABLE_CITIES, null, values);
//            City newCity = new City();
//            newCity.setCityName(cityName);
//            newCity.setId(insertId);
            return true;
        }
        return false;
    }

    public void saveCityStat(String cityName, long datestamp, int temp, int iconCode, String jsonObj){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_CITY, cityName);
        values.put(DataBaseHelper.COLUMN_DATE, datestamp);
        values.put(DataBaseHelper.COLUMN_TEMP, temp);
        values.put(DataBaseHelper.COLUMN_ICON, iconCode);
        values.put(DataBaseHelper.COLUMN_SUMMARY, jsonObj);
        long id = checkCityInBase(DataBaseHelper.TABLE_WEATHER_LOGS, cityName);
        if (id >0) {
            dataBase.update(DataBaseHelper.TABLE_WEATHER_LOGS, values, DataBaseHelper.COLUMN_ID + "=" + id, null);
        } else {
            long insertStatId = dataBase.insert(DataBaseHelper.TABLE_WEATHER_LOGS, null, values);
            System.out.println(">>> added stat id: " + insertStatId);
        }
    }

    private long checkCityInBase(String table, String cityName){
        Cursor cursor = dataBase.query(table, citiesAllNames, "CITY = ?", new String[] {cityName}, null, null, null);
        cursor.moveToFirst();
        System.out.println(">>> Kol-vo gorodov v state s takim imenem: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            System.out.println(">>> City ID: " + cursor.getLong(0));
            return cursor.getLong(0);
        }
        return 0L;
    }

    public void deleteCity(City city){
        long id = city.getId();
        dataBase.delete(DataBaseHelper.TABLE_CITIES, DataBaseHelper.COLUMN_ID + " = " + id, null);
    }

    public void deleteAllCities(){
        dataBase.delete(DataBaseHelper.TABLE_CITIES, null, null);
    }

    public List<City> getAllCities(){
        List<City> cities = new ArrayList<>();
        Cursor cursor = dataBase.query(DataBaseHelper.TABLE_CITIES, citiesAllNames,
                null, null, null, null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            City city = cursorToCity(cursor);
            cities.add(city);
            cursor.moveToNext();
        }
        cursor.close();
        return cities;
    }

    private City cursorToCity(Cursor cursor){
        City city = new City();
        city.setId(cursor.getLong(0));
        city.setCityName(cursor.getString(1));
        return city;
    }
}
