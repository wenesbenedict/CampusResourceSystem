package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CreateEventFrame extends JFrame implements ActionListener {

    JLabel titleLabel;
    JLabel eventNameLabel;
    JLabel studentCountLabel;
    JLabel dateLabel;
    JLabel startTimeLabel;
    JLabel endTimeLabel;

    JTextField eventNameField;
    JTextField studentCountField;
    JTextField dateField;
    JTextField startTimeField;
    JTextField endTimeField;

    JButton submitButton;
    JButton backButton;

    public CreateEventFrame() {

        setTitle("Create Event");
        setSize(550, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Create Academic Event");
        titleLabel.setBounds(190, 30, 200, 30);

        eventNameLabel = new JLabel("Event Name:");
        eventNameLabel.setBounds(70, 90, 120, 25);

        eventNameField = new JTextField();
        eventNameField.setBounds(200, 90, 220, 25);

        studentCountLabel = new JLabel("Student Count:");
        studentCountLabel.setBounds(70, 135, 120, 25);

        studentCountField = new JTextField();
        studentCountField.setBounds(200, 135, 220, 25);

        dateLabel = new JLabel("Date:");
        dateLabel.setBounds(70, 180, 120, 25);

        dateField = new JTextField();
        dateField.setBounds(200, 180, 220, 25);

        startTimeLabel = new JLabel("Start Time:");
        startTimeLabel.setBounds(70, 225, 120, 25);

        startTimeField = new JTextField();
        startTimeField.setBounds(200, 225, 220, 25);

        endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setBounds(70, 270, 120, 25);

        endTimeField = new JTextField();
        endTimeField.setBounds(200, 270, 220, 25);

        submitButton = new JButton("Submit Event");
        submitButton.setBounds(150, 340, 120, 35);

        backButton = new JButton("Back");
        backButton.setBounds(290, 340, 100, 35);

        submitButton.addActionListener(this);
        backButton.addActionListener(this);

        add(titleLabel);

        add(eventNameLabel);
        add(eventNameField);

        add(studentCountLabel);
        add(studentCountField);

        add(dateLabel);
        add(dateField);

        add(startTimeLabel);
        add(startTimeField);

        add(endTimeLabel);
        add(endTimeField);

        add(submitButton);
        add(backButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == submitButton) {

            String eventName = eventNameField.getText();
            String studentCount = studentCountField.getText();
            String date = dateField.getText();
            String startTime = startTimeField.getText();
            String endTime = endTimeField.getText();

            if (eventName.isEmpty()
                    || studentCount.isEmpty()
                    || date.isEmpty()
                    || startTime.isEmpty()
                    || endTime.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all details!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Event Created Successfully!"
                );
            }

        } else if (e.getSource() == backButton) {

            dispose();
            new FacultyFrame();
        }
    }

    public static void main(String[] args) {

        new CreateEventFrame();
    }
}