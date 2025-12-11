package gymmangmentsystem;

import project.ConnectionProvider;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.swing.table.JTableHeader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class MemberDashboard extends JFrame {

    private final int userId;

    // Header
    private JLabel lblWelcomeName;

    // Stats
    private JLabel lblStatWeight, lblStatHeight, lblStatBMI,
            lblStatTotalWorkouts, lblStatThisWeek, lblStatExperience;

    // Profile
    private JLabel lblProfileEmail, lblProfilePhone, lblProfileAge,
            lblProfileGender, lblProfileGoal, lblProfileLastPlan;

    // Workout
    private JLabel lblLastWorkoutName, lblLastWorkoutDate,
            lblLastWorkoutCalories, lblTotalCalories;

    // Chart
    private JPanel weightChartHolder;

    // ✅✅✅ SCHEDULE SYSTEM ✅✅✅
    private JComboBox<String> cmbTime;
    private JTable tblCrowd;
    private DefaultTableModel crowdModel;

    public MemberDashboard(int userId) {
        this.userId = userId;

        setTitle("Member Dashboard");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(25, 25, 35));

        add(buildSidebar("dashboard"), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(25, 25, 35));
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ===== HEADER =====
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);

        JLabel lblTitle = new JLabel("Member Dashboard");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        lblWelcomeName = new JLabel("Welcome...");
        lblWelcomeName.setForeground(new Color(190, 200, 230));
        lblWelcomeName.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        header.add(lblTitle);
        header.add(Box.createHorizontalStrut(20));
        header.add(lblWelcomeName);

        main.add(header, BorderLayout.NORTH);

        // ===== CENTER =====
       // ===== CENTER =====
JPanel center = new JPanel(new BorderLayout(0, 20)); // ✅ vertical gap added
center.setOpaque(false);


        // ===== STATS =====
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 10));
        statsPanel.setOpaque(false);

        lblStatWeight = createStatCard(statsPanel, "Weight", "-");
        lblStatHeight = createStatCard(statsPanel, "Height", "-");
        lblStatBMI = createStatCard(statsPanel, "BMI", "-");
        lblStatTotalWorkouts = createStatCard(statsPanel, "Total Workouts", "0");
        lblStatThisWeek = createStatCard(statsPanel, "This Week", "0");
        lblStatExperience = createStatCard(statsPanel, "Experience", "-");

        JPanel statsWrapper = new JPanel(new BorderLayout());
statsWrapper.setOpaque(false);
statsWrapper.setBorder(new EmptyBorder(0, 0, 15, 0)); // ✅ spacing under stats
statsWrapper.add(statsPanel, BorderLayout.CENTER);

center.add(statsWrapper, BorderLayout.NORTH);


    
        // ===== MID =====
JPanel mid = new JPanel(new GridLayout(1, 2, 15, 0));
mid.setOpaque(false);

      



        // ===== WEIGHT CHART =====
        weightChartHolder = new JPanel(new BorderLayout());
        weightChartHolder.setBackground(new Color(30, 30, 50));
        weightChartHolder.setBorder(new EmptyBorder(10,10,10,10));
        mid.add(weightChartHolder);

      

        // ===== ✅✅✅ SCHEDULE PANEL ✅✅✅ =====
        JPanel schedulePanel = new JPanel(new BorderLayout());
        schedulePanel.setBackground(new Color(30, 30, 50));
        schedulePanel.setBorder(new EmptyBorder(10,10,10,10));

        cmbTime = new JComboBox<>(new String[]{
                "08:00","09:00","10:00","11:00","12:00",
                "13:00","14:00","15:00","16:00",
                "17:00","18:00","19:00","20:00"
        });
        cmbTime.setBackground(new Color(40,40,60));
        cmbTime.setForeground(Color.WHITE);

        JButton btnSave = new JButton("Save Schedule");
        btnSave.addActionListener(e -> saveSchedule());

        JPanel topSchedule = new JPanel();
        topSchedule.setOpaque(false);
        JButton btnDelete = new JButton("Delete My Time");
