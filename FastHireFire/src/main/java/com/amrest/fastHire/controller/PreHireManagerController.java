package com.amrest.fastHire.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipOutputStream;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.olingo.odata2.api.batch.BatchException;
import org.apache.olingo.odata2.api.client.batch.BatchSingleResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amrest.fastHire.SF.BatchRequest;
import com.amrest.fastHire.SF.DestinationClient;
import com.amrest.fastHire.SF.PexClient;
import com.amrest.fastHire.model.CodeList;
import com.amrest.fastHire.model.CodeListText;
import com.amrest.fastHire.model.ConfirmStatus;
import com.amrest.fastHire.model.Contract;
import com.amrest.fastHire.model.ContractCriteria;
import com.amrest.fastHire.model.Field;
import com.amrest.fastHire.model.FieldDataFromSystem;
import com.amrest.fastHire.model.FieldGroupText;
import com.amrest.fastHire.model.FieldText;
import com.amrest.fastHire.model.MapCountryBusinessUnit;
import com.amrest.fastHire.model.MapCountryBusinessUnitTemplate;
import com.amrest.fastHire.model.MapTemplateFieldGroup;
import com.amrest.fastHire.model.MapTemplateFieldProperties;
import com.amrest.fastHire.model.SFAPI;
import com.amrest.fastHire.model.SFConstants;
import com.amrest.fastHire.model.Template;
import com.amrest.fastHire.service.BusinessUnitService;
import com.amrest.fastHire.service.CodeListService;
import com.amrest.fastHire.service.CodeListTextService;
import com.amrest.fastHire.service.ConfirmStatusService;
import com.amrest.fastHire.service.ContractCriteriaService;
import com.amrest.fastHire.service.ContractService;
import com.amrest.fastHire.service.FieldDataFromSystemService;
import com.amrest.fastHire.service.FieldGroupTextService;
import com.amrest.fastHire.service.FieldService;
import com.amrest.fastHire.service.FieldTextService;
import com.amrest.fastHire.service.MapCountryBusinessUnitService;
import com.amrest.fastHire.service.MapCountryBusinessUnitTemplateService;
import com.amrest.fastHire.service.MapTemplateFieldGroupService;
import com.amrest.fastHire.service.MapTemplateFieldPropertiesService;
import com.amrest.fastHire.service.SFAPIService;
import com.amrest.fastHire.service.SFConstantsService;
import com.amrest.fastHire.utilities.CreateZip;
import com.amrest.fastHire.utilities.DashBoardPositionClass;
import com.amrest.fastHire.utilities.DropDownKeyValue;
import com.google.gson.Gson;

@RestController
@RequestMapping("/PreHireManager")
public class PreHireManagerController {
	public static final String destinationName = "prehiremgrSFTest";
	public static final String scpDestinationName = "scpiBasic";
	public static final String pexDestinationName = "FastHirePEX";
	public static final String docdestinationName = "DocumentGeneration";
	public static final String pocDocDestinationName = "DocGeneration";

	private enum hunLocale {
		január, február, március, április, május, junius, julius, augusztus, szeptember, október, november, december
	};

	String timeStamp;
	int counter;

	public static final Integer padStartDate = 15;
	public static final Integer confirmStartDateDiffDays = 2;

	Logger logger = LoggerFactory.getLogger(PreHireManagerController.class);

	@Autowired
	MapCountryBusinessUnitService mapCountryBusinessUnitService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	MapCountryBusinessUnitTemplateService mapCountryBusinessUnitTemplateService;

	@Autowired
	FieldTextService fieldTextService;

	@Autowired
	MapTemplateFieldGroupService mapTemplateFieldGroupService;

	@Autowired
	MapTemplateFieldPropertiesService mapTemplateFieldPropertiesService;

	@Autowired
	CodeListService codeListService;

	@Autowired
	CodeListTextService codeListTextService;

	@Autowired
	FieldDataFromSystemService fieldDataFromSystemService;

	@Autowired
	SFAPIService sfAPIService;

	@Autowired
	FieldService fieldService;

	@Autowired
	SFConstantsService sfConstantsService;

	@Autowired
	FieldGroupTextService fieldGroupTextService;

	@Autowired
	ContractService contractService;

	@Autowired
	ContractCriteriaService contractCriteriaService;

	@Autowired
	ConfirmStatusService confirmStatusService;

	@GetMapping(value = "/UserDetails")
	public ResponseEntity<?> getUserDetails(HttpServletRequest request)
			throws NamingException, ClientProtocolException, IOException, URISyntaxException {
		String loggedInUser = request.getUserPrincipal().getName();
		if (loggedInUser.equalsIgnoreCase("S0018810731") || loggedInUser.equalsIgnoreCase("S0018269301")
				|| loggedInUser.equalsIgnoreCase("S0018810731") || loggedInUser.equalsIgnoreCase("S0019013022")) {
			loggedInUser = "E00000118";
		}

		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));

		// call to get local language of the logged in user
		HttpResponse userResponse = destClient.callDestinationGET("/User", "?$filter=userId eq '" + loggedInUser
				+ "'&$format=json&$select=userId,lastName,firstName,email,defaultLocale");
		String userResponseJsonString = EntityUtils.toString(userResponse.getEntity(), "UTF-8");
		JSONObject userResponseObject = new JSONObject(userResponseJsonString);
		userResponseObject = userResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
		return ResponseEntity.ok().body(userResponseObject.toString());
	}

	@GetMapping(value = "/DashBoardPositions")
	public ResponseEntity<List<DashBoardPositionClass>> getDashBoardPositions(HttpServletRequest request)
			throws NamingException, ClientProtocolException, IOException, URISyntaxException, java.text.ParseException {
		HttpSession session = request.getSession(false);
		String loggedInUser = request.getUserPrincipal().getName();
		// need to remove this code
		if (loggedInUser.equalsIgnoreCase("S0018810731") || loggedInUser.equalsIgnoreCase("S0018269301")
				|| loggedInUser.equalsIgnoreCase("S0018810731") || loggedInUser.equalsIgnoreCase("S0019013022")) {
			loggedInUser = "E00000118";
		}

		Map<String, String> paraMap = new HashMap<String, String>();
		List<DashBoardPositionClass> returnPositions = new ArrayList<DashBoardPositionClass>();

		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));

		// get the Emjob Details of the logged In user

		HttpResponse empJobResponse = destClient.callDestinationGET("/EmpJob", "?$filter=userId eq '" + loggedInUser
				+ "' &$format=json&$expand=positionNav,positionNav/companyNav&$select=positionNav/company,positionNav/department,position,positionNav/companyNav/country");
		String empJobResponseJsonString = EntityUtils.toString(empJobResponse.getEntity(), "UTF-8");
		JSONObject empJobResponseObject = new JSONObject(empJobResponseJsonString);
		empJobResponseObject = empJobResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
		paraMap.put("company", empJobResponseObject.getJSONObject("positionNav").getString("company"));
		paraMap.put("department", empJobResponseObject.getJSONObject("positionNav").getString("department"));
		paraMap.put("country",
				empJobResponseObject.getJSONObject("positionNav").getJSONObject("companyNav").getString("country"));
		session.setAttribute("country",
				empJobResponseObject.getJSONObject("positionNav").getJSONObject("companyNav").getString("country"));
		logger.debug("Set country to session in PerHireManager: "
				+ empJobResponseObject.getJSONObject("positionNav").getJSONObject("companyNav").getString("country"));
		paraMap.put("position", empJobResponseObject.getString("position"));
		// SFConstants vacantEmployeeClass = null;

		String vacantPositionFilter = "?$filter=" + "vacant eq true and company eq '" + paraMap.get("company") + "' "
				+ "and department eq '" + paraMap.get("department") + "' " + "and parentPosition/code eq '"
				+ paraMap.get("position") + "' " + "&$format=json" + "&$expand=employeeClassNav" + "&$select="
				+ "externalName_localized," + "externalName_defaultValue," + "payGrade,jobTitle,code,"
				+ "employeeClassNav/label_defaultValue," + "employeeClassNav/label_localized";

