package com.example.orderonlinefood.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderonlinefood.Interface.OnClickListener;
import com.example.orderonlinefood.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView menu_name;
    public ImageView menu_image;
    private OnClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        menu_image = itemView.findViewById(R.id.menu_image);
        menu_name = itemView.findViewById(R.id.menu_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
