
package fw.supernacho.ru.foxweather.data.openweather;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class WeatherData {

    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private double message;
    @SerializedName("cnt")
    @Expose
    private int cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<fw.supernacho.ru.foxweather.data.openweather.List> list = new ArrayList<fw.supernacho.ru.foxweather.data.openweather.List>();
    @SerializedName("city")
    @Expose
    private City city;

    /**
     * No args constructor for use in serialization
     * 
     */
    public WeatherData() {
    }

    /**
     * 
     * @param message
     * @param cnt
     * @param cod
     * @param list
     * @param city
     */
    public WeatherData(String cod, double message, int cnt, java.util.List<fw.supernacho.ru.foxweather.data.openweather.List> list, City city) {
        super();
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public WeatherData withCod(String cod) {
        this.cod = cod;
        return this;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public WeatherData withMessage(double message) {
        this.message = message;
        return this;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public WeatherData withCnt(int cnt) {
        this.cnt = cnt;
        return this;
    }

    public java.util.List<fw.supernacho.ru.foxweather.data.openweather.List> getList() {
        return list;
    }

    public void setList(java.util.List<fw.supernacho.ru.foxweather.data.openweather.List> list) {
        this.list = list;
    }

    public WeatherData withList(java.util.List<fw.supernacho.ru.foxweather.data.openweather.List> list) {
        this.list = list;
        return this;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public WeatherData withCity(City city) {
        this.city = city;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("cod", cod).append("message", message).append("cnt", cnt).append("list", list).append("city", city).toString();
    }

}
