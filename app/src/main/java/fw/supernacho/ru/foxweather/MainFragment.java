package fw.supernacho.ru.foxweather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fw.supernacho.ru.foxweather.recyclers.DaysAdapter;
import fw.supernacho.ru.foxweather.recyclers.WeekAdapter;

public class MainFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private WeatherInfoListener mainActivity;
    private DaysAdapter daysAdapter;
    private WeekAdapter weekAdapter;
    private TextView textViewCityName;

    interface WeatherInfoListener {
        void onListItemClick(int id);
    }

    WeatherInfoListener weatherInfo;

    void registerAdapted(WeatherInfoListener weatherInfo){
        this.weatherInfo = weatherInfo;
    }


    public MainFragment() {
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
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
        mainActivity = (WeatherInfoListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void init(View view){
        textViewCityName = view.findViewById(R.id.text_view_city_name);
        daysAdapter = ((MainActivity) mainActivity).getDaysAdapter();
        weekAdapter = ((MainActivity) mainActivity).getWeekAdapter();
        LinearLayoutManager dayLayoutManager = new LinearLayoutManager((Context) mainActivity);
        LinearLayoutManager weekLayoutManager = new LinearLayoutManager((Context) mainActivity);
        dayLayoutManager.setOrientation(MainActivity.HORIZONTAL);
        weekLayoutManager.setOrientation(MainActivity.VERTICAL);
        RecyclerView hoursRecycler = view.findViewById(R.id.recicler_view_hours);
        RecyclerView weekRecycler = view.findViewById(R.id.recycler_view_week);
        hoursRecycler.setLayoutManager(dayLayoutManager);
        hoursRecycler.setAdapter(daysAdapter);
        weekRecycler.setLayoutManager(weekLayoutManager);
        weekRecycler.setAdapter(weekAdapter);
    }

    public void setCityLabel(String cityName){
        textViewCityName.setText(cityName);
    }
}
