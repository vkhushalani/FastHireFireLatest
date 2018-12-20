package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.ContractCriteria;

@Transactional
@Component
public class ContractCriteriaServiceImp implements ContractCriteriaService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContractCriteria> findByCountryCompany(String country, String company) {
		Query query = em.createNamedQuery("ContractCriteria.findByCountryCompany")
				.setParameter("country", country)
				.setParameter("company", company);
		 List<ContractCriteria> items = query.getResultList();
	        return items;
	}

}
