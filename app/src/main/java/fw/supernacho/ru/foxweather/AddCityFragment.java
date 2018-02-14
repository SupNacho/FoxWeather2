package fw.supernacho.ru.foxweather;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import java.util.List;
import fw.supernacho.ru.foxweather.data.openweather.Country;
import fw.supernacho.ru.foxweather.prefs.WeatherPreference;


public class AddCityFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String DEFAULT_COUNTRY_TAG = "RU";

    private WeatherPreference weatherPrefs;
    private Context mainActivity;
    private EditText editTextNewCity;
    private String countryTag;


    public AddCityFragment() {
    }


    public static AddCityFragment newInstance() {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_city, container, false);
        init(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        mainActivity = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_add_city:
                String cityName = editTextNewCity.getText().toString();
                if (!cityName.isEmpty()) {
                    String cityAndCountry = cityName + "," + countryTag;
                    if(MainData.getInstance().addCity(cityAndCountry)) {
                        editTextNewCity.clearFocus();
                        editTextNewCity.setText(null);
                        ((MainActivity) mainActivity).updateCityWeather(cityAndCountry);
                        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputManager != null) {
                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        FragmentTransaction transaction = ((MainActivity) mainActivity).getSupportFragmentManager().beginTransaction();
                        transaction.remove(this);
                        transaction.commit();
                    }
                } else {
                    Snackbar.make(view, "Empty City name field.", Snackbar.LENGTH_SHORT).show();
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


    private void init(View view){
        weatherPrefs = MainData.getInstance().getMain().getWeatherPreference();
        editTextNewCity = view.findViewById(R.id.edit_text_add_city);
        Spinner spinnerCountry = view.findViewById(R.id.spinner_country);
        List<Country> countries = MainData.getInstance().getCountries();
        SpinnerAdapter countiesAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_list_item_1,
                countries);
        spinnerCountry.setAdapter(countiesAdapter);
        Button buttonAddCity = view.findViewById(R.id.button_add_city);
        buttonAddCity.setOnClickListener(this);
        spinnerCountry.setOnItemSelectedListener(this);
        spinnerCountry.setSelection(weatherPrefs.getCountry(), true);
        editTextNewCity.requestFocus();
    }
}
