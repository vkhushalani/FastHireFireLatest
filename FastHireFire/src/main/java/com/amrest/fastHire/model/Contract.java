package com.amrest.fastHire.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"com.amrest.ph.db::Table.FHD_CONTRACT\"", schema = "AMREST_PREHIRE")
public class Contract {
	
	@Id
	@Column(name = "\"ID\"", columnDefinition = "VARCHAR(5000)")
	private String id;
	
	@Column(name = "\"TEMPLATE\"", columnDefinition = "VARCHAR(132)")
	private String template;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	


}
