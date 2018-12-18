package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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
public class EmpEmployment {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(EmpEmployment.class);

	private String paramName = null;
	private String paramValue = null;
	private final String sDate = "startdate";
	

	@PostMapping(value = ConstantManager.empEmployment, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String empEmployment(@RequestBody String request) throws ParseException {

		// Extract the params and their values
		parseRequest(request);

		URLManager genURL = new URLManager(getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);

		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		String data = replaceKeys();
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration, data,
				EmpEmployment.class);

		String result = httpConnectionPOST.connectToServer();
		return result;
	}

	// Parse the request
	private void parseRequest(String request) throws ParseException {
		ObjectMapper mapper = new ObjectMapper();
		Detail[] detail = null;
		try {
			detail = mapper.readValue(request, Detail[].class);

			for (int i = 0; i < detail.length; i++) {
				List<Field> group = detail[i].getFields();
				for (Field field : group) {
//					logger.error("Heiii" + field.getField().getTechnicalName().toString());
					String techName = field.getField().getTechnicalName().toString();

					if (techName.toLowerCase().equals(sDate.toLowerCase())) {
						paramName = techName;
						paramValue = field.getValue().toString();
						
						
//						logger.error(paramName.toString());
//						logger.error(paramValue.toString());
						break;
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.toString());
		}

	}

	@SuppressWarnings("unchecked")
	private String replaceKeys() {
		String userID = ConstantManager.userID;
		JSONObject obj = new JSONObject();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(today);
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("uri", "EmpEmployment(personIdExternal='" + userID + "',userId='" + userID + "')");
		obj.put("__metadata", jsonObj);
		obj.put(paramName, dateFormatted(sdf.format(now.getTime())));
		obj.put("personIdExternal", userID);
		obj.put("userId", userID);
//		logger.error(obj.toJSONString());
		return obj.toJSONString();
	}

	private String dateFormatted(String startDate) {
//		logger.error("Start Date" + startDate);
		String timeStamp = " 00:00:00:000";
		String receivedTimetamp = startDate + timeStamp;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SSS");
		LocalDateTime dateTime = LocalDateTime.parse(receivedTimetamp, formatter);
		String date = String.valueOf(dateTime.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
		return "/Date(" + date + ")/";
	}

}
