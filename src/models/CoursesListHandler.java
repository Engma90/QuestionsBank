package models;

import controllers.DashboardController;
import controllers.QuestionsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

public class CoursesListHandler {
    ObservableList<CourseModel> coursesList = FXCollections.observableArrayList();

    public boolean Add(String code, String name, String dr_id, String cat) {
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into course (CourseCategory, CourseName, CourseCode,Doctor_idDoctor) values (\"{0}\",\"{1}\",\"{2}\",{3}) ;"
                , cat, name, code, dr_id);
        return db.execute_sql(sql);
    }
    public boolean Edit(String code, String name, String id) {
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE course SET CourseName =\"{0}\" , CourseCode = \"{1}\"  WHERE idCourse = {2}  ;"
                ,name, code, id);
        return db.execute_sql(sql);
    }
    public boolean Delete(String id) {
        DBHandler db = new DBHandler();
        DashboardController.chaptersListHandler.DeleteAllSelectedCourseChapters();
        String sql = MessageFormat.format(
                "DELETE FROM course WHERE idCourse = {0}  ;",
                 id);
        return db.execute_sql(sql);
    }

    public ObservableList<CourseModel> getCoursesList(){
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT * FROM course WHERE Doctor_idDoctor ={0} AND CourseCategory = \"{1}\";"
                ,  DashboardController.current_selected_dr_id, DashboardController.degree_category);
        ResultSet rs =  db.execute_query(sql);
        try {
            while (rs.next())
            {
                coursesList.add(new CourseModel(rs.getInt("idCourse")+"",rs.getString("CourseName"),rs.getString("CourseCode")));
            }
            return coursesList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            db.closeConnection();
        }

    }
}
