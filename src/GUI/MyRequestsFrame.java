package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MyRequestsFrame extends JFrame implements ActionListener {

    JLabel titleLabel;
    JLabel eventLabel;
    JLabel resourceLabel;
    JLabel dateLabel;
    JLabel timeLabel;
    JLabel statusLabel;

    JButton backButton;

    public MyRequestsFrame() {

        setTitle("My Requests");
        setSize(550, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("My Resource Requests");
        titleLabel.setBounds(190, 30, 200, 30);

        eventLabel = new JLabel("Event: Project Review");
        eventLabel.setBounds(120, 100, 300, 25);

        resourceLabel = new JLabel("Resource: Seminar Hall A");
        resourceLabel.setBounds(120, 140, 300, 25);

        dateLabel = new JLabel("Date: 22/09/2026");
        dateLabel.setBounds(120, 180, 300, 25);

        timeLabel = new JLabel("Time: 2:00 PM - 4:00 PM");
        timeLabel.setBounds(120, 220, 300, 25);

        statusLabel = new JLabel("Status: Checking...");
        statusLabel.setBounds(120, 260, 300, 25);

        backButton = new JButton("Back");
        backButton.setBounds(220, 320, 100, 35);

        backButton.addActionListener(this);

        add(titleLabel);
        add(eventLabel);
        add(resourceLabel);
        add(dateLabel);
        add(timeLabel);
        add(statusLabel);
        add(backButton);

        loadStatus();

        setVisible(true);
    }

    public void loadStatus() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/campus_resource_db",
                    "root",
                    "YOUR_MYSQL_PASSWORD"
            );

            Statement stmt = con.createStatement();

            String sql = "SELECT status FROM bookings WHERE booking_id = 1";

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {

                String status = rs.getString("status");

                statusLabel.setText("Status: " + status);
            }

            con.close();

        } catch (ClassNotFoundException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "JDBC Driver Not Found!"
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error!"
            );

            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == backButton) {

            dispose();
            new FacultyFrame();
        }
    }

    public static void main(String[] args) {

        new MyRequestsFrame();
    }
}