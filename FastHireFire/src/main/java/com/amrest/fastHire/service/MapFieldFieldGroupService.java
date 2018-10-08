package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.MapFieldFieldGroup;

public interface MapFieldFieldGroupService {
	public List<MapFieldFieldGroup> findAll();
	public MapFieldFieldGroup create(MapFieldFieldGroup item);
	public MapFieldFieldGroup findById(String id);
	public List<MapFieldFieldGroup>  findByFieldGroupId(String fieldGroupId);
	public List<MapFieldFieldGroup>  findByFieldGroupFieldId(String fieldGroupId,String fieldId);
	public void deleteByObject(MapFieldFieldGroup item);
	
}
