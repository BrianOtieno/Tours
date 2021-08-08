package com.royalai.tourguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    private EditText mEmail, mPassword, mPasswordresetemail;
    private Button mLogin;
    private TextView mPasswordreset;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseAuth auth;
    private ProgressDialog processDialog;

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
                                startActivity(new Intent(LoginActivity.this, TourGuideMapsActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, TouristMapsActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });

        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
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
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, TourGuideMapsActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, TouristMapsActivity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) { }
                            });
                }
            }
        };

        mEmail = findViewById(R.id.emailSignIn);
        mPassword = findViewById(R.id.password);
        mProgressBar = findViewById(R.id.progressbars);
        mLogin = findViewById(R.id.Login);

        mPasswordreset = findViewById(R.id.forgotpassword);
        mPasswordresetemail = findViewById(R.id.emailSignIn);
        mProgressBar.setVisibility(View.GONE);
        processDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();



        mLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            validate(mEmail.getText().toString(), mPassword.getText().toString());
        }

    });

        mPasswordreset.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetpasword();
        }
    });
}

    public void resetpasword(){
        final String resetemail = mPasswordresetemail.getText().toString();

        if (resetemail.isEmpty()) {
            mPasswordresetemail.setError("It's empty");
            mPasswordresetemail.requestFocus();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(resetemail)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void validate(String userEmail, String userPassword){
        processDialog.setMessage("................Please Wait.............");
        processDialog.show();

        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    processDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, TourGuideMapsActivity.class));

                    // check if guide or tourist and redirect to respective view.
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
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, TourGuideMapsActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, TouristMapsActivity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) { }
                            });
                }
                else{
                    Toast.makeText(LoginActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
                    processDialog.dismiss();
                }
            }
        });
    }



}