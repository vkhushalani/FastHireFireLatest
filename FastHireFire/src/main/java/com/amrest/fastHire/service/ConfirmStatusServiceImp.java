package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.ConfirmStatus;

public class ConfirmStatusServiceImp implements ConfirmStatusService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfirmStatus> findAll() {
		Query query = em.createNamedQuery("ConfirmStatus.findAll");
		 List<ConfirmStatus> items = query.getResultList();
	        return items;
	}

	@Override
	public ConfirmStatus findById(String id) {
		ConfirmStatus item = em.find(ConfirmStatus.class, id);
		return item;
	}

	@Override
	@Transactional
	public ConfirmStatus update(ConfirmStatus item) {
		em.merge(item);
	       return item;	}

	@Override
	@Transactional
	public ConfirmStatus create(ConfirmStatus item) {
		em.persist(item);
		return item;
	}

	@Override
	@Transactional
	public void deleteByObject(ConfirmStatus item) {
		em.remove(item);
	}

}
