
package com.amrest.fastHire.POJO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true) 
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "templateFieldGroupId",
    "fieldSeq",
    "endDate",
    "isVisibleCandidate",
    "isMandatoryManager",
    "isEditableManager",
    "dropDownValues",
    "field",
    "isVisibleManager",
    "isMandatoryCandidate",
    "value",
    "isEditableCandidate",
    "startDate",
    "fieldId",
    "valueState"
})
public class Field {

    @JsonProperty("templateFieldGroupId")
    private String templateFieldGroupId;
    @JsonProperty("fieldSeq")
    private Integer fieldSeq;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("isVisibleCandidate")
    private Boolean isVisibleCandidate;
    @JsonProperty("isMandatoryManager")
    private Boolean isMandatoryManager;
    @JsonProperty("isEditableManager")
    private Boolean isEditableManager;
    @JsonProperty("dropDownValues")
    private List<DropDownValue> dropDownValues = null;
    @JsonProperty("field")
    private Field_ field;
    @JsonProperty("isVisibleManager")
    private Boolean isVisibleManager;
    @JsonProperty("isMandatoryCandidate")
    private Boolean isMandatoryCandidate;
    @JsonProperty("value")
    private String value;
    @JsonProperty("isEditableCandidate")
    private Boolean isEditableCandidate;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("fieldId")
    private String fieldId;
    
    @JsonProperty("valueState")
    private String valueState;

    @JsonProperty("templateFieldGroupId")
    public String getTemplateFieldGroupId() {
        return templateFieldGroupId;
    }

    @JsonProperty("templateFieldGroupId")
    public void setTemplateFieldGroupId(String templateFieldGroupId) {
        this.templateFieldGroupId = templateFieldGroupId;
    }

    @JsonProperty("fieldSeq")
    public Integer getFieldSeq() {
        return fieldSeq;
    }

    @JsonProperty("fieldSeq")
    public void setFieldSeq(Integer fieldSeq) {
        this.fieldSeq = fieldSeq;
    }

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("isVisibleCandidate")
    public Boolean getIsVisibleCandidate() {
        return isVisibleCandidate;
    }

    @JsonProperty("isVisibleCandidate")
    public void setIsVisibleCandidate(Boolean isVisibleCandidate) {
        this.isVisibleCandidate = isVisibleCandidate;
    }

    @JsonProperty("isMandatoryManager")
    public Boolean getIsMandatoryManager() {
        return isMandatoryManager;
    }

    @JsonProperty("isMandatoryManager")
    public void setIsMandatoryManager(Boolean isMandatoryManager) {
        this.isMandatoryManager = isMandatoryManager;
    }

    @JsonProperty("isEditableManager")
    public Boolean getIsEditableManager() {
        return isEditableManager;
    }

    @JsonProperty("isEditableManager")
    public void setIsEditableManager(Boolean isEditableManager) {
        this.isEditableManager = isEditableManager;
    }

    @JsonProperty("dropDownValues")
    public List<DropDownValue> getDropDownValues() {
        return dropDownValues;
    }

    @JsonProperty("dropDownValues")
    public void setDropDownValues(List<DropDownValue> dropDownValues) {
        this.dropDownValues = dropDownValues;
    }

    @JsonProperty("field")
    public Field_ getField() {
        return field;
    }

    @JsonProperty("field")
    public void setField(Field_ field) {
        this.field = field;
    }

    @JsonProperty("isVisibleManager")
    public Boolean getIsVisibleManager() {
        return isVisibleManager;
    }

    @JsonProperty("isVisibleManager")
    public void setIsVisibleManager(Boolean isVisibleManager) {
        this.isVisibleManager = isVisibleManager;
    }

    @JsonProperty("isMandatoryCandidate")
    public Boolean getIsMandatoryCandidate() {
        return isMandatoryCandidate;
    }

    @JsonProperty("isMandatoryCandidate")
    public void setIsMandatoryCandidate(Boolean isMandatoryCandidate) {
        this.isMandatoryCandidate = isMandatoryCandidate;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("isEditableCandidate")
    public Boolean getIsEditableCandidate() {
        return isEditableCandidate;
    }

    @JsonProperty("isEditableCandidate")
    public void setIsEditableCandidate(Boolean isEditableCandidate) {
        this.isEditableCandidate = isEditableCandidate;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("fieldId")
    public String getFieldId() {
        return fieldId;
    }

    @JsonProperty("fieldId")
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    @JsonProperty("valueState")
	public String getValueState() {
		return valueState;
	}

    @JsonProperty("valueState")
	public void setValueState(String valueState) {
		this.valueState = valueState;
	}

}
