package projectpl2hotel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoleSelectionPage extends JFrame {
    public RoleSelectionPage() {
        setTitle("Hotel System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel bgPanel = new JPanel() {
            private Image bgImage = new ImageIcon("hotel_background.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                double imageRatio = (double) bgImage.getWidth(null) / bgImage.getHeight(null);
                double panelRatio = (double) getWidth() / getHeight();
                int scaledWidth, scaledHeight;

                if (panelRatio > imageRatio) {
                    scaledHeight = getHeight();
                    scaledWidth = (int) (scaledHeight * imageRatio);
                } else {
                    scaledWidth = getWidth();
                    scaledHeight = (int) (scaledWidth / imageRatio);
                }

                g.drawImage(bgImage, (getWidth() - scaledWidth) / 2,
                        (getHeight() - scaledHeight) / 2,
                        scaledWidth, scaledHeight, this);

                g.setColor(new Color(0, 0, 0, 80));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
        bgPanel.setOpaque(false);

        JLabel title = new JLabel("Welcome to hotel system");
        title.setFont(new Font("Segoe Script", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(100, 0, 50, 0));

        JButton managerButton = new JButton("Manager");
        styleTransparentButton(managerButton);

        JButton employeeButton = new JButton("Employee");
        styleTransparentButton(employeeButton);

        managerButton.addActionListener(e -> {
            dispose();
            new LoginPage("manager");
        });

        employeeButton.addActionListener(e -> {
            dispose();
            new LoginPage("employee");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(managerButton);
        buttonPanel.add(employeeButton);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bgPanel.add(title);
        bgPanel.add(buttonPanel);

        setContentPane(bgPanel);
        setVisible(true);
    }

    private void styleTransparentButton(JButton button) {
        button.setFont(new Font("Segoe Script", Font.PLAIN, 28));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 50));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoleSelectionPage::new);
    }
}