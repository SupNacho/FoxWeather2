package fw.supernacho.ru.foxweather;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import fw.supernacho.ru.foxweather.prefs.WeatherPreference;
import fw.supernacho.ru.foxweather.recyclers.CityAdapter;
import fw.supernacho.ru.foxweather.recyclers.DaysAdapter;
import fw.supernacho.ru.foxweather.recyclers.WeekAdapter;
import fw.supernacho.ru.foxweather.services.WeatherService;
import fw.supernacho.ru.foxweather.widget.WeatherWidget;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener , MainFragment.WeatherInfoListener {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private PreferencesFragment preferencesFragment;
    private AddCityFragment addCityFragment;
    private DrawerLayout drawer;
    private WeatherPreference weatherPreference;

    private DaysAdapter daysAdapter;
    private WeekAdapter weekAdapter;
    private CityAdapter cityAdapter;
    private boolean isMenuEditable = false;

    private boolean serviceBind = false;
    private ServiceConnection serviceConnection;
    WeatherService weatherService;


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
        weatherPreference = new WeatherPreference(this);
        initService(weatherPreference.getCity());
        if (savedInstanceState == null) {
            init();
        }

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void onBind(View view){
        Intent intent = new Intent (getBaseContext(), WeatherService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void onUnbindService(View view){
        if (serviceBind){
            unbindService(serviceConnection);
        }
        serviceBind = false;
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
        preferencesFragment = PreferencesFragment.newInstance(null, null);
        addCityFragment = AddCityFragment.newInstance(null, null);
        MainFragment mainFragment = MainFragment.newInstance(null, null);
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

    private void initService(final String cityName){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                weatherService = ((WeatherService.WeatherBinder) iBinder).getService();
                weatherService.setCityName(cityName);
                serviceBind = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                serviceBind = false;
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(int id) {
        String city = MainData.getInstance().getCities().get(id).getCityName();
        if (!serviceBind) return;
        updateCityWeather(city);
        Intent intentUpdateWidgetCity = new Intent(WeatherWidget.ACTION_GET_WEATHER);
        intentUpdateWidgetCity.putExtra("cityName", city);
        sendBroadcast(intentUpdateWidgetCity);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void updateCityWeather(String city) {
        weatherPreference.setCity(city);
        weatherService.setCityName(city);
        weatherService.getWeatherPrediction();
    }

    public WeatherPreference getWeatherPreference() {
        return weatherPreference;
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
