package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.MapTemplateFieldProperties;

@Transactional
@Component
public class MapTemplateFieldPropertiesServiceImp implements MapTemplateFieldPropertiesService {


	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MapTemplateFieldProperties> findAll() {
		Query query = em.createNamedQuery("MapTemplateFieldProperties.findAll");
		 List<MapTemplateFieldProperties> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public MapTemplateFieldProperties create(MapTemplateFieldProperties item) {
		em.persist(item);
	       return item;
	}


	@Override
	public MapTemplateFieldProperties findById(String templateFieldGroupId, String fieldId) {
		Query query = em.createNamedQuery("MapTemplateFieldProperties.findById")
				.setParameter("templateFieldGroupId",templateFieldGroupId)
				.setParameter("fieldId", fieldId);
		MapTemplateFieldProperties item =  (MapTemplateFieldProperties) query.getSingleResult();
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapTemplateFieldProperties> findByTemplateFieldGroup(String templateFieldGroupId) {
		Query query = em.createNamedQuery("MapTemplateFieldProperties.findByTemplateFieldGroup")
				.setParameter("templateFieldGroupId",templateFieldGroupId);
		List<MapTemplateFieldProperties> items =  query.getResultList();
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapTemplateFieldProperties> findByTemplateFieldGroupManager(String templateFieldGroupId,Boolean isVisibleManager) {
		Query query = em.createNamedQuery("MapTemplateFieldProperties.findByTemplateFieldGroupManager")
				.setParameter("templateFieldGroupId",templateFieldGroupId)
				.setParameter("isVisibleManager", isVisibleManager);
		List<MapTemplateFieldProperties> items =  query.getResultList();
		return items;
	}

	@Override
	@Transactional
	public void deleteByObject(MapTemplateFieldProperties item) {
		em.remove(item);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapTemplateFieldProperties> findByTemplateFieldGroupCandidate(String templateFieldGroupId,
			Boolean isVisibleCandidate) {
		Query query = em.createNamedQuery("MapTemplateFieldProperties.findByTemplateFieldGroupCandidate")
				.setParameter("templateFieldGroupId",templateFieldGroupId)
				.setParameter("isVisibleCandidate", isVisibleCandidate);
		List<MapTemplateFieldProperties> items =  query.getResultList();
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapTemplateFieldProperties> findByFieldIdVisibleManager(String fieldId, Boolean isVisibleCandidate) {
		Query query = em.createNamedQuery("MapTemplateFieldProperties.findByFieldIdVisibleManager")
				.setParameter("fieldId",fieldId)
				.setParameter("isVisibleCandidate", isVisibleCandidate);
		List<MapTemplateFieldProperties> items =  query.getResultList();
		return items;
	}


}
