package fw.supernacho.ru.foxweather.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import fw.supernacho.ru.foxweather.MainActivity;
import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.data.DayPrediction;

/**
 * Created by SuperNacho on 12.11.2017.
 */

public class WeekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textViewDayDate;
    private TextView textViewDayIcon;
    private TextView textViewDayTemp;
    private Context context;
    private List<DayPrediction> daysList;
    private DateFormat timeStamp;

    WeekViewHolder(LayoutInflater inflater, ViewGroup parent, Context context){
        super(inflater.inflate(R.layout.day_list_item, parent, false));
        this.context = context;
        itemView.setOnClickListener(this);
        textViewDayDate = itemView.findViewById(R.id.text_view_day_date);
        textViewDayIcon = itemView.findViewById(R.id.text_view_day_icon);
        textViewDayTemp = itemView.findViewById(R.id.text_view_day_temp);
        daysList = MainData.getInstance().getWeekPrediction().getDaysList();
        timeStamp = new SimpleDateFormat("E, dd MMMM");

    }

    void bind (int position){
        DayPrediction day = daysList.get(position);
        textViewDayDate.setText(timeStamp.format(day.getDayDt() * 1000));
        textViewDayIcon.setText(String.valueOf(day.getDayIcoId()));
        textViewDayTemp.setText(String.valueOf(day.getDayTemp()));
    }

    @Override
    public void onClick(View view) {
//        showMoreInfo(this.getLayoutPosition());

    }

    private void showMoreInfo(int layoutPosition) {
        ((MainActivity)context).onListItemClick(layoutPosition);
    }
}
