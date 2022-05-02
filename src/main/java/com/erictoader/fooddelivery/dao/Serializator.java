package com.erictoader.fooddelivery.dao;

import com.erictoader.fooddelivery.bll.DeliveryService;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Serializator {
    public static void serializeData(DeliveryService deliveryService) {
        try {
            FileOutputStream fileOut = new FileOutputStream("./utils/appdata.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(deliveryService);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
