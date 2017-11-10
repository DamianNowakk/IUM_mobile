package com.example.baniek.fridgemobile.MainList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.baniek.fridgemobile.Model.Product;
import com.example.baniek.fridgemobile.R;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.products_list, null);
        }

        TextView productNameTextView = (TextView) convertView.findViewById(R.id.productName);
        TextView productPriceTextView = (TextView) convertView.findViewById(R.id.productPrice);
        TextView productAmountTextView = (TextView) convertView.findViewById(R.id.productAmount);

        productNameTextView.setText(products.get(position).getName());
        productPriceTextView.setText(Float.toString(products.get(position).getPrice()));
        productAmountTextView.setText(products.get(position).getAmount().toString());

        return convertView;
    }
}
