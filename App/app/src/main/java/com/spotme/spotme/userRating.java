/**
 * Preliminary User Rating Formula for SpotMe App
 *
 * v 0.2
 *
 * Simple calculations for user ratings with weighted formula
 * Modified to start new users with zero rating until they complete transactions
 *
 * Calculate Reliability, Communication, TransactionSmoothness before
 * calculating weighted average
 */
public class UserRating {

    /**
     * Calculates reliability score based on user performance
     *
     * @param daysLate                - number of days late (0 if on time)
     * @param wasCompleted            - true if obligation was completed, false if not
     * @param previousReliabilityRate - historical reliability rate (0.0 to 1.0)
     * @param hasTransactionHistory   - true if user has completed transactions before
     * @return reliability score from 1.0 to 5.0, or 0.0 if no history
     */
    public static double calculateReliability(int daysLate, boolean wasCompleted, double previousReliabilityRate, boolean hasTransactionHistory) {

        // New users with no transaction history get 0.0
        if (!hasTransactionHistory) {
            return 0.0;
        }

        double score = 5.0;

        // Deduct points for being late
        if (daysLate > 0) {
            if (daysLate <= 7) {
                score = score - 0.5;
            } else if (daysLate <= 30) {
                score = score - 1.5;
            } else if (daysLate <= 90) {
                score = score - 2.5;
            } else {
                score = score - 3.5;
            }
        }

        // Major deduction if not completed
        if (!wasCompleted) {
            score = score - 3.0;
        }

        // Factor in historical performance
        double historyPenalty = (1.0 - previousReliabilityRate) * 2.0;
        score = score - historyPenalty;

        // Keep score between 1.0 and 5.0
        if (score > 5.0) {
            score = 5.0;
        }
        if (score < 1.0) {
            score = 1.0;
        }

        // Round to 2 decimal places
        score = Math.round(score * 100.0) / 100.0;

        return score;
    }

    /**
     * Calculates communication score based on interaction quality
     *
     * @param avgResponseTimeHours - average response time in hours
     * @param messageCount         - total number of messages exchanged
     * @param wasProactive         - true if user initiated communication proactively
     * @param hadConflicts         - true if there were communication conflicts
     * @param hasTransactionHistory - true if user has completed transactions before
     * @return communication score from 1.0 to 5.0, or 0.0 if no history
     */
    public static double calculateCommunication(double avgResponseTimeHours, int messageCount, boolean wasProactive,
                                                boolean hadConflicts, boolean hasTransactionHistory) {

        // New users with no transaction history get 0.0
        if (!hasTransactionHistory) {
            return 0.0;
        }

        double score = 3.0; // Start with neutral score

        // Response time scoring
        if (avgResponseTimeHours <= 1) {
            score = score + 1.5;
        } else if (avgResponseTimeHours <= 6) {
            score = score + 1.0;
        } else if (avgResponseTimeHours <= 24) {
            score = score + 0.5;
        } else if (avgResponseTimeHours <= 72) {
            score = score - 0.5;
        } else {
            score = score - 1.5;
        }

        // Message frequency scoring
        if (messageCount >= 10) {
            score = score + 0.5;
        } else if (messageCount >= 5) {
            score = score + 0.2;
        } else if (messageCount < 2) {
            score = score - 0.5;
        }

        // Proactive communication bonus
        if (wasProactive) {
            score = score + 0.3;
        }

        // Conflict penalty
        if (hadConflicts) {
            score = score - 1.0;
        }

        // Keep score between 1.0 and 5.0
        if (score > 5.0) {
            score = 5.0;
        }
        if (score < 1.0) {
            score = 1.0;
        }

        // Round to 2 decimal places
        score = Math.round(score * 100.0) / 100.0;

        return score;
    }

    /**
     * Calculates transaction smoothness score based on process efficiency
     *
     * @param setupTimeHours     - time to set up the transaction in hours
     * @param hadTechnicalIssues - true if there were technical problems
     * @param requiredSupport    - true if customer support was needed
     * @param changesRequested   - number of changes/modifications requested
     * @param hasTransactionHistory - true if user has completed transactions before
     * @return transaction smoothness score from 1.0 to 5.0, or 0.0 if no history
     */
    public static double calculateTransaction(double setupTimeHours, boolean hadTechnicalIssues,
                                              boolean requiredSupport, int changesRequested, boolean hasTransactionHistory) {

        // New users with no transaction history get 0.0
        if (!hasTransactionHistory) {
            return 0.0;
        }

        double score = 4.0; // Start with good score

        // Setup time scoring
        if (setupTimeHours <= 0.5) {
            score = score + 1.0;
        } else if (setupTimeHours <= 2) {
            score = score + 0.5;
        } else if (setupTimeHours <= 6) {
            score = score + 0.0;
        } else if (setupTimeHours <= 24) {
            score = score - 0.5;
        } else {
            score = score - 1.5;
        }

        // Technical issues penalty
        if (hadTechnicalIssues) {
            score = score - 1.0;
        }

        // Support requirement penalty
        if (requiredSupport) {
            score = score - 0.5;
        }

        // Changes penalty
        score = score - (changesRequested * 0.3);

        // Keep score between 1.0 and 5.0
        if (score > 5.0) {
            score = 5.0;
        }
        if (score < 1.0) {
            score = 1.0;
        }

        // Round to 2 decimal places
        score = Math.round(score * 100.0) / 100.0;

        return score;
    }

