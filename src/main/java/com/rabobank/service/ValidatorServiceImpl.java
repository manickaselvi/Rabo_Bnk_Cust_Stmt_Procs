package com.rabobank.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rabobank.constants.RBStatementProcessConstants;
import com.rabobank.exceptions.RaboBankStmtProcessException;
import com.rabobank.model.Record;
import com.rabobank.model.ResultRecord;
import com.rabobank.model.StatementProcessResponse;

/**
 * This ServiceImpl validates to find duplicate records based on transaction
 * reference and error records based on end balance
 * 
 * @author manickaselvi m
 *
 */
@Service
public class ValidatorServiceImpl implements ValidatorService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	

	@Autowired
	private ExtractorService extractorService;
	

	/**
	 * @return List<ResultRecord> to get duplicate records form given input list.
	 */
	public List<ResultRecord> getDuplicateRecordsByRef(List<Record> records) {
		logger.info("ValidatorServiceImpl : getDuplicateRecordsByRef() -->> Starts");
		Map<Integer, Record> uniqueRecords = new HashMap<>();
		List<ResultRecord> duplicateRecords = new ArrayList<>();
		records.stream().filter(Objects::nonNull).forEach(record -> {
			if (uniqueRecords.containsKey(record.getTransactionRef())) {
				duplicateRecords.add(createResultRecord(record));
			} else {
				uniqueRecords.put(record.getTransactionRef(), record);
			}
		});

		List<ResultRecord> finalDuplicateRecords = new ArrayList<>();
		finalDuplicateRecords.addAll(duplicateRecords);

		duplicateRecords.stream().filter(Objects::nonNull)
				.filter(dupRecord -> null != uniqueRecords.get(dupRecord.getTransactionRef())).forEach(dupRecord -> {
					finalDuplicateRecords.add(createResultRecord(uniqueRecords.get(dupRecord.getTransactionRef())));
					uniqueRecords.remove(dupRecord.getTransactionRef());
				});
		logger.info("ValidatorServiceImpl : getDuplicateRecordsByRef() -->> Ends");
		return finalDuplicateRecords;
	}
	
	/**
	 * @return List<ResultRecord> endbalance is wrong if startbalance - mutation !=
	 *         endbalance then that list will be returned.
	 */
	public List<ResultRecord> getEndBalanceErrorRecords(List<Record> records) {
		logger.info("ValidatorServiceImpl : getEndBalanceErrorRecords() -->> Starts");
		List<ResultRecord> endBalanceErrorRecords = new ArrayList<>();
		records.stream().filter(Objects::nonNull)
				.filter(record -> Math.round(
						(record.getStartBalance() - record.getMutation()) - Math.round(record.getEndBalance())) != 0)
				.forEach(record -> endBalanceErrorRecords.add(createResultRecord(record)));
		logger.info("ValidatorServiceImpl : getEndBalanceErrorRecords() -->> Ends");
	return endBalanceErrorRecords;
	}

	/**
	 * This method map field values from Record to ResultRecord.
	 * 
	 * @param record
	 * @return
	 */
	private ResultRecord createResultRecord(Record record) {
		ResultRecord resRcd = new ResultRecord();
		resRcd.setDescription(record.getDescription());
		resRcd.setTransactionRef(record.getTransactionRef());
		return resRcd;
	}
	
	

	/**
	 * @param extractedRecords
	 * @param stmtProcessResponse
	 */
	public Map<String, List<ResultRecord>> getErrorRecordsMap(List<Record> extractedRecords) {
		logger.info("ValidatorServiceImpl : getErrorRecordsMap()-->> Starts");
		Map<String, List<ResultRecord>> errorRecordsMap = new HashMap<>();
		List<ResultRecord> faildRecordsByTransactionRef = getDuplicateRecordsByRef(extractedRecords);
		List<ResultRecord> faildRecordsByEndBal = getEndBalanceErrorRecords(extractedRecords);
		if(!faildRecordsByTransactionRef.isEmpty()) {
			errorRecordsMap.put(RBStatementProcessConstants.ERROR_RECORDMAP_KEY_REFERENCE, faildRecordsByTransactionRef);
		} 
		if(!faildRecordsByEndBal.isEmpty()) {
			errorRecordsMap.put(RBStatementProcessConstants.ERROR_RECORDMAP_KEY_END_BAL, faildRecordsByEndBal);
		}
		return errorRecordsMap;
	}
	
	
	/* (non-Javadoc)
	 * This method extract records from csv or xml to get error records then form and send response
	 * 
	 * @see com.rabobank.service.ValidatorService#validateFile(org.springframework.web.multipart.MultipartFile)
	 */
	public StatementProcessResponse validateFile(MultipartFile multipartFile) throws RaboBankStmtProcessException {
		logger.info("ValidatorServiceImpl : validateFile()-->> Starts");
		Integer responseCode= null;
		String responseMessage="";
		Map<String, List<ResultRecord>> errorRecordsMap = new HashMap<>();
		try {
			if (!multipartFile.isEmpty()) {
				if (multipartFile.getContentType().equalsIgnoreCase(RBStatementProcessConstants.FILE_TYPE_CSV)) {
					File csvFile = new File(multipartFile.getOriginalFilename());
					multipartFile.transferTo(csvFile);
					List<Record> extractedRecords = extractorService.extractStatmentFromCSV(csvFile);
					errorRecordsMap =getErrorRecordsMap(extractedRecords);
				} else if (multipartFile.getContentType().equalsIgnoreCase(RBStatementProcessConstants.FILE_TYPE_XML)) {
					File xmlFile = new File(multipartFile.getOriginalFilename());
					multipartFile.transferTo(xmlFile);
					List<Record> extractedRecords = extractorService.extractStatmentFromXML(xmlFile);
					errorRecordsMap=getErrorRecordsMap(extractedRecords);
				} else {
					responseCode = RBStatementProcessConstants.HTTP_CODE_INVALID_INPUT;
					responseMessage = RBStatementProcessConstants.UNSUPPORTED_FILE_FORMAT;
				}
			} else {
				responseCode =RBStatementProcessConstants.HTTP_CODE_INVALID_INPUT;
				responseMessage =RBStatementProcessConstants.INVALID_INPUT;
			}
		} catch(RaboBankStmtProcessException | IllegalStateException | IOException ex){
			throw new RaboBankStmtProcessException("Exception in ValidatorServiceImpl:validateFile()", ex);
		}
		logger.info("ValidatorServiceImpl : validateFile()-->> Ends");

		return formStatementProcessResponse(responseCode, responseMessage, errorRecordsMap);
	}

	
	/**
	 * @param responseCode
	 * @param responseMessage
	 * @param errorRecordsMap
	 * @return
	 */
	private StatementProcessResponse formStatementProcessResponse(Integer responseCode, String responseMessage,Map<String, List<ResultRecord>> errorRecordsMap) {
		logger.info("ValidatorServiceImpl : formStatementProcessResponse()-->> Starts");
		StatementProcessResponse stmtProcessResponse = new StatementProcessResponse();
		if (null !=errorRecordsMap && !errorRecordsMap.isEmpty()) {
			responseCode =RBStatementProcessConstants.HTTP_CODE_SUCCESS;
			responseMessage= RBStatementProcessConstants.VALIDATION_ERROR;
			stmtProcessResponse.setRecordsMap(errorRecordsMap);
		} else {
			responseCode = responseCode != null ? responseCode :RBStatementProcessConstants.HTTP_CODE_SUCCESS ;
			responseMessage=!responseMessage.isEmpty() ? responseMessage :RBStatementProcessConstants.VALIDATION_SUCCESS;
		}
		stmtProcessResponse.setResponseCode(responseCode);
		stmtProcessResponse.setResponseMessage(responseMessage);	
		logger.info("ValidatorServiceImpl : formStatementProcessResponse()-->> Ends");
		return stmtProcessResponse;
			
	}
}
