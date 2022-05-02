package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.DbUsersBLL;
import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.bll.validators.UserPasswordValidator;
import com.erictoader.fooddelivery.bll.validators.customExceptions.PasswordTooShortException;
import com.erictoader.fooddelivery.bll.validators.customExceptions.PasswordTooSimpleException;
import com.erictoader.fooddelivery.model.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RegisterNewUserController extends ControllerClass {

    @FXML
    private Button btn_reg_cancel;

    @FXML
    private Button btn_reg_register;

    @FXML
    private TextField tf_reg_password;

    @FXML
    private TextField tf_reg_username;

    @FXML
    void cancel(ActionEvent event) {
        closeView(event);
    }

    private static final String REG_FAILED = "Registration failed";
    private static final String REG_SUCCESS = "Registration successful";

    @FXML
    void attemptRegister(ActionEvent event) {
        Users newUser = new Users();
        newUser.setUsername(tf_reg_username.getText());
        newUser.setPass(tf_reg_password.getText());
        newUser.setUsertype(Constants.USER_CLIENT);

        if(validateUsername(event, newUser) && validatePassword(event, newUser)) {
            DbUsersBLL dbUsersBLL = new DbUsersBLL();
            dbUsersBLL.insert(newUser);
            showDialog(event, REG_SUCCESS, "Your account has been registered. You can now use its credentials to log in.");
            closeView(event);
        }
    }

    private boolean validateUsername(ActionEvent event, Users u) {
        if(u.getUsername().equals("")) {
            showDialog(event, REG_FAILED, "Username field must not be empty.");
            return false;
        }
        DbUsersBLL dbUsersBLL = new DbUsersBLL();
        ArrayList<Users> uList = (ArrayList<Users>) dbUsersBLL.findUserByUsername(u);

        if(uList.isEmpty()) {
            return true;
        } else {
            showDialog(event, REG_FAILED, "The given username is already in use.");
            return false;
        }
    }

    private boolean validatePassword(ActionEvent event, Users u) {
        UserPasswordValidator passwordValidator = new UserPasswordValidator();

        try {
            passwordValidator.validate(u);
            return true;
        } catch (PasswordTooShortException | PasswordTooSimpleException ptsEx) {
            showDialog(event, REG_FAILED, ptsEx.getMessage());
            return false;
        } catch (Exception e) {
            showDialog(event, REG_FAILED, "Unexpected error occurred. Try again.");
            return false;
        }
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

    private void closeView(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}