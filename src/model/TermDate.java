package model;

import java.util.ArrayList;

/**
 * Created by Masachi on 2017/5/16.
 */
public class TermDate {
    private String week;
    private ArrayList<String> weeks;

    public TermDate(){
        weeks = new ArrayList<>();
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public ArrayList<String> getWeeks() {
        return weeks;
    }

    public void setWeeks(ArrayList<String> weeks) {
        this.weeks = weeks;
    }
}
