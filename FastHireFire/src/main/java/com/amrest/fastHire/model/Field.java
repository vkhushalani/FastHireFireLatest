package com.amrest.fastHire.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "\"com.nga.poc.fasthire.db::Table.FHD_FIELDS\"", schema = "POC_FAST_HIRE")
@NamedQueries({ @NamedQuery(name = "Field.findAll", query = "SELECT f FROM Field f") })
public class Field {
	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(32)")
	private String id;

	@Column(name = "\"TECHNICAL_FIELD_NAME\"", columnDefinition = "VARCHAR(32)")
	private String technicalName;

	@Transient
	private String name;

	@Column(name = "\"FIELD_TYPE\"", columnDefinition = "VARCHAR(16)")
	private String fieldType;

	@Column(name = "\"SFAPI.ENTITY_NAME\"", columnDefinition = "VARCHAR(64)")
	private String entityName;

	@Column(name = "\"VALUE_FROM_PATH\"", columnDefinition = "VARCHAR(132)")
	private String valueFromPath;

	@Column(name = "\"LENGTH\"", columnDefinition = "INTEGER")
	private Integer length;

	@Column(name = "\"INITIAL_VALUE\"", columnDefinition = "VARCHAR(32)")
	private String initialValue;

	@Column(name = "\"DATA_TYPE\"", columnDefinition = "VARCHAR(32)")
	private String dataType;

	@Column(name = "\"CREATED_ON\"", columnDefinition = "SECONDDATE")
	private Date createdOn;

	@Column(name = "\"START_DATE\"", columnDefinition = "SECONDDATE")
	private Date startDate;

	@Column(name = "\"END_DATE\"", columnDefinition = "SECONDDATE")
	private Date endDate;

	@Column(name = "\"FIELD_TRIGGER\"", columnDefinition = "VARCHAR(32)")
	private String fieldTrigger;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTechnicalName() {
		return technicalName;
	}

	public void setTechnicalName(String technicalName) {
		this.technicalName = technicalName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	public String getValueFromPath() {
		return valueFromPath;
	}

	public void setValueFromPath(String valueFromPath) {
		this.valueFromPath = valueFromPath;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getFieldTrigger() {
		return fieldTrigger;
	}

	public void setFieldTrigger(String fieldTrigger) {
		this.fieldTrigger = fieldTrigger;
	}

}
