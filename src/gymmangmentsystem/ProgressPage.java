package gymmangmentsystem;

import project.ConnectionProvider;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ProgressPage extends JFrame {

    private final int userId;
private DefaultTableModel workoutHistoryModel;
private JTable tblWorkoutHistory;

    // Stat labels
    private JLabel lblWeight;
    private JLabel lblHeight;
    private JLabel lblBMI;
    private JLabel lblGoal;
    private JLabel lblExperience;
    private JLabel lblWorkoutCount;
    private JLabel lblWeekWorkoutCount;

    // Overview labels
  
    // Chart + tables
    private JPanel chartPanelHolder;
   

   
    
    private JTextArea txtPlanDetails;

    public ProgressPage(int userId) {
        this.userId = userId;

        setTitle("Progress & History");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(15, 15, 25));

        // Sidebar
        JPanel sidebar = buildSidebar("progress");
        add(sidebar, BorderLayout.WEST);

        // Main content
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(15, 15, 25));
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ===== HEADER =====
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(15, 15, 25));

        JLabel title = new JLabel("Progress & History");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel subtitle = new JLabel(
                "<html><span style='color:#B0B0C5'>Track your body stats, workouts, and AI plans in one place.</span></html>");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        main.add(header, BorderLayout.NORTH);

        // ===== CENTER LAYOUT =====
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(new Color(15, 15, 25));
        center.setBorder(new EmptyBorder(15, 0, 0, 0));

        // TOP: big stat cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 15, 10));
        statsPanel.setBackground(new Color(15, 15, 25));

        lblWeight = createStatCard(statsPanel, "Weight", "loading...");
        lblHeight = createStatCard(statsPanel, "Height", "loading...");
        lblBMI = createStatCard(statsPanel, "BMI", "loading...");
        lblGoal = createStatCard(statsPanel, "Goal", "loading...");
        lblExperience = createStatCard(statsPanel, "Experience", "loading...");
        lblWorkoutCount = createStatCard(statsPanel, "Total Workouts", "0");
        lblWeekWorkoutCount = createStatCard(statsPanel, "This Week", "0");
        statsPanel.add(new JLabel());

        center.add(statsPanel, BorderLayout.NORTH);

        // ===== MIDDLE GRID (optimized layout) =====

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(new Color(15, 15, 25));
        GridBagConstraints gbc = new GridBagConstraints();

      

        

      // ========== AI WORKOUT HISTORY + TRAINING VIEW ==========
gbc.gridx = 0;
gbc.gridy = 0;
gbc.gridwidth = 2;
gbc.weightx = 1;
gbc.weighty = 1;
gbc.insets = new Insets(10, 10, 10, 10);
gbc.fill = GridBagConstraints.BOTH;

JPanel aiMainCard = new JPanel(new GridLayout(1, 2, 15, 0));
aiMainCard.setBackground(new Color(15, 15, 25));
aiMainCard.setBorder(new EmptyBorder(10, 10, 10, 10));
// ===== LEFT: WORKOUT HISTORY TABLE (REPLACES AI TABLE) =====
JPanel tableCard = new JPanel(new BorderLayout());
tableCard.setBackground(new Color(25, 25, 40));
tableCard.setBorder(new EmptyBorder(12, 12, 12, 12));

JLabel workoutTitle = new JLabel("Workout History");
workoutTitle.setForeground(Color.WHITE);
workoutTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

workoutHistoryModel = new DefaultTableModel(
        new String[]{"Date", "Workout Name"}, 0);

tblWorkoutHistory = new JTable(workoutHistoryModel);
styleTable(tblWorkoutHistory);

JScrollPane workoutScroll = new JScrollPane(tblWorkoutHistory);
workoutScroll.getViewport().setBackground(new Color(30, 30, 45));
workoutScroll.setBackground(new Color(30, 30, 45));
workoutScroll.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 100)));

workoutScroll.setBorder(BorderFactory.createEmptyBorder());

tableCard.add(workoutTitle, BorderLayout.NORTH);
tableCard.add(workoutScroll, BorderLayout.CENTER);




// ===== RIGHT: AI TRAINING TEXT =====
JPanel trainingCard = new JPanel(new BorderLayout());
trainingCard.setBackground(new Color(25, 25, 40));
trainingCard.setBorder(new EmptyBorder(12, 12, 12, 12));

