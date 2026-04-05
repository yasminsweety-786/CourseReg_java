package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import model.Course;
import model.Student;
import service.CourseService;

public class AdminUI extends JFrame {
    private JTable table;
    private JPanel statsPanel;

    public AdminUI() {
        setTitle("Admin Dashboard");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Stats Panel (Feature 2)
        statsPanel = new JPanel(new GridLayout(1, 3));
        statsPanel.setBorder(BorderFactory.createTitledBorder("System Statistics"));
        add(statsPanel, BorderLayout.NORTH);

        // Table inside a panel
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Sidebar Navigation
        JPanel navPanel = new JPanel(new BorderLayout(0, 50));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topNav = new JPanel(new GridLayout(7, 1, 5, 10));
        topNav.add(new JLabel("Admin Menu", SwingConstants.CENTER));
        
        String[] actions = { "Refresh Data", "Add Course", "Delete Course", "Add Student", "Minor Streams", "Registrations" };
        for (String action : actions) {
            JButton btn = new JButton(action);
            topNav.add(btn);
            btn.addActionListener(e -> {
                if (action.equals("Add Course")) addC();
                else if (action.equals("Delete Course")) delC();
                else if (action.equals("Add Student")) addS();
                else if (action.equals("Minor Streams")) showMinors();
                else if (action.equals("Registrations")) showR();
                else if (action.equals("Refresh Data")) refT();
            });
        }

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            new LoginUI();
            dispose();
        });
        JPanel botNav = new JPanel();
        botNav.add(btnLogout);

        navPanel.add(topNav, BorderLayout.NORTH);
        navPanel.add(botNav, BorderLayout.SOUTH);
        add(navPanel, BorderLayout.WEST);

        refT();
        setVisible(true);
    }

    public void refT() {
        // Update stats
        statsPanel.removeAll();
        statsPanel.add(new JLabel("Total Students Accounts: " + CourseService.getCount("students"), SwingConstants.CENTER));
        statsPanel.add(new JLabel("Active Courses: " + CourseService.getCount("courses"), SwingConstants.CENTER));
        statsPanel.add(new JLabel("Students Registered: " + CourseService.getEnrolledStudentsCount(), SwingConstants.CENTER));
        statsPanel.revalidate();
        statsPanel.repaint();

        java.util.ArrayList<Course> cs = CourseService.getCourses();
        String[][] d = new String[cs.size()][];
        for (int i = 0; i < cs.size(); i++) {
            Course c = cs.get(i);
            d[i] = new String[] {
                String.valueOf(c.getId()), c.getName(), String.valueOf(c.getSemester()),
                String.valueOf(c.getCredits())
            };
        }
        table.setModel(new javax.swing.table.DefaultTableModel(d, 
            new String[] { "COURSE ID", "COURSE NAME", "SEMESTER", "CREDITS" }));
    }

    public void addC() {
        JTextField idField = new JTextField(5);
        JTextField nameField = new JTextField(15);
        JTextField semField = new JTextField(5);
        JTextField crField = new JTextField(5);

        JPanel myPanel = new JPanel(new GridLayout(4, 2));
        myPanel.add(new JLabel("ID:")); myPanel.add(idField);
        myPanel.add(new JLabel("Name:")); myPanel.add(nameField);
        myPanel.add(new JLabel("Semester:")); myPanel.add(semField);
        myPanel.add(new JLabel("Credits:")); myPanel.add(crField);

        if (JOptionPane.showConfirmDialog(null, myPanel, "Enter Course Details", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                CourseService.addCourse(new Course(Integer.parseInt(idField.getText()), nameField.getText(), 
                                      Integer.parseInt(semField.getText()), Integer.parseInt(crField.getText())));
                refT();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        }
    }

    public void showMinors() {
        JDialog minorDialog = new JDialog(this, "Manage Minor Streams", true);
        minorDialog.setSize(400, 300);
        minorDialog.setLocationRelativeTo(this);
        minorDialog.setLayout(new BorderLayout());

        Map<Integer, String> minorData = CourseService.getMinorStreamsWithId();
        String[][] data = new String[minorData.size()][];
        int i = 0;
        for (Map.Entry<Integer, String> entry : minorData.entrySet()) {
            data[i++] = new String[]{ String.valueOf(entry.getKey()), entry.getValue() };
        }
        minorDialog.add(new JScrollPane(new JTable(data, new String[]{"Course ID", "Minor Stream Name"})), BorderLayout.CENTER);

        JButton addBtn = new JButton("Add Minor Stream");
        addBtn.addActionListener(e -> {
            JTextField idField = new JTextField(5);
            JTextField nameField = new JTextField(15);
            JPanel p = new JPanel(new GridLayout(2,2));
            p.add(new JLabel("Course ID:")); p.add(idField);
            p.add(new JLabel("Stream Name:")); p.add(nameField);
            
            if(JOptionPane.showConfirmDialog(minorDialog, p, "New Minor Stream", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    CourseService.addMinorStream(Integer.parseInt(idField.getText()), nameField.getText());
                    minorDialog.dispose();
                    showMinors(); 
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(minorDialog, "Invalid Input!");
                }
            }
        });
        minorDialog.add(addBtn, BorderLayout.SOUTH);
        minorDialog.setVisible(true);
    }

    public void addS() {
        JTextField rollField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField deptField = new JTextField(15);
        JTextField semField = new JTextField(5);

        JPanel myPanel = new JPanel(new GridLayout(4, 2));
        myPanel.add(new JLabel("Roll No (Student ID):")); myPanel.add(rollField);
        myPanel.add(new JLabel("Student Name:")); myPanel.add(nameField);
        myPanel.add(new JLabel("Department:")); myPanel.add(deptField);
        myPanel.add(new JLabel("Semester:")); myPanel.add(semField);

        if (JOptionPane.showConfirmDialog(null, myPanel, "Add Student", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                String defaultPass = rollField.getText().trim();
                CourseService.addStudent(new Student(
                    rollField.getText(), nameField.getText(), defaultPass, 
                    deptField.getText(), Integer.parseInt(semField.getText()), "None"
                ));
                
                JOptionPane.showMessageDialog(this, "Student Account Created Successfully!\n\nDefault System Password has been securely set to their Roll No:\n[ " + defaultPass + " ]\n\nRule: Direct the student to use the 'Change Password' option upon their first login.");
                refT();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error Adding Student, Check Fields");
            }
        }
    }

    public void delC() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            CourseService.deleteCourse(Integer.parseInt((String) table.getValueAt(r, 0)));
            refT();
        } else {
            JOptionPane.showMessageDialog(this, "Select a course to delete.");
        }
    }

    public void showR() {
        Map<String, List<Course>> regs = CourseService.getRegistrations();
        StringBuilder sb = new StringBuilder();
        for (String rollNo : regs.keySet()) {
            Student s = CourseService.getStudent(rollNo);
            String minorInfo = (s != null && s.getMinorStream() != null) ? s.getMinorStream() : "Not Selected";
            String label = (s != null) ? (s.getName() + " (Roll No: " + rollNo + ")  [ Minor Stream: " + minorInfo + " ]") : rollNo;
            sb.append("Student: ").append(label).append("\n");
            for (Course c : regs.get(rollNo)) {
                sb.append("   - ").append(c.getName()).append("\n");
            }
            sb.append("\n");
        }
        JTextArea ta = new JTextArea(sb.toString(), 20, 40);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "All Registrations", JOptionPane.INFORMATION_MESSAGE);
    }
}