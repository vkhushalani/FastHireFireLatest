package com.amrest.fastHire.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_FIELD_GROUP_TEXT\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "FieldGroupText.findAll", query = "SELECT f FROM FieldGroupText f"),
		@NamedQuery(name = "FieldGroupText.findByFieldGroupId", query = "SELECT f FROM FieldGroupText f WHERE f.fieldGroupId = :fieldGroupId"),
		@NamedQuery(name = "FieldGroupText.findByFieldGroupLanguage", query = "SELECT f FROM FieldGroupText f WHERE f.fieldGroupId = :fieldGroupId AND f.language = :language")
		
})
public class FieldGroupText {
	
	@Id
	@Column(name = "\"FIELD_GROUP.ID\"", columnDefinition = "VARCHAR(32)")
	private String fieldGroupId;
	
	@Id
	@Column(name = "\"LANGUAGE\"", columnDefinition = "VARCHAR(5)")
	private String language;
	
	@Column(name = "\"NAME\"", columnDefinition = "VARCHAR(64)")
	private String name;
	
	@Column(name = "\"DESCRIPTION\"", columnDefinition = "VARCHAR(152)")
	private String description;

	
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

	public String getFieldGroupId() {
		return fieldGroupId;
	}

	public void setFieldGroupId(String fieldGroupId) {
		this.fieldGroupId = fieldGroupId;
	}
	

}
