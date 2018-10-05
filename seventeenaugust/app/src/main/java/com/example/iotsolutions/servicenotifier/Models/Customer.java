package com.example.iotsolutions.servicenotifier.Models;

/**
 * Created by Admin on 7/25/2018.
 */

public class Customer {

    private String Name;
    private String Email;
    private String isCustomer;
    private String sellerid;

    public Customer() {
    }

    public Customer(String name, String email, String isCustomer,String sellerid) {
        Name = name;
        Email = email;
        this.isCustomer = isCustomer;
        this.sellerid = sellerid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(String isCustomer) {
        this.isCustomer = isCustomer;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }
}
