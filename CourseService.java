package service;

import model.Course;
import model.Student;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class CourseService {

    public static void addStudent(Student s) {
        String sql = "INSERT INTO students (roll_no, name, password, department, semester, minor_stream) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPassword());
            ps.setString(4, s.getDepartment());
            ps.setInt(5, s.getSemester());
            ps.setString(6, s.getMinorStream());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validateStudent(String name, String rollNo, String password) {
        String sql = "SELECT * FROM students WHERE name=? AND roll_no=? AND password=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, rollNo);
            ps.setString(3, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Student getStudent(String rollNo) {
        String sql = "SELECT * FROM students WHERE roll_no=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getString("roll_no"), rs.getString("name"), rs.getString("password"),
                        rs.getString("department"), rs.getInt("semester"), rs.getString("minor_stream")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateStudentMinor(String rollNo, String minorStream) {
        String sql = "UPDATE students SET minor_stream=? WHERE roll_no=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, minorStream);
            ps.setString(2, rollNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Minors with ID
    public static void addMinorStream(int id, String name) {
        String sql = "INSERT IGNORE INTO minor_streams (id, name) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getMinorStreams() {
        List<String> streams = new ArrayList<>();
        String sql = "SELECT DISTINCT name FROM minor_streams";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                streams.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return streams;
    }
    
    public static Map<Integer, String> getMinorStreamsWithId() {
        Map<Integer, String> streams = new HashMap<>();
        String sql = "SELECT id, name FROM minor_streams ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                streams.put(rs.getInt("id"), rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return streams;
    }

    // ✅ Add Course
    public static void addCourse(Course c) {
        String sql = "INSERT INTO courses (id, name, semester, credits) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE name=VALUES(name), semester=VALUES(semester), credits=VALUES(credits)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getId());
            ps.setString(2, c.getName());
            ps.setInt(3, c.getSemester());
            ps.setInt(4, c.getCredits());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY semester, id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"), rs.getString("name"), rs.getInt("semester"), rs.getInt("credits")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static void registerCourses(String rollNo, List<Course> selected) {
        String deleteSql = "DELETE FROM registrations WHERE roll_no = ?";
        String insertSql = "INSERT INTO registrations (roll_no, course_id, status) VALUES (?, ?, 'ENROLLED')";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement deletePs = con.prepareStatement(deleteSql);
                 PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                
                deletePs.setString(1, rollNo);
                deletePs.executeUpdate();
                
                for (Course c : selected) {
                    insertPs.setString(1, rollNo);
                    insertPs.setInt(2, c.getId());
                    insertPs.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<Course>> getRegistrations() {
        Map<String, List<Course>> registrations = new HashMap<>();
        String sql = "SELECT r.roll_no, c.id, c.name, c.semester, c.credits " +
                     "FROM registrations r JOIN courses c ON r.course_id = c.id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String rollNo = rs.getString("roll_no");
                Course course = new Course(rs.getInt("id"), rs.getString("name"), rs.getInt("semester"), rs.getInt("credits"));
                registrations.computeIfAbsent(rollNo, k -> new ArrayList<>()).add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registrations;
    }

    public static void dropCourse(String rollNo, int courseId) {
        String sql = "DELETE FROM registrations WHERE roll_no = ? AND course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getCount(String table) {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {}
        return 0;
    }

    public static int getEnrolledStudentsCount() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(DISTINCT roll_no) FROM registrations")) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {}
        return 0;
    }

    public static void updateStudentPassword(String rollNo, String newPassword) {
        String sql = "UPDATE students SET password = ? WHERE roll_no = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, rollNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
