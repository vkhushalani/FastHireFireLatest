package com.amrest.fastHire.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_HIRING_TEMPLATES\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "Template.findAll", query = "SELECT t FROM Template t"),
		@NamedQuery(name = "Template.findDefaultTemplate", query = "SELECT t FROM Template t WHERE t.isDefault = :isDefault")
		
})
public class Template {
	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(32)")
	private String id;
	
	@Column(name = "\"NAME\"",columnDefinition = "VARCHAR(32)")
     private String name;
	
//	@Column(name = "\"STATUS\"", columnDefinition = "BOOLEAN")
//	private Boolean status;
	
	@Column(name = "\"CATEGORY\"",columnDefinition = "VARCHAR(32)")
     private String category;
	
	@Column(name = "\"IS_DEFAULT\"", columnDefinition = "BOOLEAN")
	private Boolean isDefault;
	
	@Column(name = "\"DESCRIPTION\"",columnDefinition = "VARCHAR(152)")
    private String description;
	
	@Column(name = "\"CREATED_ON\"",columnDefinition = "SECONDDATE")
    private Date createdOn;
	
	@Column(name = "\"START_DATE\"",columnDefinition = "SECONDDATE")
    private Date startDate;
	
	@Column(name = "\"END_DATE\"",columnDefinition = "SECONDDATE")
    private Date endDate;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public Boolean getStatus() {
//		return status;
//	}
//
//	public void setStatus(Boolean status) {
//		this.status = status;
//	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
