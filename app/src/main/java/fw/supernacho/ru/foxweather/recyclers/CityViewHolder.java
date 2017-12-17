package fw.supernacho.ru.foxweather.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fw.supernacho.ru.foxweather.MainActivity;
import fw.supernacho.ru.foxweather.MainData;
import fw.supernacho.ru.foxweather.R;
import fw.supernacho.ru.foxweather.data.City;

import static fw.supernacho.ru.foxweather.MainData.getInstance;

/**
 * Created by SuperNacho on 12.11.2017.
 */

public class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView cityNameTextView;
    private ImageButton buttonDeleteCity;
    private Context context;
    private List<City> cities;

    CityViewHolder(LayoutInflater inflater, ViewGroup parent, Context context){
        super(inflater.inflate(R.layout.city_list_item, parent, false));
        this.context = context;
        itemView.setOnClickListener(this);
        cityNameTextView = itemView.findViewById(R.id.city_name_text_view);
        buttonDeleteCity = itemView.findViewById(R.id.button_delete_city);
        buttonDeleteCity.setOnClickListener(this);
        buttonDeleteCity.setVisibility(View.INVISIBLE);
    }

    void bind (int position){
        boolean menuEditable = ((MainActivity) context).isMenuEditable();
        if (menuEditable){
            buttonDeleteCity.setVisibility(View.VISIBLE);
        } else {
            buttonDeleteCity.setVisibility(View.INVISIBLE);
        }
        cities = MainData.getInstance().getCities();
        City city = cities.get(position);
        cityNameTextView.setText(city.getCityName());
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.button_delete_city:
               itemRemove(this.getLayoutPosition());
               break;
           default:
               showMoreInfo(this.getLayoutPosition());
               break;
       }
    }

    private void itemRemove(int pos){
        City removedCity = cities.remove(pos);
        MainData.getInstance().removeCity(removedCity);
    }

    private void showMoreInfo(int layoutPosition) {
        ((MainActivity)context).onListItemClick(layoutPosition);
    }
}
