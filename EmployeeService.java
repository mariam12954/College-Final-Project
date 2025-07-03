package projectpl2hotel;

import java.io.*;
import java.util.Scanner;

public class EmployeeService {
    public EmployeeService(employee emp) {
    }

    public static void addEmployee() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter gender (Male/Female): ");
        String gender = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        String id = "EMP" + (int) (Math.random() * 10000);
        System.out.println("Assigned ID for employee: " + id);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/employee_ids.txt", true))) {
            writer.write(id + "," + name + "," + age + "," + gender + "," + phone + "\n");
        } catch (IOException e) {
            System.out.println("❌ Failed to save employee.");
            return;
        }

        System.out.println("✅ Employee added successfully. Inform them to use the ID for registration.");
    }


    public static void removeEmployee() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter employee ID to remove: ");
        String idToRemove = scanner.nextLine().trim();

        File inputFile = new File("data/employee_ids.txt");
        File tempFile = new File("data/temp_employee_ids.txt");

        boolean found = false;

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(idToRemove + ",")) {
                    found = true;
                    continue; // Skip this line
                }
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("❌ Error processing the file.");
            return;
        }

        if (!found) {
            System.out.println("❌ Employee ID not found.");
            tempFile.delete(); // Clean up
            return;
        }

        if (inputFile.delete()) {
            if (tempFile.renameTo(inputFile)) {
                System.out.println("✅ Employee removed successfully.");
            } else {
                System.out.println("❌ Failed to rename temp file.");
            }
        } else {
            System.out.println("❌ Failed to delete original file.");
        }
    }

    public static void viewEmployees() {
        File file = new File("data/employee_ids.txt");

        if (!file.exists()) {
            System.out.println("❌ No employees found.");
            return;
        }

        System.out.println("\n===== List of Employees =====");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    System.out.println("ID: " + parts[0] + " | Name: " + parts[1] +
                            " | Age: " + parts[2] + " | Gender: " + parts[3] +
                            " | Phone: " + parts[4]);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No employees registered.");
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading employee file.");
        }
    }

    public static void updateLoginFile(String oldName, String newName, String newPassword) {
        File inputFile = new File("data/employees.txt");
        File tempFile = new File("data/temp_employees.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(oldName)) {
                    writer.write(newName + "," + newPassword + "\n");
                } else {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error updating employee data.");
            return;
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            System.out.println("❌ Could not replace the original file.");
        }
    }


    public static void updateEmployee() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter employee ID to update: ");
        String idToUpdate = scanner.nextLine().trim();

        File inputFile = new File("data/employee_ids.txt");
        File tempFile = new File("data/temp_employee_ids.txt");

        boolean found = false;
        String oldName = "";

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equals(idToUpdate)) {
                    found = true;
                    oldName = parts[1];

                    System.out.println("Leave field empty to keep existing value.");

                    // ✅ اسم المستخدم - لازم يتكتب
                    String newName;
                    do {
                        System.out.print("Enter new name [" + parts[1] + "]: ");
                        newName = scanner.nextLine().trim();
                        if (newName.isEmpty()) {
                            System.out.println("❌ Username cannot be empty.");
                        }
                    } while (newName.isEmpty());

                    System.out.print("Enter new age [" + parts[2] + "]: ");
                    String newAge = scanner.nextLine().trim();
                    if (newAge.isEmpty()) newAge = parts[2];

                    System.out.print("Enter new gender [" + parts[3] + "]: ");
                    String newGender = scanner.nextLine().trim();
                    if (newGender.isEmpty()) newGender = parts[3];

                    System.out.print("Enter new phone [" + parts[4] + "]: ");
                    String newPhone = scanner.nextLine().trim();
                    if (newPhone.isEmpty()) newPhone = parts[4];

                    // ✅ الباسورد - لازم يتكتب
                    String newPassword;
                    do {
                        System.out.print("Enter new password (for login): ");
                        newPassword = scanner.nextLine().trim();
                        if (newPassword.isEmpty()) {
                            System.out.println("❌ Password cannot be empty.");
                        }
                    } while (newPassword.isEmpty());

                    String updatedLine = idToUpdate + "," + newName + "," + newAge + "," + newGender + "," + newPhone;
                    writer.write(updatedLine + "\n");

                    // ✅ تحديث بيانات تسجيل الدخول
                    updateLoginFile(oldName, newName, newPassword);
                } else {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error updating employee.");
            return;
        }

        if (!found) {
            System.out.println("❌ Employee ID not found.");
            tempFile.delete();
            return;
        }

        if (inputFile.delete()) {
            if (tempFile.renameTo(inputFile)) {
                System.out.println("✅ Employee and login info updated successfully.");
            } else {
                System.out.println("❌ Failed to rename temp file.");
            }
        } else {
            System.out.println("❌ Failed to delete original file.");
        }
    }




}