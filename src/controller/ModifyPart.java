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
import model.Inventory;
import model.Outsourced;
import model.InHouse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPart implements Initializable {
    public RadioButton modPartInHouse;
    public RadioButton modPartOutsourced;
    public TextField modPartNameTF;
    public TextField modPartStockTF;
    public TextField modPartPriceTF;
    public TextField modPartMaxTF;
    public TextField modPartMIDCNTF;
    public TextField modPartMinTF;
    public ToggleGroup modPart;
    public Label modPartMIDCNLabel;
    public TextField modPartIdTF;

    private static InHouse selectedInHousePart = null;
    private static Outsourced selectedOutsourcedPart = null;
    private static int index;

    public static void receiveSelectedPart(InHouse part){
        selectedInHousePart = part;
        selectedOutsourcedPart = null;
        index = Inventory.getAllParts().indexOf(part);
    }

    public static void receiveSelectedPart(Outsourced part){
        selectedOutsourcedPart = part;
        selectedInHousePart = null;
        index = Inventory.getAllParts().indexOf(part);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        if(selectedInHousePart != null) {
            modPartIdTF.setText(String.valueOf(selectedInHousePart.getId()));
            modPartNameTF.setText(selectedInHousePart.getName());
            modPartStockTF.setText(String.valueOf(selectedInHousePart.getStock()));
            modPartPriceTF.setText(String.valueOf(selectedInHousePart.getPrice()));
            modPartMaxTF.setText(String.valueOf(selectedInHousePart.getMax()));
            modPartMinTF.setText(String.valueOf(selectedInHousePart.getMin()));
            modPartMIDCNTF.setText(String.valueOf(selectedInHousePart.getMachineId()));
        } else {
            modPartOutsourced.setSelected(true);
            modPartIdTF.setText(String.valueOf(selectedOutsourcedPart.getId()));
            modPartNameTF.setText(selectedOutsourcedPart.getName());
            modPartStockTF.setText(String.valueOf(selectedOutsourcedPart.getStock()));
            modPartPriceTF.setText(String.valueOf(selectedOutsourcedPart.getPrice()));
            modPartMaxTF.setText(String.valueOf(selectedOutsourcedPart.getMax()));
            modPartMinTF.setText(String.valueOf(selectedOutsourcedPart.getMin()));
            modPartMIDCNTF.setText(String.valueOf(selectedOutsourcedPart.getCompanyName()));
        }
    }

    public void handleRadioChange() {
        if(modPartInHouse.isSelected()){
            modPartMIDCNLabel.setText("Machine ID");
        } else {
            modPartMIDCNLabel.setText("Company Name");
        }
    }

    //TODO: Implement validation of data
    public void handleSaveModPart(ActionEvent actionEvent) throws IOException {
        int id = Integer.parseInt(modPartIdTF.getText());
        String name = modPartNameTF.getText();
        int stock = Integer.parseInt(modPartStockTF.getText());
        double price = Double.parseDouble(modPartPriceTF.getText());
        int min = Integer.parseInt(modPartMinTF.getText());
        int max = Integer.parseInt(modPartMaxTF.getText());
        if(modPartInHouse.isSelected()) {
            int mid = Integer.parseInt(modPartMIDCNTF.getText());
            Inventory.updatePart(index, new InHouse(id, name, price, stock, min, max, mid));
        } else {
            String compName = modPartMIDCNTF.getText();
            Inventory.updatePart(index, new Outsourced(id, name, price, stock, min, max, compName));
        }

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
