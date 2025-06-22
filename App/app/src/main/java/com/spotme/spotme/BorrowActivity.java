package com.spotme.spotme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BorrowActivity extends AppCompatActivity {

    // UI Components
    EditText borrowerNameInput, borrowerEmailInput, borrowerPhoneInput;
    EditText amountRequestedInput, reasonInput;
    RadioGroup repaymentOptions, urgencyOptions;
    Button submitRequestBtn;


    // Firebase Components
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore requestInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow);


        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        requestInfo = FirebaseFirestore.getInstance();

        // Initialize UI components
        findViews();

        // Set up button listener
        setupSubmitButton();

        // Auto-populate user info
        populateUserInfo();

        // Set up formatters
        setupFormatters();
    }

    private void findViews() {
        borrowerNameInput = findViewById(R.id.borrowerNameInput);
        borrowerEmailInput = findViewById(R.id.borrowerEmailInput);
        borrowerPhoneInput = findViewById(R.id.borrowerPhoneInput);
        amountRequestedInput = findViewById(R.id.amountRequestedInput);
        reasonInput = findViewById(R.id.reasonInput);
        repaymentOptions = findViewById(R.id.repaymentOptions);
        urgencyOptions = findViewById(R.id.urgencyOptions);
        submitRequestBtn = findViewById(R.id.submitRequestBtn);
    }



    private void setupSubmitButton() {
        if (submitRequestBtn != null) {
            submitRequestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSubmit();
                }
            });
        }
    }

    private void populateUserInfo() {
        if (currentUser != null) {
            if (currentUser.getDisplayName() != null) {
                borrowerNameInput.setText(currentUser.getDisplayName());
            }
            if (currentUser.getEmail() != null) {
                borrowerEmailInput.setText(currentUser.getEmail());
            }
        }
    }

    private void setupFormatters() {
        // Phone formatter
        borrowerPhoneInput.addTextChangedListener(new TextWatcher() {
            private boolean isEditing = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;

                String input = s.toString().replaceAll("[^\\d]", "");
                if (input.length() > 0) {
                    StringBuilder formatted = new StringBuilder();
                    if (input.length() >= 3) {
                        formatted.append("(").append(input.substring(0, 3)).append(") ");
                        if (input.length() >= 6) {
                            formatted.append(input.substring(3, 6)).append("-");
                            if (input.length() >= 10) {
                                formatted.append(input.substring(6, 10));
                            } else if (input.length() > 6) {
                                formatted.append(input.substring(6));
                            }
                        } else if (input.length() > 3) {
                            formatted.append(input.substring(3));
                        }
                    } else {
                        formatted.append("(").append(input);
                    }
                    s.replace(0, s.length(), formatted.toString());
                }
                isEditing = false;
            }
        });

        // Currency formatter - only format on focus loss, not while typing
        amountRequestedInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Format when user finishes typing (loses focus)
                    String input = amountRequestedInput.getText().toString().replaceAll("[^\\d.]", "");
                    if (!input.isEmpty()) {
                        try {
                            double amount = Double.parseDouble(input);
                            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
                            String formatted = formatter.format(amount);
                            amountRequestedInput.setText(formatted);
                        } catch (NumberFormatException e) {
                            // Keep original if parsing fails
                        }
                    }
                }
            }
        });
    }

    private void handleSubmit() {
        // Get form data
        String name = borrowerNameInput.getText().toString().trim();
        String email = borrowerEmailInput.getText().toString().trim();
        String phone = borrowerPhoneInput.getText().toString().trim();
        String amountStr = amountRequestedInput.getText().toString().trim();
        String reason = reasonInput.getText().toString().trim();

        int repaymentId = repaymentOptions.getCheckedRadioButtonId();
        int urgencyId = urgencyOptions.getCheckedRadioButtonId();

        // Simple validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                amountStr.isEmpty() || reason.isEmpty() ||
                repaymentId == -1 || urgencyId == -1) {

            showMissingFieldsDialog();
            return;
        }

        // Get selected values
        RadioButton repaymentBtn = findViewById(repaymentId);
        RadioButton urgencyBtn = findViewById(urgencyId);
        String repaymentTerm = repaymentBtn.getText().toString();
        String urgency = urgencyBtn.getText().toString();

        // Create transaction ID
        String transactionId = createTransactionID();

        // Parse amount
        double amount = parseAmount(amountStr);

        // Validate amount
        if (amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount greater than $0", Toast.LENGTH_SHORT).show();
            return;
        }
        if (amount > 1000) {
            Toast.makeText(this, "Maximum loan amount is $1,000", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create request data
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("Transaction ID", transactionId);
        requestData.put("Borrower Name", name);
        requestData.put("Borrower Email", email);
        requestData.put("Borrower Phone", phone);
        requestData.put("Loan Amount", amount);
        requestData.put("Loan Reason", reason);
        requestData.put("Repayment Term", repaymentTerm);
        requestData.put("Urgency", urgency);
        requestData.put("Request Status", "pending");
        requestData.put("Date Created", System.currentTimeMillis());

        if (currentUser != null) {
            requestData.put("Borrower User ID", currentUser.getUid());
        }

        // Save to database
        saveToDatabase(transactionId, requestData, amount, repaymentTerm, urgency);
    }

    private void saveToDatabase(String transactionId, Map<String, Object> requestData,
                                double amount, String repaymentTerm, String urgency) {

        requestInfo.collection("BorrowRequests").document(transactionId).set(requestData)
                .addOnSuccessListener(aVoid -> {
                    showSuccessDialog(transactionId, amount, repaymentTerm, urgency);
                })
                .addOnFailureListener(e -> {
                    showErrorDialog();
                });
    }

    private void showSuccessDialog(String transactionId, double amount, String repaymentTerm, String urgency) {
        String message = "Your loan request has been successfully submitted!\n\n" +
                "Transaction ID: " + transactionId + "\n" +
                "Amount: " + NumberFormat.getCurrencyInstance(Locale.US).format(amount) + "\n" +
                "Repayment: " + repaymentTerm + "\n" +
                "Urgency: " + urgency + "\n\n" +
                "Your request is now available for lenders to review.";

        new AlertDialog.Builder(this)
                .setTitle("✅ Success!")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Return to MainActivity and switch to home view
                    returnToHome();
                })
                .setCancelable(false) // Prevent dismissing by tapping outside
                .show();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("❌ Error")
                .setMessage("Failed to submit request. Please check your connection and try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showMissingFieldsDialog() {
        StringBuilder missingFields = new StringBuilder("Please complete the following required fields:\n\n");

        if (borrowerNameInput.getText().toString().trim().isEmpty()) {
            missingFields.append("• Your Name\n");
        }
        if (borrowerEmailInput.getText().toString().trim().isEmpty()) {
            missingFields.append("• Your Email\n");
        }
        if (borrowerPhoneInput.getText().toString().trim().isEmpty()) {
            missingFields.append("• Your Phone\n");
        }
        if (amountRequestedInput.getText().toString().trim().isEmpty()) {
            missingFields.append("• Loan Amount\n");
        }
        if (reasonInput.getText().toString().trim().isEmpty()) {
            missingFields.append("• Reason for Request\n");
        }
        if (repaymentOptions.getCheckedRadioButtonId() == -1) {
            missingFields.append("• Repayment Terms\n");
        }
        if (urgencyOptions.getCheckedRadioButtonId() == -1) {
            missingFields.append("• Urgency Level\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("⚠️ Missing Information")
                .setMessage(missingFields.toString())
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void returnToHome() {
        // Create intent to return to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        // Clear the activity stack and create a new task
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Add extra to indicate we want to show the home screen
        intent.putExtra("show_home", true);
        startActivity(intent);
        finish(); // Close this activity
    }

    private String createTransactionID() {
        if (currentUser != null) {
            String baseID = currentUser.getUid() + "_" + System.currentTimeMillis();
            return Integer.toHexString(baseID.hashCode());
        }
        return "guest_" + System.currentTimeMillis();
    }

    private double parseAmount(String amountStr) {
        try {
            return Double.parseDouble(amountStr.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void clearForm() {
        borrowerNameInput.setText("");
        borrowerEmailInput.setText("");
        borrowerPhoneInput.setText("");
        amountRequestedInput.setText("");
        reasonInput.setText("");
        repaymentOptions.clearCheck();
        urgencyOptions.clearCheck();
    }
}






