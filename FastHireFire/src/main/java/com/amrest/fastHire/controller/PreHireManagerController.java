package com.amrest.fastHire.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.SF.DestinationClient;
import com.amrest.fastHire.service.BusinessUnitService;
import com.amrest.fastHire.service.CodeListService;
import com.amrest.fastHire.service.CodeListTextService;
import com.amrest.fastHire.service.FieldDataFromSystemService;
import com.amrest.fastHire.service.FieldService;
import com.amrest.fastHire.service.MapCountryBusinessUnitService;
import com.amrest.fastHire.service.MapCountryBusinessUnitTemplateService;
import com.amrest.fastHire.service.MapTemplateFieldGroupService;
import com.amrest.fastHire.service.MapTemplateFieldPropertiesService;
import com.amrest.fastHire.service.SFAPIService;
import com.amrest.fastHire.utilities.DropDownKeyValue;
import com.amrest.fastHire.service.FieldTextService;
import com.google.gson.Gson;
import com.amrest.fastHire.model.CodeList;
import com.amrest.fastHire.model.CodeListText;
import com.amrest.fastHire.model.Field;
import com.amrest.fastHire.model.FieldDataFromSystem;
import com.amrest.fastHire.model.FieldText;
import com.amrest.fastHire.model.MapCountryBusinessUnit;
import com.amrest.fastHire.model.MapCountryBusinessUnitTemplate;
import com.amrest.fastHire.model.MapTemplateFieldGroup;
import com.amrest.fastHire.model.MapTemplateFieldProperties;
import com.amrest.fastHire.model.SFAPI;
import com.amrest.fastHire.model.Template;


@RestController
@RequestMapping("/PreHireManager")
public class PreHireManagerController {
	 public static final String destinationName = "prehiremgrSFTest";
	 public static final Integer  padStartDate  = 15;
	 
	Logger logger = LoggerFactory.getLogger(PreHireManagerController.class);
	
	@Autowired
	MapCountryBusinessUnitService mapCountryBusinessUnitService;
	
	@Autowired
	BusinessUnitService businessUnitService;
	
	@Autowired
	MapCountryBusinessUnitTemplateService mapCountryBusinessUnitTemplateService;
	
	@Autowired
	FieldTextService fieldTextService;
	
	@Autowired
	MapTemplateFieldGroupService mapTemplateFieldGroupService;
	
	@Autowired
	MapTemplateFieldPropertiesService mapTemplateFieldPropertiesService;
	
	@Autowired
	CodeListService codeListService;
	
	@Autowired
	CodeListTextService codeListTextService;
	
	@Autowired
	FieldDataFromSystemService fieldDataFromSystemService;
	
	@Autowired
	SFAPIService sfAPIService;
	
	@Autowired
	FieldService fieldService;
	
