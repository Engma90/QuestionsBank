package models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;

public class DBHandler {
    private Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/questionbankdb","root","Root@1234");
        }catch(Exception e){ System.out.println(e.getMessage());
            return null;}
    }

    boolean execute_sql(String sql){
        try {
            Connection con = getConnection();
            Statement stmt=con.createStatement();
            System.out.println(sql);
            stmt.execute(sql);
            con.close();
            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

    ResultSet execute_query(String sql){
        try {
            Connection con = getConnection();
            Statement stmt=con.createStatement();
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            con.close();
            return rs;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
