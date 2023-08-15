package com.weichertwm.qa.framework;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.weichertwm.qa.framework.ExtentReport;
import com.weichertwm.qa.framework.Status;

public class Listener implements ITestListener {
	WrappedWebDriver driver=null;

	@Override
	public synchronized void onTestStart(ITestResult result) {	
	}
	
	/**
	 * This is a Listener method which will be automatically executed once the test status changes 
	 */
	@Override
	public synchronized void onTestFailure(ITestResult result) {
		this.driver = ((TestCase)result.getInstance()).driver;
		//Log.catching(result.getThrowable());
		ExtentReport.log(result.getThrowable().getMessage(),Status.FAIL,driver);
		ExtentReport.log("Test "+result.getTestClass().getRealClass().getSimpleName()+" has FAILED",Status.FAIL,driver);	
	}
	
	/**
	 * This is a Listener method which will be automatically executed once the test gets passed. 
	 */
	@Override
	public synchronized void onTestSuccess(ITestResult result) {
		this.driver = ((TestCase)result.getInstance()).driver;
		ExtentReport.log("Test "+result.getTestClass().getRealClass().getSimpleName()+" has PASSED",Status.PASS,driver);
	}
	
	@Override
	public synchronized void onFinish(ITestContext arg0) {
				
	}

	@Override
	public synchronized void onStart(ITestContext arg0) {
		
	}

	@Override
	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
				
	}

	

	@Override
	public synchronized void onTestSkipped(ITestResult arg0) {
				
	}

}
