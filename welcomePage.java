/*package projectpl2hotel;
import projectpl2hotel.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.io.File;
public class welcomePage extends JFrame {
    private float opacity = 0f;
    private JLabel hotelName;
    private JLabel slogan;
    private JButton startButton;

    public welcomePage() {
        setTitle("Lunaria Hotel - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(null);
        backgroundLabel.setBounds(0, 0, 1000, 700);
        try {
            ImageIcon bgImage = new ImageIcon("src/WhatsApp Image 2025-05-16 at 23.25.52_b049f862.jpg");
            Image scaled = bgImage.getImage().getScaledInstance(1000, 700, Image.SCALE_SMOOTH);
            backgroundLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            backgroundLabel.setBackground(new Color(20, 20, 50));
            backgroundLabel.setOpaque(true);
        }

        hotelName = new JLabel("Welcome to Lunaria Hotel") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        hotelName.setBounds(0, 200, 1000, 100);

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("D:/term 4/adv pl/hotel system project/src/welcomPage/src/welcomPage/src/Dancing_Script/DancingScript-VariableFont_wght.ttf")).deriveFont(64f);
            hotelName.setFont(customFont);
        } catch (Exception e) {
            hotelName.setFont(new Font("Great Vibes", Font.PLAIN, 64));
        }

        hotelName.setForeground(Color.WHITE);
        hotelName.setHorizontalAlignment(SwingConstants.CENTER);

        slogan = new JLabel("Come as a Guest, Leave as a Friend") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        slogan.setBounds(0, 300, 1000, 40);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("D:/term 4/adv pl/hotel system project/src/welcomPage/src/welcomPage/src/Dancing_Script/DancingScript-VariableFont_wght.ttf")).deriveFont(28f);
            slogan.setFont(customFont);
        } catch (Exception e) {
            slogan.setFont(new Font("Montserrat", Font.PLAIN, 28));
        }
        slogan.setForeground(Color.WHITE);
        slogan.setHorizontalAlignment(SwingConstants.CENTER);

        // Start button
        startButton = new JButton("Start Now ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(Color.BLACK);
                try {
                    Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("")).deriveFont(24f);
                    g2.setFont(customFont);
                } catch (Exception e) {
                    g2.setFont(new Font("Poppins", Font.BOLD, 24));
                }
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        startButton.setBounds(400, 380, 200, 50);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("D:/term 4/adv pl/hotel system project/src/welcomPage/src/welcomPage/src/Dancing_Script/DancingScript-VariableFont_wght.ttf")).deriveFont(24f);
            startButton.setFont(customFont);
        } catch (Exception e) {
            startButton.setFont(new Font("Poppins", Font.BOLD, 24));
        }
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Transition to MainPanel
        startButton.addActionListener(e -> {
            try {
                getContentPane().removeAll();
                setTitle("Hotel Management System");
                Main mainPanel = new Main();
                mainPanel.setBounds(0, 0, 1000, 700);
                getContentPane().add(mainPanel);
                revalidate();
                repaint();
            } catch (Exception ex) {
                System.err.println("Error transitioning to MainPanel: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error loading the next screen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        backgroundLabel.add(hotelName);
        backgroundLabel.add(slogan);
        backgroundLabel.add(startButton);
        add(backgroundLabel);
        Timer timer = new Timer(50, e -> {
            if (opacity < 1f) {
                opacity = Math.min(opacity + 0.05f, 1f);
                hotelName.repaint();
                slogan.repaint();
                startButton.repaint();
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(welcomePage::new);
    }
}*/


