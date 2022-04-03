package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class which specifies logic for Modify Product screen.
 * @author Jeramiah Coffey
 */
public class ModifyProduct implements Initializable {
    public TableView<Part> modProdAvailablePartsTable;
    public TableColumn<Number, Integer> modProdAvailablePartsId;
    public TableColumn<Character, String> modProdAvailablePartsName;
    public TableColumn<Number, Integer> modProdAvailablePartsStock;
    public TableColumn<Number, Double> modProdAvailablePartsPrice;
    public TableView<Part> modProdCurrentPartsTable;
    public TableColumn<Number, Integer> modProdCurrentPartsId;
    public TableColumn<Character, String> modProdCurrentPartsName;
    public TableColumn<Number, Integer> modProdCurrentPartsStock;
    public TableColumn<Number, Double> modProdCurrentPartsPrice;
    public TextField modProdNameTF;
    public TextField modProdStockTF;
    public TextField modProdPriceTF;
    public TextField modProdMaxTF;
    public TextField modProdMinTF;
    public Label modProdSearchAlert;
    public Label modProdEmptyAlert;
    public TextField modProdAPQueryTF;
    public TextField modProdIdTF;
    public Label modProdNumericalAlert;
    public Label modProdMinMaxAlert;
    public Label modProdInvAlert;
    public Label modProdPriceAlert;

    /**
     * Static variable for storing the Product data received from the Main Form.
     */
    private static Product selectedProduct = null;

    /**
     * Static variable for storing the Associated Parts data for the Product received from the Main Form.
     */
    private static ObservableList<Part> currentParts = null;

    /**
     * Static variable for storing the index of the Product received from the Main Form.
     */
    private static int index;

    /**
     * Method for receiving the Product data from the Main Form.
     * @param product the Product data
     */
    public static void receiveSelectedProduct(Product product){
        selectedProduct = product;
        currentParts = product.getAllAssociatedParts();
        index = Inventory.getAllProducts().indexOf(product);
    }

    /**
     * Initializes controller.
     * Sets table values.
     * @param url URL used to resolve paths, null if not known
     * @param resourceBundle Resources used to localize the root object, null if not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        searchAlertCheck(Inventory.getAllParts());
        modProdAvailablePartsTable.setItems(Inventory.getAllParts());
        modProdAvailablePartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdAvailablePartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdAvailablePartsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdAvailablePartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        modProdCurrentPartsTable.setItems(currentParts);
        modProdCurrentPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdCurrentPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdCurrentPartsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdCurrentPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        modProdIdTF.setText(String.valueOf(selectedProduct.getId()));
        modProdNameTF.setText(selectedProduct.getName());
        modProdStockTF.setText(String.valueOf(selectedProduct.getStock()));
        modProdPriceTF.setText(String.valueOf(selectedProduct.getPrice()));
        modProdMinTF.setText(String.valueOf(selectedProduct.getMin()));
        modProdMaxTF.setText(String.valueOf(selectedProduct.getMax()));
    }

    /**
     * Checks for situations where an empty inventory or empty query results alert message needs to be displayed.
     * @param queryResults list of results matching the query
     */
    public void searchAlertCheck(ObservableList<Part> queryResults) {
        boolean emptyInventory = Inventory.getAllParts().isEmpty();
        boolean emptyQueryResults = queryResults.isEmpty();
        modProdEmptyAlert.setVisible(emptyInventory);
        modProdSearchAlert.setVisible(!emptyInventory && emptyQueryResults);
    }

    /**
     * Handles querying of the inventory of Parts.
     * Sets table to the query results.
     * Calls to searchAlertCheck passing in the query results.
     */
    public void handlePartQuery() {
        String query = modProdAPQueryTF.getText();
        ObservableList<Part> results = FXCollections.observableArrayList();
        try {
            Part partToAdd = Inventory.lookupPart(Integer.parseInt(query));
            if(partToAdd != null) {
                results.add(partToAdd);
            }
        } catch (NumberFormatException ignored){
        }
        results.addAll(Inventory.lookupPart(query));
        modProdAvailablePartsTable.setItems(results);
        searchAlertCheck(results);
    }

    /**
     * Handles the Add Associated Part button.
     */
    public void handleAddAP() {
        Part selectedPart = modProdAvailablePartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid");
            alert.setContentText("No Part selected.");
            alert.showAndWait();
            return;
        } else {
            currentParts.add(selectedPart);
        }
        modProdCurrentPartsTable.setItems(currentParts);
    }

    /**
     * Handles the Remove Associated Part button.
     */
    public void handleRemAP() {
        Part selectedPart = modProdCurrentPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure?");
            alert.setContentText("Are you sure you want to remove this associated part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                currentParts.remove(selectedPart);
            }
        }
        modProdCurrentPartsTable.setItems(currentParts);
    }

    /**
     * Validates a string input as a positive integer.
     * @param str the string to validate
     * @return boolean representing the result of validation
     */
    public static boolean isInteger(String str){
        if (str == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(str);
            if (i < 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Validates a string input as a positive double/number.
     * @param str the string to validate
     * @return boolean representing the result of validation
     */
    public static boolean isNumeric(String str){
        if (str == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
            if (d < 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Handler for Modify Product Save button. Validates input data from text fields, and sets visible any alerts needed depending on validation results.
     * Upon validation, creates updates Product object in the inventory.
     * Redirects to Main Form.
     * @param actionEvent the event which triggered the method
     */
    public void handleSave(ActionEvent actionEvent) throws IOException {
        boolean validData = true;
        modProdNumericalAlert.setVisible(false);
        modProdInvAlert.setVisible(false);
        modProdMinMaxAlert.setVisible(false);
        modProdPriceAlert.setVisible(false);

        int id = selectedProduct.getId();
        String name = modProdNameTF.getText();
        int stock = 0;
        if (isInteger(modProdStockTF.getText())) {
            stock = Integer.parseInt(modProdStockTF.getText());
        } else {
            validData = false;
            modProdNumericalAlert.setVisible(true);
        }

        double price = 0;
        if (isNumeric(modProdPriceTF.getText())) {
            price = Double.parseDouble(modProdPriceTF.getText());
        } else {
            validData = false;
            modProdPriceAlert.setVisible(true);
        }

        int min = 0;
        if (isInteger(modProdMinTF.getText())) {
            min = Integer.parseInt(modProdMinTF.getText());
        } else {
            validData = false;
            modProdNumericalAlert.setVisible(true);
        }

        int max = 0;
        if (isInteger(modProdMaxTF.getText())) {
            max = Integer.parseInt(modProdMaxTF.getText());
        } else {
            validData = false;
            modProdNumericalAlert.setVisible(true);
        }

        if (max <= min) {
            validData = false;
            modProdMinMaxAlert.setVisible(true);
        }

        if (stock < min || stock > max) {
            validData = false;
            modProdInvAlert.setVisible(true);
        }

        if (validData) {
            Inventory.updateProduct(index, new Product(currentParts, id, name, price, stock, min, max));
            toMainForm(actionEvent);
        }
    }

    /**
     * Handler for Add Product Cancel button. Redirects to Main Form.
     * @param actionEvent the event which triggered the method
     */
    public void toMainForm(javafx.event.ActionEvent actionEvent) throws IOException {
        selectedProduct = null;
        currentParts = null;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
