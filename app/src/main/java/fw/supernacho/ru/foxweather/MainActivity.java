package fw.supernacho.ru.foxweather;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fw.supernacho.ru.foxweather.data.DayPrediction;
import fw.supernacho.ru.foxweather.data.HourWeather;
import fw.supernacho.ru.foxweather.data.WeatherDataLoader;
import fw.supernacho.ru.foxweather.prefs.WeatherPreference;
import fw.supernacho.ru.foxweather.recyclers.CityAdapter;
import fw.supernacho.ru.foxweather.recyclers.DaysAdapter;
import fw.supernacho.ru.foxweather.recyclers.WeekAdapter;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener , MainFragment.WeatherInfoListener {

    private static final String LOG_TAG = "--++--";
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int HOURS_PERDICT_ELEMENTS = 8;
    private static final String CITY_ARRAY = "CITY_ARRAY";
    private PreferencesFragment preferencesFragment;
    private AddCityFragment addCityFragment;
    private DrawerLayout drawer;
    private WeatherPreference weatherPreference;
    private final Handler handler = new Handler();

    private DaysAdapter daysAdapter;
    private WeekAdapter weekAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initRecyclers();
        if (savedInstanceState == null) {
            init();
            updateWeatherData(weatherPreference.getCity());
            loadUserCities();
        }

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openPrefs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPrefs() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_container, preferencesFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_add:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.add(R.id.fragment_container, addCityFragment);
                transaction.commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.button_edit:
                Snackbar.make(view, "Edit", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.button_settings:
                openPrefs();
                drawer.closeDrawer(GravityCompat.START);
                break;
            default:
                Snackbar.make(view, "Unknown button", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    private void initRecyclers(){
        RecyclerView cityRecycler = findViewById(R.id.recycler_city);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(VERTICAL);
        cityRecycler.setLayoutManager(layoutManager);
        CityAdapter cityAdapter = new CityAdapter(this);
        daysAdapter = new DaysAdapter(this);
        weekAdapter = new WeekAdapter(this);
        cityRecycler.setAdapter(cityAdapter);
    }

    private void init(){

        weatherPreference = new WeatherPreference(this);
        preferencesFragment = PreferencesFragment.newInstance(null, null);
        addCityFragment = AddCityFragment.newInstance(null, null);
        MainFragment mainFragment = MainFragment.newInstance(null,null);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mainFragment);
        fragmentTransaction.commit();


        Button buttonAdd = findViewById(R.id.button_add);
        Button buttonEdit = findViewById(R.id.button_edit);
        Button buttonSetting = findViewById(R.id.button_settings);
        buttonAdd.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);
        buttonSetting.setOnClickListener(this);
    }

    private void loadUserCities() {
        Set<String> restoreCities = new WeatherPreference(this).getCities();
        for (String restoreCity : restoreCities) {
            MainData.getInstance().addCity(restoreCity);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(CITY_ARRAY, (ArrayList<String>) MainData.getInstance().getCities());
    }

    @Override
    public void onListItemClick(int id) {
        String city = MainData.getInstance().getCities().get(id);
        weatherPreference.setCity(city);
        System.out.println(city);
        updateWeatherData(city);
        drawer.closeDrawer(GravityCompat.START);
    }

    public WeatherPreference getWeatherPreference() {
        return weatherPreference;
    }

    public void updateWeatherData(final String city) {

        new Thread(){
            public void run(){
                final JSONObject json = WeatherDataLoader.getJSONData(MainActivity.this, city);
                if (json == null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "City not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                            daysAdapter.notifyDataSetChanged();
                            weekAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        Log.d(LOG_TAG, "json " + json.toString());
        try {

            JSONArray daysOfWeek = json.getJSONArray("list");
            List<DayPrediction> week = MainData.getInstance().getWeekPrediction().getDaysList();
            List<HourWeather> hours = MainData.getInstance().getDayPerdiction().getHours();
            week.clear();
            hours.clear();
            for (int i = 0; i < HOURS_PERDICT_ELEMENTS; i++) {
                JSONObject hour = daysOfWeek.getJSONObject(i);
                JSONObject main = hour.getJSONObject("main");
                JSONObject details = hour.getJSONArray("weather").getJSONObject(0);
                System.out.println("Main: " + main.toString());
                System.out.println("Details: " + details.toString());
                hours.add(new HourWeather(hour.getLong("dt"), details.getInt("id"),
                        main.getDouble("temp")));
            }
            for (int i = 0; i < daysOfWeek.length(); i ++) {
                    JSONObject hour = daysOfWeek.getJSONObject(i);
                    JSONObject main = hour.getJSONObject("main");
                    JSONObject details = hour.getJSONArray("weather").getJSONObject(0);
                    if (hour.getString("dt_txt").contains("21")) {
                        week.add(new DayPrediction(details.getInt("id"), hour.getLong("dt"),
                                main.getDouble("temp")));
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public DaysAdapter getDaysAdapter() {
        return daysAdapter;
    }

    public WeekAdapter getWeekAdapter() {
        return weekAdapter;
    }
}
