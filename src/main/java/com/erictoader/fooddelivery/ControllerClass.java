package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.dao.Deserializator;
import com.erictoader.fooddelivery.dao.Serializator;
import com.erictoader.fooddelivery.model.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class ControllerClass {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    void logout(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Food Delivery Application");
        stage.setResizable(false);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.5;
        stage.setX(x);
        stage.setY(y);

        stage.show();
    }

    void populateTable(TableView<MenuItem> tableView) {
        if(Constants.ds == null) {
            Deserializator.deserializeData();
        }

        Constants.dailyMenuIndex = 1;
        for(MenuItem mi : Constants.ds.getMenu()) {
            if(mi.getTitle().contains(Constants.DAILY_MENU)) {
                Constants.dailyMenuIndex++;
            }
        }

        Set<MenuItem> itemList = Constants.ds.getMenu();

        ObservableList<MenuItem> list = FXCollections.observableArrayList(itemList);
        tableView.setItems(list);

        Serializator.serializeData(Constants.ds);
    }

    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    void showDialog(ActionEvent event, String title, String content) {
        Alert.AlertType type = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(((Node) event.getSource()).getScene().getWindow());
        alert.setTitle(title);
        alert.getDialogPane().setHeaderText(content);
        alert.showAndWait();
    }
}
