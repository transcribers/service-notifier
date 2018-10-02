package com.example.iotsolutions.servicenotifier;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class user_user_sign_in extends AppCompatActivity implements View.OnClickListener {
    Button signin;
    TextView forgot_password;
    EditText email;
    EditText pwd;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "user_user_sign_in";
    Vibrator vibrator;
    String customerUid;

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
        setContentView(com.example.iotsolutions.servicenotifier.R.layout.activity_user_user_sign_in);

        Toolbar toolbar=(Toolbar)findViewById(com.example.iotsolutions.servicenotifier.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service Notifier");
        toolbar.setSubtitle("User Sign In");
        toolbar.setSubtitleTextColor(Color.WHITE);

        signin = (Button) findViewById(com.example.iotsolutions.servicenotifier.R.id.signin);
        forgot_password=(TextView) findViewById(com.example.iotsolutions.servicenotifier.R.id.forgot_password);
        email = (EditText) findViewById(com.example.iotsolutions.servicenotifier.R.id.email);
        pwd = (EditText) findViewById(com.example.iotsolutions.servicenotifier.R.id.pwd);
        signin.setOnClickListener(user_user_sign_in.this);
        forgot_password.setOnClickListener(user_user_sign_in.this);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        setupFirebaseAuth();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        case com.example.iotsolutions.servicenotifier.R.id.signin:
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(50);
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
                                    // Sign in success, update UI with the signed-in user's information
                                    customerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    initFCM();

                                    Intent intent = new Intent(user_user_sign_in.this, select_admin_or_user.class);
//                                        intent.putExtra("sellerUid", sellerUid);
                                    intent.putExtra("customerUid", customerUid);
                                    startActivity(intent);
                                    overridePendingTransition(com.example.iotsolutions.servicenotifier.R.anim.from_middle, com.example.iotsolutions.servicenotifier.R.anim.to_middle);
                                    Toast.makeText(user_user_sign_in.this, "You will now receive notifications", Toast.LENGTH_LONG).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    // Log.w(seller_sign_in.this, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(user_user_sign_in.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    email.setText("");
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
                    Toast.makeText(user_user_sign_in.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(user_user_sign_in.this, user_user_sign_in.class);
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
                    overridePendingTransition(com.example.iotsolutions.servicenotifier.R.anim.from_middle, com.example.iotsolutions.servicenotifier.R.anim.to_middle);
                    finish();


                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void initFCM()
    {
        Log.d(TAG,"Creating notification token:");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Sending token to server");

        if(token != null) {
            reference.child(getString(com.example.iotsolutions.servicenotifier.R.string.dbnode_tokens))
                    .child(customerUid)
                    .setValue(token);
        }else
        {
            Toast.makeText(user_user_sign_in.this,"Customer id is null",Toast.LENGTH_LONG).show();
        }
    }
}