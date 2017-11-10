package com.example.baniek.fridgemobile.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.baniek.fridgemobile.Model.Product;
import com.example.baniek.fridgemobile.Model.User;

import java.util.ArrayList;

public class DataBaseService extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fridge.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "User";
    private static final String USER_LOGIN = "Login";
    private static final String USER_PASSWORD = "Password";

    private static final String TABLE_PRODUCT = "Product";
    private static final String TABLE_PRODUCT_TMP = "ProductTmp";
    private static final String PRODUCT_ID = "Id";
    private static final String PRODUCT_USER_LOGIN = "UserLogin";
    private static final String PRODUCT_NAME = "Name";
    private static final String PRODUCT_PRICE = "Price";
    private static final String PRODUCT_AMOUNT = "Amount";

    private static DataBaseService sInstance;

    public static synchronized DataBaseService getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseService(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataBaseService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + USER_LOGIN + " TEXT PRIMARY KEY,"
                + USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PRODUCT_USER_LOGIN + " TEXT,"
                + PRODUCT_NAME + " TEXT,"
                + PRODUCT_PRICE + " REAL,"
                + PRODUCT_AMOUNT + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_PRODUCT_TMP + "("
                + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PRODUCT_USER_LOGIN + " TEXT,"
                + PRODUCT_NAME + " TEXT,"
                + PRODUCT_PRICE + " REAL,"
                + PRODUCT_AMOUNT + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_TMP);
        onCreate(db);
    }

    public void deleteDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1, 1);
    }

    // USER
    public boolean AddUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_LOGIN, user.getLogin());
        contentValues.put(USER_PASSWORD, user.getPassword());
        long result = db.insert(TABLE_USER, null, contentValues);
        return result != -1;
    }

    public User GetUser(String login)
    {
        User user = new User();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_USER + " where " + USER_LOGIN + " = '" + login + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            return null;
        try {
            cursor.moveToNext();
            user.setLogin(cursor.getString(0)) ;
            user.setPassword(cursor.getString(1));
        } finally {
            cursor.close();
        }

        return user;
    }

    //PRODUCT
    public void AddProduct(Product product)
    {

    }

    public Product GetProduct(Integer id)
    {
        return null;
    }

    public Product GetProducts(String login)
    {

        return null;
    }

    public void UpdateProduct(Product product)
    {

    }

    public void DeleteProduct(Product product)
    {

    }
}
