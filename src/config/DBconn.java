package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Masachi on 2017/5/16.
 */
public class DBconn {
    public static Connection connection;
    public static Statement statement;

    public void setConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");

            String url = "jdbc:mysql://182.254.152.66:3306/graduate?useUnicode=true&characterEncoding=utf8";    //JDBC的URL
            connection = DriverManager.getConnection(url, "root", "sizhaizhenexin");
            statement = connection.createStatement(); //创建Statement对象
            System.out.println("成功连接到数据库！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
