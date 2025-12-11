package gymmangmentsystem;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Home extends JFrame {


// === THEME ===
private static final Color BG_DARK = new Color(18, 18, 28);
private static final Color CARD_DARK = new Color(33, 33, 45);
private static final Color TEXT_PRIMARY = new Color(240, 240, 245);
private static final Color TEXT_SECOND = new Color(180, 185, 195);
private static final Color ACCENT = new Color(124, 92, 252);
private static final Color ACCENT_2 = new Color(255, 95, 35);
private static final Font H1 = new Font("Segoe UI", Font.BOLD, 36);
private static final Font H2 = new Font("Segoe UI", Font.BOLD, 20);
private static final Font BODY = new Font("Segoe UI", Font.PLAIN, 16);
private static final Font CHIP = new Font("Segoe UI", Font.PLAIN, 14);
private static final String HERO_IMAGE_PATH = "/hero_gym.jpg";

public Home() {
    setTitle("Gym Management System — Welcome");
    setUndecorated(false); // ✅ same as Login page
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1100, 700);
    setLocationRelativeTo(null);

    HeroPanel root = new HeroPanel(loadHero());
    root.setLayout(new BorderLayout());
    setContentPane(root);


    // ===== CUSTOM TOP BAR =====
    JPanel titleBar = new JPanel(new BorderLayout());
    titleBar.setBackground(new Color(25, 25, 35));
    titleBar.setPreferredSize(new Dimension(getWidth(), 35));

    JLabel lblTitle = new JLabel("  FitZone Gym");
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
    lblTitle.setForeground(Color.WHITE);

    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    controlPanel.setOpaque(false);

   

    titleBar.add(lblTitle, BorderLayout.WEST);
    titleBar.add(controlPanel, BorderLayout.EAST);

    // Make the top bar draggable
    final Point[] clickPoint = {null};
    titleBar.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) { clickPoint[0] = e.getPoint(); }
    });
    titleBar.addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            Point curr = e.getLocationOnScreen();
            setLocation(curr.x - clickPoint[0].x, curr.y - clickPoint[0].y);
        }
    });

    // ===== MAIN CONTENT =====
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);
    header.setBorder(new EmptyBorder(20, 24, 0, 24));

    JLabel brand = new JLabel("FitZone Gym");
    brand.setFont(new Font("Segoe UI", Font.BOLD, 22));
    brand.setForeground(TEXT_PRIMARY);

    JPanel rightBtns = new JPanel();
    rightBtns.setOpaque(false);
    rightBtns.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));

    JButton btnLogin = pillButtonFilled("Login");
    JButton btnSignUp = pillButtonFilled("Join Now");

    rightBtns.add(btnLogin);
    rightBtns.add(btnSignUp);

    header.add(brand, BorderLayout.WEST);
    header.add(rightBtns, BorderLayout.EAST);

    // ===== CENTER CARD =====
    JPanel centerWrap = new JPanel(new GridBagLayout());
    centerWrap.setOpaque(false);

    JPanel card = new JPanel();
    card.setBackground(new Color(CARD_DARK.getRed(), CARD_DARK.getGreen(), CARD_DARK.getBlue(), 220));
    card.setBorder(new EmptyBorder(28, 28, 28, 28));
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Welcome to FitZone Gym");
    title.setFont(H1);
    title.setForeground(TEXT_PRIMARY);

    JLabel tagline = new JLabel("Transform Your Body, Elevate Your Life.");
    tagline.setFont(H2);
    tagline.setForeground(ACCENT);

    JTextArea desc = new JTextArea(
            "State-of-the-art equipment, certified trainers, and smart tracking.\n" +
                    "Personal Training •  Group Classes •  Nutrition Plans ");
    desc.setFont(BODY);
    desc.setForeground(TEXT_SECOND);
    desc.setOpaque(false);
    desc.setEditable(false);
    desc.setFocusable(false);
    desc.setBorder(null);
    desc.setLineWrap(true);
    desc.setWrapStyleWord(true);

    JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    chips.setOpaque(false);
   chips.add(chip("Personal Training", "/Images/trainer.png"));
