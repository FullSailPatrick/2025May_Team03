package com.spotme.spotme;

/**
 * User Rating Formula for SpotMe App
 *
 * v 0.3.1
 *
 * Simplified rating calculation - new users start with 5.0 rating
 */
public class UserRating {

    /**
     * Calculates user rating based on transaction history
     * New users with 0 transactions get maximum 5.0 rating
     *
     * @param transactionCount - number of completed transactions
     * @return user rating of 5.0
     */
    public static double calculateRating(int transactionCount) {

        // New users with 0 transactions get maximum rating
        if (transactionCount == 0) {
            return 5.0;
        }

        // For users with transaction history, you can add more complex logic here
        // For now, returning 5.0 for all users
        return 5.0;
    }
}