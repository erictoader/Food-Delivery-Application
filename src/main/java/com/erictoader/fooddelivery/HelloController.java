package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.DbUsersBLL;
import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.connection.ConnectionFactory;
import com.erictoader.fooddelivery.model.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class HelloController extends ControllerClass {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button btn_login;

    @FXML
    private Button btn_register;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_username;

    public void attemptLogin(ActionEvent event) {
        if(isServerRunning()) {
            DbUsersBLL dbUsersBLL = new DbUsersBLL();
            Users u = new Users();
            u.setUsername(tf_username.getText());
            u.setPass(tf_password.getText());
            ArrayList<Users> uList = (ArrayList<Users>) dbUsersBLL.findUser(u);

            if(uList.isEmpty()) {
                showDialog(event, "Incorrect login information", "There is no account that matches the provided login information.");
            } else {
                u.setUsertype(uList.get(0).getUsertype());
                Constants.USER_CURRENTTYPE = u.getUsertype();
                Constants.USER_FULLNAME = u.getUsername();
                try {
                    proceedLogin(event);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            showDialog(event, "Connection unavailable", "Server is down. Please try again later.");
        }
    }

    private void proceedLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome-view.fxml")));
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

    public void registerNewAccount(ActionEvent event) throws IOException {
        if(isServerRunning()) {
            Parent root1 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register-new-user.fxml")));
            Stage stage1 = new Stage();
            stage1.setTitle("Create an account");
            Scene scene1 = new Scene(root1);
            stage1.setScene(scene1);
            stage1.setResizable(false);
            stage1.show();
        } else {
            showDialog(event, "Connection unavailable", "Server is down. Please try again later.");
        }
    }

    private boolean isServerRunning() {
        Connection server = ConnectionFactory.getConnection();
        return (server != null);
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