package crawler;

import config.DBconn;
import model.Grade;
import model.Personal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by Masachi on 2017/5/15.
 */
public class PersonalCrawler {
    private Document doc;

    public boolean getPersonalInfo(String origin, String number){
        Personal personal = new Personal();
        doc = Jsoup.parse(origin);
        Elements temp = doc.getElementsByClass("form");
        Element profile = temp.get(0);
        Elements detail = profile.select("tr");
        personal.setNumber(detail.get(0).select("td").get(0).text());
        personal.setName(detail.get(0).select("td").get(1).text());
        personal.setAcademy(detail.get(1).select("td").get(0).text());
        personal.setFaculty(detail.get(1).select("td").get(1).text());
        personal.setStudent_class(detail.get(3).select("td").get(1).text());


        try{
            Statement deleteGrade = DBconn.connection.createStatement();
            deleteGrade.execute("delete from profile where number = " + number);

            String sql = sql = "insert into profile(number, name, avatar, academy, faculty, class) values(?,?,?,?,?,?)";
            PreparedStatement personalStatement = DBconn.connection.prepareStatement(sql);
                personalStatement.setString(1, personal.getNumber());
                personalStatement.setString(2, personal.getName());
                personalStatement.setString(3, "");
                personalStatement.setString(4, personal.getAcademy());
                personalStatement.setString(5, personal.getFaculty());
                personalStatement.setString(6, personal.getStudent_class());

            if(personalStatement.executeUpdate() > 0){
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
