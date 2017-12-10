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

public class DaysAdapter extends RecyclerView.Adapter<DaysViewHolder>{
    Context context;
    public DaysAdapter(Context context){
        this.context = context;
    }
    @Override
    public DaysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new DaysViewHolder(inflater, parent, context);
    }

    @Override
    public void onBindViewHolder(DaysViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return MainData.getInstance().getDayPerdiction().getHours().size();
    }
}
