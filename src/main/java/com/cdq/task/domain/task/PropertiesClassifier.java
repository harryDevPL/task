package com.cdq.task.domain.task;

class PropertiesClassifier {

    public ClassificationType classifyField(String oldValue, String newValue) {
        if (isNullOrBlank(oldValue) && !isNullOrBlank(newValue)) {
            return ClassificationType.ADDED;
        }
        if (!isNullOrBlank(oldValue) && isNullOrBlank(newValue)) {
            return ClassificationType.DELETED;
        }
        if (isNullOrBlank(oldValue) && isNullOrBlank(newValue)) {
            return ClassificationType.HIGH;
        }

        double dissimilarity = calculateDissimilarity(oldValue, newValue);
        double similarity = 1.0 - dissimilarity;

        if (similarity > 0.9) {
            return ClassificationType.HIGH;
        } else if (similarity >= 0.4) {
            return ClassificationType.MEDIUM;
        } else {
            return ClassificationType.LOW;
        }
    }

    private double calculateDissimilarity(String oldValue, String newValue) {
        int diff = calculateCharacterDifferences(oldValue, newValue);
        int maxLength = Math.max(oldValue.length(), newValue.length());

        if (maxLength == 0) {
            return 0.0;
        }

        return (double) diff / maxLength;
    }

    private int calculateCharacterDifferences(String oldValue, String newValue) {
        int[][] dp = new int[oldValue.length() + 1][newValue.length() + 1];

        for (int i = 0; i <= oldValue.length(); i++) {
            for (int j = 0; j <= newValue.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (oldValue.charAt(i - 1) == newValue.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j],
                        Math.min(dp[i][j - 1],
                            dp[i - 1][j - 1]));
                }
            }
        }

        return dp[oldValue.length()][newValue.length()];
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}
