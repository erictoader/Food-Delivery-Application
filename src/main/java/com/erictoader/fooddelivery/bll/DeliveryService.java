package com.erictoader.fooddelivery.bll;

import com.erictoader.fooddelivery.model.BaseProduct;
import com.erictoader.fooddelivery.model.MenuItem;
import com.erictoader.fooddelivery.model.Order;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class DeliveryService extends Observable implements IDeliveryServiceProcessing, Serializable {
    private final Set<MenuItem> menu;
    private final Map<Order, ArrayList<MenuItem>> orders;

    public DeliveryService() {
        menu = new TreeSet<>();
        orders = new HashMap<>();
    }

    public void addNewOrder(Order key, ArrayList<MenuItem> value) {
        orders.put(key, value);
    }

    public void addNewMenuItem(MenuItem menuItem) {
        menu.add(menuItem);
    }

    public void removeMenuItem(MenuItem menuItem) {
        menu.remove(menuItem);
    }

    public void inputMenuItemsFromFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        br.readLine();
        while ((line = br.readLine()) != null) {
            // split on comma(',')
            String[] productCsv = line.split(",");

            // create object to store values
            BaseProduct menuItem = new BaseProduct();

            // add values from csv to object
            menuItem.setTitle(productCsv[0]);
            menuItem.setRating(Double.parseDouble(productCsv[1]));
            menuItem.setCalories(Integer.parseInt(productCsv[2]));
            menuItem.setProtein(Integer.parseInt(productCsv[3]));
            menuItem.setFat(Integer.parseInt(productCsv[4]));
            menuItem.setSodium(Integer.parseInt(productCsv[5]));
            menuItem.setPrice(Integer.parseInt(productCsv[6]));

            // adding objects to a list
            addNewMenuItem(menuItem);
        }
    }

    public void updateItemData(MenuItem old, MenuItem item) {
        menu.remove(old);
        menu.add(item);
    }

    public Set<MenuItem> getMenu() {
        return menu;
    }
}
