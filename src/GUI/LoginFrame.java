package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame implements ActionListener {

    JLabel titleLabel, usernameLabel, passwordLabel, roleLabel;
    JTextField usernameField;
    JPasswordField passwordField;
    JComboBox<String> roleBox;
    JButton loginButton;

    public LoginFrame() {

        setTitle("Campus Resource Allocation & Booking System");
        setSize(500, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Campus Resource Allocation System");
        titleLabel.setBounds(100, 40, 300, 30);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(80, 100, 100, 25);

        usernameField = new JTextField();
        usernameField.setBounds(190, 100, 220, 25);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(80, 145, 100, 25);

        passwordField = new JPasswordField();
        passwordField.setBounds(190, 145, 220, 25);

        roleLabel = new JLabel("Role:");
        roleLabel.setBounds(80, 190, 100, 25);

        String roles[] = {"Faculty", "Admin", "Student"};

        roleBox = new JComboBox<>(roles);
        roleBox.setBounds(190, 190, 220, 25);

        loginButton = new JButton("Login");
        loginButton.setBounds(190, 245, 100, 35);

        loginButton.addActionListener(this);

        add(titleLabel);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleBox);
        add(loginButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if (username.equals("faculty") &&
        	    password.equals("1234") &&
        	    role.equals("Faculty")) {

        	    JOptionPane.showMessageDialog(this, "Faculty Login Successful!");

        	    dispose();
        	    new FacultyFrame();

        } else if (username.equals("admin") &&
                password.equals("1234") &&
                role.equals("Admin")) {

         JOptionPane.showMessageDialog(this, "Admin Login Successful!");

         dispose();
         new AdminFrame();

        } else if (username.equals("student") &&
                password.equals("1234") &&
                role.equals("Student")) {

         JOptionPane.showMessageDialog(this, "Student Login Successful!");

         dispose();
         new StudentFrame();

        } else {

            JOptionPane.showMessageDialog(this, "Invalid Login Details!");
        }
    }

    public static void main(String[] args) {

        new LoginFrame();
    }
}