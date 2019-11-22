package com.rabobank.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rabobank.model.Record;
import com.rabobank.model.ResultRecord;

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

	/**
	 * @return List<ResultRecord> to get duplicate records form given input list.
	 */
	public List<ResultRecord> getDuplicateRecordsByRef(List<Record> records) {
		logger.info("ExtractorServiceImpl : getDuplicateRecordsByRef() -->> Starts");
		Map<Integer, Record> uniqueRecords = new HashMap<Integer, Record>();
		List<ResultRecord> duplicateRecords = new ArrayList<ResultRecord>();
		records.stream().filter(Objects::nonNull).forEach(record -> {
			if (uniqueRecords.containsKey(record.getTransactionRef())) {
				duplicateRecords.add(createResultRecord(record));
			} else {
				uniqueRecords.put(record.getTransactionRef(), record);
			}
		});

		List<ResultRecord> finalDuplicateRecords = new ArrayList<ResultRecord>();
		finalDuplicateRecords.addAll(duplicateRecords);

		duplicateRecords.stream().filter(Objects::nonNull)
				.filter(dupRecord -> null != uniqueRecords.get(dupRecord.getTransactionRef())).forEach(dupRecord -> {
					finalDuplicateRecords.add(createResultRecord(uniqueRecords.get(dupRecord.getTransactionRef())));
					uniqueRecords.remove(dupRecord.getTransactionRef());
				});
		logger.info("ExtractorServiceImpl : getDuplicateRecordsByRef() -->> Ends");
		return finalDuplicateRecords;
	}
	
	/**
	 * @return List<ResultRecord> endbalance is wrong if startbalance - mutation !=
	 *         endbalance then that list will be returned.
	 */
	public List<ResultRecord> getEndBalanceErrorRecords(List<Record> records) {
		logger.info("ExtractorServiceImpl : getEndBalanceErrorRecords() -->> Starts");
		List<ResultRecord> endBalanceErrorRecords = new ArrayList<ResultRecord>();
		records.stream().filter(Objects::nonNull)
				.filter(record -> Math.round(
						(record.getStartBalance() - record.getMutation()) - Math.round(record.getEndBalance())) != 0)
				.forEach(record -> {
					endBalanceErrorRecords.add(createResultRecord(record));
				});
		logger.info("ExtractorServiceImpl : getEndBalanceErrorRecords() -->> Ends");
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
	
}
