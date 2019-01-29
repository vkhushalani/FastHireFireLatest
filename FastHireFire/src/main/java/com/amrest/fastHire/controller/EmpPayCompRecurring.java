package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URI;
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
public class EmpPayCompRecurring {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(EmpPayCompRecurring.class);

	private String paramName = null;
	private String paramValue = null;

	private String paramCompName = null;
	private String paramCompValue = null;

	private String paramCurrencyName = null;
	private String paramCurrencyValue = null;

	private String paramFrequencyName = null;
	private String paramFrequencyValue = null;

	private final String payComponent = "paycomponent";
	private final String payCompValue = "paycompvalue";
	private final String currencyCode = "currencycode";
	private final String frequency = "frequency";

	@PostMapping(value = ConstantManager.empPayCompRecurring, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
		logger.error("Got UserId from session in EmpPayCompRecurring: " + userID);
		String paramStartDateName = (String) session.getAttribute("paramStartDateName");
		logger.error("Got paramStartDateName from session in EmpPayCompRecurring: " + userID);
		String paramStartDateValue = (String) session.getAttribute("paramStartDateValue");
		logger.error("Got paramStartDateValue from session in EmpPayCompRecurring: " + paramStartDateValue);
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration,
				replaceKeys(userID, paramStartDateName, paramStartDateValue), EmpPayCompRecurring.class);
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

					if (techName.toLowerCase().equals(payComponent.toLowerCase())) {
						paramName = techName;
						paramValue = field.getValue().toString();
//						logger.error(paramName.toString());
//						logger.error(paramValue.toString());
					} else if (techName.toLowerCase().equals(payCompValue.toLowerCase())) {
						paramCompName = techName;
						paramCompValue = field.getValue().toString();
//						logger.error(paramCompName.toString());
//						logger.error(paramCompValue.toString());
					} else if (techName.toLowerCase().equals(currencyCode.toLowerCase())) {
						paramCurrencyName = techName;
						paramCurrencyValue = field.getValue().toString();
//						logger.error(paramCurrencyName.toString());
//						logger.error(paramCurrencyValue.toString());
					} else if (techName.toLowerCase().equals(frequency.toLowerCase())) {
						paramFrequencyName = techName;
						paramFrequencyValue = field.getValue().toString();
//						logger.error(paramFrequencyName.toString());
//						logger.error(paramFrequencyValue.toString());
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.toString());
		}

	}

	@SuppressWarnings("unchecked")
	private String replaceKeys(String userID, String paramStartDateName, String paramStartDateValue) {
		JSONObject obj = new JSONObject();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("uri", "EmpPayCompRecurring");
		obj.put("__metadata", jsonObj);

		obj.put(paramStartDateName, paramStartDateValue);

		// logger.debug(paramStartDateName + paramStartDateValue);
		obj.put("userId", userID);
		obj.put(paramName, paramValue);
		obj.put(paramCompName, paramCompValue);
		obj.put(paramCurrencyName, paramCurrencyValue);
		obj.put(paramFrequencyName, paramFrequencyValue);
//		logger.error(obj.toJSONString());
		return obj.toJSONString();
	}

}
