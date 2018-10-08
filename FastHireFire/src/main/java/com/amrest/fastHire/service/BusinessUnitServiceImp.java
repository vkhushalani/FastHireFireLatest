package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.BusinessUnit;

@Transactional
@Component
public class BusinessUnitServiceImp implements BusinessUnitService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessUnit> findAll() {
		Query query = em.createNamedQuery("BusinessUnit.findAll");
		 List<BusinessUnit> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public BusinessUnit update(BusinessUnit item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public BusinessUnit create(BusinessUnit item) {
		em.persist(item);
		return item;
	}

	@Override
	public BusinessUnit findById(String id) {
		BusinessUnit item = em.find(BusinessUnit.class, id);
		return item;
	}

	@Override
	public BusinessUnit findDefaultBusinessUnit(Boolean isDefault) {
		Query query = em.createNamedQuery("BusinessUnit.findDefaultBusinessUnit")
						.setParameter("isDefault",isDefault);
		 BusinessUnit item = (BusinessUnit) query.getSingleResult();
	     return item;
	}

	@Override
	@Transactional
	public void deleteByObject(BusinessUnit item) {
		em.remove(item);

	}

}
