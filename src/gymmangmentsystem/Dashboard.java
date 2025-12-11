
package gymmangmentsystem;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Gym Management Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ====== LEFT SIDEBAR ======
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(25, 25, 35));
        sidebar.setPreferredSize(new Dimension(230, getHeight()));
        sidebar.setLayout(new BorderLayout());

        // --- Sidebar Top: Welcome Section ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(25, 25, 35));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblIcon = new JLabel("👋", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblWelcome = new JLabel("Welcome");
        lblWelcome.setForeground(new Color(180, 180, 200));
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAdmin = new JLabel("Admin");
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(lblIcon);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(lblWelcome);
        topPanel.add(lblAdmin);

        // --- Sidebar Menu Buttons ---
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(25, 25, 35));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        addMenuButton(menuPanel, "Dashboard", "/Images/dashboard.png");
        addMenuButton(menuPanel, "Members", "/Images/group.png");
        addMenuButton(menuPanel, "Payments", "/Images/atm-card.png");
        addMenuButton(menuPanel, "Subscriptions", "/Images/subscription-active.png");
        addMenuButton(menuPanel, "Reports", "/Images/analysis.png");

        // --- Logout Button at Bottom ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(25, 25, 35));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(20, 10, 30, 10));

        JButton btnLogout = createSidebarButton("Logout", "/Images/logout.png");
        bottomPanel.add(btnLogout);

        // --- Add logout action ---
        btnLogout.addActionListener(e -> logout());

        sidebar.add(topPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        // ====== MAIN CONTENT ======
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(20, 20, 30));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- Title ---
        JLabel lblTitle = new JLabel("Dashboard Overview");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // --- Info Panels (2x2 Grid) ---
      // --- Info Panels (2x2 Grid) ---
JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 25, 25));
cardsPanel.setOpaque(false);

// ===== FETCH DATA FROM DATABASE =====
int activeMembers = getActiveMembersCount();
double monthlyRevenue = getMonthlyRevenue();
int expiringSoon = getExpiringSoonCount();
int newSignups = getNewSignupsThisMonth();

// ===== LAST MONTH COMPARISON =====
int lastMonthActive = getActiveMembersLastMonth();
double lastMonthRevenue = getLastMonthRevenue();

String activeChange = getPercentageChange(activeMembers, lastMonthActive);
String revenueChange = getPercentageChange(monthlyRevenue, lastMonthRevenue);

// ===== CREATE DASHBOARD CARDS =====
cardsPanel.add(createInfoPanel("Active Members", String.valueOf(activeMembers), activeChange, "/Images/Active.png", new Color(40, 40, 60)));
cardsPanel.add(createInfoPanel("Monthly Revenue", "$" + String.format("%.2f", monthlyRevenue), revenueChange, "/Images/profit.png", new Color(40, 40, 60)));
cardsPanel.add(createInfoPanel("Expiring Soon", String.valueOf(expiringSoon), "Next 7 days", "/Images/expired.png", new Color(40, 40, 60)));
cardsPanel.add(createInfoPanel("New This Month", String.valueOf(newSignups), "New Signups", "/Images/new.png", new Color(40, 40, 60)));

mainPanel.add(cardsPanel, BorderLayout.CENTER);


        // ====== ADD TO FRAME ======
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    // ========================== HELPER METHODS ===============================

    private void addMenuButton(JPanel menuPanel, String text, String iconPath) {
        JButton btn = createSidebarButton(text, iconPath);

        // ===== ACTION FOR EACH BUTTON =====
        btn.addActionListener(e -> {
            switch (text) {
                case "Dashboard":
                    openPage(this, new Dashboard());
                    break;
                case "Members":
                    openPage(this, new Members());
                    break;
                case "Payments":
                    openPage(this, new Payment());
                    break;
                case "Subscriptions":
                    openPage(this, new Subscription());
                    break;
                case "Reports":
                    openPage(this, new Reports());
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Page not yet implemented!");
                    break;
            }
        });

        menuPanel.add(btn);
        menuPanel.add(Box.createVerticalStrut(10));
    }

    private JButton createSidebarButton(String text, String iconPath) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }

        JButton btn = new JButton("  " + text, icon);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(35, 35, 50));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(15);

        // Hover effects
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(50, 50, 70));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(35, 35, 50));
            }
        });

        return btn;
    }

    private JPanel createInfoPanel(String title, String value, String subtitle, String iconPath, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 80), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // --- Top section: icon + title ---
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setOpaque(false);

        JLabel iconLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            System.out.println("Card icon not found: " + iconPath);
        }

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(180, 180, 200));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        top.add(iconLabel);
        top.add(lblTitle);

        // --- Center section: value and subtitle ---
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setBorder(new EmptyBorder(10, 0, 5, 0));
JLabel lblSubtitle = new JLabel(subtitle);
lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

