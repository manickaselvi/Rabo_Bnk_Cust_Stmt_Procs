package com.rabobank.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rabobank.model.Record;
import com.rabobank.model.ResultRecord;

/**
 * @author manickaselvi m
 *
 */
@Service
public interface ValidatorService {
		
	List<ResultRecord> getDuplicateRecordsByRef(List<Record> records);
	
	List<ResultRecord> getEndBalanceErrorRecords(List<Record> records);


}
