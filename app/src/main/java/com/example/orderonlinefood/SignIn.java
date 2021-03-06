package com.example.orderonlinefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.orderonlinefood.Common.Common;
import com.example.orderonlinefood.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    Button btnSignIn;
    MaterialEditText etPhoneNo, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etPhoneNo = (MaterialEditText) findViewById(R.id.etPhoneNo);
        etPassword = (MaterialEditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //Init FIrebase

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(etPhoneNo.getText().toString()).exists()){


                        progressDialog.dismiss();
                        User user = dataSnapshot.child(etPhoneNo.getText().toString()).getValue(User.class);
                        user.setPhone(etPhoneNo.getText().toString());
                        if (user.getPassword().equals(etPassword.getText().toString())) {
                           Intent homeIntent = new Intent(SignIn.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Toast.makeText(SignIn.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                        }}
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(SignIn.this, "Invalid User", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
