package com.weichertwm.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.weichertwm.qa.framework.BuildParams;
import com.weichertwm.qa.framework.ExtentReport;
import com.weichertwm.qa.framework.FrameworkException;
import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.PageObject;

public class LoginPage extends PageObject {

	public LoginPage(WebDriver pDriver) {
		super(pDriver);
	}

	By txtUserName = By.xpath("//*[@id='userName']");
	By txtPassword = By.xpath("//*[@id='password']");
	By loginBtn = By.xpath("//*[@value='Submit']");
	By userMenu = By.xpath("//a[text()='Home']");
	
	/**
	 * Method used to login into the application
	 * 
	 * @throws Exception
	 */
	public void loginToApp() throws Exception {
		try {
			waitForElementPresent(txtUserName, 60);
			clearAndSendKeys(txtUserName, BuildParams.USER_NAME);
			clearAndSendKeys(txtPassword, BuildParams.USER_PASSWORD);
			waitFor(3000);
			click(loginBtn);
			waitUntilPageReadyStateComplete(120);	
			
			if (isElementPresent(userMenu)) {
				Log.info("Login successful");
				ExtentReport.logPass("User logged into WeichertWM application successfully");
			} else { 
				Log.info("Login failed"); 
				ExtentReport.logFail("User not logged into WeichertWM application successfully"); 
			}
		} catch (Exception e) {
			ExtentReport.logFail("loginToApp failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());
		}
	}
}
