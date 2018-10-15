package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.SF.DestinationClient;
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
import com.amrest.fastHire.service.CodeListService;
import com.amrest.fastHire.service.CodeListTextService;
import com.amrest.fastHire.service.FieldDataFromSystemService;
import com.amrest.fastHire.service.FieldTextService;
import com.amrest.fastHire.service.MapCountryBusinessUnitService;
import com.amrest.fastHire.service.MapCountryBusinessUnitTemplateService;
import com.amrest.fastHire.service.MapTemplateFieldGroupService;
import com.amrest.fastHire.service.MapTemplateFieldPropertiesService;
import com.amrest.fastHire.service.SFAPIService;
import com.amrest.fastHire.utilities.DropDownKeyValue;
import com.google.gson.Gson;

@RestController
@RequestMapping("/FastHireCandidate")
public class FastHireCandidateController {
	Logger logger = LoggerFactory.getLogger(FastHireCandidateController.class);
	
	public static final String destinationName = "prehiremgrSFTest";
	
	@Autowired
	MapCountryBusinessUnitService mapCountryBusinessUnitService;
	
	@Autowired
	MapCountryBusinessUnitTemplateService mapCountryBusinessUnitTemplateService;
	
	@Autowired
	MapTemplateFieldGroupService mapTemplateFieldGroupService;
	
	@Autowired
	MapTemplateFieldPropertiesService mapTemplateFieldPropertiesService;
	
	@Autowired
	SFAPIService sfAPIService;
	
	@Autowired
	FieldTextService fieldTextService;
	
	@Autowired
	CodeListService codeListService;
	
	@Autowired
	CodeListTextService codeListTextService;
	
	@Autowired
	FieldDataFromSystemService fieldDataFromSystemService;
	
