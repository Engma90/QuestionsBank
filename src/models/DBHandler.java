package models;


import java.sql.*;
import java.text.MessageFormat;

public class DBHandler {
    private Connection connection;
    private Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/qbdb","root","Root@1234");

//            connection = DriverManager.getConnection(
//                    "jdbc:mysql://db4free.net:3306/gcp_b6f5e2b44df2","b4ff235c5f33bb","34022b76");

            return connection;
        }catch(Exception e){ System.out.println(e.getMessage());
            return null;}
    }
    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            return rs;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    int execute_update(String sql){
        try {
            Connection con = getConnection();
            Statement stmt=con.createStatement();
            System.out.println(sql);
            int rs = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            con.close();
            return rs;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return -1;
        }
    }


    int execute_PreparedStatement(String sql, String[] params){
        try {
            Connection con = getConnection();
            PreparedStatement pstmt =
                    con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i< params.length; i++)
                pstmt.setString(i+1, params[i]);
            System.out.println(sql);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int last_inserted_id = -1;
            if(rs.next())
            {
                last_inserted_id = rs.getInt(1);
            }
            System.out.println(rs);
            pstmt.close();
            con.close();
            return last_inserted_id;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return -1;
        }
    }
}
