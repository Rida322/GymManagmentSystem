package gymmangmentsystem;

import project.ConnectionProvider;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.*;

public class Login extends JFrame {
int userId;   // from login


public Login() {
    setTitle("Gym Management Login");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500, 470);
    setLocationRelativeTo(null);
    setResizable(true); // ✅ allow resize (maximize/minimize)
    setLayout(new BorderLayout());
    getContentPane().setBackground(new Color(20, 20, 30));

    // ===== MAIN PANEL =====
    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(new Color(30, 30, 45));
    mainPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(5, 0, 5, 0);
    gbc.gridx = 0;
    gbc.weightx = 1;
    int row = 0;

    // ===== ICON =====
    JLabel lblGymIcon = new JLabel();
    try {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/gym.png"));
        Image scaled = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        lblGymIcon.setIcon(new ImageIcon(scaled));
    } catch (Exception e) {
        System.out.println("Gym icon not found.");
    }
    gbc.gridy = row++;
    gbc.anchor = GridBagConstraints.CENTER;
    mainPanel.add(lblGymIcon, gbc);

    // ===== TITLE =====
    JLabel lblTitle = new JLabel("Gym Management System");
    lblTitle.setForeground(Color.WHITE);
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
    gbc.gridy = row++;
    mainPanel.add(lblTitle, gbc);

    gbc.anchor = GridBagConstraints.WEST;

    // ===== EMAIL LABEL =====
    JLabel lblEmail = new JLabel("Email:");
    lblEmail.setForeground(new Color(180, 180, 200));
    lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    gbc.gridy = row++;
    mainPanel.add(lblEmail, gbc);

    // ===== EMAIL FIELD =====
    JPanel emailPanel = new JPanel(new BorderLayout(10, 0));
    emailPanel.setBackground(new Color(40, 40, 55));
    emailPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90)));

    JLabel iconMail = new JLabel();
    try {
        ImageIcon mailIcon = new ImageIcon(getClass().getResource("/Images/mail.png"));
        Image scaled = mailIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        iconMail.setIcon(new ImageIcon(scaled));
    } catch (Exception e) {
        System.out.println("Mail icon not found.");
    }
    iconMail.setHorizontalAlignment(SwingConstants.CENTER);
    iconMail.setBorder(new EmptyBorder(0, 8, 0, 8));

    JTextField txtEmail = new JTextField();
    txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    txtEmail.setBackground(new Color(40, 40, 55));
    txtEmail.setForeground(Color.WHITE);
    txtEmail.setCaretColor(Color.WHITE);
    txtEmail.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

    emailPanel.add(iconMail, BorderLayout.WEST);
    emailPanel.add(txtEmail, BorderLayout.CENTER);
    gbc.gridy = row++;
    mainPanel.add(emailPanel, gbc);

    // ===== PASSWORD LABEL =====
    JLabel lblPassword = new JLabel("Password:");
    lblPassword.setForeground(new Color(180, 180, 200));
    lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    gbc.gridy = row++;
    mainPanel.add(lblPassword, gbc);

    // ===== PASSWORD FIELD =====
    JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
    passwordPanel.setBackground(new Color(40, 40, 55));
    passwordPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90)));

    JLabel iconPass = new JLabel();
    try {
        ImageIcon passIcon = new ImageIcon(getClass().getResource("/Images/password.png"));
        Image scaled = passIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        iconPass.setIcon(new ImageIcon(scaled));
    } catch (Exception e) {
        System.out.println("Password icon not found.");
    }
    iconPass.setHorizontalAlignment(SwingConstants.CENTER);
    iconPass.setBorder(new EmptyBorder(0, 8, 0, 8));

    JPasswordField txtPassword = new JPasswordField();
    txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    txtPassword.setBackground(new Color(40, 40, 55));
    txtPassword.setForeground(Color.WHITE);
    txtPassword.setCaretColor(Color.WHITE);
    txtPassword.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

    passwordPanel.add(iconPass, BorderLayout.WEST);
    passwordPanel.add(txtPassword, BorderLayout.CENTER);
    gbc.gridy = row++;
    mainPanel.add(passwordPanel, gbc);

    // ===== LOGIN BUTTON =====
    JButton btnLogin = new JButton("Login");
    btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
    btnLogin.setBackground(new Color(50, 90, 200));
    btnLogin.setForeground(Color.WHITE);
    btnLogin.setFocusPainted(false);
    btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnLogin.addActionListener(e -> handleLogin(txtEmail, txtPassword));
    gbc.gridy = row++;
    gbc.anchor = GridBagConstraints.CENTER;
    mainPanel.add(btnLogin, gbc);

    // ===== SIGN UP LINK =====
    JPanel linkPanel = new JPanel();
    linkPanel.setBackground(new Color(30, 30, 45));

    JLabel lblNoAccount = new JLabel("Don’t have an account? ");
    lblNoAccount.setForeground(new Color(170, 170, 180));

    JLabel lblSignUp = new JLabel("Sign Up");
    lblSignUp.setForeground(new Color(80, 140, 255));
    lblSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
    lblSignUp.setFont(new Font("Segoe UI", Font.BOLD, 14));
    lblSignUp.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            dispose();
            new SignUp().setVisible(true);
        }
    });

    linkPanel.add(lblNoAccount);
    linkPanel.add(lblSignUp);
    gbc.gridy = row++;
    mainPanel.add(linkPanel, gbc);

    // ===== BACK TO HOME =====
    JLabel lblBack = new JLabel("← Back to home");
    lblBack.setForeground(new Color(160, 160, 170));
    lblBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
    lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            goBackToHome();
        }
    });
    gbc.gridy = row++;
    gbc.anchor = GridBagConstraints.CENTER;
    mainPanel.add(lblBack, gbc);

    JPanel container = new JPanel(new GridBagLayout());
    container.setBackground(new Color(20, 20, 30));
    container.add(mainPanel);
    add(container, BorderLayout.CENTER);
}

