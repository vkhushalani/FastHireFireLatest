package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.POJO.Detail;
import com.amrest.fastHire.POJO.Field;
import com.amrest.fastHire.connections.HttpConnectionGET;
import com.amrest.fastHire.connections.HttpConnectionPOST;
import com.amrest.fastHire.utilities.CommonFunctions;
import com.amrest.fastHire.utilities.ConstantManager;
import com.amrest.fastHire.utilities.URLManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = ConstantManager.genAPI)
public class EmpJob {

	private static final String configName = "sfconfigname";
	private static final Logger logger = LoggerFactory.getLogger(EmpJob.class);

	private String paramName = null;
	private String paramValue = null;

	private String paramEmpName = null;
	private String paramEmpValue = null;

	private String paramPositionName = null;
	private String paramPositionValue = null;

	private String paramHolCodeName = null;
	private String paramHolNameValue = null;

	private String paramTimeTypeName = null;
	private String paramTimeTypeValue = null;

	private String paramWorkSchName = null;
	private String paramWorkSchValue = null;

	private final String sDate = "startdate";
	private final String empType = "employmenttype";
	private final String posType = "position";
	private final String holCalCode = "holidaycalendarcode";
	private final String timeTypeCode = "timetypeprofilecode";
	private final String workSchCode = "workschedulecode";

	private String jobCode = null;
	private String company = null;
	private String businessUnit = null;
	private String costCenter = null;
	private String payGrade = null;
	private String location = null;
	private String deparment = null;
	private String division = null;
	private String standardHours = null;

	private static String datePattern = "dd/MM/yyyy";

	@PostMapping(value = ConstantManager.empJob, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String perPerson(@RequestBody String request) throws ParseException {

		// Extract the params and their values
		parseRequest(request);

		// getting params for the json Body
		callAPI(paramPositionValue);

		URLManager genURL = new URLManager(getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);

		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration, replaceKeys(),
				EmpJob.class);

		String result = httpConnectionPOST.connectToServer();
		return result;
	}

	// Call another api
	private void callAPI(String position) {
		String urlToCall = URLManager.dConfiguration.getProperty("URL") + "Position?$filter=code%20eq%20'" + position
				+ "'&$format=json&$select=code,location,payGrade,businessUnit,jobCode,department,division,company,costCenter,standardHours";
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);
		logger.error(urlToCall);
		
		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		HttpConnectionGET get = new HttpConnectionGET(uri, URLManager.dConfiguration, EmpJob.class);
		String result = get.connectToServer();

