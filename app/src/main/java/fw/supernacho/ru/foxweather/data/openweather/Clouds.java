
package fw.supernacho.ru.foxweather.data.openweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Clouds {

    @SerializedName("all")
    @Expose
    private int all;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Clouds() {
    }

    /**
     * 
     * @param all
     */
    public Clouds(int all) {
        super();
        this.all = all;
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    public Clouds withAll(int all) {
        this.all = all;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("all", all).toString();
    }

}
