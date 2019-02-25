package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.Statement;
import java.text.MessageFormat;

public class CoursesListHandler {
    ObservableList<CourseModel> coursesList;

    public boolean Add(String code, String name, String dr_id, String cat) {
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into course (CourseCategory, CourseName, CourseCode,Doctor_idDoctor) values (\"{0}\",\"{1}\",\"{2}\",{3}) ;"
                , cat, name, code, dr_id);
        return db.execute_sql(sql);
    }
    public ObservableList<CourseModel> getCoursesList(){
        coursesList = FXCollections.observableArrayList();

        coursesList.add(new CourseModel("1","Field","EEF"));
        coursesList.add(new CourseModel("2","Programming","EEP"));
        return coursesList;
    }

}
