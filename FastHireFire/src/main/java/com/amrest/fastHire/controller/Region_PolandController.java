package com.amrest.fastHire.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.connections.HttpConnectionGET;
import com.amrest.fastHire.utilities.CommonFunctions;
import com.amrest.fastHire.utilities.ConstantManager;
import com.amrest.fastHire.utilities.URLManager;

@RestController
@RequestMapping(value = ConstantManager.genAPI)
public class Region_PolandController {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(Region_PolandController.class);
	
	@GetMapping(value = ConstantManager.regionPoland, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getRegions(@RequestParam(value="status",required = true) String status){
		Map<String,String> queryParams = new HashMap<>();
		queryParams.put("status", status);
		URLManager genURL = new URLManager(queryParams,getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);

		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpConnectionGET httpConnectionGET = new HttpConnectionGET(uri, URLManager.dConfiguration,
				Region_PolandController.class);
		String result = httpConnectionGET.connectToServer();
		return result;
	}
	
}
