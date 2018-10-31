package com.amrest.fastHire.utilities;

/*
 * Utilities class <h1>ConstantManager</h1>
 * 
 * @author : Aseem Wangoo
 * @version : 2.0
 */
public class ConstantManager {

	// General application constants
	public static final String basePackage = "com.nga.xtendhr";

	public static final String genAPI = "/api";

	public static final String genError = "/error";

	public static final String paramLog = "Params : ";

	public static final String urlLog = "URL : ";

	public static final String resultLog = "Resp Body : ";

	public static final String respCodeLog = "Response code of ";

	public static final String lineSeparator = System.getProperty("line.separator");

	public static final String getRedirect = "Get redirect to : ";

	public static final String postRedirect = "Post redirect to : ";
	// End general application constants

	// End CA api specific constants

	// SF api specific constants
	public static final String vacantPositions = "/SF/vacantPositions";

	public static final String ongoingHiring = "/SF/ongoingHiring";
	public static final String salutationPicklist = "/SF/salutationPicklist";
	public static final String picklist = "/SF/picklist";
	public static final String paymentInfo = "/SF/paymentInfo";
	public static final String ISOCountry = "/SF/ISOCountry";
	public static final String maritalStatus = "/SF/maritalStatus";
	public static final String departmentFrance = "/SF/departmentFrance";
	public static final String regionPoland = "/SF/regionPoland";
	public static final String emailType = "/SF/emailType";
	public static final String phoneType = "/SF/phoneType";
	public static final String employeeClass = "/SF/employeeClass";
	public static final String currency = "/SF/currency";
	public static final String holidayCalendar = "/SF/holidayCalendar";
	public static final String FOFrequency = "/SF/FOFrequency";
	public static final String payComponent = "/SF/payComponent";
	public static final String userInfo = "/SF/userInfo";

	public static final String localLang = "/SF/Language";

	public static final String timetype = "/SF/timeTypeProfile";
	public static final String workschedule = "/SF/workSchedule";
	public static final String bank = "/SF/Bank";

	public static final String empType = "/SF/employmentType";
	public static final String parentPos = "/SF/parentPosition";
	public static final String managerID = "/SF/managerID";
	
	public static final String custPerIDGen = "/SF/genID";
	public static final String userEntity = "/SF/userEntity";
	public static final String perPerson = "/SF/Person";
	public static final String empEmployment = "/SF/Employment";
	public static final String empJob= "/SF/EmpJob";
	public static final String perPersonal= "/SF/PerPersonal";
	public static final String perEmail= "/SF/PerEmail";
	public static final String empCompensation= "/SF/Compensation";
	public static final String empPayCompRecurring= "/SF/PayComp";
	public static final String posVacancy= "/SF/Vacancy";
	// End SF api specific constants

	// Redirect URL constants
	public static final String redirection = "/entryPoint";
	// End redirect url constants

	// Logged In User api specific constants
	public static final String loggedInUser = "/loggedInUser";

	public static final String nameLog = "Logged in user : ";

	public static final String sessionID = "Session ID : ";

	public static final String nameLogUAT = "Logged in user UAT : ";

	public static final String fNameLog = "First name of logged in user : ";

	public static final String lNameLog = "Last name of logged in user : ";

	public static final String emailLog = "Email of logged in user : ";

	public static final String dNameLog = "Display name of logged in user : ";

	public static final String compLog = "Company Code : ";

	public static final String ssoLog = "SSO enabled : ";

	public static final String compSkillIDLog = "Company Skill ID : ";
	
	public static final Integer  padStartDate  = 15;

	public static String userName;

	public static String email;

	public static String companyCode;

	public static String companySkillID;

	public static String country;

	public static String userID;

	public static String paramStartDateName;

	public static String paramStartDateValue;
	
	public static final String customDateName = "customString11";

	public static String customDateValue;

	public static String paramOrgStartDateValue;

	public static String metaDataUpdatePosVac;
	// End Logged In User api specific constants
}
