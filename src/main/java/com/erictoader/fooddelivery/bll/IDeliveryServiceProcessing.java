package com.erictoader.fooddelivery.bll;

import com.erictoader.fooddelivery.model.MenuItem;
import com.erictoader.fooddelivery.model.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public interface IDeliveryServiceProcessing {

    void addNewOrder(Order key, ArrayList<MenuItem> value);

    void addNewMenuItem(MenuItem menuItem);

    void removeMenuItem(MenuItem menuItem);

    void inputMenuItemsFromFile(String path) throws IOException;

    void updateItemData(MenuItem old, MenuItem item);

    Set<MenuItem> getMenu();

}
