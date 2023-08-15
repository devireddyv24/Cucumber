package com.weichertwm.qa.pages;

import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.internal.AbstractParallelWorker.Arguments;

import com.weichertwm.qa.framework.BuildParams;
import com.weichertwm.qa.framework.ExtentReport;
import com.weichertwm.qa.framework.FrameworkException;
import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.PageObject;
import com.weichertwm.qa.util.Screenshot;

public class WCWM_Existing_Customer_Page extends PageObject{
	
	public WCWM_Existing_Customer_Page(WebDriver pDriver) {
		super(pDriver);
	}
	
	
	//Initiate locater
	
	By Homelink=By.xpath("//a[text()='Home']");
	By  Initiate_New_Move=By.xpath("//div[text()='Initiate New Move']");
	//By search=By.xpath("//input[@placeholder='Search for existing employee by Name, Email or Employee ID. Type at least two characters and press enter.']");
	By search=By.xpath("//div/input[@placeholder]");
	By checkbox=By.xpath("//span[@class='slds-radio_faux']");
	By nextbutton=By.xpath("//button[text()='Next']");
	By employeeTxt = By.xpath("//div[contains(text(),'Employee Contact')]");
	By select_Home_owner=By.xpath("//select[@class='slds-select']");
	By Long_terms=By.xpath("//*[@placeholder='Search below records by policy name']");
	
	By Long_Term_Radio = By.xpath("//span[@class='slds-radio_faux']");
	
	By add_addresses = By.xpath("//div[contains(text(), 'Add Address(s)')]");
	
