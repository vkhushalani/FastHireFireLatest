package com.amrest.fastHire.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_CONFIRM_STATUS\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "ConfirmStatus.findAll", query = "SELECT cs FROM ConfirmStatus cs"),
		@NamedQuery(name = "ConfirmStatus.findByCountryDepartment", query = "SELECT cs FROM ConfirmStatus cs where cs.company = :company AND cs.department= :department")
		
})
public class ConfirmStatus {

	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(164)")
	private String id;
	
	@Column(name = "\"SF_ENTITY\"", columnDefinition = "VARCHAR(16)")
	private String sfEntityFlag = "BEGIN";
	
	@Column(name = "\"PEX\"", columnDefinition = "VARCHAR(16)")
	private String pexUpdateFlag = "BEGIN";
	
	@Column(name = "\"DOC_GEN\"", columnDefinition = "VARCHAR(16)")
	private String docGenFlag = "BEGIN";
	
	@Lob
	@Column(name = "\"DOCUMENT\"", columnDefinition = "BLOB")
	private byte[] document;
	
	@Column(name = "\"COMPANY\"", columnDefinition = "VARCHAR(32)")
	private String company;

	@Column(name = "\"DEPARTMENT\"", columnDefinition = "VARCHAR(32)")
	private String department;	
	
	@Column(name = "\"POSITION\"", columnDefinition = "VARCHAR(32)")
	private String position;	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSfEntityFlag() {
		return sfEntityFlag;
	}

	public void setSfEntityFlag(String sfEntityFlag) {
		this.sfEntityFlag = sfEntityFlag;
	}

	public String getPexUpdateFlag() {
		return pexUpdateFlag;
	}

	public void setPexUpdateFlag(String pexUpdateFlag) {
		this.pexUpdateFlag = pexUpdateFlag;
	}

	public byte[] getDocument() {
		return document;
	}

	public void setDocument(byte[] document) {
		this.document = document;
	}

	public String getDocGenFlag() {
		return docGenFlag;
	}

	public void setDocGenFlag(String docGenFlag) {
		this.docGenFlag = docGenFlag;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}


}
