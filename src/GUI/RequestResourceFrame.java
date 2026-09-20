package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class RequestResourceFrame extends JFrame implements ActionListener {

    JLabel titleLabel;
    JLabel eventLabel;
    JLabel studentCountLabel;
    JLabel dateLabel;
    JLabel startTimeLabel;
    JLabel endTimeLabel;
    JLabel resourceTypeLabel;
    JLabel projectorLabel;

    JTextField eventField;
    JTextField studentCountField;
    JTextField dateField;
    JTextField startTimeField;
    JTextField endTimeField;

    JComboBox<String> resourceTypeBox;

    JButton findButton;
    JButton backButton;

    public RequestResourceFrame() {

        setTitle("Request Resource");
        setSize(550, 550);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Request Campus Resource");
        titleLabel.setBounds(180, 30, 220, 30);

        eventLabel = new JLabel("Event Name:");
        eventLabel.setBounds(70, 90, 120, 25);

        eventField = new JTextField();
        eventField.setBounds(200, 90, 220, 25);

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

        resourceTypeLabel = new JLabel("Resource Type:");
        resourceTypeLabel.setBounds(70, 315, 120, 25);

        String resources[] = {
                "Seminar Hall",
                "Classroom",
                "Computer Lab"
        };

        resourceTypeBox = new JComboBox<>(resources);
        resourceTypeBox.setBounds(200, 315, 220, 25);

        projectorLabel = new JLabel("Projector: Required");
        projectorLabel.setBounds(200, 355, 180, 25);

        findButton = new JButton("Find Resources");
        findButton.setBounds(140, 405, 140, 35);

        backButton = new JButton("Back");
        backButton.setBounds(300, 405, 100, 35);

        findButton.addActionListener(this);
        backButton.addActionListener(this);

        add(titleLabel);

        add(eventLabel);
        add(eventField);

        add(studentCountLabel);
        add(studentCountField);

        add(dateLabel);
        add(dateField);

        add(startTimeLabel);
        add(startTimeField);

        add(endTimeLabel);
        add(endTimeField);

        add(resourceTypeLabel);
        add(resourceTypeBox);

        add(projectorLabel);

        add(findButton);
        add(backButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == findButton) {

            String eventName = eventField.getText();
            String studentCountText = studentCountField.getText();
            String date = dateField.getText();
            String startTime = startTimeField.getText();
            String endTime = endTimeField.getText();

            String resourceType =
                    (String) resourceTypeBox.getSelectedItem();

            if (eventName.isEmpty()
                    || studentCountText.isEmpty()
                    || date.isEmpty()
                    || startTime.isEmpty()
                    || endTime.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all details!"
                );

                return;
            }

            int studentCount = Integer.parseInt(studentCountText);

            String result;

            if (resourceType.equals("Seminar Hall")
                    && studentCount <= 100) {

                result = "Suitable Resource Found!\n\n"
                        + "Resource: Seminar Hall A\n"
                        + "Capacity: 100\n"
                        + "Projector: Yes\n"
                        + "Status: Available";

            } else if (resourceType.equals("Classroom")
                    && studentCount <= 60) {

                result = "Suitable Resource Found!\n\n"
                        + "Resource: Classroom B\n"
                        + "Capacity: 60\n"
                        + "Projector: Yes\n"
                        + "Status: Available";

            } else if (resourceType.equals("Computer Lab")
                    && studentCount <= 50) {

                result = "Suitable Resource Found!\n\n"
                        + "Resource: Computer Lab 3\n"
                        + "Capacity: 50\n"
                        + "Projector: Yes\n"
                        + "Status: Available";

            } else {

                result = "No suitable resource available.";
            }

            JOptionPane.showMessageDialog(this, result);

        } else if (e.getSource() == backButton) {

            dispose();
            new FacultyFrame();
        }
    }

    public static void main(String[] args) {

        new RequestResourceFrame();
    }
}