package com.amrest.fastHire.model;


import java.util.Date;

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
	private String sfEntityFlag = "";
	
	@Column(name = "\"PEX\"", columnDefinition = "VARCHAR(16)")
	private String pexUpdateFlag = "";
	
	@Column(name = "\"DOC_GEN\"", columnDefinition = "VARCHAR(16)")
	private String docGenFlag = "";
	
	@Column(name = "\"UPDATED_ON\"", columnDefinition = "SECONDDATE")
	private Date updatedOn;
	
	@Column(name = "\"ENTITY_NAME\"", columnDefinition = "VARCHAR(16)")
	private String entityName;
	
	@Column(name = "\"COMPANY\"", columnDefinition = "VARCHAR(32)")
	private String company;

	@Column(name = "\"DEPARTMENT\"", columnDefinition = "VARCHAR(32)")
	private String department;	
	
	@Column(name = "\"POSITION\"", columnDefinition = "VARCHAR(32)")
	private String position;	
	
	@Column(name = "\"START_DATE\"", columnDefinition = "VARCHAR(64)")
	private String startDate;
	
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

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



}
