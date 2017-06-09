package crawler;

import config.DBconn;
import config.DateCalculate;
import model.*;
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
        ArrayList<CourseInfo> infoList = new ArrayList();
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
            Elements table = courseInfo.select("table").select("tr");
            ArrayList<CourseDate> date = new ArrayList<>();
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
            course.setDate(date);
            list.add(course);
        }

        for(Element courseInfo : info){
            CourseInfo course_info = new CourseInfo();
            Elements courseDetail = courseInfo.select(".infolist");
            if(courseDetail.size() == 0){
                break;
            }
            course_info.setNumber(number);
            course_info.setCourse(courseDetail.get(0).text());
            //System.out.println(courseDetail.get(0).text());
            course_info.setWeight(courseDetail.get(1).text());
            Elements table = courseInfo.select("table").select("tr");
            String week = "";
            for(Element courseTemp : table){
                Elements tempDetail = courseTemp.select("td");
                //System.out.println(tempDetail.get(0).text());
                week = week + tempDetail.get(0).text() + " " + tempDetail.get(1).text() + " " + tempDetail.get(2).text()  + " " + tempDetail.get(3).text() + "\n";
            }
            course_info.setWeek(week);
            course_info.setInfo("test.......test...........\ntext..............\ntest..........................................");
            infoList.add(course_info);
        }

        DateCalculate.calculateDate(year);



        ArrayList<CourseData> dataList = new ArrayList<>();
        for(Course temp1 : list){
            //System.out.println(temp1.getCourse() + temp1.getTeacher());
            for(CourseDate temp2 : temp1.getDate()){
                System.out.println(temp2.getWeek() + temp1.getTeacher());
                String time[] = temp2.getWeek().replace("第","").split("-");
                if(time.length == 1) {
                    CourseData data = new CourseData();
                    data.setNumber(number);
                    data.setCourse(temp1.getCourse());
                    data.setTeacher(temp1.getTeacher());
                    if(!temp2.getDate().equals("&nbsp; ")) {
                        data.setDate(DateCalculate.date.get(Integer.parseInt(time[0].replace("周", "")) - 1).getWeeks().get(parseDay(temp2.getDate()) - 1));
                    }
                    else{
                        data.setDate("");
                    }
                    data.setTime(parseTime(temp2.getNum()));
                    data.setNum(temp2.getNum());
                    data.setWeek(DateCalculate.date.get(Integer.parseInt(time[0].replace("周", "")) - 1).getWeek());
                    if(!temp2.getLocation().equals("&nbsp; ")) {
                        data.setLocation(temp2.getLocation());
                    }
                    else{
                        data.setLocation("");
                    }
                    dataList.add(data);
                }
                else{
                    if(time[1].contains("单")){
                        int start = Integer.parseInt(time[0]);
                        int end = Integer.parseInt(time[1].replace("单周",""));
                        for(int i = start; i<=end; i=i+2){
                            CourseData data = new CourseData();
                            data.setNumber(number);
                            data.setCourse(temp1.getCourse());
                            data.setTeacher(temp1.getTeacher());
                            if(parseDay(temp2.getDate()) != 0) {
                                data.setDate(DateCalculate.date.get(i - 1).getWeeks().get(parseDay(temp2.getDate()) - 1));
                            }
                            else{
                                data.setDate("");
                            }
                            data.setTime(parseTime(temp2.getNum()));
                            data.setNum(temp2.getNum());
                            data.setWeek(DateCalculate.date.get(i - 1).getWeek());
                            if(!temp2.getLocation().equals("&nbsp; ")) {
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
                            if(parseDay(temp2.getDate()) != 0) {
                                data.setDate(DateCalculate.date.get(i - 1).getWeeks().get(parseDay(temp2.getDate()) - 1));
                            }
                            else{
                                data.setDate("");
                            }
                            data.setTime(parseTime(temp2.getNum()));
                            data.setNum(temp2.getNum());
                            data.setWeek(DateCalculate.date.get(i - 1).getWeek());
                            if(!temp2.getLocation().equals("&nbsp; ")) {
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

            String sql = "insert into course(number, date, time, num, week, course, teacher, location) values(?,?,?,?,?,?,?,?)";
            String sql2 = "insert into course_info(number, course, weight, week, info) values(?,?,?,?,?)";
            PreparedStatement courseStatement = DBconn.connection.prepareStatement(sql);
            PreparedStatement infoStatement = DBconn.connection.prepareStatement(sql2);
            int i=0;
            int j = 0;
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
                System.out.println("Yes");
            }

            for(CourseInfo each : infoList){
                infoStatement.setString(1, each.getNumber());
                infoStatement.setString(2, each.getCourse());
                infoStatement.setString(3, each.getWeight());
                infoStatement.setString(4, each.getWeek());
                infoStatement.setString(5, each.getInfo());
                infoStatement.executeUpdate();
                j++;
                System.out.println("Yes");
            }


            System.out.println("++++++++"+ i + "+++++++++++" + "++++++++"+ j + "+++++++++++");
            if(i == dataList.size() && j == infoList.size()){
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
