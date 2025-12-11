
package gymmangmentsystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import project.ConnectionProvider;

public class Members extends JFrame {
private DefaultTableModel model;
    public Members() {
        setTitle("Member Management");
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

        // --- Logout Button ---
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
         btnLogout.addActionListener(e -> logout());

       


        // ====== MAIN PANEL ======
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(20, 20, 30));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- Title ---
        JLabel lblTitle = new JLabel("Member Management");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // --- TOP CARDS ---
        JPanel topCards = new JPanel(new GridLayout(1, 3, 25, 0));
        topCards.setOpaque(false);

       // ======== FETCH LIVE DATA FROM DATABASE ========
int totalMembers = getTotalMembersCount();
int activeMembers = getActiveMembersCount();
int expiredMembers = getExpiringSoonCount();

// ======== ADD DYNAMIC CARDS ========
topCards.add(createInfoCard("Total Members", String.valueOf(totalMembers), "/Images/group.png"));
topCards.add(createInfoCard("Active Members", String.valueOf(activeMembers), "/Images/Active.png"));
topCards.add(createInfoCard("Expiring Soon", String.valueOf(expiredMembers), "/Images/expired.png"));

        mainPanel.add(topCards, BorderLayout.NORTH);

        // --- CENTER CONTENT ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        // --- Search and Buttons ---
        JPanel topTools = new JPanel(new BorderLayout());
        topTools.setOpaque(false);

      JTextField txtSearch = new JTextField("Enter phone number...");
txtSearch.setBackground(new Color(40, 40, 55));
txtSearch.setForeground(new Color(150, 150, 170));
txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
txtSearch.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(70, 70, 90)),
        new EmptyBorder(8, 10, 8, 10)
));
txtSearch.setPreferredSize(new Dimension(250, 35));

// ===== Placeholder behavior =====
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
// ===== Search on ENTER key =====
txtSearch.addActionListener(e -> {
    String phone = txtSearch.getText().trim();

    // ignore if placeholder or empty
    if (phone.isEmpty() || phone.equals("Enter phone number...")) {
        JOptionPane.showMessageDialog(this, "⚠️ Please enter a phone number to search!");
        return;
    }

    searchMembers(phone);
});



        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        Color blueColor = new Color(50, 90, 200);
       
        JButton btnEdit = createActionButton("Edit Member", blueColor);

        btnEdit.addActionListener(e -> showEditMemberDialog());

    
        buttonPanel.add(btnEdit);

        topTools.add(txtSearch, BorderLayout.WEST);
        topTools.add(buttonPanel, BorderLayout.EAST);

        // --- TABLE ---
       // === TABLE SETTINGS ===
String[] columns = {"Name", "Phone","Email","Password", "Status"};
 model = new DefaultTableModel(columns, 0);
JTable table = new JTable(model);
table.setBackground(new Color(30, 30, 45));
table.setForeground(Color.WHITE);
table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
table.setRowHeight(28);
table.setGridColor(new Color(50, 50, 70));
table.setShowGrid(true);
table.setSelectionBackground(new Color(50, 90, 200));
table.setSelectionForeground(Color.WHITE);
table.getTableHeader().setBackground(new Color(35, 35, 50));
table.getTableHeader().setForeground(Color.WHITE);
table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

// ✅ STEP 4: Add this renderer here
table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 🔥 Correct Status column index (4)
        String status = (String) table.getValueAt(row, 4);

        if ("Expired".equalsIgnoreCase(status)) {
            c.setForeground(Color.RED);
        } else if ("Active".equalsIgnoreCase(status)) {
            c.setForeground(new Color(0, 200, 120)); // green
        } else {
            c.setForeground(Color.WHITE);
        }

        // background
        if (isSelected) {
            c.setBackground(new Color(50, 90, 200));
        } else {
            c.setBackground(new Color(30, 30, 45));
        }

        return c;
    }
});

