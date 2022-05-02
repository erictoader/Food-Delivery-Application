package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.model.BaseProduct;
import com.erictoader.fooddelivery.model.MenuItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminAddController extends ControllerClass {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button btn_add_cancel;

    @FXML
    private Button btn_add_confirm;

    @FXML
    private TextField tf_add_calories;

    @FXML
    private TextField tf_add_fat;

    @FXML
    private TextField tf_add_price;

    @FXML
    private TextField tf_add_protein;

    @FXML
    private TextField tf_add_sodium;

    @FXML
    private TextField tf_add_title;

    @FXML
    void addItem(ActionEvent event) throws IOException {
        MenuItem item = new BaseProduct();
        item.setTitle(tf_add_title.getText());
        item.setRating(0.0);
        item.setCalories(Integer.parseInt(tf_add_calories.getText()));
        item.setProtein(Integer.parseInt(tf_add_protein.getText()));
        item.setFat(Integer.parseInt(tf_add_fat.getText()));
        item.setSodium(Integer.parseInt(tf_add_sodium.getText()));
        item.setPrice(Integer.parseInt(tf_add_price.getText()));

        int currentSize = Constants.ds.getMenu().size();
        Constants.ds.addNewMenuItem(item);
        if(currentSize == Constants.ds.getMenu().size()) {
            showDialog(event, "Operation unsuccessful", "An item with the same name already exists.");
        } else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            Constants.controller.populateTable(Constants.tv);
            ((AdminPanelController)Constants.controller).showConfirmation("Item added successfully!");
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        super.cancel(event);
    }

    private void showDialog(ActionEvent event, String title, String content) {
        Alert.AlertType type = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(((Node) event.getSource()).getScene().getWindow());
        alert.setTitle(title);
        alert.getDialogPane().setHeaderText(content);
        alert.showAndWait();
    }
}

