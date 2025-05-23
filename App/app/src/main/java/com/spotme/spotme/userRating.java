/**
 * Preliminary User Rating Formula for SpotMe App
 *
 * v 0.1
 *
 * Simple calculations for user ratings with weighted formula
 *
 * Calculate Reliability, Communicaiton, TransactionSmoothness before
 * calculating wnow i need to push to teighted average
 */
public class UserRating {

    /**
     * Calculates reliability score based on user performance
     *
     * @param daysLate                - number of days late (0 if on time)
     * @param wasCompleted            - true if obligation was completed, false if not
     * @param previousReliabilityRate - historical reliability rate (0.0 to 1.0)
     * @return reliability score from 1.0 to 5.0
     */
    public static double calculateReliability(int daysLate, boolean wasCompleted, double previousReliabilityRate) {

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
     * @return communication score from 1.0 to 5.0
     */
    public static double calculateCommunication(double avgResponseTimeHours, int messageCount, boolean wasProactive,
            boolean hadConflicts) {

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
     * @return transaction smoothness score from 1.0 to 5.0
     */
    public static double calculateTransaction(double setupTimeHours, boolean hadTechnicalIssues,
            boolean requiredSupport, int changesRequested) {

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
     * Formula: UserRating = 0.5 * reliability + 0.25 * communication + 0.25 *
     * transaction
     *
     * @param reliability   - score from 1.0 to 5.0 for user reliability
     * @param communication - score from 1.0 to 5.0 for communication quality
     * @param transaction   - score from 1.0 to 5.0 for transaction smoothness
     * @return final user rating from 1.0 to 5.0
     */
    public static double calculateRating(double reliability, double communication, double transaction) {

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
     * @return final user rating from 1.0 to 5.0
     */
    public static double calculateCompleteRating(int daysLate, boolean wasCompleted, double previousReliabilityRate,
            double avgResponseTimeHours, int messageCount, boolean wasProactive, boolean hadConflicts,
            double setupTimeHours, boolean hadTechnicalIssues, boolean requiredSupport, int changesRequested) {

        double reliability = calculateReliability(daysLate, wasCompleted, previousReliabilityRate);
        double communication = calculateCommunication(avgResponseTimeHours, messageCount, wasProactive, hadConflicts);
        double transaction = calculateTransaction(setupTimeHours, hadTechnicalIssues, requiredSupport,
                changesRequested);

        return calculateRating(reliability, communication, transaction);
    }

    /**
     * Calculates average rating from multiple individual ratings
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

        // Add up all valid ratings
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
     * Checks if a rating is valid (between 1.0 and 5.0)
     *
     * @param rating - rating to check
     * @return true if valid, false if invalid
     */
    public static boolean isValidRating(double rating) {
        return (rating >= 1.0 && rating <= 5.0);
    }
}