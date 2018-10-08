package com.amrest.fastHire.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_SFAPI\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "SFAPI.findAll", query = "SELECT sa FROM SFAPI sa"),
		@NamedQuery(name = "SFAPI.findById", query = "SELECT sa FROM SFAPI sa WHERE sa.entityName = :entityName and sa.operation = :operation"),
		@NamedQuery(name = "SFAPI.findByEntityName", query = "SELECT sa FROM SFAPI sa WHERE sa.entityName = :entityName"),
		
})
public class SFAPI {
	@Id
	@Column(name = "\"ENTITY_NAME\"", columnDefinition = "VARCHAR(64)")
	private String entityName;
	
	@Id
	@Column(name = "\"OPERATION\"",columnDefinition = "VARCHAR(9)")
     private String operation;
	
	@Column(name = "\"URL\"",columnDefinition = "VARCHAR(5000)")
    private String url;
	
	@Column(name = "\"REPLACE_TAG\"",columnDefinition = "VARCHAR(132)")
    private String replaceTag;
	
	@Column(name = "\"TAG_SOURCE\"",columnDefinition = "VARCHAR(132)")
    private String tagSource;
	
	@Column(name = "\"TAG_SOURCE_VALUE_PATH\"",columnDefinition = "VARCHAR(132)")
    private String tagSourceValuePath;
	
	@Column(name = "\"DESCRIPTION\"",columnDefinition = "VARCHAR(132)")
    private String description;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReplaceTag() {
		return replaceTag;
	}

	public void setReplaceTag(String replaceTag) {
		this.replaceTag = replaceTag;
	}

	public String getTagSource() {
		return tagSource;
	}

	public void setTagSource(String tagSource) {
		this.tagSource = tagSource;
	}

	public String getTagSourceValuePath() {
		return tagSourceValuePath;
	}

	public void setTagSourceValuePath(String tagSourceValuePath) {
		this.tagSourceValuePath = tagSourceValuePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
