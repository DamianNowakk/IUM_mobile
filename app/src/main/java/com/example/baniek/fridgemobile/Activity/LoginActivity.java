package com.example.baniek.fridgemobile.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baniek.fridgemobile.DataAccess.DataBaseService;
import com.example.baniek.fridgemobile.Model.User;
import com.example.baniek.fridgemobile.R;
import com.example.baniek.fridgemobile.RestService.RestController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private DataBaseService dataBaseService;
    private RestController restController = RestController.getInstance();
    private Button loginButton;
    private Button createButton;
    private EditText loginEditText;
    private EditText passwordEditText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBaseService = DataBaseService.getInstance(this);
        setContentView(R.layout.activity_login);

        dataBaseService.AddGUID();
        String guid = dataBaseService.GetGUID();
        restController.setGuid(guid);

        loginButton = (Button) findViewById(R.id.LoginButton);
        createButton = (Button) findViewById(R.id.CreateButton);
        loginEditText = (EditText) findViewById(R.id.LoginText);
        passwordEditText = (EditText) findViewById(R.id.PasswordText);

        onClickLogin();
        onClickRegister();

        SharedPreferences shared = getSharedPreferences("data",MODE_PRIVATE);
        String login = shared.getString("login", null);
        String password = shared.getString("password", null);

        if(login != null && password != null)
        {
            User user = dataBaseService.GetUser(login);
            if(user != null && user.getPassword().equals(password))
                login(user);
        }

    }

    public void onClickLogin()
    {
        loginButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        hideKeyboard();
                        checkdata();
                    }
                }
        );
    }

    public void onClickRegister()
    {
        createButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        hideKeyboard();
                        showDialogRegister();
                    }
                }
        );
    }

    private void showDialogRegister()
    {
        final Dialog login = new Dialog(this, R.style.AlertDialogCustom);
        login.setContentView(R.layout.dialog_register);
        login.setTitle("Register");
        Button btnLogin = (Button) login.findViewById(R.id.btn_create);
        Button btnCancel = (Button) login.findViewById(R.id.btn_cancel);
        final EditText txtUsername = (EditText)login.findViewById(R.id.txt_name);
        final EditText txtPassword = (EditText)login.findViewById(R.id.txt_password);
        final EditText txtPasswordRepeat = (EditText)login.findViewById(R.id.txt_passwordRepeat);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String repeatPassword = txtPasswordRepeat.getText().toString();

                Validation(username, password, repeatPassword);
                AddUser(username, password);

            }

            private void AddUser(String username, String password) {
                User user = new User(username, password);


                Callback<User> callback = new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(LoginActivity.this, "Added user", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Some problem with internet", Toast.LENGTH_LONG).show();
                    }
                };
                restController.AddUser(callback, user);

                login.dismiss();
            }

            private boolean Validation(String username, String password, String repeatPassword)
            {
                if(username.equals("")){
                    Toast.makeText(LoginActivity.this, "Set login",Toast.LENGTH_LONG).show();
                    return false;
                }
                if(password.equals("")){
                    Toast.makeText(LoginActivity.this, "Set password",Toast.LENGTH_LONG).show();
                    return false;
                }
                if(repeatPassword.equals("")){
                    Toast.makeText(LoginActivity.this, "Repeat password",Toast.LENGTH_LONG).show();
                    return false;
                }
                if(!repeatPassword.equals(password)){
                    Toast.makeText(LoginActivity.this, "The repeat password is incorrect",Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.dismiss();
            }
        });
        login.show();
    }

    private void checkdata()
    {
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(login.equals("")){
            Toast.makeText(LoginActivity.this, "Set login",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.equals("")){
            Toast.makeText(LoginActivity.this, "Set password",Toast.LENGTH_LONG).show();
            return;
        }

        Callback<User> callback = new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(user == null) {
                    Toast.makeText(LoginActivity.this, "User not exist", Toast.LENGTH_LONG).show();
                    return;
                }

                addOrUpdateUserDB(user);
                login(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Some problem with internet", Toast.LENGTH_LONG).show();
            }
        };

        restController.GetUser(callback, login, password);
    }

    private void login(User user) // TO DO
    {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("login",user.getLogin());
        editor.putString("password",user.getPassword());
        editor.apply();

        Intent intent = new Intent(this, FridgeActivity.class);
        intent.putExtra("login", user.getLogin());
        intent.putExtra("password", user.getPassword());
        startActivity(intent);
        finish();
    }

    private void addOrUpdateUserDB(User user)
    {
        User tmpUser = dataBaseService.GetUser(user.getLogin());
        if (tmpUser == null || tmpUser.getPassword() != user.getPassword())
        {
            dataBaseService.AddUser(user);
    }
    }

    private void hideKeyboard()
    {
        if(getCurrentFocus() != null)
        {
            IBinder ibinder = getCurrentFocus().getWindowToken();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ibinder, 0);
        }
    }
}
