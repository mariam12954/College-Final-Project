package projectpl2hotel;

public class Room {
    private int id;
    private String type;
    private String subtype;
    private double price;
    private boolean isAvailable;

    public Room(int id, String type, String subtype, double price, boolean isAvailable) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    @Override
    public String toString() {
        return "Room #" + id + " (" + type + " - " + subtype + ") - " + (isAvailable ? "Available" : "Occupied");
    }

    public void setType(String newType) {
        this.type=newType;
    }

    public void setPrice(double newPrice) {
        this.price=newPrice;
    }
}