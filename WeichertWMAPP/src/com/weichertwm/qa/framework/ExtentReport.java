package com.weichertwm.qa.framework;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import com.weichertwm.qa.framework.Config;
import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.TestCase;
import com.weichertwm.qa.framework.WrappedWebDriver;
import com.weichertwm.qa.util.DateHelper;
import com.weichertwm.qa.util.PropertyUtil;

public class ExtentReport{

	private static boolean isReady = false;
	private static boolean isLogging = false;
	
	private static volatile ExtentReports report = null;
	private static ExtentTest testReporter = null;
	private static int screenShotCounter = 0;
	private static String tcName = null;
	static volatile boolean extentInitDone=false;

	private static String workingDir = Paths.get("").toAbsolutePath().toString();
	public static String resultsPath = null;
	public static String screenshotsPath  = null;

	private static volatile Map<Long, ExtentTest> extentTestMap;

	private static volatile String timeStamp = null;

	//Added by RJAWALE for multi-threaded tests
	private static long globalTestId = -1;
	private static boolean isUseGlobalTestId = false;

	

	/**
	 * This method was added to force set TestId. This can 
	 *  particularly be useful when a test spawns its own threads,
	 *  which require reporting support. A child thread spawned by 
	 *  Test will have a different ThreadId which also serves as TestId 
	 *  in case of ExtentReports code
	 *  
	 * @param 		testId	{@link long} Thread Id / Test Id
	 * @example		<pre> {@code
	 * 
	 * 			public class myTest extends TestCase {
	 * 			
	 * 				\@BeforeClass
	 * 				public void setup() {
	 * 					ExtentReport.setIsGlobalTestId(true);
	 * 					ExtentReport.setTestId(ExtentReport.getTestId());
	 * 				}
	 * 
	 * 				\@Test
	 * 				public void test() {
	 * 					//test code
	 * 				}
	 * 			}
	 * }</pre>
	 */
	public static synchronized void setTestId(long testId) {
		globalTestId = testId;
	}

	/**
	 * This method was added to force set TestId. This can 
	 *  particularly be useful when a test spawns its own threads,
	 *  which require reporting support. A child thread spawned by 
	 *  Test will have a different ThreadId which also serves as TestId 
	 *  in case of ExtentReports code
	 *  
	 * @param 		testId	{@link long} Thread Id / Test Id
	 * @example		<pre> {@code
	 * 
	 * 			public class myTest extends TestCase {
	 * 			
	 * 				\@BeforeClass
	 * 				public void setup() {
	 * 					ExtentReport.setIsGlobalTestId(true);
	 * 					ExtentReport.setTestId(ExtentReport.getTestId());
	 * 				}
	 * 
	 * 				\@Test
	 * 				public void test() {
	 * 					//test code
	 * 				}
	 * 			}
	 * }</pre>
	 */
	public static synchronized void setIsGlobalTestId(boolean isGlobalTestId) {
		isUseGlobalTestId = isGlobalTestId;
	}

	/**
	 * Returns the current Test Id
	 * This method should generally be used when designing tests
	 * 
	 * @return
	 */
	public static synchronized long getTestId() {
		return Thread.currentThread().getId();	
	}

	/**
	 * <b>Description</b> To get the current test case threat
	 *  @return  ExtentTest    returns extent test
	 */
	public static synchronized ExtentTest getTest() {
		long testId = Thread.currentThread().getId();
		return getTest(testId);			
	}

	/**
	 * <b>Description</b> To get the current test case threat
	 *  @return  ExtentTest    returns extent test
	 */
	public static synchronized ExtentTest getTest(Long testId) {
		//Initialize if not already
		//This line was added to support running a class as TestNG test
		if (isReady == false) startExtentReport();
		
		//Continue
		if (isLogging) Log.trace("Test Id : "+testId);

		//Check if the Test exists
		if (false == extentTestMap.isEmpty() 
				&& extentTestMap.containsKey(testId)) {
			if (isLogging) Log.trace("Found the test in Report Map");
			return extentTestMap.get(testId);
		} else {
			if (isLogging) Log.trace("Could NOT find the test in Report Map");
			return null;
		}

	}

	/**
	 * Removes a test from from Extent Test Map
	 * 
	 * When a test is closed in ExtentReport it is required 
	 * 	that the test be removed from the Test Map to avoid
	 *  unforeseen errors
	 *  
	 * <strong>This method should not be used by the end-user in
	 *  the test case code. </strong>
	 */
	@SuppressWarnings("unused")
	private static synchronized void removeTest() {
		long testId = Thread.currentThread().getId();
		removeTest(testId);
	}


