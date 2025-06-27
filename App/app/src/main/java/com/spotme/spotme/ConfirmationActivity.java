package com.spotme.spotme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_screen);

        Button continueToLendBtn = findViewById(R.id.continueToLendBtn);
        continueToLendBtn.setOnClickListener(v -> {
            //Save lender_app_done = true
            SharedPreferences prefs = getSharedPreferences("spotme_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("lender_app_done", true).apply();

            //Go to MainActivity and open Lend
            Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
            intent.putExtra("open_lend", true);  //this is to tell MainActivity to load Lend
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
