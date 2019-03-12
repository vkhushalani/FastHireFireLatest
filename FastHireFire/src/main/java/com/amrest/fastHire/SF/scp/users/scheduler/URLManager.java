package com.amrest.fastHire.SF.scp.users.scheduler;

import java.text.MessageFormat;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amrest.fastHire.utilities.CommonFunctions;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

public class URLManager {

	public static DestinationConfiguration dConfiguration;

	private ArrayList<String> paramList;
	private String controllerClass;
	private String configName;
	private String data;

	private static final String PATH_SUFFIX = "/";
	private static final String SCP_PROPERTY_ACCOUNTNAME = "accountName";
	private static final String SCP_PROPERTY_ROLENAME = "roleName";

	private final String encoding = "UTF-8";

	@SuppressWarnings("unused")
	private JSONObject jsonObject;

	private static final Logger logger = LoggerFactory.getLogger(URLManager.class);

	public URLManager(JSONObject object, String className, String config) {
		this.jsonObject = object;
		this.controllerClass = className;
		this.configName = config;
	}

	public URLManager(ArrayList<String> list, String className, String config) {
		this.paramList = list;
		this.controllerClass = className;
		this.configName = config;
	}

	public URLManager(String className, String config, String data) {
		this.controllerClass = className;
		this.data = data;
		this.configName = config;
	}

	/*
	 * Form the url to be called in the servlet class.
	 * 
	 * @return String
	 */
	public String formURLToCall() {
		String dUrl = destinationURL();

		switch (controllerClass) {
		case "Company":
			dUrl = dUrl
					+ "cust_TK_GT_EC_hiring_mgr?$format=json&$select=externalCode,technicalRole,cust_long1,cust_companyName,cust_companyCode&$filter=technicalRole%20eq%20'"
					+ paramList.get(0) + "'";
			break;

		case "CollectRBPGroups":
			dUrl = dUrl
					+ "cust_TK_GT_EC_hiring_mgr?$select=externalCode,technicalRole&$format=json&$sort=technicalRole";
			break;

		case "DynamicGroup":
			dUrl = dUrl + "DynamicGroup?$format=json&$filter=groupName eq '" + paramList.get(0)
					+ "' and groupType eq 'permission'&$select=groupID,groupName";
			break;

		case "CompanyByGroup":
			dUrl = dUrl + "getDynamicGroupsByUser?userId='" + paramList.get(0)
					+ "'&groupSubType='permission'&$format=json";
			break;

		case "CompanyByAmi":
			dUrl = dUrl + "cust_TK_GT_EC_hiring_mgr?$format=json&$filter=cust_long1%20eq%20'" + paramList.get(0) + "'";
			break;

		case "UniqueId":
			dUrl = dUrl + "FOBusinessUnit?$format=json&$filter=externalCode%20like%20'" + paramList.get(0) + "'";
			break;

		case "Language":
			dUrl = dUrl + "User('" + paramList.get(0) + "')?$select=userId,defaultLocale&$format=json";
			break;

		case "Hire":
			dUrl = dUrl + "cust_TK_GT_EC_idSync?$format=json";
			break;

		case "CollectGroups":
			dUrl = dUrl + "cust_TK_GT_EC_hiring_mgr?$format=json&$filter=cust_long1%20eq%20'" + paramList.get(0) + "'";
			break;

		case "CollectUsersByGroup":
			dUrl = dUrl + "cust_TK_GT_EC_hiring_mgr?$format=json&$filter=cust_long1%20eq%20'" + paramList.get(0) + "'";
			break;

		case "CollectUsers":
			dUrl = dUrl + "getUsersByDynamicGroup?groupId=" + paramList.get(0) + "&activeOnly=true&$format=json";
			break;

		default:
			break;
		}
		return dUrl;
	}

	private String destinationURL() {
		String dUrl = null;
		try {
			Context context = new InitialContext();
			ConnectivityConfiguration configuration = (ConnectivityConfiguration) context
					.lookup("java:comp/env/connectivityConfiguration");

			// Extract Values from properties file
//			LinkedHashMap<Object, Object> hashMap = new ExtractProperties().extractProps();
//			String config = configName;

			// Access the url
			dConfiguration = configuration.getConfiguration(configName);
			dUrl = dConfiguration.getProperty("URL");

			if (!dUrl.endsWith(PATH_SUFFIX)) {
				dUrl += PATH_SUFFIX;
			}

			return dUrl;
		} catch (NamingException e) {
			logger.error(MessageFormat.format("Error description for destinationURL {0}{1} ", e, CommonFunctions.CRLF));
		}
		return dUrl;
	}
}
