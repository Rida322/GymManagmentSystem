package gymmangmentsystem;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import project.ConnectionProvider;

public class Subscription extends JFrame {


private JTable table;
private DefaultTableModel model;

public Subscription() {
    setTitle("Subscription");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1100, 750);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // ===== SIDEBAR =====
    JPanel sidebar = new JPanel();
    sidebar.setBackground(new Color(25, 25, 35));
    sidebar.setPreferredSize(new Dimension(230, getHeight()));
    sidebar.setLayout(new BorderLayout());

    JPanel topPanel = new JPanel();
    topPanel.setBackground(new Color(25, 25, 35));
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    topPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

    JLabel lblIcon = new JLabel("📅", SwingConstants.CENTER);
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

    JPanel menuPanel = new JPanel();
    menuPanel.setBackground(new Color(25, 25, 35));
    menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
    menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    addMenuButton(menuPanel, "Dashboard", "/Images/dashboard.png");
    addMenuButton(menuPanel, "Members", "/Images/group.png");
    addMenuButton(menuPanel, "Payments", "/Images/atm-card.png");
    addMenuButton(menuPanel, "Subscriptions", "/Images/subscription-active.png");
    addMenuButton(menuPanel, "Reports", "/Images/analysis.png");

    JPanel bottomPanel = new JPanel();
    bottomPanel.setBackground(new Color(25, 25, 35));
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    bottomPanel.setBorder(new EmptyBorder(20, 10, 30, 10));
    JButton btnLogout = createSidebarButton("Logout", "/Images/logout.png");
    bottomPanel.add(btnLogout);
    btnLogout.addActionListener(e -> logout());

    sidebar.add(topPanel, BorderLayout.NORTH);
    sidebar.add(menuPanel, BorderLayout.CENTER);
    sidebar.add(bottomPanel, BorderLayout.SOUTH);

 // ===== MAIN PANEL =====
JPanel mainPanel = new JPanel();
mainPanel.setBackground(new Color(20, 20, 30));
mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
mainPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

// ===== TITLE =====
JLabel lblTitle = new JLabel("Subscriptions", SwingConstants.CENTER);
lblTitle.setForeground(Color.WHITE);
lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 38));
lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
lblTitle.setBorder(new EmptyBorder(10, 0, 5, 0));
mainPanel.add(lblTitle);

JLabel lblSubTitle = new JLabel("Subscriptions Expiring Soon", SwingConstants.CENTER);
lblSubTitle.setForeground(new Color(200, 200, 220));
lblSubTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
lblSubTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
mainPanel.add(lblSubTitle);

// ===== TOOLBAR (Buttons Row) =====
JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
toolbar.setOpaque(false);

JButton btnExpired = createActionButton("Expired", new Color(220, 60, 60));
JButton btn7Days = createActionButton("7 Days Till Expired", new Color(255, 165, 0));
JButton btnSendMsg = createActionButton("Send Message", new Color(50, 90, 200));

btnExpired.setPreferredSize(new Dimension(160, 40));
btn7Days.setPreferredSize(new Dimension(160, 40));
btnSendMsg.setPreferredSize(new Dimension(160, 40));

btnExpired.addActionListener(e -> loadExpiredMembers());
btn7Days.addActionListener(e -> loadMembersExpiringSoon());
btnSendMsg.addActionListener(e -> sendWhatsAppReminders());


toolbar.add(btnExpired);
toolbar.add(btn7Days);
toolbar.add(btnSendMsg);

mainPanel.add(toolbar);
mainPanel.add(Box.createVerticalStrut(25));

// ===== MOTIVATIONAL BANNER =====
JPanel bannerPanel = new JPanel(new BorderLayout());
bannerPanel.setBackground(new Color(35, 35, 50));
bannerPanel.setBorder(new EmptyBorder(15, 25, 15, 25)); // smaller padding

// Gym icon
JLabel lblImage = null;
try {
    java.net.URL iconURL = getClass().getResource("/Images/gyms.png");
    if (iconURL != null) {
        ImageIcon icon = new ImageIcon(iconURL);
        Image scaled = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        lblImage = new JLabel(new ImageIcon(scaled));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
    }
} catch (Exception e) {
    System.out.println("Gym icon not found.");
}

// Message container (two lines)
JPanel textPanel = new JPanel();
textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
textPanel.setBackground(new Color(35, 35, 50));

JLabel lblLine1 = new JLabel("Keep your members motivated — consistency builds results!");
lblLine1.setForeground(Color.WHITE);
lblLine1.setFont(new Font("Segoe UI", Font.BOLD, 20));
lblLine1.setAlignmentX(Component.CENTER_ALIGNMENT);

JLabel lblLine2 = new JLabel("Track, remind, and engage them for stronger retention ");
lblLine2.setForeground(new Color(180, 180, 200));
lblLine2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
lblLine2.setAlignmentX(Component.CENTER_ALIGNMENT);

textPanel.add(lblLine1);
textPanel.add(Box.createVerticalStrut(5));
textPanel.add(lblLine2);

// Combine icon + text
JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
contentPanel.setBackground(new Color(35, 35, 50));
if (lblImage != null) contentPanel.add(lblImage);
contentPanel.add(textPanel);

bannerPanel.add(contentPanel, BorderLayout.CENTER);
// ===== Prevent Banner from stretching too wide =====
JPanel bannerWrapper = new JPanel();
bannerWrapper.setBackground(new Color(20, 20, 30));
bannerWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

// Set a fixed width limit so it looks centered even in full screen
bannerPanel.setMaximumSize(new Dimension(950, 120)); // Width capped, height balanced
bannerPanel.setPreferredSize(new Dimension(900, 110));

