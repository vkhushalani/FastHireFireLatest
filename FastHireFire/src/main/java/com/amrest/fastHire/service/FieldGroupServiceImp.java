package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.FieldGroup;

@Transactional
@Component
public class FieldGroupServiceImp implements FieldGroupService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FieldGroup> findAll() {
		Query query = em.createNamedQuery("FieldGroup.findAll");
		 List<FieldGroup> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public FieldGroup update(FieldGroup item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public FieldGroup create(FieldGroup item) {
		em.persist(item);
	       return item;
	}

	@Override
	public FieldGroup findById(String id) {
		FieldGroup item = em.find(FieldGroup.class, id);
		return item;
	}

	@Override
	@Transactional
	public void deleteByObject(FieldGroup item) {
		em.remove(item);

	}

}
