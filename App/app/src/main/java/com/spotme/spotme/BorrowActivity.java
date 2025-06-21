package com.spotme.spotme;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BorrowActivity extends AppCompatActivity {

    // UI Components
    EditText lenderEmailInput, lenderPhoneInput, amountRequestedInput, reasonInput;
    RadioGroup repaymentOptions;
    Button submitRequestBtn;

    // Firebase Components
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore requestInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow);

        // Hide title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        requestInfo = FirebaseFirestore.getInstance();

        // Check if user is logged in
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to request money", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
        initializeUIComponents();

        // Set up auto-formatting
        setupPhoneFormatter();
        setupCurrencyFormatter();

        // Set up button click listener
        submitRequestBtn.setOnClickListener(v -> submitBorrowRequest());
    }

    /**
     * Initialize all UI components
     */
    private void initializeUIComponents() {
        lenderEmailInput = findViewById(R.id.lenderEmailInput);
        lenderPhoneInput = findViewById(R.id.lenderPhoneInput);
        amountRequestedInput = findViewById(R.id.amountRequestedInput);
        reasonInput = findViewById(R.id.reasonInput);
        repaymentOptions = findViewById(R.id.repaymentOptions);
        submitRequestBtn = findViewById(R.id.submitRequestBtn);
    }

    /**
     * Sets up automatic phone number formatting
     * Formats to: +1 (123) - 456 - 7890
     */
    private void setupPhoneFormatter() {
        lenderPhoneInput.addTextChangedListener(new TextWatcher() {
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
                    StringBuilder formatted = new StringBuilder("+1 ");

                    if (input.length() >= 3) {
                        formatted.append("(").append(input.substring(0, 3)).append(")");
                        if (input.length() >= 6) {
                            formatted.append(" - ").append(input.substring(3, 6));
                            if (input.length() >= 10) {
                                formatted.append(" - ").append(input.substring(6, 10));
                            } else if (input.length() > 6) {
                                formatted.append(" - ").append(input.substring(6));
                            }
                        } else if (input.length() > 3) {
                            formatted.append(" - ").append(input.substring(3));
                        }
                    } else {
                        formatted.append("(").append(input);
                    }

                    s.replace(0, s.length(), formatted.toString());
                }
                isEditing = false;
            }
        });
    }

    /**
     * Sets up automatic currency formatting
     * Formats to: $1,000.00
     */
    private void setupCurrencyFormatter() {
        amountRequestedInput.addTextChangedListener(new TextWatcher() {
            private boolean isEditing = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;

                isEditing = true;
                String input = s.toString().replaceAll("[^\\d.]", "");

                if (!input.isEmpty()) {
                    try {
                        double amount = Double.parseDouble(input);
                        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
                        String formatted = formatter.format(amount);
                        s.replace(0, s.length(), formatted);
                    } catch (NumberFormatException e) {
                        // Keep original if parsing fails
                    }
                }
                isEditing = false;
            }
        });
    }

    /**
     * Main submission method - pulls user input, validates, and submits request
     */
    private void submitBorrowRequest() {
        clearErrors();

        // Pull user input and store in variables
        String lenderEmail = lenderEmailInput.getText().toString().trim();
        String lenderPhone = lenderPhoneInput.getText().toString().trim();
        String amountStr = amountRequestedInput.getText().toString().trim();
        String reason = reasonInput.getText().toString().trim();
        int selectedRepaymentId = repaymentOptions.getCheckedRadioButtonId();

        // Validate all fields and highlight fields if user missed them or entered incorrectly
        if (!validateAllFields(lenderEmail, lenderPhone, amountStr, reason, selectedRepaymentId)) {
            return;
        }

        // Get selected repayment term
        RadioButton selectedRepaymentTerm = findViewById(selectedRepaymentId);
        String repaymentTerm = selectedRepaymentTerm.getText().toString();

        // Create and submit the request
        createAndSubmitRequest(lenderEmail, lenderPhone, parseAmount(amountStr), reason, repaymentTerm);
    }

    /**
     * Validates all form fields and highlights errors
     * Returns true if all fields are valid
     */
    private boolean validateAllFields(String lenderEmail, String lenderPhone, String amountStr,
                                      String reason, int selectedRepaymentId) {
        boolean isValid = true;

        // Email validation
        if (lenderEmail.isEmpty()) {
            lenderEmailInput.setError("Lender email is required");
            lenderEmailInput.requestFocus();
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(lenderEmail).matches()) {
            lenderEmailInput.setError("Please enter a valid email address");
            lenderEmailInput.requestFocus();
            isValid = false;
        }

        // Phone number validation - auto format country code (area code) - xxx - xxxx
        if (lenderPhone.isEmpty()) {
            lenderPhoneInput.setError("Lender phone is required");
            if (isValid) lenderPhoneInput.requestFocus();
            isValid = false;
        } else if (!isValidPhoneNumber(lenderPhone)) {
            lenderPhoneInput.setError("Please enter a valid phone number");
            if (isValid) lenderPhoneInput.requestFocus();
            isValid = false;
        }

        // Borrow amount validation - always autoformat to currency
        if (amountStr.isEmpty()) {
            amountRequestedInput.setError("Amount is required");
            if (isValid) amountRequestedInput.requestFocus();
            isValid = false;
        } else {
            try {
                double amount = parseAmount(amountStr);
                if (amount <= 0) {
                    amountRequestedInput.setError("Amount must be greater than $0");
                    if (isValid) amountRequestedInput.requestFocus();
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                amountRequestedInput.setError("Please enter a valid amount");
                if (isValid) amountRequestedInput.requestFocus();
                isValid = false;
            }
        }

        // Reason validation
        if (reason.isEmpty()) {
            reasonInput.setError("Reason is required");
            if (isValid) reasonInput.requestFocus();
            isValid = false;
        }

        // Repayment term validation
        if (selectedRepaymentId == -1) {
            Toast.makeText(this, "Please select a repayment term", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    /**
     * Creates the borrow request with user information and submits to database
     */
    private void createAndSubmitRequest(String lenderEmail, String lenderPhone, double amount,
                                        String reason, String repaymentTerm) {

        // Get the user ID of the logged in user
        String borrowerUserId = currentUser.getUid();
        String borrowerEmail = currentUser.getEmail();

        // Pass the user info to the createTransactionID method
        String transactionId = createTransactionID(currentUser);

        // Convert repayment term to integer
        int loanLength = convertRepaymentTermToInt(repaymentTerm);

        // Pass the transaction ID and input variables to the CreateNewRequest function
        Map<String, Object> borrowData = CreateNewRequest(this, currentUser, reason,
                transactionId, "Medium",
                (float) amount, loanLength);

        // Attach the user information to the request
        if (borrowData != null) {
            borrowData.put("Borrower User ID", borrowerUserId);
            borrowData.put("Borrower Email", borrowerEmail);
            borrowData.put("Lender Email", lenderEmail);
            borrowData.put("Lender Phone", lenderPhone);
            borrowData.put("Repayment Term", repaymentTerm);
            borrowData.put("Request Status", "pending");

            // Pass the request information to the database to be used later
            newRequest(transactionId, borrowData);

            // Provide feedback to the user if the request was successfully submitted
            showSuccessMessage(transactionId, amount, repaymentTerm);

            // Clear form and close
            clearForm();
            finish();
        } else {
            // Provide feedback if request was not successfully submitted
            showErrorMessage();
        }
    }

    /**
     * Shows success message to user
     */
    private void showSuccessMessage(String transactionId, double amount, String repaymentTerm) {
        String message = "✅ Request submitted successfully!\n" +
                "Transaction ID: " + transactionId + "\n" +
                "Amount: " + NumberFormat.getCurrencyInstance(Locale.US).format(amount) + "\n" +
                "Repayment: " + repaymentTerm;

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows error message to user
     */
    private void showErrorMessage() {
        Toast.makeText(this,
                "❌ Failed to submit request. Please check your connection and try again.",
                Toast.LENGTH_SHORT).show();
    }

    // Helper Methods

    /**
     * Validates phone number format: +1 (123) - 456 - 7890
     */
    private boolean isValidPhoneNumber(String phone) {
        String digitsOnly = phone.replaceAll("[^\\d]", "");
        return digitsOnly.length() == 10;
    }

    /**
     * Parses currency amount from formatted string
     */
    private double parseAmount(String amountStr) {
        return Double.parseDouble(amountStr.replaceAll("[^\\d.]", ""));
    }

    /**
     * Converts repayment term to integer weeks
     */
    private int convertRepaymentTermToInt(String repaymentTerm) {
        switch (repaymentTerm.toLowerCase()) {
            case "4 weeks": return 4;
            case "8 weeks": return 8;
            case "3 months": return 12;
            case "6 months": return 24;
            case "12 months": return 48;
            default: return 8;
        }
    }

    /**
     * Clears all error messages
     */
    private void clearErrors() {
        lenderEmailInput.setError(null);
        lenderPhoneInput.setError(null);
        amountRequestedInput.setError(null);
        reasonInput.setError(null);
    }

    /**
     * Clears all form fields
     */
    private void clearForm() {
        lenderEmailInput.setText("");
        lenderPhoneInput.setText("");
        amountRequestedInput.setText("");
        reasonInput.setText("");
        repaymentOptions.clearCheck();
        clearErrors();
    }

    // Backend Integration Methods (Your existing methods)

    /**
     * Creates unique transaction ID
     */
    public String createTransactionID(FirebaseUser loggedInUser) {
        String baseID = loggedInUser.getUid() + "_" + System.currentTimeMillis();
        return Integer.toHexString(baseID.hashCode());
    }

    /**
     * Creates new request data structure
     */
    public Map<String, Object> CreateNewRequest(Context context, FirebaseUser borrowerName,
                                                String loanReason, String transactionID,
                                                String urgency, Float loanAmount,
                                                Integer loanLength) {

        Map<String, Object> borrowDetails = new HashMap<>();
        if (borrowerName != null) {
            borrowDetails.put("Loan ID", transactionID);
            borrowDetails.put("Loan Reason", loanReason);
            borrowDetails.put("Loan Urgency", urgency);
            borrowDetails.put("Borrower Name", borrowerName.getDisplayName());
            borrowDetails.put("Loan Amount", loanAmount);
            borrowDetails.put("Loan Term", loanLength);
            borrowDetails.put("Loan Claimed", false);
            borrowDetails.put("Loan Rejected", false);
            borrowDetails.put("Date Created", System.currentTimeMillis());
        } else {
            Toast.makeText(context, "No User Logged In", Toast.LENGTH_SHORT).show();
        }
        return borrowDetails;
    }

    /**
     * Saves request to Firebase database
     */
    public void newRequest(String transactionID, Map<String, Object> borrowData) {
        requestInfo.collection("BorrowRequests").document(transactionID).set(borrowData)
                .addOnSuccessListener(aVoid -> {
                    // Success handled in calling method
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "❌ Database error: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearForm();
    }
}