	/**
	 * Removes a test from from Extent Test Map
	 * 
	 * When a test is closed in ExtentReport it is required 
	 * 	that the test be removed from the Test Map to avoid
	 *  unforeseen errors
	 *  
	 * <strong>This method should not be used by the end-user in
	 *  the test case code. </strong>
	 */
	private static synchronized void removeTest(long testId) {
		if (extentTestMap.containsKey(testId)) {
			if (isLogging) Log.debug("Test Id ["+testId+"] will be removed");
			extentTestMap.remove(testId);
		}
	}



	/**
	 * This method is to be called when test id is not known
	 * 
	 */
	public static synchronized void endExtentTest(){
		endExtentTest(getTestId());
	}

	/**
	 * This method will be used by Power Users who know what they are doing
	 * 
	 * @param		testId
	 */
	public static synchronized void endExtentTest(Long testId) {
		//End the Test
		endExtentTest(getTest(testId));

		//Cleanup
		removeTest(testId);

	}


	/**
	 * Ends reporting for a test case
	 * 
	 * Now has a safety net of null-check for a test. Silently suppresses
	 *  errors if any 
	 * 
	 * @param 		extentTest		{@link ExtentTest} test object
	 */
	private static synchronized void endExtentTest(ExtentTest extentTest){
		if (extentTest == null) return;

		System.out.println("-------------------------------------------------------");
		Log.debug("Ending extent test ["+extentTest+"]");
		report.endTest(extentTest);
		
		System.out.println("-------------------------------------------------------");

	}
	public static synchronized void closeExtentReport()
	{
		report.flush();
		report.close();
	}


	/**
	 * This method was added to support test execution through the Portal.
	 * This method will find a job.properties file in the project directory and 
	 * use the jobId for generating the report.
	 * 
	 * This is for the Agent and Portal to identify the report file by jobId.
	 * 
	 * @note		Please do not modify this method without consulting the {@link author}
	 * @author 		RJAWALE
	 * @return		jobId in {@link String} variable
	 */
	private static synchronized String getTestJobId() {
		//Log.info("Searching for Job Id");
		Collection<File> listOfFiles = FileUtils.listFiles(new File(workingDir), FileFilterUtils.nameFileFilter("job.properties", IOCase.INSENSITIVE), TrueFileFilter.TRUE);

		//Return empty string if no files or more than one files are found
		if (listOfFiles.size() == 0 || listOfFiles.size() > 1) return "";

		//Get the Job Id
		Iterator<File> fileIter = listOfFiles.iterator();
		File jobFile = fileIter.next();
		HashMap<String, String> properties= PropertyUtil.getProperties(jobFile.getAbsolutePath());
		if (properties.containsKey("jobId"))
			return properties.get("jobId");
		else if (properties.containsKey("jobid"))
			return properties.get("jobid");
		else 
			return "";
	}


