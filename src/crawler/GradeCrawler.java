package crawler;

import config.DBconn;
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
public class GradeCrawler {
    private Document doc;

    public boolean getGrade(String origin, String number){
        doc = Jsoup.parse(origin);
        ArrayList<Grade> list = new ArrayList<>();

        Elements detail = doc.select("tr");
        for(Element info : detail){
            Elements temp = info.select("td");
            if(temp.size() == 13){
                Grade grade = new Grade();
                grade.setNumber(number);
                grade.setYear(temp.get(0).text());
                grade.setTerm(temp.get(1).text());
                grade.setCourse(temp.get(3).text());
                //System.out.println(temp.get(3).text());
                grade.setScore(temp.get(6).text());
                grade.setType(temp.get(11).text());
                grade.setFlag(temp.get(12).text());
                list.add(grade);
            }
        }

//        System.out.println(list.size());

        try{
            Statement deleteGrade = DBconn.connection.createStatement();
            deleteGrade.execute("delete from score where number = " + number);

            String sql = sql = "insert into score(number, year, term, course, score, type, flag) values(?,?,?,?,?,?,?)";
            PreparedStatement gradeStatement = DBconn.connection.prepareStatement(sql);
            int i=0;
            for(Grade each : list){
                gradeStatement.setString(1, each.getNumber());
                gradeStatement.setString(2, each.getYear());
                gradeStatement.setString(3, each.getTerm());
                gradeStatement.setString(4, each.getCourse());
                gradeStatement.setString(5, each.getScore());
                gradeStatement.setString(6, each.getType());
                gradeStatement.setString(7, each.getFlag());
                gradeStatement.executeUpdate();
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
//        return false;
    }
}
