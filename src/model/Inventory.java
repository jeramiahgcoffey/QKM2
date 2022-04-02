package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part part) {
        allParts.add(part);
    }

    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    public static Part lookupPart(int partId) {
        ObservableList<Part> allParts = getAllParts();
        for(Part part : allParts){
            if(part.getId() == partId){
                return part;
            }
        }
        return null;
    }

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

    public static Product lookupProduct(int productId) {
        ObservableList<Product> allProducts = getAllProducts();
        for(Product product : allProducts){
            if(product.getId() == productId){
                return product;
            }
        }
        return null;
    }

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

    public static void updatePart(int index, Part newPart) {
        getAllParts().set(index, newPart);

    }

    public static void updateProduct(int index, Product newProduct) {
        getAllProducts().set(index, newProduct);
    }

    public static boolean deletePart(Part part) {
        return allParts.remove(part);
    }

    public static boolean deleteProduct(Product product) {
        return allProducts.remove(product);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