	@GetMapping(value = "/UserDetails")
	public ResponseEntity <?> getUserDetails(HttpServletRequest request) throws NamingException, ClientProtocolException, IOException, URISyntaxException{
		String loggedInUser =  request.getUserPrincipal().getName();
		loggedInUser = "E00000118";
		
		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));
		
		// call to get local language of the logged in user
		
		HttpResponse userResponse =  destClient.callDestinationGET("/User", "?$filter=userId eq '"+loggedInUser+ "'&$format=json&$select=userId,lastName,firstName,email,defaultLocale");
		String userResponseJsonString = EntityUtils.toString(userResponse.getEntity(), "UTF-8");
		JSONObject userResponseObject = new JSONObject(userResponseJsonString);
		userResponseObject = userResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
		return ResponseEntity.ok().body(userResponseObject.toString());
	}
	
	@GetMapping(value = "/FormTemplate")
	public ResponseEntity <?> getFormTemplateFields(@RequestParam(value = "businessUnit", required = false) String businessUnitId,
			@RequestParam(value = "candidateId", required = true) String tempUserId,
			HttpServletRequest request) throws NamingException, ParseException, IOException, URISyntaxException{
		JSONObject returnObject = new JSONObject();
		JSONArray returnArray =  new JSONArray();
		
		// get logged in User Id
		String userId =  request.getUserPrincipal().getName();
		// adding  dummy userId for now
		userId = tempUserId;
		
		Map<String,String> map = new HashMap<String,String>();
		
		DestinationClient destClientUser = new DestinationClient();
		destClientUser.setDestName(destinationName);
		destClientUser.setHeaderProvider();
		destClientUser.setConfiguration();
		destClientUser.setDestConfiguration();
		destClientUser.setHeaders(destClientUser.getDestProperty("Authentication"));
		 HttpResponse responseUser = destClientUser.callDestinationGET("/EmpJob","?$filter=userId eq '"+userId+"' &$format=json&$expand=positionNav,positionNav/companyNav,userNav&$select=positionNav/company,positionNav/department,position,positionNav/companyNav/country,userNav/firstName,userNav/lastName,userNav/defaultLocale");
		 String responseUserJson = EntityUtils.toString(responseUser.getEntity(), "UTF-8");
		 logger.debug("responseUserJson"+responseUserJson);
		 JSONObject userEmpJobObject = new JSONObject(responseUserJson);
		 userEmpJobObject = userEmpJobObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
		 returnObject.put("userEmpJob", userEmpJobObject);
		 map.put("position",userEmpJobObject.getString("position"));
		 map.put("department",userEmpJobObject.getJSONObject("positionNav").getString("department"));
		 map.put("company",userEmpJobObject.getJSONObject("positionNav").getString("company"));
		 map.put("country",userEmpJobObject.getJSONObject("positionNav").getJSONObject("companyNav").getString("country"));
		 map.put("category", "CONFIRM");
		 map.put("candidateId", userId);
		 map.put("locale", userEmpJobObject.getJSONObject("userNav").getString("defaultLocale"));
		 map.put("firstName",userEmpJobObject.getJSONObject("userNav").getString("firstName"));
		 map.put("lastName",userEmpJobObject.getJSONObject("userNav").getString("lastName"));
		 
		 logger.debug("map:"+map);
		// Get the Business Unit and Company Map
			MapCountryBusinessUnit mapCountryBusinessUnit;
			if(businessUnitId ==  null){
			List<MapCountryBusinessUnit> mapCountryBusinessUnitList = mapCountryBusinessUnitService.findByCountry(map.get("country"));
			mapCountryBusinessUnit = mapCountryBusinessUnitList.get(0);}
			else
			{
				 mapCountryBusinessUnit = mapCountryBusinessUnitService.findByCountryBusinessUnit(map.get("country"),businessUnitId);
			}
			//getting the template per BUnit and Country
			List<MapCountryBusinessUnitTemplate> mapTemplateList = mapCountryBusinessUnitTemplateService.findByCountryBusinessUnitId(mapCountryBusinessUnit.getId());
			
			Date today = new Date();
			Template template = null;
			
			// getting valid template per category (Confirm)
			for(MapCountryBusinessUnitTemplate mapTemplate : mapTemplateList)
			{
				
				if(today.before(mapTemplate.getEndDate()) && today.after(mapTemplate.getStartDate()))
				 {
					
					if( mapTemplate.getTemplate().getCategory().equalsIgnoreCase(map.get("category")))
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
					for(String entity : entities){
						
						SFAPI sfApi = sfAPIService.findById(entity, "GET");
						if(sfApi !=null &&sfApi.getTagSource().equalsIgnoreCase("UI")){
							 
							 DestinationClient destClientPos = new DestinationClient();
							 destClientPos.setDestName(destinationName);
							 destClientPos.setHeaderProvider();
							 destClientPos.setConfiguration();
							 destClientPos.setDestConfiguration();
							 destClientPos.setHeaders(destClientPos.getDestProperty("Authentication"));
							
							 String url = sfApi.getUrl().replace("<"+sfApi.getReplaceTag()+">", map.get(sfApi.getTagSourceValuePath()));
							
							 HttpResponse responsePos = destClientPos.callDestinationGET(url,"");
							
							 String responseString = EntityUtils.toString(responsePos.getEntity(), "UTF-8");
							 responseMap.put(entity,responseString);
							 
						 }}
						
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
							 
							 String replaceValue = getValueFromPathJson(positionEntity,sfApi.getTagSourceValuePath(),map);
							
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
					
					
					// Loop the field Group to create the response JSON array  
					for( MapTemplateFieldGroup tFieldGroup :templateFieldGroups)
					{
						// candidate app will only have fields which have is candidate visible = true
						if(tFieldGroup.getIsVisibleCandidate()){
							JSONObject fieldObject = new JSONObject();
							Gson gson = new Gson();
						
							if(tFieldGroup.getFieldGroup() !=null){
								// setting the field Group
								tFieldGroup.getFieldGroup().setFieldGroupSeq(tFieldGroup.getFieldGroupSeq());
							    String jsonString = gson.toJson(tFieldGroup.getFieldGroup());
								fieldObject.put("fieldGroup",new JSONObject(jsonString));
								
								// creating the fields entity in the json per field group
								List<MapTemplateFieldProperties> mapTemplateFieldPropertiesList = mapTemplateFieldPropertiesService.findByTemplateFieldGroupCandidate(tFieldGroup.getId(),true);
//								List<MapTemplateFieldProperties> mapTemplateFieldPropertiesList = mapTemplateFieldPropertiesService.findByTemplateFieldGroup(tFieldGroup.getId());
								for( MapTemplateFieldProperties mapTemplateFieldProperties : mapTemplateFieldPropertiesList){
									
									//setting field labels
									
									FieldText fieldText = fieldTextService.findByFieldLanguage(mapTemplateFieldProperties.getFieldId(),map.get("locale"));
									if(fieldText!=null){
										
										mapTemplateFieldProperties.getField().setName(fieldText.getName());
									}
									
									//setting the field values
									
									
									
										if(mapTemplateFieldProperties.getField().getEntityName() != null){
											if(responseMap.get(mapTemplateFieldProperties.getField().getEntityName()) !=null){
												Field field = mapTemplateFieldProperties.getField();
												if((field.getFieldType().equalsIgnoreCase("Picklist") || 
														field.getFieldType().equalsIgnoreCase("Entity") ||
														field.getFieldType().equalsIgnoreCase("Codelist")) && mapTemplateFieldProperties.getIsEditableCandidate())
													{
													logger.debug("editable picklist:" +mapTemplateFieldProperties.getField().getName());
														SFAPI sourceEntity = sfAPIService.findById(mapTemplateFieldProperties.getField().getEntityName(),"GET");
														JSONObject responseObject;
														String valuePath;
														if(sourceEntity.getTagSource().equalsIgnoreCase("UI")){
															 responseObject = new JSONObject(responseMap.get(sourceEntity.getEntityName()));
															 valuePath = mapTemplateFieldProperties.getField().getValueFromPath();
														}
														else
														{
															 responseObject = new JSONObject(responseMap.get(sourceEntity.getTagSource()));
															 valuePath = sourceEntity.getTagSourceValuePath();
														}
														
														logger.debug("responseObject:" + responseObject);
														JSONArray responseResult = responseObject.getJSONObject("d").getJSONArray("results");
														logger.debug("responseResult:" + responseResult);
														if(responseResult.length() !=0){
															JSONObject positionEntity = responseResult.getJSONObject(0);
															
															String value = getValueFromPathJson(positionEntity,valuePath,map);
															mapTemplateFieldProperties.setValue(value);
															}
													}
												else
												{
													logger.debug("non editable picklist or input:" +mapTemplateFieldProperties.getField().getName());
													JSONObject responseObject = new JSONObject(responseMap.get(mapTemplateFieldProperties.getField().getEntityName()));
													
													JSONArray responseResult = responseObject.getJSONObject("d").getJSONArray("results");
													
													if(responseResult.length() !=0){
														JSONObject positionEntity = responseResult.getJSONObject(0);
														
														String value = getValueFromPathJson(positionEntity,mapTemplateFieldProperties.getField().getValueFromPath(),map);
														logger.debug("value"+value);
														if(value !=null){
															if(mapTemplateFieldProperties.getField().getFieldType().equalsIgnoreCase("Codelist"))
															{	String codeListId = codeListService.findByCountryField(mapTemplateFieldProperties.getFieldId(), map.get("country")).getId();
															CodeListText clText = codeListTextService.findById(codeListId, map.get("locale"), value);	
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
													}
												}
											}
											else
											{
												mapTemplateFieldProperties.setValue("");
												}
										}
										else
										{
											
											if(mapTemplateFieldProperties.getValue() == null){
												if(mapTemplateFieldProperties.getField().getInitialValue() != null){
													mapTemplateFieldProperties.setValue(mapTemplateFieldProperties.getField().getInitialValue());}
													else
													{mapTemplateFieldProperties.setValue("");}
												}
										}
									
										
										
									// making the field input type if the is Editable Candidate false
									
									if(!mapTemplateFieldProperties.getIsEditableCandidate()){
										mapTemplateFieldProperties.getField().setFieldType("Input");
										
										/// may be we need to call the picklist to get the labels instead of key value for few fields
									}
									
									if(mapTemplateFieldProperties.getIsVisibleCandidate()){
									//setting drop down values if picklist, codelist, entity
									logger.debug("setting drop down values if picklist, codelist, entity");
									List<DropDownKeyValue> dropDown = new ArrayList<DropDownKeyValue>();
									List<FieldDataFromSystem> fieldDataFromSystemList;
									
									// switch case the picklist , entity and codelist to get the data from various systems
									 switch(mapTemplateFieldProperties.getField().getFieldType())
									 	{
											 case "Picklist": 
												 logger.debug("Picklist"+mapTemplateFieldProperties.getField().getName());
												 fieldDataFromSystemList =  fieldDataFromSystemService.findByFieldCountry(mapTemplateFieldProperties.getField().getId(), map.get("country"));
												
												 if(fieldDataFromSystemList.size() !=0)
												 {
													 FieldDataFromSystem fieldDataFromSystem = fieldDataFromSystemList.get(0);
													 DestinationClient destClient = new DestinationClient();
													 destClient.setDestName(fieldDataFromSystem.getDestinationName());
													 destClient.setHeaderProvider();
													 destClient.setConfiguration();
													 destClient.setDestConfiguration();
													 destClient.setHeaders(destClient.getDestProperty("Authentication"));
													 HttpResponse response = destClient.callDestinationGET(fieldDataFromSystem.getPath(),fieldDataFromSystem.getFilter());
													
													 String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
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
															 if(pickListLabels.getJSONObject(j).get("locale").toString().equalsIgnoreCase(map.get("locale")))
															 {
																 keyValue.setValue(pickListLabels.getJSONObject(j).get("label").toString());
															 }
														 }
														 
														 dropDown.add(keyValue);
													 }
													
												 }
												 break;
											 case "Entity":
												 logger.debug("Entity"+mapTemplateFieldProperties.getField().getName());
												  fieldDataFromSystemList =  fieldDataFromSystemService.findByFieldCountry(mapTemplateFieldProperties.getField().getId(),  map.get("country"));
												
												 if(fieldDataFromSystemList.size() !=0)
												 {
													 FieldDataFromSystem fieldDataFromSystem = fieldDataFromSystemList.get(0);
													 DestinationClient destClient = new DestinationClient();
													 destClient.setDestName(fieldDataFromSystem.getDestinationName());
													 destClient.setHeaderProvider();
													 destClient.setConfiguration();
													 destClient.setDestConfiguration();
													 destClient.setHeaders(destClient.getDestProperty("Authentication"));
													 HttpResponse response = destClient.callDestinationGET(fieldDataFromSystem.getPath(),fieldDataFromSystem.getFilter());
													 
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
																
																 valuePathArray[index] = valuePathArray[index].replace("<locale>",  map.get("locale"));
															 }
													 	keyValue.setValue(temp.get(valuePathArray[index]).toString());
													 	keyValue.setKey(temp.get(keyPathArray[index]).toString());
													 	
													 	dropDown.add(keyValue);
													 }
												 }
												 
												 break;
											 case "Codelist":
												 logger.debug("Codelist"+mapTemplateFieldProperties.getField().getName());
												 CodeList codeList = codeListService.findByCountryField(mapTemplateFieldProperties.getField().getId(),  map.get("country"));
												 if(codeList != null){
												 List<CodeListText> codeListValues = codeListTextService.findByCodeListIdLang(codeList.getId(),  map.get("locale"));
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
	
			
			returnObject.put("hiringForm", returnArray);
			return ResponseEntity.ok().body(returnObject.toString());
		
		}
	
public String  getValueFromPathJson(JSONObject positionEntity , String path, Map<String,String> compareMap){
		
		String [] techPathArray  = path.split("/");
//		String [] techPathArray = mapTemplateFieldProperties.getField().getValueFromPath().split("/");
		JSONArray tempArray;
		for(int i=0;i<techPathArray.length;i++)
		{
			if(i != techPathArray.length - 1){
				if(techPathArray[i].contains("[]")){
					
					tempArray = positionEntity.getJSONArray(techPathArray[i].replace("[]", ""));
				
					if(tempArray.length()!=0){
						String findObjectKey = techPathArray[i+1].substring(techPathArray[i+1].indexOf("(") + 1, techPathArray[i+1].indexOf(")"));
					for(int j = 0;j< tempArray.length() ;j++)
					{
						
						
						
						if(tempArray.getJSONObject(j).get(findObjectKey).toString().equalsIgnoreCase(compareMap.get(findObjectKey)))
						{
							
							positionEntity = tempArray.getJSONObject(j);
							techPathArray[i+1] = techPathArray[i+1].replace("("+findObjectKey+")", "");
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
						positionEntity = positionEntity.getJSONObject(techPathArray[i]);
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
					
						if(techPathArray[i].contains("<locale>"))
					 {
						 
						 techPathArray[i] = techPathArray[i].replace("<locale>", compareMap.get("locale"));
					 }
						
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

}
