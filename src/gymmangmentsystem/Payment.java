package gymmangmentsystem;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import project.ConnectionProvider;

public class Payment extends JFrame {

    private DefaultTableModel paymentModel;
    private JTable paymentTable;
    private JTable expenseTable;

    public Payment() {
        setTitle("Payment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ====== SIDEBAR ======
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(25, 25, 35));
        sidebar.setPreferredSize(new Dimension(230, getHeight()));
        sidebar.setLayout(new BorderLayout());

        // Sidebar top
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(25, 25, 35));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblIcon = new JLabel("💳", SwingConstants.CENTER);
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

        // Sidebar buttons
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

        // ====== MAIN PANEL ======
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(20, 20, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTitle = new JLabel("Payment Tracking");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0)); // Space below title
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(20));

        // ================= PAYMENT HISTORY =================
        JLabel lblPaymentHistory = new JLabel("Payment History");
        lblPaymentHistory.setForeground(Color.WHITE);
        lblPaymentHistory.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPaymentHistory.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblPaymentHistory);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel paymentPanel = createSectionPanel("Record Payment");
        mainPanel.add(paymentPanel);
        mainPanel.add(Box.createVerticalStrut(35));

        // ================= EXPENSES =================
        JLabel lblExpenses = new JLabel("Expenses History");
        lblExpenses.setForeground(Color.WHITE);
        lblExpenses.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblExpenses.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblExpenses);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel expensePanel = createSectionPanel("Add Expense");
        mainPanel.add(expensePanel);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(sidebar, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel createSectionPanel(String buttonText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // ===== SEARCH BAR (Only for Payment) =====
        JTextField txtSearch = new JTextField();
        if (buttonText.equals("Record Payment")) {
            txtSearch.setText("Enter phone number...");
            txtSearch.setBackground(new Color(40, 40, 55));
            txtSearch.setForeground(new Color(150, 150, 170));
            txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtSearch.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 90)),
                    new EmptyBorder(8, 10, 8, 10)
            ));
            txtSearch.setPreferredSize(new Dimension(250, 35));

            txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (txtSearch.getText().equals("Enter phone number...")) {
                        txtSearch.setText("");
                        txtSearch.setForeground(Color.WHITE);
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (txtSearch.getText().isEmpty()) {
                        txtSearch.setText("Enter phone number...");
                        txtSearch.setForeground(new Color(150, 150, 170));
                    }
                }
            });

            txtSearch.addActionListener(e -> {
                String phone = txtSearch.getText().trim();
                if (phone.isEmpty() || phone.equals("Enter phone number...")) {
                    loadAllPayments();
                } else {
                    loadPaymentsByPhone(phone);
                }
            });
        }

        JButton btnAction = createActionButton(buttonText, new Color(50, 90, 200));
        if (buttonText.equals("Record Payment")) {
            btnAction.addActionListener(e -> showRecordPaymentDialog());
        }

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        if (buttonText.equals("Record Payment")) {
            top.add(txtSearch, BorderLayout.WEST);
            top.add(btnAction, BorderLayout.EAST);
            panel.add(top, BorderLayout.NORTH);
        }

        // ===== TABLES =====
        if (buttonText.equals("Record Payment")) {
            // ---- Payments table ----
            String[] columns = {"ID", "Member Name", "Phone Number", "Email", "Amount", "Method", "Start Date", "End Date"};

            paymentModel = new DefaultTableModel(columns, 0);
            paymentTable = new JTable(paymentModel);
            styleTable(paymentTable);

            JScrollPane scrollPane = new JScrollPane(paymentTable);
            scrollPane.setPreferredSize(new Dimension(700, 180));
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 60)));
            scrollPane.getViewport().setBackground(new Color(30, 30, 45));

            // ===== DELETE PAYMENT BUTTON =====
            JButton btnDeletePayment = createActionButton("Delete Selected Payment", new Color(180, 50, 50));
            btnDeletePayment.addActionListener(e -> deleteSelectedPayment());

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
            bottomPanel.setOpaque(false);
            bottomPanel.add(btnDeletePayment);

            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(bottomPanel, BorderLayout.SOUTH);

            loadAllPayments();
        } else if (buttonText.equals("Add Expense")) {
            // ---- Expenses table ----
            String[] columns = {"ID", "Entity", "Cost", "Date"};
            DefaultTableModel expenseModel = new DefaultTableModel(columns, 0);
            expenseTable = new JTable(expenseModel);
            styleTable(expenseTable);

            JScrollPane scrollPane = new JScrollPane(expenseTable);
            scrollPane.setPreferredSize(new Dimension(700, 180));
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 60)));
            scrollPane.getViewport().setBackground(new Color(30, 30, 45));

            // ===== MONTH & YEAR FILTER + Add/Delete Buttons =====
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);

            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            filterPanel.setOpaque(false);

            JLabel lblFilter = new JLabel("Filter by Month & Year:");
            lblFilter.setForeground(Color.WHITE);
            lblFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JMonthChooser monthChooser = new JMonthChooser();
            JYearChooser yearChooser = new JYearChooser();

            JButton btnFilter = createActionButton("Filter", new Color(50, 90, 200));
            btnFilter.addActionListener(e -> {
                int month = monthChooser.getMonth() + 1;
                int year = yearChooser.getYear();
                loadExpensesForMonthYear(month, year);
            });

            filterPanel.add(lblFilter);
            filterPanel.add(monthChooser);
            filterPanel.add(yearChooser);
            filterPanel.add(btnFilter);

            // Right side buttons
            JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
            rightButtons.setOpaque(false);

            JButton btnAddExpense = createActionButton("Add Expense", new Color(50, 90, 200));
            btnAddExpense.addActionListener(e -> showAddExpenseDialog());

            JButton btnDeleteExpense = createActionButton("Delete Selected Expense", new Color(180, 50, 50));
            btnDeleteExpense.addActionListener(e -> deleteSelectedExpense());

            rightButtons.add(btnAddExpense);
            rightButtons.add(btnDeleteExpense);

            topPanel.add(filterPanel, BorderLayout.WEST);
            topPanel.add(rightButtons, BorderLayout.EAST);

            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

            java.util.Calendar cal = java.util.Calendar.getInstance();
            int month = cal.get(java.util.Calendar.MONTH) + 1;
            int year = cal.get(java.util.Calendar.YEAR);
            loadExpensesForMonthYear(month, year);
        }

        return panel;
    }

    // ======== Buttons, Labels, TextFields ========
    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        return btn;
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

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(180, 180, 200));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(4, 2, 4, 0));
        return lbl;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(40, 40, 55));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 90)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return field;
    }

    // ==================== DIALOGS ====================
    private void showRecordPaymentDialog() {
        final JDialog dialog = createDialog("Record Payment");

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(new Color(30, 30, 45));
        body.setBorder(new EmptyBorder(20, 24, 10, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.weightx = 1;
        int row = 0;

        // ===== SEARCH FIELD =====
        JLabel lblSearch = createLabel("Search Member (by phone):");
        gbc.gridy = row++;
        body.add(lblSearch, gbc);

        JTextField txtSearch = createTextField();
        gbc.gridy = row++;
        body.add(txtSearch, gbc);

        JButton btnFind = createActionButton("Find Member", new Color(50, 90, 200));
        gbc.gridy = row++;
        body.add(btnFind, gbc);

        // ===== MEMBER NAME =====
        JLabel lblName = createLabel("Member Name:");
        gbc.gridy = row++;
        body.add(lblName, gbc);

        JTextField txtName = createTextField();
        txtName.setEditable(false);
        gbc.gridy = row++;
        body.add(txtName, gbc);

        // ===== MEMBER EMAIL =====
        JLabel lblEmail = createLabel("Email:");
        gbc.gridy = row++;
        body.add(lblEmail, gbc);

        JTextField txtEmail = createTextField();
        txtEmail.setEditable(false);
        gbc.gridy = row++;
        body.add(txtEmail, gbc);

        // ===== PHONE =====
        JLabel lblPhone = createLabel("Phone Number:");
        gbc.gridy = row++;
        body.add(lblPhone, gbc);

        JTextField txtPhone = createTextField();
        txtPhone.setEditable(false);
        gbc.gridy = row++;
        body.add(txtPhone, gbc);

        // ===== SUBSCRIPTION PLAN =====
        JLabel lblPlan = createLabel("Subscription Plan:");
        gbc.gridy = row++;
        body.add(lblPlan, gbc);

        String[] plans = {
            "1 Day (3$)",
            "1 Month (25$)",
            "2 Months (45$)",
            "3 Months (65$)"
        };

        JComboBox<String> planBox = new JComboBox<>(plans);
        planBox.setBackground(new Color(40, 40, 55));
        planBox.setForeground(Color.WHITE);
        planBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = row++;
        body.add(planBox, gbc);

        // ===== PRICE LABEL =====
        JLabel lblPrice = createLabel("Price: $3");
        gbc.gridy = row++;
        body.add(lblPrice, gbc);

        // ===== PAYMENT METHOD =====
        JLabel lblMethod = createLabel("Payment Method:");
        gbc.gridy = row++;
        body.add(lblMethod, gbc);

        JComboBox<String> methodBox = new JComboBox<>(new String[]{"Cash", "Wish"});
        methodBox.setBackground(new Color(40, 40, 55));
        methodBox.setForeground(Color.WHITE);
        methodBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = row++;
        body.add(methodBox, gbc);

        // ========== FIND MEMBER ACTION ==============
        btnFind.addActionListener(e -> {
            String phone = txtSearch.getText().trim();
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "⚠️ Please enter a phone number!");
                return;
            }

            try (java.sql.Connection con = ConnectionProvider.con(); java.sql.PreparedStatement ps = con.prepareStatement(
                    "SELECT name, phone_number, email FROM users WHERE phone_number = ?")) {

                ps.setString(1, phone);
                java.sql.ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    txtName.setText(rs.getString("name"));
                    txtPhone.setText(rs.getString("phone_number"));
                    txtEmail.setText(rs.getString("email"));
                } else {
                    JOptionPane.showMessageDialog(dialog, "❌ No member found with this phone number!");
                    txtName.setText("");
                    txtPhone.setText("");
                    txtEmail.setText("");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage());
            }
        });

        // ===== PLAN CHANGE: UPDATE PRICE =====
        planBox.addActionListener(e -> {
            switch (planBox.getSelectedIndex()) {
                case 0 ->
                    lblPrice.setText("Price: $3");
                case 1 ->
                    lblPrice.setText("Price: $25");
                case 2 ->
                    lblPrice.setText("Price: $45");
                case 3 ->
                    lblPrice.setText("Price: $65");
            }
        });

        // ===== FOOTER =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(new Color(30, 30, 45));
        footer.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(new Color(60, 60, 80), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JButton btnCancel = createActionButton("Cancel", new Color(70, 70, 80));
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnSave = createActionButton("Record Payment", new Color(50, 90, 200));
        btnSave.addActionListener(e -> {
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            String name = txtName.getText().trim();

            if (phone.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "⚠️ Please find a member first!");
                return;
            }

            int days = switch (planBox.getSelectedIndex()) {
                case 0 ->
                    1;
                case 1 ->
                    30;
                case 2 ->
                    60;
                case 3 ->
                    90;
                default ->
                    30;
            };

            int price = switch (planBox.getSelectedIndex()) {
                case 0 ->
                    3;
                case 1 ->
                    25;
                case 2 ->
                    45;
                case 3 ->
                    65;
                default ->
                    25;
            };

            java.sql.Date start = new java.sql.Date(System.currentTimeMillis());
            java.sql.Date end = new java.sql.Date(System.currentTimeMillis() + (long) days * 24 * 60 * 60 * 1000);

            try (java.sql.Connection con = ConnectionProvider.con(); java.sql.PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO payments (phone_number, email, amount, payment_method, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)"
            )) {

                ps.setString(1, phone);
                ps.setString(2, email);     // <-- NEW
                ps.setBigDecimal(3, new java.math.BigDecimal(price));
                ps.setString(4, (String) methodBox.getSelectedItem());
                ps.setDate(5, start);
                ps.setDate(6, end);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(dialog, "✅ Payment recorded successfully!");
                    dialog.dispose();
                    loadAllPayments();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error saving payment: " + ex.getMessage());
            }
        });

        footer.add(btnCancel);
        footer.add(btnSave);

        dialog.add(body, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setSize(450, dialog.getHeight());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddExpenseDialog() {
        final JDialog dialog = createDialog("Add Expense");

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(new Color(30, 30, 45));
        body.setBorder(new EmptyBorder(20, 24, 10, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.weightx = 1;
        int row = 0;

        // ===== ENTITY FIELD =====
        JLabel lblEntity = createLabel("Entity:");
        gbc.gridy = row++;
        body.add(lblEntity, gbc);

        JTextField txtEntity = createTextField();
        gbc.gridy = row++;
        body.add(txtEntity, gbc);

        // ===== COST FIELD =====
        JLabel lblCost = createLabel("Cost:");
        gbc.gridy = row++;
        body.add(lblCost, gbc);

        JTextField txtCost = createTextField();
        gbc.gridy = row++;
        body.add(txtCost, gbc);

        // ===== FOOTER (BUTTONS) =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(new Color(30, 30, 45));
        footer.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(new Color(60, 60, 80), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JButton btnCancel = createActionButton("Cancel", new Color(70, 70, 80));
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnAdd = createActionButton("Add Expense", new Color(50, 90, 200));
        btnAdd.addActionListener(e -> {
            String entity = txtEntity.getText().trim();
            String costStr = txtCost.getText().trim();

            if (entity.isEmpty() || costStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "⚠️ Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                java.math.BigDecimal cost = new java.math.BigDecimal(costStr);

                // ===== DATABASE INSERT =====
                try (java.sql.Connection con = ConnectionProvider.con(); java.sql.PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO expenses (entity, cost, expense_date) VALUES (?, ?, CURDATE())"
                )) {

                    ps.setString(1, entity);
                    ps.setBigDecimal(2, cost);
                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(dialog, "✅ Expense added successfully!");
                dialog.dispose();

                // ===== REFRESH EXPENSE TABLE =====
                java.util.Calendar cal = java.util.Calendar.getInstance();
                int month = cal.get(java.util.Calendar.MONTH) + 1;
                int year = cal.get(java.util.Calendar.YEAR);
                loadExpensesForMonthYear(month, year);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "❌ Invalid cost format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(btnCancel);
        footer.add(btnAdd);

        dialog.add(body, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setSize(420, dialog.getHeight());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JDialog createDialog(String titleText) {
        final JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.getContentPane().setBackground(new Color(30, 30, 45));
        dialog.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 30, 45));
        header.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(new Color(60, 60, 80), 1, true),
                new EmptyBorder(14, 16, 12, 16)
        ));

        JLabel title = new JLabel(titleText);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton btnClose = new JButton("✕");
        btnClose.setForeground(new Color(200, 200, 210));
        btnClose.setBackground(new Color(45, 45, 60));
        btnClose.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dialog.dispose());

        header.add(title, BorderLayout.WEST);
        header.add(btnClose, BorderLayout.EAST);
        makeDialogDraggable(dialog, header);
        dialog.add(header, BorderLayout.NORTH);
        return dialog;
    }

    private JPanel createFooter(JDialog dialog, String mainButtonText) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(new Color(30, 30, 45));
        footer.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(new Color(60, 60, 80), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JButton btnCancel = createActionButton("Cancel", new Color(70, 70, 80));
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnMain = createActionButton(mainButtonText, new Color(50, 90, 200));
        btnMain.addActionListener(e -> dialog.dispose());

        footer.add(btnCancel);
        footer.add(btnMain);
        return footer;
    }

    private void makeDialogDraggable(JDialog dialog, JComponent dragArea) {
        final Point[] mouseDown = {null};
        dragArea.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                mouseDown[0] = e.getPoint();
            }
        });
        dragArea.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                Point curr = e.getLocationOnScreen();
                dialog.setLocation(curr.x - mouseDown[0].x, curr.y - mouseDown[0].y);
            }
        });
    }

    private void openPage(JFrame currentFrame, JFrame nextFrame) {
        currentFrame.dispose();  // close the current page
        nextFrame.setVisible(true);  // open the selected one
    }

    private void logout() {
        dispose();
        new Home().setVisible(true); // Open Home page instantly
    }
    // ===== Load all payments =====

    private void loadAllPayments() {
        paymentModel.setRowCount(0);

        try (java.sql.Connection con = ConnectionProvider.con(); java.sql.PreparedStatement ps = con.prepareStatement(
                "SELECT p.payment_id, u.name, u.phone_number, u.email, "
                + "p.amount, p.payment_method, p.start_date, p.end_date "
                + "FROM payments p JOIN users u ON p.phone_number = u.phone_number "
                + "ORDER BY p.payment_id DESC"
        )) {

            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                paymentModel.addRow(new Object[]{
                    rs.getInt("payment_id"),
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    rs.getBigDecimal("amount"),
                    rs.getString("payment_method"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading payments: " + e.getMessage());
        }
    }

// ===== Load payments by phone =====
    private void loadPaymentsByPhone(String phone) {
        paymentModel.setRowCount(0);

        try (java.sql.Connection con = ConnectionProvider.con(); java.sql.PreparedStatement ps = con.prepareStatement(
                "SELECT p.payment_id, u.name, u.phone_number, u.email, "
                + "p.amount, p.payment_method, p.start_date, p.end_date "
                + "FROM payments p JOIN users u ON p.phone_number = u.phone_number "
                + "WHERE u.phone_number LIKE ?"
        )) {

            ps.setString(1, "%" + phone + "%");
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                paymentModel.addRow(new Object[]{
                    rs.getInt("payment_id"),
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    rs.getBigDecimal("amount"),
                    rs.getString("payment_method"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading filtered payments: " + e.getMessage());
        }
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

    private void loadExpensesForMonthYear(int month, int year) {
        try (java.sql.Connection con = ConnectionProvider.con(); java.sql.PreparedStatement ps = con.prepareStatement(
                "SELECT expense_id, entity, cost, expense_date FROM expenses "
                + "WHERE MONTH(expense_date)=? AND YEAR(expense_date)=? ORDER BY expense_date DESC"
        )) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            java.sql.ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) expenseTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("expense_id"),
                    rs.getString("entity"),
                    rs.getBigDecimal("cost"),
                    rs.getDate("expense_date")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load expenses: " + ex.getMessage());
        }
    }

    private void deleteSelectedPayment() {
        int row = paymentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a payment to delete.");
            return;
        }

        int id = Integer.parseInt(paymentTable.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this payment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (java.sql.Connection con = ConnectionProvider.con(); java.sql.PreparedStatement ps = con.prepareStatement("DELETE FROM payments WHERE payment_id=?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                paymentModel.removeRow(row);
                JOptionPane.showMessageDialog(this, "✅ Payment deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting payment: " + ex.getMessage());
            }
        }
    }

    private void deleteSelectedExpense() {
        int row = expenseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select an expense to delete.");
            return;
        }

        int id = Integer.parseInt(expenseTable.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this expense?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (java.sql.Connection con = ConnectionProvider.con(); java.sql.PreparedStatement ps = con.prepareStatement("DELETE FROM expenses WHERE expense_id=?")) {
                ps.setInt(1, id);

                ps.executeUpdate();
                ((DefaultTableModel) expenseTable.getModel()).removeRow(row);
                JOptionPane.showMessageDialog(this, "✅ Expense deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting expense: " + ex.getMessage());
            }
        }
    }

    // ==================== MAIN ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Payment().setVisible(true));
    }
}
