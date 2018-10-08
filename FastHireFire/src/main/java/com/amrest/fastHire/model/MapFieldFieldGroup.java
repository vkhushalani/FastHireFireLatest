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
@Table(name = "\"com.amrest.ph.db::Table.FHD_MAP_FIELD_GROUP_FIELDS\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "MapFieldFieldGroup.findAll", query = "SELECT map FROM MapFieldFieldGroup map WHERE :todayDate BETWEEN map.startDate AND map.endDate"),
		@NamedQuery(name = "MapFieldFieldGroup.findByFieldGroupId", query = "SELECT map FROM MapFieldFieldGroup map WHERE map.fieldGroupId = :fieldGroupId"),
		@NamedQuery(name = "MapFieldFieldGroup.findByFieldGroupFieldId", query = "SELECT map FROM MapFieldFieldGroup map WHERE map.fieldGroupId = :fieldGroupId AND map.fieldId = :fieldId")
})
public class MapFieldFieldGroup {
	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(32)")
	private String id;
	
	@Column(name = "\"FIELD_GROUP.ID\"", columnDefinition = "VARCHAR(32)")
	private String fieldGroupId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="\"FIELD_GROUP.ID\"",referencedColumnName="\"ID\"",insertable=false, updatable=false)
	private FieldGroup fieldGroup;
	
	@Column(name = "\"FIELD.ID\"", columnDefinition = "VARCHAR(32)")
	private String fieldId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="\"FIELD.ID\"",referencedColumnName="\"ID\"",insertable=false, updatable=false)
	private Field field;
	
	@Column(name = "\"SF_ENTITY_NAME\"", columnDefinition = "VARCHAR(32)")
	private String entityName;
	
	@Column(name = "\"SF_ENTITY_POST_SEQ\"", columnDefinition = "VARCHAR(32)")
	private String postSequence;
	
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

	public String getFieldGroupId() {
		return fieldGroupId;
	}

	public void setFieldGroupId(String fieldGroupId) {
		this.fieldGroupId = fieldGroupId;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
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

	public FieldGroup getFieldGroup() {
		return fieldGroup;
	}

	public void setFieldGroup(FieldGroup fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getPostSequence() {
		return postSequence;
	}

	public void setPostSequence(String postSequence) {
		this.postSequence = postSequence;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

}
