package com.weichertwm.qa.framework;

public class BuildParams {

	public static final String SUITE_NAME = setSuiteName();
	public static final String PORTAL_APP_URL = setPortalAppUrl();
	public static final String TESTCASES = setTestCases();
	public static final String OS_PLATFORM = setPlatform();
	public static String BUILD_NUMBER = setBuildNumber();
	/**
	 * Browser keys
	 */
	public static final String BROWSER_NAME = setBrowser();
	public static final String BROWSER_BITS = setBrowserBits();
	public static final String BROWSER_RESOLUTION = setBrowserResolution();
	
	/**
	 * Report Mail keys
	 */
	public static final String IS_SEND_MAIL = setIsSendMail();
	public static final String MAIL_TO = setMailTo();
	public static final String REPORT_INTO_DB = setReportIntoDB();
	
	public static String USER_NAME = setUserName();
	public static String USER_PASSWORD = setPassword();
	
	
	
	private static String setPortalAppUrl() {
		if(System.getProperty("portalurl")==null || System.getProperty("portalurl").isEmpty()) {
			return Config.getEnvDetails("portal.aut.url");
		}
		else {
			return System.getProperty("portalurl");
		}
	}
	
	private static String setUserName() {
		if(System.getProperty("username")==null || System.getProperty("username").isEmpty()) {
			return Config.getEnvDetails("app.userName");
		}
		else {
			return System.getProperty("username");
		}
	}
	private static String setPassword() {
		if(System.getProperty("password")==null || System.getProperty("password").isEmpty()) {
			return Config.getEnvDetails("app.password");
		}
		else {
			return System.getProperty("password");
		}
	}
	
	
	private static String setIsSendMail() {
		if (System.getProperty("sendmail") == null || System.getProperty("sendmail").isEmpty())
			return Config.getEnvDetails("report.mail.sent");
		else
			return System.getProperty("sendmail");
	}
	private static String setMailTo() {
		if (System.getProperty("mailto") == null || System.getProperty("mailto").isEmpty())
			return Config.getEnvDetails("report.mail.to");
		else
			return System.getProperty("mailto");
	}
	
	private static String setBrowser() {
		if (System.getProperty("browser") == null || System.getProperty("browser").isEmpty())
			return Config.getEnvDetails("browser.browser");
		else
			return System.getProperty("browser");
	}
	private static String setBrowserBits() {
		if (System.getProperty("browserbits") == null || System.getProperty("browserbits").isEmpty())
			return Config.getEnvDetails("browser.bits");
		else
			return System.getProperty("browserbits");
	}
	private static String setReportIntoDB() {
		if (System.getProperty("reportintodb") == null || System.getProperty("reportintodb").isEmpty())
			return Config.getEnvDetails("report.db.insert");
		else
			return System.getProperty("reportintodb");
	}
	private static String setBrowserResolution() {
		if (System.getProperty("resolution") == null || System.getProperty("resolution").isEmpty())
			return Config.getEnvDetails("browser.resolution");
		else
			return System.getProperty("resolution");
	}
	private static String setSuiteName() {
		return System.getProperty("suite");
	}
	private static String setTestCases() {
		return System.getProperty("tests");
	}
	private static String setPlatform() {
		if (System.getProperty("platform") == null || System.getProperty("platform").isEmpty())
			return Config.getEnvDetails("browser.platform");
		else
			return System.getProperty("platform");
	}
	private static String setBuildNumber() {
		if (System.getProperty("build") == null || System.getProperty("build").isEmpty())
			return Config.getEnvDetails("aut.build");
		else
			return System.getProperty("build");
	}
	
	
}
