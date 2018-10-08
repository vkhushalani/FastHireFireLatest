package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.MapTemplateFieldGroup;

@Transactional
@Component
public class MapTemplateFieldGroupServiceImp implements MapTemplateFieldGroupService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MapTemplateFieldGroup> findAll() {
		Query query = em.createNamedQuery("MapTemplateFieldGroup.findAll");
		 List<MapTemplateFieldGroup> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public MapTemplateFieldGroup create(MapTemplateFieldGroup item) {
		em.persist(item);
	       return item;
	}


	@Override
	public MapTemplateFieldGroup findById(String id) {
		MapTemplateFieldGroup item = em.find(MapTemplateFieldGroup.class, id);
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapTemplateFieldGroup> findByTemplate(String templateId) {
		Query query = em.createNamedQuery("MapTemplateFieldGroup.findByTemplate")
				.setParameter("templateId",templateId);
		List<MapTemplateFieldGroup> items =  query.getResultList();
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapTemplateFieldGroup> findByTemplateFieldGroup(String templateId, String fieldGroupId) {
		Query query = em.createNamedQuery("MapTemplateFieldGroup.findByTemplateFieldGroup")
				.setParameter("fieldGroupId",fieldGroupId)
				.setParameter("templateId", templateId);
		List<MapTemplateFieldGroup> items =  query.getResultList();
		return items;
	}

	@Override
	@Transactional
	public void deleteByObject(MapTemplateFieldGroup item) {
		em.remove(item);

	}

}
