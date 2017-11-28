package com.example.baniek.fridgemobile.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.baniek.fridgemobile.Model.Product;
import com.example.baniek.fridgemobile.Model.User;

import java.util.ArrayList;
import java.util.UUID;

public class DataBaseService extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fridge.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "User";
    private static final String USER_LOGIN = "Login";
    private static final String USER_PASSWORD = "Password";

    private static final String TABLE_PRODUCT = "Product";
    private static final String PRODUCT_ID = "Id";
    private static final String PRODUCT_USER_LOGIN = "UserLogin";
    private static final String PRODUCT_NAME = "Name";
    private static final String PRODUCT_PRICE = "Price";
    private static final String PRODUCT_AMOUNT = "Amount";
    private static final String PRODUCT_VALUE_LAST_MODYFIED = "ValueLastModyfied";
    private static final String PRODUCT_ISSYNC = "IsSync";

    private static final String TABLE_DEVICE = "Device";
    private static final String DEVICE_GUID = "Guid";

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
                + USER_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + PRODUCT_ID + " INTEGER PRIMARY KEY,"
                + PRODUCT_USER_LOGIN + " TEXT,"
                + PRODUCT_NAME + " TEXT,"
                + PRODUCT_PRICE + " REAL,"
                + PRODUCT_AMOUNT + " INTEGER,"
                + PRODUCT_VALUE_LAST_MODYFIED + " INTEGER,"
                + PRODUCT_ISSYNC + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_DEVICE + "("
                + DEVICE_GUID + " TEXT PRIMARY KEY"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE);
        onCreate(db);
    }

    public void deleteDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1, 1);
    }

    //GUID
    public boolean AddGUID()
    {
        if(GetGUID() != null)
            return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEVICE_GUID,  UUID.randomUUID().toString());
        long result = db.insert(TABLE_DEVICE, null, contentValues);
        return result != -1;
    }

    public String GetGUID()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String guid = null;
        String query = "select * from " + TABLE_DEVICE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() != 1)
            return guid;
        try {
            for(int i=0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                guid = cursor.getString(0);
            }
        } finally {
            cursor.close();
        }
        return guid;
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
    public boolean AddProduct(Product product)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_ID, product.getId());
        contentValues.put(PRODUCT_USER_LOGIN, product.getUserLogin());
        contentValues.put(PRODUCT_NAME, product.getName());
        contentValues.put(PRODUCT_PRICE, product.getPrice());
        contentValues.put(PRODUCT_AMOUNT, product.getAmount());
        contentValues.put(PRODUCT_VALUE_LAST_MODYFIED, product.getValueLastModyfide());
        contentValues.put(PRODUCT_ISSYNC, product.isSync());
        long result = db.insert(TABLE_PRODUCT, null, contentValues);
        return result != -1;
    }

    public void AddProducts(ArrayList<Product> products)
    {
        for (Product product: products) {
            AddProduct(product);
        }
    }

    public Product GetProduct(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Product product = new Product();
        String query = "select * from " + TABLE_PRODUCT + " where " + PRODUCT_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            return null;
        try {
            cursor.moveToNext();
            product.setId(cursor.getInt(0));
            product.setUserLogin(cursor.getString(1));
            product.setName(cursor.getString(2));
            product.setPrice(cursor.getFloat(3));
            product.setAmount(cursor.getInt(4));
            product.setValueLastModyfide(cursor.getInt(5));
            if (cursor.getString(6).equals("1"))
                product.setSync(true);
            else
                product.setSync(false);
        } finally {
            cursor.close();
        }
        return product;
    }

    public ArrayList<Product> GetProducts(String login)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Product> productArrayList = new ArrayList<>();
        String query = "select * from " + TABLE_PRODUCT + " where " + PRODUCT_USER_LOGIN + " = '" + login + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            return null;
        try {
            for(int i=0; i < cursor.getCount(); i++) {
                Product product = new Product();
                cursor.moveToNext();
                product.setId(cursor.getInt(0));
                product.setUserLogin(cursor.getString(1));
                product.setName(cursor.getString(2));
                product.setPrice(cursor.getFloat(3));
                product.setAmount(cursor.getInt(4));
                product.setValueLastModyfide(cursor.getInt(5));
                if (cursor.getString(6).equals("1"))
                    product.setSync(true);
                else
                    product.setSync(false);
                productArrayList.add(product);
            }
        } finally {
            cursor.close();
        }
        return productArrayList;
    }



    public boolean UpdateProduct(Product product)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_ID, product.getId());
        contentValues.put(PRODUCT_USER_LOGIN, product.getUserLogin());
        contentValues.put(PRODUCT_NAME, product.getName());
        contentValues.put(PRODUCT_PRICE, product.getPrice());
        contentValues.put(PRODUCT_AMOUNT, product.getAmount());
        contentValues.put(PRODUCT_VALUE_LAST_MODYFIED, product.getValueLastModyfide());
        contentValues.put(PRODUCT_ISSYNC, product.isSync());
        long result = db.update(TABLE_PRODUCT, contentValues, "Id = ?", new String[]{Integer.toString(product.getId())});
        return result != -1;
    }

    public boolean DeleteProduct(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_PRODUCT, "Id = ?", new String[]{Integer.toString(id)});
        return result != -1;
    }

    public boolean DeleteProducts(String login)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_PRODUCT, "UserLogin = ?", new String[]{login});
        return result != -1;
    }

    public ArrayList<Product> GetProductsSync(String login)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Product> productArrayList = new ArrayList<>();
        String query = "select * from " + TABLE_PRODUCT + " where " + PRODUCT_USER_LOGIN + " = '" + login + "' AND " + PRODUCT_ISSYNC + " = 0";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            return null;
        try {
            for(int i=0; i < cursor.getCount(); i++) {
                Product product = new Product();
                cursor.moveToNext();
                product.setId(cursor.getInt(0));
                product.setUserLogin(cursor.getString(1));
                product.setName(cursor.getString(2));
                product.setPrice(cursor.getFloat(3));
                product.setAmount(cursor.getInt(4));
                product.setValueLastModyfide(cursor.getInt(5));
                if (cursor.getString(6).equals("1"))
                    product.setSync(true);
                else
                    product.setSync(false);
                productArrayList.add(product);
            }
        } finally {
            cursor.close();
        }
        return productArrayList;
    }

    public boolean DeleteProductsSync(String login)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_PRODUCT, "UserLogin = ? AND IsSync = ?", new String[]{login, "0"});
        return result != -1;
    }
}
