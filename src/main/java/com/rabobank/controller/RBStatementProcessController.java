package com.rabobank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rabobank.constants.RBStatementProcessConstants;
import com.rabobank.exceptions.RaboBankStmtProcessException;
import com.rabobank.model.StatementProcessResponse;
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

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * @param multipartFile
	 * @return
	 */
	@PostMapping(value = "/statmentProcessor")
	@ResponseBody
	public ResponseEntity<StatementProcessResponse> raboStatementProcess(@RequestParam("file") MultipartFile multipartFile)  throws RaboBankStmtProcessException{
		logger.info("RBStatementProcessController : Customer Statement Process -->> Starts");
		StatementProcessResponse stmtProcessResponse = validatorService.validateFile(multipartFile);
		logger.info("RBStatementProcessController : Customer Statement Process -->> Ends");
		return new ResponseEntity<>(stmtProcessResponse, HttpStatus.OK);
	} 

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public ResponseEntity<StatementProcessResponse> handleExceptions() {
		logger.info("ALERT-->>Internal Server Error");
		return new ResponseEntity<StatementProcessResponse>(
				new StatementProcessResponse(RBStatementProcessConstants.UNEXPECTED_SERVER_ERROR,
						RBStatementProcessConstants.HTTP_CODE_ERROR, null),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
