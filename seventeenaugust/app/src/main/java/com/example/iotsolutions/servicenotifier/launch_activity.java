package com.example.iotsolutions.servicenotifier;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class launch_activity extends AppCompatActivity {
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.iotsolutions.servicenotifier.R.layout.activity_launch_activity);


        t= (TextView) findViewById(com.example.iotsolutions.servicenotifier.R.id.textView);
        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/Comfortaa-Regular.ttf");
        t.setTypeface(myCustomFont);


        Thread thread=new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                   sleep(3000);
                   Intent intent=new Intent(getApplicationContext(),select_admin_or_user.class);
                   startActivity(intent);
                    overridePendingTransition(com.example.iotsolutions.servicenotifier.R.anim.from_middle, com.example.iotsolutions.servicenotifier.R.anim.to_middle);
                   finish();
                }
                catch (InterruptedException e)
                {

                    e.printStackTrace();
                }
            }

        };
        thread.start();
    }
}
