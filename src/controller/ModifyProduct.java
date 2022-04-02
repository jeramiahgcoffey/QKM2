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

    //TODO: Implement validation of data
    public void handleSave(ActionEvent actionEvent) throws IOException {
        int id = selectedProduct.getId();
        String name = modProdNameTF.getText();
        int stock = Integer.parseInt(modProdStockTF.getText());
        double price = Double.parseDouble(modProdPriceTF.getText());
        int min = Integer.parseInt(modProdMinTF.getText());
        int max = Integer.parseInt(modProdMaxTF.getText());
        Inventory.updateProduct(index, new Product(currentParts, id, name, price, stock, min, max));
        toMainForm(actionEvent);
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
