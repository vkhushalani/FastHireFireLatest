package com.amrest.fastHire.controller;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.connections.HttpConnectionGET;
import com.amrest.fastHire.utilities.CommonFunctions;
import com.amrest.fastHire.utilities.ConstantManager;
import com.amrest.fastHire.utilities.URLManager;

@RestController
@RequestMapping(value = ConstantManager.genAPI)
public class FOFrequencyController {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(FOFrequencyController.class);
	
	@GetMapping(value = ConstantManager.FOFrequency, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getFOFrequency(){
		URLManager genURL = new URLManager(getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);

		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpConnectionGET httpConnectionGET = new HttpConnectionGET(uri, URLManager.dConfiguration,
				FOFrequencyController.class);
		String result = httpConnectionGET.connectToServer();
		return result;
	}
	
}
