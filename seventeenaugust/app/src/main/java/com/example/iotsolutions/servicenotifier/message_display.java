package com.example.iotsolutions.servicenotifier;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

//import org.w3c.dom.Text;

/**
 * Created by Admin on 8/3/2018.
 */

public class message_display extends AppCompatActivity {

    TextView t1 , t2;
    String title,message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.iotsolutions.servicenotifier.R.layout.activity_message_display);
        Toolbar toolbar=(Toolbar)findViewById(com.example.iotsolutions.servicenotifier.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service Notifier");
        toolbar.setSubtitle("Notification Alert");
        toolbar.setSubtitleTextColor(Color.WHITE);
        t1 = (TextView) findViewById(com.example.iotsolutions.servicenotifier.R.id.title);
        t2 = (TextView) findViewById(com.example.iotsolutions.servicenotifier.R.id.message);

        if(getIntent().getExtras() != null) {
            title = getIntent().getExtras().getString("title");
            t1.setText("Reminder by Service Notifier");

            message = getIntent().getExtras().getString("message");
            t2.setText(message);
        }

    }

}
