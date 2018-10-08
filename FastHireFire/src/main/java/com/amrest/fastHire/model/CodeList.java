package com.amrest.fastHire.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_CODELIST\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "CodeList.findAll", query = "SELECT f FROM CodeList f"),
		@NamedQuery(name = "CodeList.findByFieldId", query = "SELECT f FROM CodeList f WHERE f.fieldId = :fieldId"),
		@NamedQuery(name = "CodeList.findByCountryField", query = "SELECT f FROM CodeList f WHERE f.fieldId = :fieldId AND f.countryId = :countryId"),
		@NamedQuery(name = "CodeList.findByCountryFieldDependent", query = "SELECT f FROM CodeList f WHERE f.fieldId = :fieldId AND f.countryId = :countryId AND f.dependentFieldId = :dependentFieldId AND f.dependentFieldValue = :dependentFieldValue")
})
public class CodeList {


	@Id
	@Column(name = "\"FIELD.ID\"", columnDefinition = "VARCHAR(32)")
	private String fieldId;
	
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(32)")
	private String id;
	
	@Column(name = "\"COUNTRY.ID\"", columnDefinition = "VARCHAR(32)")
	private String countryId;
	
	@Column(name = "\"DEPENDENT_ON_FIELD\"", columnDefinition = "VARCHAR(32)")
	private String dependentFieldId;
	
	@Column(name = "\"DEPENDENT_ON_VALUE\"", columnDefinition = "VARCHAR(64)")
	private String dependentFieldValue;
	
	@Column(name = "\"START_DATE\"",columnDefinition = "SECONDDATE")
    private Date startDate;
	
	@Column(name = "\"END_DATE\"",columnDefinition = "SECONDDATE")
    private Date endDate;
	
	

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}


	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDependentFieldId() {
		return dependentFieldId;
	}

	public void setDependentFieldId(String dependentFieldId) {
		this.dependentFieldId = dependentFieldId;
	}

	public String getDependentFieldValue() {
		return dependentFieldValue;
	}

	public void setDependentFieldValue(String dependentFieldValue) {
		this.dependentFieldValue = dependentFieldValue;
	}
}
