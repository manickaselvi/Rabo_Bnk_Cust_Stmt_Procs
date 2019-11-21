package com.rabobank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.rabobank.model.Record;
import com.rabobank.model.ResultRecord;
import com.rabobank.service.ExtractorService;
import com.rabobank.service.ExtractorServiceImpl;
import com.rabobank.service.ValidatorService;
import com.rabobank.service.ValidatorServiceImpl;


/**
 * @author manickaselvi m
 *
 */
@SpringBootTest
class RaboBnkCustStmtProcessorApplicationTests {

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

	/**
	 * Scenario : Positive 
	 * scenario : Processing the input CSV file and extracting
	 * values as POJO object for validation process
	 */
	@Test
	public void extractStatmentFromCSVTestCase() {
		ExtractorService extractorServiceImpl = new ExtractorServiceImpl();
		File inputFile = new File("records.csv");
		try {
			int totalLineInInputCSV = getNumberOfLine(inputFile);
			List<Record> extractedRecords = extractorServiceImpl.extractStatmentFromCSV(inputFile);
			assertEquals(totalLineInInputCSV-1, extractedRecords.size());
		} catch (IOException e) {
			//Assert.fail("File processing error!!" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
		//	Assert.fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Scenario : Positive 
	 * scenario : Processing the input XML file and extracting
	 * values as POJO object for validation process
	 */
	@Test
	public void extractStatmentFromXMLTestCase() {
		ExtractorService extractorServiceImpl = new ExtractorServiceImpl();
		File inputFile = new File("records.xml");
		try {
			int totalLineInInputXML = 10; /// let. input XML file has 10 records.
			List<Record> extractedRecords = extractorServiceImpl.extractStatmentFromXML(inputFile);
			assertEquals(totalLineInInputXML, extractedRecords.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public int getNumberOfLine(File file) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

}
