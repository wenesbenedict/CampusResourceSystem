package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignUpFrame extends JFrame implements ActionListener {

    JLabel titleLabel;
    JLabel nameLabel;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JLabel roleLabel;
    JLabel facultyLabel;

    JTextField nameField;
    JTextField usernameField;
    JPasswordField passwordField;

    JComboBox<String> roleBox;
    JComboBox<String> facultyBox;

    JButton signUpButton;
    JButton backButton;

    HashMap<String, Integer> facultyMap;

    public SignUpFrame() {

        setTitle("Sign Up");
        setSize(550, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Create Account");
        titleLabel.setBounds(210, 30, 150, 30);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(90, 90, 100, 25);

        nameField = new JTextField();
        nameField.setBounds(220, 90, 220, 25);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(90, 135, 100, 25);

        usernameField = new JTextField();
        usernameField.setBounds(220, 135, 220, 25);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(90, 180, 100, 25);

        passwordField = new JPasswordField();
        passwordField.setBounds(220, 180, 220, 25);

        roleLabel = new JLabel("Role:");
        roleLabel.setBounds(90, 225, 100, 25);

        String roles[] = {
                "Faculty",
                "Student"
        };

        roleBox = new JComboBox<>(roles);
        roleBox.setBounds(220, 225, 220, 25);
        roleBox.addActionListener(this);

        facultyLabel = new JLabel("Faculty:");
        facultyLabel.setBounds(90, 270, 100, 25);

        facultyBox = new JComboBox<>();
        facultyBox.setBounds(220, 270, 220, 25);

        loadFacultyList();

        signUpButton = new JButton("Create Account");
        signUpButton.setBounds(150, 350, 150, 35);

        backButton = new JButton("Back");
        backButton.setBounds(320, 350, 100, 35);

        signUpButton.addActionListener(this);
        backButton.addActionListener(this);

        add(titleLabel);

        add(nameLabel);
        add(nameField);

        add(usernameLabel);
        add(usernameField);

        add(passwordLabel);
        add(passwordField);

        add(roleLabel);
        add(roleBox);

        add(facultyLabel);
        add(facultyBox);

        add(signUpButton);
        add(backButton);

        updateFacultyVisibility();

        setVisible(true);
    }

    public void loadFacultyList() {

        facultyMap = new HashMap<>();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/campus_resource_db",
                    "root",
                    "YOUR_MYSQL_PASSWORD"
            );

            String sql =
                    "SELECT user_id, name FROM users " +
                    "WHERE role = 'Faculty' AND status = 'Approved'";

            PreparedStatement pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                int facultyId = rs.getInt("user_id");
                String facultyName = rs.getString("name");

                facultyMap.put(facultyName, facultyId);
                facultyBox.addItem(facultyName);
            }

            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load faculty list!"
            );

            e.printStackTrace();
        }
    }

    public void updateFacultyVisibility() {

        if (roleBox.getSelectedItem().equals("Student")) {

            facultyLabel.setVisible(true);
            facultyBox.setVisible(true);

        } else {

            facultyLabel.setVisible(false);
            facultyBox.setVisible(false);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == roleBox) {

            updateFacultyVisibility();

        } else if (e.getSource() == signUpButton) {

            createAccount();

        } else if (e.getSource() == backButton) {

            dispose();
            new LoginFrame();
        }
    }

    public void createAccount() {

        String name = nameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if (name.isEmpty()
                || username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all details!"
            );

            return;
        }

        int facultyId = 0;

        if (role.equals("Student")) {

            String selectedFaculty =
                    (String) facultyBox.getSelectedItem();

            if (selectedFaculty == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a faculty!"
                );

                return;
            }

            facultyId = facultyMap.get(selectedFaculty);
        }

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/campus_resource_db",
                    "root",
                    "YOUR_MYSQL_PASSWORD"
            );

            String sql =
                    "INSERT INTO users " +
                    "(name, username, password, role, status, faculty_id) " +
                    "VALUES (?, ?, ?, ?, 'Pending', ?)";

            PreparedStatement pst =
                    con.prepareStatement(sql);

            pst.setString(1, name);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.setString(4, role);

            if (role.equals("Student")) {

                pst.setInt(5, facultyId);

            } else {

                pst.setNull(5, java.sql.Types.INTEGER);
            }

            pst.executeUpdate();

            con.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Account Created!\nStatus: Pending Approval"
            );

            nameField.setText("");
            usernameField.setText("");
            passwordField.setText("");

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {

                JOptionPane.showMessageDialog(
                        this,
                        "Username already exists!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Database Error!"
                );

                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "JDBC Driver Not Found!"
            );
        }
    }

    public static void main(String[] args) {

        new SignUpFrame();
    }
}