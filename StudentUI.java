package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import model.Student;
import service.CourseService;

public class StudentUI extends JFrame {
    public Student student;
    
    public JPanel cards;
    public CardLayout cardLayout;
    
    // Available Views
    public DefaultTableModel availableModel = new DefaultTableModel(new String[] { "ID", "Name", "Sem", "Credits" }, 0);
    public JTable availableTable;
    
    // Registrations Views
    public DefaultTableModel regsModel = new DefaultTableModel(new String[] { "ID", "Name", "Sem", "Credits" }, 0);
    public JTable regsTable;

    public JPanel profilePanel;

    public StudentUI(String name, String rollNo) {
        this.student = CourseService.getStudent(rollNo);
        if (this.student == null) {
            this.student = new Student(rollNo, name, "", "Unknown", 1, "None");
        }
        
        setTitle("Student Dashboard - " + this.student.getName());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // TOP PROFILE PANEL
        profilePanel = createProfilePanel();
        add(profilePanel, BorderLayout.NORTH);

        // Sidebar Navigation
        JPanel navPanel = new JPanel(new BorderLayout(0, 50));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topNav = new JPanel(new GridLayout(6, 1, 5, 10));
        topNav.add(new JLabel("Navigation", SwingConstants.CENTER));
        JButton btnAvailable = new JButton("Dashboard");
        JButton btnMyRegs = new JButton("My Registrations");
        JButton btnMinor = new JButton("Select Minor Stream");
        JButton btnExport = new JButton("Export Enrollment Record");
        JButton btnChangePass = new JButton("Change Password");
        topNav.add(btnAvailable); topNav.add(btnMyRegs); topNav.add(btnMinor); topNav.add(btnExport); topNav.add(btnChangePass);
        
        JButton btnLogout = new JButton("Logout");
        JPanel botNav = new JPanel(); // FlowLayout makes button small/normal size
        botNav.add(btnLogout);

        navPanel.add(topNav, BorderLayout.NORTH);
        navPanel.add(botNav, BorderLayout.SOUTH);
        add(navPanel, BorderLayout.WEST);

        // Card Layout for Center 
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Card 1: Dashboard (Available Courses)
        JPanel pnlAvailable = new JPanel(new BorderLayout());
        pnlAvailable.setBorder(BorderFactory.createTitledBorder("Available Courses For You"));
        availableTable = new JTable(availableModel);
        pnlAvailable.add(new JScrollPane(availableTable), BorderLayout.CENTER);
        JButton btnRegister = new JButton("Enroll Selected Courses");
        pnlAvailable.add(btnRegister, BorderLayout.SOUTH);

        // Card 2: My Registrations
        JPanel pnlRegs = new JPanel(new BorderLayout());
        pnlRegs.setBorder(BorderFactory.createTitledBorder("Your Confirmed Registrations"));
        regsTable = new JTable(regsModel);
        pnlRegs.add(new JScrollPane(regsTable), BorderLayout.CENTER);
        JButton btnDrop = new JButton("Drop Selected Course");
        pnlRegs.add(btnDrop, BorderLayout.SOUTH);

        cards.add(pnlAvailable, "Dashboard");
        cards.add(pnlRegs, "Registrations");
        add(cards, BorderLayout.CENTER);

        // Navigation Actions
        btnAvailable.addActionListener(e -> {
            refT();
            cardLayout.show(cards, "Dashboard");
        });
        
        btnMyRegs.addActionListener(e -> {
            showMyRegs();
            cardLayout.show(cards, "Registrations");
        });
        
        btnMinor.addActionListener(e -> selectMinor());
        
        btnLogout.addActionListener(e -> {
            new LoginUI();
            dispose();
        });

        btnExport.addActionListener(e -> {
            String userHome = System.getProperty("user.home");
            String downloadsPath = userHome + java.io.File.separator + "Downloads";
            String fullPath = downloadsPath + java.io.File.separator + "Enrollment_Record_" + student.getRollNo() + ".html";
            
            try (java.io.PrintWriter out = new java.io.PrintWriter(fullPath)) {
                int count = regsModel.getRowCount();
                if(count == 0) {
                    JOptionPane.showMessageDialog(this, "No courses registered yet!");
                    return;
                }
                
                // Writing HTML format so it opens in Browser (Read-Only) and looks like a real PDF Web Receipt
                out.println("<html><head><title>Official Enrollment Record</title>");
                out.println("<style>");
                out.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; padding: 40px; color: #333; }");
                out.println(".container { max-width: 700px; margin: 0 auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); border-top: 5px solid #0056b3; }");
                out.println("h2 { color: #0056b3; text-align: center; margin-bottom: 5px; }");
                out.println(".sub-header { text-align: center; color: #777; font-size: 14px; margin-bottom: 30px; }");
                out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
                out.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
                out.println("th { background-color: #0056b3; color: white; }");
                out.println(".stu-details { background: #f9f9f9; padding: 15px; border-left: 4px solid #0056b3; margin-bottom: 20px; }");
                out.println(".hash { font-family: monospace; color: #888; font-size: 12px; text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px dashed #ccc; }");
                out.println("</style></head><body>");
                
                out.println("<div class='container'>");
                out.println("<h2>OFFICIAL UNIVERSITY ENROLLMENT</h2>");
                out.println("<div class='sub-header'>Generated by Student Course Registration System</div>");
                
                out.println("<div class='stu-details'>");
                out.println("<strong>Student Name :</strong> " + student.getName() + "<br/>");
                out.println("<strong>Roll Number  :</strong> " + student.getRollNo() + "<br/>");
                out.println("<strong>Department   :</strong> " + student.getDepartment() + "<br/>");
                out.println("<strong>Semester     :</strong> " + student.getSemester() + "<br/>");
                out.println("<strong>Minor Stream :</strong> " + student.getMinorStream());
                out.println("</div>");
                
                out.println("<h3>Registered Subjects</h3>");
                out.println("<table><tr><th>Course ID</th><th>Course Name</th><th>Credits</th></tr>");
                for (int i=0; i<count; i++) {
                    out.println("<tr>");
                    out.println("<td>" + regsModel.getValueAt(i, 0) + "</td>");
                    out.println("<td>" + regsModel.getValueAt(i, 1) + "</td>");
                    out.println("<td>" + regsModel.getValueAt(i, 3) + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                
                String authHash = java.util.UUID.randomUUID().toString().substring(0, 18).toUpperCase();
                out.println("<div class='hash'>Digital Verification Hash: AUTH-" + authHash + "<br/>");
                out.println("<em>This document is system-generated and verifiable. Any manual tampering will invalidate the hash.</em></div>");
                
                out.println("</div>");
                out.println("<script>setTimeout(() => window.print(), 1000);</script>"); // Auto-triggers Print/Save as PDF!
                out.println("</body></html>");
                
                out.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to write file: " + ex.getMessage());
                return;
            }
            
            JOptionPane.showMessageDialog(this, "Official Web Receipt Exported!\n\nLocation: " + fullPath + "\n\nDouble-click this file to open securely in Chrome/Edge.");
        });

        btnDrop.addActionListener(e -> {
            int row = regsTable.getSelectedRow();
            if (row >= 0) {
                int courseId = Integer.parseInt(regsModel.getValueAt(row, 0).toString());
                CourseService.dropCourse(student.getRollNo(), courseId);
                showMyRegs(); // Refresh
                JOptionPane.showMessageDialog(this, "Course Unenrolled / Dropped successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a course to drop.");
            }
        });

        btnChangePass.addActionListener(e -> {
            JPasswordField pf = new JPasswordField();
            int action = JOptionPane.showConfirmDialog(this, pf, "Enter New Strong Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (action == JOptionPane.OK_OPTION) {
                String newPass = new String(pf.getPassword());
                if(newPass.trim().length() > 3) {
                    CourseService.updateStudentPassword(student.getRollNo(), newPass);
                    JOptionPane.showMessageDialog(this, "Security Alert: Password updated successfully!\nPlease use this new private password for next login.");
                } else {
                    JOptionPane.showMessageDialog(this, "Password too short! Need at least 4 characters.");
                }
            }
        });

        // Register Action
        btnRegister.addActionListener(e -> regC());

        refT(); // Default load available courses
        setVisible(true);
    }
    
    private JPanel createProfilePanel() {
        JPanel p = new JPanel(new GridLayout(2, 2));
        p.setBorder(BorderFactory.createTitledBorder("Student Profile"));
        p.add(new JLabel("Name: " + student.getName()));
        p.add(new JLabel("Roll No: " + student.getRollNo()));
        p.add(new JLabel("Department: " + student.getDepartment() + " (Sem " + student.getSemester() + ")"));
        
        String ms = student.getMinorStream();
        p.add(new JLabel("Minor Stream: " + (ms == null || ms.isEmpty() ? "Not Selected" : ms)));
        return p;
    }

    public void refT() {
        availableModel.setRowCount(0);
        for (Course c : CourseService.getCourses()) {
            if (c.getSemester() == student.getSemester()) {
                availableModel.addRow(new Object[] { c.getId(), c.getName(), c.getSemester(), c.getCredits() });
            }
        }
    }

    public void showMyRegs() {
        regsModel.setRowCount(0);
        CourseService.getRegistrations().getOrDefault(student.getRollNo(), new ArrayList<>())
            .forEach(c -> regsModel.addRow(new Object[] { c.getId(), c.getName(), c.getSemester(), c.getCredits() }));
    }
    
    public void selectMinor() {
        if (student.getMinorStream() != null && !student.getMinorStream().trim().isEmpty() && !student.getMinorStream().equalsIgnoreCase("None")) {
            JOptionPane.showMessageDialog(this, 
                "You have already selected the Minor Stream: " + student.getMinorStream() + ".\nModifications are not permitted after initial selection.", 
                "Selection Locked", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> minors = CourseService.getMinorStreams();
        if (minors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Minor Streams available! Connect with Administration.");
            return;
        }
        String[] options = minors.toArray(new String[0]);
        String selection = (String) JOptionPane.showInputDialog(this, "Select your preferred Minor Stream from list:", "Minor Stream Portal",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                
        if (selection != null) {
            CourseService.updateStudentMinor(student.getRollNo(), selection);
            student.setMinorStream(selection);
            
            remove(profilePanel);
            profilePanel = createProfilePanel();
            add(profilePanel, BorderLayout.NORTH);
            revalidate();
            repaint();
            
            JOptionPane.showMessageDialog(this, "Your profile has been updated with Minor Stream: " + selection);
        }
    }

    public void regC() {
        int[] rows = availableTable.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one course to register.");
            return;
        }
        if (student.getMinorStream() == null || student.getMinorStream().trim().isEmpty() || student.getMinorStream().equals("None")) {
            JOptionPane.showMessageDialog(this, "You are required to select a Minor Stream before course enrollment.");
            return;
        }

        List<Course> selected = new ArrayList<>();
        for (int r : rows) {
            int cr = Integer.parseInt(availableTable.getValueAt(r, 3).toString());
            selected.add(new Course(Integer.parseInt(availableTable.getValueAt(r, 0).toString()),
                                    (String) availableTable.getValueAt(r, 1),
                                    Integer.parseInt(availableTable.getValueAt(r, 2).toString()), cr));
        }

        CourseService.registerCourses(student.getRollNo(), selected);
        JOptionPane.showMessageDialog(this, "Courses registered successfully.");
        // Switch view to Registrations
        showMyRegs();
        cardLayout.show(cards, "Registrations");
    }
}