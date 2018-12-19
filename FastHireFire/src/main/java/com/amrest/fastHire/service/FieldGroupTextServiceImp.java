package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.FieldGroupText;

@Transactional
@Component
public class FieldGroupTextServiceImp implements FieldGroupTextService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FieldGroupText> findAll() {
		Query query = em.createNamedQuery("FieldGroupText.findAll");
		 List<FieldGroupText> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public FieldGroupText update(FieldGroupText item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public FieldGroupText create(FieldGroupText item) {
		em.persist(item);
	       return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FieldGroupText> findByFieldGroupId(String fieldGroupId) {
		Query query = em.createNamedQuery("FieldGroupText.findByFieldGroupId")
				.setParameter("fieldGroupId", fieldGroupId);
		 List<FieldGroupText> items = query.getResultList();
	        return items;
	}

	@Override
	public FieldGroupText findByFieldGroupLanguage(String fieldGroupId, String language) {
		try
		{
		Query query = em.createNamedQuery("FieldGroupText.findByFieldGroupLanguage")
				.setParameter("fieldGroupId", fieldGroupId)
				.setParameter("language",language);
		FieldGroupText item = (FieldGroupText) query.getSingleResult();
	        return item;
	}
    catch(NoResultException e) {
        return null;
    }
	}

	@Override
	@Transactional
	public void deleteByObject(FieldGroupText item) {
		em.remove(item);

	}

}
