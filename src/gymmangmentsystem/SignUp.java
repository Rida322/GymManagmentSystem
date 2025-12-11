package gymmangmentsystem;

import project.ConnectionProvider;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.*;

public class SignUp extends JFrame {

    public SignUp() {
        setTitle("Gym Management Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(20, 20, 30));

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

      // ===== NAME =====
JLabel lblName = new JLabel("Full Name:");
lblName.setForeground(new Color(180, 180, 200));
lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
gbc.gridy = row++;
mainPanel.add(lblName, gbc);

JPanel namePanel = new JPanel(new BorderLayout(10, 0));
namePanel.setBackground(new Color(40, 40, 55));
namePanel.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90)));

JLabel iconName = new JLabel();
try {
    ImageIcon userIcon = new ImageIcon(getClass().getResource("/Images/fullname.png"));
    Image scaled = userIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    iconName.setIcon(new ImageIcon(scaled));
} catch (Exception e) {
    System.out.println("User icon not found.");
}
iconName.setHorizontalAlignment(SwingConstants.CENTER);
iconName.setBorder(new EmptyBorder(0, 8, 0, 8));

JTextField txtName = new JTextField();
txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
txtName.setBackground(new Color(40, 40, 55));
txtName.setForeground(Color.WHITE);
txtName.setCaretColor(Color.WHITE);
txtName.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

namePanel.add(iconName, BorderLayout.WEST);
namePanel.add(txtName, BorderLayout.CENTER);
gbc.gridy = row++;
mainPanel.add(namePanel, gbc);


     // ===== PHONE =====
JLabel lblPhone = new JLabel("Phone Number:");
lblPhone.setForeground(new Color(180, 180, 200));
lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
gbc.gridy = row++;
mainPanel.add(lblPhone, gbc);

JPanel phonePanel = new JPanel(new BorderLayout(10, 0));
phonePanel.setBackground(new Color(40, 40, 55));
phonePanel.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90)));

JLabel iconPhone = new JLabel();
try {
    ImageIcon phoneIcon = new ImageIcon(getClass().getResource("/Images/phonenumber.png"));
    Image scaled = phoneIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    iconPhone.setIcon(new ImageIcon(scaled));
} catch (Exception e) {
    System.out.println("Phone icon not found.");
}
iconPhone.setHorizontalAlignment(SwingConstants.CENTER);
iconPhone.setBorder(new EmptyBorder(0, 8, 0, 8));

JTextField txtPhone = new JTextField();
txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
txtPhone.setBackground(new Color(40, 40, 55));
txtPhone.setForeground(Color.WHITE);
txtPhone.setCaretColor(Color.WHITE);
txtPhone.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

phonePanel.add(iconPhone, BorderLayout.WEST);
phonePanel.add(txtPhone, BorderLayout.CENTER);
gbc.gridy = row++;
mainPanel.add(phonePanel, gbc);


       // ===== EMAIL =====
JLabel lblEmail = new JLabel("Email:");
lblEmail.setForeground(new Color(180, 180, 200));
lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
gbc.gridy = row++;
mainPanel.add(lblEmail, gbc);

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


      // ===== PASSWORD =====
JLabel lblPassword = new JLabel("Password:");
lblPassword.setForeground(new Color(180, 180, 200));
lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
gbc.gridy = row++;
mainPanel.add(lblPassword, gbc);

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
// ===== CONFIRM PASSWORD =====
JLabel lblConfirm = new JLabel("Confirm Password:");
lblConfirm.setForeground(new Color(180, 180, 200));
lblConfirm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
gbc.gridy = row++;
mainPanel.add(lblConfirm, gbc);

JPanel confirmPanel = new JPanel(new BorderLayout(10, 0));
confirmPanel.setBackground(new Color(40, 40, 55));
confirmPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90)));

JLabel iconConfirm = new JLabel(iconPass.getIcon()); // reuse same icon
iconConfirm.setHorizontalAlignment(SwingConstants.CENTER);
iconConfirm.setBorder(new EmptyBorder(0, 8, 0, 8));

JPasswordField txtConfirmPassword = new JPasswordField();
txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
txtConfirmPassword.setBackground(new Color(40, 40, 55));
txtConfirmPassword.setForeground(Color.WHITE);
txtConfirmPassword.setCaretColor(Color.WHITE);
txtConfirmPassword.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

confirmPanel.add(iconConfirm, BorderLayout.WEST);
confirmPanel.add(txtConfirmPassword, BorderLayout.CENTER);

gbc.gridy = row++;
mainPanel.add(confirmPanel, gbc);
JCheckBox chkShowPassword = new JCheckBox("Show Password");
chkShowPassword.setForeground(new Color(200, 200, 220));
chkShowPassword.setBackground(new Color(30, 30, 45));
chkShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));

chkShowPassword.addActionListener(e -> {
    if (chkShowPassword.isSelected()) {
        txtPassword.setEchoChar((char) 0);
        txtConfirmPassword.setEchoChar((char) 0);
    } else {
        txtPassword.setEchoChar('•');
        txtConfirmPassword.setEchoChar('•');
    }
});

gbc.gridy = row++;
mainPanel.add(chkShowPassword, gbc);


// ===== SIGN UP BUTTON =====
JButton btnSignUp = new JButton("Sign Up");
btnSignUp.setFont(new Font("Segoe UI", Font.BOLD, 16));
btnSignUp.setBackground(new Color(50, 90, 200));
btnSignUp.setForeground(Color.WHITE);
btnSignUp.setFocusPainted(false);
btnSignUp.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
btnSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));

btnSignUp.addActionListener(e -> 
    handleSignUp(txtName, txtPhone, txtEmail, txtPassword, txtConfirmPassword)
);


gbc.gridy = row++;
gbc.anchor = GridBagConstraints.CENTER;
mainPanel.add(btnSignUp, gbc);



        // ===== BACK TO HOME =====
        JLabel lblBack = new JLabel("← Back to home");
        lblBack.setForeground(new Color(160, 160, 170));
        lblBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                goBackToLogin();
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

    private void goBackToLogin() {
        dispose();
        new Login().setVisible(true);
    }

    // ===== SIGNUP LOGIC =====
    private void handleSignUp(
        JTextField txtName,
        JTextField txtPhone,
        JTextField txtEmail,
        JPasswordField txtPassword,
        JPasswordField txtConfirmPassword)
 {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

if (!password.equals(confirmPassword)) {
    JOptionPane.showMessageDialog(this,
            "Passwords do not match!",
            "Password Error",
            JOptionPane.ERROR_MESSAGE);
    return;
}


        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Lebanese phone validation (03, 70, 71, 76, 78, 79, 81)
if (!phone.matches("^(03|70|71|76|78|79|81)[0-9]{6}$")) {
    JOptionPane.showMessageDialog(this,
            "Invalid Lebanese phone number!\n\n" +
            "It must start with:\n03, 70, 71, 76, 78, 79, or 81\n\n" +
            "Example: 76123456",
            "Invalid Phone",
            JOptionPane.WARNING_MESSAGE);
    return;
}

       String emailRegex = "^[a-z0-9._%+-]+@gmail\\.com$";

        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = ConnectionProvider.con()) {
            // Check for duplicate phone or email
            PreparedStatement check = con.prepareStatement("SELECT COUNT(*) FROM users WHERE email=? OR phone_number=?");
            check.setString(1, email);
            check.setString(2, phone);
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Email or phone number already exists!", "Duplicate", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Insert new user
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (name, phone_number, email, password) VALUES (?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, password);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new Login().setVisible(true);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Email or phone number already exists!", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp().setVisible(true));
    }
}
