package gymmangmentsystem;

import project.ConnectionProvider;

import java.awt.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AIWorkout extends JFrame {

    private int userId;

    private JTextField txtWeight, txtHeight, txtAge;
    private JComboBox<String> cmbGender, cmbGoal, cmbLevel;
    private JTextArea output;

    public AIWorkout(int userId) {
        this.userId = userId;

        setTitle("AI Workout Generator");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(25, 25, 35));

        // ===== SIDEBAR =====
        JPanel sidebar = buildSidebar("ai");
        add(sidebar, BorderLayout.WEST);

        // ===== MAIN CONTENT (LEFT FORM + RIGHT AI PANEL) =====
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(25, 25, 35));
        add(content, BorderLayout.CENTER);

        // LEFT: user info form
        JPanel left = buildLeftFormPanel();
        content.add(left, BorderLayout.WEST);

        // RIGHT: AI coach output
        JPanel right = buildRightOutputPanel();
        content.add(right, BorderLayout.CENTER);

        // Load existing info (if user already used AI once)
        loadExistingInfo();
    }

    // --------------------------------------------------------------------
    //  UI BUILDERS
    // --------------------------------------------------------------------

    private JPanel buildSidebar(String activePage) {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(20, 20, 30));
        sidebar.setPreferredSize(new Dimension(230, getHeight()));
        sidebar.setLayout(new BorderLayout());

        // --- Top logo / title
        JPanel top = new JPanel();
        top.setBackground(new Color(20, 20, 30));
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel lblIcon = new JLabel("🤖", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblApp = new JLabel("Member Panel");
        lblApp.setForeground(Color.WHITE);
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblApp.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(lblIcon);
        top.add(Box.createVerticalStrut(8));
        top.add(lblApp);

        sidebar.add(top, BorderLayout.NORTH);

        // --- Menu buttons
        JPanel menu = new JPanel();
        menu.setBackground(new Color(20, 20, 30));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(10, 15, 10, 15));

        JButton btnDashboard = createSidebarButton(
                "Dashboard",
                "/Images/dashboard.png",
                activePage.equals("dashboard")
        );
        btnDashboard.addActionListener(e -> {
            new MemberDashboard(userId).setVisible(true);
            dispose();
        });

        JButton btnAIWorkout = createSidebarButton(
                "AI Workout Plan",
                "/Images/workouts.png",
                activePage.equals("ai")
        );
        // already here – no navigation

        JButton btnProgress = createSidebarButton(
                "Progress & History",
                "/Images/progress.png",
                activePage.equals("progress")
        );
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
        // ===== LOGOUT BUTTON AT BOTTOM =====
JPanel bottom = new JPanel();
bottom.setBackground(new Color(20, 20, 30));
bottom.setBorder(new EmptyBorder(10, 15, 20, 15));
bottom.setLayout(new BorderLayout());

JButton btnLogout = createSidebarButton("Logout", "/Images/logout.png", false);
btnLogout.addActionListener(e -> handleLogout());

bottom.add(btnLogout, BorderLayout.SOUTH);
sidebar.add(bottom, BorderLayout.SOUTH);


        return sidebar;
    }

    private JButton createSidebarButton(String text, String iconPath, boolean active) {
        Icon icon = loadIcon(iconPath, 22, 22);
        JButton btn = new JButton(text, icon);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setMaximumSize(new Dimension(220, 42));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (active) {
            btn.setBackground(new Color(60, 120, 250));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(30, 30, 45));
            btn.setForeground(new Color(220, 220, 230));
        }

        return btn;
    }

    private Icon loadIcon(String path, int w, int h) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url == null) {
                System.out.println("Icon not found: " + path);
                return null;
            }
            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception ex) {
            System.out.println("Error loading icon: " + path + " -> " + ex.getMessage());
            return null;
        }
    }

    private JPanel buildLeftFormPanel() {
        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(320, getHeight()));
        left.setBackground(new Color(30, 30, 45));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel(" AI Workout Plan");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "<html><span style='color:#C0C0D0'>Enter your info and let the AI coach build your plan.</span></html>"
        );
        subtitle.setForeground(new Color(190, 190, 210));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(title);
        left.add(Box.createVerticalStrut(4));
        left.add(subtitle);
        left.add(Box.createVerticalStrut(18));

        txtWeight = buildInput(left, "Weight (kg):");
        txtHeight = buildInput(left, "Height (cm):");
        txtAge = buildInput(left, "Age:");

        cmbGender = buildCombo(left, "Gender:", new String[]{"Male", "Female"});
        cmbGoal = buildCombo(left, "Goal:", new String[]{"Build Muscle", "Lose Fat", "Stay Fit"});
        cmbLevel = buildCombo(left, "Experience:", new String[]{"Beginner", "Intermediate", "Advanced"});
        
        // ===== Workout Name =====