//		if (vacantEmployeeClass != null) {
//
//			vacantPositionFilter = "?$filter=" + "vacant eq true and company eq '" + paraMap.get("company") + "' "
//					+ "and department eq '" + paraMap.get("department") + "' " + "and employeeClass eq '"
//					+ vacantEmployeeClass.getValue() + "' " + "and parentPosition/code eq '" + paraMap.get("position")
//					+ "' " + "&$format=json" + "&$expand=employeeClassNav" + "&$select=" + "externalName_localized,"
//					+ "externalName_defaultValue," + "payGrade,jobTitle,code," + "employeeClassNav/label_defaultValue,"
//					+ "employeeClassNav/label_localized";
//
//		}
		// get Vacant Positions
		HttpResponse vacantPosResponse = destClient.callDestinationGET("/Position", vacantPositionFilter);
		String vacantPosResponseJsonString = EntityUtils.toString(vacantPosResponse.getEntity(), "UTF-8");
		JSONObject vacantPosResponseObject = new JSONObject(vacantPosResponseJsonString);
		JSONArray vacantPosResultArray = vacantPosResponseObject.getJSONObject("d").getJSONArray("results");

		for (int i = 0; i < vacantPosResultArray.length(); i++) {
			JSONObject vacantPos = vacantPosResultArray.getJSONObject(i);
			DashBoardPositionClass pos = new DashBoardPositionClass();
			pos.setPayGrade(vacantPos.getString("payGrade"));
			pos.setPositionCode(vacantPos.getString("code"));
			pos.setPositionTitle(vacantPos.getString("externalName_localized") != null
					? vacantPos.getString("externalName_localized")
					: vacantPos.getString("externalName_defaultValue"));// null
																		// check
			pos.setEmployeeClassName(vacantPos.getJSONObject("employeeClassNav").getString("label_localized") != null
					? vacantPos.getJSONObject("employeeClassNav").getString("label_localized")
					: vacantPos.getJSONObject("employeeClassNav").getString("label_defaultValue"));// null
																									// check
			pos.setUserFirstName(null);
			pos.setUserLastName(null);
			pos.setUserId(null);
			// pos.setLastUpdatedDate(vacantPos.getString("createdDate"));
			pos.setDayDiff(null);
			pos.setVacant(true);
			pos.setStartDate(null);
			pos.setStatuses(null);
			returnPositions.add(pos);

		}

		SFConstants employeeClassConstant = sfConstantsService.findById("employeeClassId");
		SFConstants empStatusConstant = sfConstantsService.findById("emplStatusId");

		// get OnGoing Hiring
		HttpResponse ongoingPosResponse = destClient.callDestinationGET("/EmpJob",
				"?$format=json&$filter=" + "employeeClass eq '" + employeeClassConstant.getValue() + "' and "
						+ "company eq '" + paraMap.get("company") + "' and " + "department eq '"
						+ paraMap.get("department") + "' and " + "emplStatusNav/id ne '" + empStatusConstant.getValue()
						+ "' " + "and userNav/userId ne null &$expand=positionNav,userNav,"
						+ "positionNav/employeeClassNav,userNav/personKeyNav" + "&$select=userId,startDate,position,"
						+ "positionNav/externalName_localized," + "positionNav/externalName_defaultValue,"
						+ "positionNav/payGrade,positionNav/jobTitle,"
						+ "userNav/userId,userNav/username,userNav/defaultFullName,"
						+ "userNav/firstName,userNav/lastName," + "positionNav/employeeClassNav/label_localized,"
						+ "positionNav/employeeClassNav/label_defaultValue," + "userNav/personKeyNav/perPersonUuid");

		String ongoingPosResponseJsonString = EntityUtils.toString(ongoingPosResponse.getEntity(), "UTF-8");
		JSONObject ongoingPosResponseObject = new JSONObject(ongoingPosResponseJsonString);
		JSONArray ongoingPosResultArray = ongoingPosResponseObject.getJSONObject("d").getJSONArray("results");

		SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy");
		Date today = dateformatter.parse(dateformatter.format(new Date()));

		for (int i = 0; i < ongoingPosResultArray.length(); i++) {

			JSONObject ongoingPos = ongoingPosResultArray.getJSONObject(i);
			// logger.debug("userNav"+ongoingPos.get("userNav"));

			if (!ongoingPos.get("userNav").toString().equalsIgnoreCase("null")) {

				String personuuId = ongoingPos.getJSONObject("userNav").getJSONObject("personKeyNav")
						.getString("perPersonUuid");
				ConfirmStatus confirmStatus = confirmStatusService.findById(personuuId);
				logger.debug("confirmStatus" + confirmStatus);
				if (confirmStatus == null) {

					DashBoardPositionClass pos = new DashBoardPositionClass();
					pos.setPayGrade(ongoingPos.getJSONObject("positionNav").getString("payGrade"));
					pos.setPositionCode(ongoingPos.getString("position"));
					pos.setPositionTitle(
							ongoingPos.getJSONObject("positionNav").getString("externalName_localized") != null
									? ongoingPos.getJSONObject("positionNav").getString("externalName_localized")
									: ongoingPos.getJSONObject("positionNav").getString("externalName_defaultValue"));
					pos.setEmployeeClassName(ongoingPos.getJSONObject("positionNav").getJSONObject("employeeClassNav")
							.getString("label_localized") != null
									? ongoingPos.getJSONObject("positionNav").getJSONObject("employeeClassNav")
											.getString("label_localized")
									: ongoingPos.getJSONObject("positionNav").getJSONObject("employeeClassNav")
											.getString("label_defaultValue"));
					pos.setUserFirstName(ongoingPos.getJSONObject("userNav").getString("firstName"));
					pos.setUserLastName(ongoingPos.getJSONObject("userNav").getString("lastName"));
					pos.setUserId(ongoingPos.getJSONObject("userNav").getString("userId"));
					// pos.setLastUpdatedDate(ongoingPos.getString("createdOn"));
					pos.setVacant(false);
					pos.setStatuses(null);
					String startDate = ongoingPos.getString("startDate");
					String smilliSec = startDate.substring(startDate.indexOf("(") + 1, startDate.indexOf(")"));
					long smilliSecLong = Long.valueOf(smilliSec).longValue() - TimeUnit.DAYS.toMillis(padStartDate);
					smilliSec = Objects.toString(smilliSecLong, null);
					startDate = startDate.replace(
							startDate.substring(startDate.indexOf("(") + 1, startDate.lastIndexOf(")")), smilliSec);
					pos.setStartDate(startDate);

					long diffInMillies = Math.abs(smilliSecLong - today.getTime());
					long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
					pos.setDayDiff(Objects.toString(diff, null)); // calculate
																	// day
																	// difference

					returnPositions.add(pos);
				}
			}

		}

		// add the item till the DB has an entry of ongoing

		List<ConfirmStatus> confirmStatuses = confirmStatusService.findByCountryDepartment(paraMap.get("company"),
				paraMap.get("department"));

		for (ConfirmStatus confirmStatus : confirmStatuses) {

			Date confirmDate = dateformatter.parse(dateformatter.format(confirmStatus.getUpdatedOn()));
			if (confirmDate.equals(today) || !confirmStatus.getDocGenFlag().equalsIgnoreCase("SUCCESS")) {
				HttpResponse personResponse = destClient.callDestinationGET("/PerPerson", "?$filter=perPersonUuid eq '"
						+ confirmStatus.getId() + "'&$format=json&$select=personIdExternal");
				String personResponseString = EntityUtils.toString(personResponse.getEntity(), "UTF-8");
				JSONObject personResponseObject = new JSONObject(personResponseString);
				personResponseObject = personResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);

				HttpResponse userResponse = destClient.callDestinationGET("/User",
						"?$filter=userId  eq '" + personResponseObject.getString("personIdExternal")
								+ "'&$format=json&$select=userId,lastName,firstName");

				String userResponseString = EntityUtils.toString(userResponse.getEntity(), "UTF-8");
				JSONObject userResponseObject = new JSONObject(userResponseString);
				userResponseObject = userResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);

				HttpResponse empResponse = destClient.callDestinationGET("/Position",
						"?$filter=code eq '" + confirmStatus.getPosition() + "'&$format=json"
								+ "&$expand=employeeClassNav" + "&$select=" + "externalName_localized,"
								+ "externalName_defaultValue," + "payGrade,jobTitle,code,"
								+ "employeeClassNav/label_defaultValue," + "employeeClassNav/label_localized");

				String empResponseJsonString = EntityUtils.toString(empResponse.getEntity(), "UTF-8");
				JSONObject empResponseObject = new JSONObject(empResponseJsonString);
				JSONObject empResultObject = empResponseObject.getJSONObject("d").getJSONArray("results")
						.getJSONObject(0);

				DashBoardPositionClass pos = new DashBoardPositionClass();
				pos.setPayGrade(empResultObject.getString("payGrade"));
				pos.setPositionCode(empResultObject.getString("code"));
				pos.setPositionTitle(empResultObject.getString("externalName_localized") != null
						? empResultObject.getString("externalName_localized")
						: empResultObject.getString("externalName_defaultValue"));// null
																					// check
				pos.setEmployeeClassName(
						empResultObject.getJSONObject("employeeClassNav").getString("label_localized") != null
								? empResultObject.getJSONObject("employeeClassNav").getString("label_localized")
								: empResultObject.getJSONObject("employeeClassNav").getString("label_defaultValue"));// null
																														// check
				pos.setUserFirstName(userResponseObject.getString("firstName"));
				pos.setUserLastName(userResponseObject.getString("lastName"));
				pos.setUserId(personResponseObject.getString("personIdExternal"));
				pos.setDayDiff(null);
				pos.setVacant(false);
				pos.setStartDate(null);

				Map<String, String> statusMap = new HashMap<String, String>();

				statusMap.put("SFFlag", confirmStatus.getSfEntityFlag());
				statusMap.put("PexFlag", confirmStatus.getPexUpdateFlag());
				statusMap.put("DocFlag", confirmStatus.getDocGenFlag());
				pos.setStatuses(statusMap);

				returnPositions.add(pos);
			}
		}
		// return the JSON Object
		return ResponseEntity.ok().body(returnPositions);

	}

	@GetMapping(value = "/FormTemplate")
	public ResponseEntity<?> getFormTemplateFields(HttpServletRequest request,
			@RequestParam(value = "businessUnit", required = false) String businessUnitId,
			@RequestParam(value = "position", required = true) String position)
			throws NamingException, ClientProtocolException, IOException, URISyntaxException {
		HttpSession session = request.getSession(false);
		String loggedInUser = request.getUserPrincipal().getName();
		if (loggedInUser.equalsIgnoreCase("S0018810731") || loggedInUser.equalsIgnoreCase("S0018269301")
				|| loggedInUser.equalsIgnoreCase("S0018810731") || loggedInUser.equalsIgnoreCase("S0019013022")) {
			loggedInUser = "E00000118";
		}

		Date today = new Date();
		Template template = null;
		JSONObject returnObject = new JSONObject();
		JSONArray returnArray = new JSONArray();

		Map<String, String> compareMap = new HashMap<String, String>();

		compareMap.put("businessUnit", businessUnitId);
		compareMap.put("position", position);

		// make calls to get language and country

		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));

		// call to get local language of the logged in user

		HttpResponse userResponse = destClient.callDestinationGET("/User",
				"?$filter=userId eq '" + loggedInUser + "'&$format=json&$select=defaultLocale");
		String userResponseJsonString = EntityUtils.toString(userResponse.getEntity(), "UTF-8");
		JSONObject userResponseObject = new JSONObject(userResponseJsonString);
		userResponseObject = userResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
		compareMap.put("locale", userResponseObject.getString("defaultLocale"));
		session.setAttribute("defaultLocale", userResponseObject.getString("defaultLocale"));
		logger.debug(
				"Set defaultLocale to session in PerHireManager: " + userResponseObject.getString("defaultLocale"));
		HttpResponse empJobResponse = destClient.callDestinationGET("/EmpJob", "?$filter=userId eq '" + loggedInUser
				+ "' &$format=json&$expand=positionNav,positionNav/companyNav&$select=position,positionNav/companyNav/country,positionNav/company,positionNav/department");
		String empJobResponseJsonString = EntityUtils.toString(empJobResponse.getEntity(), "UTF-8");
		JSONObject empJobResponseObject = new JSONObject(empJobResponseJsonString);
		empJobResponseObject = empJobResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);

		compareMap.put("company", empJobResponseObject.getJSONObject("positionNav").getString("company"));
		compareMap.put("department", empJobResponseObject.getJSONObject("positionNav").getString("department"));
		compareMap.put("country",
				empJobResponseObject.getJSONObject("positionNav").getJSONObject("companyNav").getString("country"));

		SFConstants employeeClassConstant = sfConstantsService.findById("employeeClassId");
		SFConstants empStatusConstant = sfConstantsService.findById("emplStatusId");

		HttpResponse positionResponse = destClient.callDestinationGET("/Position", "?$filter=code eq '" + position
				+ "'&$format=json&$select=code,vacant,location,payGrade,jobCode,standardHours,externalName_localized");
		String positionResponseJsonString = EntityUtils.toString(positionResponse.getEntity(), "UTF-8");
		JSONObject positionResponseObject = new JSONObject(positionResponseJsonString);
		positionResponseObject = positionResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);

		returnObject.put("PositionDetails", positionResponseObject);

		if (!positionResponseObject.getBoolean("vacant")) {
			HttpResponse candidateResponse = destClient.callDestinationGET("/EmpJob",
					"?$format=json&$filter=position eq '" + compareMap.get("position") + "' and employeeClass eq '"
							+ employeeClassConstant.getValue() + "' and company eq '" + compareMap.get("company")
							+ "' and department eq '" + compareMap.get("department") + "' and emplStatusNav/id ne '"
							+ empStatusConstant.getValue()
							+ "' and userNav/userId ne null &$expand=userNav &$select=userId,position,userNav/userId,userNav/username,userNav/defaultFullName,userNav/firstName,userNav/lastName");
			String candidateResponseJsonString = EntityUtils.toString(candidateResponse.getEntity(), "UTF-8");
			JSONObject candidateResponseObject = new JSONObject(candidateResponseJsonString);
			JSONArray candidateResponseArray = candidateResponseObject.getJSONObject("d").getJSONArray("results");
			candidateResponseObject = candidateResponseArray.getJSONObject(0);
			compareMap.put("category", "CONFIRM");
			compareMap.put("candidateId", candidateResponseObject.getString("userId"));
			returnObject.put("CandidateDetails", candidateResponseObject.getJSONObject("userNav"));
		} else {
			compareMap.put("category", "INITIATE");
			compareMap.put("candidateId", "");
		}

		// Get the Business Unit and Company Map
		MapCountryBusinessUnit mapCountryBusinessUnit;
		if (businessUnitId == null) {
			List<MapCountryBusinessUnit> mapCountryBusinessUnitList = mapCountryBusinessUnitService
					.findByCountry(compareMap.get("country"));
			mapCountryBusinessUnit = mapCountryBusinessUnitList.get(0);
		} else {
			mapCountryBusinessUnit = mapCountryBusinessUnitService.findByCountryBusinessUnit(compareMap.get("country"),
					businessUnitId);
		}
		// getting the template per BUnit and Country
		List<MapCountryBusinessUnitTemplate> mapTemplateList = mapCountryBusinessUnitTemplateService
				.findByCountryBusinessUnitId(mapCountryBusinessUnit.getId());

		// getting valid template per category (Initiate or confirm)
		for (MapCountryBusinessUnitTemplate mapTemplate : mapTemplateList) {

			if (today.before(mapTemplate.getEndDate()) && today.after(mapTemplate.getStartDate())) {

				if (mapTemplate.getTemplate().getCategory().equalsIgnoreCase(compareMap.get("category"))) {
					template = mapTemplate.getTemplate();
					break;
				}

			}
		}

		if (template != null) {
			// get all field groups for the template
			List<MapTemplateFieldGroup> templateFieldGroups = mapTemplateFieldGroupService
					.findByTemplate(template.getId());
			if (templateFieldGroups.size() != 0) {
				HashMap<String, String> responseMap = new HashMap<>();
				Set<String> entities = new HashSet<String>();

				// get the details of Position for CONFIRM Hire Template value
				// setting
				// logger.debug("get the details of Position for CONFIRM Hire
				// Template value setting ");
				if (compareMap.get("category").equalsIgnoreCase("CONFIRM")) {

					// get all the fields Entity Names Distinct
					for (MapTemplateFieldGroup fieldGroup : templateFieldGroups) {
						List<MapTemplateFieldProperties> fieldProperties = mapTemplateFieldPropertiesService
								.findByTemplateFieldGroup(fieldGroup.getId());
						for (MapTemplateFieldProperties fieldProp : fieldProperties) {
							if (fieldProp.getField().getEntityName() != null) {
								entities.add(fieldProp.getField().getEntityName());
							}
						}
					}

					// call the entity urls which are independent like Empjob
					// logger.debug("call the entity urls which are independent
					// like Empjob ");
					for (String entity : entities) {

						SFAPI sfApi = sfAPIService.findById(entity, "GET");

						if (sfApi != null && sfApi.getTagSource().equalsIgnoreCase("UI")) {

							DestinationClient destClientPos = new DestinationClient();
							destClientPos.setDestName(destinationName);
							destClientPos.setHeaderProvider();
							destClientPos.setConfiguration();
							destClientPos.setDestConfiguration();
							destClientPos.setHeaders(destClientPos.getDestProperty("Authentication"));

							String url = sfApi.getUrl().replace("<" + sfApi.getReplaceTag() + ">",
									compareMap.get(sfApi.getTagSourceValuePath()));

							HttpResponse responsePos = destClientPos.callDestinationGET(url, "");

							String responseString = EntityUtils.toString(responsePos.getEntity(), "UTF-8");
							responseMap.put(entity, responseString);

						}
					}
					// logger.debug("call the entity URLs which dependent on
					// other entities ");
					// call the entity URLs which dependent on other entities
					for (String entity : entities) {

						SFAPI sfApi = sfAPIService.findById(entity, "GET");

						if (sfApi != null && !sfApi.getTagSource().equalsIgnoreCase("UI")) {
							DestinationClient destClientPos = new DestinationClient();
							destClientPos.setDestName(destinationName);
							destClientPos.setHeaderProvider();
							destClientPos.setConfiguration();
							destClientPos.setDestConfiguration();
							destClientPos.setHeaders(destClientPos.getDestProperty("Authentication"));

							JSONObject depEntityObj = new JSONObject(responseMap.get(sfApi.getTagSource()));

							JSONArray responseResult = depEntityObj.getJSONObject("d").getJSONArray("results");
							JSONObject positionEntity = responseResult.getJSONObject(0);

							// get the dependent value from the dependent entity

							String replaceValue = getValueFromPathJson(positionEntity, sfApi.getTagSourceValuePath(),
									compareMap);

							// replacing the tag variable from the dependent
							// entity data value
							if (replaceValue != null && replaceValue.length() != 0) {
								String url = sfApi.getUrl().replace("<" + sfApi.getReplaceTag() + ">", replaceValue);

								HttpResponse responsePos = destClientPos.callDestinationGET(url, "");
								if (responsePos.getStatusLine().getStatusCode() == 200) {

									String responseString = EntityUtils.toString(responsePos.getEntity(), "UTF-8");
									responseMap.put(entity, responseString);
								}
							}
						}
					}
				}

				// sort the groups

				Collections.sort(templateFieldGroups);

				// logger.debug("Loop the field Group to create the response
				// JSON array ");
				// Loop the field Group to create the response JSON array
				for (MapTemplateFieldGroup tFieldGroup : templateFieldGroups) {
					// manager app will only have fields which have is manager
					// visible = true
					if (tFieldGroup.getIsVisibleManager()) {
						JSONObject fieldObject = new JSONObject();
						Gson gson = new Gson();

						if (tFieldGroup.getFieldGroup() != null) {
							// setting the field Group
							// logger.debug("setting the field
							// Group"+tFieldGroup.getFieldGroup().getName());
							tFieldGroup.getFieldGroup().setFieldGroupSeq(tFieldGroup.getFieldGroupSeq());
							FieldGroupText fieldGroupText = fieldGroupTextService
									.findByFieldGroupLanguage(tFieldGroup.getFieldGroupId(), compareMap.get("locale"));
							if (fieldGroupText != null) {
								tFieldGroup.getFieldGroup().setName(fieldGroupText.getName());
							}

							String jsonString = gson.toJson(tFieldGroup.getFieldGroup());
							fieldObject.put("fieldGroup", new JSONObject(jsonString));

							// logger.debug("creating the fields entity in the
							// json per field
							// group"+tFieldGroup.getFieldGroup().getName());
							// creating the fields entity in the json per field
							// group
							List<MapTemplateFieldProperties> mapTemplateFieldPropertiesList = mapTemplateFieldPropertiesService
									.findByTemplateFieldGroupManager(tFieldGroup.getId(), true);
							// List<MapTemplateFieldProperties>
							// mapTemplateFieldPropertiesList =
							// mapTemplateFieldPropertiesService.findByTemplateFieldGroup(tFieldGroup.getId());

							// sort the fieldProperties

							Collections.sort(mapTemplateFieldPropertiesList);

							for (MapTemplateFieldProperties mapTemplateFieldProperties : mapTemplateFieldPropertiesList) {

								// setting field labels
								// logger.debug("setting field
								// labels"+mapTemplateFieldProperties.getField().getTechnicalName());

								FieldText fieldText = fieldTextService.findByFieldLanguage(
										mapTemplateFieldProperties.getFieldId(), compareMap.get("locale"));

								if (fieldText != null) {

									mapTemplateFieldProperties.getField().setName(fieldText.getName());
								}

								// setting the field values
								// logger.debug("setting the field
								// values"+mapTemplateFieldProperties.getField().getName());
								// INITIATE Template
								if (compareMap.get("category").equalsIgnoreCase("INITIATE")) {
									if (mapTemplateFieldProperties.getValue() == null) {
										if (mapTemplateFieldProperties.getField().getInitialValue() != null) {
											mapTemplateFieldProperties
													.setValue(mapTemplateFieldProperties.getField().getInitialValue());
										} else {
											mapTemplateFieldProperties.setValue("");
										}
									}

								} else {
									// CONFIRM Template
									if (mapTemplateFieldProperties.getField().getEntityName() != null) {
										if (responseMap
												.get(mapTemplateFieldProperties.getField().getEntityName()) != null) {
											// logger.debug("responseMap:"+responseMap.get(mapTemplateFieldProperties.getField().getEntityName()));
											JSONObject responseObject = new JSONObject(responseMap
													.get(mapTemplateFieldProperties.getField().getEntityName()));

											JSONArray responseResult = responseObject.getJSONObject("d")
													.getJSONArray("results");
											// logger.debug("responseResult:"+responseResult);
											if (responseResult.length() != 0) {
												JSONObject positionEntity = responseResult.getJSONObject(0);
												// logger.debug("positionEntity:"+positionEntity);
												String value;
												if (!mapTemplateFieldProperties.getField().getTechnicalName()
														.equalsIgnoreCase("startDate")) {
													value = getValueFromPathJson(positionEntity,
															mapTemplateFieldProperties.getField().getValueFromPath(),
															compareMap);
												} else {

													value = getValueFromPathJson(positionEntity, "customString11",
															compareMap);

													String milliSec = value.substring(value.indexOf("(") + 1,
															value.indexOf(")"));
													// logger.debug("Endate
													// Milli Sec: "+milliSec);
													long milliSecLong = Long.valueOf(milliSec).longValue()
															- TimeUnit.DAYS.toMillis(padStartDate);
													// milliSecLong =
													// milliSecLong +
													// TimeUnit.DAYS.toMillis(confirmStartDateDiffDays);
													milliSec = Objects.toString(milliSecLong, null);
													// logger.debug("New
													// milliSec Milli Sec:
													// "+milliSec);
													value = value.replace(value.substring(value.indexOf("(") + 1,
															value.lastIndexOf(")")), milliSec);

												}
												// logger.debug("value:"+value);
												if (value != null) {
													// logger.debug("value not
													// null:"+value);
													if (mapTemplateFieldProperties.getField().getFieldType()
															.equalsIgnoreCase("Codelist")) {
														// logger.debug("Inside
														// Codelist
														// "+mapTemplateFieldProperties.getField().getName());

														List<CodeList> CodeList = codeListService.findByCountryField(
																mapTemplateFieldProperties.getFieldId(),
																compareMap.get("country"));
														String codeListId = null;
														if (CodeList.size() == 1) {
															codeListId = CodeList.get(0).getId();
														} else {
															for (CodeList codeObject : CodeList) {

																for (MapTemplateFieldProperties existingField : mapTemplateFieldPropertiesList) {
																	// logger.debug("existingField
																	// Name: "+
																	// existingField.getField().getName());

																	// logger.debug("
																	// Existing
																	// Field
																	// TechName:
																	// "+
																	// existingField.getField().getTechnicalName());
																	Field dependentField = fieldService
																			.findById(codeObject.getDependentFieldId());
																	// logger.debug("Dependent
																	// Field
																	// TecName:
																	// "+
																	// dependentField.getTechnicalName());
																	if (existingField.getField().getTechnicalName()
																			.equalsIgnoreCase(dependentField
																					.getTechnicalName())) {
																		// logger.debug("
																		// Both
																		// Dependent
																		// and
																		// Existing
																		// Field
																		// Match
																		// "+existingField.getField().getTechnicalName());
																		// logger.debug("existingField
																		// Value"+existingField.getValue());
																		// logger.debug("Code
																		// List
																		// Object
																		// Dependent
																		// Value"+codeObject.getDependentFieldValue());
																		String existingFieldValue = null;
																		if (existingField.getField()
																				.getEntityName() != null) {
																			SFAPI existingFieldEntity = sfAPIService
																					.findById(
																							existingField.getField()
																									.getEntityName(),
																							"GET");

																			String dependentValuePath, dependentEname;
																			if (existingFieldEntity.getTagSource()
																					.equalsIgnoreCase("UI")) {
																				dependentEname = existingField
																						.getField().getEntityName();
																				dependentValuePath = existingField
																						.getField().getValueFromPath();
																			} else {
																				dependentEname = existingFieldEntity
																						.getTagSource();
																				dependentValuePath = existingFieldEntity
																						.getTagSourceValuePath();
																			}
																			JSONObject dependentJsonObj = new JSONObject(
																					responseMap.get(dependentEname));
																			dependentJsonObj = dependentJsonObj
																					.getJSONObject("d")
																					.getJSONArray("results")
																					.getJSONObject(0);
																			existingFieldValue = getValueFromPathJson(
																					dependentJsonObj,
																					dependentValuePath, compareMap);
																		} else {
																			existingFieldValue = existingField
																					.getValue();
																		}
																		// logger.debug("existingFieldValue"+existingFieldValue);
																		if (existingFieldValue.equalsIgnoreCase(
																				codeObject.getDependentFieldValue())) {
																			// logger.debug("Both
																			// Value
																			// Existing
																			// and
																			// Dependent
																			// Match
																			// "+existingFieldValue);
																			codeListId = codeObject.getId();

																			break;
																		}
																	}
																}
																if (codeListId != null) {
																	break;
																}

															}
														}

														// here
														CodeListText clText = codeListTextService.findById(codeListId,
																compareMap.get("locale"), value);

														if (clText != null) {
															value = clText.getDescription();
														} else {
															value = "";
														}
													}
													mapTemplateFieldProperties.setValue(value);
												} else {

													mapTemplateFieldProperties.setValue("");
												}
											} else {
												mapTemplateFieldProperties.setValue("");
											}
										} else {
											mapTemplateFieldProperties.setValue("");
										}
									} else {
										// logger.debug("No
										// Entity"+mapTemplateFieldProperties.getField().getTechnicalName());
										if (mapTemplateFieldProperties.getField().getInitialValue() != null) {
											mapTemplateFieldProperties
													.setValue(mapTemplateFieldProperties.getField().getInitialValue());
										} else {
											mapTemplateFieldProperties.setValue("");
										}
									}
								}
								// making the field input type if the is
								// Editable Manager is false
								// logger.debug(" making the field input type if
								// the is Editable Manager is
								// false"+mapTemplateFieldProperties.getField().getName());
								if (!mapTemplateFieldProperties.getIsEditableManager()) {
									mapTemplateFieldProperties.getField().setFieldType("Input");

									/// may be we need to call the picklist to
									/// get the labels instead of key value
									/// for few fields
								}

								if (mapTemplateFieldProperties.getIsVisibleManager()) {
									// logger.debug("setting drop down values if
									// picklist, codelist,
									// entity"+mapTemplateFieldProperties.getField().getName());
									// setting drop down values if picklist,
									// codelist, entity

									List<DropDownKeyValue> dropDown = new ArrayList<DropDownKeyValue>();
									List<FieldDataFromSystem> fieldDataFromSystemList;

									// switch case the picklist , entity and
									// codelist to get the data from various
									// systems
									switch (mapTemplateFieldProperties.getField().getFieldType()) {
									case "Picklist":
										// logger.debug("Picklist"+mapTemplateFieldProperties.getField().getName());
										fieldDataFromSystemList = fieldDataFromSystemService.findByFieldCountry(
												mapTemplateFieldProperties.getField().getId(),
												compareMap.get("country"));

										if (fieldDataFromSystemList.size() != 0) {
											FieldDataFromSystem fieldDataFromSystem = fieldDataFromSystemList.get(0);

											//
											// logger.debug("ID:
											// "+fieldDataFromSystem.getFieldId()
											// +", Name: "+
											// mapTemplateFieldProperties.getField().getName()+fieldDataFromSystem.getIsDependentField());
											String picklistUrlFilter = getPicklistUrlFilter(fieldDataFromSystem,
													mapTemplateFieldProperties, compareMap, responseMap, destClient);
											// logger.debug("picklistUrlFilter"+picklistUrlFilter);

											HttpResponse response = destClient.callDestinationGET(
													fieldDataFromSystem.getPath(), picklistUrlFilter);

											String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");

											// logger.debug("responseJson:"+responseJson);
											JSONObject responseObject = new JSONObject(responseJson);

											JSONArray responseResult = responseObject.getJSONObject("d")
													.getJSONArray("results");
											for (int i = 0; i < responseResult.length(); i++) {
												DropDownKeyValue keyValue = new DropDownKeyValue();
												String key = (String) responseResult.getJSONObject(i)
														.get(fieldDataFromSystem.getKey());
												keyValue.setKey(key);

												JSONArray pickListLabels = responseResult.getJSONObject(i)
														.getJSONObject("picklistLabels").getJSONArray("results");
												for (int j = 0; j < pickListLabels.length(); j++) {
													if (pickListLabels.getJSONObject(j).get("locale").toString()
															.equalsIgnoreCase(compareMap.get("locale"))) {
														keyValue.setValue(pickListLabels.getJSONObject(j).get("label")
																.toString());
													}
												}

												dropDown.add(keyValue);
											}

										}
										break;
									case "Entity":
										// logger.debug("Entity"+mapTemplateFieldProperties.getField().getName());
										fieldDataFromSystemList = fieldDataFromSystemService.findByFieldCountry(
												mapTemplateFieldProperties.getField().getId(),
												compareMap.get("country"));

										if (fieldDataFromSystemList.size() != 0) {
											FieldDataFromSystem fieldDataFromSystem = fieldDataFromSystemList.get(0);

											// logger.debug("ID:
											// "+fieldDataFromSystem.getFieldId()
											// +", Name: "+
											// mapTemplateFieldProperties.getField().getName()+fieldDataFromSystem.getIsDependentField());
											String picklistUrlFilter = getPicklistUrlFilter(fieldDataFromSystem,
													mapTemplateFieldProperties, compareMap, responseMap, destClient);
											// logger.debug("picklistUrlFilter"+picklistUrlFilter);
											HttpResponse response = destClient.callDestinationGET(
													fieldDataFromSystem.getPath(), picklistUrlFilter);

											String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
											JSONObject responseObject = new JSONObject(responseJson);
											// logger.debug("responseObject"+responseObject);
											JSONArray responseResult = responseObject.getJSONObject("d")
													.getJSONArray("results");
											String valuePath = fieldDataFromSystem.getValue();
											String[] valuePathArray = valuePath.split("/");
											String keyPath = fieldDataFromSystem.getKey();
											String[] keyPathArray = keyPath.split("/");
											JSONObject temp = null;
											int index = 0;

											// logger.debug("valuePathArray.length"+valuePathArray.length);
											if (valuePathArray.length > 1 && keyPathArray.length > 1) {
												for (int k = 0; k < valuePathArray.length - 1; k++) {
													JSONObject tempObj = responseResult.getJSONObject(0)
															.getJSONObject(valuePathArray[index]);
													if (tempObj.has("results")) {
														responseResult = tempObj.getJSONArray("results");
													} else {
														JSONArray tempArray = new JSONArray();
														tempArray.put(tempObj);
														responseResult = tempArray;
													}
													index = index + 1;
												}
											}

											for (int i = 0; i < responseResult.length(); i++) {
												temp = responseResult.getJSONObject(i);
												DropDownKeyValue keyValue = new DropDownKeyValue();
												if (valuePathArray[index].contains("<locale>")) {

													valuePathArray[index] = valuePathArray[index].replace("<locale>",
															compareMap.get("locale"));
												}
												keyValue.setValue(temp.get(valuePathArray[index]).toString());
												keyValue.setKey(temp.get(keyPathArray[index]).toString());

												dropDown.add(keyValue);
											}
										}

										break;
									case "Codelist":
										// logger.debug("Codelist"+mapTemplateFieldProperties.getField().getName());

										List<CodeList> codeList = codeListService.findByCountryField(
												mapTemplateFieldProperties.getField().getId(),
												compareMap.get("country"));
										if (codeList.size() != 0) {
											if (codeList.size() == 1) {
												List<CodeListText> codeListValues = codeListTextService
														.findByCodeListIdLang(codeList.get(0).getId(),
																compareMap.get("locale"));
												for (CodeListText value : codeListValues) {
													DropDownKeyValue keyValue = new DropDownKeyValue();
													keyValue.setKey(value.getValue());
													keyValue.setValue(value.getDescription());
													dropDown.add(keyValue);
												}
											}

										}
										break;
									}

									mapTemplateFieldProperties.setDropDownValues(dropDown);

								}
							}
							fieldObject.put("fields", mapTemplateFieldPropertiesList);
							returnArray.put(fieldObject);
						}
					}
				}

			}
		}
		returnObject.put("TemplateFieldGroups", returnArray);
		return ResponseEntity.ok().body(returnObject.toString());

	}

	private String getPicklistUrlFilter(FieldDataFromSystem fieldDataFromSystem,
			MapTemplateFieldProperties mapTemplateFieldProperties, Map<String, String> compareMap,
			HashMap<String, String> responseMap, DestinationClient destClient)
			throws ClientProtocolException, IOException, URISyntaxException {
		String picklistUrlFilter = fieldDataFromSystem.getFilter();
		if (fieldDataFromSystem.getIsDependentField()) {
			// logger.debug("inside is dependent field :
			// "+mapTemplateFieldProperties.getField().getName());
			if (fieldDataFromSystem.getTagSourceFromSF() != null) {
				// logger.debug("From tag source SF :
				// "+mapTemplateFieldProperties.getField().getName());
				SFAPI depenedentEntity = sfAPIService.findById(fieldDataFromSystem.getTagSourceFromSF(), "GET");
				String dependentUrl;
				if (depenedentEntity.getTagSource().equalsIgnoreCase("UI2")) {

					// logger.debug("Source is dependent on UI
					// input"+mapTemplateFieldProperties.getField().getName());
					dependentUrl = depenedentEntity.getUrl().replace("<" + depenedentEntity.getReplaceTag() + ">",
							compareMap.get(depenedentEntity.getTagSourceValuePath()));
				} else {
					JSONObject responseObject = new JSONObject(responseMap.get(depenedentEntity.getTagSource()));
					JSONArray responseResult = responseObject.getJSONObject("d").getJSONArray("results");
					String dValue = getValueFromPathJson(responseResult.getJSONObject(0),
							depenedentEntity.getTagSourceValuePath(), compareMap);
					dependentUrl = depenedentEntity.getUrl().replace("<" + depenedentEntity.getReplaceTag() + ">",
							dValue);
				}
				// logger.debug("dependentUrl:"+dependentUrl);
				HttpResponse dependentResponse = destClient.callDestinationGET(dependentUrl, "");
				String dependentResponseJson = EntityUtils.toString(dependentResponse.getEntity(), "UTF-8");
				// logger.debug("dependentResponseJson:"+dependentResponseJson);
				JSONObject dependentResponseObject = new JSONObject(dependentResponseJson);
				JSONArray dependentResponseResult = dependentResponseObject.getJSONObject("d").getJSONArray("results");
				String replaceValue = getValueFromPathJson(dependentResponseResult.getJSONObject(0),
						fieldDataFromSystem.getTagSourceValuePath(), compareMap);
				// logger.debug("replaceValue:"+replaceValue);
				picklistUrlFilter = picklistUrlFilter.replace("<" + fieldDataFromSystem.getReplaceTag() + ">",
						replaceValue);
				// logger.debug("picklistUrlFilter:"+picklistUrlFilter);
			} else if (fieldDataFromSystem.getTagSourceFromField() != null) {

				Field dependentField = fieldService.findById(fieldDataFromSystem.getTagSourceFromField());
				String replaceValue = null;
				if (dependentField.getInitialValue() != null) {
					replaceValue = dependentField.getInitialValue();
				} else {
					List<MapTemplateFieldProperties> fieldsManagerVisibleList = mapTemplateFieldPropertiesService
							.findByFieldIdVisibleManager(dependentField.getId(), true);
					if (fieldsManagerVisibleList.get(0).getValue() != null) {
						replaceValue = fieldsManagerVisibleList.get(0).getValue();
					}
				}
				if (replaceValue != null) {
					picklistUrlFilter = picklistUrlFilter.replace("<" + fieldDataFromSystem.getReplaceTag() + ">",
							replaceValue);
				}
			}
		}
		return picklistUrlFilter;

	}

	public String getValueFromPathJson(JSONObject positionEntity, String path, Map<String, String> compareMap) {

		String[] techPathArray = path.split("/");
		// String [] techPathArray =
		// mapTemplateFieldProperties.getField().getValueFromPath().split("/");
		JSONArray tempArray;
		// logger.debug("techPathArray"+techPathArray.length);
		for (int i = 0; i < techPathArray.length; i++) {
			// logger.debug("Step"+i+"techPathArray.length -
			// 1"+(techPathArray.length - 1));
			if (i != techPathArray.length - 1) {
				// logger.debug("techPathArray["+i+"]"+techPathArray[i]);
				if (techPathArray[i].contains("[]")) {

					tempArray = positionEntity.getJSONArray(techPathArray[i].replace("[]", ""));
					// logger.debug("tempArray"+i+tempArray);
					if (tempArray.length() != 0) {
						String findObjectKey = techPathArray[i + 1].substring(techPathArray[i + 1].indexOf("(") + 1,
								techPathArray[i + 1].indexOf(")"));
						for (int j = 0; j < tempArray.length(); j++) {

							// logger.debug("findObjectKey"+j+findObjectKey);
							if (tempArray.getJSONObject(j).get(findObjectKey).toString()
									.equalsIgnoreCase(compareMap.get(findObjectKey))) {

								positionEntity = tempArray.getJSONObject(j);
								// logger.debug("positionEntity"+j+positionEntity);
								techPathArray[i + 1] = techPathArray[i + 1].replace("(" + findObjectKey + ")", "");
								// logger.debug("techPathArray[i+1]"+(i+1)+techPathArray[i+1]);
								break;

							}
						}
					} else {

						break;
					}
				} else if (techPathArray[i].contains("[0]")) {

					tempArray = positionEntity.getJSONArray(techPathArray[i].replace("[0]", ""));
					if (tempArray.length() != 0) {
						positionEntity = tempArray.getJSONObject(0);
					} else {
						break;
					}
				} else {

					try {
						// logger.debug("Object no Array"+techPathArray[i]);
						positionEntity = positionEntity.getJSONObject(techPathArray[i]);
						// logger.debug("Object no Array
						// positionEntity"+positionEntity);
					} catch (JSONException exception) {

						break;
					}
				}
			} else {
				try {
					// logger.debug("techPathArray["+i+"]"+techPathArray[i]);
					if (techPathArray[i].contains("<locale>")) {
						// logger.debug("with label
						// techPathArray["+i+"]"+techPathArray[i]);
						techPathArray[i] = techPathArray[i].replace("<locale>", compareMap.get("locale"));
					}
					// logger.debug("positionEntity.get(techPathArray[i]).toString()"+i+positionEntity.get(techPathArray[i]).toString());
					return positionEntity.get(techPathArray[i]).toString();
				} catch (JSONException exception) {
					return "";
				}
			}
		}
		return "";

	}

	@GetMapping("/GetDropDown/{fieldId}")
	public ResponseEntity<List<DropDownKeyValue>> getDependentDropDown(@PathVariable("fieldId") String fieldId,
			@RequestParam(value = "triggerFieldId", required = true) String triggerFieldId,
			@RequestParam(value = "selectedValue", required = true) String selectedValue, HttpServletRequest request)
			throws NamingException, ClientProtocolException, IOException, URISyntaxException {
		HttpSession session = request.getSession(false);
		String loggedInUser = request.getUserPrincipal().getName();
		if (loggedInUser.equalsIgnoreCase("S0018810731") || loggedInUser.equalsIgnoreCase("S0018269301")
				|| loggedInUser.equalsIgnoreCase("S0018810731") || loggedInUser.equalsIgnoreCase("S0019013022")) {
			loggedInUser = "E00000118";
		}

		Map<String, String> map = new HashMap<String, String>();

		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));

		// call to get local language of the logged in user

