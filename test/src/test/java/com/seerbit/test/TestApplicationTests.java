package com.seerbit.test;

import com.seerbit.test.models.StatisticsResponse;
import com.seerbit.test.models.Transaction;
import com.seerbit.test.service.TransactionService;
import com.seerbit.test.serviceImpl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class TestApplicationTests {

	@Test
	void contextLoads() {
	}

	private TransactionService transactionService;

	@BeforeEach
	public void setup() {
		transactionService = new TransactionServiceImpl();
	}

	@Test
	public void testAddTransaction() {
		Instant currentTimestamp = Instant.now();
		Instant recentTimestamp = currentTimestamp.minusSeconds(10);

		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(10.50), currentTimestamp));
		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(20.75), recentTimestamp));
		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(15.25), currentTimestamp.minusSeconds(40)));

		StatisticsResponse statistics = transactionService.getStatistics();

		assertEquals(BigDecimal.valueOf(31.25), statistics.getSum());
		assertEquals(BigDecimal.valueOf(15.63), statistics.getAvg());
		assertEquals(BigDecimal.valueOf(20.75), statistics.getMax());
		assertEquals(BigDecimal.valueOf(10.50), statistics.getMin());
		assertEquals(2, statistics.getCount());
	}

	@Test
	public void testDeleteAllTransactions() {
		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(10.50), Instant.now()));
		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(20.75), Instant.now()));

		transactionService.deleteAllTransactions();

		StatisticsResponse statistics = transactionService.getStatistics();

		assertEquals(BigDecimal.ZERO, statistics.getSum());
		assertEquals(BigDecimal.ZERO, statistics.getAvg());
		assertEquals(BigDecimal.ZERO, statistics.getMax());
		assertEquals(BigDecimal.ZERO, statistics.getMin());
		assertEquals(0, statistics.getCount());
	}

	@Test
	public void testCleanupTransactions() {
		Instant cutoffTimestamp = Instant.now().minusSeconds(30);

		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(10.50), Instant.now()));
		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(20.75), Instant.now()));
		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(15.25), Instant.now().minusSeconds(40)));

		transactionService.cleanupTransactions(cutoffTimestamp);

		StatisticsResponse statistics = transactionService.getStatistics();

		assertEquals(BigDecimal.valueOf(31.25), statistics.getSum());
		assertEquals(BigDecimal.valueOf(15.63), statistics.getAvg());
		assertEquals(BigDecimal.valueOf(20.75), statistics.getMax());
		assertEquals(BigDecimal.valueOf(10.50), statistics.getMin());
		assertEquals(2, statistics.getCount());
	}

	@Test
	public void testRecalculateMinMax() {
		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(10.50), Instant.now()));
		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(20.75), Instant.now()));
		transactionService.addTransaction(new Transaction(BigDecimal.valueOf(15.25), Instant.now().minusSeconds(40)));

		transactionService.recalculateMinMax();

		StatisticsResponse statistics = transactionService.getStatistics();

		assertEquals(BigDecimal.valueOf(31.25), statistics.getSum());
		assertEquals(BigDecimal.valueOf(15.63), statistics.getAvg());
		assertEquals(BigDecimal.valueOf(20.75), statistics.getMax());
		assertEquals(BigDecimal.valueOf(10.50), statistics.getMin());
		assertEquals(2, statistics.getCount());
	}

}
