package com.amrest.fastHire.controller;

import java.net.URI;

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
public class CustPersonIdGen {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(CustPersonIdGen.class);
	private String userID;

	@GetMapping(value = ConstantManager.custPerIDGen, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String generateID() {

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
		this.userID = jsonObj.get("externalCode").toString();
		ConstantManager.userID = this.userID;
		logger.error("User ID" + this.userID);
		return checkResp(this.userID);
	}

	@SuppressWarnings("unchecked")
	private String checkResp(String userID) {
		JSONObject obj = new JSONObject();
		if (userID != null && userID.length() > 0) {
			obj.put("message", "SUCCESS");
		} else {
			obj.put("message", "ERROR");
		}
		return obj.toJSONString();
	}
}
