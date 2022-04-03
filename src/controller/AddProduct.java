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
 * Controller class which specifies logic for Add Product screen.
 * @author Jeramiah Coffey
 */
public class AddProduct implements Initializable {
    public TableView<Part> addProdAvailablePartsTable;
    public TableColumn<Number, Integer> addProdAvailablePartsId;
    public TableColumn<Character, String> addProdAvailablePartsName;
    public TableColumn<Number, Integer> addProdAvailablePartsStock;
    public TableColumn<Number, Double> addProdAvailablePartsPrice;
    public TableView<Part> addProdCurrentPartsTable;
    public TableColumn<Number, Integer> addProdCurrentPartsId;
    public TableColumn<Character, String> addProdCurrentPartsName;
    public TableColumn<Number, Integer> addProdCurrentPartsStock;
    public TableColumn<Number, Double> addProdCurrentPartsPrice;
    public TextField addProdAPQueryTF;
    public TextField addProdNameTF;
    public TextField addProdStockTF;
    public TextField addProdPriceTF;
    public TextField addProdMaxTF;
    public TextField addProdMinTF;
    public Label addProdSearchAlert;
    public Label addProdEmptyAlert;
    public Label addProdNumericalAlert;
    public Label addProdMinMaxAlert;
    public Label addProdInvAlert;
    public Label addProdPriceAlert;

    /**
     * Static variable for generating unique Product IDs.
     */
    private static int autoId = 1000;

    /**
     * List of current associated parts for a Product, initially empty upon instantiation.
     */
    private final ObservableList<Part> currentParts = FXCollections.observableArrayList();

    /**
     * Initializes controller.
     * @param url URL used to resolve paths, null if not known
     * @param resourceBundle Resources used to localize the root object, null if not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        searchAlertCheck(Inventory.getAllParts());
        addProdAvailablePartsTable.setItems(Inventory.getAllParts());
        addProdAvailablePartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProdAvailablePartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProdAvailablePartsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProdAvailablePartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProdCurrentPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProdCurrentPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProdCurrentPartsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProdCurrentPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Checks for situations where an empty inventory or empty query results alert message needs to be displayed.
     * @param queryResults list of results matching the query
     */
    public void searchAlertCheck(ObservableList<Part> queryResults) {
        boolean emptyInventory = Inventory.getAllParts().isEmpty();
        boolean emptyQueryResults = queryResults.isEmpty();
        addProdEmptyAlert.setVisible(emptyInventory);
        addProdSearchAlert.setVisible(!emptyInventory && emptyQueryResults);
    }

    /**
     * Handles querying of the inventory of Parts.
     * Sets table to the query results.
     * Calls to searchAlertCheck passing in the query results.
     */
    public void handlePartQuery() {
        String query = addProdAPQueryTF.getText();
        ObservableList<Part> results = FXCollections.observableArrayList();
        try {
            Part partToAdd = Inventory.lookupPart(Integer.parseInt(query));
            if(partToAdd != null) {
                results.add(partToAdd);
            }
        } catch (NumberFormatException ignored){

        }
        results.addAll(Inventory.lookupPart(query));
        addProdAvailablePartsTable.setItems(results);
        searchAlertCheck(results);
    }

    /**
     * Handles the Add Associated Part button.
     */
    public void handleAddAP() {
        Part selectedPart = addProdAvailablePartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid");
            alert.setContentText("No Part selected.");
            alert.showAndWait();
            return;
        } else {
            currentParts.add(selectedPart);
        }
        addProdCurrentPartsTable.setItems(currentParts);
    }

    /**
     * Handles the Remove Associated Part button.
     */
    public void handleRemAP() {
        Part selectedPart = addProdCurrentPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure?");
            alert.setContentText("Are you sure you want to remove this associated part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                currentParts.remove(selectedPart);
            }
        }
        addProdCurrentPartsTable.setItems(currentParts);
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
     * Handler for the Add Product Save button. Validates input data from text fields, and sets visible any alerts needed depending on validation results.
     * Upon validation, creates new Product object and adds the object to the Inventory.
     * Increments autoID.
     * Redirects to main form.
     * @param actionEvent the event which triggered the method
     */
    public void handleSave(ActionEvent actionEvent) throws IOException {
        boolean validData = true;
        addProdNumericalAlert.setVisible(false);
        addProdInvAlert.setVisible(false);
        addProdMinMaxAlert.setVisible(false);
        addProdPriceAlert.setVisible(false);

        String name = addProdNameTF.getText();
        int stock = 0;
        if (isInteger(addProdStockTF.getText())) {
            stock = Integer.parseInt(addProdStockTF.getText());
        } else {
            validData = false;
            addProdNumericalAlert.setVisible(true);
        }

        double price = 0;
        if (isNumeric(addProdPriceTF.getText())) {
            price = Double.parseDouble(addProdPriceTF.getText());
        } else {
            validData = false;
            addProdPriceAlert.setVisible(true);
        }

        int min = 0;
        if (isInteger(addProdMinTF.getText())) {
            min = Integer.parseInt(addProdMinTF.getText());
        } else {
            validData = false;
            addProdNumericalAlert.setVisible(true);
        }

        int max = 0;
        if (isInteger(addProdMaxTF.getText())) {
            max = Integer.parseInt(addProdMaxTF.getText());
        } else {
            validData = false;
            addProdNumericalAlert.setVisible(true);
        }

        if (max <= min) {
            validData = false;
            addProdMinMaxAlert.setVisible(true);
        }

        if (stock < min || stock > max) {
            validData = false;
            addProdInvAlert.setVisible(true);
        }

        if (validData) {
            Inventory.addProduct(new Product(currentParts, autoId, name, price, stock, min, max));
            autoId++;
            toMainForm(actionEvent);
        }
    }

    /**
     * Handler for Add Product Cancel button. Redirects to Main Form.
     * @param actionEvent the event which triggered the method
     */
    public void toMainForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
