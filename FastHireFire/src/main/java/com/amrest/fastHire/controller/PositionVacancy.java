package com.amrest.fastHire.controller;

import java.net.URI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.connections.HttpConnectionPOST;
import com.amrest.fastHire.utilities.CommonFunctions;
import com.amrest.fastHire.utilities.ConstantManager;
import com.amrest.fastHire.utilities.URLManager;

@RestController
@RequestMapping(value = ConstantManager.genAPI)
public class PositionVacancy {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(PositionVacancy.class);

	@GetMapping(value = ConstantManager.posVacancy, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String perPerson() {

		URLManager genURL = new URLManager(getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);

		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration, replaceKeys(),
				PositionVacancy.class);

		String result = httpConnectionPOST.connectToServer();
		String code = checkResp(result);
		String resp = sendResp(code);
		return resp;
	}

	@SuppressWarnings("unchecked")
	private String replaceKeys() {
		JSONObject obj = new JSONObject();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("uri", ConstantManager.metaDataUpdatePosVac);
		obj.put("__metadata", jsonObj);

		obj.put("vacant", false);
		logger.error(obj.toJSONString());
		return obj.toJSONString();
	}

	// Check the code
	@SuppressWarnings("unused")
	private String checkResp(String result) {
		String code = null;
		JSONObject jsonObj = (JSONObject) JSONValue.parse(result);
		JSONArray jsonArray = (JSONArray) jsonObj.get("d");
		for (int i = 0; i < jsonArray.size(); i++) {

			JSONObject json = (JSONObject) JSONValue.parse(jsonArray.get(i).toString());
			code = json.get("httpCode").toString();
			break;
		}
		return code;
	}

	// Send the response
	@SuppressWarnings("unchecked")
	private String sendResp(String code) {

		JSONObject obj = new JSONObject();
		if (code != null && code.equals("200")) {
			obj.put("message", "SUCCESS");
			obj.put("userID", ConstantManager.userID);
		} else {
			obj.put("message", "ERROR");
			obj.put("userID", "");
		}
		return obj.toJSONString();
	}
}
