package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Models a product object.
 * @author Jeramiah Coffey
 */
public class Product {

    /**
     * List of parts associated with a product
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * The ID of a product
     */
    private int id;

    /**
     * The name of a product
     */
    private String name;

    /**
     * The price of a product
     */
    private double price;

    /**
     * The level of stock of a product
     */
    private int stock;

    /**
     * The minimum level of stock of a product
     */
    private int min;

    /**
     * The maximum level of stock of a product
     */
    private int max;

    /**
     * Constructor for an instance of a product object.
     * @param associatedParts list of parts associated with the product
     * @param id id of the product
     * @param name name of the product
     * @param price price of the product
     * @param stock level of stock of the product
     * @param min minimum level of stock of the product
     * @param max maximum level of stock of the product
     */
    public Product(ObservableList<Part> associatedParts, int id, String name, double price, int stock, int min, int max) {
        this.associatedParts = associatedParts;
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets the id of a product.
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name of a product.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the price of a product.
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the stock of a product.
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets the minimum stock level of a product.
     * @param min the minimum level to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets the maximum stock level of a product.
     * @param max the maximum level to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Gets the id of a product.
     * @return the product id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of a product.
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the price of a product.
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the stock of a product.
     * @return the product stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Gets the minimum stock level of a product.
     * @return the product min
     */
    public int getMin() {
        return min;
    }

    /**
     * Gets the maximum stock level of a product.
     * @return the product max
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds an associated part to a product.
     * @param part the part to add
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part from a product.
     * @param part the part to delete
     * @return boolean representing the success or failure of deletion
     */
    public boolean deleteAssociatedPart(Part part) {
        return associatedParts.remove(part);
    }

    /**
     * Gets a list of associated parts from a product.
     * @return a list of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
