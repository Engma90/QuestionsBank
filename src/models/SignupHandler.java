package models;

import java.text.MessageFormat;

public class SignupHandler {
    public boolean Signup(String name, String email, String password) {
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into doctor (DoctorName, DoctorPassword, DoctorEmail) values (\"{0}\",\"{1}\",\"{2}\");"
                , name, password, email);
        return db.execute_sql(sql);
    }
}
