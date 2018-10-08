package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.Country;

@Transactional
@Component
public class CountryServiceImp implements CountryService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Country> findAll() {
		Query query = em.createNamedQuery("Country.findAll");
		 List<Country> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public Country update(Country item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public Country create(Country item) {
		em.persist(item);
	       return item;
	}

	@Override
	public Country findById(String id) {
		Country item = em.find(Country.class, id);
		return item;
	}

	@Override
	@Transactional
	public void deleteByObject(Country item) {
		em.remove(item);

	}

}
