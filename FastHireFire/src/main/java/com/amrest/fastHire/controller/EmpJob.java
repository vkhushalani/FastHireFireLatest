package com.amrest.fastHire.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
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
import com.amrest.fastHire.SF.DestinationClient;
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
	public static final String destinationName = "prehiremgrSFTest";
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

	private String paramContractEDateName = null;
	private String paramContractEDateValue = null;

	private String paramcontractTypeName = null;
	private String paramcontractTypeValue = null;

	private String paramFirmSubCategoryName = null;
	private String paramFirmSubCategoryValue = null;

	private String timeRecordingVariantName = null;
	private String timeRecordingVariantValue = null;

	private String payScaleTypeName = null;
	private String payScaleTypeValue = null;

	private String payScaleAreaName = null;
	private String payScaleAreaValue = null;

	private String employeeClassName = null;
	private String employeeClassValue = null;

	private final String sDate = "startdate";
	private final String empType = "employmenttype";
	private final String posType = "position";
	private final String holCalCode = "holidaycalendarcode";
	private final String timeTypeCode = "timetypeprofilecode";
	private final String workSchCode = "workschedulecode";
	private String contractEndDate = "contractEndDate";
	private String contractType = "contractType";
	private String customString1 = "customString1";

	private String jobCode = null;
	private String company = null;
	private String businessUnit = null;
	private String costCenter = null;
	private String payGrade = null;
	private String location = null;
	private String deparment = null;
	private String division = null;
	private String standardHours = null;
	private String parentCode = null;
	private String managerId = null;

	private static String datePattern = "dd/MM/yyyy";

	@PostMapping(value = ConstantManager.empJob, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String perPerson(@RequestBody String request, HttpServletRequest requestForSession)
			throws ParseException, NamingException, ClientProtocolException, IOException, URISyntaxException {
		HttpSession session = requestForSession.getSession(false);

		// Extract the params and their values
		parseRequest(request, session);

		// getting params for the json Body
		callAPI(paramPositionValue, session);
		getManagerFromEmpJob();

		URLManager genURL = new URLManager(getClass().getSimpleName(), configName);
		String urlToCall = genURL.formURLToCall();
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);

		// Get details from server
		URI uri = CommonFunctions.convertToURI(urlToCall);
		String userID = (String) session.getAttribute("userID");
		logger.error("Got UserId from session in EmpJob: " + userID);
		String customDateValue = (String) session.getAttribute("customDateValue");
		logger.error("Got customDateValue from session in EmpJob: " + customDateValue);
		String paramStartDateValue = (String) session.getAttribute("paramStartDateValue");
		logger.error("Got paramStartDateValue from session in EmpJob: " + paramStartDateValue);
		HttpConnectionPOST httpConnectionPOST = new HttpConnectionPOST(uri, URLManager.dConfiguration,
				replaceKeys(userID, customDateValue, paramStartDateValue), EmpJob.class);

		String result = httpConnectionPOST.connectToServer();
		return result;
	}

	private void getManagerFromEmpJob()
			throws NamingException, ClientProtocolException, IOException, URISyntaxException {
		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));
		HttpResponse response = destClient.callDestinationGET("/EmpJob?$format=json&$filter=positionNav/code eq '"
				+ parentCode + "'&$expand=positionNav&$select=userId", "");
		String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		JSONObject jsonObject = (JSONObject) JSONValue.parse(responseString);
		jsonObject = (JSONObject) jsonObject.get("d");
		JSONArray jsonArray = (JSONArray) jsonObject.get("results");
		jsonObject = (JSONObject) jsonArray.get(0);
		managerId = jsonObject.get("userId").toString();

	}

	// Call another api
	private void callAPI(String position, HttpSession session) {
		String urlToCall = URLManager.dConfiguration.getProperty("URL") + "/Position?$filter=code%20eq%20'" + position
				+ "'&$format=json&$expand=parentPosition&$select=code,location,payGrade,businessUnit,jobCode,department,division,company,costCenter,standardHours,parentPosition/code";
		logger.info(ConstantManager.lineSeparator + ConstantManager.urlLog + urlToCall + ConstantManager.lineSeparator);
//		logger.error(urlToCall);

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
			session.setAttribute("metaDataUpdatePosVac", metaData.get("uri").toString());
			logger.error("metaDataUpdatePosVac Set at session:" + metaData.get("uri").toString());
//			logger.error(jsonObject.toJSONString());

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
			if (jsonObject.get("parentPosition") != null) {
				JSONObject parentPositionObject = (JSONObject) JSONValue
						.parse(jsonObject.get("parentPosition").toString());
				parentCode = parentPositionObject.get("code").toString();
			} else {
				parentCode = "";
			}
		}
	}

	// Parse the request
	private void parseRequest(String request, HttpSession session)
			throws ParseException, URISyntaxException, NamingException {
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

						session.setAttribute("paramStartDateName", paramName);
						logger.error("Set paramStartDateName to session in EmpJob: " + paramName);
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Date today = new Date();
						Calendar now = Calendar.getInstance();
						now.setTime(today);
						now.set(Calendar.HOUR, 0);
						now.set(Calendar.MINUTE, 0);
						now.set(Calendar.SECOND, 0);
						now.set(Calendar.HOUR_OF_DAY, 0);

						session.setAttribute("paramStartDateValue", dateFormatted(sdf.format(now.getTime())));
						logger.error("paramStartDateValue Set at session from empjob:"
								+ dateFormatted(sdf.format(now.getTime())));
						session.setAttribute("paramOrgStartDateValue", sdf.format(now.getTime()));
						logger.error("paramOrgStartDateValue Set at session from empjob:" + sdf.format(now.getTime()));
//						logger.error(paramName.toString());
//						logger.error(paramValue.toString());
					} else if (name.toLowerCase().equals(empType.toLowerCase())) {
						paramEmpName = name;
						paramEmpValue = field.getValue().toString();
//						logger.error(paramEmpName.toString());
//						logger.error(paramEmpValue.toString());

					} else if (name.toLowerCase().equals(posType.toLowerCase())) {
						paramPositionName = name;
						paramPositionValue = field.getValue().toString();
//						logger.error(paramPositionName.toString());
//						logger.error(paramPositionValue.toString());

					} else if (name.toLowerCase().equals(holCalCode.toLowerCase())) {
						paramHolCodeName = name;
						paramHolNameValue = field.getValue().toString();
//						logger.error(paramHolCodeName.toString());
//						logger.error(paramHolNameValue.toString());

					} else if (name.toLowerCase().equals(timeTypeCode.toLowerCase())) {
						paramTimeTypeName = name;
						paramTimeTypeValue = field.getValue().toString();
//						logger.error(paramTimeTypeName.toString());
//						logger.error(paramTimeTypeValue.toString());

					} else if (name.toLowerCase().equals(workSchCode.toLowerCase())) {
						paramWorkSchName = name;
						paramWorkSchValue = field.getValue().toString();
						/*
						 * logger.error("paramWorkSchValue: " + paramWorkSchValue); DestinationClient
						 * destClient = new DestinationClient();
						 * destClient.setDestName(destinationName); destClient.setHeaderProvider();
						 * destClient.setConfiguration(); destClient.setDestConfiguration();
						 * destClient.setHeaders(destClient.getDestProperty("Authentication"));
						 * HttpResponse userResponse = destClient.callDestinationGET("/WorkSchedule",
						 * "?$format=json&$filter=externalCode eq '" + paramWorkSchValue +
						 * "'&$select=timeRecordingVariant"); String userResponseJsonString =
						 * EntityUtils.toString(userResponse.getEntity(), "UTF-8");
						 * logger.error("userResponseJsonString: " + userResponseJsonString);
						 * org.json.JSONObject jsonObj = new
						 * org.json.JSONObject(userResponseJsonString); jsonObj =
						 * jsonObj.getJSONObject("d"); jsonObj =
						 * jsonObj.getJSONArray("results").getJSONObject(0); // Get details from server
						 * timeRecordingVariantName = "timeRecordingVariant"; timeRecordingVariantValue
						 * = jsonObj.getString("timeRecordingVariant");
						 * logger.error("timeRecordingVariantValue: " + timeRecordingVariantValue);
						 * 
						 * logger.error("timeRecordingVariantValue: " + timeRecordingVariantValue);
						 */
//						logger.error(paramWorkSchValue.toString());

					} else if (name.toLowerCase().equals(contractEndDate.toLowerCase())) {
						paramContractEDateName = name;
						paramContractEDateValue = field.getValue().toString();
						paramContractEDateValue = dateFormatted(paramContractEDateValue);
//						logger.error(paramEmpName.toString());
//						logger.error(paramEmpValue.toString());

					} else if (name.toLowerCase().equals(contractType.toLowerCase())) {
						paramcontractTypeName = name;
						paramcontractTypeValue = field.getValue().toString();
//						logger.error(paramEmpName.toString());
//						logger.error(paramEmpValue.toString());

					} else if (name.toLowerCase().equals(customString1.toLowerCase())) {
						paramFirmSubCategoryName = name;
						paramFirmSubCategoryValue = field.getValue().toString();
//						logger.error(paramEmpName.toString());
//						logger.error(paramEmpValue.toString());

					}
