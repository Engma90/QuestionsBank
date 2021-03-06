package models;

import controllers.DashboardController;
import controllers.Vars;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class LoginHandler {


    private static volatile LoginHandler instance = null;

    private LoginHandler() {

    }

    public static LoginHandler getInstance() {
        if (instance == null) {
            // To provide thread-safe implementation.
            synchronized (LoginHandler.class) {
                if (instance == null) {
                    instance = new LoginHandler();
                }
            }
        }
        return instance;
    }


    private Doctor doctor = new Doctor();

    public boolean login(String email, String password) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT * FROM Doctor WHERE DoctorEmail =\"{0}\"  AND DoctorPassword = \"{1}\" ;"
                , email, password);
        ResultSet rs = DBHandler.getInstance().execute_query(sql);
        int dr_id = -1;
        try {
            if (rs.next()) {
                doctor.setId(rs.getInt("idDoctor") + "");
                doctor.setName(rs.getString("DoctorName"));
                doctor.setEmail(rs.getString("DoctorEmail"));
                doctor.setDepartment(rs.getString("DoctorDepartment"));
                //doctor.setPreferredExamLayout(rs.getString("PreferredExamLayout"));

                int College_idCollege = rs.getInt("College_idCollege");
                sql = MessageFormat.format(
                        "SELECT * FROM College WHERE idCollege =\"{0}\" ;"
                        , College_idCollege);
                rs = DBHandler.getInstance().execute_query(sql);
                if (rs.next()) {
                    doctor.setCollege(rs.getString("Name"));
                    int University_idUniversity = rs.getInt("University_idUniversity");
                    sql = MessageFormat.format(
                            "SELECT * FROM University WHERE idUniversity =\"{0}\" ;"
                            , University_idUniversity);
                    rs = DBHandler.getInstance().execute_query(sql);
                    if (rs.next()) {
                        //String colName = (doctor.getPreferredExamLayout().equals(Vars.Languages.ENGLISH)) ? "Name" : "AltName";
                        doctor.setUniversity(rs.getString("Name"));
                        doctor.setAltUniversity(rs.getString("AltName"));
                    }
                }

                //DashboardController.current_selected_dr_id = dr_id+"";
                DashboardController.doctor = doctor;
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
