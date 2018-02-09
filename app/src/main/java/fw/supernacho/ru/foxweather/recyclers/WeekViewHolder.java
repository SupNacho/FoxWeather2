package fw.supernacho.ru.foxweather.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import fw.supernacho.ru.foxweather.MainActivity;
import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.data.DayPrediction;

/**
 * Created by SuperNacho on 12.11.2017.
 */

public class WeekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textViewDayDate;
    private ImageView imageViewDayIcon;
    private TextView textViewDayTemp;
    private Context context;
    private List<DayPrediction> daysList;
    private DateFormat timeStamp;
    private TextView textViewPressure;
    private TextView textViewHumidity;
    private FrameLayout frameMoreInfo;

    WeekViewHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.day_list_item, parent, false));
        this.context = context;
        itemView.setOnClickListener(this);
        textViewDayDate = itemView.findViewById(R.id.text_view_day_date);
        imageViewDayIcon = itemView.findViewById(R.id.image_view_day_icon);
        textViewDayTemp = itemView.findViewById(R.id.text_view_day_temp);
        textViewPressure = itemView.findViewById(R.id.text_view_more_info_pressure_data);
        textViewHumidity = itemView.findViewById(R.id.text_view_more_info_humidity_data);
        frameMoreInfo = itemView.findViewById(R.id.frame_more_info);
        frameMoreInfo.setVisibility(View.GONE);
        textViewPressure.setText("Many additional info");
        daysList = MainData.getInstance().getWeekPrediction().getDaysList();
        timeStamp = new SimpleDateFormat("E, dd MMMM", Locale.US);

    }

    void bind(int position) {
        DayPrediction day = daysList.get(position);
        textViewDayDate.setText(timeStamp.format(day.getDayDt() * 1000));
        setIcon(day.getDayIcoId());
        textViewDayTemp.setText(String.valueOf(day.getStringDayTemp()));
        textViewPressure.setText(String.valueOf(day.getPressure()));
        textViewHumidity.setText(String.valueOf(day.getHumidity()));
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

    private void showMoreInfo(int layoutPosition) {
        ((MainActivity) context).onListItemClick(layoutPosition);
    }
}
