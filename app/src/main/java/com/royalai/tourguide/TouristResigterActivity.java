package com.royalai.tourguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TouristResigterActivity extends AppCompatActivity {
    private Button mTouristRegisterButton;
    private EditText mTouristRegisterEmail, mTouristPassword, mConfirmTouristPassword;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
//                    startActivity(new Intent(getApplicationContext(), TourGuideMapsActivity.class));
//                    finish();
//                    return;

            String user_id = mAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("Users")
                    .child("Guides")
                    .child(user_id) // Create a reference to the child node directly
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This callback will fire even if the node doesn't exist, so now check for existence
                            if (dataSnapshot.exists()) {
                                startActivity(new Intent(TouristResigterActivity.this, TourGuideMapsActivity.class));
                            } else {
                                startActivity(new Intent(TouristResigterActivity.this, TouristMapsActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });

        }

        setContentView(R.layout.activity_tourist_resigter);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    startActivity(new Intent(getApplicationContext(), TouristMapsActivity.class));
                    finish();
                    return;
                }
            }
        };

        mTouristRegisterEmail = findViewById(R.id.touristRegisterEmail);
        mTouristPassword = findViewById(R.id.passwordTRegister);
        mConfirmTouristPassword = findViewById(R.id.confirmTPassword);
        mTouristRegisterButton = findViewById(R.id.tourist_register_button);
        mProgressBar = findViewById(R.id.tprogressbar);

        mProgressBar.setVisibility(View.GONE);

        mTouristRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mTouristRegisterEmail.getText().toString();
                final String password = mTouristPassword.getText().toString();
                final String cpassword = mConfirmTouristPassword.getText().toString();

                if (email.isEmpty()) {
                    mTouristRegisterEmail.setError("Email is Empty");
                    mTouristRegisterEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mTouristRegisterEmail.setError("Invalid email address");
                    mTouristRegisterEmail.requestFocus();
                    return;
                }

                if (password.length() < 8) {
                    mTouristPassword.setError("Min 8 characters");
                    mTouristPassword.requestFocus();
                    return;
                }
                if(!password.equals(cpassword)){
                    mConfirmTouristPassword.setError("Passwords Don't Match");
                    mConfirmTouristPassword.requestFocus();
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(TouristResigterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    mProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Sign Up Error", Toast.LENGTH_SHORT).show();
                                }else{
                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference user_db = FirebaseDatabase
                                            .getInstance()
                                            .getReference()
                                            .child("Users")
                                            .child("Tourists")
                                            .child(user_id);
                                    user_db.setValue(true);
                                    mProgressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(getApplicationContext(), TouristMapsActivity.class));
                                    finish();
                                    return;
                                }
                            }
                        });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}