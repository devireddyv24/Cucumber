package com.weichertwm.qa.framework;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.Sheet;

import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.util.ExcelUtil;

public class Data {
	
	private HashMap<String, String> data = new HashMap<String, String>();

	/**
	 * This method is used to load the data from test data excel file
	 * @param fileName
	 * @param sheetName
	 * @param testCase
	 * @throws Exception
	 */
	public synchronized void loadFromExcel(String fileName, String sheetName,String testCase) {
		try {
			Sheet mySheet = ExcelUtil.getSheet(fileName, sheetName);
			int rowNum = ExcelUtil.findRow(mySheet, testCase.toLowerCase());
			if (rowNum == -1) {
				throw new Exception("There is no such Test Case "+testCase+" in the data sheet "+sheetName);
			}
			if (rowNum > -1) {
				data=ExcelUtil.load(mySheet,rowNum);
			}
			
		} catch (Exception e) {
			Log.error("Issue in loading the test data file:"+fileName+" and sheet:"+sheetName+" and test case:"+testCase);
			Log.catching(e);
		}
	}
	
	/**
	 * This method is used to get the value for specified keyName
	 * @param keyName
	 * @return
	 */
	public synchronized String get(String keyName) {
		String retValue="";
		try {
			if(data.isEmpty()) {
				System.out.println("No test data available for this test");
			}
			if (data.containsKey(keyName)) {
				retValue= data.get(keyName);
				if(retValue.startsWith("<") && retValue.endsWith(">"))
					retValue=data.get(retValue.substring(1, retValue.length()-1));
				System.out.println("Key:"+keyName+" and Value:"+retValue);
			} 
		} catch (Exception e) {
			System.out.println("Unable to get the test data for key:"+keyName);
		}
		return retValue;	
	} 
	

	/**
     * This method is used to get the value for specified keyName and record number ex:key1
     * @param keyName
     * @return
     */
     public synchronized String get(String recordNumber,String keyName) {
            String retValue="";
            try {
                   if(data.isEmpty()) {
                         System.out.println("[Error]No test data available for this test");
                   }
                   if (data.containsKey(keyName+recordNumber)) {
                         retValue= data.get(keyName+recordNumber);
                         System.out.println("[Debug]Key:"+keyName+recordNumber+" and Value:"+retValue);
                   } 
            } catch (Exception e) {
                   System.out.println("[Error]Unable to get the test data for key:"+keyName+recordNumber);
            }
            return retValue;     
     }
     /**
 	 * This method is used to set the dynamic test data with key and value
 	 * This method
 	 * @param testDatakey
 	 * @param testDataValue
 	 */
 	public synchronized void update(String testDatakey, String testDataValue) {
 		try {
 			if (data.containsKey(testDatakey)) {
 				data.put(testDatakey, testDataValue);
 				System.out.println("Setting dynamic test data Key:"+testDatakey+" with new value:"+testDataValue);
 			}
 			else {
 				System.out.println(testDatakey+" key not found in test data");
 			}
 			
 		} catch (Exception e) {
 			System.out.println("Unable to set the test data with key:"+testDatakey+" and value:"+testDataValue);
 		}
 	}
	
	/**
	 * This method is used to set the dynamic test data with key and value
	 * This method
	 * @param testDatakey
	 * @param testDataValue
	 */
	public synchronized void set(String testDatakey, String testDataValue) {
		try {
			if (!data.containsKey(testDatakey)) {
				System.out.println("Setting dynamic test data Key:"+testDatakey+" with Value:"+testDataValue);
				data.put(testDatakey, testDataValue);
			}
			else {
				System.out.println("already test data Key:"+testDatakey+" exists");
			}
		} catch (Exception e) {
			System.out.println("Unable to set the test data with key:"+testDatakey+" and value:"+testDataValue);
		}
	}
	
	public synchronized HashMap<String, String> getAllData() {
		return data;
		
	}
	
	/**
	 * This method is used to get the data from common sheet
	 * @param column
	 * @return
	 */
	public synchronized String getCommonData(String column) {
		String returnValue=ExcelUtil.getCommonData(column);
		if(returnValue==null || returnValue.contains("")) return "";
		else return returnValue;
	}
}
