package com.spotme.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_screen);

        Button continueButton = findViewById(R.id.continueToLendBtn); // this is for button ID
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
                intent.putExtra("goToLend", true); //check to signal to open Lend view
                startActivity(intent);
                finish(); //note optional: close the confirmation screen
            }
        });
    }
}
