package com.example.orderonlinefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.orderonlinefood.Interface.OnClickListener;
import com.example.orderonlinefood.Model.Food;
import com.example.orderonlinefood.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodList;
    String categoryId = "";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestionList = new ArrayList<>();
    MaterialSearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        recyclerView = findViewById(R.id.recycler_list);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("Category");
        }

        if (!categoryId.isEmpty() && categoryId != null) {
            loadFoodList(categoryId);

        }
        searchBar = findViewById(R.id.searchBar);
        searchBar.setHint("Find your favorite food");
        searchSuggestion();
        searchBar.setLastSuggestions(suggestionList);
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                List<String> suggest = new ArrayList<String>();
                for(String search:suggestionList){
                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
           } ) ;

        searchBar.setOnSearchActionListener(new SimpleOnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
               startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                super.onButtonClicked(buttonCode);
            }
        });

    }

    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.food_name.setText(food.getName());
                //   Picasso.get().load(food.getImage()).into(foodViewHolder.food_image);
                Picasso.get().load(food.getImage()).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(foodViewHolder.food_image);

                final Food foodName = food;
                foodViewHolder.setItemClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongPress) {
                        Intent foodList = new Intent(FoodList.this, FoodDetails.class);
                        foodList.putExtra("FoodId", searchAdapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void searchSuggestion() {
        foodList.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Food item = postSnapshot.getValue(Food.class);
                   suggestionList.add(item.getName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadFoodList(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.food_name.setText(food.getName());
                //   Picasso.get().load(food.getImage()).into(foodViewHolder.food_image);
                Picasso.get().load(food.getImage()).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(foodViewHolder.food_image);

                final Food foodName = food;
                foodViewHolder.setItemClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongPress) {
                        Intent foodList = new Intent(FoodList.this, FoodDetails.class);
                        foodList.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);
    }
}
