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

public class TourGuideRegisterActivity extends AppCompatActivity {
    private Button mTourGuideRegisterButton;
    private EditText mTourGuideRegisterEmail, mTourGuidePassword, mConfirmTourGuidePassword;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }

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
                                startActivity(new Intent(TourGuideRegisterActivity.this, TourGuideMapsActivity.class));
                            } else {
                                startActivity(new Intent(TourGuideRegisterActivity.this, TouristMapsActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
        }

        setContentView(R.layout.activity_tour_guide_register);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
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
                                        startActivity(new Intent(TourGuideRegisterActivity.this, TourGuideMapsActivity.class));
                                    } else {
                                        startActivity(new Intent(TourGuideRegisterActivity.this, TouristMapsActivity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) { }
                            });

                }
            }
        };

        mTourGuideRegisterEmail = findViewById(R.id.tourGuideRegisterEmail);
        mTourGuidePassword = findViewById(R.id.passwordTGRegister);
        mConfirmTourGuidePassword = findViewById(R.id.confirmTGPassword);
        mTourGuideRegisterButton = findViewById(R.id.tour_guide_register_button);
        mProgressBar = findViewById(R.id.tour_guide_register_progressbar);

        mProgressBar.setVisibility(View.GONE);

        mTourGuideRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mTourGuideRegisterEmail.getText().toString();
                final String password = mConfirmTourGuidePassword.getText().toString();
                final String cpassword = mConfirmTourGuidePassword.getText().toString();

                if (email.isEmpty()) {
                    mTourGuideRegisterEmail.setError("Email is Empty");
                    mTourGuideRegisterEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mTourGuideRegisterEmail.setError("Invalid email address");
                    mTourGuideRegisterEmail.requestFocus();
                    return;
                }

                if (password.length() < 8) {
                    mConfirmTourGuidePassword.setError("Min 8 characters");
                    mConfirmTourGuidePassword.requestFocus();
                    return;
                }
                if(!password.equals(cpassword)){
                    mConfirmTourGuidePassword.setError("Passwords Don't Match");
                    mConfirmTourGuidePassword.requestFocus();
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(TourGuideRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    mProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                }else{
                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference user_db = FirebaseDatabase
                                            .getInstance()
                                            .getReference()
                                            .child("Users")
                                            .child("Guides")
                                            .child(user_id);
                                    user_db.setValue(true);
                                    mProgressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(getApplicationContext(), TourGuideMapsActivity.class));
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