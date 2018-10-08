package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.Country;

public interface CountryService {
	public List<Country> findAll();
	public Country update(Country item);
	public Country create(Country item);
	public Country findById(String id);
	public void deleteByObject(Country item);
}