JLabel lblWorkoutName = new JLabel("Workout Name:");
lblWorkoutName.setForeground(Color.WHITE);
lblWorkoutName.setFont(new Font("Segoe UI", Font.PLAIN, 15));
lblWorkoutName.setAlignmentX(Component.LEFT_ALIGNMENT);

JTextField txtWorkoutName = new JTextField();
txtWorkoutName.setMaximumSize(new Dimension(280, 30));
txtWorkoutName.setBackground(new Color(40, 40, 55));
txtWorkoutName.setForeground(Color.WHITE);
txtWorkoutName.setCaretColor(Color.WHITE);
txtWorkoutName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
txtWorkoutName.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));

// Add to panel
left.add(lblWorkoutName);
left.add(txtWorkoutName);
left.add(Box.createVerticalStrut(10));
  
        JButton btnGenerate = new JButton("Generate Workout");
        btnGenerate.setBackground(new Color(60, 120, 255));
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGenerate.setFocusPainted(false);
        btnGenerate.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerate.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnGenerate.addActionListener(e -> generateWorkoutPlan());
           
        left.add(Box.createVerticalStrut(20));
        left.add(btnGenerate);
        // Log workout button
JButton btnLogWorkout = new JButton("Log This Workout");
btnLogWorkout.setBackground(new Color(0, 180, 100));
btnLogWorkout.setForeground(Color.WHITE);
btnLogWorkout.setFont(new Font("Segoe UI", Font.BOLD, 15));
btnLogWorkout.setFocusPainted(false);
btnLogWorkout.setAlignmentX(Component.LEFT_ALIGNMENT);
btnLogWorkout.setCursor(new Cursor(Cursor.HAND_CURSOR));
btnLogWorkout.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

