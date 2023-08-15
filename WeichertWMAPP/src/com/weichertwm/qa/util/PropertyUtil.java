package com.weichertwm.qa.util;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Properties;

import com.weichertwm.qa.framework.Log;

public class PropertyUtil {
	
	/**
	 * This method is used to get all the properties from the specified property file
	 * @param filePath
	 * @return
	 */
	public static LinkedHashMap<String, String> getProperties(String filePath) {
		Log.info("Getting all properties from file path : "+filePath);
		
		Properties p = new Properties();
		LinkedHashMap<String, String> propertyDetails = new LinkedHashMap<String, String>();
		String[] filepaths = filePath.split(",");
		for(String filepath:filepaths)
		{
			try {
				p.load(Files.newInputStream(Paths.get(filepath),StandardOpenOption.READ));
				p.forEach((key,value) -> propertyDetails.put((String)key,(String)value));			//Java 8 labmda expressions
			} catch (FileNotFoundException e) {
				Log.error("Could not find config file at path : " + filepath);
				System.exit(1);
			} catch (Exception e) {
				Log.error("Could not read Property file at path : " + filepath);
				System.exit(1);
			}
		}
		

		return propertyDetails;

	}

}
