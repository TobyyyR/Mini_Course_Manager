import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import controller.CourseManager;
import model.Course;

public class DashboardPanel extends JPanel {
    private MainGUI mainGUI;
    private CourseManager courseManager;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JLabel gpaLabel;
    private JLabel creditsLabel;

    public DashboardPanel(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.courseManager = new CourseManager();
        setupUI();
    }

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
        refreshCourseTable();
        updateStats();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Top Panel with GPA and Credits
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gpaLabel = new JLabel("GPA: 0.00");
        creditsLabel = new JLabel("Total Credits: 0");
        topPanel.add(gpaLabel);
        topPanel.add(creditsLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel with Course Table
        String[] columnNames = {"Course Name", "Course ID", "Professor", "Credits", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel with Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Course");
        JButton modifyButton = new JButton("Modify Course");
        JButton removeButton = new JButton("Remove Course");
        JButton reportButton = new JButton("Generate Report");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> handleAddCourse());
        modifyButton.addActionListener(e -> handleModifyCourse());
        removeButton.addActionListener(e -> handleRemoveCourse());
        reportButton.addActionListener(e -> handleGenerateReport());
        logoutButton.addActionListener(e -> {
            mainGUI.showPanel("LOGIN");
        });
    }

    private void refreshCourseTable() {
        try {
            tableModel.setRowCount(0);
            List<Course> courses = courseManager.getAllCourses();
            for (Course course : courses) {
                Object[] row = {
                    course.getName(),
                    course.getCourseID(),
                    course.getProfessor(),
                    course.getCredits(),
                    course.getGrade()
                };
                tableModel.addRow(row);
            }
        } catch (IllegalStateException e) {
            // No user logged in, table will be empty
        }
    }

    private void updateStats() {
        try {
            gpaLabel.setText(String.format("GPA: %.2f", courseManager.getGPA()));
            creditsLabel.setText(String.format("Total Credits: %d", courseManager.getTotalCredits()));
        } catch (IllegalStateException e) {
            gpaLabel.setText("GPA: 0.00");
            creditsLabel.setText("Total Credits: 0");
        }
    }

    private void handleAddCourse() {
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField profField = new JTextField();
        JTextField creditsField = new JTextField();
        JTextField gradeField = new JTextField();

        Object[] message = {
            "Course Name:", nameField,
            "Course ID:", idField,
            "Professor:", profField,
            "Credits:", creditsField,
            "Grade (e.g., A, B+, F):", gradeField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int credits = Integer.parseInt(creditsField.getText());
                Course course = new Course(
                    nameField.getText(),
                    idField.getText(),
                    profField.getText(),
                    credits,
                    gradeField.getText()
                );
                courseManager.addCourse(course);
                refreshCourseTable();
                updateStats();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Credits must be a number",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this,
                    "Invalid grade format",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleModifyCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a course to modify",
                "Selection Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 1);
        Course course = courseManager.findCourseByID(courseId);
        if (course == null) return;

        JTextField nameField = new JTextField(course.getName());
        JTextField idField = new JTextField(course.getCourseID());
        JTextField profField = new JTextField(course.getProfessor());
        JTextField creditsField = new JTextField(String.valueOf(course.getCredits()));
        JTextField gradeField = new JTextField(course.getGrade());

        Object[] message = {
            "Course Name:", nameField,
            "Course ID:", idField,
            "Professor:", profField,
            "Credits:", creditsField,
            "Grade (e.g., A, B+, F):", gradeField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Modify Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int credits = Integer.parseInt(creditsField.getText());
                Course updatedCourse = new Course(
                    nameField.getText(),
                    idField.getText(),
                    profField.getText(),
                    credits,
                    gradeField.getText()
                );
                courseManager.updateCourse(courseId, updatedCourse);
                refreshCourseTable();
                updateStats();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Credits must be a number",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this,
                    "Invalid grade format",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemoveCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a course to remove",
                "Selection Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 1);
        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to remove this course?",
            "Confirm Removal",
            JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            courseManager.removeCourse(courseId);
            refreshCourseTable();
            updateStats();
        }
    }

    private void handleGenerateReport() {
        try {
            courseManager.generateReport(courseManager.getCurrentUser());
            JOptionPane.showMessageDialog(this,
                "Report generated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Failed to generate report: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 