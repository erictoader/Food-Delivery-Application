package com.erictoader.fooddelivery.connection;

import java.sql.*;

/*
 *   This class is for generating and closing a single database connection
 *   @author Toader Eric-Stefan
 */
public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:8889/FoodDeliveryApp";
    private static final String USERNAME = "FoodDeliveryAdmin";
    private static final String DATABASE_NAME = "FoodDeliveryApp";
    private static final String PASSWORD = "M_2aM/1@o7Znuh!k";

    private static final ConnectionFactory instance = new ConnectionFactory();

    /*
     *   Constructor with no parameters
     */
    private ConnectionFactory() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     *   Method that creates a connection with a database, given the URL, USERNAME and PASSWORD
     *  @return Connection object to the database
     */
    private Connection createConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /*
     *   Getter method that returns the connection instance
     *  @return Connection object to the database
     */
    public static Connection getConnection() {
        return instance.createConnection();
    }

    /*
     *   Method that closes the connection with the database
     */
    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     *   Method that closes the SQL statement
     */
    public static void close(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     *   Method that closes the SQL ResultSet
     */
    public static void close(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     *   Method that returns the database name, useful in query formation
     *  @return The database name
     */
    public static String getDBNAME() {
        return DATABASE_NAME;
    }

}