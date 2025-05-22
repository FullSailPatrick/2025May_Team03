package com.spotme.spotme;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);

        // Example toggle listeners
//        Switch notificationSwitch = findViewById(R.id.switchToggle);
//        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            Toast.makeText(this, "Notifications " + (isChecked ? "On" : "Off"), Toast.LENGTH_SHORT).show();
//        });

        // Example text item click
        //LinearLayout logoutRow = findViewById(R.id.row_logout); // If you assign it an ID
       // logoutRow.setOnClickListener(v -> {
          //  Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
        //});
    }
}
