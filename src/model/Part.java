package model;

public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public int getId() {
        return id;
    }
     public String getName() {
        return name;
     }

     public double getPrice() {
        return price;
     }

     public int getStock() {
        return stock;
     }

     public int getMin() {
        return min;
     }

    public int getMax() {
        return max;
    }
}
