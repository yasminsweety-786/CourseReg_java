import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class PopulateData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/university";
        String user = "root";
        String password = "24B11CS415"; 
        
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement()) {
             
            // Clean up old sample data
            st.execute("DELETE FROM registrations");
            st.execute("DELETE FROM students");
            st.execute("DELETE FROM courses");

            // Add sample Students (roll_no, name, password, department, semester, minor_stream)
            st.execute("INSERT INTO students VALUES ('1001', 'Yasmin', '1001', 'Computer Science', 1, 'None')");
            st.execute("INSERT INTO students VALUES ('2001', 'Ayaan', '2001', 'Mechanical', 2, 'None')");
            st.execute("INSERT INTO students VALUES ('3001', 'Ritik', '3001', 'Electrical', 3, 'None')");
            st.execute("INSERT INTO students VALUES ('4001', 'Kabir', '4001', 'Computer Science', 4, 'None')"); // 4th Sem Student

            // ------------------ ADD COURSES ------------------------
            // Sem 1 Courses (visible to Yasmin)
            st.execute("INSERT INTO courses VALUES (101, 'Engineering Math I', 1, 4)");
            st.execute("INSERT INTO courses VALUES (102, 'Basic Physics', 1, 3)");
            st.execute("INSERT INTO courses VALUES (103, 'C Programming', 1, 3)");
            st.execute("INSERT INTO courses VALUES (104, 'Engineering Graphics', 1, 2)");
            st.execute("INSERT INTO courses VALUES (105, 'Communication Skills', 1, 2)");
            st.execute("INSERT INTO courses VALUES (106, 'Basic Electrical Engg', 1, 3)");
            
            // Sem 2 Courses (visible to Ayaan)
            st.execute("INSERT INTO courses VALUES (201, 'Engineering Math II', 2, 4)");
            st.execute("INSERT INTO courses VALUES (202, 'Data Structures', 2, 4)");
            st.execute("INSERT INTO courses VALUES (203, 'Thermodynamics', 2, 3)");
            st.execute("INSERT INTO courses VALUES (204, 'Engineering Mechanics', 2, 3)");
            st.execute("INSERT INTO courses VALUES (205, 'Environmental Science', 2, 2)");
            
            // Sem 3 Courses (visible to Ritik)
            st.execute("INSERT INTO courses VALUES (301, 'Advanced Programming', 3, 4)");
            st.execute("INSERT INTO courses VALUES (302, 'Digital Logic Design', 3, 3)");
            st.execute("INSERT INTO courses VALUES (303, 'Network Theory', 3, 4)");
            st.execute("INSERT INTO courses VALUES (304, 'Operating Systems', 3, 4)");
            st.execute("INSERT INTO courses VALUES (305, 'Professional Ethics', 3, 2)");

            // Sem 4 Courses (visible to Kabir) -> Added lots of subjects for clear visibility!
            st.execute("INSERT INTO courses VALUES (401, 'Design & Analysis of Algorithms', 4, 4)");
            st.execute("INSERT INTO courses VALUES (402, 'Database Management Systems', 4, 4)");
            st.execute("INSERT INTO courses VALUES (403, 'Computer Architecture', 4, 3)");
            st.execute("INSERT INTO courses VALUES (404, 'Software Engineering', 4, 3)");
            st.execute("INSERT INTO courses VALUES (405, 'Web Technologies', 4, 3)");
            st.execute("INSERT INTO courses VALUES (406, 'Probability & Statistics', 4, 3)");
            st.execute("INSERT INTO courses VALUES (407, 'Microprocessors & Microcontrollers', 4, 4)");
            st.execute("INSERT INTO courses VALUES (408, 'Linux System Administration', 4, 2)");

            System.out.println("Dummy Data Created Successfully. Tons of 4th Sem Courses available!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
