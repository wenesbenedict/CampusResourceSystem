package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Database.DBConnection;

public class LoginFrame extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField passwordField;

    JComboBox<String> roleBox;

    JButton loginButton;
    JButton signUpButton;

    public static int currentUserId;
    public static String currentUsername;
    public static String currentRole;

    public LoginFrame() {

        setTitle("Campus Resource Allocation & Booking System");
        setSize(600, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        // Main window
        setLayout(new BorderLayout());

        // ==========================================
        // TITLE
        // ==========================================

        JLabel titleLabel =
                new JLabel(
                        "Campus Resource Allocation System",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 20)
        );
        titleLabel.setForeground(new Color(35, 55, 80));
        JLabel logoLabel = createLogoLabel(270);

        JPanel titlePanel =
                new JPanel(new BorderLayout(0, 8));

        titlePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 10, 20
                )
        );

        titlePanel.add(
                titleLabel,
                BorderLayout.CENTER
        );
        titlePanel.add(logoLabel, BorderLayout.NORTH);
        titlePanel.setOpaque(false);

        // ==========================================
        // LOGIN CARD
        // ==========================================

        JPanel cardPanel =
                new JPanel();

        cardPanel.setLayout(
                new BorderLayout()
        );

        cardPanel.setPreferredSize(
                new Dimension(430, 250)
        );

        cardPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Login"
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 25, 15, 25
                        )
                )
        );
        cardPanel.setBackground(Color.WHITE);

        // ==========================================
        // FORM
        // ==========================================

        JPanel formPanel =
                new JPanel();

        formPanel.setLayout(
                new javax.swing.BoxLayout(
                        formPanel,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );
        formPanel.setOpaque(false);

        // Username row
        JPanel usernamePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                8
                        )
                );

        JLabel usernameLabel =
                new JLabel("Username:");

        usernameLabel.setPreferredSize(
                new Dimension(90, 28)
        );

        usernameField =
                new JTextField();

        usernameField.setPreferredSize(
                new Dimension(220, 28)
        );

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        // Password row
        JPanel passwordPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                8
                        )
                );

        JLabel passwordLabel =
                new JLabel("Password:");

        passwordLabel.setPreferredSize(
                new Dimension(90, 28)
        );

        passwordField =
                new JPasswordField();

        passwordField.setPreferredSize(
                new Dimension(220, 28)
        );

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // Role row
        JPanel rolePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                8
                        )
                );

        JLabel roleLabel =
                new JLabel("Role:");

        roleLabel.setPreferredSize(
                new Dimension(90, 28)
        );

        roleBox =
                new JComboBox<>(
                        new String[]{
                                "Faculty",
                                "Admin",
                                "Student"
                        }
                );

        roleBox.setPreferredSize(
                new Dimension(220, 28)
        );

        rolePanel.add(roleLabel);
        rolePanel.add(roleBox);

        formPanel.add(usernamePanel);
        formPanel.add(passwordPanel);
        formPanel.add(rolePanel);

        cardPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        // ==========================================
        // BUTTONS
        // ==========================================

        loginButton =
                new JButton("Login");

        signUpButton =
                new JButton("Sign Up");

        loginButton.setPreferredSize(
                new Dimension(110, 35)
        );

        signUpButton.setPreferredSize(
                new Dimension(110, 35)
        );
        loginButton.setBackground(new Color(45, 91, 140));
        loginButton.setForeground(Color.WHITE);
        signUpButton.setBackground(new Color(230, 235, 241));

        loginButton.addActionListener(this);
        signUpButton.addActionListener(this);

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                5
                        )
                );

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        cardPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // ==========================================
        // CENTER THE CARD
        // ==========================================

        JPanel centerPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                0,
                                12
                        )
                );

        centerPanel.add(cardPanel);

        // ==========================================
        // ADD TO FRAME
        // ==========================================

        add(
                titlePanel,
                BorderLayout.NORTH
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        setVisible(true);
    }

    private JLabel createLogoLabel(int targetWidth) {
        java.net.URL logoUrl = getClass().getResource("/images/karunya.png");
        if (logoUrl == null) {
            return new JLabel();
        }
        ImageIcon icon = new ImageIcon(logoUrl);
        int origWidth = icon.getIconWidth();
        int origHeight = icon.getIconHeight();
        int targetHeight = origWidth > 0
                ? (int) Math.round((double) origHeight * targetWidth / origWidth)
                : targetWidth;
        Image image = icon.getImage().getScaledInstance(
                targetWidth, targetHeight, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(image), SwingConstants.CENTER);
    }

    // ==========================================
    // BUTTON ACTIONS
    // ==========================================

    public void actionPerformed(ActionEvent e) {

        // Sign Up
        if (e.getSource() == signUpButton) {

            dispose();
            new SignUpFrame();

            return;
        }

        // Login
        String username =
                usernameField.getText() == null
                        ? ""
                        : usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String role =
                (String) roleBox.getSelectedItem();

        if (username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password!"
            );

            return;
        }

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(
                                "SELECT user_id, username, role, status "
                                + "FROM users "
                                + "WHERE username = ? "
                                + "AND password = ? "
                                + "AND role = ?"
                        )
        ) {

            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    currentUserId =
                            rs.getInt("user_id");

                    currentUsername =
                            rs.getString("username");

                    currentRole =
                            rs.getString("role");

                    String status =
                            rs.getString("status");

                    if ("Pending".equalsIgnoreCase(status)) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Your account is waiting for approval."
                        );

                        return;
                    }

                    if ("Rejected".equalsIgnoreCase(status)) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Your account has been rejected."
                        );

                        return;
                    }

                    JOptionPane.showMessageDialog(
                            this,
                            currentRole
                                    + " Login Successful!"
                    );

                    dispose();

                    if ("Faculty".equalsIgnoreCase(currentRole)) {

                        new FacultyFrame();

                    } else if ("Admin".equalsIgnoreCase(currentRole)) {

                        new AdminFrame();

                    } else if ("Student".equalsIgnoreCase(currentRole)) {

                        new StudentFrame();
                    }

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid Login Details!"
                    );
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                            + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new LoginFrame();
    }
}