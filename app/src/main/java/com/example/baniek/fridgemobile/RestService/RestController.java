package com.example.baniek.fridgemobile.RestService;

import com.example.baniek.fridgemobile.Activity.FridgeActivity;
import com.example.baniek.fridgemobile.DataAccess.DataBaseService;
import com.example.baniek.fridgemobile.Model.Product;
import com.example.baniek.fridgemobile.Model.User;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestController  {

    private static final String ENDPOINT_URL = "http://192.168.0.3:62838/";
    private DataBaseService dataBaseService;
    private Api api;
    private String guid;

    private static final RestController ourInstance = new RestController();

    public static RestController getInstance() {
        return ourInstance;
    }

    private RestController() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        api = retrofit.create(Api.class);
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void GetUser(Callback<User> callback , String login, String password)
    {
        Call<User> call = api.GetUser(login, password);
        call.enqueue(callback);
    }

    public void AddUser(Callback<User> callback ,User user)
    {
        Call<User> call = api.AddUser(user);
        call.enqueue(callback);
    }

    public void GetAllProducts(Callback<ArrayList<Product>> callback, String login, String password)
    {
        Call<ArrayList<Product>> call = api.GetAllProducts(login, password);
        call.enqueue(callback);
    }

    public void GetProducts(int i)
    {

    }

    public void DeleteProducts(int id,Callback<Product> callback, String login, String password)
    {

        Call call = api.DeleteProduct(id, login, password);
        if(callback == null) {
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        }
        else
        {
            call.enqueue(callback);
        }
    }

    public void AddProducts(Product product ,Callback<Product> callback, String login, String password)
    {
        Call call = api.AddProduct(product, login, password, guid);
        call.enqueue(callback);
    }

    public void UpdateProducts(Product product ,Callback<Product> callback, String login, String password)
    {
        Call call = api.updateProduct(product ,product.getId(), login, password, guid);
        call.enqueue(callback);
    }

    public void ChangeAmount(int id, int value ,Callback<Product> callback, String login, String password)
    {
        Call call = api.changeAmount(id, value, login, password, guid);
        call.enqueue(callback);
    }
}
