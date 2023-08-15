package com.weichertwm.qa.framework;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.internal.*;
import org.openqa.selenium.remote.*;

import com.weichertwm.qa.framework.Log;

public class WrappedWebDriver
implements WebDriver, JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText, FindsByName,
FindsByCssSelector, FindsByTagName, FindsByXPath, HasInputDevices, HasCapabilities, TakesScreenshot {

	public WebDriver driver = null;
	private final String TASKLIST = "tasklist";
	private final String KILL = "taskkill /F /IM ";
	
	public WrappedWebDriver() throws Exception {
		
		Log.debug("Setting the browser variable");
		String browser = this.getBrowser();
		
		Log.debug("Setting the grid variable from property file");
		String grid = Config.getEnvDetails("browser.grid");
		
		if (grid.equalsIgnoreCase("true")) {
			this.initiateGrid();
		} else {
			Log.info("================================================================================");
			Log.info("Grid not selected, running the browser locally");
			Log.info("Selected browser : " + browser);
			Log.info("================================================================================");
			try {
				if (browser.equalsIgnoreCase("internet explorer") || browser.equalsIgnoreCase("ie")) 
					this.initiateInternetExplorer();
				else if (browser.equalsIgnoreCase("firefox")) 
					this.initiateFirefox(false);
				else if (browser.equalsIgnoreCase("firefoxheadless") || browser.equalsIgnoreCase("firefox_headless")) 
					this.initiateFirefox(true);
				else if (browser.equalsIgnoreCase("chrome")) 
					this.initiateChrome(false);
				else if (browser.equalsIgnoreCase("chromeheadless") || browser.equalsIgnoreCase("chrome_headless")) 
					this.initiateChrome(true);
				else if (browser.equalsIgnoreCase("Edge")) 
					this.initiateEdge();
				else if (browser.equalsIgnoreCase("safari")) 
					this.initiateSafari();
				
				String timeout = Config.getEnvDetails("browser.timeoutmilli");
				Log.debug("Deleting all the browser cookies");
				driver.manage().deleteAllCookies();
				Log.debug("Setting the implicit wait to " + timeout + " Milliseconds");
				driver.manage().timeouts().implicitlyWait(Integer.parseInt(timeout), TimeUnit.MILLISECONDS);
				this.setBrowserResolution();
				ExtentReport.logInfo("Test starts with Browser: <b>"+browser + "</b> Version: <b>"+this.getBrowserVersion()+"</b>");
			} catch (Exception e) {
				Log.error("Driver initialisation failed");
				Log.catching(e);			
				if (driver != null) 
					driver.quit();
				killDriverProcess();
			}
		}

	}
	private String getBrowserBits()
	{
		String strBrowserBits = BuildParams.BROWSER_BITS;
		if(StringUtils.isEmpty(strBrowserBits))
			strBrowserBits = Config.getEnvDetails("browser.bits");
		return strBrowserBits;
	}
	private String getOsPlatform()
	{
		String strOs = BuildParams.OS_PLATFORM;
		if(StringUtils.isEmpty(strOs))
			strOs=Config.getEnvDetails("browser.platform");
		return strOs;
	}
	private void initiateInternetExplorer() throws Exception
	{
		try{
			//Get config values
			String strIeDriver = "";		
			String strBrowserBits = this.getBrowserBits();
			String strPlatform = this.getOsPlatform();
			Log.debug("Starting internet explorer by setting the capabilities");					
			if (strBrowserBits.equals("64") && strPlatform.equalsIgnoreCase("windows")) 
				strIeDriver= "IEDriverServer.exe";
			else if(strBrowserBits.equals("32") && strPlatform.equalsIgnoreCase("windows"))
				strIeDriver= "IEDriverServer32.exe";
			else if(strPlatform.equalsIgnoreCase("linux")){
				Log.error("Internet Explorer not supported in linux");
				throw new Exception("Internet Explorer not supported in linux");
			}
			//Set the system property
			System.setProperty("webdriver.ie.driver", new File(".").getCanonicalPath()
					+ Config.getEnvDetails("browser.driverpath") 
					+ File.separator 
					+ strIeDriver);
			//Initialize driver
			driver = new InternetExplorerDriver(this.getInternetExplorerOptions());
		}
		catch(Exception e){
			e.printStackTrace();
			Log.catching(e);
			throw new Exception("IE browser iniatiation failed");
		}
	}
	private void initiateFirefox(boolean blnIsHeadless) throws Exception
	{
		try{
			String strFirefoxDriver = "";		
			String strBrowserBits = this.getBrowserBits();
			String strPlatform = this.getOsPlatform();
			if (strBrowserBits.equals("64") && strPlatform.equalsIgnoreCase("windows")) 
				strFirefoxDriver= "geckodriverwin64.exe";
			else if(strBrowserBits.equals("32") && strPlatform.equalsIgnoreCase("windows"))
				strFirefoxDriver= "geckodriverwin32.exe";
			else if(strBrowserBits.equals("64") && strPlatform.equalsIgnoreCase("linux"))
				strFirefoxDriver= "geckodriverlinux64";
			else if(strBrowserBits.equals("32") && strPlatform.equalsIgnoreCase("linux"))
				strFirefoxDriver= "geckodriverlinux32";
			else{
				Log.error("Firefox driver paramaeters failed. check browser.bits and driver path");
				throw new Exception("Firefox driver paramaeters failed. check browser.bits and driver path");
			}
			Log.debug("Starting firefox by setting the capabilities");
		System.setProperty("webdriver.gecko.driver", new File(".").getCanonicalPath()
				+ Config.getEnvDetails("browser.driverpath") + File.separator + strFirefoxDriver);
		if(blnIsHeadless)
			driver = new FirefoxDriver(this.getFirefoxOptions(this.getDownloadPath(),true));
		else if(!blnIsHeadless)
			driver = new FirefoxDriver(this.getFirefoxOptions(this.getDownloadPath(),false));
		}
		catch(Exception e){
			e.printStackTrace();
			Log.catching(e);
			throw new Exception("Firefox browser iniatiation failed");
		}
	}
	private void initiateChrome(boolean blnIsHeadless) throws Exception
	{
		try{
			String strChromeDriver = "";		
			String strPlatform = this.getOsPlatform();
			Log.debug("Starting Chrome by setting the capabilities");					
			if (strPlatform.equalsIgnoreCase("windows")) 
				strChromeDriver = "chromedriver.exe";
			else if(strPlatform.equalsIgnoreCase("linux"))
				strChromeDriver = "chromedriverlinux64";
			Log.debug("Starting chrome by setting the capabilities");
			System.setProperty("webdriver.chrome.driver", new File(".").getCanonicalPath() + Config.getEnvDetails("browser.driverpath") + File.separator + strChromeDriver);
			if(blnIsHeadless)
				driver = new ChromeDriver(this.getChromeOptions(this.getDownloadPath(),true));
			else if(!blnIsHeadless)
				driver = new ChromeDriver(this.getChromeOptions(this.getDownloadPath(),false));
		}
		catch(Exception e){
			e.printStackTrace();
			Log.catching(e);
			throw new Exception("Chrome browser iniatiation failed");
		}
	}
	private void initiateEdge() throws Exception
	{
		try{
			String strEdgeDriver = "";		
			if(System.getProperty("os.name").equalsIgnoreCase("Windows 10"))
			{
				strEdgeDriver = "MicrosoftWebDriver.exe";
			}
			else{
				Log.error("Edge Supports only Windows 10");
				throw new Exception("Edge Supports only Windows 10");
			}
			System.setProperty("webdriver.edge.driver", new File(".").getCanonicalPath() + Config.getEnvDetails("browser.driverpath") + File.separator + strEdgeDriver);
			driver = new EdgeDriver(this.getEdgeOptions());
		}catch(Exception e){
			e.printStackTrace();
			Log.catching(e);
			throw new Exception("Edge browser iniatiation failed");
		}
		
	}
	private void initiateSafari() throws Exception
	{
		try{
			throw new Exception("Safari browser iniatiation not implemented yet");
		}catch(Exception e){
			e.printStackTrace();
			Log.catching(e);
			throw new Exception("Safari browser iniatiation failed");
		}
	}
	private void setBrowserResolution() throws Exception
	{
		try{
			String strBrowserResolution = BuildParams.BROWSER_RESOLUTION;
			if(StringUtils.isEmpty(strBrowserResolution))
				strBrowserResolution=Config.getEnvDetails("browser.resolution");
			if(StringUtils.isEmpty(strBrowserResolution))
				driver.manage().window().maximize();
			else
				driver.manage().window().setSize(new Dimension(Integer.parseInt(strBrowserResolution.split(",")[0]),Integer.parseInt(strBrowserResolution.split(",")[1])));
		}catch(Exception e){
			e.printStackTrace();
			Log.catching(e);
			throw new Exception("Set browser resoltion failed");
		}
	}
	/**
	 * Constructor to get WrappedWebDriver based on specified browser
	 */
	private String getBrowser()
	{
		String strBrowser = BuildParams.BROWSER_NAME;
		if(strBrowser==null || strBrowser.isEmpty()){
			strBrowser = Config.getEnvDetails("browser.browser");
		}
		return strBrowser;	
	}
	private String getDownloadPath()
	{
		Log.debug("Setting the downloadpath variable");
		String strDownloadFilePath = System.getProperty("filedownloadpath");
		if(strDownloadFilePath==null || strDownloadFilePath.isEmpty())
			strDownloadFilePath=Config.getEnvDetails("filedownloadpath");
		if(strDownloadFilePath==null || strDownloadFilePath.isEmpty())
			strDownloadFilePath="C:\\temp";
		return strDownloadFilePath;
	}
	private void initiateGrid()
	{
		Log.debug("Setting the grid_cap_platform variable from property file");
		String grid_cap_platform = Config.getEnvDetails("browser.grid_cap_platform");
		
		Log.debug("Setting the grid_cap_browser_version variable from property file");
		String grid_cap_browser_version = Config.getEnvDetails("browser.grid_cap_browser_version");
		
		Log.debug("Grid it eanbled and running the browser remotely");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//capabilities.setBrowserName(browser);
		capabilities = DesiredCapabilities.chrome();
		if (!grid_cap_platform.isEmpty()) {
			Platform enumval = Platform.valueOf(grid_cap_platform);
			capabilities.setPlatform(enumval);
		}

		if (!grid_cap_browser_version.isEmpty()) {
			capabilities.setVersion(grid_cap_browser_version);
		}

		try {
			String hub_url = "http://127.0.0.1:5357/wd/hub";
			Log.info("Initiating the remote webdriver with hub url:" + hub_url);
			driver = new RemoteWebDriver(new URL(hub_url), capabilities);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				driver = new RemoteWebDriver(DesiredCapabilities.internetExplorer());
			} catch (Exception e1) {
				e1.printStackTrace();
				driver = new RemoteWebDriver(DesiredCapabilities.firefox());
			}
		}
	}
	private boolean isProcessRunning(String serviceName) throws Exception {
		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			//Log.info(line);
			if (line.contains(serviceName)) {
				return true;
			}
		}
		return false;
	}
	private void killProcess(String serviceName) throws Exception {
		if(this.isProcessRunning(serviceName)){
			Process pr = Runtime.getRuntime().exec(KILL + serviceName);
			pr.waitFor();
			pr.destroy();
		}
	}
	private String getBrowserVersion()
	{
		Capabilities caps = ((RemoteWebDriver)driver).getCapabilities();
		return caps.getVersion();
	}
	private  ChromeOptions getChromeOptions(String downloadFilepath,boolean blnIsHeadless)
	{
		ChromeOptions chromeOptions = new ChromeOptions();
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("download.default_directory", downloadFilepath);
		chromePrefs.put("dom.ipc.plugins.enabled.libflashplayer.so", true);
		chromeOptions.setExperimentalOption("prefs", chromePrefs);
		chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
		chromeOptions.setAcceptInsecureCerts(true);
		chromeOptions.addArguments("start-maximized");
		chromeOptions.addArguments("--enable-automation");
		chromeOptions.addArguments("test-type=browser");
		chromeOptions.addArguments("disable-infobars");
		chromeOptions.addArguments("disable-extensions");
		chromeOptions.addArguments("verbose");
		chromeOptions.setExperimentalOption("useAutomationExtension", false);
		chromeOptions.setHeadless(blnIsHeadless);
		/*DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);*/
		return chromeOptions;
	}
	/*private DesiredCapabilities getEmulatedChromeCapabilities(String downloadFilepath, String deviceName) {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", deviceName);
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome().merge(getChromeOptions(downloadFilepath));
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		return desiredCapabilities;
	}*/
	private  FirefoxOptions getFirefoxOptions(String downloadFilepath,boolean blnIsHeadless)
	{
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.panel.shown", false);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.dir", downloadFilepath);
		profile.setPreference("browser.helperApps.neverAsk.openFile",
				"application/octet-stream, application/x-msdownload, application/exe, application/x-exe, application/dos-exe, vms/exe, application/x-winexe, application/msdos-windows, application/x-msdos-program,text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/octet-stream, application/x-msdownload, application/exe, application/x-exe, application/dos-exe, vms/exe, application/x-winexe, application/msdos-windows, application/x-msdos-program,text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		profile.setPreference("browser.download.manager.focusWhenStarting", false);
		profile.setPreference("browser.download.manager.useWindow", false);
		profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		profile.setPreference("browser.download.manager.closeWhenDone", false);
		
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setLogLevel(FirefoxDriverLogLevel.fromLevel(Level.OFF));
		firefoxOptions.setCapability("marionette", true);
		firefoxOptions.setAcceptInsecureCerts(true);
		firefoxOptions.setHeadless(blnIsHeadless);
		firefoxOptions.setProfile(profile);
		return firefoxOptions;
	}
	/*private DesiredCapabilities getEmulatedFirefoxCapabilities(String downloadFilepath, String deviceName) {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", deviceName);

		Map<String, Object> firefoxOptions = new HashMap<String, Object>();
		firefoxOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox().merge(getFirefoxCapabilities(downloadFilepath));
		desiredCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
		return desiredCapabilities;
	}
	private DesiredCapabilities getFirefoxCapabilities(String downloadFilepath) {
		FirefoxOptions firefoxOptions = getFirefoxOptions(downloadFilepath);
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		desiredCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
		return desiredCapabilities;
	}*/
	private InternetExplorerOptions getInternetExplorerOptions()
	{
		InternetExplorerOptions ieOptions = new InternetExplorerOptions();
		ieOptions.introduceFlakinessByIgnoringSecurityDomains();
		ieOptions.requireWindowFocus();
		return ieOptions;
	}
	private EdgeOptions getEdgeOptions()
	{
		EdgeOptions edgeOptions = new EdgeOptions();
		edgeOptions.setPageLoadStrategy("eager");
		return edgeOptions;
	}
	public WebDriver getDriver()
	{
		return driver;
	}
	/*
	 * This method is to close the current browser
	 */
	public void close() {
		driver.close();
	}

	public WebElement findElement(By arg0) {
		return driver.findElement(arg0);
	}

	public List<WebElement> findElements(By arg0) {
		return driver.findElements(arg0);
	}

	public void get(String arg0) {
		driver.get(arg0);
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public String getPageSource() {
		return driver.getPageSource();
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	public Options manage() {
		return driver.manage();
	}

	public Navigation navigate() {
		return driver.navigate();
	}

	public void quit() {
		driver.quit();
	}

	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	public Object trigger(String script) {
		return ((JavascriptExecutor) driver).executeScript(script);
	}

	public Object trigger(String script, WebElement element) {
		return ((JavascriptExecutor) driver).executeScript(script, element);
	}

	/**
	 * Opens a new tab for the given URL
	 * 
	 * @param url
	 *            The URL to
	 * @throws JavaScriptException
	 *             If unable to open tab
	 */
	public void openTab(String url) {
		String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
		// Create Object reference
		Object element = ((JavascriptExecutor) driver).executeScript(String.format(script, url));
		if (element instanceof WebElement) {
			WebElement anchor = (WebElement) element;
			anchor.click();
			((JavascriptExecutor) driver).executeScript("var a=arguments[0];a.parentNode.removeChild(a);", anchor,
					driver);

		} else {
			// throw new JavaScriptException(element, "Unable to open tab", 1);
		}
	}

	public Actions actions() {
		Actions builder = new Actions(driver);
		return builder;
	}

	public Keyboard getKeyboard() {
		HasInputDevices hasInputDevice = null;
		hasInputDevice = (HasInputDevices) getAugmentedDriver(driver);
		return hasInputDevice.getKeyboard();
	}

	public Mouse getMouse() {
		HasInputDevices hasInputDevice = null;
		hasInputDevice = (HasInputDevices) getAugmentedDriver(driver);
		return hasInputDevice.getMouse();
	}

	public Capabilities getCapabilities() {
		HasCapabilities hasCapabilities = null;
		hasCapabilities = (HasCapabilities) getAugmentedDriver(driver);
		return hasCapabilities.getCapabilities();
	}

	public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
		TakesScreenshot ts = null;
		ts = (TakesScreenshot) getAugmentedDriver(driver);
		return ts.getScreenshotAs(arg0);
	}
	
	protected WebDriver getAugmentedDriver(WebDriver driver) {
		// If the driver is instance of RemoteWebDriver return augmented driver
		if (driver.getClass().equals(RemoteWebDriver.class)) {
			Augmenter augmenter = new Augmenter();
			driver = augmenter.augment(driver);
		} else {
			driver = this.driver;
		}
		return driver;
	}

	public Object executeAsyncScript(String arg0, Object... arg1) {
		return ((RemoteWebDriver) driver).executeAsyncScript(arg0, arg1);
	}

	public Object executeScript(String arg0, Object... arg1) {
		return ((RemoteWebDriver) driver).executeScript(arg0, arg1);
	}

	public WebElement findElementById(String arg0) {
		return ((RemoteWebDriver) driver).findElementById(arg0);
	}

	public List<WebElement> findElementsById(String arg0) {
		return ((RemoteWebDriver) driver).findElementsById(arg0);
	}

	public WebElement findElementByClassName(String arg0) {
		return ((RemoteWebDriver) driver).findElementByClassName(arg0);
	}

	public List<WebElement> findElementsByClassName(String arg0) {
		return ((RemoteWebDriver) driver).findElementsByClassName(arg0);
	}

	public WebElement findElementByLinkText(String arg0) {
		return ((RemoteWebDriver) driver).findElementByLinkText(arg0);
	}

	public WebElement findElementByPartialLinkText(String arg0) {
		return ((RemoteWebDriver) driver).findElementByPartialLinkText(arg0);
	}

	public List<WebElement> findElementsByLinkText(String arg0) {
		return ((RemoteWebDriver) driver).findElementsByLinkText(arg0);
	}

	public List<WebElement> findElementsByPartialLinkText(String arg0) {
		return ((RemoteWebDriver) driver).findElementsByPartialLinkText(arg0);
	}

	public WebElement findElementByName(String arg0) {
		return ((RemoteWebDriver) driver).findElementByName(arg0);
	}

	public List<WebElement> findElementsByName(String arg0) {
		return ((RemoteWebDriver) driver).findElementsByName(arg0);
	}

	public WebElement findElementByCssSelector(String arg0) {
		return ((RemoteWebDriver) driver).findElementByCssSelector(arg0);
	}

	public List<WebElement> findElementsByCssSelector(String arg0) {
		return ((RemoteWebDriver) driver).findElementsByCssSelector(arg0);
	}

	public WebElement findElementByTagName(String arg0) {
		return ((RemoteWebDriver) driver).findElementByTagName(arg0);
	}

	public List<WebElement> findElementsByTagName(String arg0) {
		return ((RemoteWebDriver) driver).findElementsByTagName(arg0);
	}

	public WebElement findElementByXPath(String arg0) {
		return ((RemoteWebDriver) driver).findElementByXPath(arg0);
	}

	public List<WebElement> findElementsByXPath(String arg0) {
		return ((RemoteWebDriver) driver).findElementsByXPath(arg0);
	}

	public void killDriverProcess() throws Exception
	{
		String browser = this.getBrowser();
		if (browser.equalsIgnoreCase("internet explorer") || browser.equalsIgnoreCase("ie")) 
		{
			if (this.getBrowserBits().equals("64") && this.getOsPlatform().equalsIgnoreCase("windows")) 
				this.killProcess("IEDriverServer.exe");
			else if (this.getBrowserBits().equals("32") && this.getOsPlatform().equalsIgnoreCase("windows")) 
				this.killProcess("IEDriverServer32.exe");
		}
		else if(browser.equalsIgnoreCase("firefox"))
		{
			if (this.getBrowserBits().equals("64") && this.getOsPlatform().equalsIgnoreCase("windows")) 
				this.killProcess("geckodriverwin64.exe");
			else if (this.getBrowserBits().equals("32") && this.getOsPlatform().equalsIgnoreCase("windows")) 
				this.killProcess("geckodriverwin32.exe");
		}
		else if (browser.equalsIgnoreCase("chrome")) 
		{
			this.killProcess("chromedriver.exe");
		}
		else if (browser.equalsIgnoreCase("Edge")) 
		{
			this.killProcess("MicrosoftWebDriver.exe");
		}
		else if (browser.equalsIgnoreCase("safari")) 
		{
			this.killProcess("safari.exe");
		}
	}
	
}
