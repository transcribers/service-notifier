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
import android.widget.TextView;
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


import org.w3c.dom.Text;

import java.util.ArrayList;

public class EditMessage extends AppCompatActivity {


    Vibrator vibrator;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reference;
    @Override
    public void onBackPressed() {
        Vibrator vibrator;
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        Intent intentt=new Intent(this,user_sign_in.class);
        startActivity(intentt);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service Notifier");
        toolbar.setSubtitle("Custom Notification");
        toolbar.setSubtitleTextColor(Color.WHITE);


        final EditText on;
       final EditText op;
        final EditText spcldis;
        final EditText extr,prname;
        Button submit;


        TextView offname;
        TextView offper;
        TextView sd;
        TextView extra;
        TextView prodname;


        on=(EditText)findViewById(R.id.on);
        op=(EditText)findViewById(R.id.op);
        spcldis=(EditText)findViewById(R.id.spcldis);
        extr=(EditText)findViewById(R.id.extr);
        submit=(Button)findViewById(R.id.submit);
        prname = (EditText)findViewById(R.id.prname);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(50);
                String str1 = on.getText().toString();
                String str2 = op.getText().toString();
                String str3= spcldis.getText().toString();
                String str4=extr.getText().toString();
                String str5 = prname.getText().toString();

                if (str5.length() == 0 ) {
                    prname.requestFocus();
                    prname.setError("Field Cannot Be Empty");
                }else if( str2.length() == 0){
                    op.requestFocus();
                    op.setError("Field Cannot Be Empty");
                }else {

                    String concatenatedText = str5 + " have " + str1 + " offer for " + str2 + " of duration.\n" + " Special discount of " + str3 + "!\n";
                    if(str4.length() != 0)
                    {
                        concatenatedText = concatenatedText + "\nSome extra info: "+str4;
                    }

                    reference
                            .child(getString(R.string.dbnode_sellers))
                            .child(mAuth.getCurrentUser().getUid())
                            .child(reference.push().getKey())
                            .setValue(concatenatedText)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    prname.setText("");
                                    on.setText("");
                                    op.setText("");
                                    spcldis.setText("");
                                    extr.setText("");
                                    //redirect the user to the login screen
                                    Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();

                                }
                            });


                }
            }
        });



    }
}
