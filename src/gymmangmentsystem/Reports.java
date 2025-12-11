package gymmangmentsystem;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.data.category.DefaultCategoryDataset;
import project.ConnectionProvider;

public class Reports extends JFrame {

    public Reports() {
        setTitle("Reports & Analytics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ====== SIDEBAR ======
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(25, 25, 35));
        sidebar.setPreferredSize(new Dimension(230, getHeight()));

        // --- TOP SECTION ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(25, 25, 35));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblIcon = new JLabel("📊", SwingConstants.CENTER);
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

        // --- MENU BUTTONS ---
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(25, 25, 35));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        addMenuButton(menuPanel, "Dashboard", "/Images/dashboard.png");
        addMenuButton(menuPanel, "Members", "/Images/group.png");
        addMenuButton(menuPanel, "Payments", "/Images/atm-card.png");
        addMenuButton(menuPanel, "Subscriptions", "/Images/subscription-active.png");
        addMenuButton(menuPanel, "Reports", "/Images/analysis.png");

        // --- LOGOUT BUTTON ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(25, 25, 35));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(20, 10, 30, 10));

        JButton btnLogout = createSidebarButton("Logout", "/Images/logout.png");
        btnLogout.addActionListener(e -> logout());
        bottomPanel.add(btnLogout);

        sidebar.add(topPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        // ====== MAIN CONTENT ======
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(20, 20, 30));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTitle = new JLabel("Reports & Analytics");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // ====== TOP STATS ======
        JPanel topCards = new JPanel(new GridLayout(1, 4, 25, 0));
        topCards.setOpaque(false);

        double totalPayments = getCurrentMonthPayments();
double totalExpenses = getCurrentMonthExpenses();
double netProfit = totalPayments - totalExpenses;

        int newMembers = getNewMembersThisMonth();

        topCards.add(createInfoCard("Net Profit", "$" + (int) netProfit, "/Images/net-profit.png"));
        topCards.add(createInfoCard("New Members", String.valueOf(newMembers), "/Images/member1.png"));
        topCards.add(createInfoCard("Total Expenses", "$" + (int) totalExpenses, "/Images/expense.png"));
        topCards.add(createInfoCard("Payments (This Month)", "$" + (int) totalPayments, "/Images/profits.png"));

        mainPanel.add(topCards, BorderLayout.NORTH);

        // ====== CHARTS ======
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        chartsPanel.setBackground(new Color(20, 20, 30));
        chartsPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

       chartsPanel.add(createChartPanel("Monthly Net Profit", getMonthlyNetProfitChart()));
chartsPanel.add(createChartPanel("Monthly Expenses", getMonthlyExpensesChart()));

        mainPanel.add(chartsPanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    // ======== BUTTON HELPERS ========
    private void addMenuButton(JPanel menuPanel, String text, String iconPath) {
        JButton btn = createSidebarButton(text, iconPath);
        btn.addActionListener(e -> {
            switch (text) {
                case "Dashboard" -> openPage(this, new Dashboard());
                case "Members" -> openPage(this, new Members());
                case "Payments" -> openPage(this, new Payment());
                case "Subscriptions" -> openPage(this, new Subscription());
                case "Reports" -> openPage(this, new Reports());
            }
        });
        menuPanel.add(btn);
        menuPanel.add(Box.createVerticalStrut(10));
    }

    private JButton createSidebarButton(String text, String iconPath) {
        JButton btn = new JButton("  " + text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(35, 35, 50));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            System.out.println("Icon missing: " + iconPath);
        }

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(50, 50, 70)); }
            @Override public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(35, 35, 50)); }
        });
        return btn;
    }

    // ======== INFO CARD ========
    private JPanel createInfoCard(String title, String value, String iconPath) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 60));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setOpaque(false);

        JLabel iconLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaled = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            iconLabel.setText("❔");
        }

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(180, 180, 200));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));

        top.add(iconLabel);
        top.add(lblTitle);

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setBorder(new EmptyBorder(10, 5, 0, 0));

        panel.add(top, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);
        return panel;
    }

  private double getCurrentMonthExpenses() {
    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(
             "SELECT COALESCE(SUM(cost),0) FROM expenses " +
             "WHERE MONTH(expense_date)=MONTH(CURDATE()) " +
             "AND YEAR(expense_date)=YEAR(CURDATE())"
         );
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) return rs.getDouble(1);

    } catch (Exception e) { e.printStackTrace(); }

    return 0;
}



  private double getCurrentMonthPayments() {
    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(
             "SELECT COALESCE(SUM(amount),0) FROM payments " +
             "WHERE MONTH(start_date)=MONTH(CURDATE()) " +
             "AND YEAR(start_date)=YEAR(CURDATE())"
         );
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) return rs.getDouble(1);

    } catch (Exception e) { e.printStackTrace(); }

    return 0;
}


   private int getNewMembersThisMonth() {
    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(
             "SELECT COUNT(DISTINCT phone_number) FROM payments " +
             "WHERE MONTH(start_date)=MONTH(CURDATE()) " +
             "AND YEAR(start_date)=YEAR(CURDATE())"
         );
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) return rs.getInt(1);

    } catch (Exception e) { e.printStackTrace(); }

    return 0;
}



    // ======== CHARTS ========
    private JFreeChart getRevenueChart() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for (String m : months) ds.addValue(0, "Revenue", m);

        try (Connection con = ConnectionProvider.con();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT MONTH(start_date) AS m, SUM(amount) AS total FROM payments WHERE YEAR(start_date)=YEAR(CURDATE()) GROUP BY MONTH(start_date)"
             );
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.setValue(rs.getDouble("total"), "Revenue", months[rs.getInt("m") - 1]);
        } catch (Exception e) { e.printStackTrace(); }

        JFreeChart chart = ChartFactory.createBarChart("", "Month", "Amount ($)", ds, PlotOrientation.VERTICAL, false, true, false);
        styleChart(chart, new Color(80, 140, 255), true);
        CategoryPlot plot = chart.getCategoryPlot();
