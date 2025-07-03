package projectpl2hotel;

import static projectpl2hotel .UserExist.userExists;
import java.io.*;
import java.util.Scanner;

public class UserService {

    public static User login(String type) {
        Scanner scanner = new Scanner(System.in);

        if (type.equals("manager")) {
            System.out.print("Enter manager username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (username.equals("ziad") && password.equals("123456")) {
                System.out.println("✅ Login successful. Welcome, " + username);
                return new Manager(username, password);
            } else {
                System.out.println("❌ Invalid manager credentials.");
                return null;
            }
        }

        String filename = "data/employees.txt";

        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                        System.out.println("✅ Login successful. Welcome, " + username);
                        return new employee(username, password);
                    }
                }
                System.out.println("❌ Incorrect credentials. Try again.");
            } catch (IOException e) {
                System.out.println("❌ Error reading user file.");
                return null;
            }
        }
    }

    public static User registerUser(String type) {
        Scanner scanner = new Scanner(System.in);

        if (type.equals("employee")) {
            System.out.print("Enter your assigned Employee ID: ");
            String inputId = scanner.nextLine().trim();

            boolean idExists = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("data/employee_ids.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(inputId)) {
                        idExists = true;
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("❌ Failed to read employee IDs.");
                return null;
            }

            if (!idExists) {
                System.out.println("❌ Invalid ID. Please contact your manager.");
                return null;
            }

            String username;
            while (true) {
                System.out.print("Enter username: ");
                username = scanner.nextLine();

                if (userExists(type, username)) {
                    System.out.println("❌ Username already exists. Try another one.");
                } else {
                    break;
                }
            }

            String password;
            while (true) {
                System.out.print("Enter password (min 6 characters): ");
                password = scanner.nextLine();

                if (password.length() < 6) {
                    System.out.println("❌ Password too short. Try again.");
                } else {
                    break;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/employees.txt", true))) {
                writer.write(username + "," + password + "\n");
            } catch (IOException e) {
                System.out.println("❌ Failed to save user.");
                return null;
            }

            System.out.println("✅ Registration successful. Welcome, " + username);
            return new employee(username, password);
        }

        // المدير تسجيله يدوي أو ثابت - مينفعش يسجل جديد
        System.out.println("❌ Only employees can register. Managers must log in directly.");
        return null;
    }

}

