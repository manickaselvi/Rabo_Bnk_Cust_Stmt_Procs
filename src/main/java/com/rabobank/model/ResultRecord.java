package com.rabobank.model;

/**
 * @author manickaselvi m This model object holds final result to show
 *
 */

public class ResultRecord {
	int transactionRef;
	String description;

	/**
	 * @param transactionRef
	 * @param description
	 */
	public ResultRecord(int transactionRef, String description) {
		super();
		this.transactionRef = transactionRef;
		this.description = description;
	}

	public ResultRecord() {
		super();
	}

	/**
	 * @return the transactionReference
	 */
	public int getTransactionRef() {
		return transactionRef;
	}

	/**
	 * @param transactionReference the transactionReference to set
	 */
	public void setTransactionRef(int transactionReference) {
		this.transactionRef = transactionReference;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + transactionRef;
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
		if (!(obj instanceof ResultRecord)) {
			return false;
		}
		ResultRecord other = (ResultRecord) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		return transactionRef == other.transactionRef;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResultRecord [transactionRef=" + transactionRef + ", description=" + description + "]";
	}

}
