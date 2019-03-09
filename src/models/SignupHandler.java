package models;

import controllers.DashboardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class SignupHandler {
    public boolean Signup(String name, String email, String password, String college) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into doctor (DoctorName, DoctorPassword, DoctorEmail, College_idCollege) values (\"{0}\",\"{1}\",\"{2}\",{3});"
                , name, password, email, college);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }

    public ObservableList<String> getCollegesList(){
        ObservableList<String> collegesList = FXCollections.observableArrayList();
        String sql =
                "SELECT * FROM college WHERE University_idUniversity = 1;"; //to be edited

        ResultSet rs =  DBSingletonHandler.getInstance().execute_query(sql);
        try {
            while (rs.next())
            {
                collegesList.add(rs.getString("Name"));
            }
            return collegesList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            //db.closeConnection();
        }

    }
}
