package fw.supernacho.ru.foxweather;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import fw.supernacho.ru.foxweather.data.DataBaseHelper;

/**
 * Created by SuperNacho on 05.02.2018.
 */

public class CsvParser {
    private DataBaseHelper dbHelper;
    private SQLiteDatabase dataBase;
    private BufferedReader bufferedReader;
    
    CsvParser(DataBaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    public void parseCSV(File file){
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line =null;
            while ((line = bufferedReader.readLine()) != null){
                String[] result = line.split(",");
                String title = result[0];
                String tag = result[1];
                dataBase = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DataBaseHelper.COLUMN_TITLE, title);
                values.put(DataBaseHelper.COLUMN_TAG, tag);
                dataBase.insert(DataBaseHelper.TABLE_COUNTRIES, null, values);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
