package com.example.orderonlinefood.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderonlinefood.Interface.OnClickListener;
import com.example.orderonlinefood.R;

public class OrderViewHolder  extends RecyclerView.ViewHolder implements  View.OnClickListener {

    public TextView tvOrderId, tvOrderStatus, tvOrderPhone, tvOrderAddress;
    private OnClickListener onItemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        tvOrderId = itemView.findViewById(R.id.tvOrderID);
        tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
        tvOrderPhone = itemView.findViewById(R.id.tvOrderPhone);
        tvOrderAddress = itemView.findViewById(R.id.tvOrderAddress);

        itemView.setOnClickListener(this);

    }

    public void setOnItemClickListener(OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View view) {
            onItemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
