package fw.supernacho.ru.foxweather.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;

/**
 * Created by SuperNacho on 12.11.2017.
 */

public class WeekAdapter extends RecyclerView.Adapter<WeekViewHolder>{
    private Context context;
    public WeekAdapter(Context context){
        this.context = context;
    }
    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new WeekViewHolder(inflater, parent, context);
    }

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return MainData.getInstance().getWeekPrediction().getDaysList().size();
    }
}
