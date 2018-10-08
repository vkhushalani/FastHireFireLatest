package com.amrest.fastHire.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.Template;

@Transactional
@Component
public class TemplateServiceImp implements TemplateService {

	@PersistenceContext
	 EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Template> findAll() {
		Query query = em.createNamedQuery("Template.findAll");
		 List<Template> items = query.getResultList();
	        return items;
	}

	@Override
	@Transactional
	public Template update(Template item) {
		em.merge(item);
	       return item;
	}

	@Override
	@Transactional
	public Template create(Template item) {
		em.persist(item);
	       return item;
	}

	@Override
	public Template findById(String id) {
		Template item = em.find(Template.class, id);
		return item;
	}

	@Override
	public Template findDefaultTemplate(Boolean isDefault) {
		Query query = em.createNamedQuery("Template.findDefaultTemplate")
				.setParameter("isDefault",isDefault);
		Template item = (Template) query.getSingleResult();
		 return item;
	}

	@Override
	@Transactional
	public void deleteByObject(Template item) {
		em.remove(item);

	}

}
