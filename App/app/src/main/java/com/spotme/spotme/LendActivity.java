package com.spotme.spotme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LendActivity extends AppCompatActivity {

    EditText emailInput, phoneInput, amountInput;
    RadioGroup repaymentOptions;
    Button sendApplicationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend);

        // Link views
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        amountInput = findViewById(R.id.amountInput);
        repaymentOptions = findViewById(R.id.repaymentOptions);
        sendApplicationBtn = findViewById(R.id.sendApplicationBtn);

        // Handle submit
        sendApplicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();
                String amount = amountInput.getText().toString().trim();
                int selectedId = repaymentOptions.getCheckedRadioButtonId();

                if (email.isEmpty() || phone.isEmpty() || amount.isEmpty() || selectedId == -1) {
                    Toast.makeText(LendActivity.this, "Please complete all fields and select a repayment term.", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedTerm = findViewById(selectedId);
                String term = selectedTerm.getText().toString();

                Toast.makeText(
                        LendActivity.this,
                        "Loan application submitted for: " + term,
                        Toast.LENGTH_SHORT
                ).show();

                // TODO: Store or send data here
            }
        });
    }
}
