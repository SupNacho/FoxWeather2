
package fw.supernacho.ru.foxweather.data.weather;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class List {

    @SerializedName("dt")
    @Expose
    private int dt;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("weather")
    @Expose
    private java.util.List<Weather> weather = new ArrayList<Weather>();
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;
    @SerializedName("rain")
    @Expose
    private Rain rain;
    @SerializedName("snow")
    @Expose
    private Snow snow;

    /**
     * No args constructor for use in serialization
     * 
     */
    public List() {
    }

    /**
     * 
     * @param clouds
     * @param dt
     * @param wind
     * @param sys
     * @param snow
     * @param dtTxt
     * @param weather
     * @param rain
     * @param main
     */
    public List(int dt, Main main, java.util.List<Weather> weather, Clouds clouds, Wind wind, Sys sys, String dtTxt, Rain rain, Snow snow) {
        super();
        this.dt = dt;
        this.main = main;
        this.weather = weather;
        this.clouds = clouds;
        this.wind = wind;
        this.sys = sys;
        this.dtTxt = dtTxt;
        this.rain = rain;
        this.snow = snow;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public List withDt(int dt) {
        this.dt = dt;
        return this;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List withMain(Main main) {
        this.main = main;
        return this;
    }

    public java.util.List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    public List withWeather(java.util.List<Weather> weather) {
        this.weather = weather;
        return this;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public List withClouds(Clouds clouds) {
        this.clouds = clouds;
        return this;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public List withWind(Wind wind) {
        this.wind = wind;
        return this;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public List withSys(Sys sys) {
        this.sys = sys;
        return this;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

    public List withDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
        return this;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public List withRain(Rain rain) {
        this.rain = rain;
        return this;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public List withSnow(Snow snow) {
        this.snow = snow;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("dt", dt).append("main", main).append("weather", weather).append("clouds", clouds).append("wind", wind).append("sys", sys).append("dtTxt", dtTxt).append("rain", rain).append("snow", snow).toString();
    }

}
