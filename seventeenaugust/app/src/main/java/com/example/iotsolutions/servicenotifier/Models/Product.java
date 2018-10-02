package com.example.iotsolutions.servicenotifier.Models;

/**
 * Created by Admin on 7/25/2018.
 */

public class Product {

    private String Pname;
    private String Phone;
    private String Interval;
    private String Date_of_purchase;
    private String end_date=null;

    public Product() {
    }

    public Product(String pname, String phone, String interval, String date_of_purchase, String end_date) {
        Pname = pname;
        Phone = phone;
        Interval = interval;
        Date_of_purchase = date_of_purchase;
        this.end_date = end_date;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getInterval() {
        return Interval;
    }

    public void setInterval(String interval) {
        Interval = interval;
    }

    public String getDate_of_purchase() {
        return Date_of_purchase;
    }

    public void setDate_of_purchase(String date_of_purchase) {
        Date_of_purchase = date_of_purchase;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
