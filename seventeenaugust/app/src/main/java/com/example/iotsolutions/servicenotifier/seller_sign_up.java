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
import android.widget.Toast;

import com.example.iotsolutions.servicenotifier.Models.Seller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class seller_sign_up extends AppCompatActivity implements View.OnClickListener{
    Button submit;
    Button verify_email;
    EditText email,name,phone;
    EditText pwd;
    EditText cpwd;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    String sellerUid;
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
        setContentView(com.example.iotsolutions.servicenotifier.R.layout.activity_seller_sign_up);

        Toolbar toolbar=(Toolbar)findViewById(com.example.iotsolutions.servicenotifier.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service Notifier");
        toolbar.setSubtitle("Seller Sign Up");
        toolbar.setSubtitleTextColor(Color.WHITE);

        submit=(Button)findViewById(com.example.iotsolutions.servicenotifier.R.id.submit);
        verify_email=(Button)findViewById(com.example.iotsolutions.servicenotifier.R.id.verify_email);
        name = (EditText) findViewById(com.example.iotsolutions.servicenotifier.R.id.sname);
        phone = (EditText) findViewById(com.example.iotsolutions.servicenotifier.R.id.phon);
        email=(EditText)findViewById(com.example.iotsolutions.servicenotifier.R.id.email);
        pwd=(EditText)findViewById(com.example.iotsolutions.servicenotifier.R.id.pwd);
        cpwd=(EditText)findViewById(com.example.iotsolutions.servicenotifier.R.id.cpwd);
        mAuth=FirebaseAuth.getInstance();
        submit.setOnClickListener(seller_sign_up.this);
        verify_email.setOnClickListener(seller_sign_up.this);
        reference = FirebaseDatabase.getInstance().getReference();
        final String Email = email.getText().toString();
        final String Phone = phone.getText().toString();
        final String Name = name.getText().toString();
        final String Pass=pwd.getText().toString();
        final String CPass=cpwd.getText().toString();
        if (Name.length() == 0)
        {
            name.requestFocus();
            name.setError("Field Cannot Be Empty");
        }
        else if (Phone.length() == 0) {
            phone.requestFocus();
            phone.setError("Field Cannot Be Empty");
        }
        else if (Phone.length() < 10 || Phone.length() > 10) {
            phone.requestFocus();
            phone.setError("Field length should equal to 10");
        }
      else  if (Email.length() == 0)
        {
            email.requestFocus();
            email.setError("Field Cannot Be Empty");
        }
        else if (!Email.matches( "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                "\\@" +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                "(" +

                "\\." +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                ")+")) {
            email.requestFocus();
            email.setError("Enter valid Email");
        }
        else if (Pass.length()==0)
        {
            pwd.requestFocus();
            pwd.setError("Field Cannot Be Empty");
        }
        else if (CPass.length()==0)
        {
            cpwd.requestFocus();
            cpwd.setError("Field Cannot Be Empty");
        }
        else if(!Pass.equals(CPass))
        {
            Toast.makeText(this,"Enter same data in password and confirm password", Toast.LENGTH_LONG).show();
        }
        submit.setEnabled(false);

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    name.setHint("");
                } else {
                    name.setHint("");
                }
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    phone.setHint("");
                } else {
                    phone.setHint("");
                }
            }
        });

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

        cpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    cpwd.setHint("");
                } else {
                    cpwd.setHint("");
                }
            }
        });


    }


    @Override
    public void onClick(View view) {
      //  submit.setEnabled(false);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        switch(view.getId())
        {
            case com.example.iotsolutions.servicenotifier.R.id.verify_email:

                if (email.length() == 0) {
                    email.requestFocus();
                    email.setError("Field Cannot Be Empty");
                }
                else if (pwd.length()==0)
                {
                    pwd.requestFocus();
                    pwd.setError("Field Cannot Be Empty");
                }
                else if (cpwd.length()==0)
                {
                    cpwd.requestFocus();
                    cpwd.setError("Field Cannot Be Empty");
                }
                else if(pwd.getText().equals(cpwd.getText()))
                {
                    Toast.makeText(this,"Enter same data in password and confirm password", Toast.LENGTH_LONG).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), pwd.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("seller_sign_up", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    if (task.isSuccessful()) {
                                        sendEmailVerification();
                                        verify_email.setEnabled(false);
                                        submit.setEnabled(true);
                                    } else {
                                        Toast.makeText(seller_sign_up.this, "Unable to Register.Email-id already exists.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            case com.example.iotsolutions.servicenotifier.R.id.submit:

                FirebaseUser users=mAuth.getCurrentUser();
                boolean emailVerified=users.isEmailVerified();
              //  users=mAuth.getCurrentUser().reload();
                mAuth.getCurrentUser().reload();
                Toast.makeText(seller_sign_up.this,"Please Wait",Toast.LENGTH_SHORT).show();
           //     Log.d("seller", "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                if(emailVerified) {
                    sellerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Seller seller = new Seller();
                    seller.setEmail(email.getText().toString());
                    seller.setName(name.getText().toString());
                    seller.setPhone(phone.getText().toString());
                    seller.setIsSeller("seller");

                    //insert some default data

                    reference
                            .child(getString(com.example.iotsolutions.servicenotifier.R.string.dbnode_sellers))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(seller)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseAuth.getInstance().signOut();
                                    verify_email.setEnabled(true);
                                    submit.setEnabled(false);
                                    //redirect the user to the login screen
                                    redirectLoginScreen();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(seller_sign_up.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();

                            //redirect the user to the login screen
                            redirectLoginScreen();
                        }
                    });
                                        }
                else{
                    Toast.makeText(seller_sign_up.this, "Please verify your email before submitting or Press once again.", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(seller_sign_up.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        //FirebaseAuth.getInstance().signOut();
                    }
                    else {
                      //  Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(seller_sign_up.this,"Failed to send verification email.",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(seller_sign_up.this,"hii",Toast.LENGTH_SHORT).show();

        }
    }


    private void redirectLoginScreen(){
        //Log.d(seller_sign_up.this, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(seller_sign_up.this, seller_sign_in.class);
        intent.putExtra("sellerUid",sellerUid);
        startActivity(intent);
        overridePendingTransition(com.example.iotsolutions.servicenotifier.R.anim.from_middle, com.example.iotsolutions.servicenotifier.R.anim.to_middle);
        finish();
    }

}

