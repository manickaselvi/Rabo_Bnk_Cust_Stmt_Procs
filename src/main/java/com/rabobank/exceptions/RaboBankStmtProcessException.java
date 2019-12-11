package com.rabobank.exceptions;

/**
 * @author manickaselvi.m
 *
 */
public class RaboBankStmtProcessException extends Exception {

	private static final long serialVersionUID = 7718828512143293558L;
		
	/**
	 * @param message
	 * @param ex
	*/
	public RaboBankStmtProcessException(String message, Exception ex) {
		super(message, ex);
	}
}
