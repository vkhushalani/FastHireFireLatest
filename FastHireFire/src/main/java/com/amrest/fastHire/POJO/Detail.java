
package com.amrest.fastHire.POJO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true) 
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fieldGroup",
    "fields"
})
public class Detail {

    @JsonProperty("fieldGroup")
    private FieldGroup fieldGroup;
    @JsonProperty("fields")
    private List<Field> fields = null;

    @JsonProperty("fieldGroup")
    public FieldGroup getFieldGroup() {
        return fieldGroup;
    }

    @JsonProperty("fieldGroup")
    public void setFieldGroup(FieldGroup fieldGroup) {
        this.fieldGroup = fieldGroup;
    }

    @JsonProperty("fields")
    public List<Field> getFields() {
        return fields;
    }

    @JsonProperty("fields")
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
