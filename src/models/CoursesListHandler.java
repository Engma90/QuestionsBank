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

    public int Add(Course course) {
        //DBHandler db = new DBHandler();
        String sql =
                "insert into course (CourseLevel, CourseName, CourseCode, CourseYear,Doctor_idDoctor) values (?, ?, ?, ?, ?) ;";
                String[] params = new String[]{ course.level, course.name, course.code,course.year, DashboardController.doctor.getId()};
       return DBSingletonHandler.getInstance().execute_PreparedStatement(sql, params);
    }
    public boolean Edit(Course course) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE course SET CourseName =\"{0}\" , CourseCode = \"{1}\", CourseLevel = \"{2}\", CourseYear = \"{3}\"  WHERE idCourse = {4}  ;"
                ,course.name, course.code, course.level, course.year, course.id);
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

    public ObservableList<Course> getList(String filter){
        //DBHandler db = new DBHandler();
        coursesList = FXCollections.observableArrayList();
        String sql;
        if(filter.equals("All")) {
            sql = MessageFormat.format(
                    "SELECT * FROM course WHERE Doctor_idDoctor ={0};"
                    , DashboardController.doctor.getId());
        }else {
            sql = MessageFormat.format(
                    "SELECT * FROM course WHERE Doctor_idDoctor ={0} AND CourseLevel = \"{1}\";"
                    , DashboardController.doctor.getId(), filter);
        }
        ResultSet rs =  DBSingletonHandler.getInstance().execute_query(sql);
        try {
            while (rs.next())
            {
                coursesList.add(new Course(rs.getInt("idCourse")+"",rs.getString("CourseName"),rs.getString("CourseCode"),rs.getString("CourseLevel"),rs.getString("CourseYear")));
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
