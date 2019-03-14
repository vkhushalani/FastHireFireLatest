package com.amrest.fastHire.SF.scp.users.scheduler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.amrest.fastHire.SF.DestinationClient;
import com.amrest.fastHire.connections.HttpConnectionDELETE;
import com.amrest.fastHire.connections.HttpConnectionPUT;
import com.sap.cloud.account.Account;
import com.sap.cloud.account.TenantContext;

public class SCPOperationsForScheduler {
	private static final String sFDestinationName = "Scheduler_SF";
	private static final String scpAuthToken = "Scheduler_PlatFormClientAuthToken";
	private static final Logger logger = LoggerFactory.getLogger(SCPOperationsForScheduler.class);

	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getUsers()
			throws NamingException, ClientProtocolException, URISyntaxException, IOException, ParseException {
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
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < sfGroupNameArray.size(); i++) {
			ArrayList<String> paramList = new ArrayList<>();
			paramList.add(0, sfGroupNameArray.get(0).toString());
			URLManager genURL = new URLManager(paramList, "DynamicGroup", sFDestinationName);
			String urlToCall = genURL.formURLToCall();
			// Get details from server
			HttpConnectionGET httpConnectionGET = new HttpConnectionGET(urlToCall, URLManager.dConfiguration);
			String dynamicGroups = httpConnectionGET.connectToServer();
			jsonValues = parseDynamicGroups(jsonValues, dynamicGroups);
		}
		JSONObject obj = new JSONObject();
		JSONObject groupIDObj = new JSONObject();
		obj.put("results", jsonValues);
		groupIDObj.put("d", obj);
		// groupIDObj contains all ids of Group now sending that to groupIDObj in order
		// to get Users
		ResponseEntity<?> operationResponse = performOperation(groupIDObj.toString());
		return operationResponse;
	}

	public void updateUsers(String sfUsers, String group)
			throws ClientProtocolException, NamingException, URISyntaxException, IOException, ParseException {
		String usersArray[] = sfUsers.split(";");
		String token = getoAuthToken();
		if (!token.equals("")) {
			// get users in SCP
			Set<String> scpUsers = getSCPUsers(token, group);

			// collect and post new users
			addNewSCPUsers(usersArray, scpUsers, token, group);
			deleteObsoleteUsers(usersArray, scpUsers, token, group);
		}
	}

	@SuppressWarnings("unchecked")
	private void addNewSCPUsers(String users[], Set<String> scpUsers, String token, String group)
			throws NamingException {
		// compare payload from existing SCP Users - identify new users
		JSONArray newUsers = new JSONArray();
		JSONObject newUser = null;
		for (String user : users) {
			if (scpUsers.contains(user)) {
				continue;
			} else {
				newUser = new JSONObject();
				newUser.put("name", user);
				newUsers.add(newUser);
			}
		}

		JSONObject payload = new JSONObject();
		payload.put("users", newUsers);
		if (newUsers.size() != 0) {
			String cloudHost = System.getenv("HC_HOST");
			Account subscribedAccount = getSubscribedAccount();
			String cloudAccount = subscribedAccount.getId();
			String urlToCall = "https://api." + cloudHost + "/authorization/v1" + "/accounts/" + cloudAccount
					+ "/groups/users?groupName=" + group;

			HttpConnectionPUT httpPut = new HttpConnectionPUT(urlToCall, token, payload);
			httpPut.connectToServerUsingAuth();
		}

	}

	private Set<String> getSCPUsers(String token, String group) throws ParseException, NamingException {
		String cloudHost = System.getenv("HC_HOST");
		Account subscribedAccount = getSubscribedAccount();
		String cloudAccount = subscribedAccount.getId();
		String urlToCall = "https://api." + cloudHost + "/authorization/v1" + "/accounts/" + cloudAccount
				+ "/groups/users?groupName=" + group;
		// Get details from server
		HttpConnectionGET httpConnectionGET = new HttpConnectionGET(urlToCall, token);
		String usersFromSCP = httpConnectionGET.connectToServerUsingAuth();

		Set<String> scpUsers = parseSCPUsers(usersFromSCP);

		return scpUsers;
	}

	private String deleteObsoleteUsers(String[] users, Set<String> scpUsers, String token, String group)
			throws NamingException {
		Set<String> newUsers = new HashSet<>(Arrays.asList(users));
		String deleteUsers = "";
		for (String scpUser : scpUsers) {
			if (newUsers.contains(scpUser)) {
				continue;
			} else {
				if (deleteUsers.equals("")) {
					deleteUsers = scpUser;
				} else {
					deleteUsers = deleteUsers + ";" + scpUser;
				}
			}
		}
		String result = null;
		if (!deleteUsers.equals("")) {
			String cloudHost = System.getenv("HC_HOST");
			Account subscribedAccount = getSubscribedAccount();
			String cloudAccount = subscribedAccount.getId();
			String urlToCall = "https://api." + cloudHost + "/authorization/v1" + "/accounts/" + cloudAccount
					+ "/groups/users?groupName=" + group + "&users=" + deleteUsers;

			HttpConnectionDELETE httpDelete = new HttpConnectionDELETE(urlToCall, token);
			result = httpDelete.connectToServerUsingAuth();
		}
		return result;
	}

	public String getoAuthToken() throws NamingException, ClientProtocolException, URISyntaxException, IOException {
		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(scpAuthToken);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));
		HttpResponse tokenResponse = destClient.callDestinationPOST("", "", "");
		String tokenResponseJsonString = EntityUtils.toString(tokenResponse.getEntity(), "UTF-8");
		org.json.JSONObject tokenResponseObject = new org.json.JSONObject(tokenResponseJsonString);
		return tokenResponseObject.getString("access_token");
	}

	private Account getSubscribedAccount() throws NamingException {
		Context ctx = new InitialContext();
		TenantContext tenantctx = (TenantContext) ctx.lookup("java:comp/env/TenantContext");
		return tenantctx.getTenant().getAccount();
	}

	@SuppressWarnings("unchecked")
	public JSONArray getGroupNames() throws NamingException {
		DestinationClient destClient = new DestinationClient();
		destClient.setDestName(scpAuthToken);
		destClient.setHeaderProvider();
		destClient.setConfiguration();
		destClient.setDestConfiguration();
		destClient.setHeaders(destClient.getDestProperty("Authentication"));
		String sfGroupNameString = destClient.getDestProperty("SF_GROUP_NAMES");
		String[] sfGroupName = sfGroupNameString.split(",");
		JSONArray sfGroupNameArray = new JSONArray();
		for (int i = 0; i < sfGroupName.length; i++) {
			sfGroupNameArray.add(sfGroupName[i]);
		}
		return sfGroupNameArray;
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
	private ResponseEntity<?> performOperation(String result)
			throws ClientProtocolException, NamingException, URISyntaxException, IOException, ParseException {
		JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
		jsonObject = (JSONObject) JSONValue.parse(jsonObject.get("d").toString());

		JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonObject.get("results").toString());

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject1 = (JSONObject) JSONValue.parse(jsonArray.get(i).toString());
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			ArrayList<String> paramList = new ArrayList<>();
			paramList.add(0, jsonObject1.get("groupID").toString() + "L");

			URLManager genURL = new URLManager(paramList, "CollectUsers", sFDestinationName);

			String urlToCall = genURL.formURLToCall();
			// Get details from server
			HttpConnectionGET httpConnectionGET = new HttpConnectionGET(urlToCall, URLManager.dConfiguration);
			String dynamicGroups = httpConnectionGET.connectToServer();
			jsonValues = parseUsers(jsonValues, dynamicGroups);
			JSONObject obj = new JSONObject();
			JSONObject finalObj = new JSONObject();
			obj.put("results", jsonValues);
			finalObj.put("d", obj);
			String usersInSFGroup = removeDuplicateUsers(finalObj.toString());
			updateUsers(usersInSFGroup, jsonObject1.get("groupName").toString());
		}

		return ResponseEntity.ok().body("Success!");
	}

	private Set<String> parseSCPUsers(String result) {

		JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
		JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonObject.get("users").toString());

		Set<String> SCPUsers = new HashSet<String>();

		if (jsonArray.size() != 0) {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject1 = (JSONObject) JSONValue.parse(jsonArray.get(i).toString());
				String user = jsonObject1.get("name").toString();

				if (SCPUsers.contains(user)) {
					continue;
				} else {
					SCPUsers.add(user);
				}
			}
		}

		return SCPUsers;
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
