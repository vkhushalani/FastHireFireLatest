package com.amrest.fastHire.service;

import java.util.List;

import com.amrest.fastHire.model.MapTemplateFieldProperties;

public interface MapTemplateFieldPropertiesService {
	public List<MapTemplateFieldProperties> findAll();
	public MapTemplateFieldProperties create(MapTemplateFieldProperties item);
	public MapTemplateFieldProperties findById(String templateFieldGroupId, String fieldId);
	public List<MapTemplateFieldProperties> findByTemplateFieldGroup(String templateFieldGroupId);
	public List<MapTemplateFieldProperties> findByTemplateFieldGroupManager(String templateFieldGroupId,Boolean isVisibleManager);
	public List<MapTemplateFieldProperties> findByTemplateFieldGroupCandidate(String templateFieldGroupId,Boolean isVisibleCandidate);
	public List<MapTemplateFieldProperties> findByFieldIdVisibleManager(String fieldId,Boolean isVisibleCandidate);
	public List<MapTemplateFieldProperties> getCandidateIsEIsVFalseFields(String templateFieldGroupId);
	public void deleteByObject(MapTemplateFieldProperties item);

}
