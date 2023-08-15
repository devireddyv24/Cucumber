package com.weichertwm.qa.framework;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.weichertwm.qa.framework.ExtentReport;
import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.Status;
import com.weichertwm.qa.util.ReportUtil;



public abstract class TestCase{
		
	public WrappedWebDriver driver = null;
	protected Data data = null;
	protected boolean initDone = false;
	public volatile String tcStatus = "Pass";
	public volatile String buildVersion = null;
	protected String scenarioName = null;
	
	
	public volatile boolean isAutoReport = true;
	public volatile boolean isWebTest = true;
	public volatile boolean isAutoData = true;
	
	public static String envHost = null;
	
	/**
	 * This method should be overridden by the each tests class and 
	 * actual test script code will be written in the overridden method.  
	 * @throws Exception
	 */
	@Test
	public abstract void test() throws Exception;
	
	@BeforeSuite
	public void beforeSuite()
	{
		if (isAutoReport) {
			ExtentReport.createReportFolder();
			Log.logInit();
			ExtentReport.startExtentReport();
		}
	}
	/**
	 * This method will initiate all the required elements for the test script
	 * before the test get started
	 */
	@BeforeMethod
	public void beforeTest(){
		String applicationURL = null;
		try {
			Log.info("Initiating the required elements to run the test");
			Log.info("Executing Test : "+this.getTestName());
			if (isAutoReport) {
				//ExtentReport.startExtentReport();
				ExtentReport.startExtentTest(this);
			}
			buildVersion = Config.getEnvDetails("aut.build");
			if(buildVersion == null || buildVersion.trim().equals("")){
				throw new Exception("BuildVersion shouldn't be null");
			}
			if (isAutoData) {
				Log.info("Loading data");
				data=new Data();		
				String fileName  =  Config.getEnvDetails("datafilename");
				if(scenarioName != null) 
					data.loadFromExcel(fileName, this.getTestModule(), scenarioName);
				else {
					String[] strArrTestName = this.getTestName().split("_");
					String testName = null;
					if(strArrTestName.length>1)
						testName= strArrTestName[strArrTestName.length-1];
					else
						testName = this.getTestName();
					Log.info("Loading data from "+fileName+" of module "+this.getTestModule()+" for the Test : "+testName);
					data.loadFromExcel(fileName, this.getTestModule(), testName);
				}					
			}
			applicationURL = BuildParams.PORTAL_APP_URL;
			System.out.println("Application URL::"+applicationURL);
			if(applicationURL!=null && applicationURL.contains("/portal")) {
				envHost = applicationURL.split("//")[1].split("/")[0];
				Log.info("Environment Host :: "+envHost);
			}
			if (isWebTest) {
				Log.info("Currently executing test is a Web Test");
				if(driver == null) {
					Log.info("Initiating the driver for the browser");
					driver = new WrappedWebDriver();
				}
				driver.get(applicationURL);
			}
			
		} catch (Exception e) {
			Log.catching(e);
			ExtentReport.log("Failed to initialize the elements before test start", Status.FATAL, driver);
		}
	}
	
	
	/**
	 * This method will be executed at the end of each test run 
	 * And this method will perform all closing activities for test
	 */
	@AfterMethod
	public void afterTest(ITestResult result) {
		if (isWebTest) {
			try{
				Log.info("Closing the driver");
				driver.quit();
				driver.killDriverProcess();
				if(ITestResult.FAILURE==result.getStatus()) {
					Log.error(result.getTestName()+" Failed");
					ExtentReport.logFail(result.getTestName()+" Failed");
				}
			}catch(Exception e){
				Log.error("Exception Occured while ending the test case due to :"+e.getMessage());
			}
		}
		
		if (isAutoReport) {
			Log.info("Ending ExtentTest throgh class "+this.getClass().getName());
			ExtentReport.endExtentTest();
		}		
		
		System.out.println("==================================================");
		System.out.println("Test execution ended for "+this.getTestName());
		System.out.println("==================================================");
	}
	
	@AfterSuite
	public void afterSuite() throws Exception
	{
		String strSuiteName,strSendMail = "";
		if (isAutoReport) {
			ExtentReport.closeExtentReport();
		}
		strSuiteName = BuildParams.SUITE_NAME;
		strSendMail = BuildParams.IS_SEND_MAIL;
		if((!StringUtils.isEmpty(strSuiteName)) && strSendMail.equalsIgnoreCase("Yes")) {
			ReportUtil.readReportAndStoredIntoDb(ExtentReport.resultsPath+"\\report.html", strSuiteName);
			Log.info("Report read and insert into DB completed....");
		}
		
	}
	
	/**
	 * This method is used to set the attributes dynamically while test is running
	 *  which may be required for test reports.
	 *  
	 * @param		attributeName
	 * @param		attributeValue
	 */
	public final void setAttribute(String attributeName, String attributeValue) {
		if(attributeName.equalsIgnoreCase("tcStatus")) {
			tcStatus = attributeValue;			
		}
	}
				
	public String getTestName() {
		//return this.getTestModule()+"_"+this.getClass().getSimpleName();
		return this.getClass().getSimpleName();
	}
	
	public String getTestModule() {
		String canonicalName=this.getClass().getName();
		String[] splitCanonicalName=canonicalName.split("\\.");			
		return splitCanonicalName[splitCanonicalName.length-2];
	}

	
}
