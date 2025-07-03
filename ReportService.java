package projectpl2hotel;

import java.io.*;
import java.util.*;
import projectpl2hotel.CustomerService;
public class ReportService {
    public static void viewReports() {
        File file = new File(CustomerService.SERVICE_ASSIGN_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("❌ No service assignments found.");
            return;
        }

        Map<String, Integer> countMap = new HashMap<>();
        Map<String, Double> totalMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String service = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());

                    countMap.put(service, countMap.getOrDefault(service, 0) + 1);
                    totalMap.put(service, totalMap.getOrDefault(service, 0.0) + price);
                }
            }

            System.out.println("===== 📊 Service Report =====");
            for (String service : countMap.keySet()) {
                System.out.println(service + ": requested " + countMap.get(service) + " times | total $" + totalMap.get(service));
            }

        } catch (IOException e) {
            System.out.println("❌ Error reading report file.");
        }
    }
}