	@GetMapping(value = "/FormTemplate")
	public ResponseEntity <?> getFormTemplateFields(
			@RequestParam(value = "country", required = true) String countryId,
			@RequestParam(value = "category", required = true) String category,
			@RequestParam(value = "businessUnit", required = false) String businessUnitId,
			@RequestParam(value = "language", required = false , defaultValue = "en_US") String language,
			@RequestParam(value = "candidateId", required = false ) String candidateId,
			@RequestParam(value = "position", required = true ) String position
			) throws NamingException, ClientProtocolException, IOException, URISyntaxException{
		
		Date today = new Date();
		Template template = null;
		JSONArray returnArray =  new JSONArray();
		Map<String,String> compareMap = new HashMap<String,String>();
		compareMap.put("country", countryId);
		compareMap.put("category", category);
		compareMap.put("businessUnit", businessUnitId);
		compareMap.put("locale", language);
		compareMap.put("candidateId", candidateId);
		compareMap.put("position", position);
			
		// Get the Business Unit and Company Map
		MapCountryBusinessUnit mapCountryBusinessUnit;
		if(businessUnitId ==  null){
		List<MapCountryBusinessUnit> mapCountryBusinessUnitList = mapCountryBusinessUnitService.findByCountry(countryId);
		mapCountryBusinessUnit = mapCountryBusinessUnitList.get(0);}
		else
		{
			 mapCountryBusinessUnit = mapCountryBusinessUnitService.findByCountryBusinessUnit(countryId,businessUnitId);
		}
		//getting the template per BUnit and Country
		List<MapCountryBusinessUnitTemplate> mapTemplateList = mapCountryBusinessUnitTemplateService.findByCountryBusinessUnitId(mapCountryBusinessUnit.getId());
		
		// getting valid template per category (Initiate or confirm)
		for(MapCountryBusinessUnitTemplate mapTemplate : mapTemplateList)
		{
			
			if(today.before(mapTemplate.getEndDate()) && today.after(mapTemplate.getStartDate()))
			 {
				
				if( mapTemplate.getTemplate().getCategory().equalsIgnoreCase(category))
				{
					template = mapTemplate.getTemplate();
					break;
				}
				
			 }
		}
		
		if(template !=null)
		{
			// get all field groups for the template
			List<MapTemplateFieldGroup> templateFieldGroups = mapTemplateFieldGroupService.findByTemplate(template.getId());
			if(templateFieldGroups.size() !=0)
			{	
				HashMap<String, String> responseMap = new HashMap<>(); 
				Set<String> entities = new HashSet<String>();
				
				// get the details of Position for CONFIRM Hire Template value setting 
//				logger.debug("get the details of Position for CONFIRM Hire Template value setting ");
				if(category.equalsIgnoreCase("CONFIRM")){
					
					// get all the fields Entity Names Distinct
					for(MapTemplateFieldGroup fieldGroup : templateFieldGroups)
					{
						List<MapTemplateFieldProperties> fieldProperties = mapTemplateFieldPropertiesService.findByTemplateFieldGroup(fieldGroup.getId());
						for (MapTemplateFieldProperties fieldProp : fieldProperties)
						{
							if(fieldProp.getField().getEntityName() != null){
							entities.add(fieldProp.getField().getEntityName());
							}
						}
					}
					
					// call the entity urls which are independent like Empjob
//					logger.debug("call the entity urls which are independent like Empjob ");
				for(String entity : entities){
					
					SFAPI sfApi = sfAPIService.findById(entity, "GET");
					if(sfApi !=null &&sfApi.getTagSource().equalsIgnoreCase("UI")){
						 
						 DestinationClient destClientPos = new DestinationClient();
						 destClientPos.setDestName(destinationName);
						 destClientPos.setHeaderProvider();
						 destClientPos.setConfiguration();
						 destClientPos.setDestConfiguration();
						 destClientPos.setHeaders(destClientPos.getDestProperty("Authentication"));
						
						 String url = sfApi.getUrl().replace("<"+sfApi.getReplaceTag()+">", compareMap.get(sfApi.getTagSourceValuePath()));
						
						 HttpResponse responsePos = destClientPos.callDestinationGET(url,"");
						
						 String responseString = EntityUtils.toString(responsePos.getEntity(), "UTF-8");
						 responseMap.put(entity,responseString);
						 
					 }
					}
//				logger.debug("call the entity URLs which dependent on other entities ");
				// call the entity URLs which dependent on other entities
				for(String entity : entities){
					
					SFAPI sfApi = sfAPIService.findById(entity, "GET");
					if(sfApi !=null && !sfApi.getTagSource().equalsIgnoreCase("UI"))
					{
						DestinationClient destClientPos = new DestinationClient();
						 destClientPos.setDestName(destinationName);
						 destClientPos.setHeaderProvider();
						 destClientPos.setConfiguration();
						 destClientPos.setDestConfiguration();
						 destClientPos.setHeaders(destClientPos.getDestProperty("Authentication"));
						
						 JSONObject depEntityObj = new JSONObject(responseMap.get(sfApi.getTagSource()));
						 
						 JSONArray responseResult = depEntityObj.getJSONObject("d").getJSONArray("results");
						 JSONObject positionEntity = responseResult.getJSONObject(0);
						 
						 // get the dependent value from the dependent entity
						 
						 String replaceValue = getValueFromPathJson(positionEntity,sfApi.getTagSourceValuePath(),compareMap);
						
						 // replacing the tag variable from the dependent entity data value
						if(replaceValue != null && replaceValue.length() !=0){
						 String url = sfApi.getUrl().replace("<"+sfApi.getReplaceTag()+">", replaceValue);
						 
						
						 HttpResponse responsePos = destClientPos.callDestinationGET(url,"");
						 if(responsePos.getStatusLine().getStatusCode() == 200){
						 
						 String responseString = EntityUtils.toString(responsePos.getEntity(), "UTF-8");
						 responseMap.put(entity, responseString);
						 }
						 }
					}
				}
				}
				
				//sort the groups 
				
				Collections.sort(templateFieldGroups);
				
//				logger.debug("Loop the field Group to create the response JSON array ");
				// Loop the field Group to create the response JSON array  
				for( MapTemplateFieldGroup tFieldGroup :templateFieldGroups)
				{
					// manager app will only have fields which have is manager visible = true
					if(tFieldGroup.getIsVisibleManager()){
						JSONObject fieldObject = new JSONObject();
						Gson gson = new Gson();
					
						if(tFieldGroup.getFieldGroup() !=null){
							// setting the field Group
//							logger.debug("setting the field Group"+tFieldGroup.getFieldGroup().getName());
							tFieldGroup.getFieldGroup().setFieldGroupSeq(tFieldGroup.getFieldGroupSeq());
						    String jsonString = gson.toJson(tFieldGroup.getFieldGroup());
							fieldObject.put("fieldGroup",new JSONObject(jsonString));
							
//							logger.debug("creating the fields entity in the json per field group"+tFieldGroup.getFieldGroup().getName());
							// creating the fields entity in the json per field group
							List<MapTemplateFieldProperties> mapTemplateFieldPropertiesList = mapTemplateFieldPropertiesService.findByTemplateFieldGroupManager(tFieldGroup.getId(),true);
//							List<MapTemplateFieldProperties> mapTemplateFieldPropertiesList = mapTemplateFieldPropertiesService.findByTemplateFieldGroup(tFieldGroup.getId());
							
							//sort the fieldProperties 
							
							Collections.sort(mapTemplateFieldPropertiesList);
							
							for( MapTemplateFieldProperties mapTemplateFieldProperties : mapTemplateFieldPropertiesList){
								
								//setting field labels
//								logger.debug("setting field labels"+mapTemplateFieldProperties.getField().getTechnicalName());
								FieldText fieldText = fieldTextService.findByFieldLanguage(mapTemplateFieldProperties.getFieldId(),language);
								if(fieldText!=null){
									
									mapTemplateFieldProperties.getField().setName(fieldText.getName());
								}
								
								//setting the field values
//								logger.debug("setting the field values"+mapTemplateFieldProperties.getField().getName());
								//INITIATE Template
								if(category.equalsIgnoreCase("INITIATE")){
									if(mapTemplateFieldProperties.getValue() == null){
									if(mapTemplateFieldProperties.getField().getInitialValue() != null){
										mapTemplateFieldProperties.setValue(mapTemplateFieldProperties.getField().getInitialValue());}
										else
										{mapTemplateFieldProperties.setValue("");}
									}
									
								}
								else
								{
									//CONFIRM Template
									if(mapTemplateFieldProperties.getField().getEntityName() != null){
									if(responseMap.get(mapTemplateFieldProperties.getField().getEntityName()) !=null){
//										logger.debug("responseMap:"+responseMap.get(mapTemplateFieldProperties.getField().getEntityName()));
									JSONObject responseObject = new JSONObject(responseMap.get(mapTemplateFieldProperties.getField().getEntityName()));
									
									JSONArray responseResult = responseObject.getJSONObject("d").getJSONArray("results");
//									logger.debug("responseResult:"+responseResult);
									if(responseResult.length() !=0){
									JSONObject positionEntity = responseResult.getJSONObject(0);
//									logger.debug("positionEntity:"+positionEntity);
									String value ;
									if(!mapTemplateFieldProperties.getField().getTechnicalName().equalsIgnoreCase("startDate")){
									 value = getValueFromPathJson(positionEntity,mapTemplateFieldProperties.getField().getValueFromPath(),compareMap);}
									else
									{
										value = getValueFromPathJson(positionEntity,"userNav/custom10",compareMap);
										
										String milliSec = value.substring(value.indexOf("(") + 1, value.indexOf(")"));
//										logger.debug("Endate Milli Sec: "+milliSec);
										long milliSecLong = Long.valueOf(milliSec).longValue() - TimeUnit.DAYS.toMillis(padStartDate);
										milliSec = Objects.toString(milliSecLong,null);
//										logger.debug("New milliSec Milli Sec: "+milliSec);
										value = value.replace(value.substring(value.indexOf("(") + 1, value.lastIndexOf(")")), milliSec);
										
										}
//									logger.debug("value:"+value);
									if(value !=null){
//										logger.debug("value not null:"+value);
										if(mapTemplateFieldProperties.getField().getFieldType().equalsIgnoreCase("Codelist"))
										{	
//											logger.debug("Inside Codelist "+mapTemplateFieldProperties.getField().getName());
											
											String codeListId = codeListService.findByCountryField(mapTemplateFieldProperties.getFieldId(), countryId).getId();
										CodeListText clText = codeListTextService.findById(codeListId, language, value);	
											if( clText != null){
													value = clText.getDescription();
													}
											else
											{value = "";}
										}
									mapTemplateFieldProperties.setValue(value);
									}
									else
									{
										
										mapTemplateFieldProperties.setValue("");
									}
									}
									else
									{
										mapTemplateFieldProperties.setValue("");
									}}
									else
									{mapTemplateFieldProperties.setValue("");}
									}
									else
									{
//										logger.debug("No Entity"+mapTemplateFieldProperties.getField().getTechnicalName());
										if(mapTemplateFieldProperties.getField().getInitialValue() != null){
										mapTemplateFieldProperties.setValue(mapTemplateFieldProperties.getField().getInitialValue());}
										else
										{mapTemplateFieldProperties.setValue("");}
									}
								}
								// making the field input type if the is Editable Manager is false
//								logger.debug(" making the field input type if the is Editable Manager is false"+mapTemplateFieldProperties.getField().getName());
								if(!mapTemplateFieldProperties.getIsEditableManager()){
									mapTemplateFieldProperties.getField().setFieldType("Input");
									
									/// may be we need to call the picklist to get the labels instead of key value for few fields
								}
								
								if(mapTemplateFieldProperties.getIsVisibleManager()){
//									logger.debug("setting drop down values if picklist, codelist, entity"+mapTemplateFieldProperties.getField().getName());
								//setting drop down values if picklist, codelist, entity
								
								List<DropDownKeyValue> dropDown = new ArrayList<DropDownKeyValue>();
								List<FieldDataFromSystem> fieldDataFromSystemList;
								
								// switch case the picklist , entity and codelist to get the data from various systems
								 switch(mapTemplateFieldProperties.getField().getFieldType())
								 	{
										 case "Picklist": 
//											 logger.debug("Picklist"+mapTemplateFieldProperties.getField().getName());
											 fieldDataFromSystemList =  fieldDataFromSystemService.findByFieldCountry(mapTemplateFieldProperties.getField().getId(), countryId);
											
											 if(fieldDataFromSystemList.size() !=0)
											 {
												 FieldDataFromSystem fieldDataFromSystem = fieldDataFromSystemList.get(0);
												 DestinationClient destClient = new DestinationClient();
												 destClient.setDestName(fieldDataFromSystem.getDestinationName());
												 destClient.setHeaderProvider();
												 destClient.setConfiguration();
												 destClient.setDestConfiguration();
												 destClient.setHeaders(destClient.getDestProperty("Authentication"));
//												  
												 logger.debug("ID: "+fieldDataFromSystem.getFieldId() +", Name: "+ mapTemplateFieldProperties.getField().getName()+fieldDataFromSystem.getIsDependentField());
												String picklistUrlFilter = getPicklistUrlFilter(fieldDataFromSystem,mapTemplateFieldProperties,compareMap,responseMap,destClient);
												logger.debug("picklistUrlFilter"+picklistUrlFilter);
												 
												 HttpResponse response = destClient.callDestinationGET(fieldDataFromSystem.getPath(),picklistUrlFilter);
												
												 String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
												 
												 logger.debug("responseJson:"+responseJson);
												 JSONObject responseObject = new JSONObject(responseJson);
							
												 JSONArray responseResult = responseObject.getJSONObject("d").getJSONArray("results");
												 for(int i=0;i<responseResult.length();i++)
												 {
													 DropDownKeyValue keyValue = new DropDownKeyValue();
													 String key = (String) responseResult.getJSONObject(i).get(fieldDataFromSystem.getKey());
													 keyValue.setKey(key);
													 
													 JSONArray pickListLabels = responseResult.getJSONObject(i).getJSONObject("picklistLabels").getJSONArray("results");
													 for(int j=0;j<pickListLabels.length();j++)
													 {
														 if(pickListLabels.getJSONObject(j).get("locale").toString().equalsIgnoreCase(language))
														 {
															 keyValue.setValue(pickListLabels.getJSONObject(j).get("label").toString());
														 }
													 }
													 
													 dropDown.add(keyValue);
												 }
												
											 }
											 break;
										 case "Entity":
//											 logger.debug("Entity"+mapTemplateFieldProperties.getField().getName());
											  fieldDataFromSystemList =  fieldDataFromSystemService.findByFieldCountry(mapTemplateFieldProperties.getField().getId(), countryId);
											
											 if(fieldDataFromSystemList.size() !=0)
											 {
												 FieldDataFromSystem fieldDataFromSystem = fieldDataFromSystemList.get(0);
												 DestinationClient destClient = new DestinationClient();
												 destClient.setDestName(fieldDataFromSystem.getDestinationName());
												 destClient.setHeaderProvider();
												 destClient.setConfiguration();
												 destClient.setDestConfiguration();
												 destClient.setHeaders(destClient.getDestProperty("Authentication"));
												 logger.debug("ID: "+fieldDataFromSystem.getFieldId() +", Name: "+ mapTemplateFieldProperties.getField().getName()+fieldDataFromSystem.getIsDependentField());
												 String picklistUrlFilter = getPicklistUrlFilter(fieldDataFromSystem,mapTemplateFieldProperties,compareMap,responseMap,destClient);
												 logger.debug("picklistUrlFilter"+picklistUrlFilter);
												 HttpResponse response = destClient.callDestinationGET(fieldDataFromSystem.getPath(),picklistUrlFilter);
												 
												 String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
												 JSONObject responseObject = new JSONObject(responseJson);
												 
												 JSONArray responseResult = responseObject.getJSONObject("d").getJSONArray("results");
												 String valuePath = fieldDataFromSystem.getValue();
												 String[] valuePathArray = valuePath.split("/");
												 String keyPath = fieldDataFromSystem.getKey();
												 String[] keyPathArray = keyPath.split("/");
												 JSONObject temp = null;
												 int index =0 ;
												 if(valuePathArray.length > 1 && keyPathArray.length > 1)
												 {
													 for(int k =0;k<valuePathArray.length - 1;k++){
													 responseResult = responseResult.getJSONObject(0).getJSONObject(valuePathArray[index]).getJSONArray("results");
													 index = index +1;}
												 }
												 
												 for(int i=0;i<responseResult.length();i++)
												 {	
													 temp =  responseResult.getJSONObject(i);
												 	 DropDownKeyValue keyValue = new DropDownKeyValue();
												 	if(valuePathArray[index].contains("<locale>"))
														 {
															
															 valuePathArray[index] = valuePathArray[index].replace("<locale>", language);
														 }
												 	keyValue.setValue(temp.get(valuePathArray[index]).toString());
												 	keyValue.setKey(temp.get(keyPathArray[index]).toString());
												 	
												 	dropDown.add(keyValue);
												 }
											 }
											 
											 break;
										 case "Codelist":
//											 logger.debug("Codelist"+mapTemplateFieldProperties.getField().getName());
											 CodeList codeList = codeListService.findByCountryField(mapTemplateFieldProperties.getField().getId(), countryId);
											 if(codeList != null){
											 List<CodeListText> codeListValues = codeListTextService.findByCodeListIdLang(codeList.getId(), language);
											 for(CodeListText value : codeListValues){
												 DropDownKeyValue keyValue = new DropDownKeyValue();
												 keyValue.setKey(value.getValue());
												 keyValue.setValue(value.getDescription());
												 dropDown.add(keyValue);
												 }
											 }
											 break;
								 		}
								
									mapTemplateFieldProperties.setDropDownValues(dropDown); 
									
								}
									}
									fieldObject.put("fields",mapTemplateFieldPropertiesList);
									returnArray.put(fieldObject);
							}
							}
						}
					
					}
			}
				
		return ResponseEntity.ok().body(returnArray.toString());
	
	}
	