loadMembers(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(30, 30, 45));

        centerPanel.add(topTools, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

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
            System.out.println("Card icon not found: " + iconPath);
        }

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(180, 180, 200));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));

        top.add(iconLabel);
        top.add(lblTitle);

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setHorizontalAlignment(SwingConstants.LEFT);
        lblValue.setBorder(new EmptyBorder(10, 5, 0, 0));

        panel.add(top, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

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
        return field;
    }

 

    // ====================== EDIT MEMBER DIALOG ==========================
    private void showEditMemberDialog() {
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
        JLabel title = new JLabel("Edit Member");
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

        // ---- Search Row ----
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        searchRow.setBackground(new Color(30, 30, 45));
        searchRow.setBorder(new EmptyBorder(0, 24, 0, 24));

        JLabel lblSearch = new JLabel("Search Member:");
        lblSearch.setForeground(new Color(180, 180, 200));
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField txtSearch = createTextField();
        txtSearch.setPreferredSize(new Dimension(180, 34));

        JButton btnFind = createActionButton("Find", new Color(50, 90, 200));
        

        searchRow.add(lblSearch);
        searchRow.add(txtSearch);
        searchRow.add(btnFind);

        // ---- Body ----
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(new Color(30, 30, 45));
        body.setBorder(new EmptyBorder(10, 24, 10, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.weightx = 1;
        int row = 0;

        JLabel lblName = new JLabel("Full Name");
        lblName.setForeground(new Color(180, 180, 200));
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = row++;
        body.add(lblName, gbc);

        JTextField txtName = createTextField();
        gbc.gridy = row++;
        body.add(txtName, gbc);

        JLabel lblContact = new JLabel("Contact");
        lblContact.setForeground(new Color(180, 180, 200));
        lblContact.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = row++;
        body.add(lblContact, gbc);

        JTextField txtContact = createTextField();
        gbc.gridy = row++;
        body.add(txtContact, gbc);
        
         JLabel lblemail = new JLabel("Email");
        lblemail.setForeground(new Color(180, 180, 200));
        lblemail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = row++;
        body.add(lblemail, gbc);
        
        JTextField txtEmail = createTextField();
        gbc.gridy = row++;
        body.add(txtEmail, gbc);
        
           JLabel lblpassword = new JLabel("Password");
        lblpassword.setForeground(new Color(180, 180, 200));
        lblpassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = row++;
        body.add(lblpassword, gbc);
        
        JTextField txtPassword = createTextField();
        gbc.gridy = row++;
        body.add(txtPassword, gbc);


        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(new Color(30, 30, 45));
        footer.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(new Color(60, 60, 80), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JButton btnCancel = createActionButton("Cancel", new Color(70, 70, 80));
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnSave = createActionButton("Save Changes", new Color(50, 90, 200));
        btnSave.addActionListener(e -> {
    String oldPhone = txtSearch.getText().trim();
    String newName = txtName.getText().trim();
    String newPhone = txtContact.getText().trim();
     String email = txtEmail.getText().trim();
       String password = txtPassword.getText().trim();

    if (oldPhone.isEmpty() || newName.isEmpty() || newPhone.isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "⚠️ Please fill all fields!");
        return;
    }

    updateMember(oldPhone, newName, newPhone,email,password);
    dialog.dispose();
});
btnFind.addActionListener(e -> {
    String searchPhone = txtSearch.getText().trim();

    if (searchPhone.isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "⚠️ Please enter a phone number to search!");
        return;
    }

    String[] member = getMemberByPhone(searchPhone);

    if (member != null) {
        txtName.setText(member[0]);        // name
        txtContact.setText(member[1]);     // phone
        txtEmail.setText(member[2]);       // email
        txtPassword.setText(member[3]);    // password
    } else {
        JOptionPane.showMessageDialog(dialog, "❌ Member not found!");
        txtName.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }
});



        btnSave.addActionListener(e -> dialog.dispose());

        footer.add(btnCancel);
        footer.add(btnSave);

        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(new Color(30, 30, 45));
        shell.setBorder(new javax.swing.border.LineBorder(new Color(60, 60, 80), 1, true));
        shell.add(header, BorderLayout.NORTH);
        shell.add(searchRow, BorderLayout.CENTER);
        shell.add(body, BorderLayout.SOUTH);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(30, 30, 45));
        outer.add(shell, BorderLayout.CENTER);
        outer.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(outer);
        dialog.pack();
        dialog.setSize(new Dimension(Math.max(480, dialog.getWidth()), dialog.getHeight()));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
   

    // ---- Make undecorated dialog draggable ----
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
    // ======= METHOD TO NAVIGATE TO DIFFERENT PAGES =======
private void openPage(JFrame currentFrame, JFrame nextFrame) {
    currentFrame.dispose();  // close the current page
    nextFrame.setVisible(true);  // open the selected one
}
 private void logout() {
        dispose(); 
        new Home().setVisible(true); // Open Home page instantly
    }
 private String getMemberStatus(String phoneNumber) {
    String status = "No Payment";
    try (java.sql.Connection con = ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT end_date FROM payments WHERE phone_number = ? ORDER BY end_date DESC LIMIT 1"
         )) {

        ps.setString(1, phoneNumber);
        java.sql.ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            java.sql.Date endDate = rs.getDate("end_date");
            java.util.Date today = new java.util.Date();

            if (endDate != null && endDate.after(today)) {
                status = "Active";
            } else {
                status = "Expired";
            }
        }

    } catch (Exception e) {
        System.out.println("Error checking status: " + e.getMessage());
    }
    return status;
}
private void loadMembers(DefaultTableModel model) {
    model.setRowCount(0);

    try (java.sql.Connection con = ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement("SELECT name, phone_number,email,password FROM users");
         java.sql.ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String name = rs.getString("name");
            String phone = rs.getString("phone_number");
             String email = rs.getString("email");
              String password = rs.getString("password");
            String status = getMemberStatus(phone); // ✅ new logic

            model.addRow(new Object[]{name, phone,email,password, status});
        }

    } catch (Exception e) {
        System.out.println("Error loading members: " + e.getMessage());
    }
}


