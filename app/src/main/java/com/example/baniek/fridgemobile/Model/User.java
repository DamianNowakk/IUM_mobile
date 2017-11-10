package com.example.baniek.fridgemobile.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    @SerializedName("Login")
    @Expose
    private String login;

    @SerializedName("Password")
    @Expose
    private String password;


    public User(String login, String password)
    {
        this.login = login;
        this.password = password;
    }

    public User()
    {

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
