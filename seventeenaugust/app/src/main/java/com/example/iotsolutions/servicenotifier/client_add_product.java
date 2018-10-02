package com.example.iotsolutions.servicenotifier;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iotsolutions.servicenotifier.Models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class client_add_product extends AppCompatActivity implements View.OnClickListener {

    Button submit;
    Button add_more_products;
    EditText prodnme;
    EditText phnno;
    TextView dte; //Date_Of_Purchase
    // TextView mDisplayDate;
    RadioButton rad1;
    RadioButton rad2;
    RadioGroup rad;
    Vibrator vibrator;
    EditText duration;
    EditText endDate;
    String customerUid;
    String sellerUid;
    FirebaseAuth mAuth;
    //FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reference;
    @Override
    public void onBackPressed() {
        Vibrator vibrator;
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        Intent intent3=new Intent(this,select_admin_or_user.class);
        startActivity(intent3);

    }
    private DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.iotsolutions.servicenotifier.R.layout.activity_client_add_product);


        Toolbar toolbar=(Toolbar)findViewById(com.example.iotsolutions.servicenotifier.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service Notifier");
        toolbar.setSubtitle("Add Product");
        toolbar.setSubtitleTextColor(Color.WHITE);


        submit = (Button) findViewById(com.example.iotsolutions.servicenotifier.R.id.submit);
      //  add_more_products = (Button) findViewById(R.id.add_more_products);
        phnno = (EditText) findViewById(com.example.iotsolutions.servicenotifier.R.id.phnno);
      //  add_more_products.setOnClickListener(client_add_product.this);
        prodnme = (EditText) findViewById(com.example.iotsolutions.servicenotifier.R.id.prodnme);
        dte = (TextView) findViewById(com.example.iotsolutions.servicenotifier.R.id.dte);
        rad1 = (RadioButton) findViewById(com.example.iotsolutions.servicenotifier.R.id.rad1);
        rad2 = (RadioButton) findViewById(com.example.iotsolutions.servicenotifier.R.id.rad2);
        rad = (RadioGroup) findViewById(com.example.iotsolutions.servicenotifier.R.id.rad);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        if (getIntent().getExtras() != null) {
            sellerUid = getIntent().getExtras().getString("sellerUid");
        }

        if (getIntent().getExtras() != null) {
            customerUid = getIntent().getExtras().getString("customerUid");
        }
        phnno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    phnno.setHint("");
                } else {
                    phnno.setHint("");
                }
            }
        });

        prodnme.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    prodnme.setHint("");
                } else {
                    prodnme.setHint("");
                }
            }
        });

        dte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    dte.setHint("It will be set automatically");
                } else {
                    dte.setHint("");
                }
            }
        });


        //for selction of duration of service period for lifetime service
        rad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(client_add_product.this);
                View mview = getLayoutInflater().inflate(com.example.iotsolutions.servicenotifier.R.layout.lifetime, null);
                duration = (EditText) mview.findViewById(com.example.iotsolutions.servicenotifier.R.id.duration);
                duration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            duration.setHint("");
                        } else {
                            duration.setHint("");
                        }
                    }
                });

                Button ok = (Button) mview.findViewById(com.example.iotsolutions.servicenotifier.R.id.ok);
                mbuilder.setView(mview);
                final AlertDialog alertDialog = mbuilder.create();
                alertDialog.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!duration.getText().toString().isEmpty()) {
                            Toast.makeText(client_add_product.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                            alertDialog.hide();
                        } else {
                            Toast.makeText(client_add_product.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });
        //for limited service
        rad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(client_add_product.this);
                View mview = getLayoutInflater().inflate(com.example.iotsolutions.servicenotifier.R.layout.limited, null);
                duration = (EditText) mview.findViewById(com.example.iotsolutions.servicenotifier.R.id.duration);
                endDate = (EditText) mview.findViewById(com.example.iotsolutions.servicenotifier.R.id.endDate);
                duration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            duration.setHint("");

                        } else {
                            duration.setHint("");

                        }


                    }
                });

                endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            Calendar calendar = Calendar.getInstance();

                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);


                            DatePickerDialog dialog = new DatePickerDialog(
                                    client_add_product.this,
                                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                    dateSetListener,
                                    year, month, day
                            );
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                        }
                        else {
                            endDate.setHint("yyyy-mm-dd");
                        }
                        dateSetListener=new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String _day="",_month="";
                                if(day <= 9)
                                {
                                    _day = '0' + Integer.toString(day);
                                }else
                                {
                                    _day = Integer.toString(day);
                                }
                                if(month <= 8)
                                {
                                    _month= '0' + Integer.toString(month+1);
                                }
                                else
                                {
                                    _month = Integer.toString(month+1);
                                }
                                String date=year + "-" +_month + "-" + _day;
                                endDate.setText(date);

                            }
                        };
                    }
                });

                Button ok = (Button) mview.findViewById(com.example.iotsolutions.servicenotifier.R.id.ok);
                mbuilder.setView(mview);
                final AlertDialog alertDialog = mbuilder.create();
                alertDialog.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!duration.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()) {
                            Toast.makeText(client_add_product.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                            alertDialog.hide();
                        } else {
                            Toast.makeText(client_add_product.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date dt = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String check = dateFormat.format(dt);
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(50);
                final String pname = prodnme.getText().toString();
                final String Phone = phnno.getText().toString();

                Calendar cal = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(cal.getTime());  // Gives default locale format depending on the location
                dte.setText(check);


                if (pname.length() == 0) {
                    prodnme.requestFocus();
                    prodnme.setError("Field Cannot Be Empty");
                } else if (!pname.matches("[a-zA-Z ]+")) {
                    prodnme.requestFocus();
                    prodnme.setError("Enter Only Alphabetical Character");
                } else if (Phone.length() == 0) {
                    phnno.requestFocus();
                    phnno.setError("Field Cannot Be Empty");
                } else if (Phone.length() < 10 || Phone.length() > 10) {
                    phnno.requestFocus();
                    phnno.setError("Field length should equal to 10");
                } else if (!(rad1.isChecked() || rad2.isChecked())) {
                    rad.requestFocus();
                    Toast.makeText(client_add_product.this, "Please Select at least one of the radio buttons.", Toast.LENGTH_SHORT).show();

                  //  Toast.makeText(client_add_product, "Please Select at least one of the radio button", Toast.LENGTH_SHORT).show();
                }
                else if(duration.getText().toString().isEmpty()) {
                    Toast.makeText(client_add_product.this, "Please fill out all the details.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                    else {
                    Product product = new Product();
                    product.setPname(pname);
                    product.setPhone(Phone);
                    product.setDate_of_purchase(check);
                    product.setInterval(duration.getText().toString());
                    if(rad1.isChecked()) {
                        product.setEnd_date(endDate.getText().toString());
                    }
//                    if(rad2.isChecked()) {
//                        product.setEnd_date(null);
//                    }

                    reference.child("customers")
                            .child(customerUid)
                            .child("products")
                            .child(reference.push().getKey())
                            .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(client_add_product.this, "Product added successfully.", Toast.LENGTH_SHORT).show();
                            prodnme.setText("");
                            phnno.setText("");
                            dte.setText("");

                            rad.clearCheck();
                            //redirect the user to the login screen
                            redirectLoginScreen();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(client_add_product.this, "something went wrong.", Toast.LENGTH_SHORT).show();

                            redirectLoginScreen();
                        }

                    });

                }
            }
        });


    }
    private void redirectLoginScreen() {

        Intent intent = new Intent(client_add_product.this, client_add_product.class);
        rad.clearCheck();
        startActivity(intent);
       // finish();
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.iotsolutions.servicenotifier.R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== com.example.iotsolutions.servicenotifier.R.id.logout)
        {
            Intent intent = new Intent(client_add_product.this, seller_sign_in.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