JLabel trainingTitle = new JLabel("AI Training Details");
trainingTitle.setForeground(Color.WHITE);
trainingTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

txtPlanDetails = new JTextArea();
txtPlanDetails.setEditable(false);
txtPlanDetails.setForeground(Color.WHITE);
txtPlanDetails.setBackground(new Color(20, 20, 35));
txtPlanDetails.setFont(new Font("Consolas", Font.PLAIN, 14));
txtPlanDetails.setLineWrap(true);
txtPlanDetails.setWrapStyleWord(true);
txtPlanDetails.setMargin(new Insets(10, 10, 10, 10));

JScrollPane planScroll = new JScrollPane(txtPlanDetails);
planScroll.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 100)));

trainingCard.add(trainingTitle, BorderLayout.NORTH);
trainingCard.add(planScroll, BorderLayout.CENTER);

// ✅ ADD BOTH TO GRID
aiMainCard.add(tableCard);
aiMainCard.add(trainingCard);

grid.add(aiMainCard, gbc);   // ✅ your AI table + training





        
        center.add(grid, BorderLayout.CENTER);
        main.add(center, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);

        // Load data
        loadData();
    }

    // ==================== SIDEBAR (unchanged) ====================
    private JPanel buildSidebar(String activePage) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, getHeight()));
        sidebar.setBackground(new Color(10, 10, 20));

        JPanel top = new JPanel();
        top.setBackground(new Color(10, 10, 20));
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(25, 20, 20, 20));

        JLabel icon = new JLabel("📊", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appLabel = new JLabel("Member Panel");
        appLabel.setForeground(Color.WHITE);
        appLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(icon);
        top.add(Box.createVerticalStrut(8));
        top.add(appLabel);

        sidebar.add(top, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setBackground(new Color(10, 10, 20));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(10, 15, 10, 15));

        JButton btnDashboard = createSidebarButton("Dashboard", "/Images/dashboard.png",
                activePage.equals("dashboard"));
        btnDashboard.addActionListener(e -> {
            new MemberDashboard(userId).setVisible(true);
            dispose();
        });

        JButton btnAIWorkout = createSidebarButton("AI Workout Plan", "/Images/workouts.png",
                activePage.equals("ai"));
        btnAIWorkout.addActionListener(e -> {
            new AIWorkout(userId).setVisible(true);
            dispose();
        });

        JButton btnProgress = createSidebarButton("Progress & History", "/Images/progress.png",
                activePage.equals("progress"));
        // stay here

        menu.add(btnDashboard);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnAIWorkout);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnProgress);

        sidebar.add(menu, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(10, 10, 20));
        bottom.setBorder(new EmptyBorder(10, 15, 20, 15));
        bottom.setLayout(new BorderLayout());

        JButton btnLogout = createSidebarButton("Logout", "/Images/logout.png", false);
        btnLogout.addActionListener(e -> handleLogout());

        bottom.add(btnLogout, BorderLayout.SOUTH);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }

    private JButton createSidebarButton(String text, String iconPath, boolean active) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setMaximumSize(new Dimension(230, 42));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(10);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaled = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
        } catch (Exception ex) {
            System.out.println("Icon not found: " + iconPath);
        }

        if (active) {
            btn.setBackground(new Color(60, 120, 250));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(30, 30, 45));
            btn.setForeground(new Color(220, 220, 230));
        }
        return btn;
    }

    // ==================== UI HELPERS ====================
    private JLabel createStatCard(JPanel parent, String title, String valueText) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(30, 30, 50));
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(180, 180, 210));
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblValue = new JLabel(valueText);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(5));
        card.add(lblValue);

        parent.add(card);

        return lblValue;
    }

  private void styleTable(JTable table) {
    table.setBackground(new Color(30, 30, 45));
    table.setForeground(Color.WHITE);
    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    table.setRowHeight(26);

    // Header
    table.getTableHeader().setBackground(new Color(25, 25, 35));
    table.getTableHeader().setForeground(Color.WHITE);
    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

    // Grid + selection
    table.setGridColor(new Color(60, 60, 80));
    table.setSelectionBackground(new Color(60, 120, 250));
    table.setSelectionForeground(Color.WHITE);

    // ✅ Remove white background from empty space
    table.setFillsViewportHeight(true);
}


    // ==================== DATA LOADING ====================

    private void loadData() {
        loadMemberInfo();
        loadWorkoutStats();
    loadWorkoutHistory();
    tblWorkoutHistory.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) {
        int row = tblWorkoutHistory.getSelectedRow();
        if (row >= 0) {
            Date selectedDate = (Date) workoutHistoryModel.getValueAt(row, 0);
            loadAIPlanForWorkoutDate(selectedDate);
        }
    }
});

     
 

    }

    private void loadMemberInfo() {
        try (Connection con = ConnectionProvider.con();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT weight, height, goal, experience FROM member_info WHERE user_id = ?")) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double weight = rs.getDouble("weight");
                int height = rs.getInt("height");
                String goal = rs.getString("goal");
                String exp = rs.getString("experience");

                lblWeight.setText(String.format("%.1f kg", weight));
                lblHeight.setText(height + " cm");
                lblGoal.setText(goal != null ? goal : "-");
                lblExperience.setText(exp != null ? exp : "-");

                if (height > 0) {
                    double bmi = weight / Math.pow(height / 100.0, 2);
                    lblBMI.setText(String.format("%.2f", bmi));
                } else {
                    lblBMI.setText("-");
                }
            } else {
                lblWeight.setText("Not set");
                lblHeight.setText("Not set");
                lblBMI.setText("-");
                lblGoal.setText("-");
                lblExperience.setText("-");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadWorkoutStats() {
        int total = 0;
        int week = 0;
        String lastWorkoutName = "None";
        String lastWorkoutDate = "-";

        try (Connection con = ConnectionProvider.con()) {

            // total workouts
            try (PreparedStatement psTotal = con.prepareStatement(
                    "SELECT COUNT(*) FROM workout_history WHERE user_id = ?")) {
                psTotal.setInt(1, userId);
                ResultSet rs = psTotal.executeQuery();
                if (rs.next()) total = rs.getInt(1);
            }

            // workouts in last 7 days
            try (PreparedStatement psWeek = con.prepareStatement(
                    "SELECT COUNT(*) FROM workout_history WHERE user_id = ? AND workout_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")) {
                psWeek.setInt(1, userId);
                ResultSet rs = psWeek.executeQuery();
                if (rs.next()) week = rs.getInt(1);
            }

            // last workout
            try (PreparedStatement psLast = con.prepareStatement(
                    "SELECT workout_name, workout_date FROM workout_history WHERE user_id = ? ORDER BY workout_date DESC LIMIT 1")) {
                psLast.setInt(1, userId);
                ResultSet rs = psLast.executeQuery();
                if (rs.next()) {
                    lastWorkoutName = rs.getString("workout_name");
                    Date d = rs.getDate("workout_date");
                    lastWorkoutDate = (d != null) ? d.toString() : "-";
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        lblWorkoutCount.setText(String.valueOf(total));
        lblWeekWorkoutCount.setText(String.valueOf(week));
       
       

        // last AI plan
        String lastPlan = "-";
        try (Connection con = ConnectionProvider.con();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT created_at FROM workout_plans WHERE user_id = ? ORDER BY created_at DESC LIMIT 1")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) lastPlan = ts.toString();
            }
        } catch (Exception ignored) {
        }
      
    }

   

  
    private void loadWorkoutHistory() {
    workoutHistoryModel.setRowCount(0);

    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(
                 "SELECT workout_date, workout_name " +
                 "FROM workout_history " +
                 "WHERE user_id = ? ORDER BY workout_date DESC")) {

        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            workoutHistoryModel.addRow(new Object[]{
                    rs.getDate("workout_date"),
                    rs.getString("workout_name")
            });
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
    private void loadAIPlanForWorkoutDate(Date workoutDate) {
    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(
                 "SELECT plan FROM workout_plans " +
                 "WHERE user_id = ? AND DATE(created_at) = ? " +
                 "ORDER BY created_at DESC LIMIT 1")) {

        ps.setInt(1, userId);
        ps.setDate(2, workoutDate);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            txtPlanDetails.setText(rs.getString("plan"));
        } else {
            txtPlanDetails.setText("No AI workout found for this workout.");
        }

    } catch (Exception ex) {
        txtPlanDetails.setText("Error loading AI workout.");
        ex.printStackTrace();
    }
}



    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProgressPage(1).setVisible(true));
    }
}