//		HttpResponse userResponse = destClient.callDestinationGET("/User",
//				"?$filter=userId eq '" + loggedInUser + "'&$format=json&$select=defaultLocale");
//		String userResponseJsonString = EntityUtils.toString(userResponse.getEntity(), "UTF-8");
//		JSONObject userResponseObject = new JSONObject(userResponseJsonString);
//		userResponseObject = userResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
//		map.put("locale", userResponseObject.getString("defaultLocale"));
//
//		HttpResponse empJobResponse = destClient.callDestinationGET("/EmpJob", "?$filter=userId eq '" + loggedInUser
//				+ "' &$format=json&$expand=positionNav,positionNav/companyNav&$select=position,positionNav/companyNav/country,positionNav/company,positionNav/department");
//		String empJobResponseJsonString = EntityUtils.toString(empJobResponse.getEntity(), "UTF-8");
//		JSONObject empJobResponseObject = new JSONObject(empJobResponseJsonString);
//		empJobResponseObject = empJobResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);

		map.put("country", (String) session.getAttribute("country"));
		logger.debug("Got country from session in PreHireManagerController: " + session.getAttribute("country"));
		map.put("locale", (String) session.getAttribute("defaultLocale"));
		logger.debug(
				"Got defaultLocale from session in PreHireManagerController: " + session.getAttribute("defaultLocale"));
		map.put("fieldId", fieldId);
		map.put("selectedValue", selectedValue);
		map.put("triggerFieldId", triggerFieldId);

		List<DropDownKeyValue> resultDropDown = new ArrayList<DropDownKeyValue>();

		Field affectedField = fieldService.findById(fieldId);
		switch (affectedField.getFieldType()) {
		case "Codelist":
			CodeList affectedFieldCodelist = codeListService.findByCountryFieldDependent(map.get("fieldId"),
					map.get("country"), map.get("triggerFieldId"), map.get("selectedValue"));
			if (affectedFieldCodelist != null) {
				List<CodeListText> affectedFieldCodelistTextList = codeListTextService
						.findByCodeListIdLang(affectedFieldCodelist.getId(), map.get("locale"));
				for (CodeListText codeListText : affectedFieldCodelistTextList) {
					DropDownKeyValue keyValuePair = new DropDownKeyValue();
					keyValuePair.setKey(codeListText.getValue());
					keyValuePair.setValue(codeListText.getDescription());
					resultDropDown.add(keyValuePair);
				}
			}
			break;
		case "Picklist":
			FieldDataFromSystem fieldDataFrom = fieldDataFromSystemService
					.findByFieldCountry(map.get("fieldId"), map.get("country")).get(0);
			if (fieldDataFrom.getIsDependentField()) {
				String sourceFromField = fieldDataFrom.getTagSourceFromField();
				if (sourceFromField != null) {
					String filter = fieldDataFrom.getFilter().replace("<" + fieldDataFrom.getReplaceTag() + ">",
							map.get("selectedValue"));
					HttpResponse response = destClient.callDestinationGET(fieldDataFrom.getPath(), filter);

					String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
					JSONObject responseObject = new JSONObject(responseJson);

					JSONArray responseResult = responseObject.getJSONObject("d").getJSONArray("results");
					for (int i = 0; i < responseResult.length(); i++) {
						DropDownKeyValue keyValue = new DropDownKeyValue();
						String key = (String) responseResult.getJSONObject(i).get(fieldDataFrom.getKey());
						keyValue.setKey(key);

						JSONArray pickListLabels = responseResult.getJSONObject(i).getJSONObject("picklistLabels")
								.getJSONArray("results");
						for (int j = 0; j < pickListLabels.length(); j++) {
							if (pickListLabels.getJSONObject(j).get("locale").toString()
									.equalsIgnoreCase(map.get("locale"))) {
								keyValue.setValue(pickListLabels.getJSONObject(j).get("label").toString());
							}
						}

						resultDropDown.add(keyValue);
					}
				}

			}
			break;
		case "Entity":

			break;
		default:
			break;
		}

		return ResponseEntity.ok().body(resultDropDown);
	}

	@PostMapping("/InactiveUser")
	public ResponseEntity<?> inactiveUser(@RequestBody String postJson)
			throws NamingException, IOException, URISyntaxException {
		Map<String, String> map = new HashMap<String, String>();
		// get post JSON Object
		JSONObject postObject = new JSONObject(postJson);
		Iterator<?> keys = postObject.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			map.put(key, postObject.getString(key));
		}
		// declare the destinaton client to call SF APis
		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));

		// get the json string for vacant position
		JSONObject iAnctiveCandidateJson = readJSONFile("/JSONFiles/InactiveCandidate.json");

		if (iAnctiveCandidateJson != null) {

			String iAnctiveCandidateJsonString = iAnctiveCandidateJson.toString();

			for (Map.Entry<String, String> entry : map.entrySet()) {
				iAnctiveCandidateJsonString = iAnctiveCandidateJsonString.replaceAll("<" + entry.getKey() + ">",
						entry.getValue());
			}
			HttpResponse inActiveCandidateResponse = destClient.callDestinationPOST("/upsert", "?$format=json",
					iAnctiveCandidateJsonString);
			// String inActiveCandidateResponseJson =
			// EntityUtils.toString(inActiveCandidateResponse.getEntity(),
			// "UTF-8");
			return ResponseEntity.ok().body("Success");

		}
		return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/CancelHire")
	public ResponseEntity<?> cancelHire(@RequestBody String postJson)
			throws FileNotFoundException, IOException, ParseException, URISyntaxException, NamingException {
		Map<String, String> map = new HashMap<String, String>();

		// get post JSON Object
		JSONObject postObject = new JSONObject(postJson);
		Iterator<?> keys = postObject.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			map.put(key, postObject.getString(key));
		}

		// declare the destinaton client to call SF APis
		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));

		// get Job Code from the user calling Emp job
		HttpResponse EmpJobResponse = destClient.callDestinationGET("/EmpJob", "?$filter=userId eq '"
				+ map.get("userId")
				+ "' &$format=json&$select=startDate,userId,employmentType,workscheduleCode,jobCode,division,standardHours,costCenter,payGrade,eventReason,department,timeTypeProfileCode,businessUnit,managerId,position,employeeClass,location,holidayCalendarCode,company");
		String EmpJobResponseJson = EntityUtils.toString(EmpJobResponse.getEntity(), "UTF-8");
		JSONObject EmpJobResponseObject = new JSONObject(EmpJobResponseJson);
		EmpJobResponseObject = EmpJobResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
		String jobCode = EmpJobResponseObject.getString("position");

		// get Uri from the job code calling position details
		HttpResponse posResponse = destClient.callDestinationGET("/Position", "?$filter=code eq '" + jobCode
				+ "'&$format=json&$select=code,location,payGrade,businessUnit,jobCode,department,division,company");
		String posResponseJson = EntityUtils.toString(posResponse.getEntity(), "UTF-8");
		JSONObject posResponseObject = new JSONObject(posResponseJson);
		String uri = posResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0)
				.getJSONObject("__metadata").getString("uri");

		// change the location of empjob entity foe background process deletion
		// of
		// candidates

		// EmpJobResponseObject.put("location", "NA");
		EmpJobResponseObject.put("eventReason", "TEROTH");
		EmpJobResponseObject.getJSONObject("__metadata").put("uri", "EmpJob");
		HttpResponse EmpJobPostResponse = destClient.callDestinationPOST("/upsert", "?$format=json",
				EmpJobResponseObject.toString());
		String EmpJobPostResponseJson = EntityUtils.toString(EmpJobPostResponse.getEntity(), "UTF-8");

		// logger.debug("responseJson to update locaton" +
		// EmpJobPostResponseJson);
		JSONObject EmpJobPostResponseObj = new JSONObject(EmpJobPostResponseJson);
		String status = EmpJobPostResponseObj.getJSONArray("d").getJSONObject(0).getString("status");
		if (status.equalsIgnoreCase("OK")) {

			// get the json string for vacant position
			JSONObject vacantJsonObject = readJSONFile("/JSONFiles/CancelHire.json");

			// replace the values from the map
			if (vacantJsonObject != null) {
				map.put("uri", uri);
				String vacantJsonString = vacantJsonObject.toString();

				for (Map.Entry<String, String> entry : map.entrySet()) {
					vacantJsonString = vacantJsonString.replaceAll("<" + entry.getKey() + ">", entry.getValue());
				}
				// logger.debug("replace vacantJsonString: "+vacantJsonString);

				HttpResponse vacantResponse = destClient.callDestinationPOST("/upsert", "?$format=json",
						vacantJsonString);
				String vacantResponseJson = EntityUtils.toString(vacantResponse.getEntity(), "UTF-8");
				return ResponseEntity.ok().body(vacantResponseJson);
			}
		}
		// }
		logger.debug(status.toString());
		return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Transactional(timeout = 300)
	@PostMapping("/ConfirmHire")
	public ResponseEntity<?> confirmlHire(@RequestBody String postJson, HttpServletRequest request)
			throws FileNotFoundException, IOException, ParseException, URISyntaxException, NamingException,
			java.text.ParseException, BatchException, UnsupportedOperationException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String loggedInUser = request.getUserPrincipal().getName();

		Map<String, String> map = new HashMap<String, String>();

		// get post JSON Object
		JSONObject postObject = new JSONObject(postJson);
		int startDateCheck = daysBetween(postObject.getString("startDate"));
		if (startDateCheck >= 2) {
			Iterator<?> keys = postObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				map.put(key, postObject.getString(key));
			}

			// Get URI details
			DestinationClient destClient = new DestinationClient();
			destClient.setDestName(destinationName);
			destClient.setHeaderProvider();
			destClient.setConfiguration();
			destClient.setDestConfiguration();
			destClient.setHeaders(destClient.getDestProperty("Authentication"));

			// batch intitialization
			BatchRequest batchRequest = new BatchRequest();
			batchRequest.configureDestination(destinationName);

			Map<String, String> entityMap = new HashMap<String, String>();
			Map<String, String> entityResponseMap = new HashMap<String, String>();

			entityMap.put("EmpPayCompRecurring", "?$filter=userId eq '" + map.get("userId")
					+ "'&$format=json&$select=userId,startDate,payComponent,paycompvalue,currencyCode,frequency,notes");
			entityMap.put("EmpCompensation", "?$filter=userId eq '" + map.get("userId")
					+ "'&$format=json&$select=userId,startDate,payGroup,eventReason");
			entityMap.put("EmpEmployment", "?$filter=personIdExternal eq '" + map.get("userId")
					+ "'&$format=json&$select=userId,startDate,personIdExternal");
			entityMap.put("PaymentInformationV3", "?$format=json&$filter=worker eq '" + map.get("userId")
					+ "'&$expand=toPaymentInformationDetailV3&$select=effectiveStartDate,worker,toPaymentInformationDetailV3/PaymentInformationV3_effectiveStartDate,toPaymentInformationDetailV3/PaymentInformationV3_worker,toPaymentInformationDetailV3/amount,toPaymentInformationDetailV3/accountNumber,toPaymentInformationDetailV3/bank,toPaymentInformationDetailV3/payType,toPaymentInformationDetailV3/iban,toPaymentInformationDetailV3/purpose,toPaymentInformationDetailV3/routingNumber,toPaymentInformationDetailV3/bankCountry,toPaymentInformationDetailV3/currency,toPaymentInformationDetailV3/businessIdentifierCode,toPaymentInformationDetailV3/paymentMethod");
			entityMap.put("PerPersonal", "?$filter=personIdExternal eq '" + map.get("userId")
					+ "'&$format=json&$select=startDate,personIdExternal,birthName,initials,middleName,customString1,maritalStatus,certificateStartDate,title,namePrefix,salutation,nativePreferredLang,customDate4,since,gender,lastName,nameFormat,firstName,certificateEndDate,preferredName,secondNationality,suffix,formalName,nationality");
			entityMap.put("PerAddressDEFLT", "?$filter=personIdExternal eq '" + map.get("userId")
					+ "'&$format=json&$select=startDate,personIdExternal,addressType,address1,address2,address3,city,zipCode,country,address7,address6,address5,address4,county,address9,address8");
			entityMap.put("EmpJob", "?$filter=userId eq '" + map.get("userId")
					+ "'&$format=json&$expand=positionNav/companyNav,positionNav&$select=positionNav/externalName_localized,positionNav/companyNav/country,jobTitle,startDate,userId,jobCode,employmentType,workscheduleCode,division,standardHours,costCenter,payGrade,department,timeTypeProfileCode,businessUnit,managerId,position,employeeClass,countryOfCompany,location,holidayCalendarCode,company,eventReason,contractEndDate,contractType,customString1");
			entityMap.put("PerPerson", "?$filter=personIdExternal  eq '" + map.get("userId")
					+ "'&$format=json&$select=personIdExternal,dateOfBirth,placeOfBirth,perPersonUuid");
			entityMap.put("PerEmail", "?$filter=personIdExternal eq '" + map.get("userId")
					+ "'&$format=json&$select=personIdExternal,emailAddress");
			entityMap.put("cust_Additional_Information",
					"?$format=json&$filter=externalCode eq '" + map.get("userId") + "'");
			entityMap.put("cust_personIdGenerate", "?$format=json&$filter=externalCode eq '" + map.get("userId") + "'");

			// reading the records and creating batch post body

			for (Map.Entry<String, String> entity : entityMap.entrySet()) {
				batchRequest.createQueryPart("/" + entity.getKey() + entity.getValue(), entity.getKey());
			}

			timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			logger.debug("Before Batch Call GET" + timeStamp);
			// call Get Batch with all entities
			batchRequest.callBatchPOST("/$batch", "");

			timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			logger.debug("After Batch Call GET" + timeStamp);

			// creating map for other requests.
			JSONObject sfentityObject = new JSONObject();
			List<BatchSingleResponse> batchResponses = batchRequest.getResponses();
			for (BatchSingleResponse batchResponse : batchResponses) {
				// logger.debug("batch Response: " + batchResponse.getStatusCode() +
				// ";"+batchResponse.getBody());

				JSONObject batchObject = new JSONObject(batchResponse.getBody());
				if (batchObject.getJSONObject("d").getJSONArray("results").length() != 0) {
					batchObject = batchObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
					String batchResponseType = batchObject.getJSONObject("__metadata").getString("type");
					String enityKey = batchResponseType.split("\\.")[1];
					// logger.debug("enityKey" + enityKey);
					entityResponseMap.put(enityKey, batchResponse.getBody());
					if (enityKey.equalsIgnoreCase("EmpJob")) {
						batchObject.put("startDate", map.get("startDate"));
						if (batchObject.getString("countryOfCompany") != null) {
							SFConstants employeeClassConst = sfConstantsService
									.findById("employeeClassId_" + batchObject.getString("countryOfCompany"));
							batchObject.put("employeeClass", employeeClassConst.getValue());
						}
					}
					sfentityObject.put(enityKey, batchObject);
				}
			}
			// creating entry for the confirm status flags update
			String personId = sfentityObject.getJSONObject("PerPerson").getString("perPersonUuid");
			String company = sfentityObject.getJSONObject("EmpJob").getString("company");
			String department = sfentityObject.getJSONObject("EmpJob").getString("department");
			String position = sfentityObject.getJSONObject("EmpJob").getString("position");
			ConfirmStatus confirmStatusTemp = null;
			confirmStatusTemp = confirmStatusService.findById(personId);
			if (confirmStatusTemp == null) {
				confirmStatusTemp = new ConfirmStatus();
				confirmStatusTemp.setId(personId);
				confirmStatusTemp.setCompany(company);
				confirmStatusTemp.setDepartment(department);
				confirmStatusTemp.setPosition(position);
				confirmStatusTemp.setStartDate(map.get("startDate"));
				confirmStatusTemp.setUpdatedOn(new Date());
				confirmStatusTemp = confirmStatusService.create(confirmStatusTemp);
				ConfirmStatus confirmStatus = confirmStatusTemp;
				logger.debug("confirmStatus:" + confirmStatus.getId());
				final Thread parentThread = new Thread(new Runnable() {
					@Override
					public void run() {

						try {

							// updating the startDate and employeeClass to confirm the
							// hire
							logger.debug("confirmStatus.getId():" + confirmStatus.getId());
							ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
							logger.debug("ctemp:" + temp);
							temp.setUpdatedOn(new Date());
							temp.setSfEntityFlag("BEGIN");
							confirmStatusService.update(temp);
							timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
							logger.debug("before SF Updates" + timeStamp);

							for (Map.Entry<String, String> entity : entityMap.entrySet()) {

								if (!(entity.getKey().equalsIgnoreCase("PerPerson")
										|| entity.getKey().equalsIgnoreCase("PerEmail")
										|| entity.getKey().equalsIgnoreCase("cust_Additional_Information")
										|| entity.getKey().equalsIgnoreCase("cust_personIdGenerate"))) {

									String getresponseJson = entityResponseMap.get(entity.getKey());
									if (getresponseJson != null) {
										JSONObject getresponseJsonObject = new JSONObject(getresponseJson);
										// logger.debug("getresponseJson"+getresponseJson);
										if (getresponseJsonObject.getJSONObject("d").getJSONArray("results")
												.length() != 0) {
											JSONObject getresultObj = getresponseJsonObject.getJSONObject("d")
													.getJSONArray("results").getJSONObject(0);

											if (entity.getKey().equalsIgnoreCase("PaymentInformationV3")) {
												getresultObj.put("effectiveStartDate", map.get("startDate"));
												JSONObject paymentInfoDetail = getresultObj
														.getJSONObject("toPaymentInformationDetailV3")
														.getJSONArray("results").getJSONObject(0);
												paymentInfoDetail.put("PaymentInformationV3_effectiveStartDate",
														map.get("startDate"));
												getresultObj.put("toPaymentInformationDetailV3", paymentInfoDetail);

											} else if (entity.getKey().equalsIgnoreCase("EmpJob")) {
												getresultObj.put("startDate", map.get("startDate"));
												if (getresultObj.getString("countryOfCompany") != null) {
													SFConstants employeeClassConst = sfConstantsService
															.findById("employeeClassId_"
																	+ getresultObj.getString("countryOfCompany"));
													getresultObj.put("employeeClass", employeeClassConst.getValue());

												}

												// remove countryOfCompany due to un
												// upsertable field
												getresultObj.remove("countryOfCompany");
												getresultObj.remove("jobTitle");
												getresultObj.remove("positionNav");

											} else if (entity.getKey().equalsIgnoreCase("EmpPayCompRecurring")) {
												getresultObj.put("startDate", map.get("startDate"));
												getresultObj.put("notes", "Date updated");
											} else {
												getresultObj.put("startDate", map.get("startDate"));
											}

											String postJsonString = getresultObj.toString();

											HttpResponse updateresponse = destClient.callDestinationPOST("/upsert",
													"?$format=json&purgeType=full", postJsonString);
											// String entityPostResponseJsonString =
											// EntityUtils.toString(updateresponse.getEntity(),
											// "UTF-8");
											if (updateresponse.getStatusLine().getStatusCode() != 200) {
												temp = confirmStatusService.findById(confirmStatus.getId());
												temp.setUpdatedOn(new Date());
												temp.setSfEntityFlag("FAILED");
												temp.setEntityName(entity.getKey());
												confirmStatusService.update(temp);
											}
											// logger.debug(entity.getKey() + "
											// updateresponse" + updateresponse);
										}
									}
								}
							}
							if (!confirmStatus.getSfEntityFlag().equalsIgnoreCase("FAILED")) {
								temp = confirmStatusService.findById(confirmStatus.getId());
								temp.setUpdatedOn(new Date());
								temp.setSfEntityFlag("SUCCESS");
								confirmStatusService.update(temp);
							}
							timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
							logger.debug("After SF Updates" + timeStamp);
							temp = confirmStatusService.findById(confirmStatus.getId());
							temp.setUpdatedOn(new Date());
							temp.setPexUpdateFlag("BEGIN");
							confirmStatusService.update(temp);

							DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

							// updating the SF mdf to the pex

							JSONObject mdfFieldsObject = new JSONObject(
									entityResponseMap.get("cust_Additional_Information"));
							JSONObject mdfFieldsObject2 = new JSONObject(
									entityResponseMap.get("cust_personIdGenerate"));
							if (mdfFieldsObject.getJSONObject("d").getJSONArray("results").length() > 0) {

								mdfFieldsObject = mdfFieldsObject.getJSONObject("d").getJSONArray("results")
										.getJSONObject(0);
								mdfFieldsObject2 = mdfFieldsObject2.getJSONObject("d").getJSONArray("results")
										.getJSONObject(0);

								// logger.debug("mdfFieldsObject" +
								// mdfFieldsObject.toString());

								Iterator<?> mdfFieldKeys = mdfFieldsObject.keys();
								Map<String, ArrayList<String[]>> pexFormMap = new HashMap<String, ArrayList<String[]>>();
								ArrayList<String[]> fieldValues;
								while (mdfFieldKeys.hasNext()) {
									String key = (String) mdfFieldKeys.next();
									if (key.contains("cust_ZZ_MDF2PEX_")) {
										String customField = mdfFieldsObject.getString(key);
										String[] parts = customField.split("\\|\\|");
										if (parts.length == 3) {
											// logger.debug("pexFormMap - key :
											// "+key+",FormId : "+parts[1]+", FieldId:
											// "+parts[2]+", FieldName: "+parts[0]);
											fieldValues = pexFormMap.get(parts[1]);
											if (fieldValues == null) {
												fieldValues = new ArrayList<String[]>();
											}
											fieldValues.add(
													new String[] { parts[2], parts[0], "cust_Additional_Information" });
											// logger.debug("pexFormMap fieldValues:
											// "+fieldValues.get(0));
											pexFormMap.put(parts[1], fieldValues);
										}
									}

								}
								mdfFieldKeys = mdfFieldsObject2.keys();
								while (mdfFieldKeys.hasNext()) {
									String key = (String) mdfFieldKeys.next();
									if (key.contains("cust_ZZ_MDF2PEX_")) {
										String customField = mdfFieldsObject2.getString(key);
										String[] parts = customField.split("\\|\\|");
										if (parts.length == 3) {
											// logger.debug("pexFormMap - key :
											// "+key+",FormId : "+parts[1]+", FieldId:
											// "+parts[2]+", FieldName: "+parts[0]);
											fieldValues = pexFormMap.get(parts[1]);
											if (fieldValues == null) {
												fieldValues = new ArrayList<String[]>();
											}
											fieldValues
													.add(new String[] { parts[2], parts[0], "cust_personIdGenerate" });
											// logger.debug("pexFormMap fieldValues:
											// "+fieldValues.get(0));
											pexFormMap.put(parts[1], fieldValues);
										}
									}

								}

								JSONObject empJobResponseJsonObject = new JSONObject(entityResponseMap.get("EmpJob"));
								empJobResponseJsonObject = empJobResponseJsonObject.getJSONObject("d")
										.getJSONArray("results").getJSONObject(0);
								// logger.debug("empJobResponseJsonObject" +
								// empJobResponseJsonObject.toString());
								PexClient pexClient = new PexClient();
								pexClient.setDestination(pexDestinationName);
								pexClient.setJWTInitalization(loggedInUser,
										empJobResponseJsonObject.getString("company"));
								counter = 0;

								for (String pexFormMapKey : pexFormMap.keySet()) {
									Map<String, String> pexFormJsonRepMap = new HashMap<String, String>();
									pexFormJsonRepMap.put("candidateId", map.get("userId"));
									pexFormJsonRepMap.put("formId", pexFormMapKey);
									pexFormJsonRepMap.put("companyCode", empJobResponseJsonObject.getString("company"));

									JSONArray postFieldsArray = new JSONArray();
									String validFromDate = map.get("startDate");
									Calendar cal = Calendar.getInstance();
									String milliSec = validFromDate.substring(validFromDate.indexOf("(") + 1,
											validFromDate.indexOf(")"));
									long milliSecLong = Long.valueOf(milliSec).longValue();
									cal.setTimeInMillis(milliSecLong);
									validFromDate = formatter.format(cal.getTime());
									validFromDate = validFromDate + "T00:00:00.000Z";
									// valid from and valid to dates.
									JSONObject validFromPostField = new JSONObject();
									validFromPostField.put("fieldId", "valid_from");
									validFromPostField.put("value", validFromDate);
									postFieldsArray.put(validFromPostField);

									JSONObject validToPostField = new JSONObject();
									validToPostField.put("fieldId", "valid_to");
									validToPostField.put("value", "9999-12-31T00:00:00.000Z");
									postFieldsArray.put(validToPostField);

									fieldValues = pexFormMap.get(pexFormMapKey);
									for (String[] values : fieldValues) {
										// logger.debug("post fields: "+values[0] +" :
										// "+values[1]);
										JSONObject postField = new JSONObject();
										postField.put("fieldId", values[0]);
										if (values[2].equalsIgnoreCase("cust_personIdGenerate")) {
											postField
													.put("value",
															String.valueOf(mdfFieldsObject2.get(values[1]))
																	.equalsIgnoreCase("null") ? ""
																			: mdfFieldsObject2.getString(values[1]));
										} else {
											postField
													.put("value",
															String.valueOf(mdfFieldsObject.get(values[1]))
																	.equalsIgnoreCase("null") ? ""
																			: mdfFieldsObject.getString(values[1]));
										}
										postFieldsArray.put(postField);
									}
									pexFormJsonRepMap.put("fieldsArray", postFieldsArray.toString());
									// logger.debug("pexFormJsonRepMap"+pexFormJsonRepMap);
									JSONObject pexFormPostObj = readJSONFile("/JSONFiles/PexForm.json");
									String pexFormPostString = pexFormPostObj.toString();

									for (Map.Entry<String, String> entry : pexFormJsonRepMap.entrySet()) {
										if (!entry.getKey().equalsIgnoreCase("fieldsArray")) {
											pexFormPostString = pexFormPostString.replaceAll("<" + entry.getKey() + ">",
													entry.getValue());
										} else {
											pexFormPostString = pexFormPostString
													.replaceAll("\"<" + entry.getKey() + ">\"", entry.getValue());

										}
									}
									logger.debug("pexFormPostString : " + pexFormPostString);
									final String finalPexFormPostString = pexFormPostString;
									Thread pexThread = new Thread(new Runnable() {
										@Override
										public void run() {

											try {
												timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
														.format(new Date());
												logger.debug("Before PEX Updates" + timeStamp);

												HttpResponse pexPostResponse = pexClient.callDestinationPOST(
														"api/v3/forms/submit", "", finalPexFormPostString);
												String pexPostResponseJsonString = EntityUtils
														.toString(pexPostResponse.getEntity(), "UTF-8");
												try {

													if (pexPostResponse.getStatusLine().getStatusCode() == 200) {
														JSONObject pexResponseObject = new JSONObject(
																pexPostResponseJsonString);
														counter = counter + 1;
														if (counter == pexFormMap.keySet().size()) {
															ConfirmStatus temp = confirmStatusService
																	.findById(confirmStatus.getId());
															temp.setUpdatedOn(new Date());
															temp.setPexUpdateFlag("SUCCESS");
															confirmStatusService.update(temp);
														}
													} else {
														ConfirmStatus temp = confirmStatusService
																.findById(confirmStatus.getId());
														temp.setUpdatedOn(new Date());
														temp.setPexUpdateFlag("FAILED");
														confirmStatusService.update(temp);

													}

												} catch (JSONException ex) {
													ConfirmStatus temp = confirmStatusService
															.findById(confirmStatus.getId());
													temp.setUpdatedOn(new Date());
													temp.setPexUpdateFlag("FAILED");
													confirmStatusService.update(temp);
												}
												logger.error(
														"pexPostResponseJsonString : " + pexPostResponseJsonString);

											} catch (URISyntaxException | IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									});

									pexThread.start();

								}

								timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
								logger.debug("After Pex Updates" + timeStamp);
								// delete the MDF Object
								// timeStamp = new
								// SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new
								// Date());
								// logger.debug("Before MDF DELETE "+ timeStamp);
								// destClient.callDestinationDelete(mdfFieldsObject.getJSONObject("__metadata").getString("uri"),"?$format=json");
								// timeStamp = new
								// SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new
								// Date());
								// logger.debug("After MDF Delete"+ timeStamp);
							}

						} catch (NamingException e) {

							e.printStackTrace();
						} catch (ClientProtocolException e) {

							e.printStackTrace();
						} catch (IOException e) {

							e.printStackTrace();
						} catch (URISyntaxException e) {

							e.printStackTrace();
						}

					}
				});

				parentThread.start();

				return ResponseEntity.ok().body("Success");
			}
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.debug("Error: StartDate is less then 2 days!");
		return new ResponseEntity<>("Error: StartDate is less then 2 days!", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(value = "/DocDownload/{personId}")
	public ResponseEntity<?> downloadDocument(@PathVariable("personId") String personId, HttpServletRequest request)
			throws NamingException, ClientProtocolException, IOException, URISyntaxException, BatchException,
			UnsupportedOperationException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", personId);

		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));

		HttpResponse personResponse = destClient.callDestinationGET("/PerPerson",
				"?$filter=personIdExternal eq '" + personId + "'&$format=json&$select=perPersonUuid");
		String personResponseString = EntityUtils.toString(personResponse.getEntity(), "UTF-8");
		JSONObject personResponseObject = new JSONObject(personResponseString);
		personResponseObject = personResponseObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);

		ConfirmStatus confirmStatus = confirmStatusService.findById(personResponseObject.getString("perPersonUuid"));

		confirmStatus.setUpdatedOn(new Date());
		confirmStatus.setDocGenFlag("BEGIN");
		confirmStatusService.update(confirmStatus);
		try {
			SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
			Date today = new Date();
			String dateString = dateformatter.format(today);

			BatchRequest batchRequest = new BatchRequest();
			batchRequest.configureDestination(destinationName);

			Map<String, String> entityMap = new HashMap<String, String>();
			String loggedInUser = request.getUserPrincipal().getName();
			entityMap.put("User", "?$filter=userId eq '" + loggedInUser + "'&$format=json&$select=defaultLocale");
			entityMap.put("EmpPayCompRecurring", "?$filter=userId eq '" + map.get("userId") + "'&fromDate=" + dateString
					+ "&$format=json&$select=userId,startDate,payComponent,paycompvalue,currencyCode,frequency,notes");
			entityMap.put("EmpCompensation", "?$filter=userId eq '" + map.get("userId") + "'&fromDate=" + dateString
					+ "&$format=json&$select=userId,startDate,payGroup,eventReason");
			entityMap.put("EmpEmployment", "?$filter=personIdExternal eq '" + map.get("userId") + "'&fromDate="
					+ dateString + "&$format=json&$select=userId,startDate,personIdExternal");
			entityMap.put("PaymentInformationV3", "?$format=json&$filter=worker eq '" + map.get("userId")
					+ "'&fromDate=" + dateString
					+ "&$expand=toPaymentInformationDetailV3&$select=effectiveStartDate,worker,toPaymentInformationDetailV3/PaymentInformationV3_effectiveStartDate,toPaymentInformationDetailV3/PaymentInformationV3_worker,toPaymentInformationDetailV3/amount,toPaymentInformationDetailV3/accountNumber,toPaymentInformationDetailV3/bank,toPaymentInformationDetailV3/payType,toPaymentInformationDetailV3/iban,toPaymentInformationDetailV3/purpose,toPaymentInformationDetailV3/routingNumber,toPaymentInformationDetailV3/bankCountry,toPaymentInformationDetailV3/currency,toPaymentInformationDetailV3/businessIdentifierCode,toPaymentInformationDetailV3/paymentMethod");
			entityMap.put("PerPersonal", "?$filter=personIdExternal eq '" + map.get("userId") + "'&fromDate="
					+ dateString
					+ "&$format=json&$select=startDate,personIdExternal,birthName,initials,middleName,maritalStatus,certificateStartDate,title,salutation,since,gender,lastName,firstName,certificateEndDate,preferredName,nationality");
			entityMap.put("PerAddressDEFLT", "?$filter=personIdExternal eq '" + map.get("userId") + "'&fromDate="
					+ dateString
					+ "&$format=json&$expand=countryNav&$select=startDate,personIdExternal,addressType,address1,address2,address3,city,zipCode,country,address5,address4,county,countryNav/territoryName");
			entityMap.put("EmpJob", "?$filter=userId eq '" + map.get("userId") + "'&fromDate=" + dateString
					+ "&$format=json&$expand=positionNav/companyNav,positionNav&$select=positionNav/companyNav/country,jobTitle,startDate,userId,jobCode,employmentType,workscheduleCode,division,standardHours,costCenter,payGrade,department,timeTypeProfileCode,businessUnit,managerId,position,employeeClass,countryOfCompany,location,holidayCalendarCode,company,eventReason,contractEndDate,contractType,positionNav/externalName_localized");
			entityMap.put("PerPerson", "?$filter=personIdExternal  eq '" + map.get("userId") + "'&fromDate="
					+ dateString + "&$format=json&$select=personIdExternal,dateOfBirth,perPersonUuid");
			entityMap.put("PerEmail", "?$filter=personIdExternal eq '" + map.get("userId") + "'&fromDate=" + dateString
					+ "&$format=json&$select=personIdExternal,emailAddress");

			// reading the records and creating batch post body

			for (Map.Entry<String, String> entity : entityMap.entrySet()) {
				batchRequest.createQueryPart("/" + entity.getKey() + entity.getValue(), entity.getKey());
			}

			// call Get Batch with all entities
			batchRequest.callBatchPOST("/$batch", "");

			// creating map for other requests.

			JSONObject docGenerationObject = new JSONObject();

			List<BatchSingleResponse> batchResponses = batchRequest.getResponses();
			for (BatchSingleResponse batchResponse : batchResponses) {
				// logger.debug("batch Response: " + batchResponse.getStatusCode() +
				// ";"+batchResponse.getBody());
				logger.debug("batchResponse string: " + batchResponse.getBody());
				JSONObject batchObject = new JSONObject(batchResponse.getBody());
				if (batchObject.getJSONObject("d").getJSONArray("results").length() != 0) {
					batchObject = batchObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
					String batchResponseType = batchObject.getJSONObject("__metadata").getString("type");
					String enityKey = batchResponseType.split("\\.")[1];

					docGenerationObject.put(enityKey, batchObject);
				}
			}

			HttpResponse response = generateDoc(docGenerationObject.toString());
			String msg = response.getAllHeaders()[0].getValue();
			if (response != null && !msg.equals("NoTemplateFound")) {
				if (response.getStatusLine().getStatusCode() == 200) {
					String docGenerationResponseJsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
					// String stringBody = response.getBody();
					logger.debug("docGenerationResponseJsonString: " + docGenerationResponseJsonString);
					JSONObject docJson = new JSONObject(docGenerationResponseJsonString);
					if (docJson.getString("status").equalsIgnoreCase("SUCCESS")) {
						logger.debug("docJson.document " + docJson.getString("document"));
						byte[] decodedString = Base64
								.decodeBase64(new String(docJson.getString("document")).getBytes("UTF-8"));
						ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
						temp.setUpdatedOn(new Date());
						temp.setDocGenFlag("SUCCESS");
						confirmStatusService.update(temp);
						return ResponseEntity.ok().body(decodedString);
						// logger.debug("bytes " + decodedString);
					} else {
						ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
						temp.setUpdatedOn(new Date());
						temp.setDocGenFlag("FAILED");
						confirmStatusService.update(temp);

					}

				} else {
					ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
					temp.setUpdatedOn(new Date());
					temp.setDocGenFlag("FAILED");
					confirmStatusService.update(temp);
				}

			} else if (msg.equals("NoTemplateFound")) {
				ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
				temp.setUpdatedOn(new Date());
				temp.setDocGenFlag("FAILED");
				confirmStatusService.update(temp);
				return new ResponseEntity<>("NoTemplateFound", HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
				temp.setUpdatedOn(new Date());
				temp.setDocGenFlag("FAILED");
				confirmStatusService.update(temp);
			}
		} catch (Exception e) {
			ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
			temp.setUpdatedOn(new Date());
			temp.setDocGenFlag("FAILED");
			confirmStatusService.update(temp);
			logger.debug("Error at doc generation: " + e.getMessage());
		}
		// logger.debug("confirmStatus.getDocument()" +
		// confirmStatus.getDocument());
		return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping(value = "/ReUpdate/{personId}")
	public ResponseEntity<?> ReUpdate(@PathVariable("personId") String personId, HttpServletRequest request)
			throws NamingException, BatchException, ClientProtocolException, UnsupportedOperationException,
			URISyntaxException, IOException {
		String loggedInUser = request.getUserPrincipal().getName();
		Map<String, String> map = new HashMap<String, String>();
		logger.debug("START REPSOT");
		map.put("userId", personId);

		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String dateString = dateformatter.format(today);

		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(destinationName);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));

		// batch intitialization
		BatchRequest batchRequest = new BatchRequest();
		batchRequest.configureDestination(destinationName);

		Map<String, String> entityMap = new HashMap<String, String>();
		Map<String, String> entityResponseMap = new HashMap<String, String>();

		entityMap.put("EmpPayCompRecurring", "?$filter=userId eq '" + map.get("userId") + "'&fromDate=" + dateString
				+ "&$format=json&$select=userId,startDate,payComponent,paycompvalue,currencyCode,frequency,notes");
		entityMap.put("EmpCompensation", "?$filter=userId eq '" + map.get("userId") + "'&fromDate=" + dateString
				+ "&$format=json&$select=userId,startDate,payGroup,eventReason");
		entityMap.put("EmpEmployment", "?$filter=personIdExternal eq '" + map.get("userId") + "'&fromDate=" + dateString
				+ "&$format=json&$select=userId,startDate,personIdExternal");
		entityMap.put("PaymentInformationV3", "?$format=json&$filter=worker eq '" + map.get("userId") + "'&fromDate="
				+ dateString
				+ "&$expand=toPaymentInformationDetailV3&$select=effectiveStartDate,worker,toPaymentInformationDetailV3/PaymentInformationV3_effectiveStartDate,toPaymentInformationDetailV3/PaymentInformationV3_worker,toPaymentInformationDetailV3/amount,toPaymentInformationDetailV3/accountNumber,toPaymentInformationDetailV3/bank,toPaymentInformationDetailV3/payType,toPaymentInformationDetailV3/iban,toPaymentInformationDetailV3/purpose,toPaymentInformationDetailV3/routingNumber,toPaymentInformationDetailV3/bankCountry,toPaymentInformationDetailV3/currency,toPaymentInformationDetailV3/businessIdentifierCode,toPaymentInformationDetailV3/paymentMethod");
		entityMap.put("PerPersonal", "?$filter=personIdExternal eq '" + map.get("userId") + "'&fromDate=" + dateString
				+ "&$format=json&$select=startDate,personIdExternal,birthName,initials,middleName,customString1,maritalStatus,certificateStartDate,title,namePrefix,salutation,nativePreferredLang,customDate4,since,gender,lastName,nameFormat,firstName,certificateEndDate,preferredName,secondNationality,suffix,formalName,nationality");
		entityMap.put("PerAddressDEFLT", "?$filter=personIdExternal eq '" + map.get("userId") + "'&fromDate="
				+ dateString
				+ "&$format=json&$select=startDate,personIdExternal,addressType,address1,address2,address3,city,zipCode,country,address7,address6,address5,address4,county,address9,address8");
		entityMap.put("EmpJob", "?$filter=userId eq '" + map.get("userId") + "'&fromDate=" + dateString
				+ "&$format=json&$expand=positionNav/companyNav,positionNav&$select=positionNav/companyNav/country,jobTitle,startDate,userId,jobCode,employmentType,workscheduleCode,division,standardHours,costCenter,payGrade,department,timeTypeProfileCode,businessUnit,managerId,position,employeeClass,countryOfCompany,location,holidayCalendarCode,company,eventReason,contractEndDate,contractType,customString1,positionNav/externalName_localized");
		entityMap.put("PerPerson", "?$filter=personIdExternal  eq '" + map.get("userId") + "'&fromDate=" + dateString
				+ "&$format=json&$select=personIdExternal,dateOfBirth,placeOfBirth,perPersonUuid");
		entityMap.put("PerEmail", "?$filter=personIdExternal eq '" + map.get("userId") + "'&fromDate=" + dateString
				+ "&$format=json&$select=personIdExternal,emailAddress");
		entityMap.put("cust_Additional_Information",
				"?$format=json&$filter=externalCode eq '" + map.get("userId") + "'&fromDate=" + dateString);
		entityMap.put("cust_personIdGenerate", "?$format=json&$filter=externalCode eq '" + map.get("userId")
				+ "'&fromDate=" + dateString + "&$select=cust_ZZ_MDF2PEX_FEOR1,cust_FEOR1");

		// reading the records and creating batch post body

		for (Map.Entry<String, String> entity : entityMap.entrySet()) {
			batchRequest.createQueryPart("/" + entity.getKey() + entity.getValue(), entity.getKey());
		}

		timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		logger.debug("Before Batch Call GET REPOST" + timeStamp);
		// call Get Batch with all entities
		batchRequest.callBatchPOST("/$batch", "");

		timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		logger.debug("After Batch Call GET REPOST" + timeStamp);

		List<BatchSingleResponse> batchResponses = batchRequest.getResponses();
		for (BatchSingleResponse batchResponse : batchResponses) {
			logger.debug("batch Response: " + batchResponse.getStatusCode() + ";" + batchResponse.getBody());

			JSONObject batchObject = new JSONObject(batchResponse.getBody());
			if (batchObject.getJSONObject("d").getJSONArray("results").length() != 0) {
				batchObject = batchObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
				String batchResponseType = batchObject.getJSONObject("__metadata").getString("type");
				String enityKey = batchResponseType.split("\\.")[1];
				logger.debug("enityKey" + enityKey);
				entityResponseMap.put(enityKey, batchResponse.getBody());
			}
		}
		JSONObject jObject = new JSONObject(entityResponseMap.get("PerPerson"));
		jObject = jObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
		String pPuid = jObject.getString("perPersonUuid");
		ConfirmStatus confirmStatus = confirmStatusService.findById(pPuid);

		logger.debug("confirm Status REPOST" + confirmStatus.getId());

		map.put("startDate", confirmStatus.getStartDate());

		logger.debug("SF REPOST START");
		// if SF Entities Failed
		if (confirmStatus.getSfEntityFlag().equalsIgnoreCase("FAILED")
				|| confirmStatus.getSfEntityFlag().equalsIgnoreCase("")) {

			try {

				Thread entityThread = new Thread(new Runnable() {
					@Override
					public void run() {

						try {

							// updating the startDate and employeeClass to confirm
							// the hire
							ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
							temp.setUpdatedOn(new Date());
							temp.setSfEntityFlag("BEGIN");
							confirmStatusService.update(temp);
							timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
							logger.debug("before SF Updates" + timeStamp);

							for (Map.Entry<String, String> entity : entityMap.entrySet()) {

								if (!(entity.getKey().equalsIgnoreCase("PerPerson")
										|| entity.getKey().equalsIgnoreCase("PerEmail")
										|| entity.getKey().equalsIgnoreCase("cust_Additional_Information")
										|| entity.getKey().equalsIgnoreCase("cust_personIdGenerate"))) {

									String getresponseJson = entityResponseMap.get(entity.getKey());
									if (getresponseJson != null) {
										JSONObject getresponseJsonObject = new JSONObject(getresponseJson);
										// logger.debug("getresponseJson"+getresponseJson);
										if (getresponseJsonObject.getJSONObject("d").getJSONArray("results")
												.length() != 0) {
											JSONObject getresultObj = getresponseJsonObject.getJSONObject("d")
													.getJSONArray("results").getJSONObject(0);

											if (entity.getKey().equalsIgnoreCase("PaymentInformationV3")) {
												getresultObj.put("effectiveStartDate", map.get("startDate"));
												JSONObject paymentInfoDetail = getresultObj
														.getJSONObject("toPaymentInformationDetailV3")
														.getJSONArray("results").getJSONObject(0);
												paymentInfoDetail.put("PaymentInformationV3_effectiveStartDate",
														map.get("startDate"));
												getresultObj.put("toPaymentInformationDetailV3", paymentInfoDetail);

											} else if (entity.getKey().equalsIgnoreCase("EmpJob")) {
												getresultObj.put("startDate", map.get("startDate"));
												if (getresultObj.getString("countryOfCompany") != null) {
													SFConstants employeeClassConst = sfConstantsService
															.findById("employeeClassId_"
																	+ getresultObj.getString("countryOfCompany"));
													getresultObj.put("employeeClass", employeeClassConst.getValue());

												}

												// remove countryOfCompany due to un
												// upsertable field
												getresultObj.remove("countryOfCompany");
												getresultObj.remove("jobTitle");
												getresultObj.remove("positionNav");

											} else if (entity.getKey().equalsIgnoreCase("EmpPayCompRecurring")) {
												getresultObj.put("startDate", map.get("startDate"));
												getresultObj.put("notes", "Date updated");
											} else {
												getresultObj.put("startDate", map.get("startDate"));
											}

											String postJsonString = getresultObj.toString();

											HttpResponse updateresponse = destClient.callDestinationPOST("/upsert",
													"?$format=json&purgeType=full", postJsonString);
											// String entityPostResponseJsonString =
											// EntityUtils.toString(updateresponse.getEntity(),
											// "UTF-8");
											if (updateresponse.getStatusLine().getStatusCode() != 200) {
												temp = confirmStatusService.findById(confirmStatus.getId());
												temp.setUpdatedOn(new Date());
												temp.setSfEntityFlag("FAILED");
												temp.setEntityName(entity.getKey());
												confirmStatusService.update(temp);
											}
											// logger.debug(entity.getKey() + "
											// updateresponse" + updateresponse);
										}
									}
								}
							}
							if (!confirmStatus.getSfEntityFlag().equalsIgnoreCase("FAILED")) {
								temp = confirmStatusService.findById(confirmStatus.getId());
								temp.setUpdatedOn(new Date());
								temp.setSfEntityFlag("SUCCESS");
								confirmStatusService.update(temp);
							}
							timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
							logger.debug("After SF Updates" + timeStamp);
						}

						catch (MalformedURLException | URISyntaxException e) {
							ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
							temp.setUpdatedOn(new Date());
							temp.setSfEntityFlag("FAILED");
							confirmStatusService.update(temp);
							e.printStackTrace();

						} catch (Exception e) {
							ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
							temp.setUpdatedOn(new Date());
							temp.setSfEntityFlag("FAILED");
							confirmStatusService.update(temp);
						}
					}
				});

				entityThread.start();
			} catch (Exception e) {
				ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
				temp.setUpdatedOn(new Date());
				temp.setSfEntityFlag("FAILED");
				confirmStatusService.update(temp);
			}
		}
		logger.debug("PEX REPOST START");
		if (confirmStatus.getPexUpdateFlag().equalsIgnoreCase("FAILED")
				|| confirmStatus.getPexUpdateFlag().equalsIgnoreCase("")) {

			logger.debug("PEX REPOST START INSIDE");
			ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
			temp.setUpdatedOn(new Date());
			temp.setPexUpdateFlag("BEGIN");
			confirmStatusService.update(temp);
			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

				// updating the SF mdf to the pex

				JSONObject mdfFieldsObject = new JSONObject(entityResponseMap.get("cust_Additional_Information"));
				JSONObject mdfFieldsObject2 = new JSONObject(entityResponseMap.get("cust_personIdGenerate"));
				logger.debug("mdfFieldsObject Repost" + mdfFieldsObject);
				logger.debug("mdfFieldsObject2 Repost" + mdfFieldsObject2);
				if (mdfFieldsObject.getJSONObject("d").getJSONArray("results").length() > 0) {

					mdfFieldsObject = mdfFieldsObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
					mdfFieldsObject2 = mdfFieldsObject2.getJSONObject("d").getJSONArray("results").getJSONObject(0);

					logger.debug("mdfFieldsObject" + mdfFieldsObject.toString());

					Iterator<?> mdfFieldKeys = mdfFieldsObject.keys();
					Map<String, ArrayList<String[]>> pexFormMap = new HashMap<String, ArrayList<String[]>>();
					ArrayList<String[]> fieldValues;
					while (mdfFieldKeys.hasNext()) {
						String key = (String) mdfFieldKeys.next();
						if (key.contains("cust_ZZ_MDF2PEX_")) {
							String customField = mdfFieldsObject.getString(key);
							String[] parts = customField.split("\\|\\|");
							if (parts.length == 3) {
								logger.debug("pexFormMap - key : " + key + ",FormId :" + parts[1] + ", FieldId: "
										+ parts[2] + ", FieldName:" + parts[0]);
								fieldValues = pexFormMap.get(parts[1]);
								if (fieldValues == null) {
									fieldValues = new ArrayList<String[]>();
								}
								fieldValues.add(new String[] { parts[2], parts[0], "cust_Additional_Information" });
								logger.debug("pexFormMap fieldValues:" + fieldValues.get(0));
								pexFormMap.put(parts[1], fieldValues);
							}
						}

					}
					mdfFieldKeys = mdfFieldsObject2.keys();
					while (mdfFieldKeys.hasNext()) {
						String key = (String) mdfFieldKeys.next();
						if (key.contains("cust_ZZ_MDF2PEX_")) {
							String customField = mdfFieldsObject2.getString(key);
							String[] parts = customField.split("\\|\\|");
							if (parts.length == 3) {
								logger.debug("pexFormMap - key : " + key + ",FormId : " + parts[1] + ", FieldId: "
										+ parts[2] + ", FieldName:" + parts[0]);
								fieldValues = pexFormMap.get(parts[1]);
								if (fieldValues == null) {
									fieldValues = new ArrayList<String[]>();
								}
								fieldValues.add(new String[] { parts[2], parts[0], "cust_personIdGenerate" });
								logger.debug("pexFormMap fieldValues:" + fieldValues.get(0));
								pexFormMap.put(parts[1], fieldValues);
							}
						}

					}

					JSONObject empJobResponseJsonObject = new JSONObject(entityResponseMap.get("EmpJob"));
					empJobResponseJsonObject = empJobResponseJsonObject.getJSONObject("d").getJSONArray("results")
							.getJSONObject(0);
					logger.debug("empJobResponseJsonObject" + empJobResponseJsonObject.toString());
					PexClient pexClient = new PexClient();
					pexClient.setDestination(pexDestinationName);
					pexClient.setJWTInitalization(loggedInUser, empJobResponseJsonObject.getString("company"));
					counter = 0;

					for (String pexFormMapKey : pexFormMap.keySet()) {
						Map<String, String> pexFormJsonRepMap = new HashMap<String, String>();
						pexFormJsonRepMap.put("candidateId", map.get("userId"));
						pexFormJsonRepMap.put("formId", pexFormMapKey);
						pexFormJsonRepMap.put("companyCode", empJobResponseJsonObject.getString("company"));

						JSONArray postFieldsArray = new JSONArray();
						String validFromDate = map.get("startDate");
						Calendar cal = Calendar.getInstance();
						String milliSec = validFromDate.substring(validFromDate.indexOf("(") + 1,
								validFromDate.indexOf(")"));
						long milliSecLong = Long.valueOf(milliSec).longValue();
						cal.setTimeInMillis(milliSecLong);
						validFromDate = formatter.format(cal.getTime());
						validFromDate = validFromDate + "T00:00:00.000Z";
						// valid from and valid to dates.
						JSONObject validFromPostField = new JSONObject();
						validFromPostField.put("fieldId", "valid_from");
						validFromPostField.put("value", validFromDate);
						postFieldsArray.put(validFromPostField);

						JSONObject validToPostField = new JSONObject();
						validToPostField.put("fieldId", "valid_to");
						validToPostField.put("value", "9999-12-31T00:00:00.000Z");
						postFieldsArray.put(validToPostField);

						fieldValues = pexFormMap.get(pexFormMapKey);
						for (String[] values : fieldValues) {
							logger.debug("post fields: " + values[0] + " :" + values[1]);
							JSONObject postField = new JSONObject();
							postField.put("fieldId", values[0]);
							if (values[2].equalsIgnoreCase("cust_personIdGenerate")) {
								postField.put("value",
										String.valueOf(mdfFieldsObject2.get(values[1])).equalsIgnoreCase("null") ? ""
												: mdfFieldsObject2.getString(values[1]));
							} else {
								postField.put("value",
										String.valueOf(mdfFieldsObject.get(values[1])).equalsIgnoreCase("null") ? ""
												: mdfFieldsObject.getString(values[1]));
							}
							postFieldsArray.put(postField);
						}
						pexFormJsonRepMap.put("fieldsArray", postFieldsArray.toString());
						logger.debug("pexFormJsonRepMap" + pexFormJsonRepMap);
						JSONObject pexFormPostObj = readJSONFile("/JSONFiles/PexForm.json");
						String pexFormPostString = pexFormPostObj.toString();

						for (Map.Entry<String, String> entry : pexFormJsonRepMap.entrySet()) {
							if (!entry.getKey().equalsIgnoreCase("fieldsArray")) {
								pexFormPostString = pexFormPostString.replaceAll("<" + entry.getKey() + ">",
										entry.getValue());
							} else {
								pexFormPostString = pexFormPostString.replaceAll("\"<" + entry.getKey() + ">\"",
										entry.getValue());

							}
						}
						logger.debug("pexFormPostString : " + pexFormPostString);
						final String finalPexFormPostString = pexFormPostString;
						Thread pexThread = new Thread(new Runnable() {

							@Override
							public void run() {

								try {
									timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
									logger.debug("Before PEX Updates" + timeStamp);

									HttpResponse pexPostResponse = pexClient.callDestinationPOST("api/v3/forms/submit",
											"", finalPexFormPostString);
									String pexPostResponseJsonString = EntityUtils.toString(pexPostResponse.getEntity(),
											"UTF-8");
									logger.debug("Before Try pexPostResponseJsonString : " + pexPostResponseJsonString);
									try {

										if (pexPostResponse.getStatusLine().getStatusCode() == 200) {
											JSONObject pexResponseObject = new JSONObject(pexPostResponseJsonString);
											counter = counter + 1;
											if (counter == pexFormMap.keySet().size()) {
												ConfirmStatus temp = confirmStatusService
														.findById(confirmStatus.getId());
												temp.setUpdatedOn(new Date());
												temp.setPexUpdateFlag("SUCCESS");
												confirmStatusService.update(temp);
											}
										} else {
											ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
											temp.setUpdatedOn(new Date());
											temp.setPexUpdateFlag("FAILED");
											confirmStatusService.update(temp);

										}

									} catch (JSONException ex) {
										ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
										temp.setUpdatedOn(new Date());
										temp.setPexUpdateFlag("FAILED");
										confirmStatusService.update(temp);
										logger.debug("FAILED PEX REPOST" + ex.getMessage());
									}
									logger.debug("pexPostResponseJsonString : " + pexPostResponseJsonString);

								} catch (URISyntaxException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
									temp.setUpdatedOn(new Date());
									temp.setPexUpdateFlag("FAILED");
									confirmStatusService.update(temp);
									logger.debug("FAILED PEX REPOST" + e.getMessage());
								} catch (Exception e) {
									ConfirmStatus temp = confirmStatusService.findById(confirmStatus.getId());
									temp.setUpdatedOn(new Date());
									temp.setPexUpdateFlag("FAILED");
									confirmStatusService.update(temp);
									logger.debug("FAILED PEX REPOST" + e.getMessage());
								}
							}
						});

						pexThread.start();

					}
				}
			}

			catch (Exception e) {
				temp = confirmStatusService.findById(confirmStatus.getId());
				temp.setUpdatedOn(new Date());
				temp.setPexUpdateFlag("FAILED");
				confirmStatusService.update(temp);
				logger.debug("FAILED PEX REPOST" + e.getMessage());
			}

		}
		return ResponseEntity.ok().body("SUCCESS");
	}

	public JSONObject readJSONFile(String FilePath) throws IOException {
		JSONObject jsonObject = null;
		// read the json file from resource folder
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(FilePath);
		if (inputStream != null) {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null) {
				responseStrBuilder.append(inputStr);

			}
			jsonObject = new JSONObject(responseStrBuilder.toString());
		}
		return jsonObject;
	}

	public HttpResponse generateDoc(String reqString)
			throws NamingException, IOException, URISyntaxException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		logger.debug("Doc Genetration: reqString" + reqString);

		JSONObject reqObject = new JSONObject(reqString);

		JSONObject reqBodyObj = new JSONObject();

		// choosing the template name for the file generation
		String company = reqObject.getJSONObject("EmpJob").getString("company");
		String country = reqObject.getJSONObject("EmpJob").getJSONObject("positionNav").getJSONObject("companyNav")
				.getString("country");
		List<ContractCriteria> contractCriteriaList = contractCriteriaService.findByCountryCompany(country, company);
		logger.debug("GenerateDoc country:" + country);
		Collections.sort(contractCriteriaList);
		String templateID = country.toUpperCase() + "|" + company.toUpperCase() + "|" + "CONFIRM";
		logger.debug("templateID: " + templateID);
		logger.debug("contractCriteriaList: " + contractCriteriaList.toString());
		for (ContractCriteria contractCriteria : contractCriteriaList) {

			String criteriaValue;
			String entityName = contractCriteria.getEntityName();
			String field = contractCriteria.getField();
			if (entityName.equalsIgnoreCase("Custom")) {
				String para = field.substring(field.indexOf("(") + 1, field.indexOf(")"));
				Method m = this.getClass().getDeclaredMethod(field.replace("(" + para + ")", ""), String.class);
				Object rValue = m.invoke(this, reqObject.getJSONObject(para).toString());
				criteriaValue = (String) rValue;
			} else {
				criteriaValue = reqObject.getJSONObject(entityName).getString(field);
			}
			criteriaValue.toUpperCase();
			templateID = templateID + "|";
			templateID = templateID + criteriaValue;

			logger.debug("templateID " + templateID);
		}
		logger.debug("templateID: " + templateID);
		Contract contract = contractService.findById(templateID);
		logger.debug("contract: " + contract);
		if (contract != null) {
			logger.debug("contract.getTemplate()" + contract.getTemplate());
			reqBodyObj.put("TemplateName", contract.getTemplate());
		} else {
			logger.debug("Doc Genetration: gotRequest");
			HttpResponse httpResponse = new BasicHttpResponse(null, counter, "NoTemplateFound");
			httpResponse.setHeader("msg", "NoTemplateFound");
			return httpResponse;
			// reqBodyObj.put("TemplateName", "AmRest Kávézó Kft_40H");
		}

		// reqBodyObj.put("CompanyCode", company);
		reqBodyObj.put("CompanyCode", "");
		reqBodyObj.put("Gcc", "AMR");
		reqBodyObj.put("HrisId", reqObject.getJSONObject("PerPerson").getString("personIdExternal"));
		reqBodyObj.put("OutputType", "pdf");
		reqBodyObj.put("Email", reqObject.getJSONObject("PerEmail").getString("emailAddress"));
		reqBodyObj.put("OutputFileName", "Contract.pdf");
		reqBodyObj.put("FileType", "PERSONAL");

		JSONArray parameters = getReqBodyObj(reqBodyObj, reqObject);
		reqBodyObj.put("parameters", parameters);
		timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

		logger.debug("before Call doc generation" + reqBodyObj.toString());
		logger.debug("Timestamp" + timeStamp);

		DestinationClient docDestination = new DestinationClient();
		docDestination.setDestName(docdestinationName);
		docDestination.setHeaderProvider();
		docDestination.setConfiguration();
		docDestination.setDestConfiguration();
		docDestination.setHeaders(docDestination.getDestProperty("Authentication"));

		logger.debug("reqBodyObj" + reqBodyObj.toString());
		HttpResponse docResponse = docDestination.callDestinationPOST("", "", reqBodyObj.toString());
		timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		logger.debug("After doc generation" + timeStamp);

		return docResponse;

	}

	// Get Document POST Body
	private JSONArray getReqBodyObj(JSONObject reqBodyObj, JSONObject reqObject) {
		JSONArray parameters = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String fulltimeOrPartimeEN = "";
		String fulltimeOrPartimeHU = "";
		if (reqObject.getJSONObject("EmpJob").getInt("standardHours") >= 40) {
			fulltimeOrPartimeEN = "full-time";
			fulltimeOrPartimeHU = "teljes Munkaidöben";
		} else {
			fulltimeOrPartimeEN = "part-time";
			fulltimeOrPartimeHU = "Részmunkaidöben";
		}
		parameters.put(new JSONObject().put("Key", "EN_CS_PERSONALINFO_LAST_NAME").put("Value",
				reqObject.getJSONObject("PerPersonal").getString("lastName")));
		parameters.put(new JSONObject().put("Key", "EN_CS_PERSONALINFO_FIRST_NAME").put("Value",
				reqObject.getJSONObject("PerPersonal").getString("firstName")));

		parameters.put(new JSONObject().put("Key", "EN_CS_CUST_ADDITIONAL_INFORMATION_CUST_MUNAM").put("Value",
				reqObject.has("cust_Additional_Information")
						? String.valueOf(reqObject.getJSONObject("cust_Additional_Information").get("cust_MUNAM"))
								.equalsIgnoreCase("null") ? ""
										: reqObject.getJSONObject("cust_Additional_Information").getString("cust_MUNAM")
						: ""));

		parameters.put(new JSONObject().put("Key", "EN_CS_CUST_ADDITIONAL_INFORMATION_CUST_STRNR").put("Value",
				reqObject.has("cust_Additional_Information")
						? String.valueOf(reqObject.getJSONObject("cust_Additional_Information").get("cust_MUVOR"))
								.equalsIgnoreCase("null") ? ""
										: reqObject.getJSONObject("cust_Additional_Information").getString("cust_STRNR")
						: ""));
		String sDateString = reqObject.getJSONObject("EmpJob").getString("startDate");
		// sDateString = sDateString.substring(sDateString.indexOf("(") + 1,
		// sDateString.indexOf(")"));
		// Date sDate = new Date(Long.parseLong(sDateString));
		logger.debug("EN_CS_EMPLOYMENTDETAILS_HIRE_DATE" + sDateString);
		parameters.put(new JSONObject().put("Key", "EN_CS_EMPLOYMENTDETAILS_HIRE_DATE").put("Value",
				formatDate(sDateString, Locale.US, false)));
		logger.debug("reqObject" + reqObject.getJSONObject("EmpJob").toString());

		parameters.put(new JSONObject().put("Key", "EN_CS_PAYCOMPONENTRECURRING_P02HU_0010_PAYCOMPVALUE").put("Value",
				reqObject.has("EmpPayCompRecurring")
						? String.valueOf(reqObject.getJSONObject("EmpPayCompRecurring").get("paycompvalue"))
								.equalsIgnoreCase("null") ? ""
										: reqObject.getJSONObject("EmpPayCompRecurring").getString("paycompvalue")
						: ""));

//		JSONArray propertiesADDRESS8 = new JSONArray(
//				"[\"PerAddressDEFLT/address1\",\"PerAddressDEFLT/address6\",\"PerAddressDEFLT/address5\",\"PerAddressDEFLT/address4\",\"PerAddressDEFLT/address3\",\"PerAddressDEFLT/address2\",\"PerAddressDEFLT/address7\"]");
		// JSONArray propertiesADDRESS8 = new
		// JSONArray("[\"PerAddressDEFLT/zipCode\",\"PerAddressDEFLT/city\"]");
		JSONArray propertiesADDRESS8 = new JSONArray(
				"[\"PerAddressDEFLT/zipCode~ \",\"PerAddressDEFLT/city~, \",\"PerAddressDEFLT/address8~ \",\"PerAddressDEFLT{/address9Nav{/picklistLabels[/results?locale=User.defaultLocale:label~ \",\"PerAddressDEFLT/address2~. \",\"PerAddressDEFLT/address3~\"]");
		/*
		 * JSONArray propertiesADDRESS2 = new JSONArray("[\"PerAddressDEFLT/address8\""
		 * +
		 * ",\"PerAddressDEFLT{/address9Nav{/picklistLabels[/results?locale=User.defaultLocale:label\""
		 * +
		 * ",\"PerAddressDEFLT/city\",\"PerAddressDEFLT/county\",\"PerAddressDEFLT/zipCode\","
		 * + "\"PerAddressDEFLT{" + "/countryNav/territoryName\"]");
		 */
		JSONArray propertiesADDRESS2 = new JSONArray("[\"PerAddressDEFLT/address4~, \","
				+ "\"PerAddressDEFLT/address5~>\"," + "\"PerAddressDEFLT/address6~ \"]");
		parameters.put(new JSONObject().put("Key", "EN_CS_HUN_HOMEADDRESS_ADDRESS8").put("Value",
				getValuesDynamically(propertiesADDRESS8, reqObject)));
		parameters.put(new JSONObject().put("Key", "EN_CS_HUN_HOMEADDRESS_ADDRESS2").put("Value",
				getValuesDynamically(propertiesADDRESS2, reqObject)));
		parameters.put(new JSONObject().put("Key", "EN_CS_JOBINFO_CONTRACT_END_DATE").put("Value",
				String.valueOf(reqObject.getJSONObject("EmpJob").get("contractEndDate")).equalsIgnoreCase("null") ? ""
						: formatDate(reqObject.getJSONObject("EmpJob").getString("contractEndDate"), Locale.US,
								false)));// Check for contractEndDate
		// this
		parameters.put(new JSONObject().put("Key", "EN_CS_CALC3_FT_PT").put("Value", fulltimeOrPartimeEN));
		parameters.put(new JSONObject().put("Key", "HU_CS_CALC3_FT_PT").put("Value", fulltimeOrPartimeHU));
		parameters.put(new JSONObject().put("Key", "EN_CS_CALC4_DAILY_HOURS").put("Value",
				reqObject.getJSONObject("EmpJob").getInt("standardHours") / 5));
		logger.debug("HU_CS_EMPLOYMENTDETAILS_HIRE_DATE" + sDateString);
		parameters.put(new JSONObject().put("Key", "HU_CS_EMPLOYMENTDETAILS_HIRE_DATE").put("Value",
				formatDate(sDateString, "HUN", true)));
		parameters.put(new JSONObject().put("Key", "HU_CS_JOBINFO_CONTRACT_END_DATE").put("Value",
				String.valueOf(reqObject.getJSONObject("EmpJob").get("contractEndDate")).equalsIgnoreCase("null") ? ""
						: formatDate(reqObject.getJSONObject("EmpJob").getString("contractEndDate"), "HUN", true)));// Check
		parameters.put(new JSONObject().put("Key", "EN_CS_CALC5_LAST_DAY_YEAR_PLUS1").put("Value",

				formatLastYearDay(sDateString, Locale.US, false))); // contractEndDate

		parameters.put(new JSONObject().put("Key", "HU_CS_CALC5_LAST_DAY_YEAR_PLUS1").put("Value",

				formatLastYearDay(sDateString, "HUN", true)));
		parameters.put(new JSONObject().put("Key", "EN_CS_CALC1_QRTR_END_DATE").put("Value",
				calcQuarterDateYear(sDateString, Locale.ENGLISH, false)));
		parameters.put(new JSONObject().put("Key", "HU_CS_CALC1_QRTR_END_DATE").put("Value",
				calcQuarterDateYear(sDateString, "HUN", true)));
		parameters.put(new JSONObject().put("Key", "EN_CS_CUST_ADDITIONAL_INFORMATION_CUST_MUVOR").put("Value",
				reqObject.has("cust_Additional_Information")
						? String.valueOf(reqObject.getJSONObject("cust_Additional_Information").get("cust_MUVOR"))
								.equalsIgnoreCase("null") ? ""
										: reqObject.getJSONObject("cust_Additional_Information").getString("cust_MUVOR")
						: ""));

//		logger.debug("reqObject.getJSONObject(\"EmpJob\").has(\"positionNav\"): "
//				+ reqObject.getJSONObject("EmpJob").has("positionNav"));
//		logger.debug(
//				"reqObject.getJSONObject(\"EmpJob\").getJSONObject(\"positionNav\").has(\"externalName_localized\"): "
//						+ reqObject.getJSONObject("EmpJob").getJSONObject("positionNav").has("externalName_localized"));
//		logger.debug("String.valueOf(reqObject.getJSONObject(\"EmpJob\").getJSONObject(\"positionNav\")\r\n"
//				+ "										.get(\"externalName_localized\")).equalsIgnoreCase(\"null\"): "
//				+ String.valueOf(
//						reqObject.getJSONObject("EmpJob").getJSONObject("positionNav").get("externalName_localized"))
//						.equalsIgnoreCase("null"));
		parameters.put(new JSONObject().put("Key", "EN_CS_JOBINFO_JOB_TITLE").put("Value",
				reqObject.getJSONObject("EmpJob").has("positionNav")
						? reqObject.getJSONObject("EmpJob").getJSONObject("positionNav").has("externalName_localized")
								? String.valueOf(reqObject.getJSONObject("EmpJob").getJSONObject("positionNav")
										.get("externalName_localized")).equalsIgnoreCase("null")
												? ""
												: reqObject.getJSONObject("EmpJob").getJSONObject("positionNav")
														.getString("externalName_localized")
								: ""
						: ""));
		return parameters;
	}

	String identify_hours(String entityString) {
		String returnString;
		JSONObject entityObj = new JSONObject(entityString);
		// logger.debug("entityObj Object calc" + entityObj);
		int hours = entityObj.getInt("standardHours");

		if (hours >= 40) {
			returnString = ">=40";
		} else {
			returnString = "<40";
		}

		return returnString;
	}

	// age calculation function
	String compute_age(String entityString) {
		// logger.debug("Start Calculate Age");
		String returnString;
		JSONObject entityObj = new JSONObject(entityString);
		// logger.debug("entityObj Object calc" + entityObj);
		String dob = entityObj.getString("dateOfBirth");
		// logger.debug("dob calc" + dob);
		String dobms = dob.substring(dob.indexOf("(") + 1, dob.indexOf(")"));
		Date dobDate = new Date(Long.parseLong(dobms));
		Date today = new Date();

		long diffInMillies = Math.abs(today.getTime() - dobDate.getTime());
		// logger.debug("diffInMillies" + diffInMillies);
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		long age = diff / 365;
		// logger.debug("age" + age);

		if (age < 18) {
			returnString = "<18";
		} else {
			returnString = ">=18";
		}
		// logger.debug("End Calculate Age" + returnString);
		return returnString;

	}

	private static String formatDate(String dateToFormat, Object locale, Boolean custom) {
		dateToFormat = dateToFormat.substring(dateToFormat.indexOf("(") + 1, dateToFormat.indexOf(")"));
		if (custom == false) {
			Date date = new Date(Long.parseLong(dateToFormat));
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", (Locale) locale);
			return (sdf.format(date));
		} else {
			switch ((String) locale) {
			case "HUN":
				Date date = new Date(Long.parseLong(dateToFormat));
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				return (cal.get(Calendar.YEAR) + ". " + hunLocale.values()[cal.get(Calendar.MONTH)] + " "
						+ cal.get(Calendar.DAY_OF_MONTH));
			}
		}
		return dateToFormat;
	}

	private String formatLastYearDay(String dateToFormat, Object locale, boolean custom) {
		dateToFormat = dateToFormat.substring(dateToFormat.indexOf("(") + 1, dateToFormat.indexOf(")"));
		Date date = new Date(Long.parseLong(dateToFormat));
		SimpleDateFormat sdf_YYYY = new SimpleDateFormat("yyyy");
		if (custom == false) {
			Date decMonth = new Date(1577786942000L);
			SimpleDateFormat sdf_MMDD = new SimpleDateFormat("MMMM dd,", (Locale) locale);
			return (sdf_MMDD.format(decMonth) + " " + (Integer.parseInt(sdf_YYYY.format(date)) + 1));
		} else {
			switch ((String) locale) {
			case "HUN":
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				return (Integer.parseInt(sdf_YYYY.format(date)) + 1 + ". " + hunLocale.values()[11] + " " + 31);
			}
		}
		return dateToFormat;
	}

	private static String calcQuarterDateYear(String dateToFormat, Object locale, Boolean custom) {
		dateToFormat = dateToFormat.substring(dateToFormat.indexOf("(") + 1, dateToFormat.indexOf(")"));
		Date date = new Date(Long.parseLong(dateToFormat));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int quarter = cal.get(Calendar.MONTH) / 3 + 1;
		if (custom == false) {
			// SimpleDateFormat sdfForMonthNumber = new SimpleDateFormat("M");
			// int quarter = (Integer.parseInt(sdfForMonthNumber.format(date)) / 3 + 1);
			// MMMM dd, yyyy
			SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM", (Locale) locale);
			Date month;
			switch (quarter) {
			case 1:
				month = new Date(Long.parseLong("1552896366000"));
				return (sdfMonth.format(month) + " " + 31 + ", " + cal.get(Calendar.YEAR));
			case 2:
				month = new Date(Long.parseLong("1561871604000"));
				return (sdfMonth.format(month) + " " + 30 + ", " + cal.get(Calendar.YEAR));
			case 3:
				month = new Date(Long.parseLong("1569820404000"));
				return (sdfMonth.format(month) + " " + 30 + ", " + cal.get(Calendar.YEAR));
			case 4:
				month = new Date(Long.parseLong("1577769204000"));
				return (sdfMonth.format(month) + " " + 31 + ", " + cal.get(Calendar.YEAR));
			}
			return (null);
		} else {
			switch ((String) locale) {
			case "HUN":
				switch (quarter) {
				case 1:
					return (cal.get(Calendar.YEAR) + ". " + hunLocale.values()[2] + " " + 31);
				case 2:
					return (cal.get(Calendar.YEAR) + ". " + hunLocale.values()[5] + " " + 30);
				case 3:
					return (cal.get(Calendar.YEAR) + ". " + hunLocale.values()[8] + " " + 30);
				case 4:
					return (cal.get(Calendar.YEAR) + ". " + hunLocale.values()[11] + " " + 31);
				}
			}
			return null;
		}
	}

	private static String getValuesDynamically(JSONArray properties, JSONObject reqObject) {
		String responseString = "";
		String[] parts;
		JSONArray tempJsonArray = new JSONArray();
		JSONObject tempJsonObj = new JSONObject();
		String value = "";
		String currentSaperator = "";
		int prevKey = 0; // 0 for start 1 for array and 2 for obj;
		int nextKey = 0; // 0 for start 1 for array and 2 for obj and 3 for the exactValue;
		for (int i = 0; i < properties.length(); i++) {
			nextKey = 0;
			prevKey = 0;
			nextKey = 0;
			parts = properties.getString(i).split("/");
			for (int j = 0; j < parts.length; j++) {
				switch (nextKey) {
				case 0:
					if (parts[j].indexOf('{') == -1 && parts[j].indexOf('[') == -1) {
						prevKey = 1;
						nextKey = 3;
						tempJsonObj = reqObject.has(parts[j]) ? reqObject.getJSONObject(parts[j]) : null;
						break;
					} else if (parts[j].indexOf('{') != -1) {
						prevKey = 1;
						nextKey = 2;
						parts[j] = parts[j].substring(0, parts[j].length() - 1);
						tempJsonObj = reqObject.has(parts[j]) ? reqObject.getJSONObject(parts[j]) : null;
					} else if (parts[j].indexOf('[') != -1) {
						prevKey = 1;
						nextKey = 1;
						parts[j] = parts[j].substring(0, parts[j].length() - 1);
						tempJsonObj = reqObject.has(parts[j]) ? reqObject.getJSONObject(parts[j]) : null;
					}
					break;
				case 1:
					if (tempJsonObj != null) {
						String searchFor = parts[j].substring(parts[j].indexOf('?') + 1, parts[j].indexOf('='));
						String valueKeyFromEntity = parts[j].substring(parts[j].indexOf('=') + 1,
								parts[j].indexOf(':'));
						String entityName = valueKeyFromEntity.substring(0, valueKeyFromEntity.indexOf('.'));
						valueKeyFromEntity = valueKeyFromEntity.substring(valueKeyFromEntity.indexOf('.') + 1);

						String checkFor = reqObject.has(entityName)
								? String.valueOf(reqObject.get(entityName)).equalsIgnoreCase("null") ? ""
										: reqObject.getJSONObject(entityName).getString(valueKeyFromEntity)
								: "";
						String valueAt = parts[j].substring(parts[j].indexOf(':') + 1);
						tempJsonArray = tempJsonObj.getJSONArray(parts[j].substring(0, parts[j].indexOf('?')));
						for (int tempJsonArrayindex = 0; tempJsonArrayindex < tempJsonArray
								.length(); tempJsonArrayindex++) {
							String key = parts[j].substring(parts[j].indexOf(':') + 1, parts[j].indexOf('~'));
							String saperator = "";
							if (currentSaperator.equalsIgnoreCase("")) {
								currentSaperator = parts[j].substring(parts[j].indexOf('~') + 1);
								saperator = "";
							} else {
								saperator = currentSaperator;
								currentSaperator = parts[j].substring(parts[j].indexOf('~') + 1);
							}
							if (tempJsonArray.getJSONObject(tempJsonArrayindex).has(searchFor)) {
								if (tempJsonArray.getJSONObject(tempJsonArrayindex).getString(searchFor)
										.equalsIgnoreCase(checkFor)) {
									if (tempJsonArray.getJSONObject(tempJsonArrayindex).getString(key).length() > 0) {
										// System.out.println("**" + responseString);
										if (currentSaperator.equalsIgnoreCase("")) {
											currentSaperator = parts[j].substring(parts[j].indexOf('~') + 1);
											if (currentSaperator.equalsIgnoreCase(">")) {
												currentSaperator = "/";
											}
											responseString = responseString + "" + value + currentSaperator;
										} else {
											currentSaperator = parts[j].substring(parts[j].indexOf('~') + 1);
											if (currentSaperator.equalsIgnoreCase(">")) {
												currentSaperator = "/";
											}
											responseString = responseString
													+ tempJsonArray.getJSONObject(tempJsonArrayindex).getString(key)
													+ currentSaperator;
										}

									}
								}
							}
						}
					}
					break;
				case 2:
					if (tempJsonObj != null) {
						if (parts[j].indexOf('{') == -1 && parts[j].indexOf('[') == -1) {
							prevKey = 2;
							nextKey = 3;
							tempJsonObj = tempJsonObj.has(parts[j]) ? tempJsonObj.getJSONObject(parts[j]) : null;
						} else if (parts[j].indexOf('{') != -1) {
							prevKey = 2;
							nextKey = 2;
							parts[j] = parts[j].substring(0, parts[j].length() - 1);
							tempJsonObj = tempJsonObj.has(parts[j]) ? tempJsonObj.getJSONObject(parts[j]) : null;
						} else if (parts[j].indexOf('[') != -1) {
							prevKey = 2;
							nextKey = 1;
							parts[j] = parts[j].substring(0, parts[j].indexOf('['));
							tempJsonObj = tempJsonObj.has(parts[j]) ? tempJsonObj.getJSONObject(parts[j]) : null;
						}
					} else {
						value = "";
					}
					break;
				case 3:
					if (tempJsonObj != null) {
						String key = parts[j].substring(0, parts[j].indexOf('~'));
						value = tempJsonObj.has(key)
								? String.valueOf(tempJsonObj.get(key)).equalsIgnoreCase("null") ? ""
										: tempJsonObj.getString(key)
								: "";
						if (value.length() > 0) {
							// System.out.println("**" + responseString);
							if (currentSaperator.equalsIgnoreCase("")) {
								currentSaperator = parts[j].substring(parts[j].indexOf('~') + 1);
								if (currentSaperator.equalsIgnoreCase(">")) {
									currentSaperator = "/";
								}
								responseString = responseString + "" + value + currentSaperator;
							} else {
								currentSaperator = parts[j].substring(parts[j].indexOf('~') + 1);
								if (currentSaperator.equalsIgnoreCase(">")) {
									currentSaperator = "/";
								}
								responseString = responseString + value + currentSaperator;
							}
						}
						value = "";
					}
					break;
				}
			}
		}
		return responseString;
	}

	private int daysBetween(String dateStr) {
		logger.debug("Date to compare:" + dateStr);
		dateStr = dateStr.substring(dateStr.indexOf("(") + 1, dateStr.indexOf(")"));
		Date dateToVerify = new Date(Long.parseLong(dateStr));
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateToVerify);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		dateToVerify = cal.getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date currentDate = calendar.getTime();
		long difference = (dateToVerify.getTime() - currentDate.getTime()) / 86400000;
		logger.debug("difference between dates:" + difference);
		return (int) difference;
	}

	@PostMapping(value = "/SetDBStatuses")
	public ResponseEntity<?> setDBStatuses(@RequestBody String postJson, HttpServletRequest request)
			throws NamingException, ClientProtocolException, IOException, URISyntaxException, BatchException,
			UnsupportedOperationException {
		HttpSession session = request.getSession(false);
		String userID = (String) session.getAttribute("userID");
		try {
			Map<String, String> map = new HashMap<String, String>();
			JSONObject postObject = new JSONObject(postJson);
			Iterator<?> keys = postObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				map.put(key, postObject.getString(key));
			}
			// Get URI details
			DestinationClient destClient = new DestinationClient();
			destClient.setDestName(destinationName);
			destClient.setHeaderProvider();
			destClient.setConfiguration();
			destClient.setDestConfiguration();
			destClient.setHeaders(destClient.getDestProperty("Authentication"));
			// batch intitialization
			BatchRequest batchRequest = new BatchRequest();
			batchRequest.configureDestination(destinationName);
			Map<String, String> entityMap = new HashMap<String, String>();
			// Map<String, String> entityResponseMap = new HashMap<String, String>();
			entityMap.put("PerPerson",
					"?$filter=personIdExternal  eq '" + userID + "'&$format=json&$select=perPersonUuid");
			logger.debug("Formed EntityMap...");
			entityMap.put("EmpJob",
					"?$filter=userId eq '" + userID + "'&$format=json&$select=company,department,position");
			// reading the records and creating batch post body
			for (Map.Entry<String, String> entity : entityMap.entrySet()) {
				batchRequest.createQueryPart("/" + entity.getKey() + entity.getValue(), entity.getKey());
			}
			batchRequest.callBatchPOST("/$batch", "");
			JSONObject sfentityObject = new JSONObject();
			List<BatchSingleResponse> batchResponses = batchRequest.getResponses();
			for (BatchSingleResponse batchResponse : batchResponses) {
				// logger.debug("batch Response: " + batchResponse.getStatusCode() +
				// ";"+batchResponse.getBody());
				JSONObject batchObject = new JSONObject(batchResponse.getBody());
				if (batchObject.getJSONObject("d").getJSONArray("results").length() != 0) {
					batchObject = batchObject.getJSONObject("d").getJSONArray("results").getJSONObject(0);
					String batchResponseType = batchObject.getJSONObject("__metadata").getString("type");
					String enityKey = batchResponseType.split("\\.")[1];
					// logger.debug("enityKey" + enityKey);

					sfentityObject.put(enityKey, batchObject);
				}
			}
			// creating entry for the confirm status flags update
			String personId = sfentityObject.getJSONObject("PerPerson").getString("perPersonUuid");
			String company = sfentityObject.getJSONObject("EmpJob").getString("company");
			String department = sfentityObject.getJSONObject("EmpJob").getString("department");
			String position = sfentityObject.getJSONObject("EmpJob").getString("position");
			ConfirmStatus confirmStatusTemp = null;
			logger.debug("sfentityObject: " + sfentityObject.toString());
			// creating entry for the confirm status flags update
			logger.debug("personId: " + personId);

			confirmStatusTemp = confirmStatusService.findById(personId);
			if (confirmStatusTemp == null) {
				confirmStatusTemp = new ConfirmStatus();
				confirmStatusTemp.setId(personId);
				confirmStatusTemp.setCompany(company);
				confirmStatusTemp.setDepartment(department);
				confirmStatusTemp.setPosition(position);
				confirmStatusTemp.setStartDate(map.get("startDate"));
				confirmStatusTemp.setUpdatedOn(new Date());
				confirmStatusTemp.setPexUpdateFlag("SUCCESS");
				confirmStatusTemp.setSfEntityFlag("SUCCESS");
				confirmStatusTemp = confirmStatusService.create(confirmStatusTemp);
				ConfirmStatus confirmStatus = confirmStatusTemp;
				logger.debug("confirmStatus OK Confirm ID:" + confirmStatus.getId());
			}
			return ResponseEntity.ok().body("SUCCESS");
		} catch (Exception e) {
			logger.debug("{\"message\":\"Error\",\"msg\":\"" + e.getMessage() + "\"}:");
			return ResponseEntity.ok().body("ERROR");
		}
	}

	@PostMapping(value = "/contractgeneration")
	public ResponseEntity<?> contractGeneration(@RequestBody String postJson, HttpServletRequest request)
			throws NamingException, ClientProtocolException, IOException, URISyntaxException, BatchException,
			UnsupportedOperationException {
		logger.debug("contractgeneration Contract Generation Request Got" + postJson);
		try {
			DestinationClient docDestination = new DestinationClient();
			docDestination.setDestName(pocDocDestinationName);
			docDestination.setHeaderProvider();
			docDestination.setConfiguration();
			docDestination.setDestConfiguration();
			docDestination.setHeaders(docDestination.getDestProperty("Authentication"));
			logger.debug("contractgeneration");
			HttpResponse response = docDestination.callDestinationPOST("", "", postJson);
			timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			logger.debug("After doc generation" + timeStamp);
			if (response.getStatusLine().getStatusCode() == 200) {
				String docGenerationResponseJsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
				// String stringBody = response.getBody();
				logger.debug("docGenerationResponseJsonString: " + docGenerationResponseJsonString);
				JSONObject docJson = new JSONObject(docGenerationResponseJsonString);
				if (docJson.getString("status").equalsIgnoreCase("SUCCESS")) {
					logger.debug("docJson.document " + docJson.getString("document"));
					byte[] decodedString = Base64
							.decodeBase64(new String(docJson.getString("document")).getBytes("UTF-8"));
					File file = new File("contract1.pdf");
					FileUtils.writeByteArrayToFile(file, decodedString);

					List<File> fileList = new ArrayList<>();
					fileList.add(file);
					CreateZip oCreateZip = new CreateZip();
					ZipOutputStream zipFile = oCreateZip.generateZip(fileList);

					return ResponseEntity.ok().body(zipFile);
					// logger.debug("bytes " + decodedString);
				} else {
					logger.debug("contractgeneration In else" + response.getStatusLine().getStatusCode());
				}
			} else {
				logger.debug("contractgeneration In else 2:" + response.getStatusLine().getStatusCode());
			}

			return ResponseEntity.ok().body("Error");

		} catch (Exception e) {
			logger.debug("{\"message\":\"Error\",\"msg\":\"" + e.getMessage() + "\"}:");
			return ResponseEntity.ok().body("ERROR");
		}
	}
}
