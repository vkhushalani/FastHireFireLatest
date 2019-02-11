package com.amrest.fastHire.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.nga.poc.fasthire.db::Table.FHD_FIELDS_TEXT\"", schema = "POC_FAST_HIRE")
@NamedQueries({ @NamedQuery(name = "FieldText.findAll", query = "SELECT f FROM FieldText f"),
		@NamedQuery(name = "FieldText.findByFieldId", query = "SELECT f FROM FieldText f WHERE f.fieldId = :fieldId"),
		@NamedQuery(name = "FieldText.findByFieldLanguage", query = "SELECT f FROM FieldText f WHERE f.fieldId = :fieldId AND f.language = :language")

})
public class FieldText {

	@Id
	@Column(name = "\"FIELD.ID\"", columnDefinition = "VARCHAR(32)")
	private String fieldId;

	@Id
	@Column(name = "\"LANGUAGE\"", columnDefinition = "VARCHAR(5)")
	private String language;

	@Column(name = "\"NAME\"", columnDefinition = "VARCHAR(64)")
	private String name;

	@Column(name = "\"DESCRIPTION\"", columnDefinition = "VARCHAR(152)")
	private String description;

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
