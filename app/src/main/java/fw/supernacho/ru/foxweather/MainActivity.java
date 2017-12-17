package fw.supernacho.ru.foxweather;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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

import java.util.List;
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
    private PreferencesFragment preferencesFragment;
    private AddCityFragment addCityFragment;
    private DrawerLayout drawer;
    private WeatherPreference weatherPreference;
    private final Handler handler = new Handler();
    private MainFragment mainFragment;

    private DaysAdapter daysAdapter;
    private WeekAdapter weekAdapter;
    private CityAdapter cityAdapter;
    private boolean isMenuEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_share_weather);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plane");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Some subject");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Some weather data");
                startActivity(shareIntent);
            }
        });

        initRecyclers();
        MainData.getInstance().setContext(getApplicationContext());
        MainData.getInstance().setMainActivity(this);
        if (savedInstanceState == null) {
            init();
            updateWeatherData(weatherPreference.getCity());
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
        int id = item.getItemId();
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
                isMenuEditable = !isMenuEditable;
                cityAdapter.notifyDataSetChanged();
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
        cityAdapter = new CityAdapter(this);
        daysAdapter = new DaysAdapter(this);
        weekAdapter = new WeekAdapter(this);
        cityRecycler.setAdapter(cityAdapter);
    }

    private void init(){

        weatherPreference = new WeatherPreference(this);
        preferencesFragment = PreferencesFragment.newInstance(null, null);
        addCityFragment = AddCityFragment.newInstance(null, null);
        mainFragment = MainFragment.newInstance(null,null);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(int id) {
        String city = MainData.getInstance().getCities().get(id).getCityName();
        System.out.println(city);
        weatherPreference.setCity(city);
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
                            renderWeather(json, city);
                            daysAdapter.notifyDataSetChanged();
                            weekAdapter.notifyDataSetChanged();
                            mainFragment.setCityLabel(city);
                        }
                    });

                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json, String cityName){
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
                        if (week.size() >= 5) break;
                        System.out.println(">>>> Add to list: " + week.size());
                        week.add(new DayPrediction(details.getInt("id"), hour.getLong("dt"),
                                main.getDouble("temp")));
                    }
            }

            HourWeather hourWeather = hours.get(0);
            MainData.getInstance().saveCityStat(cityName, hourWeather.getDt(),
                    (int) hourWeather.getTemp(), hourWeather.getId(), json.toString() );
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

    public CityAdapter getCityAdapter() {
        return cityAdapter;
    }

    public boolean isMenuEditable() {
        return isMenuEditable;
    }

}
