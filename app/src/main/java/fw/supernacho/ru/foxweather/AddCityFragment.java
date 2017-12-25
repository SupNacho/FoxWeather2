package fw.supernacho.ru.foxweather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;



public class AddCityFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context mainActivity;
    private EditText editTextNewCity;

    private OnFragmentInteractionListener mListener;

    public AddCityFragment() {
    }


    public static AddCityFragment newInstance(String param1, String param2) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_city, container, false);
        init(view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        mainActivity = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_add_city:
                String cityName = editTextNewCity.getText().toString();
                if (!cityName.isEmpty()) {
                    if(MainData.getInstance().addCity(cityName)) {
                        editTextNewCity.clearFocus();
                        editTextNewCity.setText(null);
                        ((MainActivity) mainActivity).updateCityWeather(cityName);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void init(View view){
        editTextNewCity = view.findViewById(R.id.edit_text_add_city);
        Button buttonAddCity = view.findViewById(R.id.button_add_city);
        buttonAddCity.setOnClickListener(this);
        editTextNewCity.requestFocus();
    }
}
