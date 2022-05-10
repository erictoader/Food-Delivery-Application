package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.bll.ReportGenerator;
import com.erictoader.fooddelivery.model.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static java.util.stream.Collectors.toList;

public class AdminGenerateReportsController extends ControllerClass implements Initializable {

    @FXML
    private Button btn_genReport;

    @FXML
    private Button btn_mostOrdered;

    @FXML
    private Button btn_ordersWithinDay;

    @FXML
    private Button btn_timeinterval;

    @FXML
    private Button btn_valuableClients;

    @FXML
    private Label label_text1;

    @FXML
    private Label label_text2;

    @FXML
    private TextField tf_text1;

    @FXML
    private TextField tf_text2;

    private static final Integer UNSELECTED_REPORT = -1;
    private static final Integer T_INTERVAL_REPORT = 0;
    private static final Integer M_ORDERS_REPORT = 1;
    private static final Integer V_CLIENTS_REPORT = 2;
    private static final Integer D_ORDERS_REPORT = 3;

    private Integer currentSelection = -1;

    @FXML
    void mostOrdered(ActionEvent event) {
        refreshSelection();
        btn_genReport.setVisible(true);
        btn_mostOrdered.setCancelButton(false);
        btn_mostOrdered.setDefaultButton(true);
        label_text1.setVisible(true);
        label_text1.setText("Ordered more times than:");
        tf_text1.setVisible(true);
        tf_text1.setText("");
        currentSelection = M_ORDERS_REPORT;
    }

    @FXML
    void ordersWithinDay(ActionEvent event) {
        refreshSelection();
        btn_genReport.setVisible(true);
        btn_ordersWithinDay.setCancelButton(false);
        btn_ordersWithinDay.setDefaultButton(true);
        label_text1.setVisible(true);
        label_text1.setText("Day of interest:");
        tf_text1.setVisible(true);
        tf_text1.setText("");
        currentSelection = D_ORDERS_REPORT;
    }

    @FXML
    void timeInterval(ActionEvent event) {
        refreshSelection();
        btn_genReport.setVisible(true);
        btn_timeinterval.setCancelButton(false);
        btn_timeinterval.setDefaultButton(true);
        label_text1.setVisible(true);
        label_text1.setText("Starting hour:");
        label_text2.setVisible(true);
        label_text2.setText("Ending hour:");
        tf_text1.setVisible(true);
        tf_text1.setText("");
        tf_text2.setVisible(true);
        tf_text2.setText("");
        currentSelection = T_INTERVAL_REPORT;
    }

    @FXML
    void valuableClients(ActionEvent event) {
        refreshSelection();
        btn_genReport.setVisible(true);
        btn_valuableClients.setCancelButton(false);
        btn_valuableClients.setDefaultButton(true);
        label_text1.setVisible(true);
        label_text1.setText("Ordered more times than:");
        label_text2.setVisible(true);
        label_text2.setText("Value of order was higher than:");
        tf_text1.setVisible(true);
        tf_text1.setText("");
        tf_text2.setVisible(true);
        tf_text2.setText("");
        currentSelection = V_CLIENTS_REPORT;
    }

    @FXML
    void generateReport(ActionEvent event) {
        switch (currentSelection) {
            case 0 :
                List<Order> orderList = Constants.ds.getOrders().keySet().stream()
                        .filter(p -> Integer.parseInt(tf_text1.getText()) <= p.getOrderDate().getHours()
                        && p.getOrderDate().getHours() <= Integer.parseInt(tf_text2.getText()))
                        .collect(toList());
                ReportGenerator.generateTimeIntervalReport(orderList,tf_text1.getText(),tf_text2.getText(),Constants.USER_FULLNAME);
                break;
            case 1 :
                break;
            case 2 :
                break;
            case 3 :
                break;
            default :
                return;
        }
        super.showDialog(event, "Report generated", "Report successfully generated and stored locally.");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshSelection();
    }

    private void refreshSelection() {
        btn_genReport.setVisible(false);
        btn_mostOrdered.setDefaultButton(false);
        btn_mostOrdered.setCancelButton(true);
        btn_ordersWithinDay.setDefaultButton(false);
        btn_ordersWithinDay.setCancelButton(true);
        btn_timeinterval.setDefaultButton(false);
        btn_timeinterval.setCancelButton(true);
        btn_valuableClients.setDefaultButton(false);
        btn_valuableClients.setCancelButton(true);
        label_text1.setVisible(false);
        label_text1.setText("");
        label_text2.setVisible(false);
        label_text2.setText("");
        tf_text1.setVisible(false);
        tf_text1.setText("");
        tf_text2.setVisible(false);
        tf_text2.setText("");
        currentSelection = UNSELECTED_REPORT;
    }
}
