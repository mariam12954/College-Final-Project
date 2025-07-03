package projectpl2hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoomPage extends JFrame {
    public RoomPage() {
        setTitle("Room Booking");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bgPanel = new JPanel() {
            private Image bgImage = new ImageIcon("room.jpg").getImage();
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
                g.drawImage(bgImage, (getWidth() - scaledWidth) / 2, (getHeight() - scaledHeight) / 2, scaledWidth, scaledHeight, this);
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Book Your Room", SwingConstants.CENTER);
        title.setFont(new Font("Segoe Script", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bgPanel.add(title, BorderLayout.NORTH);

        JTextField nameField = createField("Guest Name");
        JTextField daysField = createField("Number of Days");
        String[] roomTypes = {"Single", "Double", "Suite"};
        JComboBox<String> typeBox = new JComboBox<>(roomTypes);
        typeBox.setFont(new Font("Segoe Script", Font.PLAIN, 22));

        JCheckBox food = createCheckbox("Food Delivery");
        JCheckBox cleaning = createCheckbox("Room Cleaning");
        JCheckBox laundry = createCheckbox("Laundry Service");

        JButton confirm = new JButton("Confirm Booking");
        confirm.setFont(new Font("Segoe Script", Font.BOLD, 22));
        confirm.setForeground(Color.WHITE);
        confirm.setOpaque(false);
        confirm.setContentAreaFilled(false);
        confirm.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        confirm.addActionListener(e -> {
            String name = nameField.getText();
            int days = 0;
            try {
                days = Integer.parseInt(daysField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number of days.");
                return;
            }
            String roomType = (String) typeBox.getSelectedItem();

            StringBuilder services = new StringBuilder();
            if (food.isSelected()) services.append("Food ");
            if (cleaning.isSelected()) services.append("Cleaning ");
            if (laundry.isSelected()) services.append("Laundry");

            EmployeeDashboard.setEmployeeData(name, roomType, days, services.toString().trim());
            dispose();
            new EmployeeDashboard();
        });

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(50, 300, 50, 300));

        form.add(makeLabel("Name:"));
        form.add(nameField);
        form.add(Box.createVerticalStrut(10));

        form.add(makeLabel("Days:"));
        form.add(daysField);
        form.add(Box.createVerticalStrut(10));

        form.add(makeLabel("Room Type:"));
        form.add(typeBox);
        form.add(Box.createVerticalStrut(10));
        form.add(makeLabel("Services:"));
        form.add(food);
        form.add(cleaning);
        form.add(laundry);
        form.add(Box.createVerticalStrut(20));

        form.add(confirm);
        bgPanel.add(form, BorderLayout.CENTER);

        setContentPane(bgPanel);
        setVisible(true);
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe Script", Font.PLAIN, 22));
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        return field;
    }

    private JCheckBox createCheckbox(String label) {
        JCheckBox cb = new JCheckBox(label);
        cb.setFont(new Font("Segoe Script", Font.PLAIN, 20));
        cb.setOpaque(false);
        cb.setForeground(Color.WHITE);
        cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return cb;
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe Script", Font.BOLD, 22));
        label.setForeground(Color.WHITE);
        return label;
    }
}