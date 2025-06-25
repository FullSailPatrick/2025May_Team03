package com.spotme.spotme;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Loan_Management {

    private final FirebaseFirestore loanInfo = FirebaseFirestore.getInstance();

    public void addNewLoan(String loanID, Map<String,Object> loanData){
        loanInfo.collection("Loans").document(loanID).set(loanData).
                addOnSuccessListener(aVoid -> {}).addOnFailureListener(e -> {
                });
    }

    public Map<String,Object> CreateNewLoan(String loanName,
                                            String lenderName,
                                            String borrowerName,
                                            Float loanAmount,
                                            Float interestRate,
                                            String dueDate,
                                            Integer loanLength,
                                            Boolean isPending,
                                            Boolean lenderApproved,
                                            Boolean borrowerApproved,
                                            Boolean loanComplete,
                                            Boolean isLender,
                                            Float rating
    ){

        Map<String, Object> loanDetails = new HashMap<>();
        loanDetails.put("LoanName", loanName);
        loanDetails.put("LenderName", lenderName);
        loanDetails.put("BorrowerName", borrowerName);
        loanDetails.put("LoanAmount", loanAmount);
        loanDetails.put("InterestRate", interestRate);
        loanDetails.put("LoanTerm", loanLength);
        loanDetails.put("LoanDue", dueDate);
        loanDetails.put("LoanPending", isPending);
        loanDetails.put("LenderApproved", lenderApproved);
        loanDetails.put("BorrowerApproved", borrowerApproved);
        loanDetails.put("LoanComplete", loanComplete);
        loanDetails.put("UserIsLender", isLender);
        loanDetails.put("LoanRating", rating);
        loanDetails.put("DateCreated", Timestamp.now());

        return loanDetails;
    }
}
