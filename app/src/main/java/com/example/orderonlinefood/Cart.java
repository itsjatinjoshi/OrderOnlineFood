package com.example.orderonlinefood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderonlinefood.Common.Common;
import com.example.orderonlinefood.Databases.Database;
import com.example.orderonlinefood.Model.Order;
import com.example.orderonlinefood.Model.Request;
import com.example.orderonlinefood.ViewHolder.CardAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView total;
    Button btnPlaceOrder;

    List<Order> cart = new ArrayList<>();
    CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        total = findViewById(R.id.total);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }

        });

        recyclerView = findViewById(R.id.listCart);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadListFood();
    }

    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One More Step! ");
        alertDialog.setMessage("Enter your address: ");

        final EditText etAddress = new EditText(Cart.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        etAddress.setLayoutParams(lp);
        alertDialog.setView(etAddress);
        alertDialog.setIcon(R.drawable.cart);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (etAddress.getText().toString().isEmpty()) {
                    Toast.makeText(Cart.this, "Please enter address", Toast.LENGTH_SHORT).show();
                } else {
                    Request request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            etAddress.getText().toString(),
                            total.getText().toString(),
                            cart
                    );
                    //  use system.current time as a Key
                    requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(Cart.this, "Thank You, Order Place", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }

    private void loadListFood() {

        cart = new Database(this).getCart();
        cardAdapter = new CardAdapter(cart, this);
        recyclerView.setAdapter(cardAdapter);

        int totalPrice = 0;
        for (Order order : cart)
            totalPrice += (Integer.parseInt(order.getPrice())) + (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        total.setText(fmt.format(totalPrice));
    }


}
