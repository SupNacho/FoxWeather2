package fw.supernacho.ru.foxweather.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fox_weather.db";
    private static final int DATABASE_VERSION = 5;
    static final String TABLE_CITIES = "cities";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_CITY = "city";
    static final String TABLE_WEATHER_LOGS = "weather_logs";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_TEMP = "temperature";
    static final String COLUMN_ICON = "icon";
    static final String COLUMN_SUMMARY = "summary";
    public static final String TABLE_COUNTRIES = "countries";
    public static final String COLUMN_ID_COUNTRIES = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TAG = "tag";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CITIES + "( "
                + COLUMN_ID
                +" INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COLUMN_CITY + " TEXT ); ");
        db.execSQL("CREATE TABLE " + TABLE_WEATHER_LOGS + "( "
                + COLUMN_ID
                +" INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COLUMN_CITY + " TEXT , " + COLUMN_DATE + " LONG , "
                + COLUMN_TEMP + " INTEGER , "
                + COLUMN_ICON + " INTEGER , " + COLUMN_SUMMARY + " BLOB );");
        db.execSQL("CREATE TABLE " + TABLE_COUNTRIES + "( "
                + COLUMN_ID_COUNTRIES
                +" INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COLUMN_TITLE + " TEXT , " + COLUMN_TAG + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER_LOGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
            onCreate(db);
        }
    }
}
