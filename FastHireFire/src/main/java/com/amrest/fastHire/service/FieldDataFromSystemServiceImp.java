package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.FieldDataFromSystem;

@Transactional
@Component
public class FieldDataFromSystemServiceImp implements FieldDataFromSystemService {
	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FieldDataFromSystem> findAll() {
		Query query = em.createNamedQuery("FieldDataFromSystem.findAll");
		 List<FieldDataFromSystem> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public FieldDataFromSystem update(FieldDataFromSystem item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public FieldDataFromSystem create(FieldDataFromSystem item) {
		em.persist(item);
	       return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FieldDataFromSystem> findByField(String fieldId) {
		Query query = em.createNamedQuery("FieldDataFromSystem.findByField")
				.setParameter("fieldId", fieldId);
		 List<FieldDataFromSystem> items = query.getResultList();
	        return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FieldDataFromSystem> findByFieldCountry(String fieldId, String countryId) {
		Query query = em.createNamedQuery("FieldDataFromSystem.findByFieldCountry")
				.setParameter("fieldId", fieldId)
				.setParameter("countryId", countryId);
		 List<FieldDataFromSystem> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public void deleteByObject(FieldDataFromSystem item) {
		em.remove(item);

	}

}
