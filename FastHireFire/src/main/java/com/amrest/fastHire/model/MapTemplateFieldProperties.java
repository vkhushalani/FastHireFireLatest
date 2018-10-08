package com.amrest.fastHire.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.amrest.fastHire.utilities.DropDownKeyValue;


@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_MAP_TEMPLATE_FIELD_PROPERTIES\"", schema = "AMREST_PREHIRE")
@NamedQueries({ 
		@NamedQuery(name = "MapTemplateFieldProperties.findAll", query = "SELECT map FROM MapTemplateFieldProperties map"),
		@NamedQuery(name = "MapTemplateFieldProperties.findByTemplateFieldGroup", query = "SELECT map FROM MapTemplateFieldProperties map WHERE map.templateFieldGroupId = :templateFieldGroupId"),
		@NamedQuery(name = "MapTemplateFieldProperties.findByTemplateFieldGroupManager", query = "SELECT map FROM MapTemplateFieldProperties map WHERE map.templateFieldGroupId = :templateFieldGroupId AND map.isVisibleManager = :isVisibleManager"),
		@NamedQuery(name = "MapTemplateFieldProperties.findByTemplateFieldGroupCandidate", query = "SELECT map FROM MapTemplateFieldProperties map WHERE map.templateFieldGroupId = :templateFieldGroupId AND map.isVisibleCandidate = :isVisibleCandidate"),
		@NamedQuery(name = "MapTemplateFieldProperties.findById", query = "SELECT map FROM MapTemplateFieldProperties map WHERE map.templateFieldGroupId = :templateFieldGroupId AND map.fieldId = :fieldId"),
		@NamedQuery(name = "MapTemplateFieldProperties.findByFieldIdVisibleManager", query = "SELECT map FROM MapTemplateFieldProperties map WHERE map.isVisibleManager = :isVisibleManager AND map.fieldId = :fieldId")
		
})
public class MapTemplateFieldProperties implements Comparable<MapTemplateFieldProperties>{
	@Id
	@Column(name = "\"TEMPLATE_FIELD_GROUP.ID\"", columnDefinition = "VARCHAR(32)")
	private String templateFieldGroupId;
	
	@Id
	@Column(name = "\"FIELD.ID\"", columnDefinition = "VARCHAR(32)")
	private String fieldId;
	
	@ManyToOne (cascade = CascadeType.ALL)
	@JoinColumn(name="\"FIELD.ID\"",referencedColumnName="\"ID\"",insertable=false, updatable=false)
	private Field field;
	
	@Column(name = "\"FIELD_SEQ\"", columnDefinition = "INTEGER")
	private Integer fieldSeq;
	
	@Column(name = "\"VALUE\"", columnDefinition = "VARCHAR(32)")
	private String value;
	
	@Transient 
	private List<DropDownKeyValue> dropDownValues;
	
	@Column(name = "\"IS_EDITABLE_MANAGER\"", columnDefinition = "BOOLEAN")
	private Boolean isEditableManager;
	
	@Column(name = "\"IS_EDITABLE_CANDIDATE\"", columnDefinition = "BOOLEAN")
	private Boolean isEditableCandidate;
	
	@Column(name = "\"IS_VISIBLE_MANAGER\"", columnDefinition = "VARCHAR(10)")
	private Boolean isVisibleManager;
	
	@Column(name = "\"IS_VISIBLE_CANDIDATE\"", columnDefinition = "BOOLEAN")
	private Boolean isVisibleCandidate;
	
	@Column(name = "\"IS_MANDATORY_MANAGER\"", columnDefinition = "BOOLEAN")
	private Boolean isMandatoryManager;
	
	@Column(name = "\"IS_MANDATORY_CANDIDATE\"", columnDefinition = "BOOLEAN")
	private Boolean isMandatoryCandidate;
	
	@Column(name = "\"START_DATE\"",columnDefinition = "SECONDDATE")
    private Date startDate;
	
	@Column(name = "\"END_DATE\"",columnDefinition = "SECONDDATE")
    private Date endDate;

	

	public MapTemplateFieldProperties() {}

	public MapTemplateFieldProperties(String templateFieldGroupId, String fieldId, Field field, Integer fieldSeq,
			String value, Boolean isEditableManager, Boolean isEditableCandidate, Boolean isVisibleManager,
			Boolean isVisibleCandidate, Boolean isMandatoryManager, Boolean isMandatoryCandidate, Date startDate,
			Date endDate) {
		this.templateFieldGroupId = templateFieldGroupId;
		this.fieldId = fieldId;
		this.field = field;
		this.fieldSeq = fieldSeq;
		this.value = value;
		this.isEditableManager = isEditableManager;
		this.isEditableCandidate = isEditableCandidate;
		this.isVisibleManager = isVisibleManager;
		this.isVisibleCandidate = isVisibleCandidate;
		this.isMandatoryManager = isMandatoryManager;
		this.isMandatoryCandidate = isMandatoryCandidate;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getIsEditableManager() {
		return isEditableManager;
	}

	public void setIsEditableManager(Boolean isEditableManager) {
		this.isEditableManager = isEditableManager;
	}

	public Boolean getIsEditableCandidate() {
		return isEditableCandidate;
	}

	public void setIsEditableCandidate(Boolean isEditableCandidate) {
		this.isEditableCandidate = isEditableCandidate;
	}

	public Boolean getIsVisibleManager() {
		return isVisibleManager;
	}

	public void setIsVisibleManager(Boolean isVisibleManager) {
		this.isVisibleManager = isVisibleManager;
	}

	public Boolean getIsVisibleCandidate() {
		return isVisibleCandidate;
	}

	public void setIsVisibleCandidate(Boolean isVisibleCandidate) {
		this.isVisibleCandidate = isVisibleCandidate;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Boolean getIsMandatoryCandidate() {
		return isMandatoryCandidate;
	}

	public void setIsMandatoryCandidate(Boolean isMandatoryCandidate) {
		this.isMandatoryCandidate = isMandatoryCandidate;
	}

	public Boolean getIsMandatoryManager() {
		return isMandatoryManager;
	}

	public void setIsMandatoryManager(Boolean isMandatoryManager) {
		this.isMandatoryManager = isMandatoryManager;
	}

	public Integer getFieldSeq() {
		return fieldSeq;
	}

	public void setFieldSeq(Integer fieldSeq) {
		this.fieldSeq = fieldSeq;
	}

	public String getTemplateFieldGroupId() {
		return templateFieldGroupId;
	}

	public void setTemplateFieldGroupId(String templateFieldGroupId) {
		this.templateFieldGroupId = templateFieldGroupId;
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

	public List<DropDownKeyValue> getDropDownValues() {
		return dropDownValues;
	}

	public void setDropDownValues(List<DropDownKeyValue> dropDownValues) {
		this.dropDownValues = dropDownValues;
	}
	
	@Override
	public int compareTo(MapTemplateFieldProperties o) {
		int compareValue=o.getFieldSeq().intValue();
        /* For Ascending order*/
        return this.fieldSeq.intValue()-compareValue;

        /* For Descending order do like this */
        //return compareage-this.studentage;
//		return 0;
	}


}
