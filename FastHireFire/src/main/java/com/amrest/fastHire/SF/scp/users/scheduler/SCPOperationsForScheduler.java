package com.amrest.fastHire.SF.scp.users.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SCPOperationsForScheduler {
	private static final String sFDestinationName = "configurationnameSF";
	private static final Logger logger = LoggerFactory.getLogger(SCPOperationsForScheduler.class);

	private final String urlLog = "URL : ";
	private final String resultLog = "Body : ";
	private final String groupresultLog = "Group Body : ";

	public String getGroups(HttpServletRequest request) {

		ArrayList<String> paramList = new ArrayList<>();

		URLManager genURL = new URLManager(paramList, "CollectRBPGroups", sFDestinationName);

		String urlToCall = genURL.formURLToCall();
		logger.info(urlLog + urlToCall);

		// Get details from server
		HttpConnectionGET httpConnectionGET = new HttpConnectionGET(urlToCall, URLManager.dConfiguration);
		String result = httpConnectionGET.connectToServer();

		logger.info(groupresultLog + result);

		result = parseGroups(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private String parseGroups(String result) {
		long startTime = System.nanoTime();

		JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
		jsonObject = (JSONObject) JSONValue.parse(jsonObject.get("d").toString());

		JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonObject.get("results").toString());

		Set<String> RBPGroups = new HashSet<String>();
		JSONArray tempArray = new JSONArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject1 = (JSONObject) JSONValue.parse(jsonArray.get(i).toString());
			String groupName = jsonObject1.get("technicalRole").toString();

			if (RBPGroups.contains(groupName)) {
				continue;
			} else {
				RBPGroups.add(groupName);
				tempArray.add(jsonObject1);
			}
		}

		jsonArray = tempArray;

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject1 = (JSONObject) JSONValue.parse(jsonArray.get(i).toString());

			ArrayList<String> paramList = new ArrayList<>();
			paramList.add(0, jsonObject1.get("technicalRole").toString());

			URLManager genURL = new URLManager(paramList, "DynamicGroup", sFDestinationName);

			String urlToCall = genURL.formURLToCall();

			// Get details from server
			HttpConnectionGET httpConnectionGET = new HttpConnectionGET(urlToCall, URLManager.dConfiguration);
			String dynamicGroups = httpConnectionGET.connectToServer();

			jsonValues = parseDynamicGroups(jsonValues, dynamicGroups);

		}

		long endTime = System.nanoTime();
		logger.info("Time used (in second) parseDynamicGroups: " + (endTime - startTime) / 1000000000);

		JSONObject obj = new JSONObject();
		JSONObject groupIDObj = new JSONObject();
		obj.put("results", jsonValues);
		groupIDObj.put("d", obj);

		logger.info("Group ID object: " + groupIDObj.toString());

		String usersObject = parseGroupIDs(groupIDObj.toString());
		logger.info("Users object: " + usersObject);

		String finalUsers = removeDuplicateUsers(usersObject);

		logger.info("Users final list: " + finalUsers);
		return finalUsers;
	}

	private List<JSONObject> parseDynamicGroups(List<JSONObject> jsonValues, String result) {
		JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
		jsonObject = (JSONObject) JSONValue.parse(jsonObject.get("d").toString());

		JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonObject.get("results").toString());

		for (int i = 0; i < jsonArray.size(); i++) {
			jsonValues.add((JSONObject) JSONValue.parse(jsonArray.get(i).toString()));
		}
		return jsonValues;
	}

	@SuppressWarnings("unchecked")
	private String parseGroupIDs(String result) {

		JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
		jsonObject = (JSONObject) JSONValue.parse(jsonObject.get("d").toString());

		JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonObject.get("results").toString());
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject1 = (JSONObject) JSONValue.parse(jsonArray.get(i).toString());

			ArrayList<String> paramList = new ArrayList<>();
			paramList.add(0, jsonObject1.get("groupID").toString() + "L");

			URLManager genURL = new URLManager(paramList, "CollectUsers", sFDestinationName);

			String urlToCall = genURL.formURLToCall();

			// Get details from server
			HttpConnectionGET httpConnectionGET = new HttpConnectionGET(urlToCall, URLManager.dConfiguration);
			String dynamicGroups = httpConnectionGET.connectToServer();

			jsonValues = parseUsers(jsonValues, dynamicGroups);

		}

		JSONObject obj = new JSONObject();
		JSONObject finalObj = new JSONObject();
		obj.put("results", jsonValues);
		finalObj.put("d", obj);
		return finalObj.toString();
	}

	@SuppressWarnings("unchecked")
	private List<JSONObject> parseUsers(List<JSONObject> jsonValues, String result) {
		JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
		JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonObject.get("d").toString());

		JSONObject newObject = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject1 = (JSONObject) JSONValue.parse(jsonArray.get(i).toString());
			newObject = new JSONObject();
			newObject.put("userId", jsonObject1.get("userId").toString().toLowerCase());
			jsonValues.add(newObject);
		}
		return jsonValues;
	}

	private String removeDuplicateUsers(String result) {

		JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
		jsonObject = (JSONObject) JSONValue.parse(jsonObject.get("d").toString());

		JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonObject.get("results").toString());

		Set<String> Users = new HashSet<String>();
		String finalUsers = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject1 = (JSONObject) JSONValue.parse(jsonArray.get(i).toString());
			String userId = jsonObject1.get("userId").toString();

			if (Users.contains(userId)) {
				continue;
			} else {
				Users.add(userId);
				if (finalUsers.equals("")) {
					finalUsers = userId;
				} else {
					finalUsers = finalUsers + ";" + userId;
				}
			}
		}

		return finalUsers;
	}
}
