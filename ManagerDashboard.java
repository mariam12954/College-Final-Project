package projectpl2hotel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ManagerDashboard extends JFrame {
    public static HashMap<String, String[]> employees = new HashMap<>(); // تخزين بيانات الموظفين (اسم, [roomNum, roomType, services])

    public ManagerDashboard() {
        setTitle("Manager Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bgPanel = new JPanel() {
            private Image bgImage = new ImageIcon("hotel_background.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Welcome, Manager");
        title.setFont(new Font("Segoe Script", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JButton addEmployee = new JButton("Add Employee");
        JButton viewEmployee = new JButton("View Employee");
        JButton deleteEmployee = new JButton("Delete Employee");
        JButton logout = new JButton("Logout");

        Font btnFont = new Font("Segoe Script", Font.PLAIN, 24);
        for (JButton btn : new JButton[]{addEmployee, viewEmployee, deleteEmployee, logout}) {
            btn.setFont(btnFont);
            btn.setForeground(Color.WHITE);
            btn.setContentAreaFilled(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            btn.setFocusPainted(false);
        }

        JPanel panel = new JPanel(new GridLayout(5, 1, 20, 20));
        panel.setOpaque(false);
        panel.add(title);
        panel.add(addEmployee);
        panel.add(viewEmployee);
        panel.add(deleteEmployee);
        panel.add(logout);

        bgPanel.add(panel);
        setContentPane(bgPanel);

        addEmployee.addActionListener(e -> {
            JTextField nameField = new JTextField(15);
            JTextField roomNumField = new JTextField(5);
            String[] roomTypes = {"Single", "Double", "Suite"};
            JComboBox<String> typeBox = new JComboBox<>(roomTypes);
            JTextField servicesField = new JTextField(15);
            JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            inputPanel.setOpaque(false);
            inputPanel.add(new JLabel("Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Room Number:"));
            inputPanel.add(roomNumField);
            inputPanel.add(new JLabel("Room Type:"));
            inputPanel.add(typeBox);
            inputPanel.add(new JLabel("Services:"));
            inputPanel.add(servicesField);

            int result = JOptionPane.showConfirmDialog(null, inputPanel, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String roomNum = roomNumField.getText();
                String roomType = (String) typeBox.getSelectedItem();
                String services = servicesField.getText();
                if (!name.isEmpty() && !roomNum.isEmpty()) {
                    employees.put(name, new String[]{roomNum, roomType, services});
                    JOptionPane.showMessageDialog(this, "Employee " + name + " added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!");
                }
            }
        });

        viewEmployee.addActionListener(e -> {
            if (employees.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No employees to display!");
                return;
            }
            StringBuilder message = new StringBuilder();
            for (String name : employees.keySet()) {
                String[] data = employees.get(name);
                message.append("Name: ").append(name).append("\n")
                        .append("Room Number: ").append(data[0]).append("\n")
                        .append("Room Type: ").append(data[1]).append("\n")
                        .append("Services: ").append(data[2]).append("\n\n");
            }
            JOptionPane.showMessageDialog(this, message.toString(), "Employee List", JOptionPane.INFORMATION_MESSAGE);
        });

        deleteEmployee.addActionListener(e -> {
            if (employees.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No employees to delete!");
                return;
            }
            JTextField nameField = new JTextField(15);
            JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            inputPanel.setOpaque(false);
            inputPanel.add(new JLabel("Enter Name:"));
            inputPanel.add(nameField);

            int result = JOptionPane.showConfirmDialog(null, inputPanel, "Delete Employee", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                if (employees.remove(name) != null) {
                    JOptionPane.showMessageDialog(this, "Employee " + name + " deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Employee " + name + " not found!");
                }
            }
        });

        logout.addActionListener(e -> {
            dispose();
            new RoleSelectionPage();
        });

        setVisible(true);
    }
}