package com.example.baniek.fridgemobile.RestService;


import com.example.baniek.fridgemobile.Model.Product;
import com.example.baniek.fridgemobile.Model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("api/User")
    Call<User> GetUser(@Query("login") String login, @Query("password") String password);

    @POST("api/User")
    Call<User> AddUser(@Body User User);

    @GET("api/Product")
    Call<ArrayList<Product>> GetAllProducts(@Query("login") String login, @Query("password") String password);

    @GET("api/Product/{id}")
    Call<Product> GetProduct(@Path("id") int id, @Query("login") String login, @Query("password") String password);

    @DELETE("api/Product/{id}")
    Call<Void> DeleteProduct(@Path("id") int id, @Query("login") String login, @Query("password") String password);

    @POST("api/Product")
    Call<Void> AddProduct (@Body Product product, @Query("login") String login, @Query("password") String password);

    @PUT("api/Product/{id}")
    Call<Void> updateProduct (@Body Product product, @Path("id") int id, @Query("login") String login, @Query("password") String password);

    @PUT("api/Product/{id}/UpdateAmount/{value}")
    Call<Void> changeAmount (@Path("id") int id, @Path("value") int value, @Query("login") String login, @Query("password") String password);
}
