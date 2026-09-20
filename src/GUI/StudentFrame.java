package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StudentFrame extends JFrame implements ActionListener {

    JLabel titleLabel;
    JLabel eventLabel;
    JLabel facultyLabel;
    JLabel venueLabel;
    JLabel dateLabel;
    JLabel timeLabel;

    JButton logoutButton;

    public StudentFrame() {

        setTitle("Student Dashboard");
        setSize(550, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Student Dashboard");
        titleLabel.setBounds(200, 30, 180, 30);

        eventLabel = new JLabel("Event: Project Review");
        eventLabel.setBounds(130, 100, 300, 25);

        facultyLabel = new JLabel("Faculty: Faculty A");
        facultyLabel.setBounds(130, 135, 300, 25);

        venueLabel = new JLabel("Venue: Seminar Hall A");
        venueLabel.setBounds(130, 170, 300, 25);

        dateLabel = new JLabel("Date: 22/09/2026");
        dateLabel.setBounds(130, 205, 300, 25);

        timeLabel = new JLabel("Time: 2:00 PM - 4:00 PM");
        timeLabel.setBounds(130, 240, 300, 25);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(210, 290, 100, 35);

        logoutButton.addActionListener(this);

        add(titleLabel);
        add(eventLabel);
        add(facultyLabel);
        add(venueLabel);
        add(dateLabel);
        add(timeLabel);
        add(logoutButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == logoutButton) {

            dispose();
            new LoginFrame();
        }
    }

    public static void main(String[] args) {

        new StudentFrame();
    }
}