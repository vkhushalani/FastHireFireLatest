package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.MapCountryBusinessUnitTemplate;

@Transactional
@Component
public class MapCountryBusinessUnitTemplateServiceImp implements MapCountryBusinessUnitTemplateService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MapCountryBusinessUnitTemplate> findAll() {
		Query query = em.createNamedQuery("MapCountryBusinessUnitTemplate.findAll");
		 List<MapCountryBusinessUnitTemplate> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public MapCountryBusinessUnitTemplate create(MapCountryBusinessUnitTemplate item) {
		em.persist(item);
	       return item;
	}


	@Override
	public MapCountryBusinessUnitTemplate findById(String id) {
		MapCountryBusinessUnitTemplate item = em.find(MapCountryBusinessUnitTemplate.class, id);
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapCountryBusinessUnitTemplate> findByCountryBusinessUnitId(String countryBusinessUnitId) {
		Query query = em.createNamedQuery("MapCountryBusinessUnitTemplate.findByCountryBusinessUnitId")
				.setParameter("countryBusinessUnitId",countryBusinessUnitId);
		List<MapCountryBusinessUnitTemplate> items =  query.getResultList();
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapCountryBusinessUnitTemplate> findByCountryBusinessUnitTemplate(String countryBusinessUnitId, String templateId) {
		Query query = em.createNamedQuery("MapCountryBusinessUnitTemplate.findByCountryBusinessUnitTemplate")
				.setParameter("countryBusinessUnitId",countryBusinessUnitId)
				.setParameter("templateId", templateId);
		List<MapCountryBusinessUnitTemplate> items =  query.getResultList();
		return items;
	}

	@Override
	@Transactional
	public void deleteByObject(MapCountryBusinessUnitTemplate item) {
		em.remove(item);

	}

}
