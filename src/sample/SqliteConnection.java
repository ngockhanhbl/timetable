package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteConnection {

    private SqliteConnection(){

    }

    public static SqliteConnection getInstance() {
        return new SqliteConnection();
    }

    public Connection getConnection() {
        String connect_string = "jdbc:sqlite:data.db";
        Connection connection;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(connect_string);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return connection;
    }


}
