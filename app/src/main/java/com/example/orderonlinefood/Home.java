package com.example.orderonlinefood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderonlinefood.Common.Common;
import com.example.orderonlinefood.Interface.OnClickListener;
import com.example.orderonlinefood.Model.Category;
import com.example.orderonlinefood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;
    TextView tvUserName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    public int OpenDraw, closeDraw;
    FloatingActionButton fab;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        try {
            OpenDraw = Integer.parseInt("Open Navigation drawer");
            closeDraw = Integer.parseInt("Close navigation drawer");
        } catch (Exception e) {
            e.getMessage();
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, OpenDraw, closeDraw);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        tvUserName = (TextView) headView.findViewById(R.id.tvUserName);
        tvUserName.setText(Common.currentUser.getName());

        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();
    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {

                menuViewHolder.menu_name.setText(category.getName());
                //  Picasso.get().load(category.getImage()).into(menuViewHolder.menu_image);
                Picasso.get().load(category.getImage()).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(menuViewHolder.menu_image);
                final Category clickItem = category;
                menuViewHolder.setItemClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongPress) {
                        Intent foodList = new Intent(Home.this, FoodList.class);
                        foodList.putExtra("Category", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });

            }

        };
        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.isDrawerOpen(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d("HOME_LOG", "NAVIGATION MENU");
        int id = item.getItemId();
        if (id == R.id.nav_home) {

            //todo: implement later
        } else if (id == R.id.nav_cart) {
            System.out.println("ID for Cart : " + id);
            Intent viewCart = new Intent(Home.this, Cart.class);
            startActivity(viewCart);
            Log.d("HOME_LOG", "HOME CART");

        } else if (id == R.id.nav_order) {
            System.out.println("ID for Order : " + id);
            Intent orderStatus = new Intent(Home.this, OrderStatus.class);
            startActivity(orderStatus);
            Log.d("HOME_LOG", "HOME ORDER");

        } else if (id == R.id.nav_logout) {
            System.out.println("ID for Logout : " + id);
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
            alertDialog.setTitle("LOGOUT !!!");
            alertDialog.setCancelable(true);
            alertDialog.setMessage("Are You Sure want to Logout");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent signInIntent = new Intent(Home.this, SignIn.class);
                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(signInIntent);
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
            Log.d("HOME_LOG", "HOME LOGOUT");
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
