package com.example.baniek.fridgemobile.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baniek.fridgemobile.DataAccess.DataBaseService;
import com.example.baniek.fridgemobile.Model.Product;
import com.example.baniek.fridgemobile.MainList.ProductCollection;
import com.example.baniek.fridgemobile.Model.User;
import com.example.baniek.fridgemobile.R;
import com.example.baniek.fridgemobile.RestService.RestController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private DataBaseService dataBaseService;
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

        dataBaseService = DataBaseService.getInstance(this);


        final TextView nameEditText = (TextView) findViewById(R.id.productDetailName);
        final TextView priceEditText = (TextView) findViewById(R.id.productDetailPrice);
        final TextView amountEditText = (TextView) findViewById(R.id.productDetailAmount);
        Button updateButton = (Button) findViewById(R.id.updateButton);
        Button increaseAmountButton = (Button) findViewById(R.id.increaseAmount);
        Button decreaseAmountButton = (Button) findViewById(R.id.decreaseAmount);

        nameEditText.setText(product.getName());
        priceEditText.setText(Float.toString(product.getPrice()));
        amountEditText.setText(product.getAmount().toString());



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setName(nameEditText.getText().toString());
                product.setPrice(Float.valueOf(priceEditText.getText().toString()));
                product.setAmount(Integer.valueOf(amountEditText.getText().toString()));

                if(isOnline()) {
                    Callback callback = new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            product.sync();
                            dataBaseService.UpdateProduct(product);

                            products.updateProduct(position, product);
                            Toast.makeText(ProductActivity.this, "Update successed, sync", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            product.setSync(false);
                            dataBaseService.UpdateProduct(product);

                            products.updateProduct(position, product);
                            Toast.makeText(ProductActivity.this, "Update successed, no sync, Some problem with internet", Toast.LENGTH_LONG).show();
                        }
                    };
                    restController.ChangeAmount(product.getId(), product.getValueLastModyfide(), callback, user.getLogin(), user.getPassword());
                }
                else
                {
                    product.setSync(false);
                    dataBaseService.UpdateProduct(product);

                    products.updateProduct(position, product);
                    Toast.makeText(ProductActivity.this, "Update successed, no sync", Toast.LENGTH_LONG).show();
                }


            }
        });

        increaseAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setAmount(product.getAmount() + 1);
                product.setValueLastModyfide(product.getValueLastModyfide() + 1);
                amountEditText.setText(product.getAmount().toString());
            }
        });

        decreaseAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.getAmount() - 1 >= 0) {
                    product.setAmount(product.getAmount() - 1);
                    product.setValueLastModyfide(product.getValueLastModyfide() - 1);
                    amountEditText.setText(product.getAmount().toString());
                }
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
