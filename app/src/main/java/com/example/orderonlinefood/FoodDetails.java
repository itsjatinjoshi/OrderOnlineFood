package com.example.orderonlinefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.orderonlinefood.Model.Food;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetails extends AppCompatActivity {
    public final String LOG_TAG = FoodDetails.class.getSimpleName();

    TextView food_price, food_name, food_description;
    ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";
    FirebaseDatabase database;
    DatabaseReference foods;

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
        numberButton = findViewById(R.id.numberButton);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);

        if(getIntent() != null){
            foodId = getIntent().getStringExtra("FoodId");
            Log.d(LOG_TAG, "Food ID : " + foodId);
        }
        if(!foodId.isEmpty()){
            getFoodDetails(foodId);
        }
    }

    private void getFoodDetails(final String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Food food = dataSnapshot.getValue(Food.class);
                Log.d(LOG_TAG, "Food Name : " + food.getName());
                Log.d(LOG_TAG, "Food Pic : " + food.getImage());
                Log.d(LOG_TAG, "Food Desc : " + food.getDescription());


                Picasso.get().load(food.getImage()).into(img_food);
                collapsingToolbarLayout.setTitle(food.getName());
                food_price.setText(food.getPrice());
                food_name.setText(food.getName());
                food_description.setText(food.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
