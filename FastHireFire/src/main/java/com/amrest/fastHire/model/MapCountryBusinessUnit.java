package com.amrest.fastHire.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_MAP_COUNTRY_BUSINESS_UNITS\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "MapCountryBusinessUnit.findAll", query = "SELECT map FROM MapCountryBusinessUnit map"),
		@NamedQuery(name = "MapCountryBusinessUnit.findByCountry", query = "SELECT map FROM MapCountryBusinessUnit map WHERE map.countryId = :countryId"),
		@NamedQuery(name = "MapCountryBusinessUnit.findByCountryBusinessUnit", query = "SELECT map FROM MapCountryBusinessUnit map WHERE map.countryId = :countryId AND map.businessUnitId = :businessUnitId")
})
public class MapCountryBusinessUnit {
	
	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(32)")
	private String id;
	
	@Column(name = "\"COUNTRY.ID\"", columnDefinition = "VARCHAR(32)")
	private String countryId;
	
	@Column(name = "\"BUSINESS_UNIT.ID\"", columnDefinition = "VARCHAR(32)")
	private String businessUnitId;
	
	@Column(name = "\"START_DATE\"",columnDefinition = "SECONDDATE")
    private Date startDate;
	
	@Column(name = "\"END_DATE\"",columnDefinition = "SECONDDATE")
    private Date endDate;
	
	

	public String getCountryId() {
		return countryId;
	}


	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}


	public String getBusinessUnitId() {
		return businessUnitId;
	}


	public void setBusinessUnitId(String businessUnitId) {
		this.businessUnitId = businessUnitId;
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


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}



}
