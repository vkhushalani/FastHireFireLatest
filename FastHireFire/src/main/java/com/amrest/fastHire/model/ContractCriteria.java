package com.amrest.fastHire.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.nga.poc.fasthire.db::Table.FHD_CONTRACT_CRITERIA\"", schema = "POC_FAST_HIRE")
@NamedQueries({
		@NamedQuery(name = "ContractCriteria.findByCountryCompany", query = "SELECT cc FROM ContractCriteria cc WHERE cc.country = :country AND cc.company = :company")

})
public class ContractCriteria implements Comparable<ContractCriteria> {

	@Id
	@Column(name = "\"COUNTRY\"", columnDefinition = "VARCHAR(32)")
	private String country;

	@Id
	@Column(name = "\"COMPANY\"", columnDefinition = "VARCHAR(32)")
	private String company;

	@Id
	@Column(name = "\"SFAPI.ENTITY_NAME\"", columnDefinition = "VARCHAR(64)")
	private String entityName;

	@Id
	@Column(name = "\"FIELD\"", columnDefinition = "VARCHAR(64)")
	private String field;

	@Column(name = "\"SEQ\"", columnDefinition = "INTEGER")
	private Integer sequence;

	@Column(name = "\"START_DATE\"", columnDefinition = "SECONDDATE")
	private Date startDate;

	@Column(name = "\"END_DATE\"", columnDefinition = "SECONDDATE")
	private Date endDate;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public int compareTo(ContractCriteria o) {
		int compareValue = o.getSequence().intValue();
		/* For Ascending order */
		return this.sequence.intValue() - compareValue;

		/* For Descending order do like this */
		// return compareage-this.studentage;
//		return 0;
	}
}
