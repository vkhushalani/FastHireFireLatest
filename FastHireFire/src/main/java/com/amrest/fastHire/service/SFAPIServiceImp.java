package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.SFAPI;

@Transactional
@Component
public class SFAPIServiceImp implements SFAPIService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SFAPI> findAll() {
		Query query = em.createNamedQuery("SFAPI.findAll");
		 List<SFAPI> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public SFAPI update(SFAPI item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public SFAPI create(SFAPI item) {
		em.persist(item);
	       return item;
	}

	@Override
	public SFAPI findById(String entityName, String operation) {
		try{
		Query query = em.createNamedQuery("SFAPI.findById")
				.setParameter("operation",operation)
				.setParameter("entityName", entityName);
		SFAPI item = (SFAPI) query.getSingleResult();
		 return item;
		}
		catch (NoResultException e)
		{
			return null;
		}
		}

	@SuppressWarnings("unchecked")
	@Override
	public List<SFAPI> findByEntityName(String entityName) {
		Query query = em.createNamedQuery("SFAPI.findById")
				.setParameter("entityName", entityName);
		List<SFAPI> items =  query.getResultList();
		 return items;
	}

	@Override
	@Transactional
	public void deleteByObject(SFAPI item) {
		em.remove(item);

	}

}