/*import javax.swing.*;
import java.awt.*;
import java.io.*;

public class UserService extends JPanel {

    private float opacity = 0.9f;
    private JComboBox<String> userTypeCombo;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;
    private Image backgroundImage;
    private JLabel titleLabel;

    private JPanel loginFormPanel;
    private JPanel registerFormPanel;
    private JTextField regIdField;
    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;
    private JLabel regMessageLabel;
    private JButton registerSubmitButton;
    private JButton backToLoginButton;

    public UserService(String userType) {
        setLayout(null);
        setBounds(0, 0, 1000, 700);

        try {
            ImageIcon bgIcon = new ImageIcon("D:/term 4/adv pl/hotel system project/src/welcomPage/src/pexels-pixabay-258154.jpg");
            backgroundImage = bgIcon.getImage().getScaledInstance(1000, 700, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            backgroundImage = null;
            setBackground(new Color(18, 30, 49));
            setOpaque(true);
        }
        Font baseFont;
        try {
            baseFont = Font.createFont(Font.TRUETYPE_FONT,
                            new File("D:/term 4/adv pl/hotel system project/src/welcomPage/src/welcomPage/src/Dancing_Script/DancingScript-VariableFont_wght.ttf"))
                    .deriveFont(30f);
        } catch (Exception e) {
            baseFont = new Font("SansSerif", Font.BOLD, 30);
        }

        String titleText = userType.equalsIgnoreCase("manager") ? "Manager Login" : "Employee Login";
        titleLabel = new JLabel(titleText, SwingConstants.CENTER);
        titleLabel.setFont(baseFont.deriveFont(Font.BOLD, 46f));
        titleLabel.setBounds(300, 50, 400, 70);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        createLoginForm(baseFont, userType);

        createRegistrationForm(baseFont);

        loginFormPanel.setVisible(true);
        registerFormPanel.setVisible(false);
    }
    private void createLoginForm(Font baseFont, String userType) {
        loginFormPanel = new RoundedPanel(25, new Color(255, 255, 255, 220));
        loginFormPanel.setLayout(null);
        loginFormPanel.setBounds(300, 130, 400, 450);
        add(loginFormPanel);

        userTypeCombo = new JComboBox<>(new String[]{"manager", "employee"});
        userTypeCombo.setSelectedItem(userType);
        userTypeCombo.setEnabled(false);
        userTypeCombo.setFont(baseFont.deriveFont(Font.PLAIN, 30f));
        userTypeCombo.setBounds(40, 30, 320, 55);
        userTypeCombo.setBackground(Color.WHITE);
        userTypeCombo.setForeground(Color.DARK_GRAY);
        userTypeCombo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        loginFormPanel.add(userTypeCombo);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(baseFont.deriveFont(Font.PLAIN, 26f));
        usernameLabel.setBounds(40, 95, 320, 30);
        usernameLabel.setForeground(Color.BLACK);
        loginFormPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(baseFont.deriveFont(Font.PLAIN, 26f));
        usernameField.setBounds(40, 125, 320, 50);
        usernameField.setBackground(Color.WHITE);
        usernameField.setForeground(Color.BLACK);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 0)));
        loginFormPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(baseFont.deriveFont(Font.PLAIN, 26f));
        passwordLabel.setBounds(40, 185, 320, 30);
        passwordLabel.setForeground(Color.BLACK);
        loginFormPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(baseFont.deriveFont(Font.PLAIN, 26f));
        passwordField.setBounds(40, 215, 320, 50);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.DARK_GRAY);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 0)));
        loginFormPanel.add(passwordField);

        loginButton = createStyledButton("Enter");
        loginButton.setBounds(40, 285, 320, 70);
        loginButton.addActionListener(e -> loginAction());
        loginFormPanel.add(loginButton);

        if (userType.equalsIgnoreCase("employee")) {
            registerButton = new JButton("Register");
            registerButton.setBounds(40, 365, 320, 40);
            registerButton.setFont(baseFont.deriveFont(Font.ITALIC, 24f));
            registerButton.setForeground(new Color(50, 50, 50));
            registerButton.setBackground(new Color(255, 255, 255, 0));
            registerButton.setBorderPainted(false);
            registerButton.setContentAreaFilled(false);
            registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            registerButton.addActionListener(e -> switchToRegisterForm());
            loginFormPanel.add(registerButton);
        }

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setBounds(40, 405, 320, 35);
        messageLabel.setFont(baseFont.deriveFont(Font.ITALIC, 22f));
        loginFormPanel.add(messageLabel);
    }

    private void createRegistrationForm(Font baseFont) {
        registerFormPanel = new RoundedPanel(25, new Color(255, 255, 255, 220));
        registerFormPanel.setLayout(null);
        registerFormPanel.setBounds(300, 130, 400, 470);
        add(registerFormPanel);

        JLabel titleLabel = new JLabel("Employee Registration", SwingConstants.CENTER);
        titleLabel.setFont(baseFont.deriveFont(Font.BOLD, 28f));
        titleLabel.setBounds(20, 20, 360, 40);
        titleLabel.setForeground(new Color(80, 80, 80));
        registerFormPanel.add(titleLabel);

        JLabel idLabel = new JLabel("Employee ID");
        idLabel.setFont(baseFont.deriveFont(Font.PLAIN, 24f));
        idLabel.setBounds(40, 70, 320, 25);
        idLabel.setForeground(new Color(80, 80, 80));
        registerFormPanel.add(idLabel);

        regIdField = new JTextField();
        regIdField.setFont(baseFont.deriveFont(Font.PLAIN, 24f));
        regIdField.setBounds(40, 95, 320, 45);
        regIdField.setBackground(Color.WHITE);
        regIdField.setForeground(Color.BLACK);
        regIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 0)));
        registerFormPanel.add(regIdField);

        JLabel usernameLabel = new JLabel("Choose Username");
        usernameLabel.setFont(baseFont.deriveFont(Font.PLAIN, 24f));
        usernameLabel.setBounds(40, 145, 320, 25);
        usernameLabel.setForeground(new Color(80, 80, 80));
        registerFormPanel.add(usernameLabel);

        regUsernameField = new JTextField();
        regUsernameField.setFont(baseFont.deriveFont(Font.PLAIN, 24f));
        regUsernameField.setBounds(40, 170, 320, 45);
        regUsernameField.setBackground(Color.WHITE);
        regUsernameField.setForeground(Color.BLACK);
        regUsernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 0)));
        registerFormPanel.add(regUsernameField);

        JLabel passwordLabel = new JLabel("Set Password (at least 6 characters)");
        passwordLabel.setFont(baseFont.deriveFont(Font.PLAIN, 24f));
        passwordLabel.setBounds(40, 220, 320, 25);
        passwordLabel.setForeground(new Color(80, 80, 80));
        registerFormPanel.add(passwordLabel);

        regPasswordField = new JPasswordField();
        regPasswordField.setFont(baseFont.deriveFont(Font.PLAIN, 24f));
        regPasswordField.setBounds(40, 245, 320, 45);
        regPasswordField.setBackground(Color.WHITE);
        regPasswordField.setForeground(Color.BLACK);
        regPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 0)));
        registerFormPanel.add(regPasswordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(baseFont.deriveFont(Font.PLAIN, 24f));
        confirmPasswordLabel.setBounds(40, 295, 320, 25);
        confirmPasswordLabel.setForeground(new Color(80, 80, 80));
        registerFormPanel.add(confirmPasswordLabel);

        regConfirmPasswordField = new JPasswordField();
        regConfirmPasswordField.setFont(baseFont.deriveFont(Font.PLAIN, 24f));
        regConfirmPasswordField.setBounds(40, 320, 320, 45);
        regConfirmPasswordField.setBackground(Color.WHITE);
        regConfirmPasswordField.setForeground(Color.BLACK);
        regConfirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 0)));
        registerFormPanel.add(regConfirmPasswordField);


        registerSubmitButton = createStyledButton("Register");
        registerSubmitButton.setBounds(40, 375, 320, 55);
        registerSubmitButton.addActionListener(e -> registerAction());
        registerFormPanel.add(registerSubmitButton);

        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setBounds(40, 435, 320, 30);
        backToLoginButton.setFont(baseFont.deriveFont(Font.ITALIC, 22f));
        backToLoginButton.setForeground(new Color(50, 50, 50));
        backToLoginButton.setBackground(new Color(255, 255, 255, 0));
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLoginButton.addActionListener(e -> switchToLoginForm());
        registerFormPanel.add(backToLoginButton);

        regMessageLabel = new JLabel(" ", SwingConstants.CENTER);
        regMessageLabel.setBounds(40, 380, 320, 55);
        regMessageLabel.setFont(baseFont.deriveFont(Font.ITALIC, 20f));
        registerFormPanel.add(regMessageLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this);
        } else {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

                GradientPaint gradient = new GradientPaint(0, 0, new Color(120, 40, 60),
                        getWidth(), getHeight(),
                        new Color(90, 20, 40));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);

                try {
                    Font customFont = Font.createFont(Font.TRUETYPE_FONT,
                                    new File("D:/term 4/adv pl/hotel system project/src/welcomPage/src/welcomPage/src/Dancing_Script/DancingScript-VariableFont_wght.ttf"))
                            .deriveFont(28f);
                    g2.setFont(customFont);
                } catch (Exception e) {
                    g2.setFont(new Font("SansSerif", Font.BOLD, 28));
                }

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text) - 40) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;
                g2.drawString(text, x, y);

                g2.fillPolygon(new int[]{getWidth() - 45, getWidth() - 25, getWidth() - 45},
                        new int[]{getHeight() / 2 - 12, getHeight() / 2, getHeight() / 2 + 12}, 3);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void switchToRegisterForm() {
        titleLabel.setText("Employee Registration");

        loginFormPanel.setVisible(false);
        registerFormPanel.setVisible(true);
    }
    private void switchToLoginForm() {
        titleLabel.setText("Employee Login");

        regIdField.setText("");
        regUsernameField.setText("");
        regPasswordField.setText("");
        regConfirmPasswordField.setText("");
        regMessageLabel.setText(" ");

        registerFormPanel.setVisible(false);
        loginFormPanel.setVisible(true);
    }
    private void loginAction() {
        String type = (String) userTypeCombo.getSelectedItem();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            showMessage(" Please enter username and password.", Color.RED);
            return;
        }

        if (type.equalsIgnoreCase("manager")) {
            if (username.equals("shahd") && password.equals("123456")) {
                showMessage(" Login successful. Welcome, " + username + "!", Color.GREEN);
            } else {
                showMessage(" Invalid manager credentials.", Color.RED);
            }
        } else {
            String filename = "src/data/employees.txt";
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2 && parts[0].trim().equals(username) && parts[1].trim().equals(password)) {
                        showMessage(" Login successful. Welcome, " + username + "!", Color.GREEN);
                        return;
                    }
                }
                showMessage(" Incorrect username or password.", Color.RED);
            } catch (IOException ex) {
                showMessage(" Error reading employees file.", Color.RED);
            }
        }
    }
    private void registerAction() {
        String type = (String) userTypeCombo.getSelectedItem();
        String inputId = regIdField.getText().trim();
        String username = regUsernameField.getText().trim();
        String password = new String(regPasswordField.getPassword()).trim();
        String confirmPassword = new String(regConfirmPasswordField.getPassword()).trim();
        if (!type.equalsIgnoreCase("employee")) {
            showRegMessage(" Only employees can register.", Color.RED);
            return;
        }

        if (inputId.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showRegMessage(" All fields are required.", Color.RED);
            return;
        }
        if (password.length() < 6) {
            showRegMessage(" Password must be at least 6 characters.", Color.RED);
            return;
        }
        if (!password.equals(confirmPassword)) {
            showRegMessage(" Passwords do not match.", Color.RED);
            return;
        }

        boolean idExists = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/data/employee_ids.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].trim().equals(inputId.trim())) {
                    idExists = true;
                    break;
                }
            }
        } catch (IOException ex) {
            showRegMessage(" Failed to read employee IDs.", Color.RED);
            return;
        }
        if (!idExists) {
            showRegMessage(" Invalid Employee ID. Contact your manager.", Color.RED);
            return;
        }
        if (projectpl2hotel.UserExist.userExists(type, username, this)) {
            showRegMessage(" Username already exists. Try another one.", Color.RED);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/employees.txt", true))) {
            writer.write(username + "," + password);
            writer.newLine();
            showRegMessage(" Registration successful!", new Color(0, 153, 76));

            Timer timer = new Timer(2000, event -> {
                switchToLoginForm();
                showMessage(" Registration successful! Please login.", new Color(0, 153, 76));
            });
            timer.setRepeats(false);
            timer.start();
        } catch (IOException ex) {
            showRegMessage(" Error saving new user.", Color.RED);
        }
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    private void showRegMessage(String message, Color color) {
        regMessageLabel.setText(message);
        regMessageLabel.setForeground(color);
    }

    static class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int cornerRadius = 15;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            cornerRadius = radius;
            backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}*/