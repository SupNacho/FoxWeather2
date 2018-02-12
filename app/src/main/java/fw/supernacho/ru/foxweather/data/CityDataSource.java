package fw.supernacho.ru.foxweather.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fw.supernacho.ru.foxweather.CsvParser;
import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.data.weather.Country;

/**
 * Created by SuperNacho on 14.12.2017.
 */

public class CityDataSource {
    private DataBaseHelper dbHelper;
    private SQLiteDatabase dataBase;
    private Context context;

    private String[] citiesAllNames = {
            DataBaseHelper.COLUMN_ID,
            DataBaseHelper.COLUMN_CITY
    };

    public CityDataSource(Context context){
        dbHelper = new DataBaseHelper(context);
        this.context = context;
    }

    public void open(){
        dataBase = dbHelper.getWritableDatabase();
        if (checkTable(DataBaseHelper.TABLE_COUNTRIES)){
            if (!checkRecords(DataBaseHelper.TABLE_COUNTRIES)) {
                new CsvParser(dataBase, context).parseCSV(R.raw.country_codes);
            }
        }
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
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        return 0L;
    }

    private boolean checkTable(String table){
        Cursor cursor = dataBase.rawQuery("select DISTINCT tbl_name FROM sqlite_master where tbl_name = '" + table + "';",
                null);
        if (cursor != null){
            if(cursor.getCount() > 0){
                cursor.close();
                return true;
            }
            cursor.close();

        }
        return false;
    }
    private boolean checkRecords(String table){
        Cursor cursor = dataBase.query(table, null, null, null, null, null, null);
        if (cursor != null){
            if(cursor.getCount() > 0){
                Log.d("////", String.valueOf(cursor.getCount()));
                cursor.close();
                return true;
            }
            cursor.close();

        }
        return false;
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

    public List<Country> getCountries(){
        List<Country> countries = new ArrayList<>();
        Cursor cursor = dataBase.query(DataBaseHelper.TABLE_COUNTRIES, new String[] {DataBaseHelper.COLUMN_ID_COUNTRIES,
                        DataBaseHelper.COLUMN_TITLE, DataBaseHelper.COLUMN_TAG},null,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Country country = cursorToCountry(cursor);
            countries.add(country);
            cursor.moveToNext();
        }
        cursor.close();
        return countries;
    }

    private City cursorToCity(Cursor cursor){
        City city = new City();
        city.setId(cursor.getLong(0));
        city.setCityName(cursor.getString(1));
        return city;
    }
    private Country cursorToCountry(Cursor cursor){
        Country country = new Country();
        country.setId(cursor.getLong(0));
        country.setCountryTitle(cursor.getString(1));
        country.setTag(cursor.getString(2));
        return country;
    }
}