bannerWrapper.add(bannerPanel);

mainPanel.add(bannerWrapper);
mainPanel.add(Box.createVerticalStrut(20));



// ===== TABLE =====
String[] columns = {"Member Name", "Phone Number", "Amount", "Payment Method", "Start Date", "End Date", "Days Left"};
model = new DefaultTableModel(columns, 0);
table = new JTable(model);
styleTable(table);

JScrollPane scrollPane = new JScrollPane(table);
scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
scrollPane.getViewport().setBackground(new Color(30, 30, 45));
scrollPane.setPreferredSize(new Dimension(950, 400));

mainPanel.add(scrollPane);

add(sidebar, BorderLayout.WEST);
add(mainPanel, BorderLayout.CENTER);

}

private void loadMembersExpiringSoon() {
    model.setRowCount(0);

    String query = """
        SELECT u.name,
               p1.phone_number,
               p1.amount,
               p1.payment_method,
               p1.start_date,
               p1.end_date,
               DATEDIFF(p1.end_date, CURDATE()) AS days_left
        FROM payments p1
        INNER JOIN (
            SELECT phone_number, MAX(payment_id) AS last_payment_id
            FROM payments
            GROUP BY phone_number
        ) p2 ON p1.phone_number = p2.phone_number
           AND p1.payment_id = p2.last_payment_id
        JOIN users u ON u.phone_number = p1.phone_number
        WHERE DATEDIFF(p1.end_date, CURDATE()) BETWEEN 0 AND 7
        ORDER BY p1.end_date ASC;
    """;

    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getBigDecimal("amount"),
                    rs.getString("payment_method"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    rs.getInt("days_left")
            });
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error loading 7 days before expiration: " + ex.getMessage());
    }
}



private void loadExpiredMembers() {
    model.setRowCount(0);

    String query = """
        SELECT u.name,
               p1.phone_number,
               p1.amount,
               p1.payment_method,
               p1.start_date,
               p1.end_date,
               DATEDIFF(CURDATE(), p1.end_date) AS days_since_expired
        FROM payments p1
        INNER JOIN (
            SELECT phone_number, MAX(payment_id) AS last_payment_id
            FROM payments
            GROUP BY phone_number
        ) p2 ON p1.phone_number = p2.phone_number
           AND p1.payment_id = p2.last_payment_id
        JOIN users u ON u.phone_number = p1.phone_number
        WHERE DATEDIFF(CURDATE(), p1.end_date) BETWEEN 0 AND 7
        ORDER BY p1.end_date DESC;
    """;

    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getBigDecimal("amount"),
                    rs.getString("payment_method"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    rs.getInt("days_since_expired")
            });
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error loading expired members: " + ex.getMessage());
    }
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
    return btn;
}

private void addMenuButton(JPanel menuPanel, String text, String iconPath) {
    JButton btn = createSidebarButton(text, iconPath);
    btn.addActionListener(e -> {
        switch (text) {
            case "Dashboard" -> openPage(this, new Dashboard());
            case "Members" -> openPage(this, new Members());
            case "Payments" -> openPage(this, new Payment());
            case "Subscriptions" -> openPage(this, new Subscription());
            case "Reports" -> openPage(this, new Reports());
            default -> JOptionPane.showMessageDialog(this, "Page not yet implemented!");
        }
    });
    menuPanel.add(btn);
    menuPanel.add(Box.createVerticalStrut(10));
}

private JButton createActionButton(String text, Color color) {
    JButton btn = new JButton(text);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btn.setBackground(color);
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return btn;
}

private void styleTable(JTable table) {
    table.setBackground(new Color(30, 30, 45));
    table.setForeground(Color.WHITE);
    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    table.setRowHeight(26);
    table.setGridColor(new Color(50, 50, 70));
    table.setSelectionBackground(new Color(50, 90, 200));
    table.setSelectionForeground(Color.WHITE);
    table.getTableHeader().setBackground(new Color(35, 35, 50));
    table.getTableHeader().setForeground(Color.WHITE);
    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
}

private void openPage(JFrame currentFrame, JFrame nextFrame) {
    currentFrame.dispose();
    nextFrame.setVisible(true);
}

private void logout() {
    dispose();
    new Home().setVisible(true);
}
private void sendWhatsAppReminders() {
    int rowCount = model.getRowCount();
    if (rowCount == 0) {
        JOptionPane.showMessageDialog(this, "No members to send messages to.");
        return;
    }

    try {
        for (int i = 0; i < rowCount; i++) {
            int daysLeft = (int) model.getValueAt(i, 6); // Days Left column
            if (daysLeft <= 4 && daysLeft >= 0) {
                String name = model.getValueAt(i, 0).toString();
                String phone = model.getValueAt(i, 1).toString().replaceAll("[^\\d+]", "");

                // ✅ Important: phone must include country code (e.g., 961xxxxxxxx)
                String message = "Hey " + name + "! This is a friendly reminder from SportZone Gym 💪 — "
                        + "your membership will expire in " + daysLeft + " days. "
                        + "Renew now to keep your fitness journey going strong! 🏋️‍♂️";

                String encodedMsg = java.net.URLEncoder.encode(message, "UTF-8");
                String url = "whatsapp://send?phone=" + phone + "&text=" + encodedMsg;

                // Opens WhatsApp Desktop app directly
                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));

                Thread.sleep(1500); // short pause between messages
            }
        }

        JOptionPane.showMessageDialog(this, "WhatsApp messages opened in Desktop App!");
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error sending WhatsApp messages: " + ex.getMessage());
    }
}


public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Subscription().setVisible(true));
}


}
