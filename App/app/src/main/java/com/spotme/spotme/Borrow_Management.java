package com.spotme.spotme;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Borrow_Management {
    // Firestore instance for handling borrow requests
    private final FirebaseFirestore requestInfo = FirebaseFirestore.getInstance();

    /**
     * Adds a new borrow request to the "BorrowRequests" collection in Firestore.
     * @param transactionID Unique ID for the borrow request (document name)
     * @param borrowData Map containing details of the borrow request
     */
    public void newRequest(String transactionID, Map<String,Object> borrowData){
        requestInfo.collection("BorrowRequests").document(transactionID).set(borrowData).
                addOnSuccessListener(aVoid -> {}).addOnFailureListener(e -> {
                });
    }

    /**
     * Builds a map containing details for a new borrow request.
     * @param context Android context (usually Activity or Fragment)
     * @param borrowerName FirebaseUser object for the borrower
     * @param loanReason Reason for borrowing
     * @param transactionID Unique transaction ID
     * @param urgency Urgency of the loan (e.g., "High", "Low")
     * @param loanAmount Amount requested for the loan
     * @param loanLength Length/term of the loan (in whatever units you define)
     * @return Map containing all details to be stored in Firestore
     */
    public Map<String,Object> CreateNewRequest(Context context, // You will need to pass the activity class name
                                               FirebaseUser borrowerName, // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                               String loanReason,
                                               String transactionID,
                                               String urgency,
                                               Float loanAmount,
                                               Integer loanLength

    ){

        Map<String, Object> borrowDetails = new HashMap<>();
        if(borrowerName!= null)
        {
            borrowDetails.put("LoanID", transactionID); // Unique ID for this loan request
            borrowDetails.put("LoanReason", loanReason); // Reason for requesting loan
            borrowDetails.put("LoanUrgency", urgency);   // Urgency level
            borrowDetails.put("BorrowerName", borrowerName.getDisplayName()); // Borrower's name from FirebaseUser
            borrowDetails.put("LoanAmount", loanAmount); // Amount requested
            borrowDetails.put("LoanTerm", loanLength);   // Loan duration/term
            borrowDetails.put("LoanClaimed", false);     // Initially, loan is not claimed
            borrowDetails.put("LoanRejected", false);    // Initially, loan is not rejected
            borrowDetails.put("DateCreated", Timestamp.now()); // Timestamp for record creation
        }
        else {
            // Notify user that no borrower is logged in
            Toast.makeText(context, "No User Logged In", Toast.LENGTH_SHORT).show();
        }
        return borrowDetails;
    }

    /**
     * Generates a unique transaction ID based on the user ID and current time.
     * @param loggedInUser FirebaseUser object representing the currently logged-in user
     * @return A unique hex string to be used as the transaction ID
     */
    public String createTransactionID(FirebaseUser loggedInUser){
        String baseID = loggedInUser.getUid()+ "_" + System.currentTimeMillis();
        return Integer.toHexString(baseID.hashCode());
    }
}