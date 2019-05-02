package models;

import controllers.Vars;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class SignupHandler {
    public boolean Signup(String name, String email, String password, String college, String department) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into Doctor (DoctorName, DoctorPassword, DoctorEmail, College_idCollege, DoctorDepartment) values (\"{0}\",\"{1}\",\"{2}\",{3},\"{4}\");"
                , name, password, email, college, department);
        return DBHandler.getInstance().execute_sql(sql);
    }

    public ObservableList<String> getCollegesList(){
        ObservableList<String> collegesList = FXCollections.observableArrayList();
        String sql =
                "SELECT * FROM College WHERE University_idUniversity = 1;"; //Todo: hard code edit with Univ. SW version

        ResultSet rs =  DBHandler.getInstance().execute_query(sql);
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
            //db.disconnect();
        }

    }
}
