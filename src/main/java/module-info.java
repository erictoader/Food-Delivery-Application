module com.erictoader.fooddelivery {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires mysql.connector.java;
    requires java.desktop;

    opens com.erictoader.fooddelivery to javafx.fxml;
    exports com.erictoader.fooddelivery;
    opens com.erictoader.fooddelivery.model to javafx.fxml;
    exports com.erictoader.fooddelivery.model;
}