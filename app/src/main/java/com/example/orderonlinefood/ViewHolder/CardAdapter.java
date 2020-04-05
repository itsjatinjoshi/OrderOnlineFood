package com.example.orderonlinefood.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.orderonlinefood.Interface.OnClickListener;
import com.example.orderonlinefood.Model.Order;
import com.example.orderonlinefood.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView cart_item_name, cart_item_Price;
    ImageView iv_cart_item_count;

    private OnClickListener onClickListener;


    public void setCart_item_name(TextView cart_item_name) {
        this.cart_item_name = cart_item_name;
    }

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);

        cart_item_name = itemView.findViewById(R.id.cart_item_name);
        cart_item_Price = itemView.findViewById(R.id.cart_item_Price);
        iv_cart_item_count = itemView.findViewById(R.id.iv_cart_item_count);
    }

    @Override
    public void onClick(View view) {

    }
}

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private List<Order> orders = new ArrayList<>();
    private Context context;

    public CardAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.cart_layout, parent, false);

        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        TextDrawable drawable = TextDrawable.builder().buildRound(""+ orders.get(position).getQuantity(), Color.RED);
        holder.iv_cart_item_count.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        int price = (Integer.parseInt(orders.get(position).getPrice()))* (Integer.parseInt(orders.get(position).getQuantity()));
        holder.cart_item_name.setText(orders.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
