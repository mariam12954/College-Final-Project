package projectpl2hotel;

import java.util.Scanner;

public class ManagerMenu {
    public static void show(Manager manager) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("===== Manager Menu =====");
            System.out.println("1. View Customer Reports");
            System.out.println("2. Add Employee");
            System.out.println("3. Remove Employee");
            System.out.println("4. View Employees");
            System.out.println("5. Update Employees");
            System.out.println("6. Go back");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    ReportService.viewReports();
                    break;
                case 2:
                    EmployeeService.addEmployee();
                    break;
                case 3:
                    EmployeeService.removeEmployee();
                    break;
                case 4:
                    EmployeeService.viewEmployees();
                    break;
                case 5:
                    EmployeeService.updateEmployee();
                case 6:
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
