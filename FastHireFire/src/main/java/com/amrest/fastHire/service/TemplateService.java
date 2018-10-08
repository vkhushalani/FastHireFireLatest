package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.Template;

public interface TemplateService {
	public List<Template> findAll();
	public Template update(Template item);
	public Template create(Template item);
	public Template findById(String id);
	public Template findDefaultTemplate(Boolean isDefault);
	public void deleteByObject(Template item);
}
