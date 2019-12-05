package com.rabobank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.rabobank.model.Record;
import com.rabobank.model.ResultRecord;

@SpringBootTest
public class ValidatorServiceImplTest {

	private static final String RECORD_DESCRIPTION = "Tickets for Willem Theuß";
	private static final String RECORD_ACCT_NO_ONE = "NL43AEGO0773393871";
	private static final String RECORD_ACCT_NO_TWO ="NL69ABNA0433647324";
	
	/**
	 * Scenario : Positive 
	 * scenario : Duplicate check in given Rabobank Customer
	 * Statement
	 */
	@Test
	public void getDuplicateRecordsTestCaseWithDuplilcate() {
		List<Record> inputList = Arrays.asList(
				new Record(179430, RECORD_ACCT_NO_TWO, 66.72, -41.74, RECORD_DESCRIPTION, 24.98),
				new Record(179430, RECORD_ACCT_NO_ONE, 16.52, +43.09, RECORD_DESCRIPTION, 59.61));
		ValidatorService validatorServiceImpl = new ValidatorServiceImpl();
		List<ResultRecord> duplicateRecords = validatorServiceImpl.getDuplicateRecordsByRef(inputList);
		assertEquals(inputList.size(), duplicateRecords.size());

	}

	/**
	 * Scenario : Negative 
	 * scenario : Duplicate check in given Rabobank Customer
	 * Statement
	 */
	@Test
	public void getDuplicateRecordsTestCaseWithOutDuplilcate() {
		List<Record> inputList = Arrays.asList(
				new Record(172823, RECORD_ACCT_NO_TWO, 66.72, -41.74, RECORD_DESCRIPTION, 24.98),
				new Record(179430, RECORD_ACCT_NO_ONE, 16.52, +43.09, RECORD_DESCRIPTION, 59.61));
		ValidatorService validatorServiceImpl = new ValidatorServiceImpl();
		List<ResultRecord> duplicateRecords = validatorServiceImpl.getDuplicateRecordsByRef(inputList);
		assertEquals(0, duplicateRecords.size());

	}

	/**
	 * Scenario : Positive 
	 * scenario : EndBalance validation in given Rabobank Customer
	 * Statement
	 */
	@Test
	public void getEndBalanceErrorRecordsTestCaseWithWrongValue() {
		List<Record> inputList = Arrays.asList(
				new Record(179430, RECORD_ACCT_NO_TWO, 66.72, -41.74, RECORD_DESCRIPTION, 24.98),
				new Record(179430, RECORD_ACCT_NO_ONE, 16.52, +43.09, RECORD_DESCRIPTION, 59.80));
		ValidatorService validatorServiceImpl = new ValidatorServiceImpl();
		List<ResultRecord> endBalanceErrorRecords = validatorServiceImpl.getEndBalanceErrorRecords(inputList);
		assertEquals(inputList.size(), endBalanceErrorRecords.size());

	}

	/**
	 * Scenario : Negative 
	 * scenario : EndBalance validation in given Rabobank Customer
	 * Statement
	 */
	@Test
	public void getEndBalanceErrorRecordsTestCaseWithProperValue() {
		List<Record> inputList = Arrays.asList(
				new Record(179430, RECORD_ACCT_NO_TWO, 66.72, -41.74, RECORD_DESCRIPTION, 108.46),
				new Record(179430, RECORD_ACCT_NO_ONE, 16.52, +43.09, RECORD_DESCRIPTION, -26.57));
		ValidatorService validatorServiceImpl = new ValidatorServiceImpl();
		List<ResultRecord> endBalanceErrorRecords = validatorServiceImpl.getEndBalanceErrorRecords(inputList);
		assertEquals(0, endBalanceErrorRecords.size());
	}
}