left.add(Box.createVerticalStrut(10));
left.add(btnLogWorkout);
btnLogWorkout.addActionListener(e -> {
    String name = txtWorkoutName.getText().trim();
    String plan = output.getText().trim();

    if (name.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a workout name.");
        return;
    }

    if (plan.isEmpty() || plan.startsWith("Fill your information")) {
        JOptionPane.showMessageDialog(this, "Generate a workout before saving it.");
        return;
    }

    saveWorkoutHistory(name);
});



        return left;
    }

    private JPanel buildRightOutputPanel() {
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(new Color(25, 25, 35));
        right.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 25, 35));

        JLabel lblTitle = new JLabel("Your AI Coach");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel lblHint = new JLabel(
                "<html><span style='color:#B0B0C5'>The plan below is generated live using the OpenAI API.</span></html>");
        lblHint.setForeground(new Color(180, 180, 210));
        lblHint.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        header.add(lblTitle, BorderLayout.NORTH);
        header.add(lblHint, BorderLayout.SOUTH);

        right.add(header, BorderLayout.NORTH);

        // Output area
        output = new JTextArea();
        output.setEditable(false);
        output.setForeground(Color.WHITE);
        output.setBackground(new Color(25, 25, 35));
        output.setFont(new Font("Consolas", Font.PLAIN, 15));
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setMargin(new Insets(10, 10, 10, 10));
        output.setText("Fill your information on the left and press \"Generate Workout\".");

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 70)));

        right.add(scroll, BorderLayout.CENTER);

        return right;
    }

    private JTextField buildInput(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txt = new JTextField();
        txt.setMaximumSize(new Dimension(280, 30));
        txt.setBackground(new Color(40, 40, 55));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));

        panel.add(lbl);
        panel.add(txt);
        panel.add(Box.createVerticalStrut(10));

        return txt;
    }

    private JComboBox<String> buildCombo(JPanel panel, String label, String[] items) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> cmb = new JComboBox<>(items);
        cmb.setMaximumSize(new Dimension(280, 30));
        cmb.setBackground(new Color(40, 40, 55));
        cmb.setForeground(Color.WHITE);
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JComponent) cmb.getRenderer()).setOpaque(false);

        panel.add(lbl);
        panel.add(cmb);
        panel.add(Box.createVerticalStrut(10));

        return cmb;
    }

    // --------------------------------------------------------------------
    //  MAIN LOGIC
    // --------------------------------------------------------------------

    private void generateWorkoutPlan() {
        try {
            double weight = Double.parseDouble(txtWeight.getText().trim());
            double heightCm = Double.parseDouble(txtHeight.getText().trim());
            int age = Integer.parseInt(txtAge.getText().trim());

            double heightM = heightCm / 100.0;
            double bmi = weight / (heightM * heightM);

            String gender = (String) cmbGender.getSelectedItem();
            String goal = (String) cmbGoal.getSelectedItem();
            String level = (String) cmbLevel.getSelectedItem();

            output.setText("Talking to your AI coach...\nPlease wait a few seconds...");

            // Build prompt for the AI
            String userSummary =
        "You are a professional GYM coach working inside a fully equipped fitness center.\n" +
        "Create a 7-day GYM workout plan for this member.\n\n" +

        "⚠️ IMPORTANT RULES:\n" +
        "- DO NOT include home workouts.\n" +
        "- DO NOT include push-ups, sit-ups, wall exercises, burpees, or bodyweight-only workouts.\n" +
        "- Use ONLY gym machines, barbells, dumbbells, cables, benches, squat racks, leg press, and cardio machines.\n\n" +

        "Member information:\n" +
        "- Gender: " + gender + "\n" +
        "- Age: " + age + "\n" +
        "- Weight: " + weight + " kg\n" +
        "- Height: " + heightCm + " cm\n" +
        "- BMI: " + String.format("%.1f", bmi) + " (" + getBMIStatus(bmi) + ")\n" +
        "- Goal: " + goal + "\n" +
        "- Experience level: " + level + "\n\n" +

        "Requirements:\n" +
        "1. Give a 7-day GYM split (Day 1, Day 2, ...).\n" +
        "2. Every exercise MUST use GYM equipment.\n" +
        "3. For each exercise give sets x reps.\n" +
        "4. Add gym-style warm-up and cool-down.\n" +
        "5. Use clear bullet points. Keep it under 400 words.\n" +
        "6. Make this workout plan DIFFERENT from previous weeks with new exercises and different split.\n" +
        "7. Avoid repeating the same routine as last time.";



            String aiPlan = callOpenAI(userSummary);

            StringBuilder sb = new StringBuilder();
            sb.append("🔥 AI Workout Recommendation\n\n");
            sb.append("BMI: ").append(String.format("%.1f", bmi))
              .append(" (").append(getBMIStatus(bmi)).append(")\n");
            sb.append("Goal: ").append(goal).append("\n");
            sb.append("Level: ").append(level).append("\n\n");
            sb.append(aiPlan);

            output.setText(sb.toString());
            // Save full plan text in the database
        saveWorkoutPlan(sb.toString());


            // Save to DB
            saveMemberInfo(weight, (int) heightCm, age, gender, goal, level);
            saveProgress(weight);

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for weight, height and age.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Problem calling OpenAI: " + ex.getMessage(),
                    "AI Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getBMIStatus(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    // --------------------------------------------------------------------
    //  OPENAI HTTP CALL (Chat Completions)
    // --------------------------------------------------------------------

    private String callOpenAI(String prompt) throws Exception {
        String apiKey = loadApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new Exception("API key not found in api_key.txt");
        }

        String url = "https://api.openai.com/v1/chat/completions";

        // Build JSON body
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"model\":\"gpt-4o-mini\",");
        json.append("\"messages\":[");
        json.append("{\"role\":\"system\",\"content\":")
            .append(jsonQuote("You are a friendly professional fitness coach."))
            .append("},");
        json.append("{\"role\":\"user\",\"content\":")
            .append(jsonQuote(prompt))
            .append("}");
        json.append("]}");

        byte[] bodyBytes = json.toString().getBytes(StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(bodyBytes);
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        // DEBUG PRINT — SHOW RAW RESPONSE FROM OPENAI
System.out.println("RAW AI RESPONSE = " + response.toString());


        if (status < 200 || status >= 300) {
            throw new Exception("OpenAI error (" + status + "): " + response);
        }

        String content = extractAssistantContent(response.toString());
        if (content == null || content.isEmpty()) {
            throw new Exception("Could not read AI response.");
        }
        return content.trim();
    }

    private String loadApiKey() throws IOException {
        InputStream is = getClass().getResourceAsStream("api_key.txt");
        if (is == null) {
            is = getClass().getResourceAsStream("/gymmangmentsystem/api_key.txt");
        }
        if (is == null) {
            throw new FileNotFoundException("api_key.txt not found in package gymmangmentsystem.");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            return (line == null) ? "" : line.trim();
        }
    }

    // Put quotes + escape for JSON
    private String jsonQuote(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 32) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    // Very small JSON parser: finds assistant message content and unescapes it
  private String extractAssistantContent(String json) {
    try {
        int msgIndex = json.indexOf("\"message\"");
        if (msgIndex == -1) return null;

        int contentIndex = json.indexOf("\"content\"", msgIndex);
        if (contentIndex == -1) return null;

        int start = json.indexOf(":", contentIndex) + 1;
        int firstQuote = json.indexOf("\"", start);
        int lastQuote = json.indexOf("\"", firstQuote + 1);

        String raw = json.substring(firstQuote + 1, lastQuote);

        // Decode escaped characters (e.g. \n, \")
        return raw.replace("\\n", "\n")
                  .replace("\\\"", "\"")
                  .replace("\\u2026", "…")  // "…" unicode fix
                  .trim();
    } catch (Exception e) {
        return null;
    }
}


    // --------------------------------------------------------------------
    //  DATABASE (same idea as before)
    // --------------------------------------------------------------------

    private void saveMemberInfo(double weight, int height, int age,
                                String gender, String goal, String level) {
        try {
            Connection con = ConnectionProvider.con();

            PreparedStatement check = con.prepareStatement(
                    "SELECT id FROM member_info WHERE user_id = ?");
            check.setInt(1, userId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                PreparedStatement update = con.prepareStatement(
                        "UPDATE member_info SET weight=?, height=?, age=?, gender=?, goal=?, experience=? " +
                        "WHERE user_id=?");
                update.setDouble(1, weight);
                update.setInt(2, height);
                update.setInt(3, age);
                update.setString(4, gender);
                update.setString(5, goal);
                update.setString(6, level);
                update.setInt(7, userId);
                update.executeUpdate();
            } else {
                PreparedStatement insert = con.prepareStatement(
                        "INSERT INTO member_info (user_id, weight, height, age, gender, goal, experience) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)");
                insert.setInt(1, userId);
                insert.setDouble(2, weight);
                insert.setInt(3, height);
                insert.setInt(4, age);
                insert.setString(5, gender);
                insert.setString(6, goal);
                insert.setString(7, level);
                insert.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveProgress(double weight) {
        try {
            Connection con = ConnectionProvider.con();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO member_progress (user_id, weight, progress_date) VALUES (?, ?, CURDATE())");
            ps.setInt(1, userId);
            ps.setDouble(2, weight);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadExistingInfo() {
        try {
            Connection con = ConnectionProvider.con();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT weight, height, age, gender, goal, experience FROM member_info WHERE user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtWeight.setText(rs.getString("weight"));
                txtHeight.setText(rs.getString("height"));
                txtAge.setText(rs.getString("age"));

                cmbGender.setSelectedItem(rs.getString("gender"));
                cmbGoal.setSelectedItem(rs.getString("goal"));
                cmbLevel.setSelectedItem(rs.getString("experience"));
            }
        } catch (Exception ex) {
            // ignore if no data yet
        }
    }
    private void saveWorkoutPlan(String planText) {
    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO workout_plans (user_id, plan) VALUES (?, ?)"
         )) {

        ps.setInt(1, userId);
        ps.setString(2, planText);
        ps.executeUpdate();

    } catch (Exception ex) {
        ex.printStackTrace(); // you can also show a JOptionPane if you want
    }
}
 private void saveWorkoutHistory(String workoutName) {
    try (Connection con = ConnectionProvider.con();
         PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO workout_history (user_id, workout_name, workout_date) VALUES (?, ?, CURDATE())"
         )) {

        ps.setInt(1, userId);
        ps.setString(2, workoutName);
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this,
                "Workout saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Failed to save workout: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
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




    // Test main (you can delete later)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AIWorkout(1).setVisible(true));
    }
}
