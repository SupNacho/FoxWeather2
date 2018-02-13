package fw.supernacho.ru.foxweather.recyclers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.data.HourWeather;

public class DaysViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageViewWeatherIcon;
    private TextView textViewTime;
    private TextView textViewTemp;
    private List<HourWeather> hours;
    private DateFormat dateStamp;

    DaysViewHolder(LayoutInflater inflater, ViewGroup parent){
        super(inflater.inflate(R.layout.hour_list_item, parent, false));
        imageViewWeatherIcon = itemView.findViewById(R.id.weather_icon);
        textViewTime = itemView.findViewById(R.id.text_view_hours_time);
        textViewTemp = itemView.findViewById(R.id.text_view_temp);
        hours = MainData.getInstance().getDayPrediction().getHours();
        dateStamp = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    }

    void bind (int position){
        HourWeather hour = hours.get(position);
        setIcon(hour.getId());
        textViewTime.setText(dateStamp.format(hour.getDt() * 1000));
        textViewTemp.setText(String.valueOf(hour.getStringTemp()));
    }

    private void setIcon(int iconId){
        int id = iconId / 100;
        if (iconId == 800){
            imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_sun8001);
        } else {
            switch (id) {
                case 2:
                    imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_thunder200);
                    break;
                case 3:
                    imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_drizzle300);
                    break;
                case 5:
                    imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_rain500);
                    break;
                case 6:
                    imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_snow600);
                    break;
                case 7:
                    imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_mist700);
                    break;
                case 8:
                    imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_clouds801);
                    break;
            }
        }
    }
}
