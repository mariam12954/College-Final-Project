package projectpl2hotel;

import java.util.Scanner;

public class EmployeeMenu {
    public static void show(employee employee) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("===== Employee Menu =====");
            System.out.println("1. Add Customer");
            System.out.println("2. Update Customer");
            System.out.println("3. Remove Customer");
            System.out.println("4. View Customers");
            System.out.println("5. Add Service for Customer");
            System.out.println("6. Remove Service for Customer");
            System.out.println("7. view Customer services");
            System.out.println("8.  add new room");
            System.out.println("9. update room");
            System.out.println("10. remove room");
            System.out.println("11. Print Invoice for Customer");
            System.out.println("12. view nearest customers checkout");
            System.out.println("13. view waiting customer");
            System.out.println("14. add Service From Services");
            System.out.println("15. delete Service From Services");
            System.out.println("16. Edit Service");
            System.out.println("17. Go back");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    CustomerService.addCustomer();
                    break;
                case 2:
                    CustomerService.updateCustomer();
                    break;
                case 3:
                    CustomerService.removeCustomer();
                    break;
                case 4:
                    CustomerService.viewCustomers();
                    break;
                case 5:
                    CustomerService.addServiceToCustomer();
                    break;
                case 6:
                    CustomerService.removeServiceFromCustomer();
                    break;
                case 7:
                    CustomerService.viewCustomerServices();
                    break;
                case 8:
                    CustomerService.addRoom();
                    break;
                case 9:
                    CustomerService.updateRoom();
                    break;
                case 10:
                    CustomerService.removeRoom();
                    break;
                case 11:
                    CustomerService.printInvoiceForCustomer();
                    break;
                case 12:
                    CustomerService.CheckoutManager.showUpcomingCheckouts();
                    break;
                case 13:
                    CustomerService.viewWaitingList();
                    break;
                case 14:
                    CustomerService.addServiceFromServices();
                    break;
                case 15:
                    CustomerService.deleteServiceFromServices();
                    break;
                case 16:
                    CustomerService.editService();
                    break;
                case 17:
                    System.out.println("Returning to main menu...");
                    return;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }
}


