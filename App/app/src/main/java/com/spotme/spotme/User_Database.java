package com.spotme.spotme;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User_Database {

    private final FirebaseFirestore userInfo = FirebaseFirestore.getInstance();

    public void addUser(String userID, Map<String,Object> userData){
        userInfo.collection("Users").document(userID).set(userData).
                addOnSuccessListener(aVoid -> {}).addOnFailureListener(e -> {
        });
    }
    public Map<String,Object> buildUserData(TextInputEditText _userEmail,
                                            TextInputEditText _userName,
                                            EditText _userFirstName,
                                            EditText _userLastName,
                                            EditText _userPhone,
                                            EditText _userAddress) {
        String email = String.valueOf(_userEmail.getText());
        String username = String.valueOf(_userName.getText());
        String firstName = String.valueOf(_userFirstName.getText());
        String lastName = String.valueOf(_userLastName.getText());
        String phone = String.valueOf(_userPhone.getText());
        String address = String.valueOf(_userAddress.getText());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("User Name", username);
        userInfo.put("User Email", email);
        userInfo.put("User Phone", phone);
        userInfo.put("User Address", address);
        userInfo.put("First Name", firstName);
        userInfo.put("Last Name", lastName);
        userInfo.put("Approved Lender", false);

        Map<String, Object> userSummary = new HashMap<>();
        userSummary.put("Lender Rating", 0);
        userSummary.put("Borrower Rating", 0);
        userSummary.put("Active Loans",new HashMap<>());
        userSummary.put("Pending Loans",new HashMap<>());
        userSummary.put("Closed Loans",new HashMap<>());

        Map<String, Object> userLimits = new HashMap<>();
        userLimits.put("Loan Limit", 1000);
        userLimits.put("Active Loan Limit", 1);

        Map<String, Object> userSettings = new HashMap<>();
        userSettings.put("Notifications On", true);
        userSettings.put("Dark Mode On", false);

        Map<String, Object> userNotifications = new HashMap<>();
        userNotifications.put("ID", "username");
        userNotifications.put("Is Seen", false);
        userNotifications.put("Message", " ");
        userNotifications.put("Created At", Timestamp.now());
        userNotifications.put("Deleted At", Timestamp.now());

        Map<String, Object> suggestedOpportunities = new HashMap<>();
        suggestedOpportunities.put("ID", "username");
        suggestedOpportunities.put("User Name", " ");
        suggestedOpportunities.put("Amount", 0.0);
        suggestedOpportunities.put("Rating", 0.0);
        suggestedOpportunities.put("Avatar", " ");
        suggestedOpportunities.put("Date Created", Timestamp.now());

        Map<String, Object> userData = new HashMap<>();
        userData.put("User Information", userInfo);
        userData.put("User Settings", userSettings);
        userData.put("User Summary", userSummary);
        userData.put("User Limits", userLimits);
        userData.put("User Notifications",userNotifications);
        userData.put("Suggested Opportunities", suggestedOpportunities);
        userData.put("Date Created", Timestamp.now());

        return userData;
    }
    public Map<String,Object> CreateNewLoan(String loanName,
                                       String lenderName,
                                       String borrowerName,
                                       Float loanAmount,
                                       Float interestRate,
                                       Date dueDate,
                                       Integer loanLength,
                                       Boolean isPending,
                                       Boolean lenderApproved,
                                       Boolean borrowerApproved,
                                       Boolean loanComplete,
                                       Boolean isLender,
                                       Float rating
                                       ){

        Map<String, Object> loanDetails = new HashMap<>();
        loanDetails.put("Loan Name", loanName);
        loanDetails.put("Lender Name", lenderName);
        loanDetails.put("Borrower Name", borrowerName);
        loanDetails.put("Loan Amount", loanAmount);
        loanDetails.put("Interest Rate", interestRate);
        loanDetails.put("Loan Term", loanLength);
        loanDetails.put("Loan Due", dueDate);
        loanDetails.put("Loan Pending", isPending);
        loanDetails.put("Lender Approved", lenderApproved);
        loanDetails.put("Borrower Approved", borrowerApproved);
        loanDetails.put("Loan Complete", loanComplete);
        loanDetails.put("User Is Lender", isLender);
        loanDetails.put("Loan Rating", rating);
        loanDetails.put("Date Created", Timestamp.now());

        return loanDetails;
    }
}
