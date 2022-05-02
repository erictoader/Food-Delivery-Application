package com.erictoader.fooddelivery;

import com.erictoader.fooddelivery.model.BaseProduct;
import com.erictoader.fooddelivery.model.MenuItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class TestClass {

    public void getData(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        Set<MenuItem> menuItems = new TreeSet<>();
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            // split on comma(',')
            String[] productCsv = line.split(",");

            // create car object to store values
            BaseProduct menuItem = new BaseProduct();

            // add values from csv to car object
            menuItem.setTitle(productCsv[0]);
            menuItem.setRating(Double.parseDouble(productCsv[1]));
            menuItem.setCalories(Integer.parseInt(productCsv[2]));
            menuItem.setProtein(Integer.parseInt(productCsv[3]));
            menuItem.setFat(Integer.parseInt(productCsv[4]));
            menuItem.setSodium(Integer.parseInt(productCsv[5]));
            menuItem.setPrice(Integer.parseInt(productCsv[6]));

            // adding car objects to a list
            menuItems.add(menuItem);
        }
    }
}
