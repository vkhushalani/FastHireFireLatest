package com.amrest.fastHire.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_CODELIST_TEXT\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "CodeListText.findAll", query = "SELECT f FROM CodeListText f"),
		@NamedQuery(name = "CodeListText.findByCodeListIdLang", query = "SELECT f FROM CodeListText f WHERE f.codeListId = :codeListId AND f.language = :language"),
		@NamedQuery(name = "CodeListText.findById", query = "SELECT f FROM CodeListText f WHERE f.codeListId = :codeListId AND f.language = :language AND f.value = :value")
})
public class CodeListText {

	@Id
	@Column(name = "\"CODELIST.ID\"", columnDefinition = "VARCHAR(32)")
	private String codeListId;
	
	@Id
	@Column(name = "\"LANGUAGE\"", columnDefinition = "VARCHAR(5)")
	private String language;
	
	@Id
	@Column(name = "\"VALUE\"", columnDefinition = "VARCHAR(5)")
	private String value;
	
	@Column(name = "\"DESCRIPTION\"", columnDefinition = "VARCHAR(152)")
	private String description;

	public String getCodeListId() {
		return codeListId;
	}

	public void setCodeListId(String codeListId) {
		this.codeListId = codeListId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
