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

public class ModifyProduct implements Initializable {
    public TableView modProdAvailablePartsTable;
    public TableColumn modProdAvailablePartsId;
    public TableColumn modProdAvailablePartsName;
    public TableColumn modProdAvailablePartsStock;
    public TableColumn modProdAvailablePartsPrice;
    public TableView modProdCurrentPartsTable;
    public TableColumn modProdCurrentPartsId;
    public TableColumn modProdCurrentPartsName;
    public TableColumn modProdCurrentPartsStock;
    public TableColumn modProdCurrentPartsPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        modProdAvailablePartsTable.setItems(Inventory.getAllParts());

        modProdAvailablePartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdAvailablePartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdAvailablePartsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdAvailablePartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    public void toMainForm(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
