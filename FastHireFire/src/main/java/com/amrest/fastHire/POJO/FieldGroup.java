
package com.amrest.fastHire.POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true) 
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fieldGroupSeq",
    "endDate",
    "name",
    "id",
    "createdOn",
    "startDate"
})
public class FieldGroup {

    @JsonProperty("fieldGroupSeq")
    private Integer fieldGroupSeq;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private String id;
    @JsonProperty("createdOn")
    private String createdOn;
    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("fieldGroupSeq")
    public Integer getFieldGroupSeq() {
        return fieldGroupSeq;
    }

    @JsonProperty("fieldGroupSeq")
    public void setFieldGroupSeq(Integer fieldGroupSeq) {
        this.fieldGroupSeq = fieldGroupSeq;
    }

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
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

}
