package com.seerbit.test.serviceImpl;

import com.seerbit.test.models.StatisticsResponse;
import com.seerbit.test.models.Transaction;
import com.seerbit.test.service.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Deque;
import java.util.LinkedList;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final int STATISTICS_WINDOW_SECONDS = 30;

    private Deque<Transaction> transactions = new LinkedList<>();
    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal max = BigDecimal.ZERO;
    private BigDecimal min = BigDecimal.ZERO;
    private long count = 0;

    public synchronized void addTransaction(Transaction transaction) {
        Instant currentTimestamp = Instant.now();
        Instant cutoffTimestamp = currentTimestamp.minusSeconds(STATISTICS_WINDOW_SECONDS);

        if (transaction.getTimestamp().isBefore(cutoffTimestamp)) {
            // Transaction is older than 30 seconds
            return;
        }

        transactions.add(transaction);
        sum = sum.add(transaction.getAmount());
        count++;

        if (max.compareTo(transaction.getAmount()) < 0) {
            max = transaction.getAmount();
        }

        if (min.compareTo(transaction.getAmount()) > 0 || min.equals(BigDecimal.ZERO)) {
            min = transaction.getAmount();
        }

        cleanupTransactions(cutoffTimestamp);
    }

    public synchronized void cleanupTransactions(Instant cutoffTimestamp) {
        while (!transactions.isEmpty() && transactions.peek().getTimestamp().isBefore(cutoffTimestamp)) {
            Transaction transaction = transactions.remove();
            sum = sum.subtract(transaction.getAmount());
            count--;

            if (transaction.getAmount().compareTo(max) == 0 || transaction.getAmount().compareTo(min) == 0) {
                recalculateMinMax();
            }
        }
    }

    public synchronized void recalculateMinMax() {
        if (!transactions.isEmpty()) {
            max = transactions.stream()
                    .map(Transaction::getAmount)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            min = transactions.stream()
                    .map(Transaction::getAmount)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
        } else {
            max = BigDecimal.ZERO;
            min = BigDecimal.ZERO;
        }
    }

    public synchronized void deleteAllTransactions() {
        transactions.clear();
        sum = BigDecimal.ZERO;
        max = BigDecimal.ZERO;
        min = BigDecimal.ZERO;
        count = 0;
    }

    public synchronized StatisticsResponse getStatistics() {
        return new StatisticsResponse(sum, calculateAverage(sum), max, min, count);
    }

    private BigDecimal calculateAverage(BigDecimal sum) {
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        return sum.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP);
    }
}