	private String getPicklistUrlFilter(FieldDataFromSystem fieldDataFromSystem, MapTemplateFieldProperties mapTemplateFieldProperties, Map<String, String> compareMap, HashMap<String, String> responseMap, DestinationClient destClient) throws ClientProtocolException, IOException, URISyntaxException {
		String picklistUrlFilter = fieldDataFromSystem.getFilter();
		if(fieldDataFromSystem.getIsDependentField()){
			 logger.debug("inside is dependent field : "+mapTemplateFieldProperties.getField().getName());
			 if(fieldDataFromSystem.getTagSourceFromSF() != null){
				 logger.debug("From tag source SF : "+mapTemplateFieldProperties.getField().getName());
			 SFAPI depenedentEntity = sfAPIService.findById(fieldDataFromSystem.getTagSourceFromSF(),"GET");
			 String dependentUrl;
			 if(depenedentEntity.getTagSource().equalsIgnoreCase("UI")){
				 
				 logger.debug("Source is dependent on UI input"+mapTemplateFieldProperties.getField().getName());
				 dependentUrl = depenedentEntity.getUrl().replace("<"+depenedentEntity.getReplaceTag()+">",compareMap.get(depenedentEntity.getTagSourceValuePath()));
			 }
			 else
			 {
				 JSONObject responseObject = new JSONObject(responseMap.get(depenedentEntity.getTagSource()));	
				 JSONArray responseResult = responseObject.getJSONObject("d").getJSONArray("results");
				 String dValue = getValueFromPathJson(responseResult.getJSONObject(0),depenedentEntity.getTagSourceValuePath(), compareMap);
				 dependentUrl = depenedentEntity.getUrl().replace("<"+depenedentEntity.getReplaceTag()+">",dValue);
			 }
			 logger.debug("dependentUrl:"+dependentUrl);
			 HttpResponse dependentResponse = destClient.callDestinationGET(dependentUrl,"");
			 String dependentResponseJson = EntityUtils.toString(dependentResponse.getEntity(), "UTF-8");
			 logger.debug("dependentResponseJson:"+dependentResponseJson);
			 JSONObject dependentResponseObject = new JSONObject(dependentResponseJson);
			 JSONArray dependentResponseResult = dependentResponseObject.getJSONObject("d").getJSONArray("results");
			 String replaceValue = getValueFromPathJson(dependentResponseResult.getJSONObject(0),fieldDataFromSystem.getTagSourceValuePath(),compareMap);
			 logger.debug("replaceValue:"+replaceValue);
			 picklistUrlFilter = picklistUrlFilter.replace("<"+fieldDataFromSystem.getReplaceTag()+">", replaceValue);
			 logger.debug("picklistUrlFilter:"+picklistUrlFilter);
			 }
				 else if(fieldDataFromSystem.getTagSourceFromField() != null){
					 Field dependentField = fieldService.findById(fieldDataFromSystem.getTagSourceFromField());
					 String replaceValue = null;
					 if(dependentField.getInitialValue() != null){
						 replaceValue = dependentField.getInitialValue();
					 }
					 else {
					List<MapTemplateFieldProperties> fieldsManagerVisibleList = mapTemplateFieldPropertiesService.findByFieldIdVisibleManager(dependentField.getId(), true);
					if(fieldsManagerVisibleList.get(0).getValue()!=null)
					{replaceValue =fieldsManagerVisibleList.get(0).getValue();} 
					}
					 if(replaceValue !=null){
						 picklistUrlFilter = picklistUrlFilter.replace("<"+fieldDataFromSystem.getReplaceTag()+">", replaceValue);
					 }
				 }
			 }
	return picklistUrlFilter;
	
	}

