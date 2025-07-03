package projectpl2hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class EmployeeDashboard extends JFrame {
    private static String employeeName;
    private static String roomType;
    private static int dayNumber;
    private static String selectedServices;
    private static int employeeId = 1001;

    private JLabel welcomeLabel;

    public EmployeeDashboard() {
        setTitle("Employee Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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

                g.drawImage(bgImage, (getWidth() - scaledWidth) / 2,
                        (getHeight() - scaledHeight) / 2,
                        scaledWidth, scaledHeight, this);

                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
        bgPanel.setOpaque(false);

        welcomeLabel = new JLabel("Welcome, Employee");
        welcomeLabel.setFont(new Font("Segoe Script", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(80, 0, 50, 0));

        JButton viewData = new JButton("View My Data");
        JButton extraService = new JButton("Extra Service");
        JButton changeRoom = new JButton("Change Room");
        JButton logout = new JButton("Logout");

        styleTransparentButton(viewData);
        styleTransparentButton(extraService);
        styleTransparentButton(changeRoom);
        styleTransparentButton(logout);

        viewData.addActionListener(e -> showEmployeeData());
        extraService.addActionListener(e -> showExtraServiceMenu());
        changeRoom.addActionListener(e -> showChangeRoomMenu());
        logout.addActionListener(e -> {
            dispose();
            new RoleSelectionPage();
        });

        bgPanel.add(welcomeLabel);
        bgPanel.add(viewData);
        bgPanel.add(extraService);
        bgPanel.add(changeRoom);
        bgPanel.add(logout);

        setContentPane(bgPanel);
        setVisible(true);

        // تحقق من الاسم بعد تسجيل الدخول
        JTextField nameField = new JTextField(15);
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setOpaque(false);
        inputPanel.add(new JLabel("Enter Employee Name:"));
        inputPanel.add(nameField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Employee Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            if (ManagerDashboard.employees.containsKey(name)) {
                employeeName = name;
                String[] data = ManagerDashboard.employees.get(name);
                roomType = data[1];
                dayNumber = 0; // يمكن يتحدد من RoomPage
                selectedServices = data[2];
                welcomeLabel.setText("Welcome, " + employeeName);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid employee name! Access denied.");
                dispose();
                new RoleSelectionPage();
            }
        } else {
            dispose();
            new RoleSelectionPage();
        }
    }

    private void styleTransparentButton(JButton button) {
        button.setFont(new Font("Segoe Script", Font.PLAIN, 26));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(180, 50));
        button.setMaximumSize(new Dimension(200, 60));
    }

    private void showEmployeeData() {
        String message = "Name: " + employeeName +
                "\nID: " + employeeId +
                "\nRoom Type: " + roomType +

                "\nServices: " + selectedServices;

        JOptionPane.showMessageDialog(this, message, "Employee Data", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showExtraServiceMenu() {
        JCheckBox cleaning = new JCheckBox("Room Cleaning");
        JCheckBox food = new JCheckBox("Food Delivery");
        Font font = new Font("Segoe Script", Font.BOLD, 20);
        cleaning.setFont(font);
        food.setFont(font);
        cleaning.setForeground(Color.black);
        food.setForeground(Color.black);
        cleaning.setOpaque(false);
        food.setOpaque(false);

        JPanel servicePanel = new JPanel(new GridLayout(2, 1, 10, 10));
        servicePanel.setOpaque(false);
        servicePanel.add(cleaning);
        servicePanel.add(food);

        int result = JOptionPane.showConfirmDialog(this, servicePanel, "Select Extra Services", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            StringBuilder services = new StringBuilder(selectedServices != null ? selectedServices : "");
            if (cleaning.isSelected()) services.append("Room Cleaning, ");
            if (food.isSelected()) services.append("Food Delivery, ");
            selectedServices = services.length() > 0 ? services.substring(0, services.length() - 2) : "";
            updateEmployeeData();
        }
    }

    private void showChangeRoomMenu() {
        String[] roomTypes = {"Single", "Double", "Suite"};
        JComboBox<String> typeBox = new JComboBox<>(roomTypes);
        typeBox.setFont(new Font("Segoe Script", Font.PLAIN, 20));
        typeBox.setForeground(Color.WHITE);
        typeBox.setOpaque(false);

        int result = JOptionPane.showConfirmDialog(this, typeBox, "Change Room Type", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            roomType = (String) typeBox.getSelectedItem();
            updateEmployeeData();
        }
    }

    private void updateEmployeeData() {
        if (employeeName != null && ManagerDashboard.employees.containsKey(employeeName)) {
            ManagerDashboard.employees.put(employeeName, new String[]{"", roomType, selectedServices});
            JOptionPane.showMessageDialog(this, "Data updated successfully!");
        }
    }

    public static void setEmployeeData(String name, String room, int days, String services) {
        employeeName = name;
        roomType = room;

        selectedServices = services;
    }
}