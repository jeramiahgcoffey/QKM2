package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPart implements Initializable {
    public ToggleGroup addPart;
    public TextField addPartNameTF;
    public TextField addPartMinTF;
    public TextField addPartStockTF;
    public TextField addPartPriceTF;
    public TextField addPartMaxTF;
    public TextField addPartMIDCNTF;
    public RadioButton addPartInHouse;
    public RadioButton addPartOutsourced;
    public Label addPartMIDCNLabel;

    private static int autoId = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
    }

    public void handleRadioChange() {
        if(addPartInHouse.isSelected()){
            addPartMIDCNLabel.setText("Machine ID");
        } else {
            addPartMIDCNLabel.setText("Company Name");
        }
    }


//TODO: Implement validation of data


    public void handleSaveAddPart(ActionEvent actionEvent) throws IOException {
        String name = addPartNameTF.getText();
        int stock = Integer.parseInt(addPartStockTF.getText());
        double price = Double.parseDouble(addPartPriceTF.getText());
        int min = Integer.parseInt(addPartMinTF.getText());
        int max = Integer.parseInt(addPartMaxTF.getText());
        if(addPartInHouse.isSelected()) {
            int mid = Integer.parseInt(addPartMIDCNTF.getText());
            Inventory.addPart(new InHouse(autoId, name, price, stock, min, max, mid));
        } else {
            String compName = addPartMIDCNTF.getText();
            Inventory.addPart(new Outsourced(autoId, name, price, stock, min, max, compName));
        }

        autoId++;

        toMainForm(actionEvent);
    }

    public void toMainForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
