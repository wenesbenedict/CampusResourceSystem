package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import Database.DBConnection;

public class CreateEventFrame extends JFrame implements ActionListener {

    JLabel titleLabel;
    JLabel eventNameLabel;
    JLabel eventTypeLabel;
    JLabel levelLabel;
    JLabel departmentLabel;
    JLabel programmeLabel;
    JLabel yearLabel;
    JLabel sectionLabel;
    JLabel studentCountLabel;
    JLabel dateLabel;
    JLabel startTimeLabel;
    JLabel endTimeLabel;
    JLabel resourceTypeLabel;
    JLabel commentLabel;
    JLabel commentCountLabel;

    JTextField eventNameField;
    JTextField departmentField;
    JTextField dateField;
    JTextField startTimeField;
    JTextField endTimeField;

    JTextArea commentArea;

    JComboBox<String> eventTypeBox;
    JComboBox<String> levelBox;
    JComboBox<String> programmeBox;
    JComboBox<String> yearBox;
    JComboBox<String> sectionBox;
    JComboBox<String> resourceTypeBox;

    JSpinner studentCountSpinner;

    JButton dateButton;
    JButton startTimeButton;
    JButton endTimeButton;
    JButton submitButton;
    JButton backButton;

    JProgressBar progressBar;

    HashMap<String, Integer> programmeMap;
    HashMap<String, Integer> sectionMap;

    int facultyDepartmentId = 0;
    String facultyDepartmentName = "";

    java.sql.Date selectedEventDate;
    java.sql.Time selectedStartTime;
    java.sql.Time selectedEndTime;

    public CreateEventFrame() {

        setTitle("Karunya Campus Allocation & Booking System");
        setSize(800, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 0, 25));
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel formPanel = new JPanel(null);
        formPanel.setPreferredSize(new java.awt.Dimension(650, 780));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        createForm(formPanel);
        mainPanel.add(formPanel);
        add(mainPanel, BorderLayout.CENTER);

        loadFacultyDepartment();
        loadProgrammes();
        updateSmartUI();

