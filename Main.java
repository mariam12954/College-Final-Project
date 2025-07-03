package projectpl2hotel;
//import projectpl2hotel.welcomePage;
import projectpl2hotel.Manager;
import projectpl2hotel .ManagerMenu;
import projectpl2hotel.EmployeeMenu;
import java.util.Scanner;
public class Main {
 public static void main(String[] args) {
  Scanner scanner = new Scanner(System.in);

  while (true) {
   System.out.println("===== Welcome to Hotel Management System =====");
   System.out.println("Are you a Manager or an Employee?");
   System.out.println("Type 'exit' to quit.");
   String role = scanner.nextLine().trim().toLowerCase();

   if (role.equals("exit")) {
    System.out.println("System exited.");
    break;
   }

   if (!role.equals("manager") && !role.equals("employee")) {
    System.out.println("❌ Invalid role. Please try again.");
    continue;
   }

   User user = null;

   if (role.equals("manager")) {
    System.out.println("🔐 Manager Login");
    user = UserService.login("manager");

   } else if (role.equals("employee")) {
    System.out.println("Do you have an account? (yes/no)");
    String hasAccount = scanner.nextLine().trim().toLowerCase();

    if (hasAccount.equals("no")) {
     user = UserService.registerUser("employee");
    } else {
     user = UserService.login("employee");
    }
   }

   if (user != null) {
    System.out.println("✅ Login successful! Welcome " + user.getUsername());

    if (user instanceof Manager) {
     ManagerMenu.show((Manager) user);
    } else if (user instanceof employee) {
     EmployeeMenu.show((employee) user);
    }

   } else {
    System.out.println("❌ Login failed or cancelled.");
   }
  }
 }
}



/*import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import projectpl2hotel.UserService;

public class Main extends JPanel {
 private float opacity = 0f;
 private JButton employeeBtn, managerBtn;

 public Main() {
  setLayout(null);
  setBounds(0, 0, 1000, 700);

  JLabel backgroundLabel = new JLabel();
  backgroundLabel.setLayout(null);
  backgroundLabel.setBounds(0, 0, 1000, 700);

  try {
   ImageIcon bgImage = new ImageIcon("D:/term 4/adv pl/hotel system project/src/welcomPage/src/pexels-pixabay-258154.jpg");
   Image scaled = bgImage.getImage().getScaledInstance(1000, 700, Image.SCALE_SMOOTH);
   backgroundLabel.setIcon(new ImageIcon(scaled));
  } catch (Exception e) {
   backgroundLabel.setBackground(new Color(30, 30, 60));
   backgroundLabel.setOpaque(true);
  }

  JLabel roleLabel = new JLabel("Select Your Role") {
   @Override
   protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
    super.paintComponent(g2);
    g2.dispose();
   }
  };
  roleLabel.setBounds(0, 150, 1000, 80);
  try {
   Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("D:/term 4/adv pl/hotel system project/src/welcomPage/src/welcomPage/src/Dancing_Script/DancingScript-VariableFont_wght.ttf")).deriveFont(60f);
   roleLabel.setFont(customFont);
  } catch (Exception e) {
   roleLabel.setFont(new Font("Serif", Font.BOLD, 60));
  }
  roleLabel.setForeground(Color.WHITE);
  roleLabel.setHorizontalAlignment(SwingConstants.CENTER);

  employeeBtn = createStyledButton("Employee");
  employeeBtn.setBounds(350, 280, 300, 60);
  employeeBtn.addActionListener((ActionEvent e) -> {
   navigateToUserService("employee");
  });

  managerBtn = createStyledButton("Manager");
  managerBtn.setBounds(350, 370, 300, 60);
  managerBtn.addActionListener((ActionEvent e) -> {
   navigateToUserService("manager");
  });

  backgroundLabel.add(roleLabel);
  backgroundLabel.add(employeeBtn);
  backgroundLabel.add(managerBtn);
  add(backgroundLabel);

  Timer timer = new Timer(50, e -> {
   if (opacity < 1f) {
    opacity = Math.min(opacity + 0.05f, 1f);
    roleLabel.repaint();
    employeeBtn.repaint();
    managerBtn.repaint();
   } else {
    ((Timer) e.getSource()).stop();
   }
  });
  timer.start();
 }

 private JButton createStyledButton(String text) {
  JButton btn = new JButton(text) {
   @Override
   protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
    g2.setColor(new Color(255, 255, 255, 220));
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
    g2.setColor(Color.BLACK);
    try {
     Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("D:/term 4/adv pl/hotel system project/src/welcomPage/src/welcomPage/src/Dancing_Script/DancingScript-VariableFont_wght.ttf")).deriveFont(26f);
     g2.setFont(customFont);
    } catch (Exception e) {
     g2.setFont(new Font("SansSerif", Font.BOLD, 26));
    }
    FontMetrics fm = g2.getFontMetrics();
    int x = (getWidth() - fm.stringWidth(text)) / 2;
    int y = (getHeight() + fm.getAscent()) / 2 - 4;
    g2.drawString(text, x, y);
    g2.dispose();
   }
  };
  btn.setContentAreaFilled(false);
  btn.setBorderPainted(false);
  btn.setFocusPainted(false);
  btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
  return btn;
 }

 private void navigateToUserService(String role) {
  try {
   Window window = SwingUtilities.getWindowAncestor(this);
   if (window instanceof JFrame) {
    JFrame frame = (JFrame) window;
    frame.getContentPane().removeAll();
    UserService userServicePanel;
    userServicePanel = new UserService(role);
    userServicePanel.setBounds(0, 0, 1000, 700);
    frame.getContentPane().add(userServicePanel);
    frame.setTitle("Lunaria Hotel - " + (role.equals("manager") ? "Manager" : "Employee") + " Login");
    frame.revalidate();
    frame.repaint();
   }
  } catch (Exception ex) {
   System.err.println("Error transitioning to UserService: " + ex.getMessage());
   JOptionPane.showMessageDialog(this, "Error loading the authentication screen.", "Error", JOptionPane.ERROR_MESSAGE);
  }
 }
}*/