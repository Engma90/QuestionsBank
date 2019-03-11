package models;

import java.sql.*;

// Double Check Locking Principle
public class DBSingletonHandler {

    private static volatile DBSingletonHandler instance = null;
    private Connection connection;

    private DBSingletonHandler() {
        connection = getConnection();
    }

    public static DBSingletonHandler getInstance() {
        if (instance == null) {
            // To provide thread-safe implementation.
            synchronized (DBSingletonHandler.class) {
                if (instance == null) {
                    instance = new DBSingletonHandler();
                }
            }
        }
        return instance;
    }


    private Connection getConnection() {
        try {
            Connection _connection;
            Class.forName("com.mysql.jdbc.Driver");
            _connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/qbdb", "root", "Root@1234");

//            _connection = DriverManager.getConnection(
//                    "jdbc:mysql://db4free.net:3306/gcp_b6f5e2b44df2","b4ff235c5f33bb","34022b76");

            return _connection;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkConnection() {
        try {
            if (connection == null ||connection.isClosed()) {
                System.out.println("Connection is closed, reconnecting...");
                connection = getConnection();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    boolean execute_sql(String sql) {
        checkConnection();
        try {
            Statement stmt = connection.createStatement();
            System.out.println(sql);
            stmt.execute(sql);

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }

    ResultSet execute_query(String sql) {
        checkConnection();
        try {
            Statement stmt = connection.createStatement();
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    int execute_PreparedStatement(String sql, String[] params) {
        checkConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++)
                pstmt.setString(i + 1, params[i]);
            System.out.println(sql);
            for(String s:params)
                System.out.print(s + "   ");
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int last_inserted_id = -1;
            if (rs.next()) {
                last_inserted_id = rs.getInt(1);
            }
            //System.out.println(rs);
            pstmt.close();
            return last_inserted_id;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
    }
}
