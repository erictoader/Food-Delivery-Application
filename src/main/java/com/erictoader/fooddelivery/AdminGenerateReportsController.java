package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.bll.ReportGenerator;
import com.erictoader.fooddelivery.model.MenuItem;
import com.erictoader.fooddelivery.model.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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
    private static final Integer M_ORDERED_PROD_REPORT = 1;
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
        currentSelection = M_ORDERED_PROD_REPORT;
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
        label_text2.setVisible(true);
        label_text2.setText("Date format: dd/mm/yyyy");
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
                HashMap<MenuItem, Integer> hashMap = new HashMap<>();
                for(Order o : Constants.ds.getOrders().keySet()) {
                    for(MenuItem mi : Constants.ds.getOrders().get(o)) {
                        if(hashMap.containsKey(mi)) {
                            hashMap.put(mi, hashMap.get(mi) + 1);
                        } else hashMap.put(mi, 1);
                    }
                }

                try {
                    int min = Integer.parseInt(tf_text1.getText());
                    Map<MenuItem, Integer> mostOrdered = hashMap.entrySet().stream()
                            .filter(p -> p.getValue() > min)
                            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
                    ReportGenerator.generateMostOrdered(mostOrdered, tf_text1.getText(), Constants.USER_FULLNAME);
                } catch (Exception e) {
                    super.showDialog(event, "Operation failed", "Please insert a number in the text field");
                    return;
                }
                break;
            case 2 :
                HashMap<OrderByCustomer, Integer> highOrders = new HashMap<>();
                try {
                    for(Order o : Constants.ds.getOrders().keySet()) {
                        int total = 0;
                        for(MenuItem mi : Constants.ds.getOrders().get(o)) {
                            total += mi.computePrice();
                        }
                        if(total > Integer.parseInt(tf_text2.getText())) {
                            OrderByCustomer customer = new OrderByCustomer(o.getClientName());
                            if(highOrders.containsKey(customer)) {
                                highOrders.put(customer, highOrders.get(customer) + 1);
                            } else highOrders.put(customer, 1);
                        }
                    }

                    int minOrders = Integer.parseInt(tf_text1.getText());
                    Map<OrderByCustomer, Integer> amountOrders = highOrders.entrySet().stream()
                            .filter(p -> p.getValue() > minOrders)
                            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
                    ReportGenerator.generateClientsReport(amountOrders, tf_text1.getText(),  tf_text2.getText(), Constants.USER_FULLNAME);
                } catch (Exception e) {
                    super.showDialog(event, "Operation failed", "Please insert numbers in the text fields");
                    return;
                }
                break;
            case 3 :
                try {
                    String date = tf_text1.getText();
                    String[] dates = date.split("/");
                    int day = Integer.parseInt(dates[0]);
                    int month = Integer.parseInt(dates[1]);
                    int year = Integer.parseInt(dates[2]);

                    Map<Order, ArrayList<MenuItem>> dayFilter = Constants.ds.getOrders().entrySet().stream()
                            .filter(p -> Instant.ofEpochMilli(p.getKey().getOrderDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()  == day &&
                                    Instant.ofEpochMilli(p.getKey().getOrderDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue() == month &&
                                    Instant.ofEpochMilli(p.getKey().getOrderDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate().getYear() == year)
                            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

                    HashMap<MenuItem, Integer> itemFreqHashMap = new HashMap<>();
                    for(Order o : dayFilter.keySet()) {
                        for(MenuItem mi : dayFilter.get(o)) {
                            if(itemFreqHashMap.containsKey(mi)) {
                                itemFreqHashMap.put(mi, itemFreqHashMap.get(mi) + 1);
                            } else itemFreqHashMap.put(mi, 1);
                        }
                    }
                    ReportGenerator.generateProductsWithinDay(itemFreqHashMap, tf_text1.getText(), Constants.USER_FULLNAME);
                } catch (Exception e) {
                    super.showDialog(event, "Invalid date", "Please insert a date with the specified format");
                    return;
                }
                break;
            default :
                return;
        }
        super.showDialog(event, "Report generated", "Report successfully generated and stored locally.");
    }

    public class OrderByCustomer {
        String customerName;

        public OrderByCustomer(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerName() {
            return customerName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderByCustomer that = (OrderByCustomer) o;
            if (this.customerName == null) return false;
            return customerName.equals(that.customerName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customerName);
        }
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