chips.add(chip("Group Classes", "/Images/group.png"));
chips.add(chip("Nutrition Plans", "/Images/nutrition.png"));

chips.add(chip("Progress Tracker", "/Images/progress.png"));


    JPanel ctas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    ctas.setOpaque(false);
    ctas.add(btnLogin);
    ctas.add(btnSignUp);

    card.add(title);
    card.add(Box.createVerticalStrut(6));
    card.add(tagline);
    card.add(Box.createVerticalStrut(12));
    card.add(desc);
    card.add(Box.createVerticalStrut(8));
    card.add(chips);
    card.add(Box.createVerticalStrut(12));
    card.add(ctas);
    JPanel infoPanel = new JPanel(new GridLayout(1, 2, 30, 0));
infoPanel.setOpaque(false);

    JPanel leftInfo = new JPanel();
leftInfo.setOpaque(false);
leftInfo.setLayout(new BoxLayout(leftInfo, BoxLayout.Y_AXIS));

JPanel rightInfo = new JPanel();
rightInfo.setOpaque(false);
rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.Y_AXIS));

JLabel benefitsTitle = new JLabel("Why Join a Gym?");
benefitsTitle.setFont(H2);
benefitsTitle.setForeground(TEXT_PRIMARY);

JTextArea benefitsText = new JTextArea(
        "• Faster fat loss & muscle growth\n" +
        "• Professional equipment & trainers\n" +
        "• Better discipline and motivation\n" +
        "• Improved mental health & confidence\n" +
        "• Stronger immunity & energy levels");
benefitsText.setFont(BODY);
benefitsText.setForeground(TEXT_SECOND);
benefitsText.setOpaque(false);
benefitsText.setEditable(false);
benefitsText.setBorder(null);

leftInfo.add(benefitsTitle);
leftInfo.add(Box.createVerticalStrut(8));
leftInfo.add(benefitsText);

JLabel beforeTitle = new JLabel("What To Do BEFORE Training");
beforeTitle.setFont(H2);
beforeTitle.setForeground(TEXT_PRIMARY);

JTextArea beforeText = new JTextArea(
        "• Eat light meal 60–90 minutes before\n" +
        "• Drink enough water\n" +
        "• Warm up properly\n" +
        "• Wear proper gym shoes\n" +
        "• Sleep well the night before");
beforeText.setFont(BODY);
beforeText.setForeground(TEXT_SECOND);
beforeText.setOpaque(false);
beforeText.setEditable(false);
beforeText.setBorder(null);

rightInfo.add(beforeTitle);
rightInfo.add(Box.createVerticalStrut(8));
rightInfo.add(beforeText);

JLabel afterTitle = new JLabel("What To Do AFTER Training");
afterTitle.setFont(H2);
afterTitle.setForeground(TEXT_PRIMARY);

JTextArea afterText = new JTextArea(
        "• Drink water immediately\n" +
        "• Eat protein within 30–60 minutes\n" +
        "• Stretch your muscles\n" +
        "• Get enough rest\n" +
        "• Avoid junk food");
afterText.setFont(BODY);
afterText.setForeground(TEXT_SECOND);
afterText.setOpaque(false);
afterText.setEditable(false);
afterText.setBorder(null);

rightInfo.add(Box.createVerticalStrut(18));
rightInfo.add(afterTitle);
rightInfo.add(Box.createVerticalStrut(8));
rightInfo.add(afterText);

JLabel dangerTitle = new JLabel("Dangerous Mistakes To Avoid");
dangerTitle.setFont(H2);
dangerTitle.setForeground(ACCENT_2);

JTextArea dangerText = new JTextArea(
        "• Skipping warm-up\n" +
        "• Training with bad form\n" +
        "• Training when injured\n" +
        "• Not drinking water\n" +
        "• Overtraining without rest");
dangerText.setFont(BODY);
dangerText.setForeground(TEXT_SECOND);
dangerText.setOpaque(false);
dangerText.setEditable(false);
dangerText.setBorder(null);

leftInfo.add(Box.createVerticalStrut(18));
leftInfo.add(dangerTitle);
leftInfo.add(Box.createVerticalStrut(8));
leftInfo.add(dangerText);