// ===== GET MEMBER INFO BY PHONE =====
private String[] getMemberByPhone(String phone) {
    String[] users = null;

    try (java.sql.Connection con = ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT name, phone_number,email,password FROM users WHERE phone_number = ?"
         )) {

        ps.setString(1, phone);
        
        java.sql.ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            users = new String[] { rs.getString("name"), rs.getString("phone_number"), rs.getString("email"), rs.getString("password"), };
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "⚠️ Error loading member: " + e.getMessage());
    }

    return users; // null if not found
}
// ===== UPDATE MEMBER INFO =====
private void updateMember(String oldPhone, String newName, String newPhone,String email,String password) {
    try (java.sql.Connection con = ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "UPDATE users SET name = ?, phone_number = ?,email= ? ,password= ? WHERE phone_number = ?"
         )) {

        ps.setString(1, newName);
        ps.setString(2, newPhone);   
        ps.setString(3,email);
         ps.setString(4,password);
          ps.setString(5, oldPhone);
        

        int result = ps.executeUpdate();

        if (result > 0) {
            JOptionPane.showMessageDialog(this, "✅ Member updated successfully!");
           
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ Member not found!");
        }

    } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
        JOptionPane.showMessageDialog(this, "❌ Phone number already exists!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "⚠️ Error updating member: " + e.getMessage());
    }
}
// ===== SEARCH MEMBER BY PHONE OR NAME =====
// ===== SEARCH MEMBER BY PHONE OR NAME =====
private void searchMembers(String keyword) {
    model.setRowCount(0); // clear table

    try (java.sql.Connection con = ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT name, phone_number, email, password FROM users " +
             "WHERE phone_number LIKE ? OR name LIKE ? OR email LIKE ?"
         )) {

        String searchPattern = "%" + keyword + "%";

        ps.setString(1, searchPattern); // phone_number
        ps.setString(2, searchPattern); // name
        ps.setString(3, searchPattern); // email   ✅ you forgot this one

        java.sql.ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String name     = rs.getString("name");
            String phone    = rs.getString("phone_number");
            String email    = rs.getString("email");
            String password = rs.getString("password");
            String status   = getMemberStatus(phone);   // uses payments table

            model.addRow(new Object[]{name, phone, email, password, status});
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "⚠️ No members found for: " + keyword);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "⚠️ Search error: " + e.getMessage());
    }
}

// ======== DATABASE METHODS FOR DASHBOARD ========
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

private int getTotalMembersCount() {
    int count = 0;
    try (java.sql.Connection con = project.ConnectionProvider.con();
         java.sql.PreparedStatement ps = con.prepareStatement(
             "SELECT COUNT(*) FROM users ")) {
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) count = rs.getInt(1);
    } catch (Exception e) {
        System.out.println("Error loading new signups: " + e.getMessage());
    }
    return count;
}



    // ============================== MAIN =====================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Members().setVisible(true));
    }
}