	public static synchronized void createReportFolder()
	{
		//Generate timestamp for use
		timeStamp = DateHelper.getCurrentDatenTime("yyyyMMdd") + "_" + DateHelper.getCurrentDatenTime("HHmmss");
		//Log.info("Using timestamp in result folder creation "+timeStamp);
		String jobId = getTestJobId();
		resultsPath = (jobId.isEmpty()) ? 
				workingDir + File.separator + "reports"+ File.separator + "Run_"+ timeStamp 
				: workingDir + File.separator + "reports"+ File.separator + jobId;
		//Log.info("Result folder path:"+resultsPath);
		/*resultsPath = (jobId.isEmpty()) ? 
				workingDir + File.separator + "reports"+ File.separator
				: workingDir + File.separator + "reports"+ File.separator + jobId;
		Log.info("Result folder path:"+resultsPath);*/
		System.out.println( " ********************************************8 ::" + resultsPath);
		//Create folder
		if(!new File(resultsPath).isDirectory()) {
			new File(resultsPath).mkdirs();
			//Log.debug("new folder is created at path:"+resultsPath);
		}
		screenshotsPath = resultsPath+"\\images";
		//Create folder
		if(!new File(screenshotsPath).isDirectory()) {
			new File(screenshotsPath).mkdirs();
			//Log.debug("new folder is created at path:"+screenshotsPath);
		}
		System.setProperty("reportPath", resultsPath);
		//System.setProperty("screenshotsPath", screenshotsPath);
	}
	/**
	 * This method will Initiate reporting for the suite 
	 * and starting saving the report a the newly result folder
	 * @throws UnknownHostException 
	 *  
	 */
	public static synchronized void startExtentReport() {
		//Save the state
		isReady = true;
		//Suppress freemarker debug log
		Log.info("Switching off freemarker spam");
		System.setProperty("org.freemarker.loggerLibrary", "none");
		
		//Initialize Map that keeps track of currently executing test cases
		extentTestMap = new ConcurrentHashMap<Long, ExtentTest>();

		if(resultsPath==null)
			createReportFolder();
		//Setup Extent Report 
		String extentReportFile = resultsPath+Config.getEnvDetails("extent.reportfile");
		Log.info("Starting Extent report in online mode and the report file path : ["+extentReportFile+"]");
		report = new ExtentReports(extentReportFile,NetworkMode.ONLINE);

		//Read the Extent Report config file
		String extentConfigFile = System.getProperty("user.dir") + Config.getEnvDetails("extent.configfile");
		Log.info("Loading configurations from path : "+extentConfigFile);
		report.loadConfig(Paths.get(extentConfigFile).toFile());

		try {
			//Add other information
			Log.info("Adding the environment details to the extent report");
			report.addSystemInfo("HostName", InetAddress.getLocalHost().getHostName());
			report.addSystemInfo("Environment", "QA");
			report.addSystemInfo("RunId", resultsPath);
			report.addSystemInfo("java.library.path", System.getProperty("java.library.path"));
			report.addSystemInfo("PATH",System.getenv("PATH"));
		} catch (Exception e) {
			Log.catching(e);
		}

	}	



	/**
	 * <b>Description</b> Initiate report for a test case
	 *  @param  TestCase     TestCase object
	 *   
	 */
	public static synchronized void startExtentTest(TestCase tc){
		System.out.println("-------------------------------------------------------");
		tcName = tc.getTestName();
		testReporter = report.startTest(tcName);
		Log.info("Starting extent test for test class :: "+tcName);
		extentTestMap.put(Thread.currentThread().getId(), testReporter);
		System.out.println("-------------------------------------------------------");
	}

	/**
	 * <b>Description</b> Initiate for report a test case
	 *  @param  String      Test case name
	 *  @param  String	    Test case description
	 *  @param  String		Test category
	 *  
	 */
	public static synchronized void startExtentTest(String testName, String description, String... category){
		if (isReady == false) startExtentReport();
		
		Log.debug("Starting reporting for test "+testName+" with category "+category);
		testReporter = report.startTest(testName, description).assignCategory(category);
		extentTestMap.put(Thread.currentThread().getId(), testReporter);
	}

	/**
	 * <b>Description</b> Ending report for the suite
	 * @throws InterruptedException 
	 *  
	 */
	public static synchronized void endExtentReport() throws InterruptedException{	
		//Close all open Extent Test reports
		endAllExtentTests();

		//Close the actual report
		report.flush();
		Thread.sleep(2000);
		report.close();
	}


	/**
	 * Ideally the test designer should close all opened test within the
	 * test code. Yet, a test may remain opened if the test exception are 
	 * not properly handled.
	 * 
	 * In this case, we close all open Extent Test Reports
	 */
	public static synchronized void endAllExtentTests() {
		if (extentTestMap.size() > 0) {
			extentTestMap.forEach((testId, extentTest) -> endExtentTest(testId));
		}
	}

