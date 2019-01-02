package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.ConfirmStatus;

public interface ConfirmStatusService {
	
	public List<ConfirmStatus> findAll();
	public ConfirmStatus findById(String id);
	public ConfirmStatus update(ConfirmStatus item);
	public ConfirmStatus create(ConfirmStatus item);
	public void deleteByObject(ConfirmStatus item);

}
