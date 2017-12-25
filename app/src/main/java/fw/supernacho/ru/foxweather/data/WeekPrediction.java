package fw.supernacho.ru.foxweather.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuperNacho on 09.12.2017.
 */

public class WeekPrediction {
    private List<DayPrediction> daysList;

    public WeekPrediction() {
        daysList = new ArrayList<>();
    }

    public void addDays(DayPrediction day){
        daysList.add(day);
    }

    public List<DayPrediction> getDaysList() {
        return daysList;
    }
}
