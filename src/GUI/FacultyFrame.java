package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FacultyFrame extends JFrame implements ActionListener {

    JLabel titleLabel;
    JButton createEventButton;
    JButton requestResourceButton;
    JButton myRequestsButton;
    JButton logoutButton;

    public FacultyFrame() {

        setTitle("Faculty Dashboard");
        setSize(500, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Faculty Dashboard");
        titleLabel.setBounds(180, 40, 200, 30);

        createEventButton = new JButton("Create Event");
        createEventButton.setBounds(150, 100, 200, 40);

        requestResourceButton = new JButton("Request Resource");
        requestResourceButton.setBounds(150, 155, 200, 40);

        myRequestsButton = new JButton("My Requests");
        myRequestsButton.setBounds(150, 210, 200, 40);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 265, 200, 40);

        createEventButton.addActionListener(this);
        requestResourceButton.addActionListener(this);
        myRequestsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        add(titleLabel);
        add(createEventButton);
        add(requestResourceButton);
        add(myRequestsButton);
        add(logoutButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

    	if (e.getSource() == createEventButton) {

    	    dispose();
    	    new CreateEventFrame();

    	} else if (e.getSource() == myRequestsButton) {

    	    dispose();
    	    new MyRequestsFrame();

    	}else if (e.getSource() == myRequestsButton) {

            System.out.println("My Requests clicked");

        } else if (e.getSource() == logoutButton) {

            dispose();
        }
    }

    public static void main(String[] args) {

        new FacultyFrame();
    }
}