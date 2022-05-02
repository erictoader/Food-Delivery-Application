package com.erictoader.fooddelivery.model;

import java.util.Date;

public class Order {
    private Integer orderID;
    private Integer clientID;
    private Date orderDate;

    public Order() {
    }

    public Order(Integer orderID, Integer clientID, Date orderDate) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.orderDate = orderDate;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int hashCode() {
        return (orderID + 1) * (clientID + 1) * orderDate.getMonth();
    }
}
