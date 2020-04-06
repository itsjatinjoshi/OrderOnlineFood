package com.example.orderonlinefood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.orderonlinefood.Common.Common;
import com.example.orderonlinefood.Model.Request;
import com.example.orderonlinefood.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> firebaseAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Requests");
        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {

        firebaseAdapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                reference.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int i) {
                orderViewHolder.tvOrderId.setText(firebaseAdapter.getRef(i).getKey());
                orderViewHolder.tvOrderStatus.setText(convertCodeToStatus(request.getStatus()));
                orderViewHolder.tvOrderPhone.setText(request.getPhone());
                orderViewHolder.tvOrderAddress.setText(request.getAddress());

            }
        };

        recyclerView.setAdapter(firebaseAdapter);
    }

    private String convertCodeToStatus(String status) {
        if (status.equals("0")) {
            return "placed";
        } else if (status.equals("1")) {
            return "On My way";
        } else {
            return "Shipped";
        }
    }
}
