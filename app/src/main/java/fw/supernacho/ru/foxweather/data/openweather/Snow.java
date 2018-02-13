
package fw.supernacho.ru.foxweather.data.openweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Snow {

    @SerializedName("3h")
    @Expose
    private double _3h;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Snow() {
    }

    /**
     * 
     * @param _3h
     */
    public Snow(double _3h) {
        super();
        this._3h = _3h;
    }

    public double get3h() {
        return _3h;
    }

    public void set3h(double _3h) {
        this._3h = _3h;
    }

    public Snow with3h(double _3h) {
        this._3h = _3h;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("_3h", _3h).toString();
    }

}
