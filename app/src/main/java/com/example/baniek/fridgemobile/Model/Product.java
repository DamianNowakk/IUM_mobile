package com.example.baniek.fridgemobile.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Product {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("UserLogin")
    @Expose
    private String userLogin;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Price")
    @Expose
    private float price;
    @SerializedName("Amount")
    @Expose
    private  Integer amount;

    private transient Integer valueLastModyfide = 0;

    private transient boolean  isSync = false;

    public Product(Integer id, String userLogin, String name, float price, Integer amount, Integer valueLastModyfide, boolean isSync) {
        this.id = id;
        this.userLogin = userLogin;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.valueLastModyfide = valueLastModyfide;
        this.isSync = isSync;
    }

    public Product(int id, String userLogin, String name, float price, int amount) {
        this.id = id;
        this.userLogin = userLogin;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public Product(String userLogin, String name, float price, int amount) {
        this.userLogin = userLogin;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public Product() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getValueLastModyfide() {
        return valueLastModyfide;
    }

    public void setValueLastModyfide(Integer valueLastModyfide) {
        this.valueLastModyfide = valueLastModyfide;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public void sync()
    {
        isSync = true;
    }
}