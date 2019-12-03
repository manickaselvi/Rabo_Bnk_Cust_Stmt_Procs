package com.rabobank.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import com.rabobank.exceptions.RaboBankStmtProcessException;
import com.rabobank.model.Record;
import com.rabobank.model.ResultRecord;
import com.rabobank.model.StatementProcessResponse;
/**
 * @author manickaselvi m
 *
 */
@Service
public interface ValidatorService {
		
	List<ResultRecord> getDuplicateRecordsByRef(List<Record> records);
	
	List<ResultRecord> getEndBalanceErrorRecords(List<Record> records);
	
	StatementProcessResponse validateFile(MultipartFile multipartFile) throws RaboBankStmtProcessException;

}
