package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
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

public class StudentRequestsFrame extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JLabel countLabel;

    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JTextArea detailsArea;
    private JPanel tablePanel;

    private JButton approveButton;
    private JButton rejectButton;
    private JButton refreshButton;
    private JButton backButton;

    public StudentRequestsFrame() {

        setTitle("Student Account Requests - Karunya Campus Allocation & Booking System");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 250));

        createHeader();
        createStudentTable();
        createDetailsPanel();
        createButtons();

        loadStudentRequests();
        setVisible(true);
    }

    private void createHeader() {

        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));

        titleLabel = new JLabel(
                "Student Account Requests",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 21));
        titleLabel.setForeground(new Color(35, 55, 80));
        JPanel titlePanel = new JPanel(new BorderLayout(10, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(createLogoLabel(60), BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        countLabel = new JLabel(
                "Pending Student Accounts: 0",
                SwingConstants.CENTER
        );
        countLabel.setFont(new Font("Arial", Font.BOLD, 14));

        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(countLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
    }

    private JLabel createLogoLabel(int size) {
        java.net.URL logoUrl = getClass().getResource("/images/karunya_logo.png");
        if (logoUrl == null) {
            return new JLabel();
        }
        Image image = new ImageIcon(logoUrl).getImage().getScaledInstance(
                size, size, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(image));
    }

    private void createStudentTable() {

        tableModel = new DefaultTableModel(
                new String[] {
                        "ID",
                        "Name",
                        "Register Number",
                        "Email",
                        "Department",
                        "Programme",
                        "Year",
                        "Section",
                        "Status"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(28);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentTable.setShowGrid(true);
        studentTable.setGridColor(new Color(220, 225, 230));
        studentTable.setSelectionBackground(new Color(210, 225, 240));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setAutoCreateRowSorter(true);
        studentTable.setToolTipText(
                "Select a student to view complete account details."
        );

        studentTable.getSelectionModel().addListSelectionListener(
                e -> showStudentDetails()
        );

        studentTable.getColumnModel().getColumn(0).setMinWidth(0);
        studentTable.getColumnModel().getColumn(0).setMaxWidth(0);
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        studentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(110);
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(220);
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(60);
        studentTable.getColumnModel().getColumn(7).setPreferredWidth(70);
        studentTable.getColumnModel().getColumn(8).setPreferredWidth(90);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(
                BorderFactory.createTitledBorder("Pending Student Requests")
        );
        tablePanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        studentTable.setFillsViewportHeight(true);
    }

    private void createDetailsPanel() {

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(
                BorderFactory.createTitledBorder("Selected Student Details")
        );

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsArea.setMargin(new Insets(10, 10, 10, 10));
        detailsArea.setText(
                "Select a student from the table to view complete details."
        );

        detailsPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                tablePanel,
                detailsPanel
        );
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.60);

        add(splitPane, BorderLayout.CENTER);
    }

    private void createButtons() {

        approveButton = new JButton("Approve Account");
        rejectButton = new JButton("Reject Account");
        refreshButton = new JButton("Refresh");
        backButton = new JButton("Back");

        approveButton.addActionListener(this);
        rejectButton.addActionListener(this);
        refreshButton.addActionListener(this);
        backButton.addActionListener(this);

        approveButton.setEnabled(false);
        rejectButton.setEnabled(false);
        approveButton.setPreferredSize(new java.awt.Dimension(145, 34));
        rejectButton.setPreferredSize(new java.awt.Dimension(135, 34));
        refreshButton.setPreferredSize(new java.awt.Dimension(100, 34));
        backButton.setPreferredSize(new java.awt.Dimension(90, 34));
        approveButton.setBackground(new Color(45, 91, 140));
        approveButton.setForeground(Color.WHITE);
        rejectButton.setBackground(new Color(180, 70, 70));
        rejectButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 8)
        );

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void loadStudentRequests() {

        tableModel.setRowCount(0);
        approveButton.setEnabled(false);
        rejectButton.setEnabled(false);

        int facultyId = LoginFrame.currentUserId;

        if (facultyId == 0) {
            countLabel.setText("Pending Student Accounts: 0");
            detailsArea.setText("No faculty user is logged in.");
            return;
        }

        String sql =
                "SELECT "
                + "u.user_id, u.name, u.register_number, u.email, "
                + "d.department_name, p.programme_name, "
                + "s.year, s.section_name, u.status "
                + "FROM users u "
                + "LEFT JOIN departments d ON u.department_id = d.department_id "
                + "LEFT JOIN programmes p ON u.programme_id = p.programme_id "
                + "LEFT JOIN sections s ON u.section_id = s.section_id "
                + "WHERE u.role = 'Student' "
                + "AND u.status = 'Pending' "
                + "AND u.faculty_id = ? "
                + "ORDER BY u.user_id DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setInt(1, facultyId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    tableModel.addRow(new Object[] {
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            safeValue(rs.getString("register_number")),
                            safeValue(rs.getString("email")),
                            safeValue(rs.getString("department_name")),
                            safeValue(rs.getString("programme_name")),
                            safeValue(rs.getString("year")),
                            safeValue(rs.getString("section_name")),
                            rs.getString("status")
                    });
                }
            }

            int count = tableModel.getRowCount();
            countLabel.setText("Pending Student Accounts: " + count);

            if (count == 0) {
                detailsArea.setText("No pending student account requests.");
            } else {
                detailsArea.setText(
                        "Select a student from the table to view complete details."
                );
                studentTable.setRowSelectionInterval(0, 0);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load student requests:\n" + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    private void showStudentDetails() {

        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            approveButton.setEnabled(false);
            rejectButton.setEnabled(false);
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(selectedRow);
        int studentId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        String sql =
                "SELECT "
                + "u.name, u.register_number, u.email, u.username, "
                + "u.role, u.status, d.department_name, "
                + "p.programme_name, s.year, s.section_name "
                + "FROM users u "
                + "LEFT JOIN departments d ON u.department_id = d.department_id "
                + "LEFT JOIN programmes p ON u.programme_id = p.programme_id "
                + "LEFT JOIN sections s ON u.section_id = s.section_id "
                + "WHERE u.user_id = ? "
                + "AND u.faculty_id = ? "
                + "AND u.role = 'Student'";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setInt(1, studentId);
            pst.setInt(2, LoginFrame.currentUserId);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {
                    StringBuilder details = new StringBuilder();

                    details.append("Student Account\n\n");
                    details.append("Name: ").append(rs.getString("name"));
                    details.append("\nRegister Number: ")
                            .append(safeValue(rs.getString("register_number")));
                    details.append("\nEmail: ")
                            .append(safeValue(rs.getString("email")));
                    details.append("\nUsername: ")
                            .append(safeValue(rs.getString("username")));

                    details.append("\n\nAcademic Group\n");
                    details.append("Department: ")
                            .append(safeValue(rs.getString("department_name")));
                    details.append("\nProgramme: ")
                            .append(safeValue(rs.getString("programme_name")));
                    details.append("\nYear: ")
                            .append(safeValue(rs.getString("year")));
                    details.append("\nSection: ")
                            .append(safeValue(rs.getString("section_name")));

                    details.append("\n\nRole: ")
                            .append(rs.getString("role"));
                    details.append("\nStatus: ")
                            .append(rs.getString("status"));

                    detailsArea.setText(details.toString());
                    detailsArea.setCaretPosition(0);

                    approveButton.setEnabled(true);
                    rejectButton.setEnabled(true);
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

    private void updateStudentStatus(String status) {

        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student first."
            );
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(selectedRow);
        int studentId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );
        String studentName = tableModel.getValueAt(modelRow, 1).toString();

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to "
                        + status.toLowerCase()
                        + " the account of "
                        + studentName
                        + "?",
                "Confirm Action",
                JOptionPane.YES_NO_OPTION
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "UPDATE users "
                + "SET status = ? "
                + "WHERE user_id = ? "
                + "AND faculty_id = ? "
                + "AND role = 'Student' "
                + "AND status = 'Pending'";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setString(1, status);
            pst.setInt(2, studentId);
            pst.setInt(3, LoginFrame.currentUserId);

            int updated = pst.executeUpdate();

            if (updated == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "This student request was already processed."
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Student account "
                                + status.toLowerCase()
                                + " successfully!"
                );
            }

            loadStudentRequests();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n" + e.getMessage()
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

        if (e.getSource() == approveButton) {
            updateStudentStatus("Approved");
        } else if (e.getSource() == rejectButton) {
            updateStudentStatus("Rejected");
        } else if (e.getSource() == refreshButton) {
            loadStudentRequests();
        } else if (e.getSource() == backButton) {
            dispose();
            new FacultyFrame();
        }
    }

    public static void main(String[] args) {
        new StudentRequestsFrame();
    }
}
