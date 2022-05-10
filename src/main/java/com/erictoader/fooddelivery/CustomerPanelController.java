package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.dao.Serializator;
import com.erictoader.fooddelivery.model.MenuItem;
import com.erictoader.fooddelivery.model.Order;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CustomerPanelController extends ControllerClass implements Initializable {
    private final static String CART_EMPTY = "Your shopping cart is empty!";
    private final static String CART_NOT_EMPTY = "Your shopping cart:";

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    @FXML
    private Button btn_applyfilters;

    @FXML
    private Button btn_clear_filterfields;

    @FXML
    private Button btn_client_addcart;

    @FXML
    private Button btn_client_gocheckout;

    @FXML
    private Button btn_client_hidefilters;

    @FXML
    private Button btn_client_logout;

    @FXML
    private Button btn_client_showfilters;

    @FXML
    private Button btn_resetfilters;

    @FXML
    private CheckBox cb_12stars;

    @FXML
    private CheckBox cb_23stars;

    @FXML
    private CheckBox cb_34stars;

    @FXML
    private CheckBox cb_45stars;

    @FXML
    private CheckBox cb_5stars;

    @FXML
    private CheckBox cb_norating;

    @FXML
    private Label label_currentsession;

    @FXML
    private Label label_shoppingcart_status;

    @FXML
    private Label label_shoppingcart_tooltip;

    @FXML
    private Label label_shoppingcart_total;

    @FXML
    private Pane pane_customer_search;

    @FXML
    private Pane pane_customer_tv;

    @FXML
    private TextField tf_calories_from;

    @FXML
    private TextField tf_calories_to;

    @FXML
    private TextField tf_fat_from;

    @FXML
    private TextField tf_fat_to;

    @FXML
    private TextField tf_price_from;

    @FXML
    private TextField tf_price_to;

    @FXML
    private TextField tf_protein_from;

    @FXML
    private TextField tf_protein_to;

    @FXML
    private TextField tf_sodium_from;

    @FXML
    private TextField tf_sodium_to;

    @FXML
    private TextField tf_search_by_title;

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
    private TableColumn<MenuItem, Integer> tc_shoppingcart_price;

    @FXML
    private TableColumn<MenuItem, Integer> tc_shoppingcart_title;

    @FXML
    private TableColumn<MenuItem, Integer> tc_sodium;

    @FXML
    private TableColumn<MenuItem, String> tc_title;

    @FXML
    private TableView<MenuItem> tv_menutable;

    @FXML
    private TableView<MenuItem> tv_shoppingcart;

    private ArrayList<MenuItem> shoppingCart;
    private boolean isFilterShown = false;
    private boolean searchApplied = false;
    private boolean filtersApplied = false;

    @FXML
    void addToCart(ActionEvent event) {
        MenuItem toAdd = tv_menutable.getSelectionModel().getSelectedItem();
        if(toAdd != null) {
            shoppingCart.add(toAdd);
            refreshShoppingCart();
        } else super.showDialog(event, "Operation denied", "Select an item before attempting to add to cart");
    }

    @FXML
    void attemptCheckout(ActionEvent event) {
        if(!shoppingCart.isEmpty()) {
            Integer orderID = Constants.ds.getOrders().size();
            Order order = new Order(orderID, Constants.USER_FULLNAME, new Date(), Order.NOT_ASSIGNED);

            Constants.ds.addNewOrder(order, shoppingCart);
            Serializator.serializeData(Constants.ds);

            shoppingCart = new ArrayList<>();
            refreshShoppingCart();
            super.showDialog(event, "Order placed", "Your order has been successfully placed!");
        } else super.showDialog(event, "Checkout unavailable", "Shopping cart is empty!");
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        super.logout(event);
    }

    public void refreshShoppingCart() {
        ObservableList<MenuItem> list = FXCollections.observableArrayList(shoppingCart);
        tv_shoppingcart.setItems(list);
        if(shoppingCart.size() > 0) {
            tv_shoppingcart.setVisible(true);
            label_shoppingcart_status.setText(CART_NOT_EMPTY);
            label_shoppingcart_status.setLayoutX(98.0);
            label_shoppingcart_tooltip.setVisible(true);
            int total = 0;
            for(MenuItem m : shoppingCart) {
                total += m.computePrice();
            }
            label_shoppingcart_total.setText("Total: " + total);
            label_shoppingcart_total.setVisible(true);
            btn_client_gocheckout.setVisible(true);
        } else {
            tv_shoppingcart.setVisible(false);
            label_shoppingcart_status.setText(CART_EMPTY);
            label_shoppingcart_status.setLayoutX(64.0);
            label_shoppingcart_tooltip.setVisible(false);
            label_shoppingcart_total.setText("Total: 0");
            label_shoppingcart_total.setVisible(false);
            btn_client_gocheckout.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shoppingCart = new ArrayList<>();

        btn_client_gocheckout.setVisible(false);
        tv_shoppingcart.setVisible(false);
        label_shoppingcart_status.setText(CART_EMPTY);
        label_shoppingcart_tooltip.setVisible(false);
        label_shoppingcart_total.setVisible(false);
        label_currentsession.setText("Current session : " + Constants.USER_FULLNAME);

        tc_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tc_rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        tc_calories.setCellValueFactory(new PropertyValueFactory<>("calories"));
        tc_protein.setCellValueFactory(new PropertyValueFactory<>("protein"));
        tc_fat.setCellValueFactory(new PropertyValueFactory<>("fat"));
        tc_sodium.setCellValueFactory(new PropertyValueFactory<>("sodium"));
        tc_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        tc_shoppingcart_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tc_shoppingcart_price.setCellValueFactory(new PropertyValueFactory<>("price"));


        tv_shoppingcart.setRowFactory(tv -> {
            TableRow<MenuItem> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragExited(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != (Integer) db.getContent(SERIALIZED_MIME_TYPE)) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDone(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    MenuItem draggedItem = tv_shoppingcart.getItems().remove(draggedIndex);
                    shoppingCart.remove(draggedItem);
                    refreshShoppingCart();
                    event.consume();
                }
            });

            return row ;
        });

        populateTable(tv_menutable);
    }

    @FXML
    public void showFilters(ActionEvent event) {
        if(!isFilterShown) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
                private int i = 1;
                @Override
                public void handle(ActionEvent event) {
                    tv_menutable.setPrefWidth(1100.0 - i);
                    pane_customer_tv.setPrefWidth(1100.0 - i);
                    pane_customer_search.setPrefWidth(1100.0 - i);
                    btn_client_showfilters.setLayoutX(btn_client_showfilters.getLayoutX() - 1.0);

                    i++;
                    if(i == 300) {
                        isFilterShown = true;
                        btn_client_showfilters.setText("Hide filters   ☰");
                    }
                }
            }));
            timeline.setCycleCount(300);
            timeline.play();
        } else hideFilters(event);
    }

    public void hideFilters(ActionEvent event) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            private int i = 2;
            @Override
            public void handle(ActionEvent event) {
                tv_menutable.setPrefWidth(800.0 + i);
                pane_customer_tv.setPrefWidth(800.0 + i);
                pane_customer_search.setPrefWidth(800.0 + i);
                btn_client_showfilters.setLayoutX(btn_client_showfilters.getLayoutX() + 2.0);

                i+=2;
                if(i == 300) {
                    isFilterShown = false;
                    btn_client_showfilters.setText("Show filters   ☰");
                }
            }
        }));
        timeline.setCycleCount(150);
        timeline.play();
    }

    // processing filters

    @FXML
    void resetFilters(ActionEvent event) {
        clearFilterFields(event);
        filtersApplied = false;
        searchApplied = true;
        searchByTitle(event);
    }

    @FXML
    void clearFilterFields(ActionEvent event) {
        tf_calories_from.clear();
        tf_calories_to.clear();
        tf_protein_from.clear();
        tf_protein_to.clear();
        tf_fat_from.clear();
        tf_fat_to.clear();
        tf_sodium_from.clear();
        tf_sodium_to.clear();
        tf_price_from.clear();
        tf_price_to.clear();
        cb_norating.setSelected(false);
        cb_12stars.setSelected(false);
        cb_23stars.setSelected(false);
        cb_34stars.setSelected(false);
        cb_45stars.setSelected(false);
        cb_5stars.setSelected(false);
    }

    @FXML
    void applyFilters(ActionEvent event) {
        int caloriesMin = 0;
        int caloriesMax = Integer.MAX_VALUE;

        int proteinMin = 0;
        int proteinMax = Integer.MAX_VALUE;

        int fatMin = 0;
        int fatMax = Integer.MAX_VALUE;

        int sodiumMin = 0;
        int sodiumMax = Integer.MAX_VALUE;

        int priceMin = 0;
        int priceMax = Integer.MAX_VALUE;

        class StarInterval {
            int lowerBound;
            int upperBound;

            public StarInterval(int lowerBound, int upperBound) {
                this.lowerBound = lowerBound;
                this.upperBound = upperBound;
            }
        }
        List<StarInterval> starFilters = new ArrayList<>();

        if(cb_norating.isSelected()) {
            starFilters.add(new StarInterval(0,0));
        }
        if(cb_12stars.isSelected()) {
            starFilters.add(new StarInterval(1,2));
        }
        if(cb_23stars.isSelected()) {
            starFilters.add(new StarInterval(2,3));
        }
        if(cb_34stars.isSelected()) {
            starFilters.add(new StarInterval(3,4));
        }
        if(cb_45stars.isSelected()) {
            starFilters.add(new StarInterval(4,5));
        }
        if(cb_5stars.isSelected()) {
            starFilters.add(new StarInterval(5,5));
        }

        if(!tf_calories_from.getText().equals("")) {
            caloriesMin = Integer.parseInt(tf_calories_from.getText());
        }
        if(!tf_calories_to.getText().equals("")) {
            caloriesMax = Integer.parseInt(tf_calories_to.getText());
        }
        if(!tf_protein_from.getText().equals("")) {
            proteinMin = Integer.parseInt(tf_protein_from.getText());
        }
        if(!tf_protein_to.getText().equals("")) {
            proteinMax = Integer.parseInt(tf_protein_to.getText());
        }
        if(!tf_fat_from.getText().equals("")) {
            fatMin = Integer.parseInt(tf_fat_from.getText());
        }
        if(!tf_fat_to.getText().equals("")) {
            fatMax = Integer.parseInt(tf_fat_to.getText());
        }
        if(!tf_sodium_from.getText().equals("")) {
            sodiumMin = Integer.parseInt(tf_sodium_from.getText());
        }
        if(!tf_sodium_to.getText().equals("")) {
            sodiumMax = Integer.parseInt(tf_sodium_to.getText());
        }
        if(!tf_price_from.getText().equals("")) {
            priceMin = Integer.parseInt(tf_price_from.getText());
        }
        if(!tf_price_to.getText().equals("")) {
            priceMax = Integer.parseInt(tf_price_to.getText());
        }

        if(!searchApplied) {
            List<MenuItem> currentSelection = Constants.ds.getMenu().stream().toList();
            List<MenuItem> filtered = new ArrayList<>();
            for(MenuItem item : currentSelection) {
                if(priceMin <= item.computePrice() && item.computePrice() <= priceMax &&
                        caloriesMin <= item.getCalories() && item.getCalories() <= caloriesMax &&
                        proteinMin <= item.getProtein() && item.getProtein() <= proteinMax &&
                        fatMin <= item.getFat() && item.getFat() <= fatMax &&
                        sodiumMin <= item.getSodium() && item.getSodium() <= sodiumMax) {

                    if(!starFilters.isEmpty()) {
                        for(StarInterval st : starFilters) {
                            if(st.upperBound == 0 && item.getRating() == 0) {
                                filtered.add(item);
                                break;
                            } else if (st.lowerBound == 5 && item.getRating() == 5) {
                                filtered.add(item);
                                break;
                            } else {
                                if(st.lowerBound <= item.getRating() && item.getRating() < st.upperBound) {
                                    filtered.add(item);
                                    break;
                                }
                            }
                        }
                    } else filtered.add(item);
                }
            }
            ObservableList<MenuItem> observableList = FXCollections.observableArrayList(filtered);
            tv_menutable.setItems(observableList);
            filtersApplied = true;
        } else {
            filtersApplied = false;
            searchByTitle(event);

            List<MenuItem> currentSelection = tv_menutable.getItems();
            List<MenuItem> filtered = new ArrayList<>();
            for(MenuItem item : currentSelection) {
                if(priceMin <= item.computePrice() && item.computePrice() <= priceMax &&
                        caloriesMin <= item.getCalories() && item.getCalories() <= caloriesMax &&
                        proteinMin <= item.getProtein() && item.getProtein() <= proteinMax &&
                        fatMin <= item.getFat() && item.getFat() <= fatMax &&
                        sodiumMin <= item.getSodium() && item.getSodium() <= sodiumMax) {

                    if(!starFilters.isEmpty()) {
                        for(StarInterval st : starFilters) {
                            if(st.upperBound == 0 && item.getRating() == 0) {
                                filtered.add(item);
                                break;
                            } else if (st.lowerBound == 5 && item.getRating() == 5) {
                                filtered.add(item);
                                break;
                            } else {
                                if(st.lowerBound <= item.getRating() && item.getRating() < st.upperBound) {
                                    filtered.add(item);
                                    break;
                                }
                            }
                        }
                    } else filtered.add(item);
                }
            }
            ObservableList<MenuItem> observableList = FXCollections.observableArrayList(filtered);
            tv_menutable.setItems(observableList);
            filtersApplied = true;
        }
    }

    @FXML
    void searchByTitle(ActionEvent event) {
        if(!filtersApplied) {
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
                searchApplied = true;
            } else {
                super.populateTable(tv_menutable);
                searchApplied = false;
            }
        } else {
            if(!Objects.equals(tf_search_by_title.getText(), "")) {
                searchApplied = false;
                applyFilters(event);

                List<MenuItem> currentSelection = tv_menutable.getItems();
                List<MenuItem> filteredByName = new ArrayList<>();
                for(MenuItem item : currentSelection) {
                    if(item.getTitle().toLowerCase().contains(tf_search_by_title.getText().toLowerCase())) {
                        filteredByName.add(item);
                    }
                }
                ObservableList<MenuItem> observableList = FXCollections.observableArrayList(filteredByName);
                tv_menutable.setItems(observableList);
                searchApplied = true;
            } else {
                searchApplied = false;
                applyFilters(event);
            }
        }
    }
}

// search
// if no filters
//      take full table
//      search
// if filters
//      take full table
//      apply filters
//      take displayed table
//      search

// filters
// if no search
//      take full table
//      apply filters
// if search
//      take full table
//      apply search
//      take displayed table
//      apply filters
