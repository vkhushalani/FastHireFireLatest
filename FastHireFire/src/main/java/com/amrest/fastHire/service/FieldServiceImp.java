package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.Field;

@Transactional
@Component
public class FieldServiceImp implements FieldService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Field> findAll() {
		Query query = em.createNamedQuery("Field.findAll");
		 List<Field> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public Field update(Field item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public Field create(Field item) {
		em.persist(item);
	       return item;
	}

	@Override
	public Field findById(String id) {
		Field item = em.find(Field.class, id);
		return item;
	}

	@Override
	@Transactional
	public void deleteByObject(Field item) {
		em.remove(item);

	}

}
