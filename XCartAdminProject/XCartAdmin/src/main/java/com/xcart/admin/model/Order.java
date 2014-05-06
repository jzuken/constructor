package com.xcart.admin.model;

public class Order {
    public Order(String id, String userName, String paid, String status, String fulfilmentStatus, String date) {
        this.id = id;
        this.userName = userName;
        this.paid = paid;
        this.status = status;
        this.fulfilmentStatus = fulfilmentStatus;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPaid() {
        return paid;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFulfilmentStatus() {
        return fulfilmentStatus;
    }

    public void setFulfilmentStatus(String fulfilmentStatus) {
        this.fulfilmentStatus = fulfilmentStatus;
    }

    private String id;
    private String userName;
    private String paid;
    private String status;
    private String fulfilmentStatus;
    private String date;
}