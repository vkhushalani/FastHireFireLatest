package com.amrest.fastHire.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.account.TenantContext;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

/*
 * Helper class <h1>URLManager</h1>
 * 
 * @author : Aseem Wangoo
 * @version : 1.0
 */

public class URLManager {

	public static DestinationConfiguration dConfiguration;

	private Map<String, String> queryParams;
	private String controllerClass;
	private String configName;
	private ArrayList<String> list;

	private URLMaker urlMaker;

	private static final Logger logger = LoggerFactory.getLogger(URLManager.class);

	@Resource
	public static TenantContext tContext = null;

	public URLManager(ArrayList<String> list, String className, String config) {
		this.list = list;
		this.queryParams = new HashMap<>();
		this.controllerClass = className;
		this.configName = config;
	}

	public URLManager(Map<String, String> queryParams, String className, String config) {
		this.queryParams = queryParams;
		this.list = new ArrayList<>();
		this.controllerClass = className;
		this.configName = config;
	}

	public URLManager(String className, String config) {
		this.queryParams = new HashMap<>();
		this.list = new ArrayList<>();
		this.controllerClass = className;
		this.configName = config;
	}

	/*
	 * Form the url to be called in the servlet class.
	 * 
	 * @return String
	 */
	public String formURLToCall() {
		String dUrl = destinationURL()+"/";

		switch (controllerClass) {
		case "PaymentInfoController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
		case "VacantPositions":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "OngoingHiring":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
		case "PicklistController":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
		case "SalutationPicklistController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
		case "ISOCountryController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
		case "MaritalStatusController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
		case "Department_FranceController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "Region_PolandController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "EmailTypeController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "PhoneTypeController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "EmployeeClassController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "CurrencyController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "HolidayCalendarController":
			urlMaker = new URLMaker(list,controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
			
		case "WorkScheduleController":
			urlMaker = new URLMaker(list,controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
		
		case "TimeTypeController":
			urlMaker = new URLMaker(list,controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
		
		case "BankController":
			urlMaker = new URLMaker(list,controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "FOFrequencyController":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "PayComponentController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "LoggedUser":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "ParentPosMgrController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;

		case "ManagerIDController":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
			
		case "Language":
			urlMaker = new URLMaker(queryParams, controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;	
			
		case "CustPersonIdGen":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
		case "CustPersonIdGenPost":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;	
			
		case "UserEntity":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
			
		case "PerPerson":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;		
			
		case "EmpEmployment":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
			
		case "EmpJob":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;	
			
		case "PerPersonal":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;	
			
		case "PerEmail":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;
			
		case "EmpCompensation":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;	
			
		case "EmpPayCompRecurring":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;	
			
		case "PositionVacancy":
			urlMaker = new URLMaker(controllerClass);
			dUrl = dUrl + urlMaker.urlToMake;
			break;			

		default:
			break;
		}
		return dUrl;
	}

	/*
	 * Get the configuration details from SAP HCP.
	 * 
	 * @return String
	 * 
	 * @exception NamingException
	 */
	private String destinationURL() {
		String dUrl = null;
		try {
			Context context = new InitialContext();
			ConnectivityConfiguration configuration = (ConnectivityConfiguration) context
					.lookup("java:comp/env/connectivityConfiguration");

			// Extract Values from properties file
			LinkedHashMap<Object, Object> hashMap = new ExtractProperties().extractProps();
			String config = (String) hashMap.get(this.configName);

			// Get value of Tenant Context
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");
			tContext = (TenantContext) ctx.lookup("TenantContext");

			// Access the url
			dConfiguration = configuration.getConfiguration(config);

			dUrl = dConfiguration.getProperty("URL");
			return dUrl;
		} catch (NamingException e) {
			logger.error(ConstantManager.lineSeparator + "NamingException: Error description for destinationURL :: ",
					e);
		} catch (Exception f) {
			logger.error(ConstantManager.lineSeparator + "Exception: Error description for destinationURL :: ", f);
		}
		return dUrl;
	}
}
