package com.weichertwm.qa.testrunner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.testng.TestNG;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.FailurePolicy;
import org.testng.xml.XmlTest;

import com.weichertwm.qa.framework.BuildParams;
import com.weichertwm.qa.framework.ExtentReport;
import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.TestCategory;
import com.weichertwm.qa.util.ReportUtil;

public class TestRunner {

	private static List<String> findClasses(File directory) {

		List<String> classes = new ArrayList<String>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				classes.addAll(findClasses(file));
			} else if (file.getName().endsWith(".class")) {
				String abosultePath = file.getAbsolutePath();
				classes.add(abosultePath);
			}
		}
		return classes;
	}

	private static Class<?> getClassFromFile(String fullClassName) throws Exception {
		URLClassLoader loader = new URLClassLoader(
				new URL[] { new URL("file:" + System.getProperty("user.dir") + "/bin/") });
		String fileName = fullClassName.substring(fullClassName.indexOf("com"), fullClassName.length())
				.replace("\\", ".").replace(".class", "");
		Class<?> clas = loader.loadClass(fileName);
		loader.close();
		return clas;
	}
	
	//**************************************************************************************************
	/**
	 * This method will runs before each suite and initiate the suite 
	 */
	@BeforeSuite
	public static void initialize() throws Exception {
		//Initialize Logger;	
		//Log.logger = LogManager.getRootLogger();
		ExtentReport.createReportFolder();
		Log.logInit();
		ExtentReport.startExtentReport();
	}
		
	@Test
	public static void mainTest() {
		List<String> listOfAllClasses= findClasses(new File(System.getProperty("user.dir")));
		Set<String> allClasses=new HashSet<String>();
		Map<String, Set<String>> categorizedClasses=new HashMap<String, Set<String>>();
		for(String className:listOfAllClasses){
			try {
				Class<?> cls = getClassFromFile(className);
				Method method =cls.getMethod("test") ;	
				String classFullName = cls.getName();
				TestCategory testcat=method.getAnnotation(TestCategory.class);
				if(testcat!=null){
					//System.out.println("className: "+className);
					//System.out.println("-----------------"+testcat.categories());
					String[] categories = testcat.categories().split(",");
				
					for(String category:categories){
						Set<String> list=new HashSet<String>();
						if(categorizedClasses.get(category)!=null){
							list.addAll(categorizedClasses.get(category));
						}
						list.add(classFullName);
						allClasses.add(classFullName);
						categorizedClasses.put(category, list);
					}
				}
				categorizedClasses.put("All", allClasses);
				//System.out.println("categorizedClasses size:"+categorizedClasses.size());
			}
			catch (Exception e) {				
			}
			
		}	
		TestNG tng = new TestNG();
		tng.setConfigFailurePolicy(FailurePolicy.CONTINUE);
		
		XmlSuite suite = new XmlSuite();

		List<XmlClass> classes = new ArrayList<XmlClass>();	
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suite.setName("RnA2 Automated Test Suite");
		//suite.addListener("com.zestiot.avileap.qa.framework.Listener");
		Log.debug("Tests = " + BuildParams.TESTCASES);
		if (BuildParams.TESTCASES!= null) {
			for(String multiple:BuildParams.TESTCASES.split("::")){
				if(categorizedClasses.get("All").contains(multiple)){
					classes.add(new XmlClass(multiple));
					//System.out.println("Add classes to xml file...");
				}
				else if(!multiple.contains("\\.") && !categorizedClasses.keySet().contains(multiple)){
					for(String test:categorizedClasses.get("All")){
						if(test.endsWith(multiple)) {
							classes.add(new XmlClass(test));
							break;
						}
					}
				}
				else{
					for(String cat:categorizedClasses.keySet()){
						if(cat.equalsIgnoreCase(multiple)){
							Set<String> tempCollection=categorizedClasses.get(cat);
							for(String temp:tempCollection){
								classes.add(new XmlClass(temp));
							}
						}
					}
				}
				
			}
			XmlTest test = new XmlTest(suite);
			Log.info("XML SUITE START...");
			test.setName("New R&A Automation Test ");
			test.setXmlClasses(classes);

			suites.add(suite);
			tng.setXmlSuites(suites);
			
			Log.info("TestNG will start now");
			tng.run();
		}
	}
	
		/**
		 * This method will runs after each suite and close the suite 
		 */
		/*@AfterSuite(alwaysRun = true)
		public synchronized void finish() throws Exception {
			Log.info("Closing reporting engine");
			ExtentReport.endExtentReport();
			Log.info("Shutting down Logger ");
			//LogManager.shutdown();
		}*/
	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception
	{
		String strSuiteName,strSendMail = "";
		try {
			ExtentReport.closeExtentReport();
			strSuiteName = BuildParams.SUITE_NAME;
			strSendMail = BuildParams.IS_SEND_MAIL;
			if((!StringUtils.isEmpty(strSuiteName)) && strSendMail.equalsIgnoreCase("Yes")) {
				ReportUtil.readReportAndStoredIntoDb(ExtentReport.resultsPath+"\\report.html", strSuiteName);
				Log.info("Report read and insert into DB completed....");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
