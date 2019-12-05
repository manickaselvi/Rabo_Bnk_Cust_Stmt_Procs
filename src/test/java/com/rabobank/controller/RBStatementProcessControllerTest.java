package com.rabobank.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.rabobank.constants.RBStatementProcessConstants;
import com.rabobank.exceptions.RaboBankStmtProcessException;
import com.rabobank.model.StatementProcessResponse;

import org.mockito.Mock;
import org.mockito.Mockito;
import com.rabobank.model.ResultRecord;
import com.rabobank.service.ValidatorService;

@SpringBootTest
public class RBStatementProcessControllerTest {

	
	@Value("${rabo.bank.process.statement.file.name}")
	private String processStatementFileName;
	
	@InjectMocks
	RBStatementProcessController raboStatementProcessController;
	
	@Mock
	private ValidatorService validatorService;    

	/**
	 * Scenario : Positive 
	 * scenario : Rabo Statement Process tease based on csv file
	 * Statement
	 * @throws IOException 
	 * @throws RaboBankStmtProcessException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void raboStatementProcessTestCaseCsvFile() throws RaboBankStmtProcessException, IOException {
		String fileName= processStatementFileName + RBStatementProcessConstants.FILE_FORMAT_CSV;
		MockMultipartFile multipartFile = new MockMultipartFile("file",fileName, "multipart/form-data", new FileInputStream(new File(fileName)));
		Mockito.when(validatorService.validateFile(multipartFile)).thenReturn(getStatementProcessResponseForCSVTestCase());
		ResponseEntity<StatementProcessResponse> responseEntity = raboStatementProcessController.raboStatementProcess(multipartFile);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

	}
	
	/**
	 * Scenario : Positive 
	 * scenario : Rabo Statement Process tease based on xml file
	 * Statement
	 * @throws IOException 
	 * @throws RaboBankStmtProcessException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void raboStatementProcessTestCaseXmlFile() throws RaboBankStmtProcessException, IOException {
		String fileName= processStatementFileName + RBStatementProcessConstants.FILE_FORMAT_XML;
		MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "multipart/form-data", new FileInputStream(new File(fileName)));
		Mockito.when(validatorService.validateFile(multipartFile)).thenReturn(getStatementProcessResponseForXMLTestCase());
		ResponseEntity<StatementProcessResponse> responseEntity = raboStatementProcessController.raboStatementProcess(multipartFile);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
	}
	
	/**
	 * Returns sample response with few error records to process statement processor based on csv file
	 * 
	 * @return
	 */
	private StatementProcessResponse getStatementProcessResponseForCSVTestCase() {
		StatementProcessResponse statementProcessResponse =new StatementProcessResponse();
		Map<String, List<ResultRecord>> errorRecordsMap = new HashMap<>();
		List<ResultRecord> failedRecordsByRef = Arrays.asList(
				new ResultRecord(112806,"Clothes for Richard de Vries"),
				new ResultRecord(112806, "Tickets from Richard Bakker"),
				new ResultRecord(112806, "Clothes for Willem Dekker"));
		List<ResultRecord> failedRecordsByEndBal = Arrays.asList(
				new ResultRecord(194261,"Clothes from Jan Bakker"),
				new ResultRecord(112806, "Clothes for Willem Dekker"),
				new ResultRecord(179430, "Clothes for Vincent Bakker"));
		statementProcessResponse.setResponseCode(RBStatementProcessConstants.HTTP_CODE_SUCCESS);
		statementProcessResponse.setResponseMessage(RBStatementProcessConstants.VALIDATION_ERROR);
		errorRecordsMap.put(RBStatementProcessConstants.ERROR_RECORDMAP_KEY_REFERENCE, failedRecordsByRef);
		errorRecordsMap.put(RBStatementProcessConstants.ERROR_RECORDMAP_KEY_END_BAL, failedRecordsByEndBal);
		statementProcessResponse.setRecordsMap(errorRecordsMap);
		return statementProcessResponse;
	}
	
	/**
	 * Returns sample response with few error records to process statement processor based on xml file
	 * 
	 * @return
	 */
	private StatementProcessResponse getStatementProcessResponseForXMLTestCase() {
		StatementProcessResponse statementProcessResponse =new StatementProcessResponse();
		Map<String, List<ResultRecord>> errorRecordsMap = new HashMap<>();
		List<ResultRecord> failedRecordsByEndBal = Arrays.asList(
				new ResultRecord(165102,"Tickets for Rik Theuß"),
				new ResultRecord(172833, "Tickets for Willem Theuß"),
				new ResultRecord(169639, "Tickets for Rik de Vries"));
		statementProcessResponse.setResponseCode(RBStatementProcessConstants.HTTP_CODE_SUCCESS);
		statementProcessResponse.setResponseMessage(RBStatementProcessConstants.VALIDATION_ERROR);
		errorRecordsMap.put(RBStatementProcessConstants.ERROR_RECORDMAP_KEY_REFERENCE, new ArrayList<ResultRecord>());
		errorRecordsMap.put(RBStatementProcessConstants.ERROR_RECORDMAP_KEY_END_BAL, failedRecordsByEndBal);
		statementProcessResponse.setRecordsMap(errorRecordsMap);
		return statementProcessResponse;
	}
	
}
