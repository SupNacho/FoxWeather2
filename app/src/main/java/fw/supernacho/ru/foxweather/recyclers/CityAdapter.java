package fw.supernacho.ru.foxweather.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import fw.supernacho.ru.foxweather.MainActivity;
import fw.supernacho.ru.foxweather.MainData;

/**
 * Created by SuperNacho on 12.11.2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityViewHolder>{
    private Context context;
    public CityAdapter(Context context){
        this.context = context;
    }
    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new CityViewHolder(inflater, parent, context);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return MainData.getInstance().getCities().size();
    }
}
