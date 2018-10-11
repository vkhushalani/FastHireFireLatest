package com.amrest.fastHire.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * Utilities class <h1>URLMaker</h1>
 * 
 * @author : Aseem Wangoo
 * @version : 1.0
 */

public class URLMaker {

	private String className;
	private Map<String, String> queryParams;
	private ArrayList<String> list;
	public String urlToMake = "";

	public URLMaker(ArrayList<String> list, String className) {
		this.list = list;
		this.className = className;
		this.queryParams = new HashMap<>();
		makeURL();
	}

	public URLMaker(String className) {
		this.className = className;
		this.list = new ArrayList<>();
		this.queryParams = new HashMap<>();
		makeURL();
	}

	public URLMaker(Map<String, String> queryParams, String className) {
		this.queryParams = queryParams;
		this.list = new ArrayList<>();
		this.className = className;
		makeURL();
	}

	private String makeURL() {
		switch (className) {

		case "VacantPositions":
			urlToMake = urlToMake + "Position?%24filter=" + getQueryParams("and")
					+ "&%24format=json&%24expand=employeeClassNav&" + "%24select=vacant"
					+ "%2CexternalName_en_US%2CexternalName_en_GB%2CexternalName_hu_HU"
					+ "%2CexternalName_de_DE%2CexternalName_localized%2CexternalName_defaultValue"
					+ "%2CexternalName_fr_FR%2CpayGrade%2CtargetFTE%2CjobCode%2CjobTitle%2Ccompany"
					+ "%2CbusinessUnit%2Clocation%2CcostCenter%2CregularTemporary%2Ccode%2CemployeeClass"
					+ "%2Cdepartment%2Cdivision%2CemployeeClassNav%2Flabel_hu_HU%2CemployeeClassNav"
					+ "%2Flabel_fr_FR%2CemployeeClassNav%2Flabel_defaultValue%2CemployeeClassNav"
					+ "%2Flabel_en_GB%2CemployeeClassNav%2Flabel_en_US%2CemployeeClassNav%2Flabel_de_DE"
					+ "%2CemployeeClassNav%2Flabel_pl_PL%2CemployeeClassNav%2Flabel_nl_NL"
					+ "%2CemployeeClassNav%2Flabel_localized%2CcreatedDate";
			break;

		case "OngoingHiring":
			String empstatus = queryParams.remove("emplStatusNav/id");
			urlToMake = urlToMake + "EmpJob?%24format=json&%24filter=" + getQueryParams("and")
					+ "and%20emplStatusNav%2Fid%20ne%20'" + empstatus
					+ "'&%24expand=positionNav%2CuserNav%2CpositionNav%2FemployeeClassNav%2CpositionNav%2F"
					+ "companyNav%2CuserNav%2FempInfo&%24select=userId%2CstartDate%2CendDate%2Cposition%2CcreatedDateTime%2CcreatedOn%2C"
					+ "timeTypeProfileCode%2CworkscheduleCode%2CholidayCalendarCode%2CemploymentType%2C"
					+ "employeeClass%2CpositionNav%2FexternalName_en_US%2CpositionNav%2FexternalName_en_GB%2C"
					+ "positionNav%2FexternalName_hu_HU%2CpositionNav%2FexternalName_de_DE%2CpositionNav%2F"
					+ "externalName_localized%2CpositionNav%2FexternalName_defaultValue%2CpositionNav%2F"
					+ "externalName_fr_FR%2CpositionNav%2Fcode%2CpositionNav%2FpayGrade%2CpositionNav%2F"
					+ "targetFTE%2CpositionNav%2FjobCode%2CpositionNav%2FjobTitle%2CpositionNav%2F"
					+ "company%2CpositionNav%2FbusinessUnit%2CpositionNav%2Flocation%2CpositionNav%2F"
					+ "costCenter%2CpositionNav%2FregularTemporary%2CpositionNav%2FemployeeClass%2CpositionNav%2FcreatedDate%2CuserNav%2F"
					+ "userId%2CuserNav%2Fusername%2CuserNav%2FdefaultFullName%2CuserNav%2FfirstName%2CuserNav%2F"
					+ "lastName%2CpositionNav%2FemployeeClassNav%2Flabel_hu_HU%2CpositionNav%2FemployeeClassNav%2F"
					+ "label_fr_FR%2CpositionNav%2FemployeeClassNav%2Flabel_en_GB%2CpositionNav%2FemployeeClassNav%2F"
					+ "label_defaultValue%2CpositionNav%2FemployeeClassNav%2Flabel_en_US%2CpositionNav%2F"
					+ "employeeClassNav%2Flabel_de_DE%2CpositionNav%2FemployeeClassNav%2Flabel_pl_PL%2CpositionNav%2F"
					+ "employeeClassNav%2Flabel_nl_NL%2CpositionNav%2FcompanyNav%2Fcountry%2CuserNav%2Fcustom10";
			break;

		case "PaymentInfoController":
			urlToMake = urlToMake + "PaymentInformationDetailV3?%24format=json&%24top=2&%24filter="
					+ getQueryParams("and");
			break;

		case "PicklistController":
			urlToMake = urlToMake + "PPicklist?%24format=json";
			break;

		case "SalutationPicklistController":
			urlToMake = urlToMake + "Picklist%28%27salutation%27%29/picklistOptions?%24format=json&%24"
					+ "expand=picklistLabels&%24filter=" + getQueryParams("and");
			break;

		case "ISOCountryController":
			urlToMake = urlToMake + "Picklist%28%27ISOCountryList%27%29/picklistOptions?%24format=json"
					+ "&%24expand=picklistLabels&%24filter=" + getQueryParams("and");
			break;

		case "MaritalStatusController":
			urlToMake = urlToMake + "Picklist%28%27ecMaritalStatus%27%29/picklistOptions?"
					+ "%24format=json&%24expand=picklistLabels&%24filter=" + getQueryParams("and");
			break;

		case "Department_FranceController":
			urlToMake = urlToMake + "Picklist%28%27DEPARTMENT_FRA%27%29/picklistOptions?%24format=json"
					+ "&%24expand=picklistLabels&%24filter=" + getQueryParams("and");
			break;

		case "Region_PolandController":
			urlToMake = urlToMake + "Picklist%28%27REGION_POL%27%29/picklistOptions?"
					+ "%24format=json&%24expand=picklistLabels&%24filter=" + getQueryParams("and");
			break;

		case "EmailTypeController":
			urlToMake = urlToMake + "Picklist%28%27ecEmailType%27%29/picklistOptions?%24format=json"
					+ "&%24expand=picklistLabels&%24filter=" + getQueryParams("and");
			break;

		case "PhoneTypeController":
			urlToMake = urlToMake
					+ "Picklist%28%27ecPhoneType%27%29/picklistOptions?%24format=json&%24expand=picklistLabels&%24filter="
					+ getQueryParams("and");
			break;

		case "EmployeeClassController":
			urlToMake = urlToMake
					+ "Picklist%28%27EMPLOYEECLASS%27%29/picklistOptions?%24format=json&%24expand=picklistLabels&%24filter="
					+ getQueryParams("and");
			break;

		case "CurrencyController":
			urlToMake = urlToMake
					+ "Picklist%28%27Currency%27%29/picklistOptions?%24format=json&%24expand=picklistLabels&%24filter="
					+ getQueryParams("and");
			break;

		case "HolidayCalendarController":
			urlToMake = urlToMake + "HolidayCalendar?%24format=json&%24filter=" + getFilterList("country", "or")
					+ "&%24select=externalCode%2Cname_localized%2Cname_defaultValue%2Cname_fr_FR"
					+ "%2Cname_en_US%2Cname_nl_NL%2Cname_de_DE%2Cname_pl_PL%2Cname_hu_HU%2Cname_en_GB";
			break;

		case "TimeTypeController":
			urlToMake = urlToMake + "TimeTypeProfile?%24format=json&%24filter=" + getFilterList("country", "or")
					+ "&%24select=externalCode%2CexternalName_hu_HU%2CexternalName_de_DE"
					+ "%2CexternalName_localized%2CexternalName_defaultValue%2CexternalName_en_US"
					+ "%2CexternalName_pl_PL%2CexternalName_en_GB%2CexternalName_nl_NL" + "%2CexternalName_fr_FR";
			break;

		case "WorkScheduleController":
			urlToMake = urlToMake + "WorkSchedule?%24format=json&%24filter=" + getFilterList("country", "or")
					+ "&%24select=externalCode%2CexternalName_hu_HU%2CexternalName_de_DE"
					+ "%2CexternalName_localized%2CexternalName_defaultValue%2CexternalName_en_US"
					+ "%2CexternalName_pl_PL%2CexternalName_en_GB%2CexternalName_nl_NL" + "%2CexternalName_fr_FR";
			break;

		case "BankController":
			urlToMake = urlToMake + "Bank?%24format=json&%24filter=" + getFilterList("bankCountry", "or");
			break;

		case "FOFrequencyController":
			urlToMake = urlToMake + "FOFrequency?%24format=json";
			break;

		case "PayComponentController":
			urlToMake = urlToMake + "Country?%24format=json&%24filter=" + getQueryParams("and")
					+ "&%24expand=cust_to_wrapper_paycomponent&%24select=cust_to_wrapper_paycomponent"
					+ "%2FexternalCode";
			break;

		case "LoggedUser":
			urlToMake = urlToMake + "EmpJob?%24filter=" + getQueryParams("and") + "&%24format=json"
					+ "&%24expand=positionNav%2CpositionNav%2FcompanyNav&%24select=positionNav"
					+ "%2Fcompany%2CpositionNav%2Fdepartment%2Cposition%2CpositionNav%2FcompanyNav%2Fcountry";
			break;

		case "ParentPosMgrController":
			urlToMake = urlToMake + "Position?%24filter=" + getQueryParams("and")
					+ "&%24format=json&%24expand=parentPosition&%24select=parentPosition%2Fcode";
			break;

		case "ManagerIDController":
			urlToMake = urlToMake + "EmpJob?%24filter=" + getQueryParams("and") + "&%24format=json&%24select=userId";
			break;

		case "Language":
			urlToMake = urlToMake + "User?$filter=" + getQueryParams("and") + "&$format=json&$select=defaultLocale";
			break;

		case "CustPersonIdGen":
			urlToMake = urlToMake + "cust_personIdGenerate?$format=json";
			break;

		case "UserEntity":
			urlToMake = urlToMake + "upsert?$format=json&sendWelcomeMessage=true";
			break;
			
		case "PerPerson":
			urlToMake = urlToMake + "upsert?$format=json";
			break;	

		case "EmpEmployment":
			urlToMake = urlToMake + "upsert?$format=json";
			break;

		case "EmpJob":
			urlToMake = urlToMake + "upsert?$format=json";
			break;

		case "PerPersonal":
			urlToMake = urlToMake + "upsert?$format=json";
			break;

		case "PerEmail":
			urlToMake = urlToMake + "upsert?$format=json";
			break;

		case "EmpCompensation":
			urlToMake = urlToMake + "upsert?$format=json";
			break;

		case "EmpPayCompRecurring":
			urlToMake = urlToMake + "upsert?$format=json";
			break;

		case "PositionVacancy":
			urlToMake = urlToMake + "upsert?$format=json";
			break;

		default:
			break;
		}

		return urlToMake;
	}

	private String getQueryParams(String operator) {
		StringBuilder query = new StringBuilder();
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			query.append(entry.getKey()).append("%20eq%20");
			if (entry.getValue() != null && !("null".equalsIgnoreCase(entry.getValue()))) {
				query.append("'").append(entry.getValue()).append("'");
			} else {
				query.append("null");
			}
			query.append("%20").append(operator).append("%20");
		}
		if (query.length() > 6 && operator == "and") {
			query.delete(query.length() - 6, query.length());
		} else if (query.length() > 5 && operator == "or") {
			query.delete(query.length() - 5, query.length());
		}

		return query.toString();
	}

	private String getFilterList(String operand, String operator) {
		StringBuilder query = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			query.append(operand).append("%20eq%20");
			if (list.get(i) != null && !("null".equalsIgnoreCase(list.get(i)))) {
				query.append("'").append(list.get(i)).append("'");
			} else {
				query.append("null");
			}
			query.append("%20").append(operator).append("%20");
		}
		if (query.length() > 6 && operator == "and") {
			query.delete(query.length() - 6, query.length());
		} else if (query.length() > 5 && operator == "or") {
			query.delete(query.length() - 5, query.length());
		}

		return query.toString();
	}
}
