package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;

public class GreetingController extends ControllerClass implements Initializable {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label label_greeting;
    @FXML
    private AnchorPane ap_greeting;

    private static final String MORNING = "Good morning, ";
    private static final String AFTERNOON = "Good afternoon, ";
    private static final String EVENING = "Good evening, ";

    public void initialize(URL url, ResourceBundle rb) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(formatter.format(calendar.getTime()));
        StringBuilder sb = new StringBuilder();

        if(hour > 5 && hour < 12) {
            sb.append(MORNING);
        } else if(hour >= 12 && hour < 18) {
            sb.append(AFTERNOON);
        } else {
            sb.append(EVENING);
        }

        sb.append(Constants.USER_FULLNAME);
        label_greeting.setText(sb.toString());

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            private int i = 0;
            @Override
            public void handle(ActionEvent event) {
                if(i == 1) {
                    try {
                        showPanel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                i++;
            }
        }));
        timeline.setCycleCount(2);
        timeline.play();
    }

    private void showPanel() throws IOException {
        if(Constants.USER_CURRENTTYPE.equals(Constants.USER_ADMIN)) {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin-control-panel.fxml")));
        } else if(Constants.USER_CURRENTTYPE.equals(Constants.USER_EMPLOYEE)) {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("employee-panel.fxml")));
        } else {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-panel.fxml")));
        }
        stage = (Stage)ap_greeting.getScene().getWindow();
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
}
