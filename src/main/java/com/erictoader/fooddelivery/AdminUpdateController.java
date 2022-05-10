package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.model.BaseProduct;
import com.erictoader.fooddelivery.model.CompositeProduct;
import com.erictoader.fooddelivery.model.MenuItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminUpdateController extends ControllerClass implements Initializable {

    private MenuItem old;
    private Double itemRating;

    @FXML
    private Button btn_update_cancel;

    @FXML
    private Button btn_update_confirm;

    @FXML
    private TextField tf_update_calories;

    @FXML
    private TextField tf_update_fat;

    @FXML
    private TextField tf_update_price;

    @FXML
    private TextField tf_update_protein;

    @FXML
    private TextField tf_update_sodium;

    @FXML
    private TextField tf_update_title;

    @FXML
    void updateItem(ActionEvent event) {

        MenuItem item;

        if(old.getClass() == BaseProduct.class) {
            item = new BaseProduct();
            item.setCalories(Integer.parseInt(tf_update_calories.getText()));
            item.setProtein(Integer.parseInt(tf_update_protein.getText()));
            item.setFat(Integer.parseInt(tf_update_fat.getText()));
            item.setSodium(Integer.parseInt(tf_update_sodium.getText()));
        } else {
            item = new CompositeProduct();
            ((CompositeProduct)item).setProducts(((CompositeProduct)old).getProducts());
        }
        item.setTitle(tf_update_title.getText());
        item.setRating(itemRating);
        item.setPrice(Integer.parseInt(tf_update_price.getText()));

        Constants.ds.updateItemData(old, item);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        Constants.controller.populateTable(Constants.tv);
        ((AdminPanelController)Constants.controller).showConfirmation("Item updated successfully!");
        ((AdminPanelController)Constants.controller).searchByTitle();
    }

    @FXML
    void cancel(ActionEvent event) {
        super.cancel(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tf_update_title.setEditable(false);
    }

    public void completeFields(MenuItem toUpdate) {
        this.old = toUpdate;
        tf_update_title.setText(toUpdate.getTitle());
        itemRating = toUpdate.getRating();
        tf_update_calories.setText(toUpdate.getCalories().toString());
        tf_update_protein.setText(toUpdate.getProtein().toString());
        tf_update_fat.setText(toUpdate.getFat().toString());
        tf_update_sodium.setText(toUpdate.getSodium().toString());
        tf_update_price.setText(toUpdate.getPrice().toString());
    }
}

