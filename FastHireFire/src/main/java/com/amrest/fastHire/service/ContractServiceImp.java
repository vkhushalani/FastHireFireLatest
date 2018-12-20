package com.amrest.fastHire.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amrest.fastHire.model.Contract;

@Transactional
@Component
public class ContractServiceImp implements ContractService {

	@PersistenceContext
	 EntityManager em;
	
	
	@Override
	public Contract findById(String id) {
		Contract item = em.find(Contract.class, id);
		return item;
	}

}