infoPanel.add(leftInfo);
infoPanel.add(rightInfo);

GridBagConstraints gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.gridy = 0;
gbc.insets = new Insets(10, 24, 24, 24);
gbc.anchor = GridBagConstraints.CENTER;

centerWrap.add(card, gbc);
// ===== SECOND CARD (INFO SECTION) =====
JPanel infoCard = new JPanel();
infoCard.setBackground(new Color(CARD_DARK.getRed(), CARD_DARK.getGreen(), CARD_DARK.getBlue(), 220));
infoCard.setBorder(new EmptyBorder(28, 28, 28, 28));
infoCard.setLayout(new BorderLayout());

infoCard.add(infoPanel, BorderLayout.CENTER);

// move second card below the first one
GridBagConstraints gbc2 = new GridBagConstraints();
gbc2.gridx = 0;
gbc2.gridy = 1;
gbc2.insets = new Insets(0, 24, 24, 24);
gbc2.anchor = GridBagConstraints.CENTER;

centerWrap.add(infoCard, gbc2);

root.add(titleBar, BorderLayout.NORTH);
root.add(centerWrap, BorderLayout.CENTER);




    // ===== BUTTON ACTIONS =====
    btnLogin.addActionListener((ActionEvent e) -> {
        dispose();
        new Login().setVisible(true);
    });

    btnSignUp.addActionListener((ActionEvent e) -> {
        dispose();
        new SignUp().setVisible(true);
    });
}

// ===== STYLED BUTTONS =====
private JButton pillButtonFilled(String text) {
    JButton b = new JButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape rr = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
            GradientPaint gp = new GradientPaint(0, 0, ACCENT, getWidth(), getHeight(), ACCENT_2);
            g2.setPaint(gp);
            g2.fill(rr);
            g2.dispose();
            super.paintComponent(g);
        }
    };
    styleButton(b, Color.WHITE);
    return b;
}

private void styleButton(JButton b, Color fg) {
    b.setForeground(fg);
    b.setFont(new Font("Segoe UI", Font.BOLD, 15));
    b.setFocusPainted(false);
    b.setContentAreaFilled(false);
    b.setBorderPainted(false);
    b.setOpaque(false);
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    b.setMargin(new Insets(10, 18, 10, 18));
}

private JComponent chip(String text, String iconPath) {
    JLabel l = new JLabel(text);
    l.setFont(CHIP);
    l.setForeground(TEXT_PRIMARY);

    try {
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        Image scaled = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        l.setIcon(new ImageIcon(scaled));
        l.setIconTextGap(6);
    } catch (Exception e) {
        System.out.println("Icon not found: " + iconPath);
    }

    JPanel p = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape rr = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.setColor(new Color(255, 255, 255, 20));
            g2.fill(rr);
            g2.dispose();
            super.paintComponent(g);
        }
    };

    p.setOpaque(false);
    p.setLayout(new GridBagLayout());
    p.setBorder(new EmptyBorder(6, 10, 6, 10));
    p.add(l);
    return p;
}


private BufferedImage loadHero() {
    try {
        return ImageIO.read(Home.class.getResource(HERO_IMAGE_PATH));
    } catch (IOException | IllegalArgumentException ex) {
        return null;
    }
}

private static class HeroPanel extends JPanel {
    private final BufferedImage img;
    HeroPanel(BufferedImage img) { this.img = img; setBackground(BG_DARK); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (img != null) {
            double pw = getWidth(), ph = getHeight();
            double iw = img.getWidth(), ih = img.getHeight();
            double scale = Math.max(pw / iw, ph / ih);
            int w = (int) (iw * scale), h = (int) (ih * scale);
            int x = (int) ((pw - w) / 2), y = (int) ((ph - h) / 2);
            g2.drawImage(img, x, y, w, h, null);
        } else {
            g2.setColor(BG_DARK);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        GradientPaint gp = new GradientPaint(0, 0, new Color(0, 0, 0, 160),
                getWidth(), 0, new Color(0, 0, 0, 80));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Home().setVisible(true));
}


}
