package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.model.CompositeProduct;
import com.erictoader.fooddelivery.model.MenuItem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TableColumn<MenuItem, String> tc_contains;

    @FXML
    private TableView<MenuItem> tv_menutable;

    @FXML
    private Button btn_add_menu_item;

    @FXML
    private Button btn_delete_menu_item;

    @FXML
    private Button btn_update_menu_item;

    @FXML
    private TableView<MenuItem> tv_compBuilder;

    @FXML
    private TableColumn<MenuItem, String> tc_compBuilder_title;

    @FXML
    private Label label_compName;

    @FXML
    private Label label_compPrice;

    @FXML
    private TextField tf_compName;

    @FXML
    private TextField tf_compPrice;

    @FXML
    private Button btn_add_comp_product;

    @FXML
    private TextField tf_search_by_title;

    private ArrayList<MenuItem> compBuilder;

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
        compBuilder = new ArrayList<>();
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
        tc_contains.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getContains()));
        populateTable(tv_menutable);

        tc_compBuilder_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        refreshCompBuilder();
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
        searchByTitle();
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

    public void reports(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("generate-reports.fxml")));
        Stage stage1 = new Stage();
        stage1.setTitle("Generate reports");
        Scene scene1 = new Scene(root1);
        stage1.setScene(scene1);
        stage1.setResizable(false);
        stage1.show();
    }

    public void addToCompBuilder(ActionEvent event) {
        MenuItem toAdd = tv_menutable.getSelectionModel().getSelectedItem();
        if(toAdd != null) {
            if(!compBuilder.contains(toAdd)) {
                compBuilder.add(toAdd);
                refreshCompBuilder();
            } else super.showDialog(event, "Operation denied", "Item already exists in the builder");
        } else super.showDialog(event, "Operation denied", "Select an item before attempting to add it to the builder");
    }

    private void refreshCompBuilder() {
        ObservableList<MenuItem> list = FXCollections.observableArrayList(compBuilder);
        tv_compBuilder.setItems(list);
        if(compBuilder.isEmpty()) {
            tv_compBuilder.setVisible(false);
            tc_compBuilder_title.setVisible(false);
            label_compName.setVisible(false);
            label_compPrice.setVisible(false);
            tf_compName.setVisible(false);
            tf_compPrice.setVisible(false);
            btn_add_comp_product.setVisible(false);
        } else {
            tv_compBuilder.setVisible(true);
            tc_compBuilder_title.setVisible(true);
            label_compName.setVisible(true);
            label_compPrice.setVisible(true);
            tf_compName.setVisible(true);
            tf_compPrice.setVisible(true);
            btn_add_comp_product.setVisible(true);
            int total = 0;
            for(MenuItem m : compBuilder) {
                total += m.computePrice();
            }
            tf_compPrice.setText(total + "");
            tf_compName.setText(Constants.DAILY_MENU + " " +  Constants.dailyMenuIndex);
        }
    }

    public void addCompProduct(ActionEvent event) {
        if(!compBuilder.isEmpty()) {
            if(!Objects.equals(tf_compPrice.getText(), "") && !Objects.equals(tf_compName.getText(), "")) {
                if(compBuilder.size() > 1) {
                    MenuItem compProduct = new CompositeProduct();
                    ((CompositeProduct) compProduct).setProducts(compBuilder);
                    compProduct.setTitle(tf_compName.getText());
                    compProduct.setPrice(Integer.parseInt(tf_compPrice.getText()));
                    compProduct.setRating(0.0);

                    int currentSize = Constants.ds.getMenu().size();
                    Constants.ds.addNewMenuItem(compProduct);
                    if(currentSize == Constants.ds.getMenu().size()) {
                        super.showDialog(event, "Operation unsuccessful", "An item with the same name already exists.");
                    } else {
                        super.populateTable(tv_menutable);
                        compBuilder = new ArrayList<>();
                        refreshCompBuilder();
                        showConfirmation("Composite item added successfully!");
                    }
                } else super.showDialog(event, "Cannot add item", "A composite item must have more than one menu item");
            } else super.showDialog(event, "Cannot add item", "Fill both product name and price in order to add the composite product");
        } else super.showDialog(event, "Operation unavailable", "Composite builder is empty!");
    }

    public void searchByTitle() {
        if(!Objects.equals(tf_search_by_title.getText(), "")) {
            List<MenuItem> currentSelection = Constants.ds.getMenu().stream().toList();
            List<MenuItem> filteredByName = new ArrayList<>();
            for(MenuItem item : currentSelection) {
                if(item.getTitle().toLowerCase().contains(tf_search_by_title.getText().toLowerCase())) {
                    filteredByName.add(item);
                }
            }
            ObservableList<MenuItem> observableList = FXCollections.observableArrayList(filteredByName);
            tv_menutable.setItems(observableList);
        } else super.populateTable(tv_menutable);
    }
}

