package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProduct implements Initializable {
    public TableView addProdAvailablePartsTable;
    public TableColumn addProdAvailablePartsId;
    public TableColumn addProdAvailablePartsName;
    public TableColumn addProdAvailablePartsStock;
    public TableColumn addProdAvailablePartsPrice;
    public TableView addProdCurrentPartsTable;
    public TableColumn addProdCurrentPartsId;
    public TableColumn addProdCurrentPartsName;
    public TableColumn addProdCurrentPartsStock;
    public TableColumn addProdCurrentPartsPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        addProdAvailablePartsTable.setItems(Inventory.getAllParts());

        addProdAvailablePartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProdAvailablePartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProdAvailablePartsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProdAvailablePartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    public void toMainForm(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
