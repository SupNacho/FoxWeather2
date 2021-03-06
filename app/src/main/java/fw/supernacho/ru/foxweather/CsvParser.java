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
    private BufferedReader bufferedReader;
    private SQLiteDatabase dataBase;
    private Context context;

    public CsvParser(SQLiteDatabase dataBase, Context context){
        this.dataBase = dataBase;
        this.context = context;
    }

    public void parseCSV(int resourcesID){
        try {
            Resources resources = context.getResources();
            bufferedReader = new BufferedReader(new InputStreamReader(resources.openRawResource(resourcesID)));
            String line =null;
            dataBase.beginTransaction();
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    String[] result = line.split(",");
                    String title = result[0];
                    String tag = result[1];
                    ContentValues values = new ContentValues();
                    values.put(DataBaseHelper.COLUMN_TITLE, title);
                    values.put(DataBaseHelper.COLUMN_TAG, tag);
                    long ins = dataBase.insert(DataBaseHelper.TABLE_COUNTRIES, null, values);
                }
                dataBase.setTransactionSuccessful();
            } finally {
                dataBase.endTransaction();
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
