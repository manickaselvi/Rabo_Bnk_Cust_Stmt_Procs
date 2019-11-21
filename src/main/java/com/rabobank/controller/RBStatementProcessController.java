package com.rabobank.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.rabobank.constants.RBStatementProcessConstants;
import com.rabobank.model.Record;
import com.rabobank.model.ResultRecord;
import com.rabobank.model.StatementProcessResponse;
import com.rabobank.service.ExtractorService;
import com.rabobank.service.ValidatorService;

/**
 * @author manickaselvi m It holds the api to generate response as list of error records based on
 *         transaction reference and end balance.
 *
 */
@RestController
@RequestMapping("/rabobank")
public class RBStatementProcessController {
	
	@Autowired
	private ValidatorService validatorService;

	@Autowired
	private ExtractorService extractorService;
	
	/**
	 * @param multipartFile
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/statmentProcessor")
	@ResponseBody
	public ResponseEntity<StatementProcessResponse> raboStatementProcess(@RequestParam("file") MultipartFile multipartFile) throws Exception {
		StatementProcessResponse stmtProcessResponse = new StatementProcessResponse();
		if (!multipartFile.isEmpty()) {
			if (multipartFile.getContentType().equalsIgnoreCase(RBStatementProcessConstants.FILE_TYPE_CSV)) {
				File csvFile = new File(multipartFile.getOriginalFilename());
				multipartFile.transferTo(csvFile);
				List<Record> extractedRecords = extractorService.extractStatmentFromCSV(csvFile);
				validateRecords(extractedRecords, stmtProcessResponse);
			} else if (multipartFile.getContentType().equalsIgnoreCase(RBStatementProcessConstants.FILE_TYPE_XML)) {
				File xmlFile = new File(multipartFile.getOriginalFilename());
				multipartFile.transferTo(xmlFile);
				List<Record> extractedRecords = extractorService.extractStatmentFromXML(xmlFile);
				validateRecords(extractedRecords, stmtProcessResponse);
			} else {
				stmtProcessResponse.setResponseCode(RBStatementProcessConstants.HTTP_CODE_INVALID_INPUT);
				stmtProcessResponse.setResponseMessage(RBStatementProcessConstants.UNSUPPORTED_FILE_FORMAT);
			}
		} else {
			stmtProcessResponse.setResponseCode(RBStatementProcessConstants.HTTP_CODE_INVALID_INPUT);
			stmtProcessResponse.setResponseMessage(RBStatementProcessConstants.INVALID_INPUT);
		}
		return new ResponseEntity<StatementProcessResponse>(stmtProcessResponse, HttpStatus.OK);
	} 

	/**
	 * @param extractedRecords
	 * @param stmtProcessResponse
	 */
	private void validateRecords(List<Record> extractedRecords, StatementProcessResponse stmtProcessResponse) {
		Map<String, List<ResultRecord>> errorRecordsMap = new HashMap<>();
		List<ResultRecord> faildRdsByRef = validatorService.getDuplicateRecordsByRef(extractedRecords);
		List<ResultRecord> faildRdsByEndBal = validatorService.getEndBalanceErrorRecords(extractedRecords);
		if(!faildRdsByRef.isEmpty()) {
			errorRecordsMap.put(RBStatementProcessConstants.ERROR_RECORDMAP_KEY_REFERENCE, faildRdsByRef);
		} 
		if(!faildRdsByEndBal.isEmpty()) {
			errorRecordsMap.put(RBStatementProcessConstants.ERROR_RECORDMAP_KEY_END_BAL, faildRdsByEndBal);
		}
		if (!errorRecordsMap.isEmpty()) {
			stmtProcessResponse.setResponseCode(RBStatementProcessConstants.HTTP_CODE_SUCCESS);
			stmtProcessResponse.setResponseMessage(RBStatementProcessConstants.VALIDATION_ERROR);
			stmtProcessResponse.setRecordsMap(errorRecordsMap);
		} else {
			stmtProcessResponse.setResponseCode(RBStatementProcessConstants.HTTP_CODE_SUCCESS);
			stmtProcessResponse.setResponseMessage(RBStatementProcessConstants.VALIDATION_SUCCESS);
		}
	}

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public final ResponseEntity<StatementProcessResponse> handleExceptions(Exception ex, WebRequest request) {
		StatementProcessResponse stmtProcessResponse = new StatementProcessResponse();
		stmtProcessResponse.setResponseCode(RBStatementProcessConstants.HTTP_CODE_ERROR);
		stmtProcessResponse.setResponseMessage(RBStatementProcessConstants.UNEXPECTED_SERVER_ERROR);
		return new ResponseEntity(stmtProcessResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
