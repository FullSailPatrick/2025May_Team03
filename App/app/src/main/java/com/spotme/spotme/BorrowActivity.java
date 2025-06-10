package com.spotme.spotme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BorrowActivity extends AppCompatActivity {

    EditText lenderEmailInput, lenderPhoneInput, amountRequestedInput, reasonInput;
    RadioGroup repaymentOptions, urgencyOptions;
    Button submitRequestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow);

        // Hide title bar if needed
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize form elements
        lenderEmailInput = findViewById(R.id.lenderEmailInput);
        lenderPhoneInput = findViewById(R.id.lenderPhoneInput);
        amountRequestedInput = findViewById(R.id.amountRequestedInput);
        reasonInput = findViewById(R.id.reasonInput);
        repaymentOptions = findViewById(R.id.repaymentOptions);
        urgencyOptions = findViewById(R.id.urgencyOptions);
        submitRequestBtn = findViewById(R.id.submitRequestBtn);

        // Handle submit button click
        submitRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBorrowRequest();
            }
        });
    }

    private void submitBorrowRequest() {
        // Get form data
        String lenderEmail = lenderEmailInput.getText().toString().trim();
        String lenderPhone = lenderPhoneInput.getText().toString().trim();
        String amount = amountRequestedInput.getText().toString().trim();
        String reason = reasonInput.getText().toString().trim();

        int selectedRepaymentId = repaymentOptions.getCheckedRadioButtonId();
        int selectedUrgencyId = urgencyOptions.getCheckedRadioButtonId();

        // Validate required fields
        if (!validateForm(lenderEmail, lenderPhone, amount, reason, selectedRepaymentId, selectedUrgencyId)) {
            return;
        }

        // Get selected options text
        RadioButton selectedRepaymentTerm = findViewById(selectedRepaymentId);
        RadioButton selectedUrgency = findViewById(selectedUrgencyId);

        String repaymentTerm = selectedRepaymentTerm.getText().toString();
        String urgencyLevel = selectedUrgency.getText().toString();

        // Show success message
        Toast.makeText(this,
                "Money request sent!\nAmount: $" + amount +
                        "\nRepayment: " + repaymentTerm +
                        "\nPriority: " + urgencyLevel,
                Toast.LENGTH_LONG).show();

        // TODO: Here you would typically:
        // 1. Save the request to Firebase/database
        // 2. Send notification to the lender
        // 3. Navigate to confirmation screen

        // Clear form after successful submission
        clearForm();
    }

    private boolean validateForm(String email, String phone, String amount, String reason,
                                 int repaymentId, int urgencyId) {

        if (email.isEmpty()) {
            lenderEmailInput.setError("Lender email is required");
            lenderEmailInput.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            lenderEmailInput.setError("Please enter a valid email address");
            lenderEmailInput.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            lenderPhoneInput.setError("Lender phone is required");
            lenderPhoneInput.requestFocus();
            return false;
        }

        if (amount.isEmpty()) {
            amountRequestedInput.setError("Amount is required");
            amountRequestedInput.requestFocus();
            return false;
        }

        try {
            double amountValue = Double.parseDouble(amount.replace("$", ""));
            if (amountValue <= 0) {
                amountRequestedInput.setError("Amount must be greater than 0");
                amountRequestedInput.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            amountRequestedInput.setError("Please enter a valid amount");
            amountRequestedInput.requestFocus();
            return false;
        }

        if (reason.isEmpty()) {
            reasonInput.setError("Reason is required");
            reasonInput.requestFocus();
            return false;
        }

        if (repaymentId == -1) {
            Toast.makeText(this, "Please select a repayment term", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (urgencyId == -1) {
            Toast.makeText(this, "Please select urgency level", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void clearForm() {
        lenderEmailInput.setText("");
        lenderPhoneInput.setText("");
        amountRequestedInput.setText("");
        reasonInput.setText("");
        repaymentOptions.clearCheck();
        urgencyOptions.clearCheck();

        // Clear any error messages
        lenderEmailInput.setError(null);
        lenderPhoneInput.setError(null);
        amountRequestedInput.setError(null);
        reasonInput.setError(null);
    }

    /*
     ToDo: add some form of lender rating experience to the page after transaction complete
     Optional: Method to handle back button or navigation
    */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // You could add custom behavior here if needed
    }


}