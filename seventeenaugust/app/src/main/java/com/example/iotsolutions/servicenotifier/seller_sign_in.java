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
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class seller_sign_in extends AppCompatActivity implements View.OnClickListener {

    Button signup;
    Button signin;
    EditText email;
    EditText pwd;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reference;
    private static final String TAG = "seller_sign_in";
    String sellerUid;
    Vibrator vibrator;
    @Override
    public void onBackPressed() {
        Vibrator vibrator;
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        Intent intent3=new Intent(this,select_admin_or_user.class);
        startActivity(intent3);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_sign_in);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service Notifier");
        toolbar.setSubtitle("Seller Sign IN");
        toolbar.setSubtitleTextColor(Color.WHITE);


        signup = (Button) findViewById(R.id.signup);
        signin = (Button) findViewById(R.id.signin);
        signup.setOnClickListener(seller_sign_in.this);
        signin.setOnClickListener(seller_sign_in.this);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

//        if (getIntent().getExtras() != null) {
//            sellerUid = getIntent().getExtras().getString("sellerUid");
//        }
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    email.setHint("");
                } else {
                    email.setHint("");
                }
            }
        });

        pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    pwd.setHint("");
                } else {
                    pwd.setHint("");
                }
            }
        });

        setupFirebaseAuth();

    }


    @Override
    public void onClick(View view) {
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        switch (view.getId()) {
            case R.id.signup:

                Intent intent1 = new Intent(this, seller_sign_up.class);
                startActivity(intent1);
                break;
            case R.id.signin:
                final String Email = email.getText().toString();
                final String Pass = pwd.getText().toString();
                if (Email.length() == 0) {
                    email.requestFocus();
                    email.setError("Field Cannot Be Empty");
                } else if (!Email.matches("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                        "\\@" +

                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                        "(" +

                        "\\." +

                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                        ")+")) {
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
                                        Log.d(TAG, "100000000");
                                        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
                                        boolean emailVerified = users.isEmailVerified();
                                        if (emailVerified) {

                                            reference.child("sellers").child(mAuth.getCurrentUser().getUid())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.hasChild("isSeller")  ) {

                                                                Intent intent = new Intent(seller_sign_in.this, user_sign_in.class);
                                                                if (mAuth.getCurrentUser() != null) {
                                                                    sellerUid = mAuth.getCurrentUser().getUid();
                                                                }
                                                                intent.putExtra("sellerUid", sellerUid);
                                                                startActivity(intent);
                                                                overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
                                                            } else {
                                                                Toast.makeText(seller_sign_in.this, "You cannot add products.",
                                                                        Toast.LENGTH_LONG).show();

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                            // Sign in success, update UI with the signed-in user's information


                                        }
                                        else
                                        {

                                            Toast.makeText(seller_sign_in.this, "Email not authorised",
                                                    Toast.LENGTH_LONG).show();
                                            mAuth.signOut();
                                            finish();
                                        }
                                    }
                                    else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(seller_sign_in.this, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(seller_sign_in.this, "Authentication failed.",
                                                Toast.LENGTH_LONG).show();
                                        //   email.setText("");
                                        pwd.setText("");
                                        //  updateUI(null);
                                    }

                                    // ...
                                }
                            });


                }
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
                FirebaseUser seller = firebaseAuth.getCurrentUser();
                if (seller != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + seller.getUid());
                    Toast.makeText(seller_sign_in.this, "Authenticated with: " + seller.getEmail(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(seller_sign_in.this, user_sign_in.class);
                    intent.putExtra("sellerUid", sellerUid);
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
                    // seller is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
}