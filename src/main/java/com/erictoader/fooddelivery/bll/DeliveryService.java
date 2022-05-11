package com.erictoader.fooddelivery.bll;

import com.erictoader.fooddelivery.model.BaseProduct;
import com.erictoader.fooddelivery.model.MenuItem;
import com.erictoader.fooddelivery.model.Order;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author Toader Eric-Stefan
 * This class contains both the menu and the orders placed and implements methods that add/update/remove elements in these collections
 */
public class DeliveryService extends Observable implements IDeliveryServiceProcessing, Serializable {
    private final Set<MenuItem> menu;
    private final Map<Order, ArrayList<MenuItem>> orders;

    /**
     * Empty constructor that is called whenever information cannot be deserialized due to any reason, resetting the content of the menu and orders
     */
    public DeliveryService() {
        menu = new TreeSet<>();
        orders = new HashMap<>();
    }

    /**
     * Method that attempts to insert into the collection of orders, and if successful, will generate a new bill that will be stored locally and will finally notify the employees interface
     * @param key A unique, non-null instance of an Order object, that will be associated to the newly placed order
     * @param value A non-null, non-empty list of menu items that have been ordered by a customer
     */
    public void addNewOrder(Order key, ArrayList<MenuItem> value) {
        assert key != null : "The key is null";
        assert value != null : "The value is null";
        assert value.size() > 0 : "The value array is empty";

        orders.put(key, value);
        BillGenerator.generateBill(key, value);
        setChanged();
        notifyObservers();
    }

    /**
     * Method that attempts to insert into the collection of menu items, and will be successful if another item with the same name is already present in the collection
     * @param menuItem A non-null, unique menu item that will be inserted into the existing collection of menu items
     */
    public void addNewMenuItem(MenuItem menuItem) {
        assert menuItem != null : "Menu item is null";
        assert !menuItem.getTitle().isEmpty() : "Item does not have a title";

        menu.add(menuItem);
    }

    /**
     * Method that attempts to remove from the collection of menu items, and will be successful if the item provided is already a part of the collection
     * @param menuItem A non-null, existing menu item that will be removed from the existing collection of menu items
     */
    public void removeMenuItem(MenuItem menuItem) {
        assert menuItem != null : "Menu item is null";
        assert !menuItem.getTitle().isEmpty() : "Item does not have a title";

        int currentMenuSize = menu.size();
        menu.remove(menuItem);

        assert currentMenuSize == menu.size() + 1 : "Item was not removed";
    }

    /**
     * Method that will parse and populate the menu from a locally stored .csv file
     * @param path A non-null, non-empty path to a valid .csv file
     * @throws IOException In case the provided path does not lead to a valid .csv file
     */
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

    /**
     * Method that will update the information of an already existing menu item in the collection
     * @param old A non-null reference to an already existing menu item in the collection
     * @param item A non-null reference to modified menu item that keeps the same name as the previous already existing menu item
     */
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
