package com.rabobank.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rabobank.model.Record;

/**
 * @author manickaselvi m
 *
 */
@Service
public interface ExtractorService {

	List<Record> extractStatmentFromCSV(File file) throws Exception;
	
	List<Record> extractStatmentFromXML(File file) throws Exception;
	
}
