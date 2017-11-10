package com.example.baniek.fridgemobile.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baniek.fridgemobile.Model.Product;
import com.example.baniek.fridgemobile.MainList.ProductCollection;
import com.example.baniek.fridgemobile.Model.User;
import com.example.baniek.fridgemobile.R;
import com.example.baniek.fridgemobile.RestService.RestController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private RestController restController = RestController.getInstance();
    private ProductCollection products = ProductCollection.getInstance();
    private Product product;
    private int position;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        user = new User(getIntent().getStringExtra("login"), getIntent().getStringExtra("password"));
        position = intent.getExtras().getInt("productId");

        product = products.getProduct(position);


        final EditText nameEditText = (EditText) findViewById(R.id.productDetailName);
        final EditText priceEditText = (EditText) findViewById(R.id.productDetailPrice);
        final EditText amountEditText = (EditText) findViewById(R.id.productDetailAmount);
        Button updateButton = (Button) findViewById(R.id.updateButton);

        nameEditText.setText(product.getName());
        priceEditText.setText(Float.toString(product.getPrice()));
        amountEditText.setText(product.getAmount().toString());



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setName(nameEditText.getText().toString());
                product.setPrice(Float.valueOf(priceEditText.getText().toString()));
                product.setAmount(Integer.valueOf(amountEditText.getText().toString()));


                Callback callback = new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        products.updateProduct(position, product);
                        Toast.makeText(ProductActivity.this, "Update successed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(ProductActivity.this, "Some problem with internet", Toast.LENGTH_LONG).show();
                    }
                };

                restController.UpdateProducts(product, callback, user.getLogin(), user.getPassword());
            }
        });
    }
}
