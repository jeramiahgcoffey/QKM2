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

    private static int autoId = 1000;
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();

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
            currentParts.remove(selectedPart);
        }
        addProdCurrentPartsTable.setItems(currentParts);
    }
//TODO: Implement Data Validation
    public void handleSave(ActionEvent actionEvent) throws IOException {
        String name = addProdNameTF.getText();
        int stock = Integer.parseInt(addProdStockTF.getText());
        double price = Double.parseDouble(addProdPriceTF.getText());
        int min = Integer.parseInt(addProdMinTF.getText());
        int max = Integer.parseInt(addProdMaxTF.getText());
        Inventory.addProduct(new Product(currentParts, autoId, name, price, stock, min, max));
        autoId++;

        toMainForm(actionEvent);
    }

    public void toMainForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
