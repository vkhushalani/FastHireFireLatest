package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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
public class CustPersonIdGen {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(CustPersonIdGen.class);
	private String userID;
	private String cust_FEOR1 = "cust_FEOR1";
	private String paramName = null;
	private String paramValue = null;
	private String paramName2 = null;
	private String paramValue2 = null;
	private String paramName3 = null;
	private String paramValue3 = null;

	@GetMapping(value = ConstantManager.custPerIDGen, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String generateID(HttpServletRequest request) {

		URLManager genURL = new URLManager(getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);

		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration, "{}",
				CustPersonIdGen.class);

		String result = httpConnectionPOST.connectToServer();
		JSONObject jsonObj = (JSONObject) JSONValue.parse(result);
		jsonObj = (JSONObject) jsonObj.get("d");
		jsonObj = (JSONObject) jsonObj.get("GenerateNextPersonIDResponse");
		this.userID = jsonObj.get("personID").toString();
		HttpSession session = request.getSession(false);
		session.setAttribute("userID", this.userID);
		logger.error("UserId Set at session:" + this.userID);
		return checkResp(this.userID);
	}

	@PostMapping(value = ConstantManager.custPerIDGen, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String postCustPersonCustomField(@RequestBody String request, HttpServletRequest requestForSession) {

		parseRequest(request);

		URLManager genURL = new URLManager(getClass().getSimpleName() + "Post", configName);
		String urlToCall = genURL.formURLToCall();
		HttpSession session = requestForSession.getSession(false);
		String userID = (String) session.getAttribute("userID");
		logger.error("Got UserId from session in CustPersonIdGen: " + userID);
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration,
				postBodyCreation(userID), CustPersonIdGen.class);
		String result = httpConnectionPOST.connectToServer();
		return result;
	}

	private void parseRequest(String request) {
		ObjectMapper mapper = new ObjectMapper();
		Detail[] detail = null;
		try {
			detail = mapper.readValue(request, Detail[].class);

			for (int i = 0; i < detail.length; i++) {
				List<Field> group = detail[i].getFields();
				for (Field field : group) {
					logger.debug("Heiii" + field.getField().getTechnicalName().toString());
					String techName = field.getField().getTechnicalName().toString();
					logger.debug("techName.toLowerCase().equals(cust_FEOR1.toLowerCase()):"
							+ techName.toLowerCase().equals(cust_FEOR1.toLowerCase() + ""));
					logger.debug("techName.toLowerCase().equals(\"cust_WAY\".toLowerCase()):"
							+ techName.toLowerCase().equals("cust_WAY".toLowerCase() + ""));
					logger.debug("techName.toLowerCase().equals(\"cust_SECTOR\".toLowerCase()):"
							+ techName.toLowerCase().equals("cust_SECTOR".toLowerCase()) + "");
//					if (techName.toLowerCase().equals(cust_FEOR1.toLowerCase())) {
//						paramName = techName;
//						paramValue = field.getValue().toString();
////						logger.error(paramName.toString());
////						logger.error(paramValue.toString());
//					} else if (techName.toLowerCase().equals("cust_WAY".toLowerCase())) {
//						paramName2 = techName;
//						paramValue2 = field.getValue().toString();
//						logger.debug(paramName2.toString());
//						logger.debug(paramValue2.toString());
//					} else if (techName.toLowerCase().equals("cust_SECTOR".toLowerCase())) {
//						paramName3 = techName;
//						paramValue3 = field.getValue().toString();
//						logger.debug(paramName3.toString());
//						logger.debug(paramValue3.toString());
//					}
				}
			}
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private String postBodyCreation(String userId) {
		JSONObject obj = new JSONObject();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("uri", "generateNextPersonID('" + userID + "')");
		obj.put("__metadata", jsonObj);
//		obj.put(paramName, paramValue);
//		obj.put(paramName2, paramValue2);
//		obj.put(paramName3, paramValue3);
//		logger.error(obj.toJSONString());
		return obj.toJSONString();
	}

	@SuppressWarnings("unchecked")
	private String checkResp(String userID) {
		JSONObject obj = new JSONObject();
		if (userID != null && userID.length() > 0) {
			obj.put("message", userID);
		} else {
			obj.put("message", "ERROR");
		}
		return obj.toJSONString();
	}
}
