package projectpl2hotel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RoomServicePage extends JFrame {
    public String employeeName;
    public static ArrayList<String> selectedServices = new ArrayList<>();

    public RoomServicePage(String employeeName) {
        this.employeeName = employeeName;
        setTitle("Room Services");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bgPanel = new JPanel() {
            private Image bgImage = new ImageIcon("room.jpg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Room Service Menu");
        title.setFont(new Font("Segoe Script", Font.BOLD, 36));
        title.setForeground(Color.WHITE);

        JCheckBox food = new JCheckBox("Food Delivery");
        JCheckBox cleaning = new JCheckBox("Room Cleaning");
        JCheckBox laundry = new JCheckBox("Laundry Service");

        Font font = new Font("Segoe Script", Font.PLAIN, 24);
        for (JCheckBox cb : new JCheckBox[]{food, cleaning, laundry}) {
            cb.setFont(font);
            cb.setOpaque(false);
            cb.setForeground(Color.WHITE);
        }

        JButton request = new JButton("Request Services");
        request.setFont(font);
        request.setForeground(Color.WHITE);
        request.setContentAreaFilled(false);
        request.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        request.setFocusPainted(false);

        JPanel form = new JPanel(new GridLayout(5, 1, 15, 15));
        form.setOpaque(false);
        form.add(title);
        form.add(food);
        form.add(cleaning);
        form.add(laundry);
        form.add(request);

        bgPanel.add(form);
        setContentPane(bgPanel);

        request.addActionListener(e -> {
            selectedServices.clear();
            if (food.isSelected()) selectedServices.add("Food Delivery");
            if (cleaning.isSelected()) selectedServices.add("Room Cleaning");
            if (laundry.isSelected()) selectedServices.add("Laundry");
            String services = String.join(", ", selectedServices);
            ManagerDashboard.employees.put(employeeName, new String[]{"", "", services}); // تحديث الخدمات
            JOptionPane.showMessageDialog(this, "Services requested successfully!");
            dispose();
            new EmployeeDashboard();
        });

        setVisible(true);
    }
}