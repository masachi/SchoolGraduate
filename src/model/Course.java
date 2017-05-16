package model;

import java.util.ArrayList;

/**
 * Created by Masachi on 2017/5/15.
 */
public class Course {
    private String course;
    private String teacher;
    private ArrayList<CourseDate> date;

    public Course(){
        date = new ArrayList<>();
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public ArrayList<CourseDate> getDate() {
        return date;
    }

    public void setDate(ArrayList<CourseDate> date) {
        this.date = date;
    }
}
