package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.model.MenuItem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AdminPanelController extends ControllerClass implements Initializable {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button btn_admin_logout;

    @FXML
    private Button btn_populate_with_file;

    @FXML
    private Label label_currentsession;

    @FXML
    private Label label_status;

    @FXML
    private TableColumn<MenuItem, Integer> tc_calories;

    @FXML
    private TableColumn<MenuItem, Integer> tc_fat;

    @FXML
    private TableColumn<MenuItem, Integer> tc_price;

    @FXML
    private TableColumn<MenuItem, Integer> tc_protein;

    @FXML
    private TableColumn<MenuItem, Double> tc_rating;

    @FXML
    private TableColumn<MenuItem, Integer> tc_sodium;

    @FXML
    private TableColumn<MenuItem, String> tc_title;

    @FXML
    private TableView<MenuItem> tv_menutable;

    @FXML
    private Button btn_add_menu_item;

    @FXML
    private Button btn_delete_menu_item;

    @FXML
    private Button btn_update_menu_item;

    @FXML
    void logout(ActionEvent event) throws IOException {
        super.logout(event);
    }

    @FXML
    void populateMenuFromFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if(file != null) {
            try {
                Constants.ds.inputMenuItemsFromFile(file.getAbsolutePath());
                populateTable(tv_menutable);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constants.controller = this;
        Constants.tv = tv_menutable;
        label_status.setVisible(false);
        label_currentsession.setText("Current session : " + Constants.USER_FULLNAME);
        tc_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tc_rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        tc_calories.setCellValueFactory(new PropertyValueFactory<>("calories"));
        tc_protein.setCellValueFactory(new PropertyValueFactory<>("protein"));
        tc_fat.setCellValueFactory(new PropertyValueFactory<>("fat"));
        tc_sodium.setCellValueFactory(new PropertyValueFactory<>("sodium"));
        tc_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        populateTable(tv_menutable);
    }

    @FXML
    void addMenuItem(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("insert-menu-item.fxml")));
        Stage stage1 = new Stage();
        stage1.setTitle("Input item details");
        Scene scene1 = new Scene(root1);
        stage1.setScene(scene1);
        stage1.setResizable(false);
        stage1.show();
    }

    @FXML
    void deleteMenuItem(ActionEvent event) {
        MenuItem toDelete = tv_menutable.getSelectionModel().getSelectedItem();
        Constants.ds.removeMenuItem(toDelete);
        populateTable(tv_menutable);
        showConfirmation("Item  removed successfully!");
    }

    @FXML
    void updateMenuItem(ActionEvent event) throws IOException {
        MenuItem toUpdate = tv_menutable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-menu-item.fxml"));
        Parent root1 = loader.load();
        Stage stage1 = new Stage();
        stage1.setTitle("Update item details");
        Scene scene1 = new Scene(root1);
        stage1.setScene(scene1);
        stage1.setResizable(false);
        AdminUpdateController adminUpdateController = loader.getController();
        adminUpdateController.completeFields(toUpdate);
        stage1.show();
    }

    public void showConfirmation(String message) {
        label_status.setText(message);
        label_status.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            private int i = 0;
            @Override
            public void handle(ActionEvent event) {
                if(i == 1) {
                    label_status.setVisible(false);
                }
                i++;
            }
        }));
        timeline.setCycleCount(2);
        timeline.play();
    }
}

