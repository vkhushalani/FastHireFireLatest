package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.FieldGroup;

public interface FieldGroupService {
	public List<FieldGroup> findAll();
	public FieldGroup update(FieldGroup item);
	public FieldGroup create(FieldGroup item);
	public FieldGroup findById(String id);
	public void deleteByObject(FieldGroup item);
}
