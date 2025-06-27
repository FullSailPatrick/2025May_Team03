package com.spotme.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LendActivity extends AppCompatActivity {

    EditText emailInput, phoneInput, amountInput;
    RadioGroup repaymentOptions;
    Button sendApplicationBtn;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend);

        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        amountInput = findViewById(R.id.amountInput);
        repaymentOptions = findViewById(R.id.repaymentOptions);
        sendApplicationBtn = findViewById(R.id.sendApplicationBtn);
        bottomNav = findViewById(R.id.main_navigation);

        sendApplicationBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String amount = amountInput.getText().toString().trim();
            int selectedTerm = repaymentOptions.getCheckedRadioButtonId();

            if (email.isEmpty() || phone.isEmpty() || amount.isEmpty() || selectedTerm == -1) {
                Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Lend application submitted!", Toast.LENGTH_SHORT).show();

            //this navigates to confirmation screen
            Intent intent = new Intent(LendActivity.this, LendConfirmationActivity.class);
            startActivity(intent);
            finish(); //optional but it closes LendActivity so user can’t return to it with back button
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_deals) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("open_deals", true);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_borrow) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("open_borrow", true);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_lend) {
                return true; // already here
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }

            return false;
        });

        // Highlight "Lend" in bottom navigation
        bottomNav.setSelectedItemId(R.id.nav_lend);
    }
}
