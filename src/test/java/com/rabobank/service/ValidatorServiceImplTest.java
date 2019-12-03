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

	/**
	 * Scenario : Positive 
	 * scenario : Duplicate check in given Rabobank Customer
	 * Statement
	 */
	@Test
	public void getDuplicateRecordsTestCaseWithDuplilcate() {
		List<Record> inputList = Arrays.asList(
				new Record(179430, "NL69ABNA0433647324", 66.72, -41.74, "Tickets for Willem Theuß", 24.98),
				new Record(179430, "NL43AEGO0773393871", 16.52, +43.09, "Tickets for Willem Theuß", 59.61));
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
				new Record(172823, "NL69ABNA0433647324", 66.72, -41.74, "Tickets for Willem Theuß", 24.98),
				new Record(179430, "NL43AEGO0773393871", 16.52, +43.09, "Tickets for Willem Theuß", 59.61));
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
				new Record(179430, "NL69ABNA0433647324", 66.72, -41.74, "Tickets for Willem Theuß", 24.98),
				new Record(179430, "NL43AEGO0773393871", 16.52, +43.09, "Tickets for Willem Theuß", 59.80));
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
				new Record(179430, "NL69ABNA0433647324", 66.72, -41.74, "Tickets for Willem Theuß", 108.46),
				new Record(179430, "NL43AEGO0773393871", 16.52, +43.09, "Tickets for Willem Theuß", -26.57));
		ValidatorService validatorServiceImpl = new ValidatorServiceImpl();
		List<ResultRecord> endBalanceErrorRecords = validatorServiceImpl.getEndBalanceErrorRecords(inputList);
		assertEquals(0, endBalanceErrorRecords.size());
	}
}
