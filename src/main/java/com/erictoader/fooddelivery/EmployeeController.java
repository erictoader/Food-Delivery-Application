package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.dao.Deserializator;
import com.erictoader.fooddelivery.dao.Serializator;
import com.erictoader.fooddelivery.model.MenuItem;
import com.erictoader.fooddelivery.model.Order;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeController extends ControllerClass implements Observer, Initializable {

    @FXML
    private Button btn_admin_logout;

    @FXML
    private Button btn_finalize_order;

    @FXML
    private Button btn_start_processing;

    @FXML
    private Label label_currentsession;

    @FXML
    private TableColumn<Map.Entry<Order, ArrayList<MenuItem>>, String> tc_date;

    @FXML
    private TableColumn<Map.Entry<Order, ArrayList<MenuItem>>, String> tc_emp;

    @FXML
    private TableColumn<Map.Entry<Order, ArrayList<MenuItem>>, String> tc_orderid;

    @FXML
    private TableColumn<Map.Entry<Order, ArrayList<MenuItem>>, String> tc_status;

    @FXML
    private TableColumn<Map.Entry<Order, ArrayList<MenuItem>>, String> tc_contents;

    @FXML
    private TableView<Map.Entry<Order, ArrayList<MenuItem>>> tv_ordertable;

    @FXML
    void finalizeOrder(ActionEvent event) {
        Map.Entry<Order, ArrayList<MenuItem>> selectedItem = tv_ordertable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            if(selectedItem.getKey().getStatus().equals(Order.IN_PROGRESS)) {
                if(selectedItem.getKey().getAssignedEmployee().equals(Constants.USER_FULLNAME)) {
                    selectedItem.getKey().setStatus(Order.FINALIZED);
                    Constants.ds.addNewOrder(selectedItem.getKey(), selectedItem.getValue());
                    Serializator.serializeData(Constants.ds);
                    Deserializator.deserializeData();
                    populateTable();
                } else super.showDialog(event, "Unable to finalize", "Cannot finalize another employee's order");
            } else super.showDialog(event, "Unable to finalize", "Order must be in progress before finalizing.");
        } else super.showDialog(event, "Operation denied", "Select an item before attempting to finalize the order");
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        super.logout(event);
    }

    @FXML
    void startProcessingOrder(ActionEvent event) {
        Map.Entry<Order, ArrayList<MenuItem>> selectedItem = tv_ordertable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            if(selectedItem.getKey().getStatus().equals(Order.NOT_ASSIGNED)) {
                selectedItem.getKey().setStatus(Order.IN_PROGRESS);
                selectedItem.getKey().setAssignedEmployee(Constants.USER_FULLNAME);
                Constants.ds.addNewOrder(selectedItem.getKey(), selectedItem.getValue());
                Serializator.serializeData(Constants.ds);
                Deserializator.deserializeData();
                populateTable();
            } else super.showDialog(event, "Unable to start processing", "Order must not have an assigned employee");
        } else super.showDialog(event, "Operation denied", "Select an item before attempting to start processing order");
    }

    @Override
    public void update(Observable o, Object arg) {
        populateTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label_currentsession.setText("Current session : " + Constants.USER_FULLNAME);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        tc_orderid.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey().getOrderID().toString()));
        tc_date.setCellValueFactory(p -> new SimpleStringProperty(formatter.format(p.getValue().getKey().getOrderDate())));
        tc_status.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey().getStatus()));
        tc_emp.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey().getAssignedEmployee()));
        tc_contents.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        Constants.ds.addObserver(this);


        populateTable();
    }

    private void populateTable() {
        if(Constants.ds == null) {
            Deserializator.deserializeData();
        }

        Map<Order, ArrayList<MenuItem>> orderSet = Constants.ds.getOrders();
        ObservableMap<Order, ArrayList<MenuItem>> set = FXCollections.observableMap(orderSet);
        ObservableList<Map.Entry<Order, ArrayList<MenuItem>>> list = FXCollections.observableArrayList(set.entrySet().stream().toList());
        tv_ordertable.setItems(list);

        Serializator.serializeData(Constants.ds);
    }
}