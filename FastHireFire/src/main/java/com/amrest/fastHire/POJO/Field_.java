
package com.amrest.fastHire.POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true) 
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "endDate",
    "entityName",
    "dataType",
    "length",
    "name",
    "valueFromPath",
    "id",
    "technicalName",
    "fieldType",
    "createdOn",
    "initialValue",
    "startDate"
})
public class Field_ {

    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("entityName")
    private String entityName;
    @JsonProperty("dataType")
    private String dataType;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("name")
    private String name;
    @JsonProperty("valueFromPath")
    private String valueFromPath;
    @JsonProperty("id")
    private String id;
    @JsonProperty("technicalName")
    private String technicalName;
    @JsonProperty("fieldType")
    private String fieldType;
    @JsonProperty("createdOn")
    private String createdOn;
    
    @JsonProperty("initialValue")
    private String initialValue;
    
    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("entityName")
    public String getEntityName() {
        return entityName;
    }

    @JsonProperty("entityName")
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @JsonProperty("dataType")
    public String getDataType() {
        return dataType;
    }

    @JsonProperty("dataType")
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(Integer length) {
        this.length = length;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("valueFromPath")
    public String getValueFromPath() {
        return valueFromPath;
    }

    @JsonProperty("valueFromPath")
    public void setValueFromPath(String valueFromPath) {
        this.valueFromPath = valueFromPath;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("technicalName")
    public String getTechnicalName() {
        return technicalName;
    }

    @JsonProperty("technicalName")
    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

    @JsonProperty("fieldType")
    public String getFieldType() {
        return fieldType;
    }

    @JsonProperty("fieldType")
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @JsonProperty("createdOn")
    public String getCreatedOn() {
        return createdOn;
    }

    @JsonProperty("createdOn")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("initialValue")
	public String getInitialValue() {
		return initialValue;
	}

    @JsonProperty("initialValue")
	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

}
