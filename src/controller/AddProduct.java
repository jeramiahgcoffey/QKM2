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


    private static int autoId = 1000;
    private final ObservableList<Part> currentParts = FXCollections.observableArrayList();

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

    public void searchAlertCheck(ObservableList<Part> queryResults) {
        boolean emptyInventory = Inventory.getAllParts().isEmpty();
        boolean emptyQueryResults = queryResults.isEmpty();
        addProdEmptyAlert.setVisible(emptyInventory);
        addProdSearchAlert.setVisible(!emptyInventory && emptyQueryResults);
    }

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

    public void handleAddAP() {
        Part selectedPart = addProdAvailablePartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            currentParts.add(selectedPart);
        }
        addProdCurrentPartsTable.setItems(currentParts);
    }

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

    public void toMainForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
