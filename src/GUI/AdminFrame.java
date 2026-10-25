package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import Database.DBConnection;

public class AdminFrame extends JFrame implements ActionListener {

    JLabel titleLabel;

    JLabel pendingBookingLabel;
    JLabel pendingFacultyLabel;
    JLabel resourceLabel;

    JTabbedPane tabbedPane;

    JTable bookingTable;
    JTable facultyTable;
    JTable resourceTable;
    JTable bookingHistoryTable;

    DefaultTableModel bookingModel;
    DefaultTableModel facultyModel;
    DefaultTableModel resourceModel;
    DefaultTableModel bookingHistoryModel;

    JTextArea bookingDetailsArea;
    JTextArea facultyDetailsArea;
    JTextArea bookingHistoryDetailsArea;

    JButton approveBookingButton;
    JButton rejectBookingButton;
    JButton refreshBookingButton;

    JButton approveFacultyButton;
    JButton rejectFacultyButton;
    JButton refreshFacultyButton;

    JButton refreshResourceButton;
    JButton refreshHistoryButton;

    JButton logoutButton;

    public AdminFrame() {

        setTitle(
                "Admin Dashboard - Karunya Campus Allocation & Booking System"
        );

        setSize(1150, 750);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );
        getContentPane().setBackground(new Color(245, 247, 250));

        createHeader();

        createTabs();

        createFooter();

        loadBookingRequests();
        loadFacultyRequests();
        loadResources();
        loadBookingHistory();

