package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.FieldDataFromSystem;

public interface FieldDataFromSystemService {
	
	public List<FieldDataFromSystem> findAll();
	public FieldDataFromSystem update(FieldDataFromSystem item);
	public FieldDataFromSystem create(FieldDataFromSystem item);
	public List<FieldDataFromSystem> findByField(String fieldId);
	public List<FieldDataFromSystem> findByFieldCountry(String fieldId,String countryId);
	public void deleteByObject(FieldDataFromSystem item);

}
