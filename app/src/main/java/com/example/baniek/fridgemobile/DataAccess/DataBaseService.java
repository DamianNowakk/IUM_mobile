package com.example.baniek.fridgemobile.DataAccess;

import com.example.baniek.fridgemobile.Model.Product;
import com.example.baniek.fridgemobile.Model.User;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class DataBaseService {
    private static final DataBaseService ourInstance = new DataBaseService();
    public static DataBaseService getInstance() {
        return ourInstance;
    }

    private Realm realm;

    private DataBaseService() {
        realm = Realm.getDefaultInstance();
    }

    public void AddUser(User user)
    {
        realm.beginTransaction();
        realm.insert(user);
        realm.commitTransaction();
    }

    public User GetUser(String login)
    {
        RealmResults<User> result = realm.where(User.class).equalTo("login", login).findAll();

        if(result.size() == 0)
            return null;

        return result.get(0);
    }

    public void AddProduct(Product product)
    {
        realm.beginTransaction();
        realm.insertOrUpdate(product);
        realm.commitTransaction();
    }

    public Product GetProduct(Integer id)
    {
        RealmResults<Product> result = realm.where(Product.class).equalTo("id", id).findAll();

        if(result.size() == 0)
            return null;

        return result.get(0);
    }

    public RealmResults<Product> GetProducts(String login)
    {
        RealmResults<Product> result = realm.where(Product.class).equalTo("userLogin", login).findAll();

        return result;
    }

    public void Close() {
        realm.close();
    }
}
