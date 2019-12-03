package com.rabobank.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.rabobank.exceptions.RaboBankStmtProcessException;
import com.rabobank.model.Record;
import com.rabobank.model.Records;

/**
 * @author manickaselvi m
 *
 */
@Service	
public class ExtractorServiceImpl implements ExtractorService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * This method maps the column id of csv file to the java bean property.
	 * 
	 * @return List<Records>
	 * @throws RaboBankStmtProcessException 
	 */
	public List<Record> extractStatmentFromCSV(File file) throws RaboBankStmtProcessException {
		logger.info("ExtractorServiceImpl : extractStatmentFromCSV() -->> Starts");
		List<Record> records = new ArrayList<>();
		try {
			HeaderColumnNameTranslateMappingStrategy<Record> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<Record>();
			beanStrategy.setType(Record.class);
	
			Map<String, String> columnMapping = new HashMap<String, String>();
			columnMapping.put("Reference", "transactionRef");
			columnMapping.put("AccountNumber", "accountNumber");
			columnMapping.put("Description", "description");
			columnMapping.put("Start Balance", "startBalance");
			columnMapping.put("Mutation", "mutation");
			columnMapping.put("End Balance", "endBalance");
	
			beanStrategy.setColumnMapping(columnMapping);
	
			CsvToBean<Record> csvToBean = new CsvToBean<Record>();
			CSVReader reader = new CSVReader(new FileReader(file));
			records = csvToBean.parse(beanStrategy, reader);
		}catch(FileNotFoundException ex) {
			logger.debug("Exception in ExtractorServiceImpl:extractStatmentFromCSV() ", ex);	
			throw new RaboBankStmtProcessException(ex.getMessage());
		}
		logger.info("ExtractorServiceImpl : extractStatmentFromCSV() -->> Ends");
		return records;
	}

	/**
	 * This method convert xml data into the java bean property.
	 * 
	 * @return List<Records>
	 * @throws RaboBankStmtProcessException 
	 */
	public List<Record> extractStatmentFromXML(File file) throws RaboBankStmtProcessException {
		logger.info("ExtractorServiceImpl : extractStatmentFromXML() -->> Starts");
		Records rootRecord = new Records();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Records.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			rootRecord = (Records) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException ex) {
			logger.debug("Exception in ExtractorServiceImpl: extractStatmentFromXML() ", ex);
			throw new RaboBankStmtProcessException(ex.getMessage());
		}
		logger.info("ExtractorServiceImpl : extractStatmentFromXML() -->> Ends");
		return rootRecord.getRecord();
	}
}
