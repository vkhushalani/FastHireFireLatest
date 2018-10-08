package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.MapCountryBusinessUnit;

@Transactional
@Component
public class MapCountryBusinessUnitServiceImp implements MapCountryBusinessUnitService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MapCountryBusinessUnit> findAll() {
		Query query = em.createNamedQuery("MapCountryBusinessUnit.findAll");
		 List<MapCountryBusinessUnit> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public MapCountryBusinessUnit create(MapCountryBusinessUnit item) {
		em.persist(item);
	       return item;
	}


	@Override
	public MapCountryBusinessUnit findById(String id) {
		MapCountryBusinessUnit item = em.find(MapCountryBusinessUnit.class, id);
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapCountryBusinessUnit> findByCountry(String countryId) {
		Query query = em.createNamedQuery("MapCountryBusinessUnit.findByCountry")
				.setParameter("countryId",countryId);
		List<MapCountryBusinessUnit> items =  query.getResultList();
		return items;
	}


	@Override
	public MapCountryBusinessUnit findByCountryBusinessUnit(String countryId, String businessUnitId) {
		Query query = em.createNamedQuery("MapCountryBusinessUnit.findByCountryBusinessUnit")
				.setParameter("businessUnitId",businessUnitId)
				.setParameter("countryId", countryId);
		MapCountryBusinessUnit item =  (MapCountryBusinessUnit) query.getSingleResult();
		return item;
	}

	@Override
	@Transactional
	public void deleteByObject(MapCountryBusinessUnit item) {
		em.remove(item);

	}

}
