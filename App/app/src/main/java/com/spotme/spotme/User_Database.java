package com.spotme.spotme;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;


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
        userInfo.put("UserName", username);
        userInfo.put("UserEmail", email);
        userInfo.put("UserPhone", phone);
        userInfo.put("UserAddress", address);
        userInfo.put("FirstName", firstName);
        userInfo.put("LastName", lastName);
        userInfo.put("ApprovedLender", false);
        userInfo.put("MFAEnabled",null);

        Map<String, Object> userSummary = new HashMap<>();
        userSummary.put("LenderRating", 0);
        userSummary.put("BorrowerRating", 0);
        userSummary.put("ActiveLoans",new HashMap<>());
        userSummary.put("PendingLoans",new HashMap<>());
        userSummary.put("ClosedLoans",new HashMap<>());

        Map<String, Object> userLimits = new HashMap<>();
        userLimits.put("LoanLimit", 1000);
        userLimits.put("ActiveLoan Limit", 1);

        Map<String, Object> userSettings = new HashMap<>();
        userSettings.put("NotificationsOn", true);
        userSettings.put("DarkModeOn", false);

        Map<String, Object> userNotifications = new HashMap<>();
        userNotifications.put("ID", "username");
        userNotifications.put("IsSeen", false);
        userNotifications.put("Message", " ");
        userNotifications.put("CreatedAt", Timestamp.now());
        userNotifications.put("DeletedAt", Timestamp.now());

        Map<String, Object> suggestedOpportunities = new HashMap<>();
        suggestedOpportunities.put("ID", "username");
        suggestedOpportunities.put("UserName", " ");
        suggestedOpportunities.put("Amount", 0.0);
        suggestedOpportunities.put("Rating", 0.0);
        suggestedOpportunities.put("Avatar", " ");
        suggestedOpportunities.put("DateCreated", Timestamp.now());

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
}