        setVisible(true);
    }

    private void createHeader() {

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        titleLabel =
                new JLabel(
                        "Karunya Campus Allocation & Booking System",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );
        titleLabel.setForeground(new Color(35, 55, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));

        headerPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        JPanel summaryPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                10,
                                10
                        )
                );
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));

        pendingBookingLabel =
                new JLabel(
                        "Pending Bookings: 0",
                        SwingConstants.CENTER
                );

        pendingFacultyLabel =
                new JLabel(
                        "Pending Faculty: 0",
                        SwingConstants.CENTER
                );

        resourceLabel =
                new JLabel(
                        "Resources: 0",
                        SwingConstants.CENTER
                );

        summaryPanel.add(
                createSummaryPanel(
                        pendingBookingLabel,
                        "Booking Requests"
                )
        );

        summaryPanel.add(
                createSummaryPanel(
                        pendingFacultyLabel,
                        "Faculty Accounts"
                )
        );

        summaryPanel.add(
                createSummaryPanel(
                        resourceLabel,
                        "Campus Resources"
                )
        );

        headerPanel.add(
                summaryPanel,
                BorderLayout.CENTER
        );

        add(
                headerPanel,
                BorderLayout.NORTH
        );
    }

    private JPanel createSummaryPanel(
            JLabel label,
            String title
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        title
                )
        );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        panel.add(
                label,
                BorderLayout.CENTER
        );

        return panel;
    }

    private void createTabs() {

        tabbedPane =
                new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 13));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        createBookingTab();
        createBookingHistoryTab();

        createFacultyTab();

        createResourceTab();

        add(
                tabbedPane,
                BorderLayout.CENTER
        );
    }

    private void createBookingTab() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        bookingModel =
                new DefaultTableModel(
                        new String[] {
                                "Request",
                                "Event",
                                "Faculty",
                                "Department",
                                "Programme",
                                "Year",
                                "Section",
                                "Date",
                                "Time",
                                "Resource",
                                "Status"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        bookingTable =
                new JTable(
                        bookingModel
                );
        styleTable(bookingTable);

        bookingTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        bookingTable.setRowHeight(
                28
        );

        bookingTable.setAutoCreateRowSorter(
                true
        );

        bookingTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> showBookingDetails()
                );

        JScrollPane tableScrollPane =
                new JScrollPane(
                        bookingTable
                );

        JPanel detailsPanel =
                new JPanel(
                        new BorderLayout()
                );

        detailsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Selected Booking Details"
                )
        );

        bookingDetailsArea =
                new JTextArea();

        bookingDetailsArea.setEditable(
                false
        );

        bookingDetailsArea.setLineWrap(
                true
        );

        bookingDetailsArea.setWrapStyleWord(
                true
        );

        bookingDetailsArea.setMargin(
                new Insets(
                        10,
                        10,
                        10,
                        10
                )
        );

        bookingDetailsArea.setText(
                "Select a booking request to view complete details."
        );

        detailsPanel.add(
                new JScrollPane(
                        bookingDetailsArea
                ),
                BorderLayout.CENTER
        );

        detailsPanel.setPreferredSize(
                new Dimension(
                        1000,
                        220
                )
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                8
                        )
                );

        approveBookingButton =
                new JButton(
                        "Approve Booking"
                );

        rejectBookingButton =
                new JButton(
                        "Reject Booking"
                );

        refreshBookingButton =
                new JButton(
                        "Refresh"
                );

        approveBookingButton
                .addActionListener(this);

        rejectBookingButton
                .addActionListener(this);

        refreshBookingButton
                .addActionListener(this);

        approveBookingButton.setEnabled(
                false
        );

        rejectBookingButton.setEnabled(
                false
        );

        buttonPanel.add(
                approveBookingButton
        );

        buttonPanel.add(
                rejectBookingButton
        );

        buttonPanel.add(
                refreshBookingButton
        );

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.add(
                detailsPanel,
                BorderLayout.CENTER
        );

        bottomPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        panel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        panel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        tabbedPane.addTab(
                "Booking Requests",
                panel
        );
    }

    private void createFacultyTab() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        facultyModel =
                new DefaultTableModel(
                        new String[] {
                                "User ID",
                                "Name",
                                "Register Number",
                                "Email",
                                "Username",
                                "Department",
                                "Status"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        facultyTable =
                new JTable(
                        facultyModel
                );
        styleTable(facultyTable);

        facultyTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        facultyTable.setRowHeight(
                28
        );

        facultyTable.setAutoCreateRowSorter(
                true
        );

        facultyTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> showFacultyDetails()
                );

        JScrollPane tableScrollPane =
                new JScrollPane(
                        facultyTable
                );

        JPanel detailsPanel =
                new JPanel(
                        new BorderLayout()
                );

        detailsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Selected Faculty Account"
                )
        );

        facultyDetailsArea =
                new JTextArea();

        facultyDetailsArea.setEditable(
                false
        );

        facultyDetailsArea.setLineWrap(
                true
        );

        facultyDetailsArea.setWrapStyleWord(
                true
        );

        facultyDetailsArea.setMargin(
                new Insets(
                        10,
                        10,
                        10,
                        10
                )
        );

        facultyDetailsArea.setText(
                "Select a faculty account request to view details."
        );

        detailsPanel.add(
                new JScrollPane(
                        facultyDetailsArea
                ),
                BorderLayout.CENTER
        );

        detailsPanel.setPreferredSize(
                new Dimension(
                        1000,
                        190
                )
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                8
                        )
                );

        approveFacultyButton =
                new JButton(
                        "Approve Account"
                );

        rejectFacultyButton =
                new JButton(
                        "Reject Account"
                );

        refreshFacultyButton =
                new JButton(
                        "Refresh"
                );

        approveFacultyButton
                .addActionListener(this);

        rejectFacultyButton
                .addActionListener(this);

        refreshFacultyButton
                .addActionListener(this);

        approveFacultyButton.setEnabled(
                false
        );

        rejectFacultyButton.setEnabled(
                false
        );

        buttonPanel.add(
                approveFacultyButton
        );

        buttonPanel.add(
                rejectFacultyButton
        );

        buttonPanel.add(
                refreshFacultyButton
        );

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.add(
                detailsPanel,
                BorderLayout.CENTER
        );

        bottomPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        panel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        panel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        tabbedPane.addTab(
                "Faculty Accounts",
                panel
        );
    }

    private void createResourceTab() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        resourceModel =
                new DefaultTableModel(
                        new String[] {
                                "ID",
                                "Resource",
                                "Type",
                                "Department",
                                "Capacity",
                                "Projector",
                                "Scope",
                                "Status"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        resourceTable =
                new JTable(
                        resourceModel
                );
        styleTable(resourceTable);

        resourceTable.setRowHeight(
                28
        );

        resourceTable.setAutoCreateRowSorter(
                true
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        resourceTable
                );

        JPanel topPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        refreshResourceButton =
                new JButton(
                        "Refresh Resources"
                );

        refreshResourceButton
                .addActionListener(this);

        topPanel.add(
                refreshResourceButton
        );

        panel.add(
                topPanel,
                BorderLayout.NORTH
        );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        tabbedPane.addTab(
                "Resource Overview",
                panel
        );
    }

    private void createBookingHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        bookingHistoryModel = new DefaultTableModel(
                new String[] {
                        "Request",
                        "Event",
                        "Faculty",
                        "Department",
                        "Programme",
                        "Year",
                        "Section",
                        "Date",
                        "Time",
                        "Resource",
                        "Status"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingHistoryTable = new JTable(bookingHistoryModel);
        styleTable(bookingHistoryTable);
        bookingHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingHistoryTable.setRowHeight(28);
        bookingHistoryTable.setAutoCreateRowSorter(true);
        bookingHistoryTable.getSelectionModel().addListSelectionListener(e -> showBookingHistoryDetails());

        JScrollPane tableScrollPane = new JScrollPane(bookingHistoryTable);

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Booking Details"));

        bookingHistoryDetailsArea = new JTextArea();
        bookingHistoryDetailsArea.setEditable(false);
        bookingHistoryDetailsArea.setLineWrap(true);
        bookingHistoryDetailsArea.setWrapStyleWord(true);
        bookingHistoryDetailsArea.setMargin(new Insets(10, 10, 10, 10));
        bookingHistoryDetailsArea.setText("Select a booking from the history to view complete details.");

        detailsPanel.add(new JScrollPane(bookingHistoryDetailsArea), BorderLayout.CENTER);
        detailsPanel.setPreferredSize(new Dimension(1000, 220));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        refreshHistoryButton = new JButton("Refresh");
        refreshHistoryButton.addActionListener(this);
        buttonPanel.add(refreshHistoryButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(detailsPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Booking History", panel);
    }

    private void createFooter() {

        logoutButton =
                new JButton(
                        "Logout"
                );

        logoutButton.addActionListener(
                this
        );

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        bottomPanel.add(
                logoutButton
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );
    }

    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 225, 230));
        table.setSelectionBackground(new Color(210, 225, 240));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);
    }

    public void loadBookingRequests() {

        bookingModel.setRowCount(0);

        String sql =
                "SELECT "
                + "b.booking_id, "
                + "e.event_name, "
                + "e.event_type, "
                + "u.name AS faculty_name, "
                + "d.department_name, "
                + "p.programme_name, "
                + "s.year, "
                + "s.section_name, "
                + "e.event_date, "
                + "e.start_time, "
                + "e.end_time, "
                + "r.resource_name, "
                + "b.status "
                + "FROM bookings b "
                + "JOIN events e "
                + "ON b.event_id = e.event_id "
                + "JOIN users u "
                + "ON e.faculty_id = u.user_id "
                + "LEFT JOIN departments d "
                + "ON e.department_id = d.department_id "
                + "LEFT JOIN programmes p "
                + "ON e.programme_id = p.programme_id "
                + "LEFT JOIN sections s "
                + "ON e.section_id = s.section_id "
                + "JOIN resources r "
                + "ON b.resource_id = r.resource_id "
                + "WHERE b.status = 'Pending' "
                + "ORDER BY b.booking_id DESC";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql);

                ResultSet rs =
                        pst.executeQuery()
        ) {

            SimpleDateFormat dateFormat =
                    new SimpleDateFormat(
                            "dd/MM/yyyy"
                    );

            SimpleDateFormat timeFormat =
                    new SimpleDateFormat(
                            "hh:mm a"
                    );

            while (rs.next()) {

                bookingModel.addRow(
                        new Object[] {
                                rs.getInt(
                                        "booking_id"
                                ),
                                rs.getString(
                                        "event_name"
                                ),
                                rs.getString(
                                        "faculty_name"
                                ),
                                safeValue(
                                        rs.getString(
                                                "department_name"
                                        )
                                ),
                                safeValue(
                                        rs.getString(
                                                "programme_name"
                                        )
                                ),
                                safeValue(
                                        rs.getString(
                                                "year"
                                        )
                                ),
                                safeValue(
                                        rs.getString(
                                                "section_name"
                                        )
                                ),
                                dateFormat.format(
                                        rs.getDate(
                                                "event_date"
                                        )
                                ),
                                timeFormat.format(
                                        rs.getTime(
                                                "start_time"
                                        )
                                )
                                + " - "
                                + timeFormat.format(
                                        rs.getTime(
                                                "end_time"
                                        )
                                ),
                                rs.getString(
                                        "resource_name"
                                ),
                                rs.getString(
                                        "status"
                                )
                        }
                );
            }

            updateBookingCount();

            approveBookingButton.setEnabled(
                    false
            );

            rejectBookingButton.setEnabled(
                    false
            );

            bookingDetailsArea.setText(
                    bookingModel.getRowCount() == 0
                            ? "No pending booking requests."
                            : "Select a booking request to view its complete details."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load booking requests:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void loadBookingHistory() {
        bookingHistoryModel.setRowCount(0);

        String sql = "SELECT "
                + "b.booking_id, "
                + "e.event_name, "
                + "e.event_type, "
                + "u.name AS faculty_name, "
                + "d.department_name, "
                + "p.programme_name, "
                + "s.year, "
                + "s.section_name, "
                + "e.event_date, "
                + "e.start_time, "
                + "e.end_time, "
                + "r.resource_name, "
                + "b.status "
                + "FROM bookings b "
                + "JOIN events e ON b.event_id = e.event_id "
                + "JOIN users u ON e.faculty_id = u.user_id "
                + "LEFT JOIN departments d ON e.department_id = d.department_id "
                + "LEFT JOIN programmes p ON e.programme_id = p.programme_id "
                + "LEFT JOIN sections s ON e.section_id = s.section_id "
                + "JOIN resources r ON b.resource_id = r.resource_id "
                + "WHERE b.status IN ('Approved', 'Rejected') "
                + "ORDER BY b.booking_id DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()
        ) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

            while (rs.next()) {
                bookingHistoryModel.addRow(new Object[] {
                        rs.getInt("booking_id"),
                        rs.getString("event_name"),
                        rs.getString("event_type"),
                        rs.getString("faculty_name"),
                        safeValue(rs.getString("department_name")),
                        safeValue(rs.getString("programme_name")),
                        safeValue(rs.getString("year")),
                        safeValue(rs.getString("section_name")),
                        dateFormat.format(rs.getDate("event_date")),
                        timeFormat.format(rs.getTime("start_time")) + " - " + timeFormat.format(rs.getTime("end_time")),
                        rs.getString("resource_name"),
                        rs.getString("status")
                });
            }

            if (bookingHistoryModel.getRowCount() == 0) {
                bookingHistoryDetailsArea.setText("No booking history records found.");
            } else {
                bookingHistoryDetailsArea.setText("Select a booking from the history to view complete details.");
                bookingHistoryTable.setRowSelectionInterval(0, 0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load booking history:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showBookingDetails() {

        int selectedRow =
                bookingTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow =
                bookingTable.convertRowIndexToModel(
                        selectedRow
                );

        int bookingId =
                Integer.parseInt(
                        bookingModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        String sql =
                "SELECT "
                + "b.booking_id, "
                + "e.event_name, "
                + "e.event_type, "
                + "u.name AS faculty_name, "
                + "u.email, "
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
                + "r.capacity, "
                + "r.projector, "
                + "b.status, "
                + "e.faculty_comments "
                + "FROM bookings b "
                + "JOIN events e "
                + "ON b.event_id = e.event_id "
                + "JOIN users u "
                + "ON e.faculty_id = u.user_id "
                + "LEFT JOIN departments d "
                + "ON e.department_id = d.department_id "
                + "LEFT JOIN programmes p "
                + "ON e.programme_id = p.programme_id "
                + "LEFT JOIN sections s "
                + "ON e.section_id = s.section_id "
                + "JOIN resources r "
                + "ON b.resource_id = r.resource_id "
                + "WHERE b.booking_id = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    bookingId
            );

            try (
                    ResultSet rs =
                            pst.executeQuery()
            ) {

                if (rs.next()) {

                    SimpleDateFormat dateFormat =
                            new SimpleDateFormat(
                                    "dd/MM/yyyy"
                            );

                    SimpleDateFormat timeFormat =
                            new SimpleDateFormat(
                                    "hh:mm a"
                            );

                    StringBuilder details =
                            new StringBuilder();

                    details.append(
                            "Request #"
                    );

                    details.append(
                            rs.getInt(
                                    "booking_id"
                            )
                    );

                    details.append("\n\nEvent\n");

                    details.append(
                            rs.getString(
                                    "event_name"
                            )
                    );

                    details.append("\n");

                    details.append(
                            "Type: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "event_type"
                                    )
                            )
                    );

                    details.append(
                            "\n\nFaculty\n"
                    );

                    details.append(
                            rs.getString(
                                    "faculty_name"
                            )
                    );

                    details.append(
                            "\nEmail: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "email"
                                    )
                            )
                    );

                    details.append(
                            "\n\nAcademic Group\n"
                    );

                    details.append(
                            "Department: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "department_name"
                                    )
                            )
                    );

                    details.append(
                            "\nProgramme: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "programme_name"
                                    )
                            )
                    );

                    details.append(
                            "\nYear: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "year"
                                    )
                            )
                    );

                    details.append(
                            "\nSection: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "section_name"
                                    )
                            )
                    );

                    details.append(
                            "\nStudents: "
                    );

                    details.append(
                            rs.getInt(
                                    "student_count"
                            )
                    );

                    details.append(
                            "\n\nSchedule\n"
                    );

                    details.append(
                            "Date: "
                    );

                    details.append(
                            dateFormat.format(
                                    rs.getDate(
                                            "event_date"
                                    )
                            )
                    );

                    details.append(
                            "\nTime: "
                    );

                    details.append(
                            timeFormat.format(
                                    rs.getTime(
                                            "start_time"
                                    )
                            )
                    );

                    details.append(
                            " - "
                    );

                    details.append(
                            timeFormat.format(
                                    rs.getTime(
                                            "end_time"
                                    )
                            )
                    );

                    details.append(
                            "\n\nResource\n"
                    );

                    details.append(
                            "Requested Type: "
                    );

                    details.append(
                            rs.getString(
                                    "resource_type"
                            )
                    );

                    details.append(
                            "\nAllocated Resource: "
                    );

                    details.append(
                            rs.getString(
                                    "resource_name"
                            )
                    );

                    details.append(
                            "\nCapacity: "
                    );

                    details.append(
                            rs.getObject(
                                    "capacity"
                            ) == null
                                    ? "Not specified"
                                    : rs.getInt(
                                            "capacity"
                                    )
                    );

                    details.append(
                            "\nProjector: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "projector"
                                    )
                            )
                    );

                    details.append(
                            "\n\nFaculty Note\n"
                    );

                    String comment =
                            rs.getString(
                                    "faculty_comments"
                            );

                    if (
                            comment == null
                                    || comment
                                    .trim()
                                    .isEmpty()
                    ) {

                        details.append(
                                "No additional note provided."
                        );

                    } else {

                        details.append(
                                comment
                        );
                    }

                    details.append(
                            "\n\nStatus: "
                    );

                    details.append(
                            rs.getString(
                                    "status"
                            )
                    );

                    bookingDetailsArea.setText(
                            details.toString()
                    );

                    bookingDetailsArea.setCaretPosition(
                            0
                    );

                    approveBookingButton.setEnabled(
                            true
                    );

                    rejectBookingButton.setEnabled(
                            true
                    );
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load booking details:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void showBookingHistoryDetails() {
        int selectedRow = bookingHistoryTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow = bookingHistoryTable.convertRowIndexToModel(selectedRow);
        int bookingId = Integer.parseInt(bookingHistoryModel.getValueAt(modelRow, 0).toString());

        String sql = "SELECT "
                + "b.booking_id, "
                + "e.event_name, "
                + "e.event_type, "
                + "u.name AS faculty_name, "
                + "u.email, "
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
                + "r.capacity, "
                + "r.projector, "
                + "b.status, "
                + "e.faculty_comments "
                + "FROM bookings b "
                + "JOIN events e ON b.event_id = e.event_id "
                + "JOIN users u ON e.faculty_id = u.user_id "
                + "LEFT JOIN departments d ON e.department_id = d.department_id "
                + "LEFT JOIN programmes p ON e.programme_id = p.programme_id "
                + "LEFT JOIN sections s ON e.section_id = s.section_id "
                + "JOIN resources r ON b.resource_id = r.resource_id "
                + "WHERE b.booking_id = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)
        ) {
            pst.setInt(1, bookingId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                    StringBuilder details = new StringBuilder();

                    details.append("Request #").append(rs.getInt("booking_id"));

                    details.append("\n\nEvent\n");
                    details.append(rs.getString("event_name"));
                    details.append("\n");
                    details.append("Type: ").append(safeValue(rs.getString("event_type")));

                    details.append("\n\nFaculty\n");
                    details.append(rs.getString("faculty_name"));
                    details.append("\nEmail: ").append(safeValue(rs.getString("email")));

                    details.append("\n\nAcademic Group\n");
                    details.append("Department: ").append(safeValue(rs.getString("department_name")));
                    details.append("\nProgramme: ").append(safeValue(rs.getString("programme_name") == null ? "Department-wide" : rs.getString("programme_name")));
                    details.append("\nYear: ").append(safeValue(rs.getString("year") == null ? "All Years" : rs.getString("year")));
                    details.append("\nSection: ").append(safeValue(rs.getString("section_name") == null ? "All Sections" : rs.getString("section_name")));
                    details.append("\nStudents: ").append(rs.getInt("student_count"));

                    details.append("\n\nSchedule\n");
                    details.append("Date: ").append(dateFormat.format(rs.getDate("event_date")));
                    details.append("\nTime: ").append(timeFormat.format(rs.getTime("start_time")))
                            .append(" - ").append(timeFormat.format(rs.getTime("end_time")));

                    details.append("\n\nResource\n");
                    details.append("Requested Type: ").append(rs.getString("resource_type"));
                    details.append("\nAllocated Resource: ").append(rs.getString("resource_name"));
                    details.append("\nCapacity: ").append(rs.getObject("capacity") == null ? "Not specified" : rs.getInt("capacity"));
                    details.append("\nProjector: ").append(safeValue(rs.getString("projector")));

                    details.append("\n\nFaculty Note\n");
                    String comment = rs.getString("faculty_comments");
                    if (comment == null || comment.trim().isEmpty()) {
                        details.append("No additional note provided.");
                    } else {
                        details.append(comment);
                    }

                    details.append("\n\nStatus: ").append(rs.getString("status"));

                    bookingHistoryDetailsArea.setText(details.toString());
                    bookingHistoryDetailsArea.setCaretPosition(0);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load booking history details:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateBookingStatus(
            String status
    ) {

        int selectedRow =
                bookingTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a booking request first."
            );

            return;
        }

        int modelRow =
                bookingTable.convertRowIndexToModel(
                        selectedRow
                );

        int bookingId =
                Integer.parseInt(
                        bookingModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to "
                        + status.toLowerCase()
                        + " this booking?",
                        "Confirm Action",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                answer
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        String sql =
                "UPDATE bookings "
                + "SET status = ? "
                + "WHERE booking_id = ? "
                + "AND status = 'Pending'";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setString(
                    1,
                    status
            );

            pst.setInt(
                    2,
                    bookingId
            );

            int updated =
                    pst.executeUpdate();

            if (updated == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "This booking was already processed."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Booking "
                        + status
                        + " successfully!"
                );
            }

            loadBookingRequests();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void loadFacultyRequests() {

        facultyModel.setRowCount(0);

        String sql =
                "SELECT "
                + "u.user_id, "
                + "u.name, "
                + "u.register_number, "
                + "u.email, "
                + "u.username, "
                + "d.department_name, "
                + "u.status "
                + "FROM users u "
                + "LEFT JOIN departments d "
                + "ON u.department_id = d.department_id "
                + "WHERE u.role = 'Faculty' "
                + "AND u.status = 'Pending' "
                + "ORDER BY u.user_id DESC";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql);

                ResultSet rs =
                        pst.executeQuery()
        ) {

            while (rs.next()) {

                facultyModel.addRow(
                        new Object[] {
                                rs.getInt(
                                        "user_id"
                                ),
                                rs.getString(
                                        "name"
                                ),
                                safeValue(
                                        rs.getString(
                                                "register_number"
                                        )
                                ),
                                safeValue(
                                        rs.getString(
                                                "email"
                                        )
                                ),
                                rs.getString(
                                        "username"
                                ),
                                safeValue(
                                        rs.getString(
                                                "department_name"
                                        )
                                ),
                                rs.getString(
                                        "status"
                                )
                        }
                );
            }

            updateFacultyCount();

            approveFacultyButton.setEnabled(
                    false
            );

            rejectFacultyButton.setEnabled(
                    false
            );

            facultyDetailsArea.setText(
                    facultyModel.getRowCount() == 0
                            ? "No pending faculty account requests."
                            : "Select a faculty account to view complete details."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load faculty requests:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void showFacultyDetails() {

        int selectedRow =
                facultyTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow =
                facultyTable.convertRowIndexToModel(
                        selectedRow
                );

        int userId =
                Integer.parseInt(
                        facultyModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        String sql =
                "SELECT "
                + "u.name, "
                + "u.email, "
                + "u.register_number, "
                + "u.username, "
                + "u.role, "
                + "u.status, "
                + "d.department_name "
                + "FROM users u "
                + "LEFT JOIN departments d "
                + "ON u.department_id = d.department_id "
                + "WHERE u.user_id = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    userId
            );

            try (
                    ResultSet rs =
                            pst.executeQuery()
            ) {

                if (rs.next()) {

                    StringBuilder details =
                            new StringBuilder();

                    details.append(
                            "Faculty Account\n\n"
                    );

                    details.append(
                            "Name: "
                    );

                    details.append(
                            rs.getString(
                                    "name"
                            )
                    );

                    details.append(
                            "\nRegister Number: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "register_number"
                                    )
                            )
                    );

                    details.append(
                            "\nEmail: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "email"
                                    )
                            )
                    );

                    details.append(
                            "\nUsername: "
                    );

                    details.append(
                            rs.getString(
                                    "username"
                            )
                    );

                    details.append(
                            "\nDepartment: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "department_name"
                                    )
                            )
                    );

                    details.append(
                            "\nRole: "
                    );

                    details.append(
                            rs.getString(
                                    "role"
                            )
                    );

                    details.append(
                            "\nStatus: "
                    );

                    details.append(
                            rs.getString(
                                    "status"
                            )
                    );

                    facultyDetailsArea.setText(
                            details.toString()
                    );

                    facultyDetailsArea.setCaretPosition(
                            0
                    );

                    approveFacultyButton.setEnabled(
                            true
                    );

                    rejectFacultyButton.setEnabled(
                            true
                    );
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load faculty details:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void updateFacultyStatus(
            String status
    ) {

        int selectedRow =
                facultyTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a faculty account first."
            );

            return;
        }

        int modelRow =
                facultyTable.convertRowIndexToModel(
                        selectedRow
                );

        int userId =
                Integer.parseInt(
                        facultyModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to "
                        + status.toLowerCase()
                        + " this faculty account?",
                        "Confirm Action",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                answer
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        String sql =
                "UPDATE users "
                + "SET status = ? "
                + "WHERE user_id = ? "
                + "AND role = 'Faculty' "
                + "AND status = 'Pending'";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setString(
                    1,
                    status
            );

            pst.setInt(
                    2,
                    userId
            );

            int updated =
                    pst.executeUpdate();

            if (updated == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "This faculty account was already processed."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Faculty account "
                        + status
                        + " successfully!"
                );
            }

            loadFacultyRequests();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void loadResources() {

        resourceModel.setRowCount(0);

        String sql =
                "SELECT "
                + "r.resource_id, "
                + "r.resource_name, "
                + "r.resource_type, "
                + "d.department_name, "
                + "r.capacity, "
                + "r.projector, "
                + "r.scope, "
                + "r.status "
                + "FROM resources r "
                + "LEFT JOIN departments d "
                + "ON r.department_id = d.department_id "
                + "ORDER BY r.resource_type, "
                + "r.resource_name";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql);

                ResultSet rs =
                        pst.executeQuery()
        ) {

            while (rs.next()) {

                Object capacity =
                        rs.getObject(
                                "capacity"
                        );

                resourceModel.addRow(
                        new Object[] {
                                rs.getInt(
                                        "resource_id"
                                ),
                                rs.getString(
                                        "resource_name"
                                ),
                                rs.getString(
                                        "resource_type"
                                ),
                                safeValue(
                                        rs.getString(
                                                "department_name"
                                        )
                                ),
                                capacity == null
                                        ? "N/A"
                                        : capacity,
                                safeValue(
                                        rs.getString(
                                                "projector"
                                        )
                                ),
                                safeValue(
                                        rs.getString(
                                                "scope"
                                        )
                                ),
                                rs.getString(
                                        "status"
                                )
                        }
                );
            }

            updateResourceCount();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load resources:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void updateBookingCount() {

        pendingBookingLabel.setText(
                "Pending Bookings: "
                + bookingModel.getRowCount()
        );
    }

    private void updateFacultyCount() {

        pendingFacultyLabel.setText(
                "Pending Faculty: "
                + facultyModel.getRowCount()
        );
    }

    private void updateResourceCount() {

        resourceLabel.setText(
                "Resources: "
                + resourceModel.getRowCount()
        );
    }

    private String safeValue(
            String value
    ) {

        if (
                value == null
                        || value.trim().isEmpty()
        ) {

            return "Not available";
        }

        return value;
    }

    @Override
    public void actionPerformed(
            ActionEvent e
    ) {

        if (
                e.getSource()
                        == approveBookingButton
        ) {

            updateBookingStatus(
                    "Approved"
            );

        } else if (
                e.getSource()
                        == rejectBookingButton
        ) {

            updateBookingStatus(
                    "Rejected"
            );

        } else if (
                e.getSource()
                        == refreshBookingButton
        ) {

            loadBookingRequests();

        } else if (
                e.getSource()
                        == approveFacultyButton
        ) {

            updateFacultyStatus(
                    "Approved"
            );

        } else if (
                e.getSource()
                        == rejectFacultyButton
        ) {

            updateFacultyStatus(
                    "Rejected"
            );

        } else if (
                e.getSource()
                        == refreshFacultyButton
        ) {

            loadFacultyRequests();

        } else if (
                e.getSource()
                        == refreshResourceButton
        ) {

            loadResources();
        } else if (
                e.getSource()
                        == refreshHistoryButton
        ) {
            loadBookingHistory();
        } else if (
                e.getSource()
                        == logoutButton
        ) {

            dispose();

            new LoginFrame();
        }
    }

    public static void main(
            String[] args
    ) {

        new AdminFrame();
    }
}