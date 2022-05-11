package com.erictoader.fooddelivery.bll;

import com.erictoader.fooddelivery.ControllerClass;
import com.erictoader.fooddelivery.model.MenuItem;
import javafx.scene.control.TableView;

public final class Constants {
    public static final String USER_CLIENT = "client";
    public static final String USER_EMPLOYEE = "employee";
    public static final String USER_ADMIN = "admin";

    public static String USER_FULLNAME = "";
    public static String USER_CURRENTTYPE = "";

    public static DeliveryService ds = null;
    public static ControllerClass controller = null;
    public static TableView<MenuItem> tv;

    public static Integer dailyMenuIndex = 1;
    public static final String DAILY_MENU = "Daily Menu";
}
