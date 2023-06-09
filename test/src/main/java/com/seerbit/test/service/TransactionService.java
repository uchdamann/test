package com.seerbit.test.service;

import com.seerbit.test.models.StatisticsResponse;
import com.seerbit.test.models.Transaction;

import java.time.Instant;

public interface TransactionService {
    public void addTransaction(Transaction transaction);
    public void cleanupTransactions(Instant cutoffTimestamp);
    public void recalculateMinMax();
    public void deleteAllTransactions();
    public StatisticsResponse getStatistics();

}
