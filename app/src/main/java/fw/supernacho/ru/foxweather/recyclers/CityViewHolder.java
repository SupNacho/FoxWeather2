package fw.supernacho.ru.foxweather.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fw.supernacho.ru.foxweather.MainActivity;
import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;

import static fw.supernacho.ru.foxweather.MainData.getInstance;

/**
 * Created by SuperNacho on 12.11.2017.
 */

public class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView cityNameTextView;
    private Context context;
    private List<String> cities;

    CityViewHolder(LayoutInflater inflater, ViewGroup parent, Context context){
        super(inflater.inflate(R.layout.city_list_item, parent, false));
        this.context = context;
        itemView.setOnClickListener(this);
        cityNameTextView = itemView.findViewById(R.id.city_name_text_view);
        cities = MainData.getInstance().getCities();
    }

    void bind (int position){
        String city = cities.get(position);
        cityNameTextView.setText(city);
    }

    @Override
    public void onClick(View view) {
        showMoreInfo(this.getLayoutPosition());
    }

    private void showMoreInfo(int layoutPosition) {
        ((MainActivity)context).onListItemClick(layoutPosition);
    }
}
