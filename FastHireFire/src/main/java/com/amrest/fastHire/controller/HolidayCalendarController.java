package com.amrest.fastHire.controller;

import java.net.URI;
import java.util.ArrayList;

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
public class HolidayCalendarController {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(HolidayCalendarController.class);
	
	@GetMapping(value = ConstantManager.holidayCalendar, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getHolidayCalendar(@RequestParam(value="country",required = true) ArrayList<String> countries){
		ArrayList<String> queryParams = new ArrayList<>();
		queryParams.add(countries.get(0));
		queryParams.add(countries.get(1));
		URLManager genURL = new URLManager(queryParams,getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);
		
		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpConnectionGET httpConnectionGET = new HttpConnectionGET(uri, URLManager.dConfiguration,
				HolidayCalendarController.class);
		String result = httpConnectionGET.connectToServer();
		return result;
	}
	

	
}
