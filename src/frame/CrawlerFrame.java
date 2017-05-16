package frame;

import config.DBconn;
import crawler.CourseCrawler;
import crawler.DateCrawler;
import crawler.GradeCrawler;
import crawler.PersonalCrawler;
import model.Grade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Masachi on 2017/5/15.
 */
public class CrawlerFrame {
    private static JLabel label2;
    private static JTextArea row1;

    public static void main(String[] args){
        DBconn dBconn = new DBconn();
        dBconn.setConnection();

        JFrame frame = new JFrame("Graduate");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setLayout(new GridLayout(1,2));
        Container c = frame.getContentPane();
        JPanel panel1 = new JPanel(new GridLayout(5,2));
        JTextArea origin = new JTextArea();
        origin.setSize(700,400);
        origin.setLineWrap(true);
//                    panel.add(origin, FlowLayout.LEFT);
        JScrollPane pane = new JScrollPane(origin);
        c.add(pane);
        JLabel label = new JLabel();
        label.setText("学号");
        panel1.add(label);
        JTextArea number = new JTextArea();
        number.setSize(50,50);
        panel1.add(number);
//                    panel.add(cate,FlowLayout.LEFT);
//                    frame.add(cate,FlowLayout.LEADING);
//        JLabel label1 = new JLabel();
//        label1.setText("行数");
//        panel1.add(label1);
//        row1 = new JTextArea();
//        row1.setSize(50,100);
//        panel1.add(row1);
        JPanel contentPane = new JPanel();// 创建内容面板
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JRadioButton radioButton1 = new JRadioButton("课程");// 创建单选按钮
        contentPane.add(radioButton1);// 应用单选按钮

        JRadioButton radioButton2 = new JRadioButton("成绩");// 创建单选按钮
        contentPane.add(radioButton2);// 应用单选按钮

        JRadioButton radioButton3 = new JRadioButton("考试时间");// 创建单选按钮
        contentPane.add(radioButton3);// 应用单选按钮

        JRadioButton radioButton4 = new JRadioButton("个人信息");// 创建单选按钮
        contentPane.add(radioButton4);// 应用单选按钮

        ButtonGroup group = new ButtonGroup();// 创建单选按钮组
        group.add(radioButton1);// 将radioButton1增加到单选按钮组中
        group.add(radioButton2);// 将radioButton2增加到单选按钮组中
        group.add(radioButton3);
        group.add(radioButton4);// 将radioButton3增加到单选按钮组中
        radioButton1.setSelected(true);
        panel1.add(contentPane);
////                    row.setVisible(true);
////                    panel.add(row,FlowLayout.LEFT);
////                    frame.add(row,FlowLayout.LEADING);
        JButton button = new JButton();
        button.setText("输出到mysql");
        button.setVisible(true);
        button.setSize(20,20);
        panel1.add(button);
        label2 = new JLabel();
        label2.setText("");
        panel1.add(label2);
//                    panel.add(button,FlowLayout.LEFT);
        c.add(panel1);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label2.setText("");
                if(group.isSelected(radioButton1.getModel())){
                    CourseCrawler courseCrawler = new CourseCrawler();
                    if(courseCrawler.getCourseCrawler(origin.getText(),number.getText())){
                        label2.setText("Yes");
                    }
                    else{
                        label2.setText("No");
                    }
                }
                if(group.isSelected(radioButton2.getModel())){
                    GradeCrawler gradeCrawler = new GradeCrawler();
                    if(gradeCrawler.getGrade(origin.getText(), number.getText())){
                        label2.setText("Yes");
                    }
                    else{
                        label2.setText("No");
                    }
                }
                if(group.isSelected(radioButton3.getModel())){
                    DateCrawler dateCrawler = new DateCrawler();
                    if(dateCrawler.getDate(origin.getText(), number.getText())){
                        label2.setText("Yes");
                    }
                    else{
                        label2.setText("No");
                    }
                }
                if(group.isSelected(radioButton4.getModel())){
                    PersonalCrawler personalCrawler = new PersonalCrawler();
                    if(personalCrawler.getPersonalInfo(origin.getText(), number.getText())){
                        label2.setText("Yes");
                    }
                    else{
                        label2.setText("No");
                    }
                }
            }
        });
        frame.setVisible(true);
    }
}
