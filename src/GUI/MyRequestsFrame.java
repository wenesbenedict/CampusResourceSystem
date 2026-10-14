package GUI;

import java.awt.BorderLayout;
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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import Database.DBConnection;

public class MyRequestsFrame extends JFrame
        implements ActionListener {

    JLabel titleLabel;
    JLabel detailsTitle;

    JTable requestsTable;
    DefaultTableModel tableModel;

    JTextArea detailsArea;

    JButton refreshButton;
    JButton backButton;

    public MyRequestsFrame() {

        setTitle(
                "My Resource Requests - Karunya Campus Allocation & Booking System"
        );

        setSize(1000, 700);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout(
                        15,
                        15
                )
        );

        JPanel topPanel =
                new JPanel(
                        new BorderLayout()
                );

        titleLabel =
                new JLabel(
                        "My Resource Requests",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        topPanel.add(
                titleLabel,
                BorderLayout.CENTER
        );

        refreshButton =
                new JButton("Refresh");

        refreshButton.addActionListener(this);

        JPanel refreshPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        refreshPanel.add(
                refreshButton
        );

        topPanel.add(
                refreshPanel,
                BorderLayout.EAST
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        tableModel =
                new DefaultTableModel(
                        new String[] {
                                "Request",
                                "Event",
                                "Type",
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

        requestsTable =
                new JTable(
                        tableModel
                );

        requestsTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        requestsTable.setRowHeight(
                28
        );

        requestsTable.setAutoCreateRowSorter(
                true
        );

        requestsTable.setToolTipText(
                "Select a request to view its complete details."
        );

        requestsTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> showSelectedRequest()
                );

        JScrollPane tableScrollPane =
                new JScrollPane(
                        requestsTable
                );

        JPanel tablePanel =
                new JPanel(
                        new BorderLayout()
                );

        tablePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Booking Requests"
                )
        );

        tablePanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        detailsTitle =
                new JLabel(
                        "Selected Request Details"
                );

        detailsTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        detailsArea =
                new JTextArea();

        detailsArea.setEditable(
                false
        );

        detailsArea.setLineWrap(
                true
        );

        detailsArea.setWrapStyleWord(
                true
        );

        detailsArea.setMargin(
                new Insets(
                        10,
                        10,
                        10,
                        10
                )
        );

        detailsArea.setText(
                "Select a request from the table to view its details."
        );

        JScrollPane detailsScrollPane =
                new JScrollPane(
                        detailsArea
                );

        detailsScrollPane.setPreferredSize(
                new Dimension(
                        300,
                        200
                )
        );

        JPanel detailsPanel =
                new JPanel(
                        new BorderLayout(
                                5,
                                5
                )
        );

        detailsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Request Details"
                )
        );

        detailsPanel.add(
                detailsTitle,
                BorderLayout.NORTH
        );

        detailsPanel.add(
                detailsScrollPane,
                BorderLayout.CENTER
        );

        JPanel centerPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                10,
                                10
                        )
                );

        centerPanel.add(
                tablePanel
        );

        centerPanel.add(
                detailsPanel
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        backButton =
                new JButton("Back");

        backButton.addActionListener(this);

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        bottomPanel.add(
                backButton
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        loadRequests();

        setVisible(true);
    }

    public void loadRequests() {

        tableModel.setRowCount(0);

        int facultyId =
                LoginFrame.currentUserId;

        if (facultyId == 0) {

            detailsArea.setText(
                    "No faculty user is logged in."
            );

            return;
        }

        String sql =
                "SELECT "
                + "b.booking_id, "
                + "e.event_name, "
                + "e.event_type, "
                + "d.department_name, "
                + "p.programme_name, "
                + "s.year, "
                + "s.section_name, "
                + "e.student_count, "
                + "e.event_date, "
                + "e.start_time, "
                + "e.end_time, "
                + "r.resource_name, "
                + "b.status, "
                + "e.faculty_comments "
                + "FROM bookings b "
                + "JOIN events e "
                + "ON b.event_id = e.event_id "
                + "JOIN resources r "
                + "ON b.resource_id = r.resource_id "
                + "LEFT JOIN departments d "
                + "ON e.department_id = d.department_id "
                + "LEFT JOIN programmes p "
                + "ON e.programme_id = p.programme_id "
                + "LEFT JOIN sections s "
                + "ON e.section_id = s.section_id "
                + "WHERE e.faculty_id = ? "
                + "ORDER BY b.booking_id DESC";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    facultyId
            );

            try (
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

                    int bookingId =
                            rs.getInt(
                                    "booking_id"
                            );

                    String eventName =
                            rs.getString(
                                    "event_name"
                            );

                    String eventType =
                            rs.getString(
                                    "event_type"
                            );

                    String programme =
                            rs.getString(
                                    "programme_name"
                            );

                    String year =
                            rs.getString(
                                    "year"
                            );

                    String section =
                            rs.getString(
                                    "section_name"
                            );

                    String date =
                            formatDate(
                                    rs.getDate(
                                            "event_date"
                                    ),
                                    dateFormat
                            );

                    String time =
                            formatTimeRange(
                                    rs.getTime(
                                            "start_time"
                                    ),
                                    rs.getTime(
                                            "end_time"
                                    ),
                                    timeFormat
                            );

                    String resource =
                            rs.getString(
                                    "resource_name"
                            );

                    String status =
                            rs.getString(
                                    "status"
                            );

                    String department =
                            rs.getString(
                                    "department_name"
                            );

                    int studentCount =
                            rs.getInt(
                                    "student_count"
                            );

                    tableModel.addRow(
                            new Object[] {
                                    bookingId,
                                    eventName,
                                    eventType,
                                    programme,
                                    year,
                                    section,
                                    date,
                                    time,
                                    resource,
                                    status
                            }
                    );
                }
            }

            if (
                    tableModel.getRowCount()
                            == 0
            ) {

                detailsArea.setText(
                        "You have no resource requests."
                );

            } else {

                detailsArea.setText(
                        "Select a request from the table to view its details."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public void showSelectedRequest() {

        int selectedRow =
                requestsTable.getSelectedRow();

        if (selectedRow == -1) {

            return;
        }

        int modelRow =
                requestsTable.convertRowIndexToModel(
                        selectedRow
                );

        int bookingId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        String sql =
                "SELECT "
                + "b.booking_id, "
                + "e.event_name, "
                + "e.event_type, "
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
                + "b.status, "
                + "e.faculty_comments "
                + "FROM bookings b "
                + "JOIN events e "
                + "ON b.event_id = e.event_id "
                + "JOIN resources r "
                + "ON b.resource_id = r.resource_id "
                + "LEFT JOIN departments d "
                + "ON e.department_id = d.department_id "
                + "LEFT JOIN programmes p "
                + "ON e.programme_id = p.programme_id "
                + "LEFT JOIN sections s "
                + "ON e.section_id = s.section_id "
                + "WHERE b.booking_id = ? "
                + "AND e.faculty_id = ?";

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

            pst.setInt(
                    2,
                    LoginFrame.currentUserId
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

                    details.append(
                            "\n\n"
                    );

                    details.append(
                            "Event: "
                    );

                    details.append(
                            rs.getString(
                                    "event_name"
                            )
                    );

                    details.append(
                            "\n"
                    );

                    details.append(
                            "Event Type: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "event_type"
                                    )
                            )
                    );

                    details.append(
                            "\n\n"
                    );

                    details.append(
                            "Academic Group\n"
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
                            "\n\n"
                    );

                    details.append(
                            "Schedule\n"
                    );

                    details.append(
                            "Date: "
                    );

                    details.append(
                            formatDate(
                                    rs.getDate(
                                            "event_date"
                                    ),
                                    dateFormat
                            )
                    );

                    details.append(
                            "\nTime: "
                    );

                    details.append(
                            formatTimeRange(
                                    rs.getTime(
                                            "start_time"
                                    ),
                                    rs.getTime(
                                            "end_time"
                                    ),
                                    timeFormat
                            )
                    );

                    details.append(
                            "\n\n"
                    );

                    details.append(
                            "Resource\n"
                    );

                    details.append(
                            "Type: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "resource_type"
                                    )
                            )
                    );

                    details.append(
                            "\nAllocated Resource: "
                    );

                    details.append(
                            safeValue(
                                    rs.getString(
                                            "resource_name"
                                    )
                            )
                    );

                    details.append(
                            "\n\n"
                    );

                    details.append(
                            "Status: "
                    );

                    details.append(
                            rs.getString(
                                    "status"
                            )
                    );

                    details.append(
                            "\n\n"
                    );

                    details.append(
                            "Faculty Note\n"
                    );

                    String comments =
                            rs.getString(
                                    "faculty_comments"
                            );

                    if (
                            comments == null
                                    || comments
                                    .trim()
                                    .isEmpty()
                    ) {

                        details.append(
                                "No additional note provided."
                        );

                    } else {

                        details.append(
                                comments
                        );
                    }

                    detailsArea.setText(
                            details.toString()
                    );

                    detailsArea.setCaretPosition(
                            0
                    );
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load request details: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
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

    private String formatDate(
            java.sql.Date date,
            SimpleDateFormat format
    ) {

        if (date == null) {

            return "Not available";
        }

        return format.format(date);
    }

    private String formatTimeRange(
            java.sql.Time startTime,
            java.sql.Time endTime,
            SimpleDateFormat format
    ) {

        if (
                startTime == null
                        || endTime == null
        ) {

            return "Not available";
        }

        return format.format(startTime)
                + " - "
                + format.format(endTime);
    }

    @Override
    public void actionPerformed(
            ActionEvent e
    ) {

        if (
                e.getSource()
                        == refreshButton
        ) {

            loadRequests();

        } else if (
                e.getSource()
                        == backButton
        ) {

            dispose();

            new FacultyFrame();
        }
    }

    public static void main(
            String[] args
    ) {

        new MyRequestsFrame();
    }
}