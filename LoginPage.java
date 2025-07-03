package projectpl2hotel;
import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    public LoginPage(String role) {
        setTitle(role.substring(0, 1).toUpperCase() + role.substring(1) + " Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bgPanel = new JPanel() {
            private Image bgImage = new ImageIcon("hotel_background.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Login as " + role);
        title.setFont(new Font("Segoe Script", Font.BOLD, 36));
        title.setForeground(Color.WHITE);

        JTextField username = new JTextField(15);
        JPasswordField password = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");

        Font font = new Font("Segoe Script", Font.PLAIN, 24);
        username.setFont(font);
        password.setFont(font);
        loginBtn.setFont(font);

        username.setOpaque(false);
        username.setForeground(Color.WHITE);
        username.setBackground(new Color(255, 255, 255, 50));
        username.setCaretColor(Color.WHITE);

        password.setOpaque(false);
        password.setForeground(Color.WHITE);
        password.setBackground(new Color(255, 255, 255, 50));
        password.setCaretColor(Color.WHITE);

        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setOpaque(false);
        panel.add(title);
        panel.add(username);
        panel.add(password);
        panel.add(loginBtn);

        bgPanel.add(panel);
        setContentPane(bgPanel);

        loginBtn.addActionListener(e -> {
            String user = username.getText();
            String pass = new String(password.getPassword());
            if (role.equals("manager") && user.equals("ziad") && pass.equals("123456")) {
                dispose();
                new ManagerDashboard();
            } else if (role.equals("employee")) {
                if (user.equals("ziad") && pass.equals("123456")) { // يوزر وباسورد افتراضي للموظف
                    dispose();
                    new EmployeeDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        });

        setVisible(true);
    }
}