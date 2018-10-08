package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.CodeList;

public interface CodeListService {
	public List<CodeList> findAll();
	public CodeList update(CodeList item);
	public CodeList create(CodeList item);
	public CodeList findById(String id);
	public List<CodeList> findByFieldId(String fieldId);
	public CodeList findByCountryField(String fieldId,String countryId);
	public CodeList findByCountryFieldDependent(String fieldId,String countryId,String dependentFieldId, String dependentFieldValue);
	public void deleteByObject(CodeList item);
}
