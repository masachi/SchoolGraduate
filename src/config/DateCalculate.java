package config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.TermDate;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Masachi on 2017/5/16.
 */
public class DateCalculate {
    public static List<TermDate> date = new ArrayList<TermDate>();
    public static void calculateDate(String temp){
        String path = temp.replace(" 课程安排","") + ".json";
        try {
            date = new Gson().fromJson(new JsonReader(new FileReader(path)), new TypeToken<List<TermDate>>() {
            }.getType());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
