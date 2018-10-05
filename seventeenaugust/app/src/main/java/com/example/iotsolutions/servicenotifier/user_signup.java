package com.example.iotsolutions.servicenotifier;

import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iotsolutions.servicenotifier.Models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class user_signup extends AppCompatActivity implements View.OnClickListener{
    EditText name;
    EditText email;
    EditText password;
    EditText con_pwd;
    Button submit;
    Button verify_email1;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reference;
    String sellerUid;
    Vibrator vibrator;
    @Override
    public void onBackPressed() {
        Vibrator vibrator;
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        Intent intent3=new Intent(this,user_sign_in.class);
        startActivity(intent3);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.iotsolutions.servicenotifier.R.layout.activity_user_signup);

        Toolbar toolbar=(Toolbar)findViewById(com.example.iotsolutions.servicenotifier.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service Notifier");
        toolbar.setSubtitle("User Sign Up");
        toolbar.setSubtitleTextColor(Color.WHITE);


        name=(EditText)findViewById(com.example.iotsolutions.servicenotifier.R.id.name);
        email=(EditText)findViewById(com.example.iotsolutions.servicenotifier.R.id.email);
        password=(EditText)findViewById(com.example.iotsolutions.servicenotifier.R.id.password);
        con_pwd=(EditText)findViewById(com.example.iotsolutions.servicenotifier.R.id.con_pass);
        submit=(Button)findViewById(com.example.iotsolutions.servicenotifier.R.id.submit);
        verify_email1=(Button)findViewById(com.example.iotsolutions.servicenotifier.R.id.verify_email1);
        submit.setOnClickListener(user_signup.this);
        verify_email1.setOnClickListener(user_signup.this);
        if(getIntent().getExtras() != null) {
            sellerUid = getIntent().getExtras().getString("sellerUid");
        }
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        password.getText().toString();
        con_pwd.getText().toString();

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (name.getText().length()<2)
                {
                    name.setError("Please Enter Correct Name");         //if name is less than 5 characters then it will generate error
                }
            }
        });


        con_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!validatepass(password,con_pwd))
                {
                    con_pwd.setError("Enter same data in password and confirm password field");
                }
            }

            private boolean validatepass(EditText password, EditText con_pwd) {

                if (password.equals(con_pwd))
                {
                    return true;
                }
                else
                {
                    return false;

                }
            }

        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!validateEmail(email.getText().toString()))
                {
                    email.setError("Invalid Email!");
                }
            }

           private boolean validateEmail(String s) {
               String emailRegEx;
               Pattern pattern;
               // Regex for a valid email address
               emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
               // Compare the regex with the email address
               pattern = Pattern.compile(emailRegEx);
               Matcher matcher = pattern.matcher(s);
               if (!matcher.find()) {
                   return false;
               }
               return true;
            }

        });

        submit.setEnabled(false);

    }

    @Override
    public void onClick(View view) {
        //  submit.setEnabled(false);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        switch(view.getId())
        {
            case com.example.iotsolutions.servicenotifier.R.id.verify_email1:

                if (email.length() == 0) {
                    email.requestFocus();
                    email.setError("Field Cannot Be Empty");
                }
                else if (name.length()==0)
                {
                    name.requestFocus();
                    name.setError("Field Cannot Be Empty");
                }
                else if (password.length()==0)
                {
                    password.requestFocus();
                    password.setError("Field Cannot Be Empty");
                }
                else if (con_pwd.length()==0)
                {
                    con_pwd.requestFocus();
                    con_pwd.setError("Field Cannot Be Empty");
                }
                else if(con_pwd.getText().equals(con_pwd.getText()))
                {
                    Toast.makeText(this,"Enter same data in password and confirm password", Toast.LENGTH_LONG).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Log.d("seller_sign_up", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    if (task.isSuccessful()) {
                                        sendEmailVerification();
                                        verify_email1.setEnabled(false);
                                        submit.setEnabled(true);
                                    } else {
                                        Toast.makeText(user_signup.this, "Unable to Register.Email-id already exists.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                     //   Intent intent3=new Intent(this,client_add_product.class);
                     //   startActivity(intent3);

                }
                break;
            case com.example.iotsolutions.servicenotifier.R.id.submit:



                // verify_email.setEnabled(false);
                // submit.setEnabled(true);

                FirebaseUser users=mAuth.getCurrentUser();
                boolean emailVerified=users.isEmailVerified();
                //  users=mAuth.getCurrentUser().reload();
                mAuth.getCurrentUser().reload();
                Toast.makeText(user_signup.this,"Please Wait" ,Toast.LENGTH_SHORT).show();
                //     Log.d("seller", "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                if(emailVerified) {

                    Customer customer = new Customer();
                    customer.setName(name.getText().toString());
                    customer.setEmail(email.getText().toString());
                    customer.setIsCustomer("customer");
                    customer.setSellerid(sellerUid);
                    reference.child("customers")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(customer)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //  FirebaseAuth.getInstance().signOut();

                                    //redirect the user to the login screen
                                    //      Toast.makeText(user_signup.this,"Welcome New User",Toast.LENGTH_LONG).show();
                                    //   redirectLoginScreen();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(user_signup.this, "something went wrong.", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();

                            //redirect the user to the login screen
                            redirectLoginScreen();
                        }

                    });

                    reference.child("sellers")
                            .child(sellerUid)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseAuth.getInstance().signOut();

                                    //redirect the user to the login screen
                                    Toast.makeText(user_signup.this,"Welcome New User",Toast.LENGTH_LONG).show();
                                    redirectLoginScreen();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(user_signup.this, "something went wrong.", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();

                            //redirect the user to the login screen
                            redirectLoginScreen();
                        }

                    });

                }
                else{
                    Toast.makeText(user_signup.this, "Please verify your email before submitting or Press once again.", Toast.LENGTH_SHORT).show();


                }
                break;
        }
    }

    private void redirectLoginScreen(){
        //Log.d(seller_sign_up.this, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(user_signup.this, user_sign_in.class);
        intent.putExtra("sellerUid", sellerUid);
        startActivity(intent);
        overridePendingTransition(com.example.iotsolutions.servicenotifier.R.anim.from_middle, com.example.iotsolutions.servicenotifier.R.anim.to_middle);
        finish();
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(user_signup.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        //FirebaseAuth.getInstance().signOut();
                    }
                    else {
                        //  Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(user_signup.this,"Failed to send verification email.",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(user_signup.this,"hii",Toast.LENGTH_SHORT).show();

        }
    }

}
