import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SetupDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "24B11CS415"; // Pulled from DBConnection.java
        
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement()) {
             
            st.execute("DROP DATABASE IF EXISTS university");
            st.execute("CREATE DATABASE university");
            st.execute("USE university");
            
            st.execute("CREATE TABLE students (roll_no VARCHAR(50) PRIMARY KEY, name VARCHAR(255), password VARCHAR(255), department VARCHAR(100), semester INT, minor_stream VARCHAR(100))");
            st.execute("CREATE TABLE courses (id INT PRIMARY KEY, name VARCHAR(255), semester INT, credits INT)");
            st.execute("CREATE TABLE registrations (id INT AUTO_INCREMENT PRIMARY KEY, roll_no VARCHAR(50), course_id INT, status VARCHAR(20) DEFAULT 'ENROLLED', FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE, FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE)");
            st.execute("CREATE TABLE minor_streams (id INT PRIMARY KEY, name VARCHAR(100))");
            
            // Populate initially
            st.execute("INSERT INTO minor_streams VALUES (1, 'AI & ML')");
            st.execute("INSERT INTO minor_streams VALUES (2, 'Data Science')");
            st.execute("INSERT INTO minor_streams VALUES (3, 'Cybersecurity')");

            System.out.println("Database reset and tables created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
