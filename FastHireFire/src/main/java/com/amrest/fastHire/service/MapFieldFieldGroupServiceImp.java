package com.amrest.fastHire.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.MapFieldFieldGroup;

@Transactional
@Component
public class MapFieldFieldGroupServiceImp implements MapFieldFieldGroupService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MapFieldFieldGroup> findAll() {
		Query query = em.createNamedQuery("MapFieldFieldGroup.findAll")
						.setParameter("todayDate", new Date());
		 List<MapFieldFieldGroup> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public MapFieldFieldGroup create(MapFieldFieldGroup item) {
		em.persist(item);
	       return item;
	}

	@Override
	public MapFieldFieldGroup findById(String id) {
		MapFieldFieldGroup item = em.find(MapFieldFieldGroup.class, id);
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapFieldFieldGroup> findByFieldGroupId(String fieldGroupId) {
		Query query = em.createNamedQuery("MapFieldFieldGroup.findByFieldGroupId")
				.setParameter("fieldGroupId",fieldGroupId);
		List<MapFieldFieldGroup> items =  query.getResultList();
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapFieldFieldGroup>  findByFieldGroupFieldId(String fieldGroupId, String fieldId) {
		Query query = em.createNamedQuery("MapFieldFieldGroup.findByTemplateFieldGroup")
				.setParameter("fieldId",fieldId)
				.setParameter("fieldGroupId", fieldGroupId);
		List<MapFieldFieldGroup> items =  query.getResultList();
		return items;
	}

	@Override
	@Transactional
	public void deleteByObject(MapFieldFieldGroup item) {
		em.remove(item);

	}



}
