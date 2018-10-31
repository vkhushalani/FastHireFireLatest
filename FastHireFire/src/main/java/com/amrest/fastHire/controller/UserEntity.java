package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.POJO.Detail;
import com.amrest.fastHire.POJO.Field;
import com.amrest.fastHire.connections.HttpConnectionPOST;
import com.amrest.fastHire.utilities.CommonFunctions;
import com.amrest.fastHire.utilities.ConstantManager;
import com.amrest.fastHire.utilities.URLManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = ConstantManager.genAPI)
public class UserEntity {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(UserEntity.class);

	private String paramName = null;
	private String paramValue = null;
	private final String sDate = "startdate";
	private String emailValue = null;
	private String lastNameValue = null;
	private String firstNameValue = null;
	
	@PostMapping(value = ConstantManager.userEntity, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String userEntity(@RequestBody String request) throws ParseException {
		
		parseRequest(request);
		URLManager genURL = new URLManager(getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);
		
		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration,
				createBody(ConstantManager.userID), UserEntity.class);
		
		
		String result = httpConnectionPOST.connectToServer();
		return result;
	}
	private void parseRequest(String request) throws ParseException {
		ObjectMapper mapper = new ObjectMapper();
		Detail[] detail = null;
		try {
			detail = mapper.readValue(request, Detail[].class);

			for (int i = 0; i < detail.length; i++) {
				List<Field> group = detail[i].getFields();
				for (Field field : group) {
					logger.error("Heiii" + field.getField().getTechnicalName().toString());
					String techName = field.getField().getTechnicalName().toString();

					if (techName.toLowerCase().equals(sDate.toLowerCase())) {
						paramName = techName;
						paramValue = field.getValue().toString();
						
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Calendar c = Calendar.getInstance();
						c.setTime(sdf.parse(paramValue));
						c.add(Calendar.DAY_OF_MONTH, ConstantManager.padStartDate);
						ConstantManager.customDateValue = dateFormatted(sdf.format(c.getTime()));
						logger.error(paramName.toString());
						logger.error(paramValue.toString());
						break;
					}
					else if(techName.equalsIgnoreCase("emailAddress")){
						emailValue =  field.getValue().toString();
						logger.error("emailValue"+emailValue);
					}
					else if(techName.equalsIgnoreCase("lastName")){
						lastNameValue = field.getValue().toString();	
						logger.error("lastNameValue"+lastNameValue);
					}
										
					else if(techName.equalsIgnoreCase("firstName")){
						firstNameValue = field.getValue().toString();
						logger.error("firstNameValue"+firstNameValue);
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.toString());
		}

	}
	@SuppressWarnings("unchecked")
	private String createBody(String userID) {
		JSONObject obj = new JSONObject();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("uri", "User('" + userID + "')");
		obj.put("__metadata", jsonObj);

		obj.put("username", userID);
		obj.put("status", "Active");
		obj.put("userId", userID);
//		obj.put(ConstantManager.customDateName, ConstantManager.customDateValue);
		obj.put("email",emailValue);
		obj.put("lastName", lastNameValue);
		obj.put("firstName", firstNameValue);
		logger.debug("input object"+obj.toJSONString());
		return obj.toJSONString();
	}
	private String dateFormatted(String startDate) {
		logger.error("Start Date" + startDate);
		String timeStamp = " 00:00:00:000";
		String receivedTimetamp = startDate + timeStamp;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SSS");
		LocalDateTime dateTime = LocalDateTime.parse(receivedTimetamp, formatter);
		String date = String.valueOf(dateTime.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
		return "/Date(" + date + ")/";
	}

}