	By departurecopy=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[2]/td[9]/div");
	By Originationcopy=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[3]/td[9]/div");
	By destination_work_country=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[3]/div/lightning-combobox");
	By usa=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]//span[text()='United States']");
	By destination_work_state=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[4]/div/lightning-combobox");
	By Florida=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]//span[text()='Florida']");
	By destination_work_City=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[5]/div/lightning-combobox");
	By Miami=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]//span[text()='Miami']");
	By destination_work_Zip=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[6]/div/lightning-combobox");
	By Zip=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]//span[text()='33102']");
	By destination_work_Street=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[8]/div");
	By destination_work_Street_enter=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[8]/div//input[@class='slds-input']");
	By save_nextbutton=By.xpath("//button[text()='Save and Next']");
	By authorization_Scope_bussines=By.xpath("//button[@name='Authorization_Scope_of_Business__c']");
	By full_authorization=By.xpath("//span[text()='Full Authorization']");
	By estimated_Move_Start_date=By.xpath("//input[@name='Estimated_Move_Start_Date__c']");	
	By estimated_Move_end_date=By.xpath("//input[@name='Estimated_Move_End_Date__c']");
	By newAnnualSalary=By.xpath("//input[@name='New_Annual_Salary__c']");
	
	By new_Annual_Salary_Currency=By.xpath("//button[@name='New_Annual_Salary_Currency__c']");
	By unitedStateDollar=By.xpath("//span[text()='United States Dollar - USD']");
	
	By new_Annual_Salary_Effective_Date=By.xpath("//input[@name='New_Annual_Salary_Effective_Date__c']");
	
	By newJobTitle=By.xpath("//input[@name='New_Job_Title__c']");
	
	By home_Housing=By.xpath("//button[@name='Home_Housing__c']");	
	
	By company_Owned=By.xpath("//span[text()='Company Owned']");
	
	By employee_Inf_checkBox=By.xpath("//input[@name='Home_Payroll_ID_is_same_as_Employee_ID__c']");
	By taxFillingTextBox=By.xpath("//button[@name='Tax_Filing_Status__c']");
	
	By select_married=By.xpath("//span[text()='2-Married']");
	
	By empolymentType=By.xpath("//button[@name='Employment_Type__c']");
	By select_CurrentEmployee=By.xpath("//span[text()='Current Employee']");
	By job_grade=By.xpath("//button[@name='Test_Job_Grade__c']");
	By grade=By.xpath("//span[text()='A']"); 
	By test_company_code=By.xpath("//button[@name='Test_Company_Code__c']");
	By company_code=By.xpath("//span[text()='001']");
	By costcenter=By.xpath("//input[@name='Cost_Center__c']");
	By managing_director=By.xpath("//input[@name='Managing_Director__c']");
	By testcrop_data=By.xpath("//input[@name='Test_Corp_Destination_Office__c']");
	By test_crop_center=By.xpath("//input[@name='Test_Corp_Cost_Center__c']");
	By test_crop_gl=By.xpath("//input[@name='Test_Corp_GL_Account__c']");
	By submit_authorization=By.xpath("//*[@title='Submit Authorization to Weichert']");
	
	/**
	 * Method used to verify data move for Existing Customer
	 * @throws Exception
	 */
	public void ExistingCustomer() throws Exception {
		
	try {
		ExtentReport.logPass("Navigated to Home page");
		click(Homelink);
		String name=driver.getTitle();
		Screenshot sh=new Screenshot();
		sh.screenshot(driver, name);
		waitFor(20);
		click(Initiate_New_Move);
		ExtentReport.logPass("Clicked on Initiate Move button to navigate to Search Employee section");
		waitFor(200);
		Thread.sleep(4000);
		click(search);
		Thread.sleep(4000);
		clearAndSendKeys(search, "Enhops1");
		
		name=driver.getTitle();
		sh.screenshot(driver, name);
		
		keysclass(search, Keys.ENTER);
		ExtentReport.logPass("Searched for Existing customer");
		waitFor(20);
		click(checkbox);
		waitFor(20);
		click(nextbutton);
		ExtentReport.logPass("Clicked on Next button to navigate Employee contact page");
		
		waitFor(20);
		click(nextbutton);
		ExtentReport.logPass("Clicked on Next button to navigate Client Policy page");
		
		waitFor(200);
		selectByValue(select_Home_owner, "Home Owner");
		name=driver.getTitle();
		sh.screenshot(driver, name);
		//Domicile Type Selection		
		click(Long_terms);
		clearAndSendKeys(Long_terms, "Long Term Assignment");		
		waitFor(200);
		click(Long_Term_Radio);		
		click(nextbutton);		
		ExtentReport.logPass("Clicked on Next button to navigate Add Address page");
		Thread.sleep(3000);
		
		waitFor(900);
		
		name=driver.getTitle();
		sh.screenshot(driver, name);
		mouseHover(departurecopy);
		Thread.sleep(9000);
		click(departurecopy);
		waitFor(200);
		Thread.sleep(9000);
		click(Originationcopy);
		Thread.sleep(6000);
		click(destination_work_country);
		waitFor(200);
		click(usa);
		Thread.sleep(1000);
		click(destination_work_state);
		waitFor(200);
		click(Florida);
		Thread.sleep(1000);
		click(destination_work_City);
		waitFor(200);
		click(Miami);
		Thread.sleep(1000);
		click(destination_work_Zip);
		waitFor(200);
		click(Zip);
		waitFor(200);
		Thread.sleep(1000);
		click(destination_work_Street);
		sendKeys(destination_work_Street_enter, "1 Street");		
		waitFor(200);
		click(nextbutton);
		waitFor(200);
		click(nextbutton);
		
		waitFor(200);
		name=driver.getTitle();
		sh.screenshot(driver, name);
		ExtentReport.logPass("Clicked on Next button to navigate Move Details page");
		click(authorization_Scope_bussines);
		waitFor(20);
		click(full_authorization);
		waitFor(20);

		sendKeys(estimated_Move_Start_date, "Aug 4, 2023");
		waitFor(20);

		sendKeys(estimated_Move_end_date, "Oct 4, 2023");
		waitFor(20);

		clearAndSendKeys(newAnnualSalary, "150,000");
		waitFor(20);

		click(new_Annual_Salary_Currency);

		waitFor(20);

		click(unitedStateDollar);
		waitFor(20);
		click(new_Annual_Salary_Effective_Date);
		waitFor(20);

		sendKeys(new_Annual_Salary_Effective_Date, "Aug 4, 2023");
		waitFor(20);

		clearAndSendKeys(newJobTitle, "New Job");
		waitFor(20);
		click(home_Housing);
		waitFor(20);
		click(company_Owned);
		waitFor(20);
		click(employee_Inf_checkBox);
		waitFor(20);

		click(taxFillingTextBox);
		waitFor(20);

		click(select_married);
		waitFor(20);

		click(empolymentType);
		waitFor(20);

		click(select_CurrentEmployee);
		waitFor(20);

		click(job_grade);
		waitFor(20);

		click(grade);
		waitFor(20);

		click(test_company_code);
		waitFor(20);

		click(company_code);
		waitFor(20);
/*
		sendKeys(costcenter, "99");
		waitFor(10);
		sendKeys(managing_director, "Managing Director");
		waitFor(10);
		sendKeys(testcrop_data, "New Jersey");
		waitFor(10);
		sendKeys(test_crop_center, "001");
		waitFor(10);
		sendKeys(test_crop_gl, "296");
		waitFor(10); */
		Thread.sleep(3000);
		click(save_nextbutton);
		ExtentReport.logPass("Clicked on Save and Next button to save and navigate review authorization page");
		
		Thread.sleep(20000);
		name=driver.getTitle();
		sh.screenshot(driver, name);
		click(submit_authorization);
		ExtentReport.logPass("Clicked on Submit Authorization button to complete authorization and nvaigate to home page");
		Thread.sleep(20000);
	} catch (Exception e) {
		ExtentReport.logFail("ExistingCustomer failed - " + e.getMessage());
		throw new FrameworkException(e.getMessage());	
	}
  }	
}

