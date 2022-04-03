package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class which specifies logic for Main Form screen.
 * @author Jeramiah Coffey
 */
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

    /**
     * Initializes controller.
     * Sets table values.
     * @param url URL used to resolve paths, null if not known
     * @param resourceBundle Resources used to localize the root object, null if not localized
     */
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

    /**
     * Checks for situations where an empty Part inventory or empty Part query results alert message needs to be displayed.
     * @param queryResults list of results matching the query
     */
    public void partSearchAlertCheck(ObservableList<Part> queryResults) {
        boolean emptyInventory = Inventory.getAllParts().isEmpty();
        boolean emptyQueryResults = queryResults.isEmpty();
        mainPartEmptyAlert.setVisible(emptyInventory);
        mainPartSearchAlert.setVisible(!emptyInventory && emptyQueryResults);
    }

    /**
     * Checks for situations where an empty Product inventory or empty Product query results alert message needs to be displayed.
     * @param queryResults list of results matching the query
     */
    public void productSearchAlertCheck(ObservableList<Product> queryResults) {
        boolean emptyInventory = Inventory.getAllProducts().isEmpty();
        boolean emptyQuery = queryResults.isEmpty();
        mainProductEmptyAlert.setVisible(emptyInventory);
        mainProductSearchAlert.setVisible(!emptyInventory && emptyQuery);
    }

    /**
     * Handles querying of the inventory of Parts.
     * Sets Part table to the query results.
     * Calls to partSearchAlertCheck passing in the query results.
     */
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

    /**
     * Handles querying of the inventory of Products.
     * Sets Product table to the query results.
     * Calls to productSearchAlertCheck passing in the query results.
     */
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

    /**
     * Handles Delete button for Parts pane.
     *
     * LOGICAL ERROR: The Parts table would not update after deletion if there was a query in the search bar.
     * Fixed by adding a call to handlePartQuery at the end of the method.
     */
    public void handleDeletePart() {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid");
            alert.setContentText("No Part selected for deletion.");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setContentText("Are you sure you want to delete " + selectedPart.getName() + "?\nThis action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (Inventory.deletePart(selectedPart)) {
                partsTable.getItems().remove(selectedPart);
            }
        }
        handlePartQuery();
    }

    /**
     * Handles delete button for Products pane.
     *
     * LOGICAL ERROR: The Products table would not update after deletion if there was a query in the search bar.
     * Fixed by adding a call to handleProductQuery at the end of the method.
     */
    public void handleDeleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid");
            alert.setContentText("No Product selected for deletion.");
            alert.showAndWait();
            return;
        }
        if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Deletion");
            alert.setContentText("Cannot delete Product which has Part(s) associated with it.");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setContentText("Are you sure you want to delete " + selectedProduct.getName() + "?\nThis action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (Inventory.deleteProduct(selectedProduct)) {
                productsTable.getItems().remove(selectedProduct);
            }
        }
        handleProductQuery();
    }

    /**
     * Redirects to Add Part screen.
     * @param actionEvent the event which triggered the method
     */
    public void toAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddPart.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Redirects to Modify Part screen. Passes data to Modify Part controller.
     * @param actionEvent the event which triggered the method
     */
    public void toModifyPart(ActionEvent actionEvent) throws IOException {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid");
            alert.setContentText("No Part selected.");
            alert.showAndWait();
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

    /**
     * Redirects to Add Product screen.
     * @param actionEvent the event which triggered the method
     */
    public void toAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddProduct.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Redirects to Modify Product screen. Passes data to Modify Product controller.
     * @param actionEvent the event which triggered the method
     */
    public void toModifyProduct(ActionEvent actionEvent) throws IOException {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid");
            alert.setContentText("No Product selected.");
            alert.showAndWait();
            return;
        }
        ModifyProduct.receiveSelectedProduct(selectedProduct);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyProduct.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles Exit button.
     */
    public void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setContentText("Are you sure you want to close the application?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

}
