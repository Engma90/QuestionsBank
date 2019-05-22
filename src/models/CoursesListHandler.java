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
                "insert into Course (CourseLevel, CourseName, CourseCode, CourseProgram, CourseYear,Doctor_idDoctor, PreferredExamLayout) values (?, ?, ?, ?, ?, ?, ?) ;";
                String[] params = new String[]{ course.level, course.name, course.code, course.program,course.year, DashboardController.doctor.getId(), course.getPreferredExamLayout()};
       return DBHandler.getInstance().execute_PreparedStatement(sql, params);
    }
    public boolean Edit(Course course) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE Course SET CourseName =\"{0}\" , CourseCode = \"{1}\", CourseProgram = \"{2}\", CourseLevel = \"{3}\", CourseYear = \"{4}\", PreferredExamLayout = \"{5}\"  WHERE idCourse = {6}  ;"
                ,course.name, course.code, course.program, course.level, course.year, course.getPreferredExamLayout(), course.id);
        return DBHandler.getInstance().execute_sql(sql);
    }
    public boolean Delete(String id) {
        //DBHandler db = new DBHandler();
        //boolean success = DashboardController.chaptersListHandler.DeleteAllSelectedCourseChapters();
        String sql = MessageFormat.format(
                "DELETE FROM Course WHERE idCourse = {0}  ;",
                 id);
        return DBHandler.getInstance().execute_sql(sql);
    }

    public ObservableList<Course> getList(String filter){
        //DBHandler db = new DBHandler();
        coursesList = FXCollections.observableArrayList();
        String sql;
        if(filter.equals("All")) {
            sql = MessageFormat.format(
                    "SELECT * FROM Course WHERE Doctor_idDoctor ={0};"
                    , DashboardController.doctor.getId());
        }else {
            sql = MessageFormat.format(
                    "SELECT * FROM Course WHERE Doctor_idDoctor ={0} AND CourseLevel = \"{1}\";"
                    , DashboardController.doctor.getId(), filter);
        }
        ResultSet rs =  DBHandler.getInstance().execute_query(sql);
        try {
            while (rs.next())
            {
                Course c = new Course(rs.getInt("idCourse")+"",
                        rs.getString("CourseName"),rs.getString("CourseCode"),
                        rs.getString("CourseLevel"),rs.getString("CourseYear"));
                c.setPreferredExamLayout(rs.getString("PreferredExamLayout"));
                c.setProgram(rs.getString("CourseProgram"));
                coursesList.add(c);
            }
            return coursesList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            //db.disconnect();
        }

    }

    public ObservableList<Course> getCachedList(){
        return this.coursesList;
    }
}
