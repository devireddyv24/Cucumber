package com.weichertwm.qa.framework;

import java.util.Random;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.weichertwm.qa.framework.ExtentReport;
import com.weichertwm.qa.framework.FrameworkException;
import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.PageObject;

public class CommonPage extends PageObject {

	public CommonPage(WebDriver pDriver) {
		super(pDriver);
	}

	By userMenu = By.xpath("//*[@id='usr-menu']");
	By lnkLogout = By.xpath("//*[@href='/login/logout.php']");
	By txtWelcome = By.xpath("//h2[text()='Welcome']");
	 
	By ChangeDivisionsettings= By.xpath("//*[text()='Change Division']");
	By ChangeDivisiondropdown=By.xpath("//*[@id='division_id']");
	By SelectDivision=By.xpath("//*[@title='Change your session to the selected division.']");
	
	By ConstructionScheduling = By.xpath("//*[text()=' Construction Scheduling']");
	By save = By.xpath("//*[@accesskey='s']");
//	By RefreshReport = By.xpath("//*[@title='Refresh/Reload this current report']");
//	By ReviseReport = By.xpath("//*[@title='Change report parameters and run again']");
	
	By RefreshReport = By.xpath("//*[@accesskey='f']");
	By ReviseReport = By.xpath("//*[@accesskey='r']");
	
	By Quit = By.xpath("//*[@value='(Q)uit']");
	By viewschedules = By.xpath("//*[@title='Run report']");
	By template_name = By.xpath("//*[@name='template_name']");
	By Savetemplate = By.xpath("//*[@value='Save Template']");
	By Templatecreatemsg = By.xpath("//*[@id='display_message']");
	By application_template_to_use = By.xpath("//*[@id='application_template_to_use']");
	By viewinPrintMode = By.xpath("//*[@title='Run report in print mode']");
	
	By PrintReport_ExcatText = By.xpath("//*[text()='Print Report']");
	By PrintReport_ContainsValue = By.xpath("//*[@value='Print Report']");
	
	By sendemail = By.xpath("//*[@name='send_email_to']");
	By Emailformat = By.xpath("//*[@name='email_attachment_format']");
	By Emailreport = By.xpath("//*[@value='(E)mail Report']");
	By emailconfrimmsg = By.xpath("//*[contains(@class,'warningmsg')]");
	
	By lotcsinformation = By.xpath("//*[@title='Update construction scheduling information for a lot/unit(s)']");
	By selectfiledtoupdate = By.xpath("//*[@name='additional_fields']");
	By lot_id_list = By.xpath("//*[@id='lot_id_list']");
	By Search = By.xpath("//*[@value='(S)earch']");
	By schedulesstatusdetail = By.xpath("//*[@title='View schedule status details for specific project(s) or lot(s)']");
	By updatedmsg = By.xpath("//*[contains(@class,'informationmsg')]");
	
	/**
     * Method:logoutApp Description: Logs out from the application
     * 
     * @throws Exception
     */
    public void logoutApp() throws Exception {
        try {
            waitForElementPresent(userMenu, 30);
            waitFor(2000);
            click(userMenu);
            waitForElementPresent(lnkLogout, 30);
            click(lnkLogout);
            waitUntilPageReadyStateComplete(50);
            waitForElementPresent(txtWelcome, 30);
            if (isElementPresent(txtWelcome)) {
                Log.info("Application logout successfully");
                ExtentReport.logPass("Application logout successfully");
            } else {
                ExtentReport.logFail("logout failed ");
                throw new FrameworkException("logout failed ");
            }
        } catch (Exception e) {
            ExtentReport.logFail("logoutApp failed - " + e.getMessage());
            throw new FrameworkException(e.getMessage());
        }
    }
    /**
     * Method: click On Construction Scheduling
     * 
     * @throws Exception
     */    

