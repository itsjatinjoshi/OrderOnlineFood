package com.example.orderonlinefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderonlinefood.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    Button btnSignUp;
    MaterialEditText etName, etPhoneNo, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPhoneNo = (MaterialEditText) findViewById(R.id.etPhoneNo);
        etPassword = (MaterialEditText) findViewById(R.id.etPassword);
        etName = (MaterialEditText) findViewById(R.id.etName);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(etPhoneNo.getText().toString()).exists()){
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "Phone Num Already Registered", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.dismiss();
                            User user = new User(etName.getText().toString(), etPassword.getText().toString());
                            table_user.child(etPhoneNo.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();
                            finish();
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