	public String  getValueFromPathJson(JSONObject positionEntity , String path, Map<String,String> compareMap){
		
		String [] techPathArray  = path.split("/");
//		String [] techPathArray = mapTemplateFieldProperties.getField().getValueFromPath().split("/");
		JSONArray tempArray;
//		logger.debug("techPathArray"+techPathArray.length);
		for(int i=0;i<techPathArray.length;i++)
		{
//			logger.debug("Step"+i+"techPathArray.length - 1"+(techPathArray.length - 1));
			if(i != techPathArray.length - 1){
//				logger.debug("techPathArray["+i+"]"+techPathArray[i]);
				if(techPathArray[i].contains("[]")){
					
					tempArray = positionEntity.getJSONArray(techPathArray[i].replace("[]", ""));
//					logger.debug("tempArray"+i+tempArray);
					if(tempArray.length()!=0){
						String findObjectKey = techPathArray[i+1].substring(techPathArray[i+1].indexOf("(") + 1, techPathArray[i+1].indexOf(")"));
					for(int j = 0;j< tempArray.length() ;j++)
					{
						
						
//						logger.debug("findObjectKey"+j+findObjectKey);
						if(tempArray.getJSONObject(j).get(findObjectKey).toString().equalsIgnoreCase(compareMap.get(findObjectKey)))
						{
							
							positionEntity = tempArray.getJSONObject(j);
//							logger.debug("positionEntity"+j+positionEntity);
							techPathArray[i+1] = techPathArray[i+1].replace("("+findObjectKey+")", "");
//							logger.debug("techPathArray[i+1]"+(i+1)+techPathArray[i+1]);
							break;
							
						}
					}}
					else
					{
						
						break;}
				}
				else if (techPathArray[i].contains("[0]")){
					
					
					 tempArray = positionEntity.getJSONArray(techPathArray[i].replace("[0]", ""));
					 if(tempArray.length() !=0){
					positionEntity = tempArray.getJSONObject(0);}
					 else
					 {
						 break;
					 }
				}
				else{
					
					
					
					try{
//						logger.debug("Object no Array"+techPathArray[i]);
						positionEntity = positionEntity.getJSONObject(techPathArray[i]);
//						logger.debug("Object no Array positionEntity"+positionEntity);
					}
					catch(JSONException exception)
					{
						
						break;
					}
				}
			}
			else
			{
				try{
//					logger.debug("techPathArray["+i+"]"+techPathArray[i]);
						if(techPathArray[i].contains("<locale>"))
					 {
//							logger.debug("with label techPathArray["+i+"]"+techPathArray[i]);
						 techPathArray[i] = techPathArray[i].replace("<locale>", compareMap.get("locale"));
					 }
//						logger.debug("positionEntity.get(techPathArray[i]).toString()"+i+positionEntity.get(techPathArray[i]).toString());
				return positionEntity.get(techPathArray[i]).toString();
				}
				catch(JSONException exception)
				{
					return "";
				}
			}
		}
		return "";
		
	}
	
