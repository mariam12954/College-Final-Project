package projectpl2hotel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserExist {
    public static boolean userExists(String type, String username) {
        String filename = type.equals("manager") ? "data/managers.txt" : "data/employees.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
        }
        return false;
    }
}
