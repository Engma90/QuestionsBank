package models;

import controllers.DashboardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class CoursesListHandler {
    private ObservableList<Course> coursesList = FXCollections.observableArrayList();


    private static volatile CoursesListHandler instance = null;

    private CoursesListHandler() {

    }

    public static CoursesListHandler getInstance() {
        if (instance == null) {
            // To provide thread-safe implementation.
            synchronized (CoursesListHandler.class) {
                if (instance == null) {
                    instance = new CoursesListHandler();
                }
            }
        }
        return instance;
    }

    public boolean Add(String code, String name, String dr_id, String level) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into course (CourseCategory, CourseName, CourseCode,Doctor_idDoctor) values (\"{0}\",\"{1}\",\"{2}\",{3}) ;"
                , level, name, code, dr_id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
    public boolean Edit(String code, String name, String level, String id) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE course SET CourseName =\"{0}\" , CourseCode = \"{1}\", CourseCategory = \"{2}\"  WHERE idCourse = {3}  ;"
                ,name, code, level, id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
    public boolean Delete(String id) {
        //DBHandler db = new DBHandler();
        //boolean success = DashboardController.chaptersListHandler.DeleteAllSelectedCourseChapters();
        String sql = MessageFormat.format(
                "DELETE FROM course WHERE idCourse = {0}  ;",
                 id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }

    public ObservableList<Course> getCoursesList(String filter){
        //DBHandler db = new DBHandler();
        coursesList = FXCollections.observableArrayList();
        String sql;
        if(filter.equals("All")) {
            sql = MessageFormat.format(
                    "SELECT * FROM course WHERE Doctor_idDoctor ={0};"
                    , DashboardController.current_selected_dr_id);
        }else {
            sql = MessageFormat.format(
                    "SELECT * FROM course WHERE Doctor_idDoctor ={0} AND CourseCategory = \"{1}\";"
                    , DashboardController.current_selected_dr_id, filter);
        }
        ResultSet rs =  DBSingletonHandler.getInstance().execute_query(sql);
        try {
            while (rs.next())
            {
                coursesList.add(new Course(rs.getInt("idCourse")+"",rs.getString("CourseName"),rs.getString("CourseCode"),rs.getString("CourseCategory")));
            }
            return coursesList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            //db.closeConnection();
        }

    }

    public ObservableList<Course> getCachedList(){
        return this.coursesList;
    }
}
