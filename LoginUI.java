package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import service.CourseService;
import util.DBConnection;

public class LoginUI extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;

    public LoginUI() {
        setTitle("Student Course Registration - Login");
        setSize(400, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(createMainPanel(), "Main");
        cards.add(createStudentPanel(), "Student");
        cards.add(createAdminPanel(), "Admin");

        add(cards);
        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(null);
        JLabel title = new JLabel("System Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(125, 40, 150, 30);
        panel.add(title);

        JButton studentBtn = new JButton("Login as Student");
        studentBtn.setBounds(100, 100, 200, 40);
        studentBtn.addActionListener(e -> cardLayout.show(cards, "Student"));
        panel.add(studentBtn);
        
        JButton adminBtn = new JButton("Login as Admin");
        adminBtn.setBounds(100, 160, 200, 40);
        adminBtn.addActionListener(e -> cardLayout.show(cards, "Admin"));
        panel.add(adminBtn);

        return panel;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(null);
        JLabel title = new JLabel("Student Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(125, 20, 150, 30);
        panel.add(title);

        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setBounds(50, 70, 80, 25);
        panel.add(nameLabel);
        JTextField nameField = new JTextField();
        nameField.setBounds(150, 70, 150, 25);
        panel.add(nameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 80, 25);
        panel.add(passLabel);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 110, 150, 25);
        panel.add(passField);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setBounds(50, 150, 80, 25);
        panel.add(idLabel);
        JTextField idField = new JTextField();
        idField.setBounds(150, 150, 150, 25);
        panel.add(idField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(80, 200, 240, 30);
        loginBtn.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passField.getPassword());
            String rollNo = idField.getText();
            if (CourseService.validateStudent(name, rollNo, password)) {
                new StudentUI(name, rollNo);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username, Student ID or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(80, 240, 240, 30);
        backBtn.addActionListener(e -> cardLayout.show(cards, "Main"));
        panel.add(backBtn);

        return panel;
    }


    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(null);
        JLabel title = new JLabel("Admin Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(125, 40, 150, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 100, 80, 25);
        panel.add(userLabel);
        JTextField userField = new JTextField();
        userField.setBounds(150, 100, 150, 25);
        panel.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 140, 80, 25);
        panel.add(passLabel);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 140, 150, 25);
        panel.add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(80, 200, 240, 30);
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (username.equals("admin") && password.equals("admin123")) {
                new AdminUI();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(80, 240, 240, 30);
        backBtn.addActionListener(e -> cardLayout.show(cards, "Main"));
        panel.add(backBtn);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI());
    }
}
