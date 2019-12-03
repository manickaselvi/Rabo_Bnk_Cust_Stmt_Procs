package com.rabobank.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

@SpringBootTest
public class RBStatementProcessControllerTest {

	
	@Value("${rabo.bank.process.statement.file.name}")
	private String processStatementFileName;
	
	@InjectMocks
	RBStatementProcessController raboStatementProcessController;
     

	/**
	 * Scenario : Positive 
	 * scenario : Rabo Statement Process tease based on csv file
	 * Statement
	 * @throws IOException 
	 * @throws RaboBankStmtProcessException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void raboStatementProcessTestCaseCsvFile() throws RaboBankStmtProcessException, FileNotFoundException, IOException {
		String fileName= processStatementFileName + RBStatementProcessConstants.FILE_FORMAT_CSV;
		MockMultipartFile multipartFile = new MockMultipartFile("file",fileName, "multipart/form-data", new FileInputStream(new File(fileName)));		
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
	public void raboStatementProcessTestCaseXmlFile() throws RaboBankStmtProcessException, FileNotFoundException, IOException {
		String fileName= processStatementFileName + RBStatementProcessConstants.FILE_FORMAT_XML;
		MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "multipart/form-data", new FileInputStream(new File(fileName)));		
		ResponseEntity<StatementProcessResponse> responseEntity = raboStatementProcessController.raboStatementProcess(multipartFile);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
	}
	
	
}
