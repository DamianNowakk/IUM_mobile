package com.example.baniek.fridgemobile.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.baniek.fridgemobile.DataAccess.DataBaseService;
import com.example.baniek.fridgemobile.Model.Product;
import com.example.baniek.fridgemobile.MainList.ProductCollection;
import com.example.baniek.fridgemobile.Model.User;
import com.example.baniek.fridgemobile.MainList.ProductAdapter;
import com.example.baniek.fridgemobile.R;
import com.example.baniek.fridgemobile.RestService.RestController;

import java.util.ArrayList;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FridgeActivity extends AppCompatActivity {

    private DataBaseService dataBaseService;
    private RestController restController = RestController.getInstance();
    private ProductCollection products = ProductCollection.getInstance();
    private ProductAdapter productAdapter;
    private ListView listView;
    private Context context;
    private User user;

    @Override
    protected void onResume() {
        super.onResume();
        productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);

        dataBaseService = DataBaseService.getInstance(this);


        context = this;
        user = new User(getIntent().getStringExtra("login"), getIntent().getStringExtra("password"));

        listView = (ListView) findViewById(R.id.productListView);
        productAdapter = new ProductAdapter(this, products.getProducts());
        listView.setAdapter(productAdapter);

        OnItemClick();
        onItemLongClick();
        setAddButton();

        if(isOnline())
        {
            sync();
        }
        else
        {
            getDataFromDataBase();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.sync_button)
        {
            sync();
            Toast.makeText(FridgeActivity.this, "sync data", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sync() {
        if(isOnline())
        {
            ArrayList<Product> syncProducts = dataBaseService.GetProductsSync(user.getLogin());
            if(syncProducts == null) {
                getDataFromServer();
                return;
            }
            for (int i = 0; i < syncProducts.size(); i++) {
                Product product = syncProducts.get(i);

                Callback callback;
                if(i == syncProducts.size() - 1) {
                    callback = new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            getDataFromServer();
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    };
                }
                else
                {
                    callback = new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    };
                }
                restController.ChangeAmount(product.getId(), product.getValueLastModyfide(), callback, user.getLogin(), user.getPassword());
            }


        }
        else
        {
            Toast.makeText(FridgeActivity.this, "Some problem with internet", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void setAddButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCreateProduct();
            }
        });
    }

    private void OnItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("productId", position);
                intent.putExtra("login", user.getLogin());
                intent.putExtra("password", user.getPassword());
                startActivity(intent);
            }
        });
    }

    private void onItemLongClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete this product")
                        .setMessage("Are you sure?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int id = products.getProduct(position).getId();
                                restController.DeleteProducts(id , user.getLogin(), user.getPassword());
                                dataBaseService.DeleteProduct(id);
                                products.deleteProduct(position);
                                productAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    private void getDataFromServer() {

        Callback<ArrayList<Product>> callback = new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                dataBaseService.DeleteProducts(user.getLogin());
                ArrayList<Product> tmp = response.body();
                for (Product product: tmp) {
                    product.setSync(true);
                }
                dataBaseService.AddProducts(tmp);
                setData(tmp);
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                getDataFromDataBase();
                t.printStackTrace();
                Toast.makeText(FridgeActivity.this, "Some problem with internet - getDataFromServer", Toast.LENGTH_LONG).show();
            }
        };
        restController.GetAllProducts(callback, user.getLogin(), user.getPassword());
    }

    private void getDataFromDataBase() {
        setData(dataBaseService.GetProducts(user.getLogin()));
    }

    private void setData(ArrayList<Product> products)
    {
        this.products.clear();
        this.products.addProducts(products);
        productAdapter.notifyDataSetChanged();
    }

    private void showDialogCreateProduct()
    {
        final Dialog addDialog = new Dialog(this, R.style.AlertDialogCustom);
        addDialog.setContentView(R.layout.dialog_add_product);
        addDialog.setTitle("Add Product");
        Button btnLogin = (Button) addDialog.findViewById(R.id.btn_create);
        Button btnCancel = (Button) addDialog.findViewById(R.id.btn_cancel);
        final EditText txtUsername = (EditText)addDialog.findViewById(R.id.txt_name);
        final EditText txtPassword = (EditText)addDialog.findViewById(R.id.txt_price);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtUsername.getText().toString();
                float price = Float.parseFloat(txtPassword.getText().toString());

                final Product product = new Product(user.getLogin(), name, price, 0);

                if(isOnline()) {
                    Callback callback = new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Headers headers = response.headers();
                            String location = response.headers().get("Location");
                            product.setId(Integer.valueOf(location.substring(location.lastIndexOf("/") + 1, location.length())));

                            product.setSync(true);
                            dataBaseService.AddProduct(product);

                            dataBaseService.AddProduct(product);

                            products.addProduct(product);
                            productAdapter.notifyDataSetChanged();

                            addDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Toast.makeText(FridgeActivity.this, "Some problem with internet", Toast.LENGTH_LONG).show();
                        }
                    };

                    restController.AddProducts(product, callback, user.getLogin(), user.getPassword());
                }
                else
                {
                    product.setSync(false);
                    dataBaseService.AddProduct(product);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });
        addDialog.show();
    }
}
