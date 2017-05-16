package crawler;

import config.DBconn;
import model.ExamDate;
import model.Grade;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Created by Masachi on 2017/5/15.
 */
public class DateCrawler {
    private Document doc;

    public boolean getDate(String origin, String number){
        origin = origin.replace("<tr>\n" +
                "    <th><a href=\"studentQueryAllExam.do?pagingPageVLID=1&amp;sortDirectionVLID=-1&amp;pagingNumberPerVLID=100&amp;sortColumnVLID=executionPlan.course.pcourseid&amp;\">课程号</a><img alt=\"Sort\" src=\"/academic/images/valuelist/sort(null).png\" border=\"0\"/></th>\n" +
                "    <th><a href=\"studentQueryAllExam.do?pagingPageVLID=1&amp;sortDirectionVLID=-1&amp;pagingNumberPerVLID=100&amp;sortColumnVLID=executionPlan.course.courseName&amp;\">课程名称</a><img alt=\"Sort\" src=\"/academic/images/valuelist/sort(null).png\" border=\"0\"/></th>\n" +
                "    <th><a href=\"studentQueryAllExam.do?pagingPageVLID=1&amp;sortDirectionVLID=-1&amp;pagingNumberPerVLID=100&amp;sortColumnVLID=examRoom.exam.endTime&amp;\">考试时间</a><img alt=\"Sort\" src=\"/academic/images/valuelist/sort(null).png\" border=\"0\"/></th>\n" +
                "    <th><a href=\"studentQueryAllExam.do?pagingPageVLID=1&amp;sortDirectionVLID=-1&amp;pagingNumberPerVLID=100&amp;sortColumnVLID=examRoom.room.rname&amp;\">考试地点</a><img alt=\"Sort\" src=\"/academic/images/valuelist/sort(null).png\" border=\"0\"/></th>\n" +
                "    <th><a href=\"studentQueryAllExam.do?pagingPageVLID=1&amp;sortDirectionVLID=-1&amp;pagingNumberPerVLID=100&amp;sortColumnVLID=examProperty.name&amp;\">考试性质</a><img alt=\"Sort\" src=\"/academic/images/valuelist/sort(null).png\" border=\"0\"/></th></tr>","")
                .replace("<tr class='classicLookPaging Paging'>\n" +
                        "     <td><img alt=\"关注被禁用\" src=\"/academic/images/valuelist/focus(disabled).gif\" border=\"0\"/></td>\n" +
                        "     <td><img alt=\"首页\" src=\"/academic/images/valuelist/first(off).gif\" border=\"0\"/></td>\n" +
                        "     <td><img alt=\"上一页\" src=\"/academic/images/valuelist/previous(off).gif\" border=\"0\"/></td>\n" +
                        "     <td><img alt=\"下一页\" src=\"/academic/images/valuelist/forward(off).gif\" border=\"0\"/></td>\n" +
                        "     <td><img alt=\"末页\" src=\"/academic/images/valuelist/last(off).gif\" border=\"0\"/></td>\n" +
                        "    </tr>","");
        doc = Jsoup.parse(origin);
        ArrayList<ExamDate> list = new ArrayList<>();

        Elements detail = doc.select("tr");
        for(Element date : detail){
            Elements info = date.select("td");
            if(info.size() == 5){
                //System.out.println(info.size());
                ExamDate examDate = new ExamDate();
                examDate.setNumber(number);
                examDate.setCourse(info.get(1).text());
                examDate.setDate(info.get(2).text().split(" ")[0]);
                //System.out.println(info.get(2).text().split(" ")[0]);
                examDate.setTime(info.get(2).text().split(" ")[1]);
                examDate.setLocation(info.get(3).text().replace("&nbsp;", " "));
                examDate.setInfo(info.get(4).text());
                list.add(examDate);
            }
        }

        try{
            Statement deleteGrade = DBconn.connection.createStatement();
            deleteGrade.execute("delete from exam where number = " + number);

            String sql = sql = "insert into exam(number, date, course, time, location, type) values(?,?,?,?,?,?)";
            PreparedStatement dateStatement = DBconn.connection.prepareStatement(sql);
            int i=0;
            for(ExamDate each : list){
                dateStatement.setString(1, each.getNumber());
                dateStatement.setString(2, each.getDate());
                dateStatement.setString(3, each.getCourse());
                dateStatement.setString(4, each.getTime());
                dateStatement.setString(5, each.getLocation());
                dateStatement.setString(6, each.getInfo());
                dateStatement.executeUpdate();
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

}
