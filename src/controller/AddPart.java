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
import java.util.Objects;
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
    public Label addPartNumericalAlert;
    public Label addPartMinMaxAlert;
    public Label addPartInvAlert;
    public Label addPartPriceAlert;

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
        addPartNumericalAlert.setVisible(false);
        addPartInvAlert.setVisible(false);
        addPartMinMaxAlert.setVisible(false);
        addPartPriceAlert.setVisible(false);

        String name = addPartNameTF.getText();
        int stock = 0;
        if (isInteger(addPartStockTF.getText())) {
            stock = Integer.parseInt(addPartStockTF.getText());
        } else {
            validData = false;
            addPartNumericalAlert.setVisible(true);
        }

        double price = 0;
        if (isNumeric(addPartPriceTF.getText())) {
            price = Double.parseDouble(addPartPriceTF.getText());
        } else {
            validData = false;
            addPartPriceAlert.setVisible(true);
        }

        int min = 0;
        if (isInteger(addPartMinTF.getText())) {
            min = Integer.parseInt(addPartMinTF.getText());
        } else {
            validData = false;
            addPartNumericalAlert.setVisible(true);
        }

        int max = 0;
        if (isInteger(addPartMaxTF.getText())) {
            max = Integer.parseInt(addPartMaxTF.getText());
        } else {
            validData = false;
            addPartNumericalAlert.setVisible(true);
        }

        if (max <= min) {
            validData = false;
            addPartMinMaxAlert.setVisible(true);
        }

        if (stock < min || stock > max) {
            validData = false;
            addPartInvAlert.setVisible(true);
        }

        if (validData) {
            if (addPartInHouse.isSelected()) {
                if (isInteger(addPartMIDCNTF.getText())) {
                    int mid = Integer.parseInt(addPartMIDCNTF.getText());
                    Inventory.addPart(new InHouse(autoId, name, price, stock, min, max, mid));
                } else {
                    addPartNumericalAlert.setVisible(true);
                    return;
                }
            } else {
                String compName = addPartMIDCNTF.getText();
                Inventory.addPart(new Outsourced(autoId, name, price, stock, min, max, compName));
            }
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
