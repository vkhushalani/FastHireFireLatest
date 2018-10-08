package com.amrest.fastHire.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_DROPDOWN_VALUES_FROM_SF\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "FieldDataFromSystem.findAll", query = "SELECT f FROM FieldDataFromSystem f"),
		@NamedQuery(name = "FieldDataFromSystem.findByField", query = "SELECT f FROM FieldDataFromSystem f WHERE f.fieldId = :fieldId"),
		@NamedQuery(name = "FieldDataFromSystem.findByFieldCountry", query = "SELECT f FROM FieldDataFromSystem f WHERE f.fieldId = :fieldId AND f.countryId = :countryId")
		
})
public class FieldDataFromSystem {
	@Id
	@Column(name = "\"COUNTRY.ID\"", columnDefinition = "VARCHAR(32)")
	private String countryId;
	
	@Id
	@Column(name = "\"FIELD.ID\"", columnDefinition = "VARCHAR(32)")
	private String fieldId;
	
	
	@Column(name = "\"DESTINATION_NAME\"", columnDefinition = "VARCHAR(64)")
	private String destinationName;
	
	
	@Column(name = "\"PATH\"", columnDefinition = "VARCHAR(132)")
	private String path;
	
	
	@Column(name = "\"FILTER\"", columnDefinition = "VARCHAR(256)")
	private String filter;
	
	@Column(name = "\"KEY_PATH\"", columnDefinition = "VARCHAR(132)")
	private String key;
	
	@Column(name = "\"VALUE_PATH\"", columnDefinition = "VARCHAR(132)")
	private String value;
	
	@Column(name = "\"DESCRIPTION\"", columnDefinition = "VARCHAR(152)")
	private String description;
	
	@Column(name = "\"IS_FIELD_DEPENDENT\"", columnDefinition = "BOOLEAN")
	private Boolean isDependentField;
	
	@Column(name = "\"REPLACE_TAG\"", columnDefinition = "VARCHAR(132)")
	private String replaceTag;
	
	@Column(name = "\"TAG_SOURCE_SFAPI\"", columnDefinition = "VARCHAR(132)")
	private String tagSourceFromSF;
	
	@Column(name = "\"TAG_SOURCE_FIELD_ID\"", columnDefinition = "VARCHAR(32)")
	private String tagSourceFromField;
	
	@Column(name = "\"TAG_SOURCE_VALUE_PATH\"", columnDefinition = "VARCHAR(132)")
	private String tagSourceValuePath;

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getReplaceTag() {
		return replaceTag;
	}

	public void setReplaceTag(String replaceTag) {
		this.replaceTag = replaceTag;
	}


	public String getTagSourceValuePath() {
		return tagSourceValuePath;
	}

	public void setTagSourceValuePath(String tagSourceValuePath) {
		this.tagSourceValuePath = tagSourceValuePath;
	}

	public Boolean getIsDependentField() {
		return isDependentField;
	}

	public void setIsDependentField(Boolean isDependentField) {
		this.isDependentField = isDependentField;
	}

	public String getTagSourceFromSF() {
		return tagSourceFromSF;
	}

	public void setTagSourceFromSF(String tagSourceFromSF) {
		this.tagSourceFromSF = tagSourceFromSF;
	}

	public String getTagSourceFromField() {
		return tagSourceFromField;
	}

	public void setTagSourceFromField(String tagSourceFromField) {
		this.tagSourceFromField = tagSourceFromField;
	}
}
