package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.ContractCriteria;

public interface ContractCriteriaService {
	public List<ContractCriteria> findByCountryCompany(String country , String company);
}