CategoryAxis xAxis = plot.getDomainAxis();

// Force all categories (months) to appear
xAxis.setCategoryMargin(0.05);
xAxis.setLowerMargin(0.02);
xAxis.setUpperMargin(0.02);

// Make sure month labels are visible and readable
xAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
xAxis.setTickLabelsVisible(true);
xAxis.setTickMarksVisible(true);
xAxis.setTickLabelPaint(Color.WHITE);
xAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));

        return chart;
    }

    private JFreeChart getMembersChart() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for (String m : months) ds.addValue(0, "Members", m);

        try (Connection con = ConnectionProvider.con();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT MONTH(start_date) AS m, COUNT(DISTINCT phone_number) AS cnt FROM payments WHERE YEAR(start_date)=YEAR(CURDATE()) GROUP BY MONTH(start_date)"
             );
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.setValue(rs.getInt("cnt"), "Members", months[rs.getInt("m") - 1]);
        } catch (Exception e) { e.printStackTrace(); }

        JFreeChart chart = ChartFactory.createLineChart("", "Month", "Members", ds, PlotOrientation.VERTICAL, false, true, false);
        styleChart(chart, new Color(80, 250, 180), false);
        CategoryPlot plot = chart.getCategoryPlot();
CategoryAxis xAxis = plot.getDomainAxis();

// Force all categories (months) to appear
xAxis.setCategoryMargin(0.05);
xAxis.setLowerMargin(0.02);
xAxis.setUpperMargin(0.02);

