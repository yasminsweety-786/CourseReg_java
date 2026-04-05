package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // Database URL
    public static final String URL = "jdbc:mysql://localhost:3306/university";

    // MySQL username
    public static final String USER = "root";

    // MySQL password (change this!)
    public static final String PASSWORD = "24B11CS415";

    public static Connection getConnection() throws Exception {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}