package com.example.iotsolutions.servicenotifier.Models;

/**
 * Created by Admin on 7/25/2018.
 */

public class Seller {
    private String Email;
    private String isSeller;
    private String Name;
    private String Phone;


    public Seller() {
    }

    public Seller(String email, String isSeller, String name, String phone) {
        Email = email;
        this.isSeller = isSeller;
        Name = name;
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIsSeller() {
        return isSeller;
    }

    public void setIsSeller(String isSeller) {
        this.isSeller = isSeller;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }
}
