package com.example.iotsolutions.servicenotifier;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class select_admin_or_user extends AppCompatActivity implements View.OnClickListener{
    Button admin;
    Button user;
    Vibrator vibrator;
    boolean twice;
    public void onBackPressed() {
        Vibrator vibrator;
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        if(twice==true)
        {
            Intent intent=new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        //super.onBackPressed();
        twice=true;
        Toast.makeText(select_admin_or_user.this,"Please press BACK again to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice=false;
            }
        },3000);
        twice=true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.iotsolutions.servicenotifier.R.layout.activity_select_admin_or_user);
        Toolbar toolbar=(Toolbar)findViewById(com.example.iotsolutions.servicenotifier.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service Notifier");
        toolbar.setSubtitle("Choose your role");
        toolbar.setSubtitleTextColor(Color.WHITE);
        admin=(Button)findViewById(com.example.iotsolutions.servicenotifier.R.id.admin);
        user=(Button)findViewById(com.example.iotsolutions.servicenotifier.R.id.user);
        admin.setOnClickListener(select_admin_or_user.this);
        user.setOnClickListener(select_admin_or_user.this);
    }

    @Override
    public void onClick(View view) {
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
        switch(view.getId())
        {
            case com.example.iotsolutions.servicenotifier.R.id.admin:
                Intent intent1=new Intent(this,seller_sign_in.class);
                startActivity(intent1);
                overridePendingTransition(com.example.iotsolutions.servicenotifier.R.anim.from_middle, com.example.iotsolutions.servicenotifier.R.anim.to_middle);
                break;
            case com.example.iotsolutions.servicenotifier.R.id.user:
                Intent intent2=new Intent(this,user_user_sign_in.class);
                startActivity(intent2);
                overridePendingTransition(com.example.iotsolutions.servicenotifier.R.anim.from_middle, com.example.iotsolutions.servicenotifier.R.anim.to_middle);
                break;
        }
    }
}
