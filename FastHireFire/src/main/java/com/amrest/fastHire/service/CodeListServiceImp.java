package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.CodeList;

@Transactional
@Component
public class CodeListServiceImp implements CodeListService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CodeList> findAll() {
		Query query = em.createNamedQuery("CodeList.findAll");
		 List<CodeList> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public CodeList update(CodeList item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public CodeList create(CodeList item) {
		em.persist(item);
	       return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CodeList> findByFieldId(String fieldId) {
		Query query = em.createNamedQuery("CodeList.findByFieldId")
				.setParameter("fieldId", fieldId);
		 List<CodeList> items = query.getResultList();
	        return items;
	}

	@Override
	public CodeList findByCountryField(String fieldId, String countryId) {
		try
		{
		Query query = em.createNamedQuery("CodeList.findByCountryField")
				.setParameter("fieldId", fieldId)
				.setParameter("countryId", countryId);
		 CodeList item = (CodeList) query.getSingleResult();
	        return item;
		}
	        catch(NoResultException e) {
	            return null;
	        }
	}

	@Override
	@Transactional
	public void deleteByObject(CodeList item) {
		em.remove(item);

	}

	@Override
	public CodeList findById(String id) {
		CodeList item = em.find(CodeList.class, id);
		return item;
	}

	@Override
	public CodeList findByCountryFieldDependent(String fieldId, String countryId, String dependentFieldId,
			String dependentFieldValue) {
		try
		{
		Query query = em.createNamedQuery("CodeList.findByCountryFieldDependent")
				.setParameter("fieldId", fieldId)
				.setParameter("countryId", countryId)
				.setParameter("dependentFieldId", dependentFieldId)
				.setParameter("dependentFieldValue", dependentFieldValue);
		 CodeList item = (CodeList) query.getSingleResult();
	        return item;
		}
	        catch(NoResultException e) {
	            return null;
	        }
	}

}
