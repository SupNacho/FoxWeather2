package fw.supernacho.ru.foxweather.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fw.supernacho.ru.foxweather.R;

public class WidgetConfigActivity extends AppCompatActivity {
    private int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent resultValue;
    private EditText editViewCityName;
    private Button buttonSaveCityWidget;

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
    }

    public void onClickButton(View view) {
        Log.d(">>>>>>", "onClik");
        if (view.getId() == R.id.button_apply_settings){
            if (editViewCityName.getText().length() > 0){
                Log.d(">>>>>>", "in IF");
                SharedPreferences sharedPreferences = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(WIDGET_CITY + widgetID, editViewCityName.getText().toString());
                editor.apply();
                Log.d(">>>>>", "Entered city " + editViewCityName.getText().toString());
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
}
