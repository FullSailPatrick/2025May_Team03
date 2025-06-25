package com.spotme.spotme;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class User_Database {

    // Reference to Firestore database for user information
    private final FirebaseFirestore userInfo = FirebaseFirestore.getInstance();

    /**
     * Adds a new user document to the "Users" collection in Firestore.
     * @param userID The UID of the user (document name)
     * @param userData The user's information as a map
     */
    public void addUser(String userID, Map<String,Object> userData){
        userInfo.collection("Users").document(userID).set(userData).
                addOnSuccessListener(aVoid -> {}).addOnFailureListener(e -> {
                });
    }

    /**
     * Builds the user data map for Firestore storage from EditText fields.
     * @param _userEmail User's email input field
     * @param _userName User's username input field
     * @param _userFirstName User's first name input field
     * @param _userLastName User's last name input field
     * @param _userPhone User's phone number input field
     * @param _userAddress User's address input field
     * @return A nested map representing the user's full data structure
     */
    public Map<String,Object> buildUserData(TextInputEditText _userEmail,
                                            TextInputEditText _userName,
                                            EditText _userFirstName,
                                            EditText _userLastName,
                                            EditText _userPhone,
                                            EditText _userAddress) {
        // Extract values from EditText fields
        String email = String.valueOf(_userEmail.getText());
        String username = String.valueOf(_userName.getText());
        String firstName = String.valueOf(_userFirstName.getText());
        String lastName = String.valueOf(_userLastName.getText());
        String phone = String.valueOf(_userPhone.getText());
        String address = String.valueOf(_userAddress.getText());

        // Build map for user's basic information
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("UserName", username);
        userInfo.put("UserEmail", email);
        userInfo.put("UserPhone", phone);
        userInfo.put("UserAddress", address);
        userInfo.put("FirstName", firstName);
        userInfo.put("LastName", lastName);
        userInfo.put("ApprovedLender", false); // Whether user is approved to lend
        userInfo.put("MFAEnabled",null); // Placeholder for multi-factor auth flag

        // Build map for user's summary information
        Map<String, Object> userSummary = new HashMap<>();
        userSummary.put("LenderRating", 0);
        userSummary.put("BorrowerRating", 0);
        userSummary.put("ActiveLoans",new HashMap<>()); // Placeholder for active loans
        userSummary.put("PendingLoans",new HashMap<>()); // Placeholder for pending loans
        userSummary.put("ClosedLoans",new HashMap<>()); // Placeholder for closed loans

        // Build map for user's loan limits
        Map<String, Object> userLimits = new HashMap<>();
        userLimits.put("LoanLimit", 1000); // Maximum loan amount
        userLimits.put("ActiveLoanLimit", 1); // Maximum number of active loans

        // Build map for user's app settings
        Map<String, Object> userSettings = new HashMap<>();
        userSettings.put("NotificationsOn", true); // Whether notifications are enabled
        userSettings.put("DarkModeOn", false); // Whether dark mode is enabled

        // Build map for user notifications (sample data)
        Map<String, Object> userNotifications = new HashMap<>();
        userNotifications.put("ID", "username");
        userNotifications.put("IsSeen", false);
        userNotifications.put("Message", " ");
        userNotifications.put("CreatedAt", Timestamp.now());
        userNotifications.put("DeletedAt", Timestamp.now());

        // Build map for suggested loan opportunities (sample data)
        Map<String, Object> suggestedOpportunities = new HashMap<>();
        suggestedOpportunities.put("ID", "username");
        suggestedOpportunities.put("UserName", " ");
        suggestedOpportunities.put("Amount", 0.0);
        suggestedOpportunities.put("Rating", 0.0);
        suggestedOpportunities.put("Avatar", " ");
        suggestedOpportunities.put("DateCreated", Timestamp.now());

        // Combine all sections into one nested map for Firestore
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