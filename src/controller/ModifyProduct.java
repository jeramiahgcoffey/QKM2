package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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

    private static Product selectedProduct = null;
    private static ObservableList<Part> currentParts = null;
    private static int index;

    public static void receiveSelectedProduct(Product product){
        selectedProduct = product;
        currentParts = product.getAllAssociatedParts();
        index = Inventory.getAllProducts().indexOf(product);
    }

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

    public void searchAlertCheck(ObservableList<Part> queryResults) {
        boolean emptyInventory = Inventory.getAllParts().isEmpty();
        boolean emptyQueryResults = queryResults.isEmpty();
        modProdEmptyAlert.setVisible(emptyInventory);
        modProdSearchAlert.setVisible(!emptyInventory && emptyQueryResults);
    }

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

    public void handleAddAP() {
        Part selectedPart = modProdAvailablePartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            currentParts.add(selectedPart);
        }
        modProdCurrentPartsTable.setItems(currentParts);
    }

    public void handleRemAP() {
        Part selectedPart = modProdCurrentPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            currentParts.remove(selectedPart);
        }
        modProdCurrentPartsTable.setItems(currentParts);
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
