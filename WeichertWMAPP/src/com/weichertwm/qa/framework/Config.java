package com.weichertwm.qa.framework;

import java.util.*;

import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.util.PropertyUtil;

public class Config {

	private static LinkedHashMap<String, String> envDetails = new LinkedHashMap<String, String>();;
	private static final String configFilePath = "resources/inputs/appconfig.properties,resources/inputs/frameworkconfig.properties";
	private static String addnlConfigFilePath = "";		//Remote Execution Portal
	/**
	 * This method is used to get required property from config.properties
	 * @param 		propertyName
	 * @return
	 */
	public static synchronized String getEnvDetails(String propertyName) {
		return getORmodifyProperty("get",propertyName, "");
	}

	/**
	 * This method is used to set the new property dynamically when needed
	 * 
	 * @param 		propertyName
	 * @param 		propertyValue
	 * @return
	 */
	public static synchronized void setEnvDetails(String propertyName, String propertyValue) {
		getORmodifyProperty("add",propertyName, propertyValue);
	}
	
	/**
	 * This method is used to update existing property
	 * 
	 * @param 		propertyName
	 * @param 		propertyValue
	 * @return
	 */
	public static synchronized void updateProperty(String propertyName, String propertyValue) {
		getORmodifyProperty("update",propertyName, propertyValue);
	}
	
	/**
	 * This method will initialize and load the properties from config.properties
	 *  and perform a requested operation.
	 * 
	 * Method is internal to the code and should not be called in Test Code.
	 * 
	 * @param 		op				Could be <b>get</b> or <b>add</b> or <b>update</b>
	 * @param 		propertyName	Name of the prop that needs be retrieved or set
	 * @param		propertyValue	Value to be set against prop. Can be passed as null or empty in case of get operation
	 * 
	 * @return		{@link String}
	 */
	private static synchronized String getORmodifyProperty(String op, String propertyName, String propertyValue) {
		try {

			if (envDetails == null || envDetails.isEmpty()) {
				Log.info("Initializing and loading the Configurations");
				envDetails = PropertyUtil.getProperties(configFilePath);
				
				//For the remote exec portal
				if (addnlConfigFilePath != null && !addnlConfigFilePath.trim().isEmpty()) {
					envDetails.putAll(PropertyUtil.getProperties(addnlConfigFilePath));
				}
				Log.info("All Properties loaded successfully..");
			}
			
			if (op.equalsIgnoreCase("get")) {
				if (envDetails.containsKey(propertyName)) {
					String returnValue = envDetails.get(propertyName);
					Log.debug("Property:"+propertyName+" and Value:"+returnValue);
					return returnValue;
				} else {
					Log.warn("Could not find property ["+propertyName+"] hence returning empty");
					return "";
				}
			} else if (op.equalsIgnoreCase("add") || op.equalsIgnoreCase("update")) {
				Log.debug("Adding/Updating Property name:"+propertyName+" with Value:"+propertyValue);
				envDetails.put(propertyName, propertyValue);
				return propertyValue;
			}
			
		} catch (Exception e) {
			Log.error("Problem in Updating Property name:"+propertyName+" with Value:"+propertyName);
		}
		
		return propertyValue;
		
	}

	public static synchronized void setAddnlConfigFilePath(String path) {
		addnlConfigFilePath = path;
	}
}