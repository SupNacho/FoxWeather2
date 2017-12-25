package fw.supernacho.ru.foxweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fw.supernacho.ru.foxweather.recyclers.DaysAdapter;
import fw.supernacho.ru.foxweather.recyclers.WeekAdapter;

public class MainFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private WeatherInfoListener mainActivity;
    private DaysAdapter daysAdapter;
    private WeekAdapter weekAdapter;
    private TextView textViewCityName;

    interface WeatherInfoListener {
        void onListItemClick(int id);
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


    @Override
    public void onAttach(Context context) {
        mainActivity = (WeatherInfoListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void init(View view) {
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
        ((MainActivity) mainActivity).onBind(view);
        BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                textViewCityName.setText(intent.getStringExtra("cityName"));
                daysAdapter.notifyDataSetChanged();
                weekAdapter.notifyDataSetChanged();
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(serviceReceiver,
                new IntentFilter("fw.supernacho.ru.foxweather.action.DATA_READY"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) mainActivity).onUnbindService(getView());
    }
}
