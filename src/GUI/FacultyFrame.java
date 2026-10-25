package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FacultyFrame extends JFrame implements ActionListener {

    JLabel titleLabel;

    JButton createEventButton;
    JButton studentRequestsButton;
    JButton myRequestsButton;
    JButton logoutButton;

    public FacultyFrame() {

        setTitle("Faculty Dashboard");
        setSize(560, 460);
        setLayout(new BorderLayout(15, 15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Faculty Dashboard");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(35, 55, 80));

        createEventButton =
                new JButton("Create Event & Request Resource");

        studentRequestsButton =
                new JButton("Student Account Requests");

        myRequestsButton =
                new JButton("My Resource Requests");

        logoutButton =
                new JButton("Logout");
        createEventButton.setPreferredSize(new Dimension(300, 42));
        studentRequestsButton.setPreferredSize(new Dimension(300, 42));
        myRequestsButton.setPreferredSize(new Dimension(300, 42));
        logoutButton.setPreferredSize(new Dimension(300, 42));
        createEventButton.setBackground(new Color(45, 91, 140));
        createEventButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(230, 235, 241));

        createEventButton.addActionListener(this);
        studentRequestsButton.addActionListener(this);
        myRequestsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 12));
        buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 100, 20, 100));
        buttonPanel.add(createEventButton);
        buttonPanel.add(studentRequestsButton);
        buttonPanel.add(myRequestsButton);
        buttonPanel.add(logoutButton);
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == createEventButton) {

            dispose();
            new CreateEventFrame();

        } else if (e.getSource() == studentRequestsButton) {

            dispose();
            new StudentRequestsFrame();

        } else if (e.getSource() == myRequestsButton) {

            dispose();
            new MyRequestsFrame();

        } else if (e.getSource() == logoutButton) {

            dispose();
            new LoginFrame();
        }
    }

    public static void main(String[] args) {

        new FacultyFrame();
    }
}