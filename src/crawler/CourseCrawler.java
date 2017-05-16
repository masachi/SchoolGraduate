package crawler;

import config.DBconn;
import config.DateCalculate;
import model.Course;
import model.CourseData;
import model.CourseDate;
import model.Grade;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.CORBA.INTERNAL;

import java.lang.annotation.ElementType;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Masachi on 2017/5/15.
 */
public class CourseCrawler {
    private Document doc;

    public boolean getCourseCrawler(String origin, String number){
        ArrayList<Course> list = new ArrayList<>();
        ArrayList<CourseDate> date = new ArrayList<>();
        doc = Jsoup.parse(origin);
        String year = doc.select("#title").select("tr").select("td").select("div").text();
        System.out.println(year);
        Elements info = doc.select(".infolist_common");
        for(Element courseInfo : info){
            Course course = new Course();
            Elements courseDetail = courseInfo.select(".infolist");
            if(courseDetail.size() == 0){
                break;
            }
            course.setCourse(courseDetail.get(0).text());
            //System.out.println(courseDetail.get(0).text());
            course.setTeacher(courseDetail.get(1).text());
            Elements table = courseInfo.select(".none").select("tr");
            for(Element courseTemp : table){
                CourseDate courseDate = new CourseDate();
                Elements tempDetail = courseTemp.select("td");
                //System.out.println(tempDetail.get(0).text());
                courseDate.setWeek(tempDetail.get(0).text());
                courseDate.setDate(tempDetail.get(1).text());
                courseDate.setNum(tempDetail.get(2).text());
                courseDate.setLocation(tempDetail.get(3).text());
                date.add(courseDate);
            }
            list.add(course);
        }

        DateCalculate.calculateDate(year);



        ArrayList<CourseData> dataList = new ArrayList<>();
        for(Course temp1 : list){
            for(CourseDate temp2 : date){
                String time[] = temp2.getWeek().replace("第","").split("-");
                if(time.length == 1) {
                    CourseData data = new CourseData();
                    data.setNumber(number);
                    data.setCourse(temp1.getCourse());
                    data.setTeacher(temp1.getTeacher());
                    if(!temp2.getDate().equals("")) {
                        data.setDate(DateCalculate.date.get(Integer.parseInt(time[0].replace("周", "")) - 1).getWeeks().get(parseDay(temp2.getDate()) - 1));
                    }
                    else{
                        data.setDate("");
                    }
                    if(!temp2.getNum().equals("")) {
                        data.setTime(parseTime(temp2.getNum()));
                    }
                    else{
                        data.setTime("");
                    }
                    data.setWeek(DateCalculate.date.get(Integer.parseInt(time[0].replace("周", "")) - 1).getWeek());
                    if(!temp2.getLocation().equals("")) {
                        data.setLocation(temp2.getLocation());
                    }
                    else{
                        data.setLocation("");
                    }
                    dataList.add(data);
                }
                else{
                    if(time[1].substring(2,3).equals("单")){
                        int start = Integer.parseInt(time[0]);
                        int end = Integer.parseInt(time[1].replace("单周",""));
                        for(int i = start; i<=end; i=i+2){
                            CourseData data = new CourseData();
                            data.setNumber(number);
                            data.setCourse(temp1.getCourse());
                            data.setTeacher(temp1.getTeacher());
                            if(!temp2.getDate().equals("")) {
                                data.setDate(DateCalculate.date.get(i - 1).getWeeks().get(parseDay(temp2.getDate()) - 1));
                            }
                            else{
                                data.setDate("");
                            }
                            if(!temp2.getNum().equals("")) {
                                data.setTime(parseTime(temp2.getNum()));
                            }
                            else{
                                data.setTime("");
                            }
                            data.setWeek(DateCalculate.date.get(i - 1).getWeek());
                            if(!temp2.getLocation().equals("")) {
                                data.setLocation(temp2.getLocation());
                            }
                            else{
                                data.setLocation("");
                            }
                            dataList.add(data);
                        }
                    }
                    else{
                        int start = Integer.parseInt(time[0]);
                        int end = Integer.parseInt(time[1].replace("周",""));
                        for(int i = start; i<=end; i++){
                            CourseData data = new CourseData();
                            data.setNumber(number);
                            data.setCourse(temp1.getCourse());
                            data.setTeacher(temp1.getTeacher());
                            if(!temp2.getDate().equals("")) {
                                data.setDate(DateCalculate.date.get(i - 1).getWeeks().get(parseDay(temp2.getDate()) - 1));
                            }
                            else{
                                data.setDate("");
                            }
                            if(!temp2.getNum().equals("")) {
                                data.setTime(parseTime(temp2.getNum()));
                            }
                            else{
                                data.setTime("");
                            }
                            data.setWeek(DateCalculate.date.get(i - 1).getWeek());
                            if(!temp2.getLocation().equals("")) {
                                data.setLocation(temp2.getLocation());
                            }
                            else{
                                data.setLocation("");
                            }
                            dataList.add(data);
                        }
                    }
                }

            }
        }

        try{
            Statement deleteGrade = DBconn.connection.createStatement();
            deleteGrade.execute("delete from course where number = " + number);

            String sql = sql = "insert into course(number, date, time, num, week, course, teacher, location) values(?,?,?,?,?,?,?,?)";
            PreparedStatement courseStatement = DBconn.connection.prepareStatement(sql);
            int i=0;
            for(CourseData each : dataList){
                courseStatement.setString(1, each.getNumber());
                courseStatement.setString(2, each.getDate());
                courseStatement.setString(3, each.getTime());
                courseStatement.setString(4, each.getNum());
                courseStatement.setString(5, each.getWeek());
                courseStatement.setString(6, each.getCourse());
                courseStatement.setString(7, each.getTeacher());
                courseStatement.setString(8, each.getLocation());
                courseStatement.executeUpdate();
                i++;
            }
            if(i == list.size()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private int parseDay(String temp){
        switch (temp){
            case "星期一":
                return 1;
            case "星期二":
                return 2;
            case "星期三":
                return 3;
            case "星期四":
                return 4;
            case "星期五":
                return 5;
        }
        return 0;
    }

    private String parseTime(String temp){
        switch (temp){
            case "第一大节":
                return "08:10 -- 09:50";
            case "第二大节":
                return "10:10 -- 11:50";
            case "第三大节":
                return "13:30 -- 15:10";
            case "第四大节":
                return "15:20 -- 17:00";
            case "第五大节":
                return "18:00 -- 19:40";
        }
        return "";
    }
}