    public void clickOnConstructionScheduling() throws Exception {
		try {
			waitForElementPresent(ConstructionScheduling, 60);
			click(ConstructionScheduling);
			
		} catch (Exception e) {
			ExtentReport.logFail("clickOnConstructionScheduling failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}	
    /**
     * Method: click On Quit
     * 
     * @throws Exception
     */    
	public void clickOnQuit() throws Exception {
		try {
			waitForElementPresent(Quit, 60);
			click(Quit);
			
		} catch (Exception e) {
			ExtentReport.logFail("clickOnQuit failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}	
	/**
     * Method: click On View Schedules
     * 
     * @throws Exception
     */    
	public void clickOnViewSchedules() throws Exception {
		try {
			waitForElementPresent(viewschedules, 60);
			click(viewschedules);
			
		} catch (Exception e) {
			ExtentReport.logFail("clickOnViewSchedules failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}	
	/**
     * Method: click On Refresh Report
     * 
     * @throws Exception
     */    
	public void clickOnRefreshReport() throws Exception {
		try {
			waitForElementPresent(RefreshReport, 60);
			click(RefreshReport);
			
		} catch (Exception e) {
			ExtentReport.logFail("clickOnRefreshReport failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}
	/**
     * Method: click On Revise Report
     * 
     * @throws Exception
     */    
	public void clickOnReviseReport() throws Exception {
		try {
			waitForElementPresent(ReviseReport, 60);
			click(ReviseReport);
			
		} catch (Exception e) {
			ExtentReport.logFail("clickOnReviseReport failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}	
	/**
     * Method: click On Save
     * 
     * @throws Exception
     */    
	public void clickOnSave() throws Exception {
		try {
			waitForElementPresent(save, 60);
			click(save);
			
		} catch (Exception e) {
			ExtentReport.logFail("clickOnSave failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}	
	/**
     * Method: click On View Print Mode With Excat Text and Verify Print Report
     * 
     * @throws Exception
     */    
	public void detailInPrintMode_ExcatText() throws Exception {
		try {
			click(viewinPrintMode);
			String pw = driver.getWindowHandle();
			Set<String> windows = driver.getWindowHandles();
			for(String window : windows)
			{
				driver.switchTo().window(window);		
				}
			if(isElementPresent(PrintReport_ExcatText)) {
				Log.info("Report is generated in print mode");
				ExtentReport.logPass("Report is generated in print mode");
			} else {
				ExtentReport.logFail("Report is not generated in print mode");
				throw new FrameworkException("Report is not generated in print mode");
			}
			driver.close();
			driver.switchTo().window(pw);
			waitFor(2000);	
		} catch (Exception e) {
			ExtentReport.logFail("detailInPrintMode_ExcatText failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}
	/**
     * Method: click On View Print Mode Contains Value and Verify Print Report
     * 
     * @throws Exception
     */    
	public void detailInPrintMode_ContainsValue() throws Exception {
		try {
			click(viewinPrintMode);
			String pw = driver.getWindowHandle();
			Set<String> windows = driver.getWindowHandles();
			for(String window : windows)
			{
				driver.switchTo().window(window);		
				}
			if(isElementPresent(PrintReport_ContainsValue)) {
				Log.info("Report is generated in print mode");
				ExtentReport.logPass("Report is generated in print mode");
			} else {
				ExtentReport.logFail("Report is not generated in print mode");
				throw new FrameworkException("Report is not generated in print mode");
			}
			driver.close();
			driver.switchTo().window(pw);
			waitFor(2000);	
		} catch (Exception e) {
			ExtentReport.logFail("detailInPrintMode failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}
	/**
     * Method: To create Template or Update Template
     * 
     * @throws Exception
     */    
	public void templateCreation() throws Exception {
		try {
			waitForElementPresent(template_name, 60);
			clearAndSendKeys(template_name, "Test Template1");
			click(Savetemplate);
			if(isElementPresent(Templatecreatemsg)) {
				Log.info("Template created successfully");
				ExtentReport.logPass("Template created successfully");
			} else {
				ExtentReport.logFail("Template not created successfully");
				throw new FrameworkException("Template not created successfully");
			}
			click(application_template_to_use);
			selectByValue(application_template_to_use, "Test+Template1|20667");
			waitFor(2000);	
		} catch (Exception e) {
			ExtentReport.logFail("templateCreation failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}	
	/**
     * Method: Verify Email is Sent  In HTML Format or Not
     * 
     * @throws Exception
     */    
	public void sendEmailInHTMLFormat() throws Exception {
		try {
			click(Emailformat);			
			selectByValue(Emailformat, "HTML");
			waitForElementPresent(sendemail, 60);
			clearAndSendKeys(sendemail, "testreport612@gmail.com");
			click(Emailreport);
			waitFor(5000);
			if(isElementPresent(emailconfrimmsg)) {
				Log.info("The report has been sent on configured email in HTML format:");
				ExtentReport.logPass("The report has been sent on configured email in HTML format:");
			} else {
				ExtentReport.logFail("The report has been not sent on configured email in HTML format:");
				throw new FrameworkException("The report has been not sent on configured email in HTML format:");
			}	
		} catch (Exception e) {
			ExtentReport.logFail("sendEmailInHTMLFormat failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}	
	/**
     * Method: Verify Email is Sent  In CSV Format or Not
     * 
     * @throws Exception
     */    
	public void sendEmailInCSVFormat() throws Exception {
		try {
			click(Emailformat);			
			selectByValue(Emailformat, "CSV");
			waitForElementPresent(sendemail, 60);
			clearAndSendKeys(sendemail, "testreport612@gmail.com");
			click(Emailreport);
			waitFor(5000);
			if(isElementPresent(emailconfrimmsg)) {
				Log.info("The report has been sent on configured email in CSV format:");
				ExtentReport.logPass("The report has been sent on configured email in CSV format:");
			} else {
				ExtentReport.logFail("The report has been not sent on configured email in CSV format:");
				throw new FrameworkException("The report has been not sent on configured email in HTML format:");
			}	
		} catch (Exception e) {
			ExtentReport.logFail("sendEmailInCSVFormat failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}
	/**
     * Method: change Division To AtlantaTEST
     * 
     * @throws Exception
     */    
	public void changeDivisionToAtlantaTEST() throws Exception {
		try {
			waitForElementPresent(userMenu, 60);
            click(userMenu);
			waitForElementPresent(ChangeDivisionsettings, 60);
			click(ChangeDivisionsettings);
			if (isElementPresent(ChangeDivisiondropdown)) {
				Log.info("Division Changed successful");
				ExtentReport.logPass("Division Changed successful");
			} else {
				ExtentReport.logFail("Division Change failed");
				throw new FrameworkException("Division Change failed");
			}	
			selectByVisibleText(ChangeDivisiondropdown,"Atlanta - TEST");
			click(SelectDivision);
			waitFor(3000);
			
		} catch (Exception e) {
			ExtentReport.logFail("changeDivisionToAtlantaTEST failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}	
	/**
     * Method: change Division To HoustonTEST
     * 
     * @throws Exception
     */    
	public void changeDivisionToHoustonTEST() throws Exception {
		try {
			waitForElementPresent(userMenu, 60);
			waitForElementPresent(ChangeDivisionsettings, 60);
			click(ChangeDivisionsettings);
			if (isElementPresent(ChangeDivisiondropdown)) {
				Log.info("Division Changed successful");
				ExtentReport.logPass("Division Changed successful");
			} else {
				ExtentReport.logFail("Division Change failed");
				throw new FrameworkException("Division Change failed");
			}	
			selectByVisibleText(ChangeDivisiondropdown,"Houston - TEST");
			click(SelectDivision);
			waitFor(3000);
			
		} catch (Exception e) {
			ExtentReport.logFail("changeDivisionToHoustonTEST failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}
	/**
     * Method: lot CS Information_Existing Lots
     * 
     * @throws Exception
     */    
	public void lotCsInformation_ExistingLots() throws Exception {
		try {
			click(lotcsinformation);
			waitForElementPresent(lot_id_list, 60);
			clearAndSendKeys(lot_id_list, "127790,127859");
			waitFor(2000);
			selectByVisibleText(selectfiledtoupdate, "Partial Lot Control");
			waitFor(2000);
			click(Search);
			waitFor(2000);
			
		} catch (Exception e) {
			ExtentReport.logFail("lotCsInformation_ExistingLots failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}	
	/**
     * Method: Verify the Updated Message
     * 
     * @throws Exception
     */    
	public void updatedMessage() throws Exception {
		try {
		if(isElementPresent(updatedmsg)) {
			Log.info("Successfully updated");
			ExtentReport.logPass("Successfully updated");
		} else {
			ExtentReport.logFail("Not updated Successfully");
			throw new FrameworkException("Not updated Successfully");
		}
	  } catch (Exception e) {
		ExtentReport.logFail("updatedMessage failed - " + e.getMessage());
		throw new FrameworkException(e.getMessage());	
	  }
	}
	/**
     * Method: schedules Status Detail
     * 
     * @throws Exception
     */    
	public void schedulesStatusDetail() throws Exception {
		try {
			waitFor(3000);
			click(schedulesstatusdetail);
			waitForElementPresent(lot_id_list, 60);
			clearAndSendKeys(lot_id_list, "127790,127859");
		} catch (Exception e) {
			ExtentReport.logFail("schedulesStatusDetail failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}
	}
	/**
     * Method: Get random string
     * 
     * @throws Exception
     */    
	public String getRandomString(final int len) {
		final String abc = "abcdefghijklmnopqrstuvwxyz"; 
		final Random rnd = new Random();
		final StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(abc.charAt(rnd.nextInt(abc.length())));
		}
		return sb.toString();
	}
	/**
     * Method: Get random numbers
     * 
     * @throws Exception
     */   
	public String getRandomNumber(final int len) {
        final String abc = "0123456789";
        final Random rnd = new Random();
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(abc.charAt(rnd.nextInt(abc.length())));
        }
        return sb.toString();
    }
	
}