	/**
	 * <b>Description</b> log a statement in report
	 * 
	 *  @param  message     message to be logged
	 *  @param  status	    status of the logger
	 *  @param	driver		Web Driver
	 */
	public static void log(String message, Status status, WrappedWebDriver driver){
		ExtentTest test = getTest();
		String loggerMsg = message;
		
		//Added for multi-threaded tests where a thread unknown to ExtentReport tries to log the 
		//_message
		if (test == null && isUseGlobalTestId) test = getTest(globalTestId);
		if (test == null) return;
		if (message.contains("<pre>") && 
				message.contains("</pre>")) {
				loggerMsg = message.replaceAll("<pre>", "").replaceAll("</pre>", "");
		}
		
		switch (status) {
		case INFO:
			Log.info(loggerMsg);
			test.log(LogStatus.INFO, "<span style='color:green'>" + message + "</span>");
			break;
		case PASS:
			Log.info(loggerMsg);
			test.log(LogStatus.PASS, "<span style='color:green'>" + message + "</span>");
			break;
		case FAIL:
			Log.error(loggerMsg);
			test.log(LogStatus.FAIL,"<span style='color:red'>" + message + "</span>" 
									+ ((driver != null) ? testReporter.addScreenCapture(takeScreenShot(driver)) : ""));
			break;
		case BUSINESSSTEP:
			Log.info(loggerMsg);
			test.log(LogStatus.INFO, "<span style='background-color:#EA8570'>" + message + "</span>");
			break;
		case DONE:
			Log.info(loggerMsg);
			test.log(LogStatus.INFO, message);
			break;
		case WARN:
			Log.warn(loggerMsg);
			test.log(LogStatus.WARNING, message);
			break;
		case FATAL:
			Log.fatal(loggerMsg);
			test.log(LogStatus.FAIL, message);
			break;
		case DEBUG:
			Log.debug(loggerMsg);
			break;
		case KEYWORD:
			break;
		default:
			break;
		}	

		report.flush();
	}


	/**
	 * <b>Description</b> log a statement in report
	 *  
	 * @param  message     message to be logged
	 * @param  status	    status of the logger
	 * 
	 * @history
	 * 2017-12-27 : Added a safety net to reject NULL tests when 
	 *  
	 */
	
	/*public synchronized static void log(LogStatus logStatus, String message){
		ExtentTest test = getTest();

		//Added for multi-threaded tests where a thread unknown to ExtentReport tries to log the 
		//_message
		if (test == null && isUseGlobalTestId) test = getTest(globalTestId);
		if (test != null) test.log(logStatus, message);
	}*/
	public synchronized static void logFail(String message,WrappedWebDriver driver){
		ExtentTest test = getTest();
		if (test == null && isUseGlobalTestId) test = getTest(globalTestId);
		if (test != null){
			test.log(LogStatus.FAIL, message);
			if(Config.getEnvDetails("extent.failscreenshot").equalsIgnoreCase("true")){
			String scrnshotfile = takeScreenShot(driver);
			test.log(LogStatus.INFO, "Snapshot below: (" + scrnshotfile + ")" + test.addScreenCapture(scrnshotfile));
			}
		}
	}
	public synchronized static void logFail(String message){
		ExtentTest test = getTest();
		if (test == null && isUseGlobalTestId) test = getTest(globalTestId);
		if (test != null) test.log(LogStatus.FAIL, message);
		
	}
	public synchronized static void logPass(String message){
		ExtentTest test = getTest();
		if (test == null && isUseGlobalTestId) test = getTest(globalTestId);
		if (test != null) test.log(LogStatus.PASS, message);
	}
	public synchronized static void logInfo(String message){
		ExtentTest test = getTest();
		if (test == null && isUseGlobalTestId) test = getTest(globalTestId);
		if (test != null) test.log(LogStatus.INFO, message);
	}
	
	public static synchronized String takeScreenShot(WebDriver driver){
		//String screenShotPath = resultsPath + "/images/" + tcName + "/" + ++screenShotCounter + ".png";
		//String screenShotPath = screenshotsPath + tcName + "/" + screenShotCounter + ".png";
		String screenShotPath = screenshotsPath + "/" + (screenShotCounter++) + ".png";
		File screenshotTestPathAsFile = Paths.get(screenShotPath).toFile();
		Log.debug("Screenshot will be saved here : "+screenShotPath);
		try {
			Log.trace("Creating parent directories for the image file");
			//FileUtils.forceMkdirParent(screenshotTestPathAsFile);
			Log.trace("Saving image file");
			// take screenshot
			FileUtils.writeByteArrayToFile(screenshotTestPathAsFile, ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
//			FileUtils.copyFile(screenshotTestPathAsFile, ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE));
		} catch (Exception e) {
			try {
				//Alternate method of capturing screenshots
				Toolkit toolkit = Toolkit.getDefaultToolkit();  
				Rectangle rect = new Rectangle(0, 0,toolkit.getScreenSize().width,
						toolkit.getScreenSize().height);   
				Log.trace("Capturing and saving image file using Robot class");
				ImageIO.write(new Robot().createScreenCapture(rect), "png", screenshotTestPathAsFile);  
			} catch (Exception e2) {
				Log.error("An error was encountered when capturing screen");
				Log.error(e2.getMessage());
			}
		}
		
		return screenShotPath;
	}
}
