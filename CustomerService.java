package projectpl2hotel;


import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CustomerService {

    static String CUSTOMER_FILE = "data/customers.txt";
    static String ROOM_FILE = "data/rooms.txt";
    static String SERVICE_FILE = "data/services.txt";
    public static String SERVICE_ASSIGN_FILE = "data/customer_services.txt";

    static List<Room> rooms = new ArrayList<>();

    static {
        loadRooms();
    }

    // 🧱 Ensure file exists
    private static void ensureFileExists(String path) {
        File file = new File(path);
        try {
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("❌ Couldn't create file: " + path);
        }
    }

    // 🧍 Add new customer
    public static void addCustomer() {
        ensureFileExists(CUSTOMER_FILE);
        Scanner scanner = new Scanner(System.in);

        // إدخال البيانات الأساسية
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter desired room type (Single/Double/Suite): ");
        String desiredType = scanner.nextLine();

        List<Room> availableRooms = getAvailableRoomsByType(desiredType);

        if (availableRooms.isEmpty()) {
            System.out.println("❌ No available rooms of type " + desiredType);
            System.out.print("Would you like to join the waiting list? (yes/no): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("yes")) {
                System.out.print("Enter number of days needed: ");
                int numberOfDays = scanner.nextInt();
                scanner.nextLine(); // لقراءة newline

                // إضافة لقائمة الانتظار باستخدام البيانات المدخلة
                try (FileWriter writer = new FileWriter(WAITING_LIST_FILE, true)) {
                    writer.write(id + "," + name + "," + phone + "," + email + "," + desiredType + "," + numberOfDays + "\n");
                    System.out.println("✅ Customer added to waiting list successfully.");
                } catch (IOException e) {
                    System.out.println("❌ Failed to add customer to waiting list.");
                }
            }
            return;
        }

        // باقي الكود الأصلي لاختيار الغرفة...
        System.out.println("Available Rooms:");
        for (Room room : availableRooms) {
            System.out.println("Room ID: " + room.getId() + " - Subtype: " + room.getSubtype() + " - Price: $" + room.getPrice());
        }

        System.out.print("Enter Room ID to assign: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();

        Room selectedRoom = null;
        for (Room room : availableRooms) {
            if (room.getId() == roomId) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("❌ Invalid room selection.");
            return;
        }

        selectedRoom.setAvailable(false);

        System.out.print("Enter number of days: ");
        int numberOfDays = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();

        try (FileWriter writer = new FileWriter(CUSTOMER_FILE, true)) {
            writer.write(id + "," + name + "," + phone + "," + email + "," + roomId + "," + numberOfDays + "," + dateStr + "\n");
            System.out.println("✅ Customer added successfully.");
        } catch (IOException e) {
            System.out.println("❌ Failed to add customer.");
        }

        saveRooms();
    }
    public static void updateCustomer() {
        ensureFileExists(CUSTOMER_FILE);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter customer ID to update: ");
        String id = scanner.nextLine();

        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // تأكد إن السطر فيه 7 عناصر
                if (parts.length < 7) {
                    System.out.println("⚠️ Skipping invalid line: " + line);
                    lines.add(line);
                    continue;
                }

                if (parts[0].trim().equals(id.trim())) {
                    found = true;

                    System.out.println("Current name: " + parts[1]);
                    System.out.print("Enter new name (leave blank to keep current): ");
                    String newName = scanner.nextLine();
                    if (newName.isEmpty()) newName = parts[1];

                    System.out.println("Current phone: " + parts[2]);
                    System.out.print("Enter new phone (leave blank to keep current): ");
                    String newPhone = scanner.nextLine();
                    if (newPhone.isEmpty()) newPhone = parts[2];

                    System.out.println("Current email: " + parts[3]);
                    System.out.print("Enter new email (leave blank to keep current): ");
                    String newEmail = scanner.nextLine();
                    if (newEmail.isEmpty()) newEmail = parts[3];

                    System.out.println("Current number of days: " + parts[5]);
                    System.out.print("Enter new number of days (or press Enter to keep current): ");
                    String daysInput = scanner.nextLine();
                    String newDays = daysInput.isEmpty() ? parts[5] : daysInput;

                    // إعادة بناء السطر بعد التحديث
                    String updatedLine = parts[0] + "," + newName + "," + newPhone + "," + newEmail + "," + parts[4] + "," + newDays + "," + parts[6];
                    lines.add(updatedLine);
                } else {
                    lines.add(line); // احتفظ بالسطر كما هو
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read customer data.");
            return;
        }

        if (!found) {
            System.out.println("❌ Customer ID not found.");
            return;
        }

        // إعادة كتابة الملف بعد التحديث
        try (FileWriter writer = new FileWriter(CUSTOMER_FILE)) {
            for (String l : lines) {
                writer.write(l + "\n");
            }
            System.out.println("✅ Customer updated successfully.");
        } catch (IOException e) {
            System.out.println("❌ Failed to update customer.");
        }
    }


    // 👁️ View customers
    public static void viewCustomers() {
        ensureFileExists(CUSTOMER_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            System.out.println("📋 Customers List:");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    System.out.println("ID: " + parts[0] + ", Name: " + parts[1] + ", Phone: " + parts[2]
                            + ", Email: " + parts[3] + ", Room ID: " + parts[4]
                            + ", Days: " + parts[5] + ", Date: " + parts[6]);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read customers.");
        }
    }

    // ❌ Remove customer
    public static void removeCustomer() {
        ensureFileExists(CUSTOMER_FILE);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer ID to remove: ");
        String idToRemove = scanner.nextLine().trim();

        List<String> remainingLines = new ArrayList<>();
        boolean found = false;
        int freedRoomId = -1;
        String freedRoomType = ""; // متغير جديد لتخزين نوع الغرفة المحررة

        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[0].trim().equals(idToRemove)) {
                    found = true;
                    try {
                        freedRoomId = Integer.parseInt(parts[4].trim());
                        // تحديد نوع الغرفة المحررة
                        for (Room room : rooms) {
                            if (room.getId() == freedRoomId) {
                                freedRoomType = room.getType();
                                break;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Invalid room ID format for this customer.");
                    }
                } else {
                    remainingLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read customer file.");
            return;
        }

        if (!found) {
            System.out.println("❌ Customer ID " + idToRemove + " not found.");
            return;
        }

        // Free the room if applicable
        for (Room room : rooms) {
            if (room.getId() == freedRoomId) {
                room.setAvailable(true);
                break;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (String line : remainingLines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to update customer file.");
            return;
        }

        saveRooms();
        System.out.println("✅ Customer removed and room freed.");

        // الجديد: التحقق من قائمة الانتظار عند تحرير غرفة
        if (!freedRoomType.isEmpty()) {
            checkWaitingList(freedRoomType);
        }
    }

    // 🛏️ Add new room
    public static void addRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter type: ");
        String type = scanner.nextLine();
        System.out.print("Enter subtype: ");
        String subtype = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();

        rooms.add(new Room(id, type, subtype, price, true));
        saveRooms();
        System.out.println("✅ Room added successfully.");
    }
    public static void updateRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Room roomToUpdate = null;
        for (Room room : rooms) {
            if (room.getId() == id) {
                roomToUpdate = room;
                break;
            }
        }

        if (roomToUpdate != null) {

            // ✅ تحقق إن الغرفة متاحة قبل التعديل
            if (!roomToUpdate.isAvailable()) {
                System.out.println("❌ Cannot update a booked room.");
                return;
            }

            System.out.print("Enter new type (current: " + roomToUpdate.getType() + "): ");
            String newType = scanner.nextLine();
            System.out.print("Enter new subtype (current: " + roomToUpdate.getSubtype() + "): ");
            String newSubtype = scanner.nextLine();
            System.out.print("Enter new price (current: " + roomToUpdate.getPrice() + "): ");
            double newPrice = scanner.nextDouble();

            roomToUpdate.setType(newType);
            roomToUpdate.setSubtype(newSubtype);
            roomToUpdate.setPrice(newPrice);

            saveRooms();
            System.out.println("✅ Room updated successfully.");

        } else {
            System.out.println("❌ Room ID not found.");
        }
    }


    public static void removeRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room ID to remove: ");
        int id = scanner.nextInt();
        Room roomToRemove = null;
        for (Room room : rooms) {
            if (room.getId() == id) {
                roomToRemove = room;
                break;
            }
        }

        if (roomToRemove != null) {
            rooms.remove(roomToRemove);
            saveRooms();
            System.out.println("✅ Room removed successfully.");
        } else {
            System.out.println("❌ Room ID not found.");
        }
    }

    // Add these constants to your class
    static String WAITING_LIST_FILE = "data/waiting_list.txt";

    // Add this method to generate default waiting list file
    private static void ensureWaitingListFile() {
        ensureFileExists(WAITING_LIST_FILE);
    }

    // Add this method to handle waiting list
    public static void addToWaitingList(String id, String name, String phone, String email, String desiredType, int numberOfDays) {
        ensureFileExists(WAITING_LIST_FILE);

        try (FileWriter writer = new FileWriter(WAITING_LIST_FILE, true)) {
            writer.write(id + "," + name + "," + phone + "," + email + "," + desiredType + "," + numberOfDays + "\n");
            System.out.println("✅ Customer added to waiting list successfully.");
        } catch (IOException e) {
            System.out.println("❌ Failed to add customer to waiting list.");
        }
    }
    // Add this method to check waiting list when a room becomes available
    private static void checkWaitingList(String roomType) {
        ensureFileExists(WAITING_LIST_FILE);
        List<String> remainingEntries = new ArrayList<>();
        boolean assigned = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(WAITING_LIST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (assigned) {
                    remainingEntries.add(line);
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[4].equalsIgnoreCase(roomType)) {
                    // Customer data
                    String id = parts[0];
                    String name = parts[1];
                    String phone = parts[2];
                    String email = parts[3];
                    int numberOfDays = Integer.parseInt(parts[5]);

                    // Try to assign room
                    List<Room> availableRooms = getAvailableRoomsByType(roomType);
                    if (!availableRooms.isEmpty()) {
                        Room selectedRoom = availableRooms.get(0);
                        selectedRoom.setAvailable(false);

                        // Add to customers file
                        try (FileWriter writer = new FileWriter(CUSTOMER_FILE, true)) {
                            writer.write(id + "," + name + "," + phone + "," + email + "," +
                                    selectedRoom.getId() + "," + numberOfDays + "," +
                                    LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "\n");
                            System.out.println("✅ Automatically assigned room " + selectedRoom.getId() +
                                    " to waiting list customer " + name);
                            assigned = true;
                            saveRooms();
                        } catch (IOException e) {
                            System.out.println("❌ Failed to assign room to waiting list customer.");
                            remainingEntries.add(line);
                        }
                    } else {
                        remainingEntries.add(line);
                    }
                } else {
                    remainingEntries.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read waiting list.");
            return;
        }

        // Update waiting list file
        try (FileWriter writer = new FileWriter(WAITING_LIST_FILE)) {
            for (String entry : remainingEntries) {
                writer.write(entry + "\n");
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to update waiting list file.");
        }
    }

    // Modify your removeCustomer method to call checkWaitingList


    // Add this method to view waiting list
    public static void viewWaitingList() {
        ensureFileExists(WAITING_LIST_FILE);

        try (BufferedReader reader = new BufferedReader(new FileReader(WAITING_LIST_FILE))) {
            String line;
            int counter = 1;

            System.out.println("\n📋 Waiting List Customers:");
            System.out.println("------------------------------------------------------------");
            System.out.println("No. | ID       | Name          | Phone       | Room Type | Days");
            System.out.println("------------------------------------------------------------");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    System.out.printf("%-3d | %-8s | %-13s | %-11s | %-9s | %-4s%n",
                            counter++,
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[4].trim(),
                            parts[5].trim());
                }
            }

            if (counter == 1) {
                System.out.println("No customers in waiting list currently.");
            }
            System.out.println("------------------------------------------------------------");

        } catch (IOException e) {
            System.out.println("❌ Error reading waiting list: " + e.getMessage());
        }
    }




    // 🧼 Internal: get available rooms
    private static List<Room> getAvailableRoomsByType(String type) {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable() && room.getType().equalsIgnoreCase(type)) {
                available.add(room);
            }
        }
        return available;
    }

    // 💾 Save rooms to file
    private static void saveRooms() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ROOM_FILE))) {
            for (Room room : rooms) {
                writer.write(room.getId() + "," + room.getType() + "," + room.getSubtype() + "," + room.getPrice() + "," + room.isAvailable() + "\n");
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save rooms.");
        }
    }

    // 🔁 Load rooms from file
    private static void loadRooms() {
        File file = new File(ROOM_FILE);
        try {
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean isEmpty = true;

            while ((line = reader.readLine()) != null) {
                isEmpty = false;
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0]);
                    String type = parts[1];
                    String subtype = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    boolean available = Boolean.parseBoolean(parts[4]);
                    rooms.add(new Room(id, type, subtype, price, available));
                }
            }
            reader.close();

            if (isEmpty) {
                System.out.println("⚠️ No rooms found. Generating default rooms...");
                generateDefaultRooms();
                saveRooms();
            }

        } catch (IOException e) {
            System.out.println("❌ Failed to load rooms.");
        }
    }

    // 🏗️ Default rooms if none exist
    private static void generateDefaultRooms() {
        String[] types = {"Single", "Double", "Suite"};
        String[] subtypes = {"Standard", "Sea View", "Deluxe"};
        double[] prices = {300.0, 500.0, 800.0};

        int id = 1;
        for (String type : types) {
            for (int i = 0; i < subtypes.length; i++) {
                rooms.add(new Room(id++, type, subtypes[i], prices[i], true));
            }
        }
    }



    private static void generateDefaultServices() {
        String SERVICE_FILE = "data/services.txt";
        ensureFileExists(SERVICE_FILE);
        File file = new File(SERVICE_FILE);

        try {
            if (file.length() == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("Breakfast,20.0\n");
                    writer.write("Laundry,15.0\n");
                    writer.write("Airport Pickup,50.0\n");
                    writer.write("Room Cleaning,10.0\n");
                    writer.write("Spa Access,30.0\n");
                    System.out.println("ℹ️ Default services created.");
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to generate default services.");
        }
    }


    // ➕ Assign service to customer
    public static void addServiceToCustomer() {
        ensureFileExists(SERVICE_FILE);
        ensureFileExists(SERVICE_ASSIGN_FILE);
        ensureFileExists(CUSTOMER_FILE); // تأكد إن ملف العملاء موجود
        generateDefaultServices();

        List<String> services = new ArrayList<>();
        List<Double> prices = new ArrayList<>();

        // قراءة الخدمات
        try (BufferedReader reader = new BufferedReader(new FileReader(SERVICE_FILE))) {
            String line;
            int index = 1;
            System.out.println("📋 Available Services:");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) { // ID, Name, Price
                    String serviceName = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    services.add(serviceName);
                    prices.add(price);
                    System.out.println(index + ". " + serviceName + " - $" + price);
                    index++;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read services.");
            return;
        }

        if (services.isEmpty()) {
            System.out.println("❌ No services available.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Customer ID to assign service: ");
        String customerId = scanner.nextLine().trim();

        // التحقق من وجود العميل
        boolean customerExists = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equals(customerId)) {
                    customerExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to check customer ID.");
            return;
        }

        if (!customerExists) {
            System.out.println("❌ Customer ID " + customerId + " not found.");
            return;
        }

        System.out.print("Enter service number to assign: ");
        int serviceIndex = scanner.nextInt();
        scanner.nextLine();

        if (serviceIndex < 1 || serviceIndex > services.size()) {
            System.out.println("❌ Invalid service selection.");
            return;
        }

        String selectedService = services.get(serviceIndex - 1);
        double selectedPrice = prices.get(serviceIndex - 1);

        try (FileWriter writer = new FileWriter(SERVICE_ASSIGN_FILE, true)) {
            writer.write(customerId + "," + selectedService + "," + selectedPrice + "\n");
            System.out.println("✅ Service assigned successfully.");
        } catch (IOException e) {
            System.out.println("❌ Failed to assign service.");
        }
    }


    // ❌ Remove services from customer
    public static void removeServiceFromCustomer() {
        ensureFileExists(SERVICE_ASSIGN_FILE);
        ensureFileExists(CUSTOMER_FILE);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Customer ID to remove service: ");
        String customerId = scanner.nextLine().trim();

        // التحقق من وجود العميل
        boolean customerExists = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equals(customerId)) {
                    customerExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to check customer ID.");
            return;
        }

        if (!customerExists) {
            System.out.println("❌ Customer ID " + customerId + " not found.");
            return;
        }

        // تجميع الخدمات المرتبطة بالعميل
        List<String> allAssignments = new ArrayList<>();
        List<String> customerServices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(SERVICE_ASSIGN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allAssignments.add(line);
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(customerId)) {
                    customerServices.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read service assignments.");
            return;
        }

        if (customerServices.isEmpty()) {
            System.out.println("❌ No services found for this customer.");
            return;
        }

        System.out.println("📋 Services assigned to Customer ID " + customerId + ":");
        for (int i = 0; i < customerServices.size(); i++) {
            String[] parts = customerServices.get(i).split(",");
            System.out.println((i + 1) + ". " + parts[1] + " ($" + parts[2] + ")");
        }

        System.out.print("Enter the number of the service to remove: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > customerServices.size()) {
            System.out.println("❌ Invalid selection.");
            return;
        }

        String serviceToRemove = customerServices.get(choice - 1);

        allAssignments.remove(serviceToRemove); // شيل السطر ده من كل التعيينات

        // إعادة كتابة الملف بدون الخدمة المحذوفة
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SERVICE_ASSIGN_FILE))) {
            for (String line : allAssignments) {
                writer.write(line + "\n");
            }
            System.out.println("✅ Service removed successfully.");
        } catch (IOException e) {
            System.out.println("❌ Failed to update customer services.");
        }
    }



    // 👁️ عرض الخدمات الخاصة بكل عميل
    public static void viewCustomerServices() {
        ensureFileExists(SERVICE_ASSIGN_FILE);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Customer ID to view services: ");
        String customerId = scanner.nextLine().trim();

        boolean found = false;
        System.out.println("📋 Services assigned to Customer ID " + customerId + ":");
        try (BufferedReader reader = new BufferedReader(new FileReader(SERVICE_ASSIGN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(customerId)) {
                    System.out.println("- " + parts[1] + " ($" + parts[2] + ")");
                    found = true;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read customer services.");
            return;
        }

        if (!found) {
            System.out.println("❌ No services found for this customer.");
        }
    }

    // 🧾 Generate invoice for a customer
    public static void printInvoiceForCustomer() {
        ensureFileExists(CUSTOMER_FILE);
        ensureFileExists(SERVICE_ASSIGN_FILE);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Customer ID to print invoice: ");
        String customerId = scanner.nextLine();

        String[] customerData = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[0].equals(customerId)) {
                    customerData = parts;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read customer data.");
            return;
        }

        if (customerData == null) {
            System.out.println("❌ Customer not found.");
            return;
        }

        String name = customerData[1];
        String phone = customerData[2];
        String email = customerData[3];
        int roomId = Integer.parseInt(customerData[4]);
        int days = Integer.parseInt(customerData[5]);
        String checkinDate = customerData[6];

        Room customerRoom = null;
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                customerRoom = room;
                break;
            }
        }

        if (customerRoom == null) {
            System.out.println("❌ Room not found.");
            return;
        }

        double roomCost = customerRoom.getPrice() * days;
        List<String> customerServices = new ArrayList<>();
        double servicesTotal = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(SERVICE_ASSIGN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(customerId)) {
                    customerServices.add(parts[1] + " - $" + parts[2]);
                    servicesTotal += Double.parseDouble(parts[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read service assignments.");
            return;
        }

        double total = roomCost + servicesTotal;

        // 🧾 Print invoice
        System.out.println("\n========== 🧾 INVOICE ==========");
        System.out.println("Customer: " + name);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Check-in Date: " + checkinDate);
        System.out.println("Room: #" + customerRoom.getId() + " (" + customerRoom.getType() + " - " + customerRoom.getSubtype() + ")");
        System.out.println("Room Price per Day: $" + customerRoom.getPrice());
        System.out.println("Number of Days: " + days);
        System.out.println("Room Total: $" + roomCost);
        System.out.println("\nServices:");
        if (customerServices.isEmpty()) {
            System.out.println("No additional services.");
        } else {
            for (String service : customerServices) {
                System.out.println(" - " + service);
            }
        }
        System.out.println("Services Total: $" + servicesTotal);
        System.out.println("---------------------------------");
        System.out.println("Total Amount Due: $" + total);
        System.out.println("=================================\n");
    }


    public static class CheckoutManager {

        private static final String CUSTOMER_FILE = "data/customers.txt"; // الملف في مجلد data
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        public static void showUpcomingCheckouts() {
            List<String> upcoming = new ArrayList<>();
            LocalDate today = LocalDate.now();

            try {
                ensureFileExists(String.valueOf(new File(CUSTOMER_FILE)));

                try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");

                        // تأكد إن السطر سليم
                        if (parts.length < 6) continue; // 6 لأننا بنحتاج number of days في الجزء السادس

                        String id = parts[0];
                        String name = parts[1];
                        String roomId = parts[4];

                        try {
                            int numberOfDays = Integer.parseInt(parts[5]);
                            long daysLeft = numberOfDays;

                            if (daysLeft > 0 && daysLeft <= 2) {
                                upcoming.add(" ID: " + id + " | name: " + name + " | roomid: " + roomId +
                                        " | checkout in " + daysLeft + " day");
                            }
                        } catch (NumberFormatException e) {
                            continue; // لو عدد الأيام مش رقم صحيح، نتجاهل العميل
                        }
                    }

                } catch (IOException e) {
                    System.out.println("❌ Failed to read customer data:\n " + e.getMessage());
                    return;
                }

                if (upcoming.isEmpty()) {
                    System.out.println("✅ No customers leaving in the next two days\n");
                } else {
                    System.out.println("📅 Customers leaving within the next two days:\n");
                    upcoming.forEach(System.out::println);
                }
            } catch (IOException e) {
                System.out.println("❌ Data file setup error:\n " + e.getMessage());
            }
        }

        private static void ensureFileExists(String filePath) throws IOException {
            File file = new File(filePath);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("The data folder could not be created.\n");
                }
            }

            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Data file could not be created.\n");
                }
            }
        }
    }


    //ADD
    public static void addServiceFromServices() {
        ensureFileExists("data/services.txt");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter service name: ");
        String name = scanner.nextLine();

        System.out.print("Enter service price: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid price. Please enter a valid number.");
            return;
        }

        try {
            List<String> services = Files.readAllLines(Paths.get("data/services.txt"));
            int newId = services.size() + 1;
            String newService = newId + "," + name + "," + price;
            services.add(newService);
            Files.write(Paths.get("data/services.txt"), services);
            System.out.println("✅ Service added successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error while adding the service.");
        }
    }
    //DELEDE
    public static void deleteServiceFromServices() {
        ensureFileExists("data/services.txt");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter service ID to delete: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID. Please enter a valid number.");
            return;
        }

        try {
            List<String> services = Files.readAllLines(Paths.get("data/services.txt"));
            List<String> updatedServices = new ArrayList<>();
            boolean found = false;

            // حذف الخدمة المطلوبة
            for (String line : services) {
                String[] parts = line.split(",");
                int currentId = Integer.parseInt(parts[0]);
                if (currentId != id) {
                    updatedServices.add(line);
                } else {
                    found = true;
                }
            }

            if (!found) {
                System.out.println("❌ Service ID not found.");
                return;
            }

            // إعادة ترقيم الـ IDs بعد الحذف
            List<String> renumberedServices = new ArrayList<>();
            int newId = 1;
            for (String line : updatedServices) {
                String[] parts = line.split(",");
                // parts[1] = name, parts[2] = price
                renumberedServices.add(newId + "," + parts[1] + "," + parts[2]);
                newId++;
            }

            Files.write(Paths.get("data/services.txt"), renumberedServices);
            System.out.println("🗑 Service deleted and IDs updated successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error while deleting the service.");
        }
    }

    public static void editService() {
        ensureFileExists("data/services.txt");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter service ID to edit: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID. Please enter a valid number.");
            return;
        }

        System.out.print("Enter new service name: ");
        String newName = scanner.nextLine();

        System.out.print("Enter new service price: ");
        double newPrice;
        try {
            newPrice = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid price. Please enter a valid number.");
            return;
        }

        try {
            List<String> services = Files.readAllLines(Paths.get("data/services.txt"));
            List<String> updatedServices = new ArrayList<>();
            boolean found = false;

            for (String line : services) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) == id) {
                    updatedServices.add(id + "," + newName + "," + newPrice);
                    found = true;
                } else {
                    updatedServices.add(line);
                }
            }

            if (found) {
                Files.write(Paths.get("data/services.txt"), updatedServices);
                System.out.println("✏ Service edited successfully.");
            } else {
                System.out.println("❌ Service ID not found.");
            }
        } catch (IOException e) {
            System.out.println("❌ Error while editing the service.");
        }
    }



}

