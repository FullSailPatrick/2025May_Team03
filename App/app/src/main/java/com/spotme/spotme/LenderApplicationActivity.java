package com.spotme.spotme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LenderApplicationActivity extends AppCompatActivity {

    EditText firstNameInput, lastNameInput;
    RadioGroup peerLendingGroup, incomeGroup;
    Button nextBtn, goBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lender_application);

        // Link views
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        peerLendingGroup = findViewById(R.id.peerLendingGroup);
        incomeGroup = findViewById(R.id.incomeGroup);
        nextBtn = findViewById(R.id.nextBtn);
        goBackBtn = findViewById(R.id.goBackBtn);

        // Next button logic
        nextBtn.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            int peerId = peerLendingGroup.getCheckedRadioButtonId();
            int incomeId = incomeGroup.getCheckedRadioButtonId();

            if (firstName.isEmpty() || lastName.isEmpty() || peerId == -1 || incomeId == -1) {
                Toast.makeText(this, "Please complete all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mark lender application as complete
            SharedPreferences prefs = getSharedPreferences("spotme_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("lender_app_done", true).apply();

            // Go to confirmation screen
            Intent intent = new Intent(this, ConfirmationActivity.class);
            intent.putExtra("open_lend", true);
            startActivity(intent);
            finish(); // close this screen
        });

        // Go back button
        goBackBtn.setOnClickListener(v -> finish());
    }
}
