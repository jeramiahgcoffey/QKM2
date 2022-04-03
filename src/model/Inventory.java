package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Locale;
/**
 *
 * Models an inventory of Parts and Products.
 * @author Jeramiah Coffey
 */
public class Inventory {

    /**
     * List of all Parts in the Inventory.
     */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * List of all Products in the Inventory.
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Add a Part to the Inventory.
     * @param part the Part object to add
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     * Add a Product to the Inventory.
     * @param product the Product object to add
     */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /**
     * Lookup a Part by its ID.
     * @param partId the id of the Part to lookup
     * @return the matching Part
     */
    public static Part lookupPart(int partId) {
        ObservableList<Part> allParts = getAllParts();
        for(Part part : allParts){
            if(part.getId() == partId){
                return part;
            }
        }
        return null;
    }

    /**
     * Lookup a Part by its name.
     * @param partName the name of the Part to lookup
     * @return the matching Part
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> allParts = getAllParts();
        ObservableList<Part> results = FXCollections.observableArrayList();
        for(Part part : allParts){
            if(part.getName().toLowerCase(Locale.ROOT).contains(partName.toLowerCase(Locale.ROOT))){
                results.add(part);
            }
        }
        return results;
    }

    /**
     * Lookup a Product by its ID.
     * @param productId the id of the Product to lookup
     * @return the matching Product
     */
    public static Product lookupProduct(int productId) {
        ObservableList<Product> allProducts = getAllProducts();
        for(Product product : allProducts){
            if(product.getId() == productId){
                return product;
            }
        }
        return null;
    }

    /**
     * Lookup a Product by its name.
     * @param productName the name of the Product to lookup
     * @return the matching Product
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> allProducts = getAllProducts();
        ObservableList<Product> results = FXCollections.observableArrayList();
        for(Product product : allProducts){
            if(product.getName().toLowerCase(Locale.ROOT).contains(productName.toLowerCase(Locale.ROOT))){
                results.add(product);
            }
        }
        return results;
    }

    /**
     * Replaces a Part in the Inventory with a modified version.
     * @param index the index of the Part within the Inventory
     * @param newPart the modified Part
     */
    public static void updatePart(int index, Part newPart) {
        getAllParts().set(index, newPart);

    }

    /**
     * Replaces a Product in the Inventory with a modified version.
     * @param index the index of the Product within the Inventory
     * @param newProduct the modified Product
     */
    public static void updateProduct(int index, Product newProduct) {
        getAllProducts().set(index, newProduct);
    }

    /**
     * Deletes a Part from the Inventory.
     * @param part the Part object to delete
     * @return boolean representing the success or failure of deletion
     */
    public static boolean deletePart(Part part) {
        return allParts.remove(part);
    }

    /**
     * Deletes a Product from the Inventory.
     * @param product the Product object to delete
     * @return boolean representing the success or failure of deletion
     */
    public static boolean deleteProduct(Product product) {
        return allProducts.remove(product);
    }

    /**
     * Retrieve list of Parts from the Inventory.
     * @return a list of Parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Retrieve list of Products from the Inventory.
     * @return a list of Products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
