package fw.supernacho.ru.foxweather;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fw.supernacho.ru.foxweather.data.DataBaseHelper;

/**
 * Created by SuperNacho on 05.02.2018.
 */

public class CsvParser {
    private DataBaseHelper dbHelper;
    private BufferedReader bufferedReader;
    private SQLiteDatabase dataBase;
    private Context context;

    public CsvParser(DataBaseHelper dbHelper, SQLiteDatabase dataBase, Context context){
        this.dbHelper = dbHelper;
        this.dataBase = dataBase;
        this.context = context;
    }

    public void parseCSV(int resurcesID){
        try {
            Resources resources = context.getResources();
            bufferedReader = new BufferedReader(new InputStreamReader(resources.openRawResource(R.raw.country_codes)));
            String line =null;
            while ((line = bufferedReader.readLine()) != null){
                String[] result = line.split(",");
                String title = result[0];
                Log.d("////", title);
                String tag = result[1];
                Log.d("////", tag);
                ContentValues values = new ContentValues();
                values.put(DataBaseHelper.COLUMN_TITLE, title);
                values.put(DataBaseHelper.COLUMN_TAG, tag);
                long ins = dataBase.insert(DataBaseHelper.TABLE_COUNTRIES, null, values);
                Log.d("////", String.valueOf(ins));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if ( bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
