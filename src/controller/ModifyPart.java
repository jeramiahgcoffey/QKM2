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
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class which specifies logic for Modify Part screen.
 * @author Jeramiah Coffey
 */
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
    public Label modPartNumericalAlert;
    public Label modPartMinMaxAlert;
    public Label modPartInvAlert;
    public Label modPartPriceAlert;

    /**
     * Static variable for storing an in-house Part object received from the Main Form.
     */
    private static InHouse selectedInHousePart = null;

    /**
     * Static variable for storing an outsourced Part object received from the Main Form.
     */
    private static Outsourced selectedOutsourcedPart = null;

    /**
     * Static variable for storing the index of the Part object received from the Main Form.
     */
    private static int index;

    /**
     * Overloaded method for receiving the Part data from the Main Form.
     * @param part the InHouse Part data
     */
    public static void receiveSelectedPart(InHouse part){
        selectedInHousePart = part;
        selectedOutsourcedPart = null;
        index = Inventory.getAllParts().indexOf(part);
    }

    /**
     * Overloaded method for receiving the Part data from the Main Form.
     * @param part the Outsourced part data
     */
    public static void receiveSelectedPart(Outsourced part){
        selectedOutsourcedPart = part;
        selectedInHousePart = null;
        index = Inventory.getAllParts().indexOf(part);
    }

    /**
     * Initializes controller.
     * Sets table values.
     * @param url URL used to resolve paths, null if not known
     * @param resourceBundle Resources used to localize the root object, null if not localized
     */
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
            modPartMIDCNLabel.setText("Company Name");
            modPartIdTF.setText(String.valueOf(selectedOutsourcedPart.getId()));
            modPartNameTF.setText(selectedOutsourcedPart.getName());
            modPartStockTF.setText(String.valueOf(selectedOutsourcedPart.getStock()));
            modPartPriceTF.setText(String.valueOf(selectedOutsourcedPart.getPrice()));
            modPartMaxTF.setText(String.valueOf(selectedOutsourcedPart.getMax()));
            modPartMinTF.setText(String.valueOf(selectedOutsourcedPart.getMin()));
            modPartMIDCNTF.setText(String.valueOf(selectedOutsourcedPart.getCompanyName()));
        }
    }

    /**
     * Handler for radio button change. Changes the text field label for Machine ID/Company Name.
     */
    public void handleRadioChange() {
        if(modPartInHouse.isSelected()){
            modPartMIDCNLabel.setText("Machine ID");
        } else {
            modPartMIDCNLabel.setText("Company Name");
        }
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
     * Handler for Modify Part Save button. Validates input data from text fields, and sets visible any alerts needed depending on validation results.
     * Upon validation, creates updates Part object (InHouse/Outsourced) in the Inventory.
     * Redirects to Main Form.
     * @param actionEvent the event which triggered the method
     */
    public void handleSave(ActionEvent actionEvent) throws IOException {
        boolean validData = true;
        modPartNumericalAlert.setVisible(false);
        modPartInvAlert.setVisible(false);
        modPartMinMaxAlert.setVisible(false);
        modPartPriceAlert.setVisible(false);

        int id = Integer.parseInt(modPartIdTF.getText());
        String name = modPartNameTF.getText();

        int stock = 0;
        if (isInteger(modPartStockTF.getText())) {
            stock = Integer.parseInt(modPartStockTF.getText());
        } else {
            validData = false;
            modPartNumericalAlert.setVisible(true);
        }

        double price = 0;
        if (isNumeric(modPartPriceTF.getText())) {
            price = Double.parseDouble(modPartPriceTF.getText());
        } else {
            validData = false;
            modPartPriceAlert.setVisible(true);
        }

        int min = 0;
        if (isInteger(modPartMinTF.getText())) {
            min = Integer.parseInt(modPartMinTF.getText());
        } else {
            validData = false;
            modPartNumericalAlert.setVisible(true);
        }

        int max = 0;
        if (isInteger(modPartMaxTF.getText())) {
            max = Integer.parseInt(modPartMaxTF.getText());
        } else {
            validData = false;
            modPartNumericalAlert.setVisible(true);
        }

        if (max <= min) {
            validData = false;
            modPartMinMaxAlert.setVisible(true);
        }

        if (stock < min || stock > max) {
            validData = false;
            modPartInvAlert.setVisible(true);
        }

        if (validData) {
            if (modPartInHouse.isSelected()) {
                if (isInteger(modPartMIDCNTF.getText())) {
                    int mid = Integer.parseInt(modPartMIDCNTF.getText());
                    Inventory.updatePart(index, new InHouse(id, name, price, stock, min, max, mid));
                } else {
                    modPartNumericalAlert.setVisible(true);
                    return;
                }
            } else {
                String compName = modPartMIDCNTF.getText();
                Inventory.updatePart(index, new Outsourced(id, name, price, stock, min, max, compName));
            }

            toMainForm(actionEvent);
        }
    }

    /**
     * Handler for Modify Part Cancel button. Redirects to Main Form.
     * @param actionEvent the event which triggered the method
     */
    public void toMainForm(ActionEvent actionEvent) throws IOException {
        selectedInHousePart = null;
        selectedOutsourcedPart = null;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
