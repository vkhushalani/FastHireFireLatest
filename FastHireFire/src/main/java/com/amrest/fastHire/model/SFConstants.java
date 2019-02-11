package com.amrest.fastHire.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.nga.poc.fasthire.db::Table.FHD_SF_CONSTANTS\"", schema = "POC_FAST_HIRE")
@NamedQueries({ @NamedQuery(name = "SFConstants.findAll", query = "SELECT bu FROM BusinessUnit bu") })

public class SFConstants {
	@Id
	@Column(name = "\"TECHNICAL_NAME\"", columnDefinition = "VARCHAR(64)")
	private String tehcnicalName;

	@Column(name = "\"VALUE\"", columnDefinition = "VARCHAR(64)")
	private String value;

	public String getTehcnicalName() {
		return tehcnicalName;
	}

	public void setTehcnicalName(String tehcnicalName) {
		this.tehcnicalName = tehcnicalName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
