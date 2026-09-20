package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Database.DBConnection;

public class AdminFrame extends JFrame implements ActionListener {

    JLabel titleLabel;

    JTabbedPane tabbedPane;

    JButton refreshButton;
    JButton logoutButton;

    // Booking tab
    JLabel bookingTitleLabel;
    JLabel bookingDetailsLabel;
    JButton approveBookingButton;
    JButton rejectBookingButton;

    int pendingBookingId = -1;

    // Faculty account tab
    JLabel facultyTitleLabel;
    JLabel facultyDetailsLabel;
    JButton approveFacultyButton;
    JButton rejectFacultyButton;

    int pendingFacultyId = -1;

    public AdminFrame() {

        setTitle("Admin Dashboard");
        setSize(700, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setBounds(285, 20, 160, 30);

        add(titleLabel);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50, 65, 600, 420);

        JPanel bookingPanel = createBookingPanel();
        JPanel facultyPanel = createFacultyPanel();

        tabbedPane.addTab("Booking Requests", bookingPanel);
        tabbedPane.addTab("Faculty Account Requests", facultyPanel);

        add(tabbedPane);

        refreshButton = new JButton("Refresh");
        refreshButton.setBounds(245, 505, 100, 35);
        refreshButton.addActionListener(this);
        add(refreshButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(360, 505, 100, 35);
        logoutButton.addActionListener(this);
        add(logoutButton);

        loadPendingBooking();
        loadPendingFaculty();

        setVisible(true);
    }

    public JPanel createBookingPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(null);

        bookingTitleLabel = new JLabel("Pending Booking Request");
        bookingTitleLabel.setBounds(210, 20, 220, 30);

        bookingDetailsLabel = new JLabel("Loading...");
        bookingDetailsLabel.setBounds(120, 65, 360, 150);

        approveBookingButton = new JButton("Approve");
        approveBookingButton.setBounds(160, 250, 120, 35);
        approveBookingButton.addActionListener(this);

        rejectBookingButton = new JButton("Reject");
        rejectBookingButton.setBounds(320, 250, 120, 35);
        rejectBookingButton.addActionListener(this);

        panel.add(bookingTitleLabel);
        panel.add(bookingDetailsLabel);
        panel.add(approveBookingButton);
        panel.add(rejectBookingButton);

        return panel;
    }

    public JPanel createFacultyPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(null);

        facultyTitleLabel = new JLabel("Pending Faculty Account");
        facultyTitleLabel.setBounds(210, 20, 220, 30);

        facultyDetailsLabel = new JLabel("Loading...");
        facultyDetailsLabel.setBounds(150, 70, 320, 120);

        approveFacultyButton = new JButton("Approve Account");
        approveFacultyButton.setBounds(145, 240, 150, 35);
        approveFacultyButton.addActionListener(this);

        rejectFacultyButton = new JButton("Reject Account");
        rejectFacultyButton.setBounds(320, 240, 150, 35);
        rejectFacultyButton.addActionListener(this);

        panel.add(facultyTitleLabel);
        panel.add(facultyDetailsLabel);
        panel.add(approveFacultyButton);
        panel.add(rejectFacultyButton);

        return panel;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == approveBookingButton) {

            updateBookingStatus("Approved");

        } else if (e.getSource() == rejectBookingButton) {

            updateBookingStatus("Rejected");

        } else if (e.getSource() == approveFacultyButton) {

            updateFacultyStatus("Approved");

        } else if (e.getSource() == rejectFacultyButton) {

            updateFacultyStatus("Rejected");

        } else if (e.getSource() == refreshButton) {

            loadPendingBooking();
            loadPendingFaculty();

        } else if (e.getSource() == logoutButton) {

            dispose();
            new LoginFrame();
        }
    }

    // ==============================
    // LOAD BOOKING
    // ==============================

    public void loadPendingBooking() {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT b.booking_id, "
                    + "e.event_name, "
                    + "u.name AS faculty_name, "
                    + "r.resource_name, "
                    + "e.student_count, "
                    + "e.start_time, "
                    + "e.end_time "
                    + "FROM bookings b "
                    + "JOIN events e ON b.event_id = e.event_id "
                    + "JOIN users u ON e.faculty_id = u.user_id "
                    + "JOIN resources r ON b.resource_id = r.resource_id "
                    + "WHERE b.status = 'Pending' "
                    + "LIMIT 1";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                pendingBookingId = rs.getInt("booking_id");

                String eventName = rs.getString("event_name");
                String facultyName = rs.getString("faculty_name");
                String resourceName = rs.getString("resource_name");
                int studentCount = rs.getInt("student_count");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");

                bookingDetailsLabel.setText(
                        "<html>"
                        + "Event: " + eventName + "<br>"
                        + "Faculty: " + facultyName + "<br>"
                        + "Resource: " + resourceName + "<br>"
                        + "Students: " + studentCount + "<br>"
                        + "Time: " + startTime + " - " + endTime + "<br>"
                        + "Status: Pending"
                        + "</html>"
                );

                approveBookingButton.setEnabled(true);
                rejectBookingButton.setEnabled(true);

            } else {

                pendingBookingId = -1;

                bookingDetailsLabel.setText(
                        "No pending booking requests"
                );

                approveBookingButton.setEnabled(false);
                rejectBookingButton.setEnabled(false);
            }

            con.close();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ==============================
    // UPDATE BOOKING
    // ==============================

    public void updateBookingStatus(String status) {

        if (pendingBookingId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "No pending booking!"
            );

            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "UPDATE bookings "
                    + "SET status = ? "
                    + "WHERE booking_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, status);
            pst.setInt(2, pendingBookingId);

            pst.executeUpdate();

            con.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Booking " + status + " Successfully!"
            );

            loadPendingBooking();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ==============================
    // LOAD FACULTY ACCOUNT
    // ==============================

    public void loadPendingFaculty() {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT user_id, name, username, role "
                    + "FROM users "
                    + "WHERE role = 'Faculty' "
                    + "AND status = 'Pending' "
                    + "LIMIT 1";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                pendingFacultyId = rs.getInt("user_id");

                String name = rs.getString("name");
                String username = rs.getString("username");
                String role = rs.getString("role");

                facultyDetailsLabel.setText(
                        "<html>"
                        + "Name: " + name + "<br>"
                        + "Username: " + username + "<br>"
                        + "Role: " + role + "<br>"
                        + "Status: Pending"
                        + "</html>"
                );

                approveFacultyButton.setEnabled(true);
                rejectFacultyButton.setEnabled(true);

            } else {

                pendingFacultyId = -1;

                facultyDetailsLabel.setText(
                        "No pending faculty accounts"
                );

                approveFacultyButton.setEnabled(false);
                rejectFacultyButton.setEnabled(false);
            }

            con.close();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ==============================
    // UPDATE FACULTY ACCOUNT
    // ==============================

    public void updateFacultyStatus(String status) {

        if (pendingFacultyId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "No pending faculty account!"
            );

            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "UPDATE users "
                    + "SET status = ? "
                    + "WHERE user_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, status);
            pst.setInt(2, pendingFacultyId);

            int rows = pst.executeUpdate();

            con.close();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Faculty Account "
                        + status
                        + " Successfully!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No account was updated!"
                );
            }

            loadPendingFaculty();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new AdminFrame();
    }
}