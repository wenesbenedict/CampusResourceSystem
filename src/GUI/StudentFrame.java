package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import Database.DBConnection;

public class StudentFrame extends JFrame implements ActionListener {

    JLabel titleLabel;
    JLabel academicLabel;
    JLabel eventCountLabel;

    JTable eventsTable;
    DefaultTableModel tableModel;

    JTextArea detailsArea;

    JButton refreshButton;
    JButton logoutButton;

    public StudentFrame() {

        setTitle("Student Dashboard - Karunya Campus Allocation & Booking System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 250));

        createHeader();
        createEventTable();
        createDetailsPanel();
        createFooter();

        loadStudentDetails();
        loadEvents();

        setVisible(true);
    }

    private void createHeader() {

        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));

        titleLabel = new JLabel(
                "Karunya Campus Allocation & Booking System",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 21));
        titleLabel.setForeground(new Color(35, 55, 80));

        academicLabel = new JLabel(
                "Loading academic details...",
                SwingConstants.CENTER
        );
        academicLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        eventCountLabel = new JLabel(
                "Approved Events: 0",
                SwingConstants.CENTER
        );
        eventCountLabel.setFont(new Font("Arial", Font.BOLD, 14));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(academicLabel, BorderLayout.CENTER);
        headerPanel.add(eventCountLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createEventTable() {

        tableModel = new DefaultTableModel(
                new String[] {
                        "ID",
                        "Event",
                        "Type",
                        "Date",
                        "Time",
                        "Venue",
                        "Faculty",
                        "Status"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventsTable = new JTable(tableModel);
        eventsTable.setRowHeight(30);
        eventsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        eventsTable.getTableHeader().setReorderingAllowed(false);
        eventsTable.setShowGrid(true);
        eventsTable.setGridColor(new Color(220, 225, 230));
        eventsTable.setSelectionBackground(new Color(210, 225, 240));
        eventsTable.setFillsViewportHeight(true);
        eventsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventsTable.setAutoCreateRowSorter(true);
        eventsTable.setToolTipText(
                "Select an event to view complete details."
        );

        eventsTable.getSelectionModel().addListSelectionListener(
                e -> showSelectedEvent()
        );

        eventsTable.getColumnModel().getColumn(0).setMinWidth(0);
        eventsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        eventsTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        eventsTable.getColumnModel().getColumn(1).setPreferredWidth(190);
        eventsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        eventsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        eventsTable.getColumnModel().getColumn(4).setPreferredWidth(160);
        eventsTable.getColumnModel().getColumn(5).setPreferredWidth(170);
        eventsTable.getColumnModel().getColumn(6).setPreferredWidth(130);
        eventsTable.getColumnModel().getColumn(7).setPreferredWidth(90);
    }

    private void createDetailsPanel() {

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(
                BorderFactory.createTitledBorder("Approved Academic Events")
        );
        tablePanel.add(new JScrollPane(eventsTable), BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(
                BorderFactory.createTitledBorder("Selected Event Details")
        );

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsArea.setMargin(new Insets(10, 10, 10, 10));
        detailsArea.setText(
                "Select an event from the table to view its details."
        );

        detailsPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                tablePanel,
                detailsPanel
        );
        splitPane.setDividerLocation(340);
        splitPane.setResizeWeight(0.65);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    private void createFooter() {

        refreshButton = new JButton("Refresh Events");
        logoutButton = new JButton("Logout");

        refreshButton.addActionListener(this);
        logoutButton.addActionListener(this);

        JPanel footerPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 8)
        );
        refreshButton.setPreferredSize(new Dimension(130, 34));
        logoutButton.setPreferredSize(new Dimension(100, 34));
        refreshButton.setBackground(new Color(45, 91, 140));
        refreshButton.setForeground(Color.WHITE);
        footerPanel.add(refreshButton);
        footerPanel.add(logoutButton);

        add(footerPanel, BorderLayout.SOUTH);
    }

    public void loadStudentDetails() {

        int studentId = LoginFrame.currentUserId;

        if (studentId == 0) {
            academicLabel.setText("No student user is logged in.");
            return;
        }

        String sql =
                "SELECT "
                + "u.name, "
                + "d.department_name, "
                + "p.programme_name, "
                + "s.year, "
                + "s.section_name "
                + "FROM users u "
                + "LEFT JOIN departments d "
                + "ON u.department_id = d.department_id "
                + "LEFT JOIN programmes p "
                + "ON u.programme_id = p.programme_id "
                + "LEFT JOIN sections s "
                + "ON u.section_id = s.section_id "
                + "WHERE u.user_id = ? "
                + "AND u.role = 'Student'";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setInt(1, studentId);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {
                    String department = safeValue(rs.getString("department_name"));
                    String programme = safeValue(rs.getString("programme_name"));
                    String year = safeValue(rs.getString("year"));
                    String section = safeValue(rs.getString("section_name"));

                    academicLabel.setText(
                            "My Academic Group: "
                            + department
                            + "  |  "
                            + programme
                            + "  |  Year "
                            + year
                            + "  |  Section "
                            + section
                    );
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load student details:\n" + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    public void loadEvents() {

        tableModel.setRowCount(0);

        int studentId = LoginFrame.currentUserId;

        if (studentId == 0) {
            detailsArea.setText("No student user is logged in.");
            return;
        }

        String sql =
                "SELECT "
                + "e.event_id, "
                + "e.event_name, "
                + "e.event_type, "
                + "f.name AS faculty_name, "
                + "e.event_date, "
                + "e.start_time, "
                + "e.end_time, "
                + "r.resource_name, "
                + "b.status "
                + "FROM users st "
                + "JOIN events e "
                + "ON e.department_id = st.department_id "
                + "AND (e.programme_id IS NULL OR e.programme_id = st.programme_id) "
                + "AND (e.section_id IS NULL OR e.section_id = st.section_id) "
                + "JOIN users f "
                + "ON e.faculty_id = f.user_id "
                + "JOIN bookings b "
                + "ON b.event_id = e.event_id "
                + "JOIN resources r "
                + "ON b.resource_id = r.resource_id "
                + "WHERE st.user_id = ? "
                + "AND st.role = 'Student' "
                + "AND st.status = 'Approved' "
                + "AND b.status = 'Approved' "
    + "AND f.status = 'Approved' "
                + "ORDER BY e.event_date, e.start_time";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setInt(1, studentId);

            try (ResultSet rs = pst.executeQuery()) {

                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("dd/MM/yyyy");

                SimpleDateFormat timeFormat =
                        new SimpleDateFormat("hh:mm a");

                while (rs.next()) {
                    tableModel.addRow(
                            new Object[] {
                                    rs.getInt("event_id"),
                                    rs.getString("event_name"),
                                    safeValue(rs.getString("event_type")),
                                    dateFormat.format(rs.getDate("event_date")),
                                    timeFormat.format(rs.getTime("start_time"))
                                            + " - "
                                            + timeFormat.format(rs.getTime("end_time")),
                                    rs.getString("resource_name"),
                                    rs.getString("faculty_name"),
                                    rs.getString("status")
                            }
                    );
                }
            }

            int count = tableModel.getRowCount();

            eventCountLabel.setText("Approved Events: " + count);

            if (count == 0) {
                detailsArea.setText(
                        "No approved events are available for your academic group."
                );
            } else {
                detailsArea.setText(
                        "Select an event from the table to view its details."
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n" + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    private void showSelectedEvent() {

        int selectedRow = eventsTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow = eventsTable.convertRowIndexToModel(selectedRow);

        int eventId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        String sql =
                "SELECT "
                + "e.event_name, "
                + "e.event_type, "
                + "f.name AS faculty_name, "
                + "d.department_name, "
                + "p.programme_name, "
                + "s.year, "
                + "s.section_name, "
                + "e.student_count, "
                + "e.event_date, "
                + "e.start_time, "
                + "e.end_time, "
                + "r.resource_name, "
                + "r.resource_type, "
                + "b.status "
                + "FROM users st "
                + "JOIN events e "
                + "ON e.department_id = st.department_id "
                + "AND (e.programme_id IS NULL OR e.programme_id = st.programme_id) "
                + "AND (e.section_id IS NULL OR e.section_id = st.section_id) "
                + "JOIN users f "
                + "ON e.faculty_id = f.user_id "
                + "JOIN bookings b "
                + "ON b.event_id = e.event_id "
                + "JOIN resources r "
                + "ON b.resource_id = r.resource_id "
                + "LEFT JOIN departments d "
                + "ON e.department_id = d.department_id "
                + "LEFT JOIN programmes p "
                + "ON e.programme_id = p.programme_id "
                + "LEFT JOIN sections s "
                + "ON e.section_id = s.section_id "
                + "WHERE st.user_id = ? "
                + "AND st.role = 'Student' "
                + "AND st.status = 'Approved' "
                + "AND e.event_id = ? "
                + "AND b.status = 'Approved' "
    + "AND f.status = 'Approved'";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setInt(1, LoginFrame.currentUserId);
            pst.setInt(2, eventId);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    SimpleDateFormat dateFormat =
                            new SimpleDateFormat("dd/MM/yyyy");

                    SimpleDateFormat timeFormat =
                            new SimpleDateFormat("hh:mm a");

                    StringBuilder details = new StringBuilder();

                    details.append("EVENT\n");
                    details.append(rs.getString("event_name"));

                    details.append("\nEvent Type: ");
                    details.append(safeValue(rs.getString("event_type")));

                    details.append("\n\nFACULTY\n");
                    details.append(rs.getString("faculty_name"));

                    details.append("\n\nACADEMIC GROUP\n");
                    details.append("Department: ");
                    details.append(safeValue(rs.getString("department_name")));

                    details.append("\nProgramme: ");
                    String programme = rs.getString("programme_name");
                    details.append(
                            programme == null ? "Department-wide" : programme
                    );

                    details.append("\nYear: ");
                    String year = rs.getString("year");
                    details.append(year == null ? "All Years" : year);

                    details.append("\nSection: ");
                    String section = rs.getString("section_name");
                    details.append(section == null ? "All Sections" : section);

                    details.append("\nStudents: ");
                    details.append(rs.getInt("student_count"));

                    details.append("\n\nSCHEDULE\n");
                    details.append("Date: ");
                    details.append(
                            dateFormat.format(rs.getDate("event_date"))
                    );

                    details.append("\nTime: ");
                    details.append(
                            timeFormat.format(rs.getTime("start_time"))
                    );
                    details.append(" - ");
                    details.append(
                            timeFormat.format(rs.getTime("end_time"))
                    );

                    details.append("\n\nVENUE\n");
                    details.append("Type: ");
                    details.append(
                            safeValue(rs.getString("resource_type"))
                    );

                    details.append("\nVenue: ");
                    details.append(rs.getString("resource_name"));

                    details.append("\n\nSTATUS: ");
                    details.append(rs.getString("status"));

                    detailsArea.setText(details.toString());
                    detailsArea.setCaretPosition(0);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load event details:\n" + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    private String safeValue(String value) {

        if (value == null || value.trim().isEmpty()) {
            return "Not available";
        }

        return value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == refreshButton) {
            loadStudentDetails();
            loadEvents();

        } else if (e.getSource() == logoutButton) {
            dispose();
            new LoginFrame();
        }
    }

    public static void main(String[] args) {
        new StudentFrame();
    }
}
