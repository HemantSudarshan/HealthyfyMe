package com.labs.healthify.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orderplace")
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String username;
    private String name;
    private String address;
    private String connum;
    private int pin;
    private String date;
    private String time;
    private float amount;
    private String otype;

    public Order(String username, String name, String address, String connum, 
                 int pin, String date, String time, float amount, String otype) {
        this.username = username;
        this.name = name;
        this.address = address;
        this.connum = connum;
        this.pin = pin;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.otype = otype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConnum() {
        return connum;
    }

    public void setConnum(String connum) {
        this.connum = connum;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }
}
