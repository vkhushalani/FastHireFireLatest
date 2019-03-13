package com.amrest.fastHire.SF.scp.users.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amrest.fastHire.SF.DestinationClient;

public class SCPOperationsForScheduler {
	private static final String sFDestinationName = "configurationnameSF";
	private static final String scpAuthToken = "Scheduler_PlatFormClientAuthToken";
	private static final Logger logger = LoggerFactory.getLogger(SCPOperationsForScheduler.class);

	@SuppressWarnings("unchecked")
	public String getUsers() throws NamingException {
		logger.debug("Inside getUsers function");
		JSONArray sfGroupNameArray = getGroupNames();
		Set<String> uniqueSFGroupsSet = new HashSet<String>();
		JSONArray tempArray = new JSONArray();
		for (int i = 0; i < sfGroupNameArray.size(); i++) {
			String groupName = sfGroupNameArray.get(i).toString();
			if (uniqueSFGroupsSet.contains(groupName)) {
				continue;
			} else {
				uniqueSFGroupsSet.add(groupName);
				tempArray.add(groupName);
			}
		}
		sfGroupNameArray = tempArray;
		logger.debug("sfGroupNameArray without duplicates:" + sfGroupNameArray.toString());
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < sfGroupNameArray.size(); i++) {
			ArrayList<String> paramList = new ArrayList<>();
			paramList.add(0, sfGroupNameArray.get(0).toString());
			URLManager genURL = new URLManager(paramList, "DynamicGroup", sFDestinationName);
			String urlToCall = genURL.formURLToCall();
			logger.debug("urlToCall:" + urlToCall);
			// Get details from server
			HttpConnectionGET httpConnectionGET = new HttpConnectionGET(urlToCall, URLManager.dConfiguration);
			String dynamicGroups = httpConnectionGET.connectToServer();
			logger.debug("dynamicGroups:" + dynamicGroups);
			jsonValues = parseDynamicGroups(jsonValues, dynamicGroups);
		}
		JSONObject obj = new JSONObject();
		JSONObject groupIDObj = new JSONObject();
		obj.put("results", jsonValues);
		groupIDObj.put("d", obj);
		logger.debug("Group ID object: " + groupIDObj.toString());
		String usersObject = parseGroupIDs(groupIDObj.toString());
		logger.debug("Users object: " + usersObject);
		String finalUsers = removeDuplicateUsers(usersObject);
		logger.debug("Users final list: " + finalUsers);
		return finalUsers;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getGroupNames() throws NamingException {
		logger.debug("Inside getGroupNames function");
		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(scpAuthToken);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));
		String sfGroupNameString = destClient.getDestProperty("SF_GROUP_NAMES");
		logger.debug("sfGroupNameString:" + sfGroupNameString);
		String[] sfGroupName = sfGroupNameString.split(",");
		JSONArray sfGroupNameArray = new JSONArray();
		for (int i = 0; i < sfGroupName.length; i++) {
			sfGroupNameArray.add(sfGroupName[i]);
		}
		logger.debug("sfGroupNameArray:" + sfGroupNameArray.toString());
		return sfGroupNameArray;
	}

	private List<JSONObject> parseDynamicGroups(List<JSONObject> jsonValues, String result) {
		logger.debug("inside parseDynamicGroups");
		JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
		jsonObject = (JSONObject) JSONValue.parse(jsonObject.get("d").toString());

		JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonObject.get("results").toString());

		for (int i = 0; i < jsonArray.size(); i++) {
			logger.debug("inside parseDynamicGroups jsonArray.get(i).toString(): " + jsonArray.get(i).toString());
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
