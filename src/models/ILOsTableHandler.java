package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class ILOsTableHandler {

    private ObservableList<ILO> ILOsList = FXCollections.observableArrayList();


    private static volatile ILOsTableHandler instance = null;

    private ILOsTableHandler() {

    }

    public static ILOsTableHandler getInstance() {
        if (instance == null) {
            // To provide thread-safe implementation.
            synchronized (ILOsTableHandler.class) {
                if (instance == null) {
                    instance = new ILOsTableHandler();
                }
            }
        }
        return instance;
    }

    public int Add(Course c, ILO ilo) {
        //DBHandler db = new DBHandler();
        String sql =
                "insert into CourseILO (Course_idCourse, Code, Description) values (?, ?, ?) ;";
        String[] params = new String[]{c.id, ilo.getCode(), ilo.getDescription()};
        return DBHandler.getInstance().execute_PreparedStatement(sql, params);
    }

    public boolean Edit(ILO ilo) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE CourseILO SET Code =\"{0}\" , Description = \"{1}\"  WHERE idCourseILOs = {2}  ;"
                , ilo.getCode(), ilo.getDescription(), ilo.getId());
        return DBHandler.getInstance().execute_sql(sql);
    }

    public boolean Delete(String id) {
        String sql = MessageFormat.format(
                "DELETE FROM CourseILO WHERE idCourseILOs = {0}  ;",
                id);
        return DBHandler.getInstance().execute_sql(sql);
    }

    public ObservableList<ILO> getList(Course course) {
        //DBHandler db = new DBHandler();
        ILOsList = FXCollections.observableArrayList();
        String sql;
        sql = MessageFormat.format(
                "SELECT * FROM CourseILO WHERE Course_idCourse ={0};"
                , course.id);

        ResultSet rs = DBHandler.getInstance().execute_query(sql);
        try {
            while (rs.next()) {
                ILO ilo = new ILO();
                ilo.setId(rs.getInt("idCourseILOs") + "");
                ilo.setCode(rs.getString("Code"));
                ilo.setDescription(rs.getString("Description"));

                ILOsList.add(ilo);
            }
            return ILOsList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //db.disconnect();
        }

    }

    public ObservableList<ILO> getCachedList() {
        return this.ILOsList;
    }
}
