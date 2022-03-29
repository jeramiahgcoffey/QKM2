package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainForm implements Initializable {
    public TableView<Part> partsTable;
    public TableView<Product> productsTable;
    public TableColumn<Number, Integer> partIdColumn;
    public TableColumn<Character, String> partNameColumn;
    public TableColumn<Number, Integer> partInventoryLevelColumn;
    public TableColumn<Number, Double> partPriceColumn;
    public TableColumn<Number, Integer> productIdColumn;
    public TableColumn<Character, String> productNameColumn;
    public TableColumn<Number, Integer> productInventoryLevelColumn;
    public TableColumn<Number, Double> productPriceColumn;
    public TextField partQueryTF;
    public TextField productQueryTF;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(Inventory.getAllProducts());

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void handlePartQuery() {
        String query = partQueryTF.getText();
        partsTable.setItems(partSearch(query));

    }

    public void handleProductQuery() {
        String query = productQueryTF.getText();
        productsTable.setItems(productSearch(query));

    }

    private ObservableList<Part> partSearch(String query) {
        ObservableList<Part> results = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part part : allParts){
            if(part.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) || String.valueOf(part.getId()).equals(query)){
                results.add(part);
            }
        }

        return results;
    }

    private ObservableList<Product> productSearch(String query) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(Product product: allProducts){
            if(product.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) || String.valueOf(product.getId()).equals(query)){
                results.add(product);
            }
        }

        return results;
    }

    public void onDeletePart() {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) { return; }
        Inventory.deletePart(selectedPart);
        partsTable.setItems(partSearch(partQueryTF.getText()));
    }

    public void onDeleteProduct() {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) { return; }
        Inventory.deleteProduct(selectedProduct);
        productsTable.setItems(productSearch(productQueryTF.getText()));
    }

    public void toAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toModifyPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toModifyProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
