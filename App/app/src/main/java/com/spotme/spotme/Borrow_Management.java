package com.spotme.spotme;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class Borrow_Management {
    private final FirebaseFirestore requestInfo = FirebaseFirestore.getInstance();

    public void newRequest(String transactionID, Map<String,Object> borrowData){
        requestInfo.collection("BorrowRequests").document(transactionID).set(borrowData).
                addOnSuccessListener(aVoid -> {}).addOnFailureListener(e -> {
                });
    }

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
            borrowDetails.put("LoanID", transactionID);
            borrowDetails.put("LoanReason", loanReason);
            borrowDetails.put("LoanUrgency", urgency);
            borrowDetails.put("BorrowerName", borrowerName.getDisplayName());
            borrowDetails.put("LoanAmount", loanAmount);
            borrowDetails.put("LoanTerm", loanLength);
            borrowDetails.put("LoanClaimed", false);
            borrowDetails.put("LoanRejected", false);
            borrowDetails.put("DateCreated", Timestamp.now());
        }
        else {
            Toast.makeText(context, "No User Logged In", Toast.LENGTH_SHORT).show();
        }
        return borrowDetails;
    }

    public String createTransactionID(FirebaseUser loggedInUser){
        String baseID = loggedInUser.getUid()+ "_" + System.currentTimeMillis();
        return Integer.toHexString(baseID.hashCode());
    }
}
