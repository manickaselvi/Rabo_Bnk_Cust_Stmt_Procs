package com.rabobank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.rabobank.exceptions.RaboBankStmtProcessException;
import com.rabobank.model.Record;

@SpringBootTest
public class ExtractorServiceImplTest {
	
	@Value("${rabo.bank.process.statement.file.name}")
	private String processStatementFileName;
	
	/**
	 * Scenario : Positive 
	 * scenario : Processing the input CSV file and extracting
	 * values as POJO object for validation process
	 * @throws IOException 
	 * @throws RaboBankStmtProcessException 
	 */
	@Test
	public void extractStatmentFromCSVTestCase() throws IOException, RaboBankStmtProcessException {
		ExtractorService extractorServiceImpl = new ExtractorServiceImpl();
		File inputFile = new File(processStatementFileName + ".csv");
		int totalLineInInputCSV;
		try(Stream<String> totalLineInInputCSVString= Files.lines(Paths.get(inputFile.getName()))){
			totalLineInInputCSV =(int) totalLineInInputCSVString.count();
		} 
		List<Record> extractedRecords = extractorServiceImpl.extractStatmentFromCSV(inputFile);
		assertEquals(totalLineInInputCSV - 1, extractedRecords.size());
	}

	/**
	 * Scenario : Positive 
	 * scenario : Processing the input XML file and extracting
	 * values as POJO object for validation process
	 * @throws IOException 
	 * @throws RaboBankStmtProcessException 
	 */
	@Test
	public void extractStatmentFromXMLTestCase() throws IOException, RaboBankStmtProcessException {
		ExtractorService extractorServiceImpl = new ExtractorServiceImpl();
		File inputFile = new File(processStatementFileName + ".xml");
		int totalLineInInputXML;
		try(Stream<String> totalLineInInputXMLString= Files.lines(Paths.get(inputFile.getName()))){
			totalLineInInputXML =(int) totalLineInInputXMLString.count();
		}		
		List<Record> extractedRecords = extractorServiceImpl.extractStatmentFromXML(inputFile);
		assertEquals((totalLineInInputXML - 2) / 7, extractedRecords.size());
	}

}