/*package projectpl2hotel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeMenu extends JFrame {
    // Core components
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JTextArea outputArea;
    private CardLayout cardLayout;
    private JPanel cardsPanel;
    private JLabel statusLabel;

    // Console redirection
    private PrintStream originalOut;
    private PrintStream printStream;
    private PipedOutputStream pipedOut;
    private PipedInputStream pipedIn;

    // UI Constants
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color ACCENT_COLOR = new Color(231, 76, 60);
    private final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private final Color TEXT_COLOR = new Color(236, 240, 241);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    private final Font MENU_FONT = new Font("Arial", Font.BOLD, 14);
    private final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 14);

    // Employee reference
    private employee currentEmployee;

    // Menu buttons list
    private List<JButton> menuButtons = new ArrayList<>();

    public EmployeeMenu(employee employee) {
        this.currentEmployee = employee;

        // Basic frame setup
        setTitle("Lunaria Hotel Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize components
        setupHeader();
        setupSidebar();
        setupContentArea();
        redirectSystemOut();

        // Add welcome message
        outputArea.append("Welcome to Lunaria Hotel Management System\n");
        outputArea.append("Please select an option from the sidebar\n");

        // Show the frame
        setVisible(true);
    }

    private void setupHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Hotel logo/name on left
        JLabel hotelLabel = new JLabel("Lunaria Hotel");
        hotelLabel.setFont(TITLE_FONT);
        hotelLabel.setForeground(TEXT_COLOR);

        // Status info on right
        statusLabel = new JLabel("Employee: " + (currentEmployee != null ? currentEmployee.toString() : "Staff"));
        statusLabel.setFont(TEXT_FONT);
        statusLabel.setForeground(TEXT_COLOR);

        headerPanel.add(hotelLabel, BorderLayout.WEST);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void setupSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_COLOR);
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));

        // Menu category: Customers
        addMenuCategory("CUSTOMERS");
        addMenuButton("Add Customer", 1);
        addMenuButton("Update Customer", 2);
        addMenuButton("Remove Customer", 3);
        addMenuButton("View Customers", 4);

        // Menu category: Services
        addMenuCategory("SERVICES");
        addMenuButton("Add Service for Customer", 5);
        addMenuButton("Remove Service from Customer", 6);
        addMenuButton("View Customer Services", 7);
        addMenuButton("Print Invoice", 11);

        // Menu category: Rooms
        addMenuCategory("ROOMS");
        addMenuButton("Add New Room", 8);
        addMenuButton("Update Room", 9);
        addMenuButton("Remove Room", 10);

        // Menu category: Administration
        addMenuCategory("ADMINISTRATION");
        addMenuButton("View Nearest Checkouts", 12);
        addMenuButton("View Waiting List", 13);
        addMenuButton("Manage Services", 14);

        // Menu category: System
        addMenuCategory("SYSTEM");
        addMenuButton("Logout", 18);

        add(sidebarPanel, BorderLayout.WEST);
    }

    private void addMenuCategory(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        sidebarPanel.add(titleLabel);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(200, 1));
        separator.setForeground(new Color(99, 110, 114, 150));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(separator);
    }

    private void addMenuButton(String text, int actionId) {
        JButton button = new JButton(text);
        button.setFont(MENU_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(SIDEBAR_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Special button for Manage Services
        if (actionId == 14) {
            button.addActionListener(e -> showServicesManagementDialog());
        } else {
            final int choice = actionId;
            button.addActionListener(e -> handleMenuAction(choice));
        }

        // Mouse hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 73, 94));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(SIDEBAR_COLOR);
            }
        });

        menuButtons.add(button);
        sidebarPanel.add(button);

        // Add a bit of space between buttons
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 2)));
    }

    private void setupContentArea() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        // Card layout for different screens
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(Color.WHITE);

        // Output console area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBackground(new Color(245, 246, 250));
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Add to content panel
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void redirectSystemOut() {
        try {
            originalOut = System.out; // Store the original System.out
            pipedOut = new PipedOutputStream();
            pipedIn = new PipedInputStream(pipedOut);
            printStream = new PrintStream(pipedOut);
            System.setOut(printStream);

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(pipedIn))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String outputLine = line;
                        SwingUtilities.invokeLater(() -> outputArea.append(outputLine + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMenuAction(int choice) {
        // Clear console before each action
        outputArea.setText("");

        new Thread(() -> {
            switch (choice) {
                case 1: CustomerService.addCustomer(); break;
                case 2: CustomerService.updateCustomer(); break;
                case 3: CustomerService.removeCustomer(); break;
                case 4: CustomerService.viewCustomers(); break;
                case 5: CustomerService.addServiceToCustomer(); break;
                case 6: CustomerService.removeServiceFromCustomer(); break;
                case 7: CustomerService.viewCustomerServices(); break;
                case 8: CustomerService.addRoom(); break;
                case 9: CustomerService.updateRoom(); break;
                case 10: CustomerService.removeRoom(); break;
                case 11: CustomerService.printInvoiceForCustomer(); break;
                case 12: CustomerService.CheckoutManager.showUpcomingCheckouts(); break;
                case 13: CustomerService.viewWaitingList(); break;
                // Management of services is handled by the dialog
                case 18:
                    SwingUtilities.invokeLater(() -> {
                        int confirm = JOptionPane.showConfirmDialog(
                                this,
                                "Are you sure you want to logout?",
                                "Confirm Logout",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            outputArea.append("Logging out...\n");
                            restoreSystemOut();
                            dispose();
                            System.exit(0);
                        }
                    });
                    break;
                default:
                    SwingUtilities.invokeLater(() ->
                            outputArea.append("Feature not implemented yet.\n"));
            }
        }).start();
    }

    private void showServicesManagementDialog() {
        JDialog servicesDialog = new JDialog(this, "Services Management", true);
        servicesDialog.setSize(400, 300);
        servicesDialog.setLocationRelativeTo(this);
        servicesDialog.setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addServiceButton = createActionButton("Add New Service", e -> {
            servicesDialog.dispose();
            outputArea.setText("");
            new Thread(() -> CustomerService.addServiceFromServices()).start();
        });

        JButton editServiceButton = createActionButton("Edit Service", e -> {
            servicesDialog.dispose();
            outputArea.setText("");
            new Thread(() -> CustomerService.editService()).start();
        });

        JButton deleteServiceButton = createActionButton("Delete Service", e -> {
            servicesDialog.dispose();
            outputArea.setText("");
            new Thread(() -> CustomerService.deleteServiceFromServices()).start();
        });

        buttonsPanel.add(addServiceButton);
        buttonsPanel.add(editServiceButton);
        buttonsPanel.add(deleteServiceButton);

        servicesDialog.add(buttonsPanel, BorderLayout.CENTER);
        servicesDialog.setVisible(true);
    }

    private JButton createActionButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(MENU_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(new EmptyBorder(10, 10, 10, 10));
        button.setFocusPainted(false);
        button.addActionListener(action);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private void restoreSystemOut() {
        if (originalOut != null) {
            System.setOut(originalOut);
        }

        try {
            if (pipedOut != null) pipedOut.close();
            if (pipedIn != null) pipedIn.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Window adapter to restore System.out when closing
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                restoreSystemOut();
            }
        });
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            employee emp = new employee();
            new EmployeeMenu(emp);
        });
    }
}*/