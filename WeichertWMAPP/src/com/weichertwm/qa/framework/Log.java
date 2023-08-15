package com.weichertwm.qa.framework;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.*;

/**
 * @references		
 * 		https://memorynotfound.com/log4j2-with-log4j2-xml-configuration-example/
 * 		https://logging.apache.org/log4j/2.0/faq.html
 * 
 * @author RJAWALE
 *
 */
public class Log {
	private static Logger logger;
	public static void logInit()
	{
		String folderpath = ExtentReport.resultsPath;
		/*if(ExtentReport.resultsPath==null)
		{
			ExtentReport.createReportFolder();
			folderpath = ExtentReport.resultsPath;
		}*/
		String file = "";
		DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
		Calendar cal1 = Calendar.getInstance();
		file = "Log_"+dateFormat.format(cal1.getTime());
		// Logger Configuration
		System.setProperty("log4j.folder", folderpath);
		System.setProperty("log4j.Title", file);
		PropertyConfigurator.configure(System.getProperty("user.dir")+"//resources//inputs//log4j.properties");
		//Initialize Log4j logs
		//logger = LogManager.getLogger(Logger.class.getName());
		logger =Logger.getLogger("RnA2");
		logger.setLevel(Level.toLevel(Config.getEnvDetails("log.level")));
	}
	// Need to create these methods, so that they can be called  
	public static void info(String message) {
		if(logger==null)
			logInit();
		logger.info(message);
	}
	public static void warn(String message) {
		if(logger==null)
			logInit();
		logger.warn(message);
	}
	public static void error(String message) {
		if(logger==null)
			logInit();
		logger.error(message);
	}
	public static void fatal(String message) {
		if(logger==null)
			logInit();
		logger.fatal(message);
	}
	public static void debug(String message) {
		if(logger==null)
			logInit();
		logger.debug(message);
	}
	public static void trace(String message) {
		if(logger==null)
			logInit();
		logger.trace(message);
	}
	public static void printHaltMessage() {
		logger.error("----------------------------------------------------------------------");
		logger.error("- 			          Halting execution		   					   -");
		logger.error("----------------------------------------------------------------------");
	}
	public static void catching(Exception e) {
		Log.error(e.getMessage());
	}
	public static void catchExceptionAndStop(Exception e) {
		Log.error(e.getMessage());
	}
	public static boolean isTraceEnabled() {
		return true;
	}

	
}