		// parsing Position Details
		JSONObject jsonObj = (JSONObject) JSONValue.parse(result);
		jsonObj = (JSONObject) jsonObj.get("d");
		JSONArray jsonArray = (JSONArray) jsonObj.get("results");

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonArray.get(i).toString());			
			JSONObject metaData = (JSONObject) jsonObject.get("__metadata");
			ConstantManager.metaDataUpdatePosVac = metaData.get("uri").toString();
			
			logger.error(jsonObject.toJSONString());

			if (jsonObject.get("jobCode") != null) {
				jobCode = jsonObject.get("jobCode").toString();
			} else {
				jobCode = "";
			}

			if (jsonObject.get("company") != null) {
				company = jsonObject.get("company").toString();
			} else {
				company = "";
			}

			if (jsonObject.get("businessUnit") != null) {
				businessUnit = jsonObject.get("businessUnit").toString();
			} else {
				businessUnit = "";
			}

			if (jsonObject.get("costCenter") != null) {
				costCenter = jsonObject.get("costCenter").toString();
			} else {
				costCenter = "";
			}

			if (jsonObject.get("payGrade") != null) {
				payGrade = jsonObject.get("payGrade").toString();
			} else {
				payGrade = "";
			}

			if (jsonObject.get("location") != null) {
				location = jsonObject.get("location").toString();
			} else {
				location = "";
			}

			if (jsonObject.get("department") != null) {
				deparment = jsonObject.get("department").toString();
			} else {
				deparment = "";
			}

			if (jsonObject.get("division") != null) {
				division = jsonObject.get("division").toString();
			} else {
				division = "";
			}

			if (jsonObject.get("standardHours") != null) {
				standardHours = jsonObject.get("standardHours").toString();
			} else {
				standardHours = "0";
			}
		}
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
					String name = field.getField().getTechnicalName().toString();

					if (name.toLowerCase().equals(sDate.toLowerCase())) {
						paramName = name;
						paramValue = field.getValue().toString();

						ConstantManager.paramStartDateName = paramName;
						
						
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Date today = new Date();
						Calendar now = Calendar.getInstance();
						now.setTime(today);
				        now.set(Calendar.HOUR, 0);
				        now.set(Calendar.MINUTE, 0);
				        now.set(Calendar.SECOND, 0);
				        now.set(Calendar.HOUR_OF_DAY, 0);
						
						ConstantManager.paramStartDateValue = dateFormatted(sdf.format(now.getTime()));
						ConstantManager.paramOrgStartDateValue = sdf.format(now.getTime());
						
						

						logger.error(paramName.toString());
						logger.error(paramValue.toString());
					} else if (name.toLowerCase().equals(empType.toLowerCase())) {
						paramEmpName = name;
						paramEmpValue = field.getValue().toString();
						logger.error(paramEmpName.toString());
						logger.error(paramEmpValue.toString());

					} else if (name.toLowerCase().equals(posType.toLowerCase())) {
						paramPositionName = name;
						paramPositionValue = field.getValue().toString();
						logger.error(paramPositionName.toString());
						logger.error(paramPositionValue.toString());

					} else if (name.toLowerCase().equals(holCalCode.toLowerCase())) {
						paramHolCodeName = name;
						paramHolNameValue = field.getValue().toString();
						logger.error(paramHolCodeName.toString());
						logger.error(paramHolNameValue.toString());

					} else if (name.toLowerCase().equals(timeTypeCode.toLowerCase())) {
						paramTimeTypeName = name;
						paramTimeTypeValue = field.getValue().toString();
						logger.error(paramTimeTypeName.toString());
						logger.error(paramTimeTypeValue.toString());

					} else if (name.toLowerCase().equals(workSchCode.toLowerCase())) {
						paramWorkSchName = name;
						paramWorkSchValue = field.getValue().toString();
						logger.error(paramWorkSchName.toString());
						logger.error(paramWorkSchValue.toString());

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

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("uri", "EmpJob");
		obj.put("__metadata", jsonObj);

		obj.put("jobCode", jobCode);
		obj.put("userId", userID);
		obj.put(paramName, ConstantManager.paramStartDateValue);
//		obj.put(ConstantManager.paramEndDateName, ConstantManager.paramEndDateValue);
		obj.put("eventReason", "HIRNEW");
		obj.put("company", company);
		obj.put("businessUnit", businessUnit);
		obj.put("managerId", "NO_MANAGER");
		obj.put("costCenter", costCenter);
		obj.put("employeeClass", "45341");
		obj.put("payGrade", payGrade);
		obj.put(paramPositionName, paramPositionValue);
		obj.put("location", location);
		obj.put(paramEmpName, paramEmpValue);
		obj.put("department", deparment);
		obj.put("division", division);
		obj.put(paramHolCodeName, paramHolNameValue);
		obj.put(paramTimeTypeName, paramTimeTypeValue);
		obj.put(paramWorkSchName, paramWorkSchValue);
		obj.put("standardHours", standardHours);
		logger.error(obj.toJSONString());
		return obj.toJSONString();
	}

	private String dateFormatted(String startDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
		Date date = null;
		try {
			date = simpleDateFormat.parse(startDate);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		long epoch = date.getTime();
		return "/Date(" + epoch + ")/";
	}
}
