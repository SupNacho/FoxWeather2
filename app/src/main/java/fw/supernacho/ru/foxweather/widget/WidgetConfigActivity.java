package fw.supernacho.ru.foxweather.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.List;

import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.data.openweather.Country;
import fw.supernacho.ru.foxweather.prefs.WeatherPreference;

public class WidgetConfigActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent resultValue;
    private EditText editViewCityName;
    private Button buttonSaveCityWidget;
    private String countryTag;

    private WeatherPreference weatherPrefs;

    private final static String DEFAULT_COUNTRY_TAG = "RU";
    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_CITY = "widget_city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null){
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        setResult(RESULT_CANCELED, resultValue);
        setContentView(R.layout.activity_widget_config);

        editViewCityName = findViewById(R.id.edit_text_widget_city);
        buttonSaveCityWidget = findViewById(R.id.button_apply_settings);
        weatherPrefs = MainData.getInstance().getMain().getWeatherPreference();
        Spinner countrySpinner = findViewById(R.id.widget_conf_country_spinner);
        List<Country> countries = MainData.getInstance().getCountries();
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                countries);
        countrySpinner.setAdapter(spinnerAdapter);
        countrySpinner.setOnItemSelectedListener(this);
        countrySpinner.setSelection(weatherPrefs.getCountry(), true);
    }

    public void onClickButton(View view) {
        if (view.getId() == R.id.button_apply_settings){
            if (editViewCityName.getText().length() > 0){
                SharedPreferences sharedPreferences = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(WIDGET_CITY + widgetID, editViewCityName.getText().toString() +
                "," + countryTag);
                editor.apply();
                setResult(RESULT_OK, resultValue);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                WeatherWidget.updateAppWidget(this, appWidgetManager, widgetID,
                        sharedPreferences, false);
                finish();
            } else {
                Snackbar.make(buttonSaveCityWidget, getResources().getText(R.string.empty_input_city_name),
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        countryTag = ((Country)adapterView.getItemAtPosition(i)).getTag();
        weatherPrefs.setCountry(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        countryTag = DEFAULT_COUNTRY_TAG;
    }
}
