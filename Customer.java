package projectpl2hotel;

import java.time.LocalDate;

public class Customer {
    private String id;
    private String name;
    private String phone;
    private String email;
    private Room room;
    private int stayDays;
    private LocalDate checkInDate;

    public Customer(String id, String name, String phone, String email, Room room, int stayDays, LocalDate checkInDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.room = room;
        this.stayDays = stayDays;
        this.checkInDate = checkInDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Room getRoom() { return room; }
    public int getStayDays() { return stayDays; }
    public LocalDate getCheckInDate() { return checkInDate; }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Name: " + name +
                ", Phone: " + phone +
                ", Email: " + email +
                ", Room: " + room.toString() +
                ", Stay Days: " + stayDays +
                ", Check-in Date: " + checkInDate;
    }
}
