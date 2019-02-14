package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
public class PerPersonal {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(PerPersonal.class);

	private static final String datePattern = "dd/MM/yyyy";

	private String paramName = null;
	private String paramValue = null;

	private String paramNationalityName = null;
	private String paramNationalityValue = null;

	private String paramFirstName = null;
	private String paramFirstNameValue = null;

	private String paramLastName = null;
	private String paramLastNameValue = null;

	private String paramPrefLang = null;
	private String paramPrefLangValue = null;
	private String maritalStatus = null;
	private final String gender = "gender";
	private final String nationality = "nationality";
	private final String firstName = "firstname";
	private final String lastName = "lastname";
	private final String prefLang = "nativePreferredLang";

	@PostMapping(value = ConstantManager.perPersonal, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String perPerson(@RequestBody String request, HttpServletRequest requestForSession) {

		// Extract the params and their values
		parseRequest(request);

		URLManager genURL = new URLManager(getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);

		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpSession session = requestForSession.getSession(false);
		String userID = (String) session.getAttribute("userID");
		String paramOrgStartDateValue = (String) session.getAttribute("paramOrgStartDateValue");
		logger.error("Got UserId from session in PerPersonal: " + userID);
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration,
				replaceKeys(userID, paramOrgStartDateValue), PerPersonal.class);

		String result = httpConnectionPOST.connectToServer();
		return result;
	}

	// Parse the request
	private void parseRequest(String request) {
		ObjectMapper mapper = new ObjectMapper();
		Detail[] detail = null;
		try {
			detail = mapper.readValue(request, Detail[].class);
			for (int i = 0; i < detail.length; i++) {
				List<Field> group = detail[i].getFields();

				for (Field field : group) {
					String techName = field.getField().getTechnicalName().toString();

					if (techName.toLowerCase().equals(gender.toLowerCase())) {
						paramName = techName;
						paramValue = field.getValue().toString();
//						logger.error(paramName.toString());
//						logger.error(paramValue.toString());
					} else if (techName.toLowerCase().equals(nationality.toLowerCase())) {
						paramNationalityName = techName;
						paramNationalityValue = field.getValue().toString();
//						logger.error(paramNationalityName.toString());
//						logger.error(paramNationalityValue.toString());
					} else if (techName.toLowerCase().equals(firstName.toLowerCase())) {
						paramFirstName = techName;
						paramFirstNameValue = field.getValue().toString();
//						logger.error(paramFirstName.toString());
//						logger.error(paramFirstNameValue.toString());
					} else if (techName.toLowerCase().equals(lastName.toLowerCase())) {
						paramLastName = techName;
						paramLastNameValue = field.getValue().toString();
//						logger.error(paramLastName.toString());
//						logger.error(paramLastNameValue.toString());
					} else if (techName.toLowerCase().equals(prefLang.toLowerCase())) {
						paramPrefLang = techName;
						paramPrefLangValue = field.getValue().toString();
//						logger.error(paramPrefLang.toString());
//						logger.error(paramPrefLangValue.toString());
					} else if (techName.toLowerCase().equals("maritalstatus")) {
						maritalStatus = field.getValue().toString();
//						logger.error(paramPrefLang.toString());
//						logger.error(paramPrefLangValue.toString());
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private String replaceKeys(String userID, String paramOrgStartDateValue) {
		JSONObject obj = new JSONObject();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("uri", "PerPersonal(personIdExternal='" + userID + "',startDate=datetime'"
				+ dateFormatted(paramOrgStartDateValue) + "')");
		obj.put("__metadata", jsonObj);

		obj.put("personIdExternal", userID);
		obj.put(paramName, paramValue);
		obj.put("maritalStatus", maritalStatus);
		obj.put(paramNationalityName, paramNationalityValue);
		obj.put(paramFirstName, paramFirstNameValue);
		obj.put(paramLastName, paramLastNameValue);

//		logger.error(obj.toJSONString());
		return obj.toJSONString();
	}

	private String dateFormatted(String paramOrgStartDateValue) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
		Date date = null;
		DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = null;
		try {
			date = simpleDateFormat.parse(paramOrgStartDateValue);
			formattedDate = targetFormat.format(date);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		return formattedDate + "T00:00:00";
	}
}
