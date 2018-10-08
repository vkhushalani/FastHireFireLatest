package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.Field;

public interface FieldService {
	public List<Field> findAll();
	public Field update(Field item);
	public Field create(Field item);
	public Field findById(String id);
	public void deleteByObject(Field item);
}
