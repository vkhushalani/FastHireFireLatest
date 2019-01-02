package com.amrest.fastHire.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_CONFIRM_STATUS\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "ConfirmStatus.findAll", query = "SELECT f FROM CodeListText f"),
		
})
public class ConfirmStatus {

	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(164)")
	private String id;
	
	@Column(name = "\"SF_ENTITY\"", columnDefinition = "BOOLEAN")
	private Boolean sfEntityFlag = false;
	
	@Column(name = "\"PEX\"", columnDefinition = "BOOLEAN)")
	private Boolean pexUpdateFlag = false;
	
	@Column(name = "\"DOC_GEN\"", columnDefinition = "BOOLEAN")
	private Boolean docGenFlag = false;

	@Column(name = "\"DOCUMENT\"", columnDefinition = "BLOB")
	private String document;	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getSfEntityFlag() {
		return sfEntityFlag;
	}

	public void setSfEntityFlag(Boolean sfEntityFlag) {
		this.sfEntityFlag = sfEntityFlag;
	}

	public Boolean getPexUpdateFlag() {
		return pexUpdateFlag;
	}

	public void setPexUpdateFlag(Boolean pexUpdateFlag) {
		this.pexUpdateFlag = pexUpdateFlag;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Boolean getDocGenFlag() {
		return docGenFlag;
	}

	public void setDocGenFlag(Boolean docGenFlag) {
		this.docGenFlag = docGenFlag;
	}


}