// Make sure month labels are visible and readable
xAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
xAxis.setTickLabelsVisible(true);
xAxis.setTickMarksVisible(true);
xAxis.setTickLabelPaint(Color.WHITE);
xAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));

        return chart;
    }

   private void styleChart(JFreeChart chart, Color seriesColor, boolean isBar) {
    // ======== Make entire chart dark ========
    chart.setBackgroundPaint(new Color(20, 20, 30));

    CategoryPlot plot = chart.getCategoryPlot();
    plot.setBackgroundPaint(new Color(30, 30, 45));
    plot.setRangeGridlinePaint(new Color(90, 90, 120));
    plot.setOutlineVisible(false);

    // ======== X Axis (Months) ========
    CategoryAxis xAxis = plot.getDomainAxis();
    xAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
    xAxis.setTickLabelsVisible(true);
    xAxis.setTickMarksVisible(true);
    xAxis.setTickLabelPaint(Color.WHITE);
    xAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
    xAxis.setLowerMargin(0.02);
    xAxis.setUpperMargin(0.02);
    xAxis.setCategoryMargin(0.05);
    xAxis.setLabel("Month");
    xAxis.setLabelPaint(Color.WHITE);
    xAxis.setLabelFont(new Font("Segoe UI", Font.BOLD, 13));

    // ======== Y Axis ========
    ValueAxis yAxis = plot.getRangeAxis();
    yAxis.setTickLabelPaint(Color.WHITE);
    yAxis.setLabelPaint(Color.WHITE);
    yAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));

    // ======== Renderers ========
    if (isBar) {
        BarRenderer r = (BarRenderer) plot.getRenderer();
        r.setSeriesPaint(0, seriesColor);
        r.setBarPainter(new StandardBarPainter());
        r.setShadowVisible(false);

        // Anonymous class instead of lambda
        r.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator() {
            @Override
            public String generateLabel(org.jfree.data.category.CategoryDataset dataset, int series, int category) {
                Number v = dataset.getValue(series, category);
                return (v != null && v.doubleValue() != 0) ? v.toString() : null;
            }
        });

        r.setDefaultItemLabelsVisible(true);
        r.setDefaultItemLabelPaint(Color.WHITE);
        r.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 13));
        r.setItemMargin(0.05);
        r.setMaximumBarWidth(0.08);
    } else {
        LineAndShapeRenderer r = (LineAndShapeRenderer) plot.getRenderer();
        r.setSeriesPaint(0, seriesColor);
        r.setSeriesStroke(0, new BasicStroke(2.5f));
        r.setDefaultShapesVisible(true);
        r.setDefaultShape(new java.awt.geom.Ellipse2D.Double(-4, -4, 8, 8));

        // Anonymous class instead of lambda
        r.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator() {
            @Override
            public String generateLabel(org.jfree.data.category.CategoryDataset dataset, int series, int category) {
                Number v = dataset.getValue(series, category);
                return (v != null && v.doubleValue() != 0) ? v.toString() : null;
            }
        });

        r.setDefaultItemLabelsVisible(true);
        r.setDefaultItemLabelPaint(Color.WHITE);
        r.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    // ======== Final Touch: Make sure month labels always appear ========
    plot.setDomainGridlinesVisible(false);
    plot.getDomainAxis().setTickLabelsVisible(true);
    plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
}
   private JFreeChart getMonthlyExpensesChart() {
    DefaultCategoryDataset ds = new DefaultCategoryDataset();
    String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    for (String m : months) ds.addValue(0, "Expenses", m);

    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(
             "SELECT MONTH(expense_date) AS m, SUM(cost) AS total " +
             "FROM expenses WHERE YEAR(expense_date)=YEAR(CURDATE()) " +
             "GROUP BY MONTH(expense_date)"
         );
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            ds.setValue(
                rs.getDouble("total"),
                "Expenses",
                months[rs.getInt("m") - 1]
            );
        }

    } catch (Exception e) { e.printStackTrace(); }

    JFreeChart chart = ChartFactory.createBarChart(
        "", "Month", "Expenses ($)",
        ds, PlotOrientation.VERTICAL,
        false, true, false
    );

    styleChart(chart, new Color(255, 100, 100), true);
    return chart;
}
private JFreeChart getMonthlyNetProfitChart() {
    DefaultCategoryDataset ds = new DefaultCategoryDataset();
    String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    for (String m : months) ds.addValue(0, "Net Profit", m);

    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(
             "SELECT m.mth, " +
             "COALESCE(p.total,0) - COALESCE(e.total,0) AS profit " +
             "FROM (SELECT 1 mth UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 " +
             "      UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) m " +
             "LEFT JOIN (SELECT MONTH(start_date) mth, SUM(amount) total FROM payments " +
             "           WHERE YEAR(start_date)=YEAR(CURDATE()) GROUP BY MONTH(start_date)) p " +
             "ON m.mth = p.mth " +
             "LEFT JOIN (SELECT MONTH(expense_date) mth, SUM(cost) total FROM expenses " +
             "           WHERE YEAR(expense_date)=YEAR(CURDATE()) GROUP BY MONTH(expense_date)) e " +
             "ON m.mth = e.mth"
         );
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            ds.setValue(
                rs.getDouble("profit"),
                "Net Profit",
                months[rs.getInt("mth") - 1]
            );
        }

    } catch (Exception e) { e.printStackTrace(); }

    JFreeChart chart = ChartFactory.createLineChart(
        "", "Month", "Net Profit ($)",
        ds, PlotOrientation.VERTICAL,
        false, true, false
    );

    styleChart(chart, new Color(100, 255, 150), false);
    return chart;
}




    private JPanel createChartPanel(String title, JFreeChart chart) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 20, 30));
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lbl, BorderLayout.NORTH);

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(new Color(20, 20, 30));
        panel.add(cp, BorderLayout.CENTER);
        return panel;
    }

    private void openPage(JFrame current, JFrame next) {
        current.dispose();
        next.setVisible(true);
    }

    private void logout() {
        dispose();
        new Home().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Reports().setVisible(true));
    }
}