btnDelete.addActionListener(e -> deleteSchedule());

topSchedule.add(cmbTime);
topSchedule.add(btnSave);
topSchedule.add(btnDelete);


        schedulePanel.add(topSchedule, BorderLayout.NORTH);

        crowdModel = new DefaultTableModel(new String[]{"Time","Members"},0);
        tblCrowd = new JTable(crowdModel);
        tblCrowd.getColumnModel().getColumn(1).setCellRenderer(
    new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            lbl.setHorizontalAlignment(SwingConstants.CENTER);

            int members = Integer.parseInt(value.toString());

            if (members <= 3) {
                lbl.setForeground(new Color(0, 220, 120));   // 🟢 GREEN
            } else if (members <= 6) {
                lbl.setForeground(new Color(255, 200, 0));   // 🟡 YELLOW
            } else {
                lbl.setForeground(new Color(255, 80, 80));   // 🔴 RED
            }

            if (isSelected) {
                lbl.setForeground(Color.WHITE);
                lbl.setBackground(new Color(60, 120, 250));
            }

            return lbl;
        }
    }
);

tblCrowd.setBackground(new Color(35, 35, 50));     // dark row
tblCrowd.setForeground(Color.WHITE);              // white text
tblCrowd.setGridColor(new Color(60, 60, 90));     // subtle grid
tblCrowd.setRowHeight(26);
tblCrowd.setFont(new Font("Segoe UI", Font.PLAIN, 13));

tblCrowd.setSelectionBackground(new Color(60, 120, 250)); // blue like buttons
tblCrowd.setSelectionForeground(Color.WHITE);
JTableHeader h = tblCrowd.getTableHeader();
h.setBackground(new Color(30, 30, 45));
h.setForeground(Color.WHITE);
h.setFont(new Font("Segoe UI", Font.BOLD, 14));
h.setReorderingAllowed(false);
DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
centerRenderer.setBackground(new Color(35, 35, 50));
centerRenderer.setForeground(Color.WHITE);




tblCrowd.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);


// ✅ HEADER STYLE
tblCrowd.getTableHeader().setBackground(new Color(30, 30, 45));
tblCrowd.getTableHeader().setForeground(Color.WHITE);
tblCrowd.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));






      JScrollPane scroll = new JScrollPane(tblCrowd);
scroll.getViewport().setBackground(new Color(35, 35, 50));
scroll.setBorder(BorderFactory.createEmptyBorder());
schedulePanel.add(scroll, BorderLayout.CENTER);


        mid.add(schedulePanel);

        JPanel midWrapper = new JPanel(new BorderLayout());
midWrapper.setOpaque(false);
midWrapper.setBorder(new EmptyBorder(10, 0, 0, 0)); // ✅ pushes mid DOWN
midWrapper.add(mid, BorderLayout.CENTER);

center.add(midWrapper, BorderLayout.CENTER);

        main.add(center, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);

        // ✅✅✅ LOAD EVERYTHING ✅✅✅
     
       // ✅✅✅ LOAD EVERYTHING ✅✅✅
loadOverviewData();      // ✅ fixes Welcome, Weight, Height, BMI, Experience
loadWorkoutStats();     // ✅ fixes Total Workouts + This Week
loadWeightTrendChart(); // ✅ keeps chart working
loadTodayCrowd();       // ✅ keeps schedule table working

    }

    // ==================== ✅ SCHEDULE SAVE ====================
