package com.example.baniek.fridgemobile.MainList;

import com.example.baniek.fridgemobile.Model.Product;

import java.util.ArrayList;

public class ProductCollection {
    private static final ProductCollection ourInstance = new ProductCollection();

    public static ProductCollection getInstance() {
        return ourInstance;
    }

    private ArrayList<Product> products;

    private ProductCollection() {
        products = new ArrayList<>();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public Product getProduct(int index) {
        return products.get(index);
    }

    public void deleteProduct(Product product) {
        products.remove(product);
    }

    public void deleteProduct(int id) {
        products.remove(id);
    }

    public void updateProduct(int id, Product newProduct) {
        Product product = products.get(id);
        product.setId(newProduct.getId());
        product.setAmount(newProduct.getAmount());
        product.setName(newProduct.getName());
        product.setUserLogin(newProduct.getUserLogin());
        product.setPrice(newProduct.getPrice());
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addProducts(ArrayList<Product> products) {
        for(Product product : products)
        {
            addProduct(product);
        }
    }

    public void clear()
    {
        products.clear();
    }
}
