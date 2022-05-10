package com.erictoader.fooddelivery.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Order implements Serializable {
    private Integer orderID;
    private String clientName;
    private Date orderDate;
    private String status;
    private String assignedEmployee;

    public static final String FINALIZED = "FINALIZED";
    public static final String IN_PROGRESS = "IN PROGRESS";
    public static final String NOT_ASSIGNED = "NOT ASSIGNED";

    public Order() {
    }

    public Order(Integer orderID, String clientName, Date orderDate, String status) {
        this.orderID = orderID;
        this.clientName = clientName;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(String assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public int hashCode() {
        return orderID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderID, order.orderID);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", clientName='" + clientName + '\'' +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", assignedEmployee='" + assignedEmployee + '\'' +
                '}';
    }
}
