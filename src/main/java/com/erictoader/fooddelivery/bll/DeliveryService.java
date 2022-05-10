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
        assert key != null : "The key is null";
        assert value != null : "The value is null";
        assert value.size() > 0 : "The value array is empty";

        int currentOrders = orders.size();
        orders.put(key, value);
        BillGenerator.generateBill(key, value);
        setChanged();
        notifyObservers();

    }

    public void addNewMenuItem(MenuItem menuItem) {
        assert menuItem != null : "Menu item is null";
        assert !menuItem.getTitle().isEmpty() : "Item does not have a title";

        menu.add(menuItem);
    }

    public void removeMenuItem(MenuItem menuItem) {
        assert menuItem != null : "Menu item is null";
        assert !menuItem.getTitle().isEmpty() : "Item does not have a title";

        int currentMenuSize = menu.size();
        menu.remove(menuItem);

        assert currentMenuSize == menu.size() + 1 : "Item was not removed";
    }

    public void inputMenuItemsFromFile(String path) throws IOException {
        assert path != null : "Path string is null";
        assert !path.isEmpty() : "Path string is empty";

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
        assert old != null : "Old menu item is null";
        assert old.getTitle() != null && !old.getTitle().isEmpty() : "Old menu item has no title";
        assert menu.contains(old) : "The menu does not currently contain the old menu item";
        assert item != null : "Updated menu item is null";
        assert item.getTitle() != null && !item.getTitle().isEmpty() : "Updated menu item has no title";
        assert old.getTitle().equalsIgnoreCase(item.getTitle()) : "The updated item does not have the same title as the old one. Cannot modify title";

        menu.remove(old);
        menu.add(item);

        assert menu.contains(item) : "Could not update item's details";
    }

    public Set<MenuItem> getMenu() {
        return menu;
    }

    public Map<Order, ArrayList<MenuItem>> getOrders() {
        return orders;
    }
}
