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
@Table(name = "\"com.amrest.ph.db::Table.FHD_MAP_TEMPLATE_FIELD_GROUPS\"", schema = "AMREST_PREHIRE")
@NamedQueries({
		@NamedQuery(name = "MapTemplateFieldGroup.findAll", query = "SELECT map FROM MapTemplateFieldGroup map"),
		@NamedQuery(name = "MapTemplateFieldGroup.findByTemplate", query = "SELECT map FROM MapTemplateFieldGroup map WHERE map.templateId = :templateId"),
		@NamedQuery(name = "MapTemplateFieldGroup.findByTemplateFieldGroup", query = "SELECT map FROM MapTemplateFieldGroup map WHERE map.templateId = :templateId AND map.fieldGroupId = :fieldGroupId") })
public class MapTemplateFieldGroup implements Comparable<MapTemplateFieldGroup>{

	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(32)")
	private String id;

	@Column(name = "\"HIRING_TEMPLATE.ID\"", columnDefinition = "VARCHAR(32)")
	private String templateId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "\"HIRING_TEMPLATE.ID\"", referencedColumnName = "\"ID\"", insertable = false, updatable = false)
	private Template template;

	@Column(name = "\"FIELD_GROUP.ID\"", columnDefinition = "VARCHAR(32)")
	private String fieldGroupId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "\"FIELD_GROUP.ID\"", referencedColumnName = "\"ID\"", insertable = false, updatable = false)
	private FieldGroup fieldGroup;

	@Column(name = "\"FIELD_GROUP_SEQ\"", columnDefinition = "INTEGER")
	private Integer fieldGroupSeq;

	@Column(name = "\"START_DATE\"", columnDefinition = "SECONDDATE")
	private Date startDate;

	@Column(name = "\"END_DATE\"", columnDefinition = "SECONDDATE")
	private Date endDate;

	@Column(name = "\"IS_EDITABLE_MANAGER\"", columnDefinition = "BOOLEAN")
	private Boolean isEditableManager;

	@Column(name = "\"IS_EDITABLE_CANDIDATE\"", columnDefinition = "BOOLEAN")
	private Boolean isEditableCandidate;

	@Column(name = "\"IS_VISIBLE_MANAGER\"", columnDefinition = "VARCHAR(10)")
	private Boolean isVisibleManager;

	@Column(name = "\"IS_VISIBLE_CANDIDATE\"", columnDefinition = "BOOLEAN")
	private Boolean isVisibleCandidate;

	public MapTemplateFieldGroup() {}

	public MapTemplateFieldGroup(String id, String templateId, Template template, String fieldGroupId,
			FieldGroup fieldGroup, Integer fieldGroupSeq, Date startDate, Date endDate, Boolean isEditableManager,
			Boolean isEditableCandidate, Boolean isVisibleManager, Boolean isVisibleCandidate) {
		this.id = id;
		this.templateId = templateId;
		this.template = template;
		this.fieldGroupId = fieldGroupId;
		this.fieldGroup = fieldGroup;
		this.fieldGroupSeq = fieldGroupSeq;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isEditableManager = isEditableManager;
		this.isEditableCandidate = isEditableCandidate;
		this.isVisibleManager = isVisibleManager;
		this.isVisibleCandidate = isVisibleCandidate;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Integer getFieldGroupSeq() {
		return fieldGroupSeq;
	}

	public void setFieldGroupSeq(Integer fieldGroupSeq) {
		this.fieldGroupSeq = fieldGroupSeq;
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

	public String getFieldGroupId() {
		return fieldGroupId;
	}

	public void setFieldGroupId(String fieldGroupId) {
		this.fieldGroupId = fieldGroupId;
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

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public FieldGroup getFieldGroup() {
		return fieldGroup;
	}

	public void setFieldGroup(FieldGroup fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

	@Override
	public String toString() {

		String string = "{" + "\"id\"" + ":" + "\"" + id + "\"," + "\"templateId\"" + ":" + "\"" + templateId + "\","
				+ "\"fieldGroupId\"" + ":" + "\"" + fieldGroupId + "\"," + "\"isVisibleManager\"" + ":" + "\""
				+ isVisibleManager + "\"" + "}";

		return string;
	}

	@Override
	public int compareTo(MapTemplateFieldGroup o) {
		int compareValue=o.getFieldGroupSeq().intValue();
        /* For Ascending order*/
        return this.fieldGroupSeq.intValue()-compareValue;

        /* For Descending order do like this */
        //return compareage-this.studentage;
//		return 0;
	}

}