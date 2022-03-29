package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene(root, 1000, 400));
        stage.setResizable(false);
        stage.show();
    }

    public static void addTestData() {
        InHouse testPart = new InHouse(1, "InHouse Part 1", 2.5, 3, 1, 5, 1000);
        Inventory.addPart(testPart);
        InHouse testPart2 = new InHouse(2, "InHouse Part 2", 50.5, 2, 1, 5, 1001);
        Inventory.addPart(testPart2);
        Outsourced testPart3 = new Outsourced(3, "Outsourced Part 1", 20, 5, 1, 10, "Fake Company");
        Inventory.addPart(testPart3);

        ObservableList<Part> testProductParts = FXCollections.observableArrayList();
        testProductParts.add(testPart);
        Product testProduct = new Product(testProductParts, 5, "Test Product", 50.5, 3, 1, 5);
        Inventory.addProduct(testProduct);

    }

    public static void main(String[] args){
        addTestData();

        launch(args);
    }
}
