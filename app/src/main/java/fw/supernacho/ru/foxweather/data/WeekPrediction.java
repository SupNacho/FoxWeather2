package fw.supernacho.ru.foxweather.data;

import java.util.ArrayList;
import java.util.List;


public class WeekPrediction {
    private List<DayPrediction> daysList;

    public WeekPrediction() {
        daysList = new ArrayList<>();
    }

    public List<DayPrediction> getDaysList() {
        return daysList;
    }
}
