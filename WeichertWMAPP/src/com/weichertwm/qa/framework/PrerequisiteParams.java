package com.weichertwm.qa.framework;

import java.io.FileInputStream;
import java.util.Properties;

public class PrerequisiteParams{
	
	public static PrerequisiteParams preparams = null;
	
	public static PrerequisiteParams getInstance() throws Exception{
		if (preparams == null){
			preparams = new PrerequisiteParams();
		}
		return preparams;
	}
	
	public PrerequisiteParams() throws Exception{
		loadPreParams();
	}
	
	public static String DEPARTDELAYCODEFLIGHT = null;
	public static String INCOMINGCANCELFLIGHT = null;
	public static String INCOMINGEDITFLIGHT = null;	
	public static String INCOMINGEDITFLIGHTBAY = null;	
	public static String INCOMINGFLAGFLIGHT = null;
	public static String INCOMINGFLAGFLIGHTCHARTER = null;
	public static String LOGGEDINUSERSHORTNAME = null;
	public static String ONGOINGEDITFLIGHT = null;	
	public static String ONGOINGEDITFLIGHTBELT = null;
	public static String ONGOINGEDITFLIGHTBELTNO = null;
	public static String ONGOINGFLAGFLIGHT = null;
	public static String ONGOINGFLAGFLIGHTCHARTER = null;
	public static String EQUIPMENT_NUMBER = null;
	public static String HOME_SEARCH_FLIGHT = null;
	public static String DAILY_SERVICE_REPORT_DATE = null;
	public static String MONTHLY_FLIGHT_HANDLING_REPORT_DATE = null;
	public static String RH_FORM_REPORT_DATE = null;
	public static String DELETE_GEOFENCE_EQUIPMENT = null;
	public static String CREATE_GEOFENCE_EQUIPMENT = null;
	public static String NON_COMPLIANCE_EQUIPMENT = null;
	public static String ACTIVATE = null;
	public static String INACTIVATE = null;
	public static String ACCESSMGMTACTINACT = null;
	public static String ACCMGMTACTROLE = null;
	public static String ACCMGMTACTCODE = null;
	public static String ACC_MGMT_CREATE_ROLE = null;
	public static String ACC_MGMT_CREATE_ROLE_CODE = null;
	public static String ACC_MGMT_DELETE_ROLE_CODE = null;
	public static String ACC_MGMT_EDIT_ROLE_CODE = null;
	public static String ACC_MGMT_TARGET_EDIT_ROLE_CODE = null;
	public static String ACC_MGMT_TARGET_EDIT_ROLE_NAME = null;
	public static String ACC_MGMT_SCREEN_ROLE = null;
	public static String ACC_MGMT_SCREEN_NAME = null;
	public static String ACC_MGMT_USER_ACTIONS_ROLE = null;
	public static String ACC_MGMT_USER_ACTIONS_SCREEN = null;
	public static String ACC_MGMT_USER_ACTIONS_PERMISSION = null;
	public static String ACC_MGMT_AIRLINE_SCHEDULES_AIRLINE_CODE = null;
	public static String ACC_MGMT_AIRLINE_SCHEDULES_DELETE_AIRLINE_CODE = null;
	public static String ADM_AL_SC_EDIT_AIRLINE_CODE = null;
	public static String ADM_AL_SC_EDIT_TARGET_AIRLINE_CODE = null;
	public static String ADM_AL_SC_EDIT_TARGET_AIRLINE_NAME = null;
	public static String ADM_AL_SC_ADD_AIRLINE_NAME = null;
	public static String ADM_AL_SC_ADD_AIRLINE_CODE = null;
	public static String ADM_PTS_BODY_TYPE = null;
	public static String ADM_PTS_ARR_TYPE = null;
	public static String ADM_PTS_BAY_TYPE = null;
	public static String ADM_PTS_ACT_NAME = null;
	public static String ADM_PTS_DELETE_AIRCRAFT_NAME = null;
	public static String ADM_PTS_EDIT_AC_CATEGORY = null;
	public static String ADM_RESOURCE_INACT_EMP = null;
	public static String ADM_RES_ADD_EMP = null;
	public static String ADM_RES_ADD_EMP_FNAME = null;
	public static String ADM_RES_ADD_EMP_LNAME = null;
	public static String ADM_RES_ADD_EMP_DESIGNATION = null;
	public static String ADM_RES_EDIT_EMP = null;
	public static String ADM_RES_EDIT_FNAME = null;
	public static String ADM_RES_EDIT_LNAME = null;	
	public static String ADM_MGT_ADD_AL_CODE = null;
	public static String ADM_MGT_ADD_ARC_TYPE = null;
	public static String ADM_MGT_ADD_FLIGHT_TYPE = null;
	public static String ADM_MGT_ADD_MINGRD_TIME = null;
	public static String ADM_MGT_ADD_MAXGRD_TIME = null;
	public static String ADM_EQP_PROF_ADD_CATEGORY = null;
	public static String ADM_EQP_PROF_ADD_OWNERTYPE = null;
	public static String ADM_EQP_PROF_ADD_EQPNAME = null;
	public static String ADM_EQP_PROF_ADD_IATACODE = null;
	public static String ADM_EQP_PROF_ADD_EQPNUM = null;
	public static String ADM_EQP_PROF_ADD_ASSTAGNO = null;
	public static String ADM_EQP_PROF_ADD_LMAINDATE = null;
	public static String ADM_EQP_PROF_EDIT_EQPNUM = null;
	public static String ADM_EQP_PROF_EDIT_EQPNAME = null;
	public static String ADM_EQP_PROF_EDIT_TARGET_NUM = null;
	public static String ADM_EQP_PROF_EDIT_TARGET_ASSTAGNO = null;
	public static String ADM_EQP_PROF_EDIT_TARGET_EQPNUM = null;
	public static String ADM_EQP_PROF_ACT_INACT_EQPNUM = null;
	public static String ADM_EQP_PROF_DELETE_EQPNUM = null;
	public static String ROSTER_FILE_PATH = null;
	public static long ELEMENT_FIND_TIME = 0;
	public static String TEST_ENV = null;
	public static String PROD_ENV = null;
	
