package com.erictoader.fooddelivery.dao;

import com.erictoader.fooddelivery.bll.Constants;
import com.erictoader.fooddelivery.bll.DeliveryService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserializator {
    public static void deserializeData() {
        DeliveryService deliveryService;
        try {
            FileInputStream fileIn = new FileInputStream("./utils/appdata.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            deliveryService = (DeliveryService)in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            deliveryService = new DeliveryService();
        }
        Constants.ds = deliveryService;
    }
}