        setVisible(true);
    }

    private void createForm(JPanel panel) {

        titleLabel = new JLabel(
                "Create Event & Request Resource",
                SwingConstants.CENTER);
        titleLabel.setBounds(100, 15, 450, 30);
        titleLabel.setFont(new java.awt.Font(
                "Arial", java.awt.Font.BOLD, 18));

        eventNameLabel = new JLabel("Event Name:");
        eventNameLabel.setBounds(35, 65, 130, 25);

        eventNameField = new JTextField();
        eventNameField.setBounds(180, 65, 410, 28);
        eventNameField.setToolTipText(
                "Enter the name of the academic event.");

        eventTypeLabel = new JLabel("Event Type:");
        eventTypeLabel.setBounds(35, 110, 130, 25);

        eventTypeBox = new JComboBox<>(new String[] {
                "Project Review",
                "Project Presentation",
                "Workshop",
                "Seminar",
                "Presentation",
                "Faculty Meeting",
                "Other"
        });
        eventTypeBox.setBounds(180, 110, 410, 28);

        levelLabel = new JLabel("Level:");
        levelLabel.setBounds(35, 155, 130, 25);

        levelBox = new JComboBox<>(new String[] {
                "UG",
                "PG"
        });
        levelBox.setBounds(180, 155, 410, 28);
        levelBox.addActionListener(this);

        departmentLabel = new JLabel("Department:");
        departmentLabel.setBounds(35, 200, 130, 25);

        departmentField = new JTextField();
        departmentField.setBounds(180, 200, 410, 28);
        departmentField.setEditable(false);
        departmentField.setToolTipText(
                "Automatically taken from the logged-in faculty.");

        programmeLabel = new JLabel("Programme:");
        programmeLabel.setBounds(35, 245, 130, 25);

        programmeBox = new JComboBox<>();
        programmeBox.setBounds(180, 245, 410, 28);
        programmeBox.addActionListener(this);
        programmeBox.setToolTipText(
                "Programme available under the selected level.");

        yearLabel = new JLabel("Year:");
        yearLabel.setBounds(35, 290, 130, 25);

        yearBox = new JComboBox<>();
        yearBox.setBounds(180, 290, 410, 28);
        yearBox.addActionListener(this);

        sectionLabel = new JLabel("Section:");
        sectionLabel.setBounds(35, 335, 130, 25);

        sectionBox = new JComboBox<>();
        sectionBox.setBounds(180, 335, 410, 28);

        studentCountLabel = new JLabel("Student Count:");
        studentCountLabel.setBounds(35, 380, 130, 25);

        studentCountSpinner = new JSpinner(
                new SpinnerNumberModel(1, 1, 1000, 1));
        studentCountSpinner.setBounds(180, 380, 410, 28);
        studentCountSpinner.setToolTipText(
                "Select the expected number of students.");
        studentCountSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateSmartUI();
            }
        });

        dateLabel = new JLabel("Date:");
        dateLabel.setBounds(35, 425, 130, 25);

        dateField = new JTextField();
        dateField.setBounds(180, 425, 290, 28);
        dateField.setEditable(false);

        dateButton = new JButton("Calendar");
        dateButton.setBounds(480, 425, 110, 28);
        dateButton.addActionListener(this);
        dateButton.setToolTipText(
                "Choose the event date from a calendar.");

        startTimeLabel = new JLabel("Start Time:");
        startTimeLabel.setBounds(35, 470, 130, 25);

        startTimeField = new JTextField();
        startTimeField.setBounds(180, 470, 290, 28);
        startTimeField.setEditable(false);

        startTimeButton = new JButton("Clock");
        startTimeButton.setBounds(480, 470, 110, 28);
        startTimeButton.addActionListener(this);
        startTimeButton.setToolTipText(
                "Choose the event start time.");

        endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setBounds(35, 515, 130, 25);

        endTimeField = new JTextField();
        endTimeField.setBounds(180, 515, 290, 28);
        endTimeField.setEditable(false);

        endTimeButton = new JButton("Clock");
        endTimeButton.setBounds(480, 515, 110, 28);
        endTimeButton.addActionListener(this);
        endTimeButton.setToolTipText(
                "Choose the event end time.");

        resourceTypeLabel = new JLabel("Resource Type:");
        resourceTypeLabel.setBounds(35, 560, 130, 25);

        resourceTypeBox = new JComboBox<>(new String[] {
                "Auditorium",
                "Seminar Hall",
                "Gallery Hall",
                "Laboratory"
        });
        resourceTypeBox.setBounds(180, 560, 410, 28);
        resourceTypeBox.setToolTipText(
                "Select the type of campus resource required.");
        resourceTypeBox.addActionListener(this);

        commentLabel = new JLabel("Admin Note (Optional):");
        commentLabel.setBounds(35, 605, 145, 25);
        commentLabel.setToolTipText(
                "Add requirements or special instructions for the administrator.");

        commentArea = new JTextArea();
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setMargin(new Insets(6, 6, 6, 6));
        commentArea.setToolTipText(
                "Example: Need projector, front seating, extra tables, etc.");

        JScrollPane commentScrollPane = new JScrollPane(commentArea);
        commentScrollPane.setBounds(180, 600, 410, 70);

        commentCountLabel = new JLabel("0 / 300");
        commentCountLabel.setBounds(510, 675, 80, 20);
        commentCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        commentArea.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        updateCommentCount();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        updateCommentCount();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        updateCommentCount();
                    }
                });

        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(180, 705, 410, 25);
        progressBar.setStringPainted(true);

        submitButton = new JButton("Find & Request");
        submitButton.setBounds(180, 745, 195, 35);
        submitButton.addActionListener(this);
        submitButton.setToolTipText(
                "Find an available resource and send the request to admin.");

        backButton = new JButton("Back");
        backButton.setBounds(395, 745, 195, 35);
        backButton.addActionListener(this);

        panel.add(titleLabel);

        panel.add(eventNameLabel);
        panel.add(eventNameField);

        panel.add(eventTypeLabel);
        panel.add(eventTypeBox);

        panel.add(levelLabel);
        panel.add(levelBox);

        panel.add(departmentLabel);
        panel.add(departmentField);

        panel.add(programmeLabel);
        panel.add(programmeBox);

        panel.add(yearLabel);
        panel.add(yearBox);

        panel.add(sectionLabel);
        panel.add(sectionBox);

        panel.add(studentCountLabel);
        panel.add(studentCountSpinner);

        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(dateButton);

        panel.add(startTimeLabel);
        panel.add(startTimeField);
        panel.add(startTimeButton);

        panel.add(endTimeLabel);
        panel.add(endTimeField);
        panel.add(endTimeButton);

        panel.add(resourceTypeLabel);
        panel.add(resourceTypeBox);

        panel.add(commentLabel);
        panel.add(commentScrollPane);
        panel.add(commentCountLabel);

        panel.add(progressBar);
        panel.add(submitButton);
        panel.add(backButton);
    }

    private void updateCommentCount() {

        if (commentArea.getText().length() > 300) {
            SwingUtilities.invokeLater(() -> {
                String text = commentArea.getText();
                commentArea.setText(text.substring(0, 300));
            });
        }

        commentCountLabel.setText(
                commentArea.getText().length() + " / 300");
    }

    public void loadFacultyDepartment() {

        int facultyId = LoginFrame.currentUserId;

        if (facultyId == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No faculty user is logged in!");
            return;
        }

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(
                        "SELECT d.department_id, d.department_name "
                        + "FROM users u "
                        + "JOIN departments d "
                        + "ON u.department_id = d.department_id "
                        + "WHERE u.user_id = ?")
        ) {

            pst.setInt(1, facultyId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    facultyDepartmentId = rs.getInt("department_id");
                    facultyDepartmentName = rs.getString("department_name");
                    departmentField.setText(facultyDepartmentName);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load faculty department!");
            e.printStackTrace();
        }
    }

    public void loadProgrammes() {

        programmeMap = new HashMap<>();
        programmeBox.removeAllItems();
        yearBox.removeAllItems();
        sectionBox.removeAllItems();

        if (facultyDepartmentId == 0) {
            return;
        }

        String level = (String) levelBox.getSelectedItem();
        if (level == null) {
            return;
        }

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(
                        "SELECT programme_id, programme_name "
                        + "FROM programmes "
                        + "WHERE department_id = ? "
                        + "AND level = ? "
                        + "ORDER BY programme_name")
        ) {

            pst.setInt(1, facultyDepartmentId);
            pst.setString(2, level);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int programmeId = rs.getInt("programme_id");
                    String programmeName = rs.getString("programme_name");

                    programmeMap.put(programmeName, programmeId);
                    programmeBox.addItem(programmeName);
                }
            }

            loadYears();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load programmes!");
            e.printStackTrace();
        }
    }

    public void loadYears() {

        yearBox.removeAllItems();
        sectionBox.removeAllItems();

        String programmeName =
                (String) programmeBox.getSelectedItem();

        if (programmeName == null) {
            return;
        }

        Integer programmeId = programmeMap.get(programmeName);
        if (programmeId == null) {
            return;
        }

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(
                        "SELECT duration_years "
                        + "FROM programmes "
                        + "WHERE programme_id = ?")
        ) {

            pst.setInt(1, programmeId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int duration = rs.getInt("duration_years");

                    for (int i = 1; i <= duration; i++) {
                        yearBox.addItem(String.valueOf(i));
                    }
                }
            }

            loadSections();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load years!");
            e.printStackTrace();
        }
    }

    public void loadSections() {

        sectionMap = new HashMap<>();
        sectionBox.removeAllItems();

        String programmeName =
                (String) programmeBox.getSelectedItem();
        String yearText =
                (String) yearBox.getSelectedItem();

        if (programmeName == null || yearText == null) {
            return;
        }

        Integer programmeId = programmeMap.get(programmeName);
        if (programmeId == null) {
            return;
        }

        int year = Integer.parseInt(yearText);

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(
                        "SELECT section_id, section_name "
                        + "FROM sections "
                        + "WHERE programme_id = ? "
                        + "AND year = ? "
                        + "ORDER BY section_name")
        ) {

            pst.setInt(1, programmeId);
            pst.setInt(2, year);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int sectionId = rs.getInt("section_id");
                    String sectionName = rs.getString("section_name");

                    sectionMap.put(sectionName, sectionId);
                    sectionBox.addItem(sectionName);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sections!");
            e.printStackTrace();
        }
    }

    public void chooseDate() {

        Date pickedDate = DatePickerDialog.showDialog(
                this,
                new Date());

        if (pickedDate != null) {
            selectedEventDate =
                    new java.sql.Date(pickedDate.getTime());

            SimpleDateFormat sdf =
                    new SimpleDateFormat("dd/MM/yyyy");

            dateField.setText(sdf.format(pickedDate));
            updateSmartUI();
        }
    }

    public void chooseStartTime() {

        TimePickerDialog dialog =
                new TimePickerDialog(
                        this,
                        selectedStartTime);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (dialog.getSelectedTime() != null) {
            selectedStartTime = dialog.getSelectedTime();
            startTimeField.setText(formatTime(selectedStartTime));
            updateSmartUI();
        }
    }

    public void chooseEndTime() {

        java.sql.Time initialTime = selectedEndTime;

        if (initialTime == null && selectedStartTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedStartTime);
            calendar.add(Calendar.HOUR, 1);
            initialTime =
                    new java.sql.Time(calendar.getTimeInMillis());
        }

        TimePickerDialog dialog =
                new TimePickerDialog(
                        this,
                        initialTime);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (dialog.getSelectedTime() != null) {
            selectedEndTime = dialog.getSelectedTime();
            endTimeField.setText(formatTime(selectedEndTime));
            updateSmartUI();
        }
    }

    private String formatTime(java.sql.Time time) {
        return new SimpleDateFormat("hh:mm a").format(time);
    }

    public void updateSmartUI() {

        int completed = 0;

        if (!eventNameField.getText().trim().isEmpty()) {
            completed++;
        }

        if (eventTypeBox.getSelectedItem() != null) {
            completed++;
        }

        if (levelBox.getSelectedItem() != null) {
            completed++;
        }

        if (!departmentField.getText().trim().isEmpty()) {
            completed++;
        }

        if (programmeBox.getSelectedItem() != null) {
            completed++;
        }

        if (yearBox.getSelectedItem() != null) {
            completed++;
        }

        if (sectionBox.getSelectedItem() != null) {
            completed++;
        }

        if (selectedEventDate != null) {
            completed++;
        }

        if (selectedStartTime != null) {
            completed++;
        }

        if (selectedEndTime != null) {
            completed++;
        }

        if (resourceTypeBox.getSelectedItem() != null) {
            completed++;
        }

        int percentage = (completed * 100) / 11;
        progressBar.setValue(percentage);
        progressBar.setString(
                "Request Completion: " + percentage + "%");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == levelBox) {
            loadProgrammes();
        }
        else if (e.getSource() == programmeBox) {
            loadYears();
        }
        else if (e.getSource() == yearBox) {
            loadSections();
        }
        else if (e.getSource() == dateButton) {
            chooseDate();
        }
        else if (e.getSource() == startTimeButton) {
            chooseStartTime();
        }
        else if (e.getSource() == endTimeButton) {
            chooseEndTime();
        }
        else if (e.getSource() == submitButton) {
            createEventAndBooking();
        }
        else if (e.getSource() == backButton) {
            dispose();
            new FacultyFrame();
        }

        updateSmartUI();
    }

    public void createEventAndBooking() {

        String eventName = eventNameField.getText().trim();
        String eventType = (String) eventTypeBox.getSelectedItem();
        String programmeName = (String) programmeBox.getSelectedItem();
        String yearText = (String) yearBox.getSelectedItem();
        String sectionName = (String) sectionBox.getSelectedItem();
        String resourceType = (String) resourceTypeBox.getSelectedItem();
        String comments = commentArea.getText().trim();

        int studentCount =
                (Integer) studentCountSpinner.getValue();

        if (eventName.isEmpty()
                || eventType == null
                || programmeName == null
                || yearText == null
                || sectionName == null
                || selectedEventDate == null
                || selectedStartTime == null
                || selectedEndTime == null
                || resourceType == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all event details.");
            return;
        }

        if (!selectedStartTime.before(selectedEndTime)) {
            JOptionPane.showMessageDialog(
                    this,
                    "End time must be after start time.");
            return;
        }

        int facultyId = LoginFrame.currentUserId;
        Integer programmeId = programmeMap.get(programmeName);
        Integer sectionId = sectionMap.get(sectionName);

        if (facultyId == 0
                || facultyDepartmentId == 0
                || programmeId == null
                || sectionId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid faculty or academic details!");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                String resourceSql =
                        "SELECT r.resource_id, r.resource_name "
                        + "FROM resources r "
                        + "WHERE r.resource_type = ? "
                        + "AND r.status = 'Available' "
                        + "AND (r.capacity IS NULL OR r.capacity >= ?) "
                        + "AND (r.scope = 'Campus' "
                        + "OR (r.scope = 'Department' "
                        + "AND r.department_id = ?)) "
                        + "AND NOT EXISTS ( "
                        + "SELECT 1 FROM bookings b "
                        + "JOIN events e ON b.event_id = e.event_id "
                        + "WHERE b.resource_id = r.resource_id "
                        + "AND b.status IN ('Pending', 'Approved') "
                        + "AND e.event_date = ? "
                        + "AND e.start_time < ? "
                        + "AND e.end_time > ?) "
                        + "ORDER BY CASE "
                        + "WHEN r.scope = 'Department' THEN 0 ELSE 1 END, "
                        + "r.resource_name LIMIT 1";

                int resourceId;
                String resourceName;

                try (PreparedStatement resourcePst =
                        con.prepareStatement(resourceSql)) {

                    resourcePst.setString(1, resourceType);
                    resourcePst.setInt(2, studentCount);
                    resourcePst.setInt(3, facultyDepartmentId);
                    resourcePst.setDate(4, selectedEventDate);
                    resourcePst.setTime(5, selectedEndTime);
                    resourcePst.setTime(6, selectedStartTime);

                    try (ResultSet rs = resourcePst.executeQuery()) {

                        if (!rs.next()) {
                            con.rollback();
                            JOptionPane.showMessageDialog(
                                    this,
                                    "No available " + resourceType
                                            + " found for the selected date and time.");
                            return;
                        }

                        resourceId = rs.getInt("resource_id");
                        resourceName = rs.getString("resource_name");
                    }
                }

                String eventSql =
                        "INSERT INTO events "
                        + "(event_name, event_type, faculty_id, department_id, "
                        + "programme_id, section_id, faculty_comments, student_count, "
                        + "event_date, start_time, end_time) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                int eventId;

                try (PreparedStatement eventPst =
                        con.prepareStatement(
                                eventSql,
                                java.sql.Statement.RETURN_GENERATED_KEYS)) {

                    eventPst.setString(1, eventName);
                    eventPst.setString(2, eventType);
                    eventPst.setInt(3, facultyId);
                    eventPst.setInt(4, facultyDepartmentId);
                    eventPst.setInt(5, programmeId);
                    eventPst.setInt(6, sectionId);

                    if (comments.isEmpty()) {
                        eventPst.setNull(7, Types.VARCHAR);
                    } else {
                        eventPst.setString(7, comments);
                    }

                    eventPst.setInt(8, studentCount);
                    eventPst.setDate(9, selectedEventDate);
                    eventPst.setTime(10, selectedStartTime);
                    eventPst.setTime(11, selectedEndTime);

                    eventPst.executeUpdate();

                    try (ResultSet keys = eventPst.getGeneratedKeys()) {
                        if (!keys.next()) {
                            con.rollback();
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Could not create event!");
                            return;
                        }
                        eventId = keys.getInt(1);
                    }
                }

                String bookingSql =
                        "INSERT INTO bookings (event_id, resource_id, status) "
                        + "VALUES (?, ?, 'Pending')";

                try (PreparedStatement bookingPst =
                        con.prepareStatement(bookingSql)) {

                    bookingPst.setInt(1, eventId);
                    bookingPst.setInt(2, resourceId);
                    bookingPst.executeUpdate();
                }

                con.commit();

                JOptionPane.showMessageDialog(
                        this,
                        "Event and Resource Request Created!\n\n"
                                + "Event: " + eventName + "\n"
                                + "Type: " + eventType + "\n"
                                + "Department: " + facultyDepartmentName + "\n"
                                + "Programme: " + programmeName + "\n"
                                + "Year: " + yearText + "\n"
                                + "Section: " + sectionName + "\n"
                                + "Students: " + studentCount + "\n"
                                + "Date: " + dateField.getText() + "\n"
                                + "Time: " + startTimeField.getText() + " - "
                                + endTimeField.getText() + "\n"
                                + "Resource: " + resourceName + "\n"
                                + "Status: Pending Approval");

                clearFields();

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void clearFields() {

        eventNameField.setText("");
        studentCountSpinner.setValue(1);
        dateField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");
        commentArea.setText("");

        selectedEventDate = null;
        selectedStartTime = null;
        selectedEndTime = null;

        levelBox.setSelectedIndex(0);
        updateCommentCount();
        updateSmartUI();
    }

    public static void main(String[] args) {
        new CreateEventFrame();
    }

    static class DatePickerDialog extends JDialog {

        private Date selectedDate;
        private JPanel calendarPanel;
        private JComboBox<String> monthBox;
        private JSpinner yearSpinner;
        private Calendar displayCalendar;

        public DatePickerDialog(Window owner, Date initialDate) {

            super(owner, "Select Event Date", ModalityType.APPLICATION_MODAL);

            setSize(400, 350);
            setResizable(false);
            setLayout(new BorderLayout(10, 10));

            displayCalendar = new GregorianCalendar();
            displayCalendar.setTime(initialDate);

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            JButton previousButton = new JButton("<");
            JButton nextButton = new JButton(">");

            String[] months = {
                    "January", "February", "March", "April",
                    "May", "June", "July", "August",
                    "September", "October", "November", "December"
            };

            monthBox = new JComboBox<>(months);
            monthBox.setSelectedIndex(
                    displayCalendar.get(Calendar.MONTH));

            yearSpinner = new JSpinner(
                    new SpinnerNumberModel(
                            displayCalendar.get(Calendar.YEAR),
                            displayCalendar.get(Calendar.YEAR),
                            2100,
                            1));

            topPanel.add(previousButton);
            topPanel.add(monthBox);
            topPanel.add(yearSpinner);
            topPanel.add(nextButton);
            add(topPanel, BorderLayout.NORTH);

            calendarPanel = new JPanel(new GridLayout(0, 7, 3, 3));
            calendarPanel.setBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(calendarPanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(
                    new FlowLayout(FlowLayout.CENTER));

            JButton todayButton = new JButton("Today");
            JButton selectButton = new JButton("Select");
            JButton cancelButton = new JButton("Cancel");

            bottomPanel.add(todayButton);
            bottomPanel.add(selectButton);
            bottomPanel.add(cancelButton);
            add(bottomPanel, BorderLayout.SOUTH);

            previousButton.addActionListener(e -> {
                displayCalendar.add(Calendar.MONTH, -1);
                monthBox.setSelectedIndex(
                        displayCalendar.get(Calendar.MONTH));
                yearSpinner.setValue(
                        displayCalendar.get(Calendar.YEAR));
                buildCalendar();
            });

            nextButton.addActionListener(e -> {
                displayCalendar.add(Calendar.MONTH, 1);
                monthBox.setSelectedIndex(
                        displayCalendar.get(Calendar.MONTH));
                yearSpinner.setValue(
                        displayCalendar.get(Calendar.YEAR));
                buildCalendar();
            });

            monthBox.addActionListener(e -> {
                displayCalendar.set(
                        Calendar.MONTH,
                        monthBox.getSelectedIndex());
                buildCalendar();
            });

            yearSpinner.addChangeListener(e -> {
                int year = (Integer) yearSpinner.getValue();
                displayCalendar.set(Calendar.YEAR, year);
                buildCalendar();
            });

            todayButton.addActionListener(e -> {
                Date today = new Date();
                displayCalendar.setTime(today);
                selectedDate = today;
                monthBox.setSelectedIndex(
                        displayCalendar.get(Calendar.MONTH));
                yearSpinner.setValue(
                        displayCalendar.get(Calendar.YEAR));
                buildCalendar();
            });

            selectButton.addActionListener(e -> {
                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(
                            DatePickerDialog.this,
                            "Please select a date.");
                    return;
                }
                dispose();
            });

            cancelButton.addActionListener(e -> {
                selectedDate = null;
                dispose();
            });

            buildCalendar();
            setLocationRelativeTo(owner);
        }

        private void buildCalendar() {

            calendarPanel.removeAll();

            String[] days = {
                    "Sun", "Mon", "Tue", "Wed",
                    "Thu", "Fri", "Sat"
            };

            for (String day : days) {
                calendarPanel.add(
                        new JLabel(day, SwingConstants.CENTER));
            }

            Calendar calendar = new GregorianCalendar(
                    displayCalendar.get(Calendar.YEAR),
                    displayCalendar.get(Calendar.MONTH),
                    1);

            int firstDay =
                    calendar.get(Calendar.DAY_OF_WEEK) - 1;

            int maxDays =
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 0; i < firstDay; i++) {
                calendarPanel.add(new JLabel(""));
            }

            for (int day = 1; day <= maxDays; day++) {

                final int selectedDay = day;
                JButton dayButton =
                        new JButton(String.valueOf(day));

                dayButton.setMargin(
                        new Insets(2, 2, 2, 2));

                if (isPastDate(selectedDay)) {
                    dayButton.setEnabled(false);
                }

                if (isSelectedDate(selectedDay)) {
                    dayButton.setBorder(
                            BorderFactory.createLineBorder(
                                    Color.BLUE,
                                    2));
                }

                dayButton.addActionListener(e -> {
                    Calendar chosen = new GregorianCalendar(
                            displayCalendar.get(Calendar.YEAR),
                            displayCalendar.get(Calendar.MONTH),
                            selectedDay);
                    selectedDate = chosen.getTime();
                    buildCalendar();
                });

                calendarPanel.add(dayButton);
            }

            calendarPanel.revalidate();
            calendarPanel.repaint();
        }

        private boolean isPastDate(int day) {

            Calendar chosen = new GregorianCalendar(
                    displayCalendar.get(Calendar.YEAR),
                    displayCalendar.get(Calendar.MONTH),
                    day);

            Calendar today = new GregorianCalendar();

            chosen.set(Calendar.HOUR_OF_DAY, 0);
            chosen.set(Calendar.MINUTE, 0);
            chosen.set(Calendar.SECOND, 0);
            chosen.set(Calendar.MILLISECOND, 0);

            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            return chosen.before(today);
        }

        private boolean isSelectedDate(int day) {

            if (selectedDate == null) {
                return false;
            }

            Calendar selected = new GregorianCalendar();
            selected.setTime(selectedDate);

            return selected.get(Calendar.YEAR)
                    == displayCalendar.get(Calendar.YEAR)
                    && selected.get(Calendar.MONTH)
                    == displayCalendar.get(Calendar.MONTH)
                    && selected.get(Calendar.DAY_OF_MONTH) == day;
        }

        public static Date showDialog(Window owner, Date initialDate) {

            DatePickerDialog dialog =
                    new DatePickerDialog(owner, initialDate);

            dialog.setVisible(true);
            return dialog.selectedDate;
        }
    }

    static class TimePickerDialog extends JDialog {

        private JSpinner hourSpinner;
        private JSpinner minuteSpinner;
        private JComboBox<String> amPmBox;
        private JLabel previewLabel;
        private java.sql.Time selectedTime;

        public TimePickerDialog(Window owner, java.sql.Time initialTime) {

            super(owner, "Select Time", ModalityType.APPLICATION_MODAL);

            setSize(380, 230);
            setResizable(false);
            setLayout(new BorderLayout(10, 10));

            int hour = 9;
            int minute = 0;
            String amPm = "AM";

            if (initialTime != null) {

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(initialTime);

                int hour24 =
                        calendar.get(Calendar.HOUR_OF_DAY);

                minute =
                        calendar.get(Calendar.MINUTE);

                if (hour24 == 0) {
                    hour = 12;
                    amPm = "AM";
                }
                else if (hour24 < 12) {
                    hour = hour24;
                    amPm = "AM";
                }
                else if (hour24 == 12) {
                    hour = 12;
                    amPm = "PM";
                }
                else {
                    hour = hour24 - 12;
                    amPm = "PM";
                }
            }

            JLabel title = new JLabel(
                    "Choose Event Time",
                    SwingConstants.CENTER);
            add(title, BorderLayout.NORTH);

            JPanel timePanel = new JPanel(
                    new FlowLayout(
                            FlowLayout.CENTER,
                            10,
                            15));

            hourSpinner = new JSpinner(
                    new SpinnerNumberModel(
                            hour, 1, 12, 1));

            minuteSpinner = new JSpinner(
                    new SpinnerNumberModel(
                            minute, 0, 59, 1));

            amPmBox = new JComboBox<>(
                    new String[] { "AM", "PM" });
            amPmBox.setSelectedItem(amPm);

            timePanel.add(new JLabel("Hour"));
            timePanel.add(hourSpinner);
            timePanel.add(new JLabel("Minute"));
            timePanel.add(minuteSpinner);
            timePanel.add(amPmBox);

            add(timePanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(
                    new FlowLayout(FlowLayout.CENTER));

            previewLabel = new JLabel();
            JButton setButton = new JButton("Set Time");
            JButton cancelButton = new JButton("Cancel");

            bottomPanel.add(previewLabel);
            bottomPanel.add(setButton);
            bottomPanel.add(cancelButton);
            add(bottomPanel, BorderLayout.SOUTH);

            ChangeListener listener = e -> updatePreview();
            hourSpinner.addChangeListener(listener);
            minuteSpinner.addChangeListener(listener);
            amPmBox.addActionListener(e -> updatePreview());

            setButton.addActionListener(e -> {

                int selectedHour =
                        (Integer) hourSpinner.getValue();

                int selectedMinute =
                        (Integer) minuteSpinner.getValue();

                int hour24 = selectedHour % 12;

                if ("PM".equals(amPmBox.getSelectedItem())) {
                    hour24 += 12;
                }

                selectedTime = java.sql.Time.valueOf(
                        LocalTime.of(hour24, selectedMinute));

                dispose();
            });

            cancelButton.addActionListener(e -> {
                selectedTime = null;
                dispose();
            });

            updatePreview();
            setLocationRelativeTo(owner);
        }

        private void updatePreview() {

            int hour =
                    (Integer) hourSpinner.getValue();

            int minute =
                    (Integer) minuteSpinner.getValue();

            String amPm =
                    (String) amPmBox.getSelectedItem();

            previewLabel.setText(
                    String.format(
                            "%02d:%02d %s",
                            hour,
                            minute,
                            amPm));
        }

        public java.sql.Time getSelectedTime() {
            return selectedTime;
        }
    }
}