// ===== BACK TO HOME =====
private void goBackToHome() {
    dispose();
    new Home().setVisible(true);
}

// ===== HANDLE LOGIN =====
private void handleLogin(JTextField txtEmail, JPasswordField txtPassword) {
    String email = txtEmail.getText().trim();
    String password = new String(txtPassword.getPassword()).trim();

    if (email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (Connection con = ConnectionProvider.con()) {

        // ==========================
        // 1️⃣ CHECK ADMIN TABLE
        // ==========================
        PreparedStatement psAdmin = con.prepareStatement(
                "SELECT * FROM admin WHERE email = ? AND password = ?"
        );
        psAdmin.setString(1, email);
        psAdmin.setString(2, password);

        ResultSet rsAdmin = psAdmin.executeQuery();

        if (rsAdmin.next()) {
            // ADMIN FOUND
            JOptionPane.showMessageDialog(this, "Welcome Admin!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new Dashboard().setVisible(true);
            return;
        }

        // ==========================
        // 2️⃣ CHECK USERS TABLE
        // ==========================
        PreparedStatement psUser = con.prepareStatement(
                "SELECT * FROM users WHERE email = ? AND password = ?"
        );
        psUser.setString(1, email);
        psUser.setString(2, password);

        ResultSet rsUser = psUser.executeQuery();

      if (rsUser.next()) {
    // USER FOUND
    userId = rsUser.getInt("id");  // ✅ GET USER ID FROM DATABASE

    JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
    dispose();
 SwingUtilities.invokeLater(() -> new MemberDashboard(userId).setVisible(true));
 // ✅ PASS REAL USER ID
    return;
}


        // ==========================
        // 3️⃣ NO ACCOUNT FOUND
        // ==========================
        JOptionPane.showMessageDialog(this, "Invalid email or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}




public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Login().setVisible(true));
}


}

