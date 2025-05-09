import javax.swing.*;
import java.awt.*;
import controller.CourseManager;

public class MainGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private DashboardPanel dashboardPanel;
    private CourseManager courseManager;

    public MainGUI() {
        setTitle("NYU Course Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Initialize components
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        courseManager = new CourseManager();
        
        // Create panels
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        dashboardPanel = new DashboardPanel(this);

        // Add panels to main panel
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");
        mainPanel.add(dashboardPanel, "DASHBOARD");

        // Add main panel to frame
        add(mainPanel);

        // Show login panel first
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showPanel(String panelName) {
        if (panelName.equals("DASHBOARD")) {
            dashboardPanel.setCourseManager(courseManager);
        }
        cardLayout.show(mainPanel, panelName);
    }

    public CourseManager getCourseManager() {
        return courseManager;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
} 