//					else if (name.toLowerCase().equals("timerecordingvariant")) {
//						timeRecordingVariantName = name;
//						timeRecordingVariantValue = field.getValue().toString();
////						logger.error(paramEmpName.toString());
////						logger.error(paramEmpValue.toString());
//
//					} 
					else if (name.toLowerCase().equals("payscaletype")) {
						payScaleTypeName = name;
						payScaleTypeValue = field.getValue().toString();
//						logger.error(paramEmpName.toString());
//						logger.error(paramEmpValue.toString());

					} else if (name.toLowerCase().equals("payscalearea")) {
						payScaleAreaName = name;
						payScaleAreaValue = field.getValue().toString();
//						logger.error(paramEmpName.toString());
//						logger.error(paramEmpValue.toString());

					} else if (name.toLowerCase().equals("employeeclass")) {
						employeeClassName = name;
						employeeClassValue = field.getValue().toString();
//						logger.error(paramEmpName.toString());
//						logger.error(paramEmpValue.toString());

					}
				}
			}

		} catch (IOException e) {
			logger.error(e.toString());
		}

	}

	@SuppressWarnings("unchecked")
	private String replaceKeys(String userID, String customDateValue, String paramStartDateValue) {
		JSONObject obj = new JSONObject();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("uri", "EmpJob");
		obj.put("__metadata", jsonObj);

		obj.put("jobCode", jobCode);
		obj.put("userId", userID);
		obj.put(paramName, paramStartDateValue);
		obj.put("eventReason", "HIRNEW");
		obj.put("company", company);
		obj.put("businessUnit", businessUnit);
		obj.put("costCenter", costCenter);
		obj.put("employeeClass", "30094");
		obj.put("payGrade", payGrade);
		obj.put(paramPositionName, paramPositionValue);
		obj.put("location", location);
		// obj.put(paramEmpName, paramEmpValue);
		obj.put("department", deparment);
		obj.put("division", division);
		// obj.put(paramHolCodeName, paramHolNameValue);
		// obj.put(paramTimeTypeName, paramTimeTypeValue);
		// obj.put(paramWorkSchName, paramWorkSchValue);
		obj.put("standardHours", standardHours);
		obj.put("managerId", managerId);
		// obj.put(paramContractEDateName, paramContractEDateValue);
		// obj.put(paramcontractTypeName, paramcontractTypeValue);
		// obj.put(paramFirmSubCategoryName, paramFirmSubCategoryValue);
		// obj.put(ConstantManager.customDateName, customDateValue);

		// obj.put(timeRecordingVariantName, timeRecordingVariantValue);
		obj.put(payScaleTypeName, payScaleTypeValue);
		obj.put(payScaleAreaName, payScaleAreaValue);
		// obj.put(employeeClassName, employeeClassValue);

		if (obj.containsKey("null")) {
			obj.remove("null");
			logger.error("InsideNullCheck" + obj.toJSONString());
		}
//		logger.error(obj.toJSONString()); 
		logger.error("obj::::" + obj.toJSONString());
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
