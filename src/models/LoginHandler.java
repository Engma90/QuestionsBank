package models;

import controllers.DashboardController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class LoginHandler {
    public Doctor dr = new Doctor();
    public boolean login(String email, String password) {
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT * FROM doctor WHERE DoctorEmail =\"{0}\"  AND DoctorPassword = \"{1}\" ;"
                ,  email, password);
        ResultSet rs =  db.execute_query(sql);
        int dr_id = -1;
        try {
            if(rs.next())
            {
                dr_id = rs.getInt("idDoctor");
                dr.id = dr_id + "";
                dr.name = rs.getString("DoctorName");


                DashboardController.current_selected_dr_id = dr_id+"";
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            db.closeConnection();
        }
        return false;
    }
}
