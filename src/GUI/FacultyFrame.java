package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FacultyFrame extends JFrame implements ActionListener {

    JLabel titleLabel;

    JButton createEventButton;
    JButton studentRequestsButton;
    JButton myRequestsButton;
    JButton logoutButton;

    public FacultyFrame() {

        setTitle("Faculty Dashboard");
        setSize(500, 420);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Faculty Dashboard");
        titleLabel.setBounds(180, 30, 200, 30);

        createEventButton =
                new JButton("Create Event & Request Resource");
        createEventButton.setBounds(100, 90, 300, 40);

        studentRequestsButton =
                new JButton("Student Account Requests");
        studentRequestsButton.setBounds(150, 145, 200, 40);

        myRequestsButton =
                new JButton("My Resource Requests");
        myRequestsButton.setBounds(150, 200, 200, 40);

        logoutButton =
                new JButton("Logout");
        logoutButton.setBounds(150, 270, 200, 40);

        createEventButton.addActionListener(this);
        studentRequestsButton.addActionListener(this);
        myRequestsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        add(titleLabel);
        add(createEventButton);
        add(studentRequestsButton);
        add(myRequestsButton);
        add(logoutButton);

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