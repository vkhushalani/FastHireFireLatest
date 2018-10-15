package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import com.amrest.fastHire.model.SFConstants;

@Transactional
@Component
public class SFConstantsServiceImp implements SFConstantsService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SFConstants> findAll() {
		
		Query query = em.createNamedQuery("SFConstants.findAll");
		 List<SFConstants> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public SFConstants update(SFConstants item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public SFConstants create(SFConstants item) {
		em.persist(item);
		return item;
	}

	@Override
	public SFConstants findById(String technicalName) {
		SFConstants item = em.find(SFConstants.class, technicalName);
		return item;
	}

	@Override
	@Transactional
	public void deleteByObject(SFConstants item) {
		em.remove(item);

	}

}