    /**
     * Calculates user rating using weighted formula
     * Formula: UserRating = 0.5 * reliability + 0.25 * communication + 0.25 * transaction
     * Returns 0.0 for users with no transaction history
     *
     * @param reliability   - score from 1.0 to 5.0 for user reliability (or 0.0 for no history)
     * @param communication - score from 1.0 to 5.0 for communication quality (or 0.0 for no history)
     * @param transaction   - score from 1.0 to 5.0 for transaction smoothness (or 0.0 for no history)
     * @return final user rating from 1.0 to 5.0, or 0.0 if no transaction history
     */
    public static double calculateRating(double reliability, double communication, double transaction) {

        // If any score is 0.0 (no history), return 0.0
        if (reliability == 0.0 || communication == 0.0 || transaction == 0.0) {
            return 0.0;
        }

        // Check if inputs are valid
        if (reliability < 1.0 || reliability > 5.0) {
            return 0.0;
        }

        if (communication < 1.0 || communication > 5.0) {
            return 0.0;
        }

        if (transaction < 1.0 || transaction > 5.0) {
            return 0.0;
        }

        // Calculate weighted rating
        double result = (0.5 * reliability) + (0.25 * communication) + (0.25 * transaction);

        // Round to 2 decimal places
        result = Math.round(result * 100.0) / 100.0;

        return result;
    }

    /**
     * Calculates complete user rating from transaction data
     *
     * @param daysLate                - days late
     * @param wasCompleted            - was obligation completed
     * @param previousReliabilityRate - historical reliability rate
     * @param avgResponseTimeHours    - average response time
     * @param messageCount            - number of messages
     * @param wasProactive            - was proactive in communication
     * @param hadConflicts            - had communication conflicts
     * @param setupTimeHours          - setup time in hours
     * @param hadTechnicalIssues      - had technical issues
     * @param requiredSupport         - required customer support
     * @param changesRequested        - number of changes requested
     * @param hasTransactionHistory   - true if user has completed transactions before
     * @return final user rating from 1.0 to 5.0, or 0.0 if no transaction history
     */
    public static double calculateCompleteRating(int daysLate, boolean wasCompleted, double previousReliabilityRate,
                                                 double avgResponseTimeHours, int messageCount, boolean wasProactive, boolean hadConflicts,
                                                 double setupTimeHours, boolean hadTechnicalIssues, boolean requiredSupport, int changesRequested,
                                                 boolean hasTransactionHistory) {

        double reliability = calculateReliability(daysLate, wasCompleted, previousReliabilityRate, hasTransactionHistory);
        double communication = calculateCommunication(avgResponseTimeHours, messageCount, wasProactive, hadConflicts, hasTransactionHistory);
        double transaction = calculateTransaction(setupTimeHours, hadTechnicalIssues, requiredSupport,
                changesRequested, hasTransactionHistory);

        return calculateRating(reliability, communication, transaction);
    }

    /**
     * Calculates average rating from multiple individual ratings
     * Excludes zero ratings (no history) from the average calculation
     *
     * @param ratings - array of individual rating scores
     * @return average rating
     */
    public static double calculateAverage(double[] ratings) {

        if (ratings.length == 0) {
            return 0.0;
        }

        double total = 0.0;
        int validRatings = 0;

        // Add up all valid ratings (excluding 0.0 which means no history)
        for (int i = 0; i < ratings.length; i++) {
            if (ratings[i] >= 1.0 && ratings[i] <= 5.0) {
                total = total + ratings[i];
                validRatings++;
            }
        }

        if (validRatings == 0) {
            return 0.0;
        }

        double average = total / validRatings;

        // Round to 2 decimal places
        average = Math.round(average * 100.0) / 100.0;

        return average;
    }

    /**
     * Checks if a rating is valid (between 1.0 and 5.0, or 0.0 for no history)
     *
     * @param rating - rating to check
     * @return true if valid, false if invalid
     */
    public static boolean isValidRating(double rating) {
        return (rating == 0.0 || (rating >= 1.0 && rating <= 5.0));
    }

    /**
     * Gets the total number of users with transaction history from ratings array
     *
     * @param ratings - array of individual rating scores
     * @return count of users with ratings > 0.0
     */
    public static int getExperiencedUserCount(double[] ratings) {
        int count = 0;
        for (int i = 0; i < ratings.length; i++) {
            if (ratings[i] > 0.0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the total number of new users (zero ratings) from ratings array
     *
     * @param ratings - array of individual rating scores
     * @return count of users with 0.0 ratings
     */
    public static int getNewUserCount(double[] ratings) {
        int count = 0;
        for (int i = 0; i < ratings.length; i++) {
            if (ratings[i] == 0.0) {
                count++;
            }
        }
        return count;
    }
}