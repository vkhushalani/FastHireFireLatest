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
public class EmpCompensation {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(EmpCompensation.class);

	private String paramName = null;
	private String paramValue = null;

	private final String payGroup = "paygroup";

	@PostMapping(value = ConstantManager.empCompensation, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
		logger.error("Got UserId from session in EmpCompensation: " + userID);
		String paramStartDateName = (String) session.getAttribute("paramStartDateName");
		logger.error("Got paramStartDateName from session in EmpCompensation: " + userID);
		String paramStartDateValue = (String) session.getAttribute("paramStartDateValue");
		logger.error("Got paramStartDateValue from session in EmpCompensation: " + paramStartDateValue);
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration,
				replaceKeys(userID, paramStartDateName, paramStartDateValue), EmpCompensation.class);

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
					logger.debug("field: " + field.toString());
					String techName = field.getField().getTechnicalName().toString();

					if (techName.toLowerCase().equals(payGroup.toLowerCase())) {
						paramName = techName;
						logger.debug("paramName: " + paramName);
						paramValue = field.getValue().toString();
						logger.debug("paramValue: " + paramValue);
//						logger.error(paramName.toString());
//						logger.error(paramValue.toString());
						break;
					}
				}
				if (paramName != null && paramName.length() > 0) {
					break;
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
		jsonObj.put("uri", "EmpCompensation");
		obj.put("__metadata", jsonObj);

		logger.debug(paramStartDateName + paramStartDateValue);
//		logger.debug(ConstantManager.paramEndDateName + ConstantManager.paramEndDateValue);
//		obj.put(ConstantManager.paramEndDateName, ConstantManager.paramEndDateValue);
		obj.put(paramStartDateName, paramStartDateValue);
		obj.put("userId", userID);
		obj.put(paramName, paramValue);
		obj.put("eventReason", "HIRNEW");
//		logger.error(obj.toJSONString());
		return obj.toJSONString();
	}

}
