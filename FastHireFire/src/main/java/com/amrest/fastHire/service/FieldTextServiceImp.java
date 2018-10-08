package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.FieldText;

@Transactional
@Component
public class FieldTextServiceImp implements FieldTextService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FieldText> findAll() {
		Query query = em.createNamedQuery("FieldText.findAll");
		 List<FieldText> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public FieldText update(FieldText item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public FieldText create(FieldText item) {
		em.persist(item);
	       return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FieldText> findByFieldId(String fieldId) {
		Query query = em.createNamedQuery("FieldText.findByFieldId")
				.setParameter("fieldId", fieldId);
		 List<FieldText> items = query.getResultList();
	        return items;
	}

	@Override
	public FieldText findByFieldLanguage(String fieldId, String language) {
		try
		{
		Query query = em.createNamedQuery("FieldText.findByFieldLanguage")
				.setParameter("fieldId", fieldId)
				.setParameter("language",language);
		FieldText item = (FieldText) query.getSingleResult();
	        return item;
	}
    catch(NoResultException e) {
        return null;
    }
	}

	@Override
	@Transactional
	public void deleteByObject(FieldText item) {
		em.remove(item);

	}

}