// auto color if it's a percentage
if (subtitle.startsWith("+")) {
    lblSubtitle.setForeground(new Color(0, 200, 120)); // green for increase
} else if (subtitle.startsWith("-")) {
    lblSubtitle.setForeground(new Color(220, 50, 50)); // red for decrease
} else {
    lblSubtitle.setForeground(new Color(150, 150, 150)); // neutral gray
}


        center.add(lblValue);
        center.add(lblSubtitle);

        panel.add(top, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    // ======= METHOD TO NAVIGATE TO DIFFERENT PAGES =======
    private void openPage(JFrame currentFrame, JFrame nextFrame) {
        currentFrame.dispose();
        nextFrame.setVisible(true);
    }

    // ======= LOGOUT METHOD =======
    private void logout() {
        dispose(); // Close dashboard
        new Home().setVisible(true); // Open Home page instantly
    }
 

// ======== DATABASE METHODS FOR DASHBOARD (FINAL FIXED) ========

// Count members with subscriptions still active
private int getActiveMembersCount() {
    int count = 0;
    try (java.sql.Connection con = project.ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT COUNT(DISTINCT phone_number) FROM payments WHERE end_date >= CURDATE()")) {
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) count = rs.getInt(1);
    } catch (Exception e) {
        System.out.println("Error loading active members: " + e.getMessage());
    }
    return count;
}

// Calculate total revenue for this month
private double getMonthlyRevenue() {
    double revenue = 0;
    try (java.sql.Connection con = project.ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT SUM(amount) FROM payments " +
             "WHERE MONTH(start_date)=MONTH(CURDATE()) AND YEAR(start_date)=YEAR(CURDATE())")) {
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) revenue = rs.getDouble(1);
    } catch (Exception e) {
        System.out.println("Error loading monthly revenue: " + e.getMessage());
    }
    return revenue;
}

// Count members whose subscription ends within the next 7 days
private int getExpiringSoonCount() {
    int count = 0;
    try (java.sql.Connection con = project.ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT COUNT(DISTINCT phone_number) FROM payments " +
             "WHERE DATEDIFF(end_date, CURDATE()) BETWEEN 0 AND 7")) {
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) count = rs.getInt(1);
    } catch (Exception e) {
        System.out.println("Error loading expiring soon members: " + e.getMessage());
    }
    return count;
}

// Count members who started a new subscription this month
private int getNewSignupsThisMonth() {
    int count = 0;
    try (java.sql.Connection con = project.ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT COUNT(DISTINCT phone_number) FROM payments " +
             "WHERE MONTH(start_date)=MONTH(CURDATE()) AND YEAR(start_date)=YEAR(CURDATE())")) {
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) count = rs.getInt(1);
    } catch (Exception e) {
        System.out.println("Error loading new signups: " + e.getMessage());
    }
    return count;
}
// ======== PERCENTAGE CHANGE HELPERS ========

// Get count of active members for the previous month
private int getActiveMembersLastMonth() {
    int count = 0;
    try (java.sql.Connection con = project.ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT COUNT(DISTINCT phone_number) FROM payments " +
             "WHERE MONTH(start_date)=MONTH(CURDATE() - INTERVAL 1 MONTH) " +
             "AND YEAR(start_date)=YEAR(CURDATE() - INTERVAL 1 MONTH)")) {
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) count = rs.getInt(1);
    } catch (Exception e) {
        System.out.println("Error loading active members (last month): " + e.getMessage());
    }
    return count;
}

// Get revenue for last month
private double getLastMonthRevenue() {
    double revenue = 0;
    try (java.sql.Connection con = project.ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT SUM(amount) FROM payments " +
             "WHERE MONTH(start_date)=MONTH(CURDATE() - INTERVAL 1 MONTH) " +
             "AND YEAR(start_date)=YEAR(CURDATE() - INTERVAL 1 MONTH)")) {
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) revenue = rs.getDouble(1);
    } catch (Exception e) {
        System.out.println("Error loading last month's revenue: " + e.getMessage());
    }
    return revenue;
}

// Generic helper to compute percentage difference
private String getPercentageChange(double current, double previous) {
    if (previous == 0) return "+0%";
    double change = ((current - previous) / previous) * 100;
    String sign = (change >= 0) ? "+" : "";
    return sign + String.format("%.1f", change) + "%";
}




    // ============================== MAIN =====================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}




