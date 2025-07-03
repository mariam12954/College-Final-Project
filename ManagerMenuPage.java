package projectpl2hotel;

import javax.swing.*;
import java.awt.*;

public class ManagerMenuPage extends JFrame {
    public ManagerMenuPage() {
        setTitle("Manager Menu");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Welcome Manager!");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new RoleSelectionPage();
        });

        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        add(logoutButton, BorderLayout.SOUTH);
        setVisible(true);
    }
}
