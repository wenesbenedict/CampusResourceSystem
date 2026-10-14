package GUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Database.DBConnection;

public class SignUpFrame extends JFrame
        implements ActionListener {

    JLabel titleLabel;

    JLabel nameLabel;
    JLabel registerLabel;
    JLabel emailLabel;
    JLabel roleLabel;
    JLabel levelLabel;
    JLabel departmentLabel;
    JLabel programmeLabel;
    JLabel yearLabel;
    JLabel sectionLabel;
    JLabel facultyLabel;
    JLabel usernameLabel;
    JLabel passwordLabel;

    JTextField nameField;
    JTextField registerField;
    JTextField emailField;
    JTextField departmentField;
    JTextField usernameField;

    JPasswordField passwordField;

    JComboBox<String> roleBox;
    JComboBox<String> levelBox;
    JComboBox<String> departmentBox;
    JComboBox<String> programmeBox;
    JComboBox<String> yearBox;
    JComboBox<String> sectionBox;
    JComboBox<String> facultyBox;

    JButton signUpButton;
    JButton backButton;

    HashMap<String, Integer> departmentMap;
    HashMap<String, Integer> programmeMap;
    HashMap<String, Integer> sectionMap;
    HashMap<String, Integer> facultyMap;

    public SignUpFrame() {

        setTitle(
                "Karunya Campus Allocation & Booking System"
        );

        setSize(
                700,
                720
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(true);

        createForm();

        loadDepartments();

        updateForm();

        setVisible(true);
    }

    private void createForm() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        35,
                        20,
                        35
                )
        );

        titleLabel =
                new JLabel(
                        "Create Account"
                );

        titleLabel.setHorizontalAlignment(
                JLabel.CENTER
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        8,
                        6,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 0;

        nameLabel =
                new JLabel("Name:");

        registerLabel =
                new JLabel("Register Number:");

        emailLabel =
                new JLabel("Email:");

        roleLabel =
                new JLabel("Role:");

        levelLabel =
                new JLabel("Level:");

        departmentLabel =
                new JLabel("Department:");

        programmeLabel =
                new JLabel("Programme:");

        yearLabel =
                new JLabel("Year:");

        sectionLabel =
                new JLabel("Section:");

        facultyLabel =
                new JLabel("Faculty:");

        usernameLabel =
                new JLabel("Username:");

        passwordLabel =
                new JLabel("Password:");

        nameField =
                new JTextField();

        registerField =
                new JTextField();

        emailField =
                new JTextField();

        departmentField =
                new JTextField();

        departmentField.setEditable(
                false
        );

        usernameField =
                new JTextField();

        passwordField =
                new JPasswordField();

        roleBox =
                new JComboBox<>(
                        new String[] {
                                "Student",
                                "Faculty"
                        }
                );

        levelBox =
                new JComboBox<>(
                        new String[] {
                                "UG",
                                "PG"
                        }
                );

        departmentBox =
                new JComboBox<>();

        programmeBox =
                new JComboBox<>();

        yearBox =
                new JComboBox<>();

        sectionBox =
                new JComboBox<>();

        facultyBox =
                new JComboBox<>();

        roleBox.addActionListener(this);

        levelBox.addActionListener(this);

        departmentBox.addActionListener(this);

        programmeBox.addActionListener(this);

        yearBox.addActionListener(this);

        int row = 0;

        addRow(
                formPanel,
                gbc,
                row++,
                nameLabel,
                nameField
        );

        addRow(
                formPanel,
                gbc,
                row++,
                registerLabel,
                registerField
        );

        addRow(
                formPanel,
                gbc,
                row++,
                emailLabel,
                emailField
        );

        addRow(
                formPanel,
                gbc,
                row++,
                roleLabel,
                roleBox
        );

        addRow(
                formPanel,
                gbc,
                row++,
                levelLabel,
                levelBox
        );

        addRow(
                formPanel,
                gbc,
                row++,
                departmentLabel,
                departmentBox
        );

        addRow(
                formPanel,
                gbc,
                row++,
                programmeLabel,
                programmeBox
        );

        addRow(
                formPanel,
                gbc,
                row++,
                yearLabel,
                yearBox
        );

        addRow(
                formPanel,
                gbc,
                row++,
                sectionLabel,
                sectionBox
        );

        addRow(
                formPanel,
                gbc,
                row++,
                facultyLabel,
                facultyBox
        );

        addRow(
                formPanel,
                gbc,
                row++,
                usernameLabel,
                usernameField
        );

        addRow(
                formPanel,
                gbc,
                row++,
                passwordLabel,
                passwordField
        );

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        JPanel buttonPanel =
                new JPanel();

        signUpButton =
                new JButton(
                        "Create Account"
                );

        backButton =
                new JButton(
                        "Back"
                );

        signUpButton.addActionListener(this);

        backButton.addActionListener(this);

        buttonPanel.add(
                signUpButton
        );

        buttonPanel.add(
                backButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);
    }

    private void addRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            JLabel label,
            java.awt.Component component
    ) {

        gbc.gridx = 0;

        gbc.gridy = row;

        gbc.weightx = 0;

        panel.add(
                label,
                gbc
        );

        gbc.gridx = 1;

        gbc.weightx = 1;

        panel.add(
                component,
                gbc
        );
    }

    public void loadDepartments() {

        departmentMap =
                new HashMap<>();

        departmentBox.removeAllItems();

        String sql =
                "SELECT department_id, department_name "
                + "FROM departments "
                + "ORDER BY department_name";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql);

                ResultSet rs =
                        pst.executeQuery()
        ) {

            while (rs.next()) {

                int departmentId =
                        rs.getInt(
                                "department_id"
                        );

                String departmentName =
                        rs.getString(
                                "department_name"
                        );

                departmentMap.put(
                        departmentName,
                        departmentId
                );

                departmentBox.addItem(
                        departmentName
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load departments:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void loadProgrammes() {

        programmeMap =
                new HashMap<>();

        programmeBox.removeAllItems();

        yearBox.removeAllItems();

        sectionBox.removeAllItems();

        String departmentName =
                (String)
                departmentBox.getSelectedItem();

        String level =
                (String)
                levelBox.getSelectedItem();

        if (
                departmentName == null
                || level == null
        ) {
            return;
        }

        Integer departmentId =
                departmentMap.get(
                        departmentName
                );

        if (departmentId == null) {
            return;
        }

        String sql =
                "SELECT programme_id, programme_name "
                + "FROM programmes "
                + "WHERE department_id = ? "
                + "AND level = ? "
                + "ORDER BY programme_name";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    departmentId
            );

            pst.setString(
                    2,
                    level
            );

            try (
                    ResultSet rs =
                            pst.executeQuery()
            ) {

                while (rs.next()) {

                    int programmeId =
                            rs.getInt(
                                    "programme_id"
                            );

                    String programmeName =
                            rs.getString(
                                    "programme_name"
                            );

                    programmeMap.put(
                            programmeName,
                            programmeId
                    );

                    programmeBox.addItem(
                            programmeName
                    );
                }
            }

            loadYears();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load programmes:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void loadYears() {

        yearBox.removeAllItems();

        sectionBox.removeAllItems();

        String programmeName =
                (String)
                programmeBox.getSelectedItem();

        if (programmeName == null) {
            return;
        }

        Integer programmeId =
                programmeMap.get(
                        programmeName
                );

        if (programmeId == null) {
            return;
        }

        String sql =
                "SELECT duration_years "
                + "FROM programmes "
                + "WHERE programme_id = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    programmeId
            );

            try (
                    ResultSet rs =
                            pst.executeQuery()
            ) {

                if (rs.next()) {

                    int duration =
                            rs.getInt(
                                    "duration_years"
                            );

                    for (
                            int i = 1;
                            i <= duration;
                            i++
                    ) {

                        yearBox.addItem(
                                String.valueOf(i)
                        );
                    }
                }
            }

            loadSections();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load years:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void loadSections() {

        sectionMap =
                new HashMap<>();

        sectionBox.removeAllItems();

        String programmeName =
                (String)
                programmeBox.getSelectedItem();

        String yearText =
                (String)
                yearBox.getSelectedItem();

        if (
                programmeName == null
                || yearText == null
        ) {
            return;
        }

        Integer programmeId =
                programmeMap.get(
                        programmeName
                );

        if (programmeId == null) {
            return;
        }

        int year =
                Integer.parseInt(
                        yearText
                );

        String sql =
                "SELECT section_id, section_name "
                + "FROM sections "
                + "WHERE programme_id = ? "
                + "AND year = ? "
                + "ORDER BY section_name";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    programmeId
            );

            pst.setInt(
                    2,
                    year
            );

            try (
                    ResultSet rs =
                            pst.executeQuery()
            ) {

                while (rs.next()) {

                    int sectionId =
                            rs.getInt(
                                    "section_id"
                            );

                    String sectionName =
                            rs.getString(
                                    "section_name"
                            );

                    sectionMap.put(
                            sectionName,
                            sectionId
                    );

                    sectionBox.addItem(
                            sectionName
                    );
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sections:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void loadFacultyList() {

        facultyMap =
                new HashMap<>();

        facultyBox.removeAllItems();

        facultyBox.setEnabled(
                true
        );

        String departmentName =
                (String)
                departmentBox.getSelectedItem();

        if (departmentName == null) {
            return;
        }

        Integer departmentId =
                departmentMap.get(
                        departmentName
                );

        if (departmentId == null) {
            return;
        }

        String sql =
                "SELECT user_id, name "
                + "FROM users "
                + "WHERE role = 'Faculty' "
                + "AND status = 'Approved' "
                + "AND department_id = ? "
                + "ORDER BY name";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    departmentId
            );

            try (
                    ResultSet rs =
                            pst.executeQuery()
            ) {

                while (rs.next()) {

                    int facultyId =
                            rs.getInt(
                                    "user_id"
                            );

                    String facultyName =
                            rs.getString(
                                    "name"
                            );

                    facultyMap.put(
                            facultyName,
                            facultyId
                    );

                    facultyBox.addItem(
                            facultyName
                    );
                }
            }

            if (
                    facultyBox.getItemCount()
                            == 0
            ) {

                facultyBox.addItem(
                        "No approved faculty available"
                );

                facultyBox.setEnabled(
                        false
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load faculty list:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void updateForm() {

        boolean student =
                "Student".equals(
                        roleBox.getSelectedItem()
                );

        boolean faculty =
                "Faculty".equals(
                        roleBox.getSelectedItem()
                );

        registerLabel.setVisible(
                student
        );

        registerField.setVisible(
                student
        );

        emailLabel.setVisible(
                true
        );

        emailField.setVisible(
                true
        );

        levelLabel.setVisible(
                student
        );

        levelBox.setVisible(
                student
        );

        departmentLabel.setVisible(
                true
        );

        departmentBox.setVisible(
                true
        );

        departmentField.setVisible(
                false
        );

        programmeLabel.setVisible(
                student
        );

        programmeBox.setVisible(
                student
        );

        yearLabel.setVisible(
                student
        );

        yearBox.setVisible(
                student
        );

        sectionLabel.setVisible(
                student
        );

        sectionBox.setVisible(
                student
        );

        facultyLabel.setVisible(
                student
        );

        facultyBox.setVisible(
                student
        );

        if (faculty) {

            facultyBox.removeAllItems();

            facultyMap.clear();

        } else {

            loadProgrammes();

            loadFacultyList();
        }

        revalidate();

        repaint();
    }

    public void createAccount() {

        String name =
                nameField.getText().trim();

        String registerNumber =
                registerField.getText().trim();

        String email =
                emailField.getText().trim();

        String role =
                (String)
                roleBox.getSelectedItem();

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (
                name.isEmpty()
                || email.isEmpty()
                || username.isEmpty()
                || password.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required details!"
            );

            return;
        }

        Integer departmentId =
                null;

        Integer programmeId =
                null;

        Integer sectionId =
                null;

        Integer facultyId =
                null;

        if (
                "Student".equals(role)
        ) {

            if (
                    registerNumber.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter register number!"
                );

                return;
            }

            String departmentName =
                    (String)
                    departmentBox.getSelectedItem();

            String programmeName =
                    (String)
                    programmeBox.getSelectedItem();

            String yearText =
                    (String)
                    yearBox.getSelectedItem();

            String sectionName =
                    (String)
                    sectionBox.getSelectedItem();

            String facultyName =
                    (String)
                    facultyBox.getSelectedItem();

            departmentId =
                    departmentMap.get(
                            departmentName
                    );

            programmeId =
                    programmeMap.get(
                            programmeName
                    );

            sectionId =
                    sectionMap.get(
                            sectionName
                    );

            facultyId =
                    facultyMap.get(
                            facultyName
                    );

            if (
                    departmentId == null
                    || programmeId == null
                    || yearText == null
                    || sectionId == null
                    || facultyId == null
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select valid academic details and faculty!"
                );

                return;
            }

        } else {

            String departmentName =
                    (String)
                    departmentBox.getSelectedItem();

            departmentId =
                    departmentMap.get(
                            departmentName
                    );

            if (
                    departmentId == null
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a department!"
                );

                return;
            }
        }

        String sql =
                "INSERT INTO users "
                + "(name, email, register_number, "
                + "role, status, department_id, "
                + "programme_id, section_id, faculty_id, "
                + "username, password) "
                + "VALUES (?, ?, ?, ?, 'Pending', "
                + "?, ?, ?, ?, ?, ?)";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setString(
                    1,
                    name
            );

            pst.setString(
                    2,
                    email
            );

            if (
                    "Student".equals(role)
            ) {

                pst.setString(
                        3,
                        registerNumber
                );

            } else {

                pst.setNull(
                        3,
                        java.sql.Types.VARCHAR
                );
            }

            pst.setString(
                    4,
                    role
            );

            pst.setInt(
                    5,
                    departmentId
            );

            if (
                    programmeId != null
            ) {

                pst.setInt(
                        6,
                        programmeId
                );

            } else {

                pst.setNull(
                        6,
                        java.sql.Types.INTEGER
                );
            }

            if (
                    sectionId != null
            ) {

                pst.setInt(
                        7,
                        sectionId
                );

            } else {

                pst.setNull(
                        7,
                        java.sql.Types.INTEGER
                );
            }

            if (
                    facultyId != null
            ) {

                pst.setInt(
                        8,
                        facultyId
                );

            } else {

                pst.setNull(
                        8,
                        java.sql.Types.INTEGER
                );
            }

            pst.setString(
                    9,
                    username
            );

            pst.setString(
                    10,
                    password
            );

            pst.executeUpdate();

            if (
                    "Student".equals(role)
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Student account created!\n\n"
                        + "Status: Pending Faculty Approval"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Faculty account created!\n\n"
                        + "Status: Pending Admin Approval"
                );
            }

            clearFields();

        } catch (SQLException e) {

            if (
                    e.getErrorCode() == 1062
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Username already exists!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Database Error:\n"
                        + e.getMessage()
                );

                e.printStackTrace();
            }
        }
    }

    private void clearFields() {

        nameField.setText("");

        registerField.setText("");

        emailField.setText("");

        usernameField.setText("");

        passwordField.setText("");

        if (
                departmentBox.getItemCount()
                        > 0
        ) {

            departmentBox.setSelectedIndex(
                    0
            );
        }

        updateForm();
    }

    @Override
    public void actionPerformed(
            ActionEvent e
    ) {

        if (
                e.getSource()
                        == roleBox
        ) {

            updateForm();

        } else if (
                e.getSource()
                        == levelBox
        ) {

            loadProgrammes();

        } else if (
                e.getSource()
                        == departmentBox
        ) {

            loadProgrammes();

            loadFacultyList();

        } else if (
                e.getSource()
                        == programmeBox
        ) {

            loadYears();

        } else if (
                e.getSource()
                        == yearBox
        ) {

            loadSections();

        } else if (
                e.getSource()
                        == signUpButton
        ) {

            createAccount();

        } else if (
                e.getSource()
                        == backButton
        ) {

            dispose();

            new LoginFrame();
        }
    }

    public static void main(
            String[] args
    ) {

        new SignUpFrame();
    }
}