package fw.supernacho.ru.foxweather.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import fw.supernacho.ru.foxweather.MainData;



public class DaysAdapter extends RecyclerView.Adapter<DaysViewHolder>{
    private Context context;
    public DaysAdapter(Context context){
        this.context = context;
    }
    @Override
    public DaysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new DaysViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(DaysViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return MainData.getInstance().getDayPrediction().getHours().size();
    }
}
