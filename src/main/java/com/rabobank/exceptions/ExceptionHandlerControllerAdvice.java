package com.rabobank.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rabobank.constants.RBStatementProcessConstants;
import com.rabobank.model.StatementProcessResponse;

/**
 * @author manickaselvi.m
 *
 */
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * @param exception
	 * @param req
	 * @return
	 */
	@ExceptionHandler(RaboBankStmtProcessException.class)
	public @ResponseBody ResponseEntity<StatementProcessResponse> handleStatementProcessExp(
			final RaboBankStmtProcessException exception, HttpServletRequest req) {
		logger.error("ALERT-->>Internal Server Error", exception);
		return new ResponseEntity<>(
				new StatementProcessResponse(exception.getMessage(), RBStatementProcessConstants.HTTP_CODE_ERROR, null),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<StatementProcessResponse> handleExceptions() {
		logger.error("ALERT-->>Internal Server Error");
		return new ResponseEntity<>(
				new StatementProcessResponse(RBStatementProcessConstants.UNEXPECTED_SERVER_ERROR,
						RBStatementProcessConstants.HTTP_CODE_ERROR, null),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