	public static void loadPreParams() throws Exception{
		
		FileInputStream fis = new FileInputStream("resources/inputs/prerequisite.properties");
		Properties prop = new Properties();
		prop.load(fis);
		
		DEPARTDELAYCODEFLIGHT = prop.getProperty("DepartDelayCodeFlight");
		INCOMINGCANCELFLIGHT = prop.getProperty("IncomingCancelFlight");
		INCOMINGEDITFLIGHT = prop.getProperty("IncomingEditFlight");
		INCOMINGEDITFLIGHTBAY = prop.getProperty("IncomingEditFlightBay");		
		INCOMINGFLAGFLIGHT = prop.getProperty("IncomingFlagFlight");
		INCOMINGFLAGFLIGHTCHARTER = prop.getProperty("IncomingFlagFlightCharter");
		LOGGEDINUSERSHORTNAME = prop.getProperty("LoggedInUserShortName");
		ONGOINGEDITFLIGHT = prop.getProperty("OngoingEditFlight");
		ONGOINGEDITFLIGHTBELT = prop.getProperty("OngoingEditFlightBelt");
		ONGOINGEDITFLIGHTBELTNO = prop.getProperty("OngoingEditFlightBeltNo");
		ONGOINGFLAGFLIGHT = prop.getProperty("OngoingFlagFlight");
		ONGOINGFLAGFLIGHTCHARTER = prop.getProperty("OngingFlagFlightCharter");
		EQUIPMENT_NUMBER = prop.getProperty("Equipment_number");
		HOME_SEARCH_FLIGHT = prop.getProperty("HomeSearchFlight");
		DAILY_SERVICE_REPORT_DATE = prop.getProperty("Daily_service_report_date");
		MONTHLY_FLIGHT_HANDLING_REPORT_DATE = prop.getProperty("Monthly_flight_handling_report_date");
		RH_FORM_REPORT_DATE =  prop.getProperty("RH_form_report_date");
		DELETE_GEOFENCE_EQUIPMENT = prop.getProperty("Delete_geofence_equipment");
		CREATE_GEOFENCE_EQUIPMENT = prop.getProperty("Create_geofence_equipment");
		NON_COMPLIANCE_EQUIPMENT = prop.getProperty("Non_compliance_equipment");
		INACTIVATE = prop.getProperty("Inactivate");
		ACTIVATE = prop.getProperty("Activate");
		ACCESSMGMTACTINACT = prop.getProperty("AccessMgmtActInact");
		ACCMGMTACTROLE = prop.getProperty("AccMgmt_ActRole");
		ACCMGMTACTCODE = prop.getProperty("AccMgmtActCode");
		ACC_MGMT_CREATE_ROLE = prop.getProperty("AccMgmt_create_role");
		ACC_MGMT_CREATE_ROLE_CODE = prop.getProperty("AccMgmt_create_role_code");
		ACC_MGMT_DELETE_ROLE_CODE = prop.getProperty("AccMgmt_delete_role_code");
		ACC_MGMT_EDIT_ROLE_CODE = prop.getProperty("AccMgmt_edit_role_code");
		ACC_MGMT_TARGET_EDIT_ROLE_CODE = prop.getProperty("AccMgmt_target_edit_role_code");
		ACC_MGMT_TARGET_EDIT_ROLE_NAME = prop.getProperty("AccMgmt_target_edit_role_name");
		ACC_MGMT_SCREEN_ROLE = prop.getProperty("AccMgmt_screen_role");
		ACC_MGMT_SCREEN_NAME = prop.getProperty("AccMgmt_screen_name");
		ACC_MGMT_USER_ACTIONS_ROLE = prop.getProperty("AccMgmt_user_actions_role");
		ACC_MGMT_USER_ACTIONS_SCREEN = prop.getProperty("AccMgmt_user_actions_screen");
		ACC_MGMT_USER_ACTIONS_PERMISSION = prop.getProperty("AccMgmt_user_actions_permission");
		ACC_MGMT_AIRLINE_SCHEDULES_AIRLINE_CODE = prop.getProperty("Adm_airline_scedules_airline_code");
		ACC_MGMT_AIRLINE_SCHEDULES_DELETE_AIRLINE_CODE = prop.getProperty("AccMgmt_airline_scedules_delete_airline_code");
		ADM_AL_SC_EDIT_AIRLINE_CODE = prop.getProperty("Adm_airline_scedules_airline_code");
		ADM_AL_SC_EDIT_TARGET_AIRLINE_CODE = prop.getProperty("Adm_al_sc_edit_target_airline_code");
		ADM_AL_SC_EDIT_TARGET_AIRLINE_NAME = prop.getProperty("Adm_al_sc_edit_target_airline_name");
		ADM_AL_SC_ADD_AIRLINE_NAME = prop.getProperty("Adm_al_sc_add_airline_code");
		ADM_AL_SC_ADD_AIRLINE_NAME = prop.getProperty("Adm_al_sc_add_target_airline_name");
		ADM_PTS_BODY_TYPE = prop.getProperty("Adm_PTS_body_type");
		ADM_PTS_ARR_TYPE = prop.getProperty("Adm_PTS_arr_type");
		ADM_PTS_BAY_TYPE = prop.getProperty("Adm_PTS_bay_type");
		ADM_PTS_ACT_NAME = prop.getProperty("Adm_PTS_act_name");
		ADM_PTS_DELETE_AIRCRAFT_NAME = prop.getProperty("Adm_PTS_delete_aircraft_name");
		ADM_PTS_EDIT_AC_CATEGORY = prop.getProperty("Adm_PTS_edit_AC_category");
		ADM_RESOURCE_INACT_EMP = prop.getProperty("Adm_resource_inact_emp");
		ADM_RES_ADD_EMP = prop.getProperty("Adm_res_add_emp");
		ADM_RES_ADD_EMP_FNAME = prop.getProperty("Adm_res_add_emp_fname");
		ADM_RES_ADD_EMP_LNAME = prop.getProperty("Adm_res_add_emp_lname");
		ADM_RES_ADD_EMP_DESIGNATION = prop.getProperty("Adm_res_add_emp_desgination");
		ADM_RES_EDIT_EMP = prop.getProperty("Adm_res_edit_emp");
		ADM_RES_EDIT_FNAME = prop.getProperty("Adm_res_edit_emp_fname");
		ADM_RES_EDIT_LNAME = prop.getProperty("Adm_res_edit_emp_lname");
		ADM_MGT_ADD_AL_CODE = prop.getProperty("Adm_MGT_add_airline_code");
		ADM_MGT_ADD_ARC_TYPE = prop.getProperty("Adm_MGT_add_aircraft_type");
		ADM_MGT_ADD_FLIGHT_TYPE = prop.getProperty("Adm_MGT_add_flight_type");
		ADM_MGT_ADD_MINGRD_TIME = prop.getProperty("Adm_MGT_add_mingrd_time");
		ADM_MGT_ADD_MAXGRD_TIME = prop.getProperty("Adm_MGT_add_maxgrd_time");
		ADM_EQP_PROF_ADD_CATEGORY = prop.getProperty("Adm_EQP_prof_add_category");
		ADM_EQP_PROF_ADD_OWNERTYPE = prop.getProperty("Adm_EQP_prof_add_ownerType");
		ADM_EQP_PROF_ADD_EQPNAME = prop.getProperty("Adm_EQP_prof_add_eqpName");
		ADM_EQP_PROF_ADD_IATACODE = prop.getProperty("Adm_EQP_prof_add_iataCode");
		ADM_EQP_PROF_ADD_EQPNUM = prop.getProperty("Adm_EQP_prof_add_eqpNumber");
		ADM_EQP_PROF_ADD_ASSTAGNO = prop.getProperty("Adm_EQP_prof_add_assetTagNo");
		ADM_EQP_PROF_ADD_LMAINDATE = prop.getProperty("Adm_EQP_prof_add_lmainDate");
		ADM_EQP_PROF_EDIT_EQPNUM = prop.getProperty("Adm_EQP_prof_edit_eqpNum");
		ADM_EQP_PROF_EDIT_EQPNAME = prop.getProperty("Adm_EQP_prof_edit_eqpName");
		ADM_EQP_PROF_EDIT_TARGET_NUM = prop.getProperty("Adm_EQP_prof_edit_target_Num");
		ADM_EQP_PROF_EDIT_TARGET_ASSTAGNO = prop.getProperty("Adm_EQP_prof_edit_target_assetTagNo");
		ADM_EQP_PROF_EDIT_TARGET_EQPNUM = prop.getProperty("Adm_EQP_prof_edit_target_eqpNum");
		ADM_EQP_PROF_ACT_INACT_EQPNUM = prop.getProperty("Adm_EQP_prof_act_inact_eqpNum");
		ADM_EQP_PROF_DELETE_EQPNUM = prop.getProperty("Adm_EQP_prof_delete_eqpNum");
		ROSTER_FILE_PATH = prop.getProperty("filePath");
		ELEMENT_FIND_TIME = Long.parseLong(prop.getProperty("element_time"));
		TEST_ENV = prop.getProperty("test_environemnt");
		PROD_ENV = prop.getProperty("prod_environemnt");
	}
}
