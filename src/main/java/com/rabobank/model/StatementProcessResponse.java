package com.rabobank.model;

import java.util.List;
import java.util.Map;


/**
 * @author manickaselvi m
 *
 */
public class StatementProcessResponse {
	private String responseMessage;
	private int responseCode;
	private Map<String, List<ResultRecord>> recordsMap;

		
	/**
	 * @param responseMessage
	 * @param responseCode
	 * @param recordsMap
	 */
	public StatementProcessResponse(String responseMessage, int responseCode,
			Map<String, List<ResultRecord>> recordsMap) {
		super();
		this.responseMessage = responseMessage;
		this.responseCode = responseCode;
		this.recordsMap = recordsMap;
	}
	
	public StatementProcessResponse() {
		super();
	}
	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}
	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}
	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	/**
	 * @return the recordsMap
	 */
	public Map<String, List<ResultRecord>> getRecordsMap() {
		return recordsMap;
	}
	/**
	 * @param recordsMap the recordsMap to set
	 */
	public void setRecordsMap(Map<String, List<ResultRecord>> recordsMap) {
		this.recordsMap = recordsMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((recordsMap == null) ? 0 : recordsMap.hashCode());
		result = prime * result + responseCode;
		result = prime * result + ((responseMessage == null) ? 0 : responseMessage.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StatementProcessResponse)) {
			return false;
		}
		StatementProcessResponse other = (StatementProcessResponse) obj;
		if (recordsMap == null) {
			if (other.recordsMap != null) {
				return false;
			}
		} else if (!recordsMap.equals(other.recordsMap)) {
			return false;
		}
		if (responseCode != other.responseCode) {
			return false;
		}
		if (responseMessage == null) {
			if (other.responseMessage != null) {
				return false;
			}
		} else if (!responseMessage.equals(other.responseMessage)) {
			return false;
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatementProcessResponse [responseMessage=" + responseMessage + ", responseCode=" + responseCode
				+ ", recordsMap=" + recordsMap + "]";
	}

	
}
