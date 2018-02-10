package fw.supernacho.ru.foxweather.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.data.DayPrediction;
import fw.supernacho.ru.foxweather.data.weather.Wind;

/**
 * Created by SuperNacho on 12.11.2017.
 */

public class WeekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textViewDayDate;
    private ImageView imageViewDayIcon;
    private TextView textViewDayTemp;
    private List<DayPrediction> daysList;
    private DateFormat timeStamp;
    private TextView textViewPressure;
    private TextView textViewHumidity;
    private TextView textViewWindDegreeM;
    private TextView textViewWindDegreeD;
    private TextView textViewWindDegreeE;
    private TextView textViewWindDegreeN;
    private TextView textViewWindSpeedM;
    private TextView textViewWindSpeedD;
    private TextView textViewWindSpeedE;
    private TextView textViewWindSpeedN;
    private LinearLayout frameMoreInfo;

    WeekViewHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.day_list_item, parent, false));
        itemView.setOnClickListener(this);
        textViewDayDate = itemView.findViewById(R.id.text_view_day_date);
        imageViewDayIcon = itemView.findViewById(R.id.image_view_day_icon);
        textViewDayTemp = itemView.findViewById(R.id.text_view_day_temp);
        textViewPressure = itemView.findViewById(R.id.text_view_more_info_pressure_data);
        textViewHumidity = itemView.findViewById(R.id.text_view_more_info_humidity_data);
        textViewWindDegreeM = itemView.findViewById(R.id.text_view_more_info_degree_data);
        textViewWindDegreeD = itemView.findViewById(R.id.text_view_more_info_degree_data_d);
        textViewWindDegreeE = itemView.findViewById(R.id.text_view_more_info_degree_data_e);
        textViewWindDegreeN = itemView.findViewById(R.id.text_view_more_info_degree_data_n);
        textViewWindSpeedM = itemView.findViewById(R.id.text_view_more_info_wind_speed);
        textViewWindSpeedD = itemView.findViewById(R.id.text_view_more_info_wind_speed_d);
        textViewWindSpeedE = itemView.findViewById(R.id.text_view_more_info_wind_speed_e);
        textViewWindSpeedN = itemView.findViewById(R.id.text_view_more_info_wind_speed_n);
        frameMoreInfo = itemView.findViewById(R.id.frame_more_info);
        frameMoreInfo.setVisibility(View.GONE);
        daysList = MainData.getInstance().getWeekPrediction().getDaysList();
        timeStamp = new SimpleDateFormat("E, dd MMMM", Locale.US);

    }

    void bind(int position) {
        DayPrediction day = daysList.get(position);
        List<Wind> winds = day.getWinds();
        textViewDayDate.setText(timeStamp.format(day.getDayDt() * 1000));
        setIcon(day.getDayIcoId());
        textViewDayTemp.setText(String.valueOf(day.getStringDayTemp()));
        textViewPressure.setText(String.valueOf(day.getPressure()));
        textViewHumidity.setText(String.valueOf(day.getHumidity()));
        textViewWindDegreeM.setText(String.valueOf(winds.get(0).getDeg()));
        textViewWindSpeedM.setText(String.valueOf(winds.get(0).getSpeed()));
        textViewWindDegreeD.setText(String.valueOf(winds.get(1).getDeg()));
        textViewWindSpeedD.setText(String.valueOf(winds.get(1).getSpeed()));
        textViewWindDegreeE.setText(String.valueOf(winds.get(2).getDeg()));
        textViewWindSpeedE.setText(String.valueOf(winds.get(2).getSpeed()));
        textViewWindDegreeN.setText(String.valueOf(winds.get(3).getDeg()));
        textViewWindSpeedN.setText(String.valueOf(winds.get(3).getSpeed()));
    }

    private void setIcon(int iconId) {
        int id = iconId / 100;
        if (iconId == 800) {
            imageViewDayIcon.setImageResource(R.drawable.weather_icon_sun8001);
        } else {
            switch (id) {
                case 2:
                    imageViewDayIcon.setImageResource(R.drawable.weather_icon_thunder200);
                    break;
                case 3:
                    imageViewDayIcon.setImageResource(R.drawable.weather_icon_drizzle300);
                    break;
                case 5:
                    imageViewDayIcon.setImageResource(R.drawable.weather_icon_rain500);
                    break;
                case 6:
                    imageViewDayIcon.setImageResource(R.drawable.weather_icon_snow600);
                    break;
                case 7:
                    imageViewDayIcon.setImageResource(R.drawable.weather_icon_mist700);
                    break;
                case 8:
                    imageViewDayIcon.setImageResource(R.drawable.weather_icon_clouds801);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
// TODO: 25.12.2017 add some functional for view more details
        if (frameMoreInfo.getVisibility() == View.GONE) {
            frameMoreInfo.setVisibility(View.VISIBLE);
        } else {
            frameMoreInfo.setVisibility(View.GONE);
        }
    }
}
