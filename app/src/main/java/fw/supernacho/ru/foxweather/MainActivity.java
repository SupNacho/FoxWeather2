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
    private static final String CITY_NAME = "cityName";
    private AddCityFragment addCityFragment;
    private DrawerLayout drawer;
    private WeatherPreference weatherPreference;

    private DaysAdapter daysAdapter;
    private WeekAdapter weekAdapter;
    private CityAdapter cityAdapter;
    private boolean isMenuEditable = false;

    private boolean serviceBind = false;
    private ServiceConnection serviceConnection;
    private WeatherService weatherService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initRecyclers();
        MainData.getInstance().setContext(getApplicationContext());
        MainData.getInstance().setMainActivity(this);
        weatherPreference = new WeatherPreference(this);
        initService(weatherPreference.getCity());
        if (savedInstanceState == null) init();

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
            default:
                Snackbar.make(view, getResources().getString(R.string.main_activ_unkwn_button),
                        Snackbar.LENGTH_SHORT).show();
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
        addCityFragment = AddCityFragment.newInstance();
        MainFragment mainFragment = MainFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mainFragment);
        fragmentTransaction.commit();


        Button buttonAdd = findViewById(R.id.button_add);
        Button buttonEdit = findViewById(R.id.button_edit);
        buttonAdd.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);
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
        intentUpdateWidgetCity.putExtra(CITY_NAME, city);
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
