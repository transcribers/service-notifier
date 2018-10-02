package com.example.iotsolutions.servicenotifier;

import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class user_sign_in extends AppCompatActivity implements View.OnClickListener {
    Button signin;
    Button signup;
    EditText email;
    EditText pwd;
    TextView forgot_password;
    FirebaseAuth mAuth;
    private static final String TAG = "user_sign_in";
    String sellerUid;
    String customerUid;
    ImageView addButton;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reference;
    Vibrator vibrator;

    @Override
    public void onBackPressed() {
        Vibrator vibrator;
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        Intent intent3=new Intent(this,seller_sign_in.class);
        startActivity(intent3);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.iotsolutions.servicenotifier.R.layout.activity_user_sign_in);

        Toolbar toolbar=(Toolbar)findViewById(com.example.iotsolutions.servicenotifier.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service Notifier");
        toolbar.setSubtitle("User Sign In");
        toolbar.setSubtitleTextColor(Color.WHITE);

        addButton = (ImageView) findViewById(R.id.addbtn);
        addButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Intent a = new Intent(user_sign_in.this, EditMessage.class);
                                             startActivity(a);
                                         }
        });

        forgot_password=(TextView) findViewById(com.example.iotsolutions.servicenotifier.R.id.forgot_password);
        signin = (Button) findViewById(com.example.iotsolutions.servicenotifier.R.id.signin);
        signup = (Button) findViewById(com.example.iotsolutions.servicenotifier.R.id.signup);
        email = (EditText) findViewById(com.example.iotsolutions.servicenotifier.R.id.email);
        pwd = (EditText) findViewById(com.example.iotsolutions.servicenotifier.R.id.pwd);
        signin.setOnClickListener(user_sign_in.this);
        forgot_password.setOnClickListener(user_sign_in.this);
        signup.setOnClickListener(user_sign_in.this);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        if(getIntent().getExtras()!= null) {
            sellerUid = getIntent().getExtras().getString("sellerUid");
        }
        setupFirebaseAuth();
    }

    @Override
    public void onClick(View view) {
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        switch (view.getId()) {
            case com.example.iotsolutions.servicenotifier.R.id.signin:
                final String Email = email.getText().toString();
                final String Pass = pwd.getText().toString();
                if (Email.length() == 0) {
                    email.requestFocus();
                    email.setError("Field Cannot Be Empty");
                } else if (!Email.matches("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")) {
                    email.requestFocus();
                    email.setError("Enter valid Email");
                } else if (Pass.length() == 0) {
                    pwd.requestFocus();
                    pwd.setError("Field Cannot Be Empty");
                } else {


                    mAuth.signInWithEmailAndPassword(email.getText().toString(), pwd.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "1" );
                                        // Sign in success, update UI with the signed-in user's information
                                        if(mAuth.getCurrentUser() != null) {
                                            Log.d(TAG, "2" );
                                            customerUid = mAuth.getCurrentUser().getUid();
                                        }
                                        Log.d(TAG, "sellerUid" + sellerUid);
                                        Log.d(TAG, "customerUid" + customerUid);
                                        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
                                        boolean emailVerified = users.isEmailVerified();
                                        if(emailVerified) {


                                            reference.child("customers").child(customerUid)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.hasChild("isCustomer")) {
                                                                Log.d(TAG, "3");
                                                                Intent intent = new Intent(user_sign_in.this, client_add_product.class);
                                                                //   intent.putExtra("sellerUid", sellerUid);
                                                                intent.putExtra("customerUid", customerUid);
                                                                startActivity(intent);
                                                                overridePendingTransition(com.example.iotsolutions.servicenotifier.R.anim.from_middle, com.example.iotsolutions.servicenotifier.R.anim.to_middle);
                                                            } else {
                                                                Toast.makeText(user_sign_in.this, "You cannot add products.",
                                                                        Toast.LENGTH_LONG).show();

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                        }
                                        else
                                        {

                                            Toast.makeText(user_sign_in.this, "Email not authorised",
                                                    Toast.LENGTH_LONG).show();
                                            mAuth.signOut();
                                            finish();
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        // Log.w(seller_sign_in.this, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(user_sign_in.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                     //   email.setText("");
                                        pwd.setText("");
                                        //  updateUI(null);
                                    }

                                    // ...
                                }
                            });


                    //      Intent intent=new Intent(this,client_add_product.class);
                    //      startActivity(intent);
                }
                break;
            case com.example.iotsolutions.servicenotifier.R.id.signup:
                Intent intent2 = new Intent(this, user_signup.class);
                intent2.putExtra("sellerUid", sellerUid);
                startActivity(intent2);
                overridePendingTransition(com.example.iotsolutions.servicenotifier.R.anim.from_middle, com.example.iotsolutions.servicenotifier.R.anim.to_middle);

                break;

            case com.example.iotsolutions.servicenotifier.R.id.forgot_password:


                Intent intent11 = new Intent(this, ResetPasswordActivity.class);
                startActivity(intent11);
                break;

        }
    }


    /*
           ----------------------------- Firebase setup ---------------------------------
        */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(user_sign_in.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(user_sign_in.this, client_add_product.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    //check for extras from FCM
//                    if (getIntent().getExtras() != null) {
//                        Log.d(TAG, "initFCM: found intent extras: " + getIntent().getExtras().toString());
//                        for (String key : getIntent().getExtras().keySet()) {
//                            Object value = getIntent().getExtras().get(key);
//                            Log.d(TAG, "initFCM: Key: " + key + " Value: " + value);
//                        }
//                        String data = getIntent().getStringExtra("data");
//                        Log.d(TAG, "initFCM: data: " + data);
//                    }
                    startActivity(intent);
                    finish();


                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
}