	@GetMapping("/GetDropDown/{fieldId}")
	public ResponseEntity <List<DropDownKeyValue>> getDependentDropDown(
			@PathVariable("fieldId") String fieldId,
			@RequestParam(value = "triggerFieldId", required = true) String triggerFieldId,
			@RequestParam(value = "selectedValue", required = true) String selectedValue,
			@RequestParam(value = "country", required = true) String countryId,
			@RequestParam(value = "language", required = false , defaultValue = "en_US") String language) throws NamingException, ClientProtocolException, IOException, URISyntaxException {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("country", countryId);
		map.put("locale", language);
		map.put("fieldId", fieldId);
		map.put("selectedValue", selectedValue);
		map.put("fieldId", fieldId);
		map.put("triggerFieldId", triggerFieldId);
		
		DestinationClient destClient = new DestinationClient();
		 destClient.setDestName(destinationName);
		 destClient.setHeaderProvider();
		 destClient.setConfiguration();
		 destClient.setDestConfiguration();
		 destClient.setHeaders(destClient.getDestProperty("Authentication"));
		 
			
		List<DropDownKeyValue> resultDropDown = new ArrayList<DropDownKeyValue>();
		
		Field affectedField = fieldService.findById(fieldId);
		switch(affectedField.getFieldType())
		{
		case "Codelist":
			CodeList affectedFieldCodelist = codeListService.findByCountryFieldDependent(map.get("fieldId"),map.get("country"), map.get("triggerFieldId"), map.get("selectedValue"));
			List<CodeListText> affectedFieldCodelistTextList = codeListTextService.findByCodeListIdLang(affectedFieldCodelist.getId(), map.get("locale"));
			for(CodeListText codeListText : affectedFieldCodelistTextList)
			{
				DropDownKeyValue keyValuePair = new DropDownKeyValue();
				keyValuePair.setKey(codeListText.getValue());
				keyValuePair.setKey(codeListText.getDescription());
				resultDropDown.add(keyValuePair);
			}
			break;
		case "Picklist":
			FieldDataFromSystem fieldDataFrom = fieldDataFromSystemService.findByFieldCountry(map.get("fieldId"), map.get("country")).get(0);
			if(fieldDataFrom.getIsDependentField()){
				String sourceFromField = fieldDataFrom.getTagSourceFromField();
				if(sourceFromField != null)
				{
					String filter = fieldDataFrom.getFilter().replace("<"+fieldDataFrom.getReplaceTag()+">", map.get("selectedValue"));
					HttpResponse response = destClient.callDestinationGET(fieldDataFrom.getPath(),filter);
					
					 String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
					 JSONObject responseObject = new JSONObject(responseJson);

					 JSONArray responseResult = responseObject.getJSONObject("d").getJSONArray("results");
					 for(int i=0;i<responseResult.length();i++)
					 {
						 DropDownKeyValue keyValue = new DropDownKeyValue();
						 String key = (String) responseResult.getJSONObject(i).get(fieldDataFrom.getKey());
						 keyValue.setKey(key);
						 
						 JSONArray pickListLabels = responseResult.getJSONObject(i).getJSONObject("picklistLabels").getJSONArray("results");
						 for(int j=0;j<pickListLabels.length();j++)
						 {
							 if(pickListLabels.getJSONObject(j).get("locale").toString().equalsIgnoreCase(map.get("locale")))
							 {
								 keyValue.setValue(pickListLabels.getJSONObject(j).get("label").toString());
							 }
						 }
						 
						 resultDropDown.add(keyValue);
					 }
				}
				
			}
			break;
		case "Entity":
			
			break;
		default:
			break;
		}
		
		return ResponseEntity.ok().body(resultDropDown);
	}
	@PostMapping("/CancelHire")
	public ResponseEntity <?> cancelHire(@RequestBody String postJson) throws FileNotFoundException, IOException, ParseException, URISyntaxException, NamingException{
		Map<String,String> map = new HashMap<String,String>();  
		
		// get post JSON Object
		JSONObject postObject = new JSONObject(postJson);
		Iterator<?> keys = postObject.keys();
		
		while(keys.hasNext()) {
		    String key = (String)keys.next();
		    map.put(key, postObject.getString(key));
		}
		
		JSONObject jsonObject =  readJSONFile("/JSONFiles/InactivateCandidate.json");
		
		//parse json file to JSON String
		if(jsonObject !=null){
        String jsonString = jsonObject.toString();
        // replace the values from the map
        for (Map.Entry<String, String> entry : map.entrySet()) {
    		jsonString = jsonString.replaceAll("<"+entry.getKey()+">", entry.getValue());
    	}
        
        // now call all the requests for cancel hire
        
        //inactivate employee
         DestinationClient destClient = new DestinationClient();
		 destClient.setDestName(destinationName);
		 destClient.setHeaderProvider();
		 destClient.setConfiguration();
		 destClient.setDestConfiguration();
		 destClient.setHeaders(destClient.getDestProperty("Authentication"));
		 HttpResponse response = destClient.callDestinationPOST("upsert", "?$format=json",jsonString );
		 String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
		
		 // get Job Code from the user calling Emp job
//		 logger.debug("get Job Code from the user calling Emp job");
		 DestinationClient empJobDestClient = new DestinationClient();
		 empJobDestClient.setDestName(destinationName);
		 empJobDestClient.setHeaderProvider();
		 empJobDestClient.setConfiguration();
		 empJobDestClient.setDestConfiguration();
		 empJobDestClient.setHeaders(empJobDestClient.getDestProperty("Authentication"));
		 HttpResponse EmpJobResponse = empJobDestClient.callDestinationGET("/EmpJob","?$filter=userId eq '"+map.get("userId") +"' &$format=json&$select=position");
		 String EmpJobResponseJson = EntityUtils.toString(EmpJobResponse.getEntity(), "UTF-8");
		 JSONObject EmpJobResponseObject = new JSONObject(EmpJobResponseJson);
//		 logger.debug("EmpJobResponseObject: "+EmpJobResponseObject);
		 String jobCode = EmpJobResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0).getString("position");
//		 logger.debug("JobCode: "+ jobCode);
		 // get Uri from the job code calling position details 
		 DestinationClient posDestClient = new DestinationClient();
		 posDestClient.setDestName(destinationName);
		 posDestClient.setHeaderProvider();
		 posDestClient.setConfiguration();
		 posDestClient.setDestConfiguration();
		 posDestClient.setHeaders(posDestClient.getDestProperty("Authentication"));
		 HttpResponse posResponse = posDestClient.callDestinationGET("/Position","?$filter=code eq '" + jobCode+ "'&$format=json&$select=code,location,payGrade,businessUnit,jobCode,department,division,company");
		 String posResponseJson = EntityUtils.toString(posResponse.getEntity(), "UTF-8");
		 JSONObject posResponseObject = new JSONObject(posResponseJson);
		 
//		 logger.debug("posResponseObject"+posResponseObject);
		 String uri = posResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0).getJSONObject("__metadata").getString("uri");
		 
//		 logger.debug("uri"+uri);
		 // get the json for vacant position 
		 JSONObject vacantJsonObject =  readJSONFile("/JSONFiles/CancelHire.json");
			if(vacantJsonObject !=null){
			
				// clear Map
				map.clear();
		 	
				// add new values
				
				map.put("uri", uri);
		 	
				//parse json file to JSON String
				
				String vacantJsonString = vacantJsonObject.toString();
				// replace the values from the map
				for (Map.Entry<String, String> entry : map.entrySet()) {
					vacantJsonString = vacantJsonString.replaceAll("<"+entry.getKey()+">", entry.getValue());
	    	}
//				logger.debug("replace vacantJsonString: "+vacantJsonString);
			 DestinationClient vacantDestClient = new DestinationClient();
			 vacantDestClient.setDestName(destinationName);
			 vacantDestClient.setHeaderProvider();
			 vacantDestClient.setConfiguration();
			 vacantDestClient.setDestConfiguration();
			 vacantDestClient.setHeaders(vacantDestClient.getDestProperty("Authentication"));
			 HttpResponse vacantResponse = vacantDestClient.callDestinationPOST("upsert", "?$format=json",vacantJsonString);
			 String vacantResponseJson = EntityUtils.toString(vacantResponse.getEntity(), "UTF-8");
			 return ResponseEntity.ok().body(vacantResponseJson);
			}
		}
			return new ResponseEntity<>("Error",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PostMapping("/ConfirmHire")
	public ResponseEntity <?> confirmlHire(@RequestBody String postJson) throws FileNotFoundException, IOException, ParseException, URISyntaxException, NamingException, java.text.ParseException{
				Map<String,String> map = new HashMap<String,String>();  
		
				// get post JSON Object
				JSONObject postObject = new JSONObject(postJson);
				Iterator<?> keys = postObject.keys();
				
				while(keys.hasNext()) {
				    String key = (String)keys.next();
				    map.put(key, postObject.getString(key));
				}
				
				//Get URI details
					 DestinationClient destClient = new DestinationClient();
					 destClient.setDestName(destinationName);
					 destClient.setHeaderProvider();
					 destClient.setConfiguration();
					 destClient.setDestConfiguration();
					 destClient.setHeaders(destClient.getDestProperty("Authentication"));
					 HttpResponse response = destClient.callDestinationGET("/EmpJob","?$filter=userId eq '"+map.get("userId")+"'&$format=json&$expand=userNav&$select=employeeClass,countryOfCompany,userNav/custom10");
					 String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
				 
					 JSONObject responseObject = new JSONObject(responseJson);
					 JSONObject resultObj = responseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
					 map.put("uri", resultObj.getJSONObject("__metadata").getString("uri"));
					 
					 String value= resultObj.getJSONObject("userNav").getString("custom10");
					 String milliSec = value.substring(value.indexOf("(") + 1, value.indexOf(")"));
//						logger.debug("Endate Milli Sec: "+milliSec);
						long milliSecLong = Long.valueOf(milliSec).longValue() - TimeUnit.DAYS.toMillis(padStartDate);
						milliSec = Objects.toString(milliSecLong,null);
//						logger.debug("New milliSec Milli Sec: "+milliSec);
						value = value.replace(value.substring(value.indexOf("(") + 1, value.lastIndexOf(")")), milliSec);
						map.put("startDate",value);
				 // not recommended but no choice need to discuss for employee class
					 switch(resultObj.getString("countryOfCompany"))
					 {
					 	case "POL":
					 		 map.put("employeeClass", "34465");
					 		break;
					 	case "HUN":
					 		map.put("employeeClass", "34208");
					 		break;
					 	case "FRA"	:
					 		map.put("employeeClass", "34043");
					 		break;	
						 
					 }
				// add end Date
//				 Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/9999");
//				 map.put("endDate", "/Date("+Long.toString(endDate.getTime())+")/");
				 // update the employee class to an actual one 
				 // read the json file from resource folder
					 JSONObject jsonObject = readJSONFile("/JSONFiles/ConfirmHire.json");
			        
					if(jsonObject !=null){
					String jsonString = jsonObject.toString();
					
			        for (Map.Entry<String, String> entry : map.entrySet()) {
			    		jsonString = jsonString.replaceAll("<"+entry.getKey()+">", entry.getValue());
			    	}
		        
		       
			        DestinationClient destPostClient = new DestinationClient();
			        destPostClient.setDestName(destinationName);
			        destPostClient.setHeaderProvider();
			        destPostClient.setConfiguration();
			        destPostClient.setDestConfiguration();
			        destPostClient.setHeaders(destPostClient.getDestProperty("Authentication"));
					HttpResponse postresponse = destPostClient.callDestinationPOST("upsert", "?$format=json",jsonString);
					String postresponseJson = EntityUtils.toString(postresponse.getEntity(), "UTF-8");
					
		        //updating the startDate and endDate to confirm the hire
				Map<String,String> entityMap = new HashMap<String,String>();  
				Map<String,String> entityResponseMap = new HashMap<String,String>();
				entityMap.put("EmpJob", "?$filter=userId eq '"+map.get("userId")+"'&$format=json&$select= startDate,userId,jobCode,employmentType,workscheduleCode,division,standardHours,costCenter,payGrade,department,timeTypeProfileCode,businessUnit,managerId,position,employeeClass,location,holidayCalendarCode,company,eventReason");
				entityMap.put("EmpPayCompRecurring", "?$filter=userId eq '"+map.get("userId")+"'&$format=json&$select=userId,startDate,payComponent,paycompvalue,currencyCode,frequency");
				entityMap.put("EmpCompensation", "?$filter=userId eq '"+map.get("userId")+"'&$format=json&$select=userId,startDate,payGroup,eventReason");
				entityMap.put("EmpEmployment", "?$filter=personIdExternal eq '"+map.get("userId")+"'&$format=json&$select=userId,startDate,personIdExternal");
				
				// reading the records
				for (Map.Entry<String,String> entity : entityMap.entrySet())  {
					DestinationClient destGetClient = new DestinationClient();
					destGetClient.setDestName(destinationName);
					destGetClient.setHeaderProvider();
					destGetClient.setConfiguration();
					destGetClient.setDestConfiguration();
					destGetClient.setHeaders(destGetClient.getDestProperty("Authentication"));
					HttpResponse getresponse = destGetClient.callDestinationGET("/"+entity.getKey(), entity.getValue());
					String getresponseJson = EntityUtils.toString(getresponse.getEntity(), "UTF-8");
//					logger.debug("getresponseJson read"+getresponseJson);
					entityResponseMap.put(entity.getKey(), getresponseJson);
				}
				for (Map.Entry<String,String> entity : entityMap.entrySet())  {
		           
					String getresponseJson  = entityResponseMap.get(entity.getKey());
					JSONObject getresponseJsonObject =  new JSONObject(getresponseJson);
//					logger.debug("getresponseJson"+getresponseJson);
					
//					if(getresponseJsonObject.getJSONObject("d").getJSONArray("results").length() !=0){
					JSONObject getresultObj = getresponseJsonObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
					getresultObj.put("startDate", map.get("startDate"));
					String postJsonString = getresultObj.toString();	
//					logger.debug("Entity: "+entity.getKey()+" postJsonString: "+postJsonString);
					
						DestinationClient destUpdateClient = new DestinationClient();
					 	destUpdateClient.setDestName(destinationName);
					 	destUpdateClient.setHeaderProvider();
					 	destUpdateClient.setConfiguration();
					 	destUpdateClient.setDestConfiguration();
					 	destUpdateClient.setHeaders(destUpdateClient.getDestProperty("Authentication"));
						HttpResponse updateresponse = destUpdateClient.callDestinationPOST("upsert", "?$format=json&purgeType=full",postJsonString);
						String updateresponseJson = EntityUtils.toString(updateresponse.getEntity(), "UTF-8");
						JSONObject updateresponseObject = new JSONObject(updateresponseJson);
						String status =  updateresponseObject.getJSONArray("d").getJSONObject(0).getString("status");
//						String message =  updateresponseObject.getJSONArray("d").getJSONObject(0).getString("message");
						if(!status.equalsIgnoreCase("OK")){
							return new ResponseEntity<>("Error",HttpStatus.INTERNAL_SERVER_ERROR);
									 
						}
//						logger.debug("updateresponseJson" + updateresponseJson);
//					}
						
					}
				 return ResponseEntity.ok().body("Success");
		        }
				
				return new ResponseEntity<>("Error",HttpStatus.INTERNAL_SERVER_ERROR);
				
		
	}
	
	public JSONObject  readJSONFile(String FilePath) throws IOException{ 
		JSONObject jsonObject = null;
		// read the json file from resource folder
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(FilePath);
		if (inputStream != null) {
			 BufferedReader streamReader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"));
		            StringBuilder responseStrBuilder = new StringBuilder();
		            String inputStr;
		            while ((inputStr = streamReader.readLine()) != null)
		                {
		            	responseStrBuilder.append(inputStr);
		                
		                }
		            jsonObject = new JSONObject(responseStrBuilder.toString());
		}
		return jsonObject;
	}
}
