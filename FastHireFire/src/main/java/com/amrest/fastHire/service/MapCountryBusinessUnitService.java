package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.MapCountryBusinessUnit;

public interface MapCountryBusinessUnitService {
	public List<MapCountryBusinessUnit> findAll();
	public MapCountryBusinessUnit create(MapCountryBusinessUnit item);
	public MapCountryBusinessUnit findById(String id);
	public List<MapCountryBusinessUnit> findByCountry(String countryId);
	public MapCountryBusinessUnit findByCountryBusinessUnit(String countryId, String businessUnitId);
	public void deleteByObject(MapCountryBusinessUnit item);
}
