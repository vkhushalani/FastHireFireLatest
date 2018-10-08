package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.FieldText;

public interface FieldTextService {
	public List<FieldText> findAll();
	public FieldText update(FieldText item);
	public FieldText create(FieldText item);
	public List<FieldText> findByFieldId(String fieldId);
	public FieldText findByFieldLanguage(String fieldId,String language);
	public void deleteByObject(FieldText item);

}
