package com.amrest.fastHire.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_MAP_COUNTRY_UNIT_TEMPLATES\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "MapCountryBusinessUnitTemplate.findAll", query = "SELECT t FROM MapCountryBusinessUnitTemplate t"),
		@NamedQuery(name = "MapCountryBusinessUnitTemplate.findByCountryBusinessUnitId", query = "SELECT t FROM MapCountryBusinessUnitTemplate t WHERE t.countryBusinessUnitId = :countryBusinessUnitId"),
		@NamedQuery(name = "MapCountryBusinessUnitTemplate.findByCountryBusinessUnitTemplate", query = "SELECT t FROM MapCountryBusinessUnitTemplate t WHERE t.countryBusinessUnitId = :countryBusinessUnitId AND t.templateId = :templateId")
})
public class MapCountryBusinessUnitTemplate {
	
	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(32)")
	private String id;
	
	@Column(name = "\"COUNTRY_BUSINESS_UNIT.ID\"", columnDefinition = "VARCHAR(32)")
	private String countryBusinessUnitId;
	
	@Column(name = "\"HIRING_TEMPLATE.ID\"", columnDefinition = "VARCHAR(32)")
	private String templateId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="\"HIRING_TEMPLATE.ID\"",referencedColumnName="\"ID\"",insertable=false, updatable=false)
	private Template template;
	
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

	public String getCountryBusinessUnitId() {
		return countryBusinessUnitId;
	}

	public void setCountryBusinessUnitId(String countryBusinessUnitId) {
		this.countryBusinessUnitId = countryBusinessUnitId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

}