private void saveSchedule() {
    String time = cmbTime.getSelectedItem().toString();

    try (Connection con = ConnectionProvider.con()) {

        // ✅ Check if user already booked today
        PreparedStatement check = con.prepareStatement(
            "SELECT id FROM gym_schedule WHERE user_id = ? AND visit_date = CURDATE()"
        );
        check.setInt(1, userId);
        ResultSet rs = check.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this,
                "You already selected a time today.\nDelete it first to change.",
                "Schedule Exists",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // ✅ Insert new time
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO gym_schedule (user_id, visit_date, visit_time) VALUES (?, CURDATE(), ?)"
        );
        ps.setInt(1, userId);
        ps.setString(2, time);
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Schedule saved successfully ✅");
        loadTodayCrowd();

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving schedule.");
    }
}
private void deleteSchedule() {
    try (Connection con = ConnectionProvider.con()) {

        PreparedStatement ps = con.prepareStatement(
            "DELETE FROM gym_schedule WHERE user_id = ? AND visit_date = CURDATE()"
        );
        ps.setInt(1, userId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            JOptionPane.showMessageDialog(this, "Your schedule was deleted ✅");
            loadTodayCrowd();
        } else {
            JOptionPane.showMessageDialog(this, "You have no schedule for today.");
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error deleting schedule.");
    }
}



    // ==================== ✅ CROWD TABLE ====================
    private void loadTodayCrowd() {
        crowdModel.setRowCount(0);
        try (Connection con = ConnectionProvider.con();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT visit_time, COUNT(*) FROM gym_schedule WHERE visit_date = CURDATE() GROUP BY visit_time"
)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                crowdModel.addRow(new Object[]{
                        rs.getString(1),
                        rs.getInt(2)
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

 

    // ================= SIDEBAR ===================

    private JPanel buildSidebar(String activePage) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(20, 20, 30));
        sidebar.setPreferredSize(new Dimension(230, getHeight()));

        // Top
        JPanel top = new JPanel();
        top.setBackground(new Color(20, 20, 30));
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel lblIcon = new JLabel("💪", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblApp = new JLabel("Member Panel");
        lblApp.setForeground(Color.WHITE);
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblApp.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(lblIcon);
        top.add(Box.createVerticalStrut(10));
        top.add(lblApp);

        sidebar.add(top, BorderLayout.NORTH);

        // Menu buttons
        JPanel menu = new JPanel();
        menu.setBackground(new Color(20, 20, 30));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(10, 15, 10, 15));

        JButton btnDashboard = createSidebarButton("Dashboard",
                "/Images/dashboard.png", activePage.equals("dashboard"));
        btnDashboard.addActionListener(e -> {
            // already here
        });

        JButton btnAIWorkout = createSidebarButton("AI Workout Plan",
                "/Images/workouts.png", activePage.equals("ai"));
        btnAIWorkout.addActionListener(e -> {
            new AIWorkout(userId).setVisible(true);
            dispose();
        });

        JButton btnProgress = createSidebarButton("Progress & History",
                "/Images/progress.png", activePage.equals("progress"));
        btnProgress.addActionListener(e -> {
            new ProgressPage(userId).setVisible(true);
            dispose();
        });

        menu.add(btnDashboard);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnAIWorkout);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnProgress);

        sidebar.add(menu, BorderLayout.CENTER);

        // Bottom: Logout button
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(20, 20, 30));
        bottom.setBorder(new EmptyBorder(10, 15, 20, 15));

        JButton btnLogout = createSidebarButton("Logout", "/Images/logout.png", false);
        btnLogout.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        bottom.add(btnLogout, BorderLayout.SOUTH);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createSidebarButton(String text, String iconPath, boolean active) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(10);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaled = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
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

    // ================== UI HELPERS ===================

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

   

    // ================== DATA LOADING ===================

   private void loadOverviewData() {
    try (Connection con = ConnectionProvider.con()) {

        String sql =
                "SELECT u.name, u.email, u.phone_number, " +
                "       mi.weight, mi.height, mi.age, mi.gender, mi.goal, mi.experience " +
                "FROM users u " +
                "LEFT JOIN member_info mi ON u.id = mi.user_id " +
                "WHERE u.id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name   = rs.getString("name");
                String email  = rs.getString("email");
                String phone  = rs.getString("phone_number");

                // --- Convert numeric DB values safely ---
                BigDecimal weightBD = rs.getBigDecimal("weight");
                BigDecimal heightBD = rs.getBigDecimal("height");
                BigDecimal ageBD    = rs.getBigDecimal("age");

                Double weight = (weightBD != null) ? weightBD.doubleValue() : null;
                Integer height = (heightBD != null) ? heightBD.intValue() : null;
                Integer age = (ageBD != null) ? ageBD.intValue() : null;

                String gender = rs.getString("gender");
                String goal   = rs.getString("goal");
                String exp    = rs.getString("experience");

                // ==== SET UI VALUES ====

                lblWelcomeName.setText("Welcome, " + (name != null ? name : "Member"));
               

                // Weight
                if (weight != null)
                    lblStatWeight.setText(String.format("%.1f kg", weight));
                else
                    lblStatWeight.setText("Not set");

                // Height + BMI
                if (height != null && height > 0) {
                    lblStatHeight.setText(height + " cm");

                    if (weight != null) {
                        double bmi = weight / Math.pow(height / 100.0, 2);
                        lblStatBMI.setText(String.format("%.1f", bmi));
                    } else {
                        lblStatBMI.setText("-");
                    }
                } else {
                    lblStatHeight.setText("Not set");
                    lblStatBMI.setText("-");
                }

                lblStatExperience.setText(exp != null ? exp : "-");
            }
        }

        // ===== LAST AI PLAN =====
        String lastPlan = "-";
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT created_at FROM workout_plans WHERE user_id = ? ORDER BY created_at DESC LIMIT 1")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) lastPlan = ts.toString();
            }
        }
      

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


    private void loadWorkoutStats() {
        int total = 0;
        int week = 0;
        int totalCalories = 0;

        String lastName = "None";
        String lastDate = "-";
        String lastCalories = "-";

        try (Connection con = ConnectionProvider.con()) {

            // total workouts + total calories
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*), COALESCE(SUM(calories),0) FROM workout_history WHERE user_id = ?")) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    total = rs.getInt(1);
                    totalCalories = rs.getInt(2);
                }
            }

            // workouts in last 7 days
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM workout_history " +
                    "WHERE user_id = ? AND workout_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    week = rs.getInt(1);
                }
            }

            // last workout
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT workout_name, workout_date, calories " +
                    "FROM workout_history WHERE user_id = ? " +
                    "ORDER BY workout_date DESC, id DESC LIMIT 1")) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    lastName = rs.getString("workout_name");
                    Date d   = rs.getDate("workout_date");
                    int cals = rs.getInt("calories");
                    lastDate = (d != null) ? d.toString() : "-";
                    lastCalories = cals + " kcal";
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        lblStatTotalWorkouts.setText(String.valueOf(total));
        lblStatThisWeek.setText(String.valueOf(week));

       
    }

    private void loadWeightTrendChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection con = ConnectionProvider.con();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT weight, progress_date " +
                     "FROM member_progress WHERE user_id = ? " +
                     "ORDER BY progress_date ASC")) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double w = rs.getDouble("weight");
                Date d = rs.getDate("progress_date");
                String dateStr = (d != null) ? d.toString() : "";
                dataset.addValue(w, "Weight", dateStr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "", "Date", "Weight (kg)",
                dataset, PlotOrientation.VERTICAL,
                false, true, false);

        chart.setBackgroundPaint(new Color(25, 25, 40));
        chart.getPlot().setBackgroundPaint(new Color(30, 30, 50));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(new Color(25, 25, 40));

        weightChartHolder.removeAll();
        weightChartHolder.add(chartPanel, BorderLayout.CENTER);
        weightChartHolder.revalidate();
        weightChartHolder.repaint();
        weightChartHolder.updateUI();

    }

    // ================== ACTION: ADD WEIGHT ===================

   

    // ================== MAIN (for testing) ===================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberDashboard(1).setVisible(true));
    }
}

