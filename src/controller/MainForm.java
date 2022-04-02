package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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
    public Label mainPartSearchAlert;
    public Label mainPartEmptyAlert;
    public Label mainProductSearchAlert;
    public Label mainProductEmptyAlert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        partSearchAlertCheck(Inventory.getAllParts());
        productSearchAlertCheck(Inventory.getAllProducts());
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

    public void partSearchAlertCheck(ObservableList<Part> queryResults) {
        boolean emptyInventory = Inventory.getAllParts().isEmpty();
        boolean emptyQueryResults = queryResults.isEmpty();
        mainPartEmptyAlert.setVisible(emptyInventory);
        mainPartSearchAlert.setVisible(!emptyInventory && emptyQueryResults);
    }

    public void productSearchAlertCheck(ObservableList<Product> queryResults) {
        boolean emptyInventory = Inventory.getAllProducts().isEmpty();
        boolean emptyQuery = queryResults.isEmpty();
        mainProductEmptyAlert.setVisible(emptyInventory);
        mainProductSearchAlert.setVisible(!emptyInventory && emptyQuery);
    }

    public void handlePartQuery() {
        String query = partQueryTF.getText();
        ObservableList<Part> results = FXCollections.observableArrayList();
        try {
            Part partToAdd = Inventory.lookupPart(Integer.parseInt(query));
            if(partToAdd != null) {
                results.add(partToAdd);
            }
        } catch (NumberFormatException ignored){

        }
        results.addAll(Inventory.lookupPart(query));
        partsTable.setItems(results);
        partSearchAlertCheck(results);
    }

    public void handleProductQuery() {
        String query = productQueryTF.getText();
        ObservableList<Product> results = FXCollections.observableArrayList();
        try {
            Product productToAdd = Inventory.lookupProduct(Integer.parseInt(query));
            if(productToAdd != null) {
                results.add(productToAdd);
            }
        } catch (NumberFormatException ignored){

        }
        results.addAll(Inventory.lookupProduct(query));
        productsTable.setItems(results);
        productSearchAlertCheck(results);
    }

    public void handleDeletePart() {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) { return; }
        if(Inventory.deletePart(selectedPart)) {
            partsTable.getItems().remove(selectedPart);
        }

        handlePartQuery();
    }

    public void handleDeleteProduct() {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) { return; }
        if(Inventory.deleteProduct(selectedProduct)) {
            productsTable.getItems().remove(selectedProduct);
        }

        handleProductQuery();
    }

    public void toAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddPart.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toModifyPart(ActionEvent actionEvent) throws IOException {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null) {
            return;
        }
        if(selectedPart.getClass().getSimpleName().equals("InHouse")){
            ModifyPart.receiveSelectedPart((InHouse) selectedPart);
        } else {
            ModifyPart.receiveSelectedPart((Outsourced) selectedPart);
        }
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyPart.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddProduct.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toModifyProduct(ActionEvent actionEvent) throws IOException {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null) {
            return;
        }
        ModifyProduct.receiveSelectedProduct(selectedProduct);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyProduct.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
