package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.CodeListText;
@Transactional
@Component
public class CodeListTextServiceImp implements CodeListTextService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CodeListText> findAll() {
		Query query = em.createNamedQuery("CodeListText.findAll");
		 List<CodeListText> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public CodeListText update(CodeListText item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public CodeListText create(CodeListText item) {
		em.persist(item);
	       return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CodeListText> findByCodeListIdLang(String codeListId, String language) {
		Query query = em.createNamedQuery("CodeListText.findByCodeListIdLang")
				.setParameter("codeListId", codeListId)
				.setParameter("language", language);
		 List<CodeListText> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public void deleteByObject(CodeListText item) {
		em.remove(item);

	}

	@Override
	public CodeListText findById(String codeListId,String language,String value) {
		try
		{
		Query query = em.createNamedQuery("CodeListText.findById")
				.setParameter("codeListId", codeListId)
				.setParameter("language", language)
				.setParameter("value", value);
		 CodeListText item = (CodeListText) query.getSingleResult();
	        return item;
	        }
	        catch(NoResultException e) {
	            return null;
	        }
	}
		

}
