package com.example.orderonlinefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.orderonlinefood.Databases.Database;
import com.example.orderonlinefood.Model.Food;
import com.example.orderonlinefood.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class FoodDetails extends AppCompatActivity {
    public final String LOG_TAG = FoodDetails.class.getSimpleName();

    TextView food_price, food_name, food_description;
    ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference foods;
    Food currentFood;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_description = findViewById(R.id.food_description);
        img_food = findViewById(R.id.img_food);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(FoodDetails.this, "Added To Cart", Toast.LENGTH_SHORT).show();

            }
        });


        numberButton = findViewById(R.id.numberButton);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);

        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");
            Log.d(LOG_TAG, "Food ID : " + foodId);
        }
        if (!foodId.isEmpty()) {
            getFoodDetails(foodId);
        }
    }

    private void getFoodDetails(final String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

              //  Picasso.get().load(currentFood.getImage()).into(img_food);
                Picasso.get().load(currentFood.getImage()).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(img_food);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
