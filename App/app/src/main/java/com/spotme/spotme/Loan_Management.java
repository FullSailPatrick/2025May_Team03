package com.spotme.spotme;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Loan_Management {

    // Firestore instance for managing loan records
    private final FirebaseFirestore loanInfo = FirebaseFirestore.getInstance();

    /**
     * Adds a new loan document to the "Loans" collection in Firestore.
     * @param loanID The unique identifier for the loan (used as the document name)
     * @param loanData Map containing all loan details and metadata
     */
    public void addNewLoan(String loanID, Map<String,Object> loanData){
        loanInfo.collection("Loans").document(loanID).set(loanData).
                addOnSuccessListener(aVoid -> {}).addOnFailureListener(e -> {
                });
    }

    /**
     * Builds a map containing details for a new loan.
     * @param loanName Name/title of the loan
     * @param lenderName Name of the lender
     * @param borrowerName Name of the borrower
     * @param loanAmount Amount for the loan
     * @param interestRate Interest rate for the loan
     * @param dueDate Due date for the loan repayment (string format)
     * @param loanLength Loan term/length (number of days, months, etc.)
     * @param isPending Whether the loan is pending approval
     * @param lenderApproved Whether the lender has approved the loan
     * @param borrowerApproved Whether the borrower has approved the loan
     * @param loanComplete Whether the loan has been completed/closed
     * @param isLender Whether the current user is the lender
     * @param rating Loan rating (feedback or trust metric)
     * @return Map containing all the loan details to be stored in Firestore
     */
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

        // Create map for loan details with all relevant fields
        Map<String, Object> loanDetails = new HashMap<>();
        loanDetails.put("LoanName", loanName);                // Name/title of the loan
        loanDetails.put("LenderName", lenderName);            // Name of the lender
        loanDetails.put("BorrowerName", borrowerName);        // Name of the borrower
        loanDetails.put("LoanAmount", loanAmount);            // Loan amount
        loanDetails.put("InterestRate", interestRate);        // Interest rate
        loanDetails.put("LoanTerm", loanLength);              // Duration/length of the loan
        loanDetails.put("LoanDue", dueDate);                  // Loan due date
        loanDetails.put("LoanPending", isPending);            // Pending approval status
        loanDetails.put("LenderApproved", lenderApproved);    // Lender approval status
        loanDetails.put("BorrowerApproved", borrowerApproved);// Borrower approval status
        loanDetails.put("LoanComplete", loanComplete);        // Whether the loan is completed
        loanDetails.put("UserIsLender", isLender);            // Indicates if the current user is the lender
        loanDetails.put("LoanRating", rating);                // Rating associated with the loan
        loanDetails.put("DateCreated", Timestamp.now());      // Timestamp of loan creation

        return loanDetails;
    }
}