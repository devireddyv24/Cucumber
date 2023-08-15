package com.weichertwm.qa.pages;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;
import com.weichertwm.qa.framework.ExtentReport;
import com.weichertwm.qa.framework.FrameworkException;
import com.weichertwm.qa.framework.PageObject;
import com.weichertwm.qa.util.Screenshot;

import net.bytebuddy.utility.RandomString;

public class WCWM_New_Customer_Page extends PageObject{
	public WebDriver driver;
	public WCWM_New_Customer_Page(WebDriver pDriver) {
		super(pDriver);
		this.driver=pDriver;
		PageFactory.initElements(pDriver,this);
	}
	//Initiate locater
	
	By Homelink=By.xpath("//a[text()='Home']");
	By  Initiate_New_Move=By.xpath("//div[text()='Initiate New Move']");
	By nextbutton=By.xpath("//button[text()='Next']");
	By first_name=By.xpath("//input[@name='FirstName']");
	By lastname=By.xpath("//input[@name='LastName']");
	By email=By.xpath("//input[@name='Email']");
	By personalmail=By.xpath("//input[@name='Personal_Email_Address__c']");
	By preferd_method=By.xpath("//button[@name='Preferred_Method_of_Contact__c']");
	By preferd_method_select=By.xpath("//span[text()='Work Email']");
	By emplyid=By.xpath("//input[@name='Employee_ID__c']");
	By country=By.xpath("//button[@name='Level1']");
	By select_country=By.xpath("//span[text()='United States']");
	By state=By.xpath("//button[@name='Level2']");
	By select_state=By.xpath("//span[text()='New Jersey']");
	By city=By.xpath("//button[@name='Level4']");
	By select_city=By.xpath("//span[text()='Morristown']");
	By Zip=By.xpath("//button[@name='Level5']");
	By select_Zip=By.xpath("//span[text()='07962']");
	By street=By.xpath("//input[@name='Street']");
	By save_next=By.xpath("//button[text()='Save and Next']");
	By select_Home_owner=By.xpath("//select[@class='slds-select']");
	//By Long_terms=By.xpath("//table[@style='table-layout:fixed;width:1256px']/tbody/tr[20]/td/lightning-primitive-cell-checkbox");
	By Long_terms=By.xpath("//*[@placeholder='Search below records by policy name']");	
	By Long_Term_Radio = By.xpath("//span[@class='slds-radio_faux']");
	
	By departurecopy=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[2]/td[9]/div/a");
	By Originationcopy=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[3]/td[9]/div");
	By Origination_city=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[3]/td[5]/div/lightning-combobox");
	By Morris_Plains=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[3]//span[text()='Morris Plains']");
	By origination_work_Zip=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[3]/td[6]/div/lightning-combobox");
	By origination_Zip=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[3]//span[text()='07950']");
	By origination_Street=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[3]/td[8]/div");
	By origination_Street_enter=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[3]/td[8]/div//input[@class='slds-input']");
	
	By destination_work_country=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[3]/div/lightning-combobox");
	By usa=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]//span[text()='United States']");
	By destination_work_state=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[4]/div/lightning-combobox");
	By Florida=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]//span[text()='Florida']");
	By destination_work_City=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[5]/div/lightning-combobox");
	By Bonita_Springs=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]//span[text()='Miami']");
	By destination_work_Zip=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[6]/div/lightning-combobox");
	By destination_zip=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]//span[text()='33102']");
	By destination_work_Street=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[8]/div");
	By destination_work_Street_enter=By.xpath("//div[@id='secAccTblContent']/table/tbody/tr[4]/td[8]/div//input[@class='slds-input']");
	By save_nextbutton=By.xpath("//button[text()='Save and Next']");
	By add_dependent=By.xpath("//button[text()='Add Dependent(s)']");
	By relation=By.xpath("//select[@class='slds-select']");
	By date_of_birth=By.xpath("//input[@name='Birthdate']");
	By empids=By.xpath("//span[text()='Employee of Company?']/../../div//select[@class='slds-select']");
	By mailing_country=By.xpath("//button[@name='Level3']");
	By Mailing_morries_country=By.xpath("//span[text()='Morris']");
	By mailing_city=By.xpath("//button[@name='Level4']");
	By mailing_borries_city=By.xpath("//span[text()='Morristown']");
	By maling_zip_select=By.xpath("//span[text()='07961']");
	By maling_zip_click=By.xpath("//button[@name='Level5']");
	By mailing_street=By.xpath("//label[text()='Mailing Street']/../div/input");
	By save_dependents=By.xpath("//button[text()='Save Dependents']");
	//this
	By selectdependent=By.xpath("//span[@class='slds-checkbox_faux']");
	//
	
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
	
	By  company_Owned=By.xpath("//span[text()='Company Owned']");
	
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
	By submit_authorization=By.xpath("//button[text()='Submit Authorization to Weichert']");
	By upload=By.xpath("//button[text()='Upload/View Documents']");
	By filetype=By.xpath("//button[@name='fileType']");
	By clientauthorization=By.xpath("//span[text()='Client Authorization']");
	By upload_a_file=By.xpath("//*[text()='Upload Files']");
	By close=By.xpath("//*[text()='Close']");
	By checkbox=By.xpath("//span[@class='slds-radio_faux']");
	
	/**
	 * Method used to Add New Customer
	 * 
	 * @throws Exception
	 */
	public void AddingNewCustomer() throws Exception {
		try {
			
			String mail = RandomStringUtils.randomAlphanumeric(5)+"ab"+"@weichertwm.com";
			String firstName = 'm'+RandomStringUtils.randomNumeric(5)+"fa";
			String lastName = 'c'+RandomStringUtils.randomNumeric(5)+"la";
			String spouseName = RandomStringUtils.randomNumeric(4)+"spl";
			String spouseEmail = RandomStringUtils.randomNumeric(4)+"spl"+"@weichertwm.com";
			String empNo = RandomStringUtils.randomAlphanumeric(6);
			
			ExtentReport.logPass("Navigated to Home page");
			click(Homelink);
			Thread.sleep(3000);
			waitFor(20);
			click(Initiate_New_Move);
			Thread.sleep(9000);
			waitFor(200);
			click(nextbutton);
			Thread.sleep(6000);
			Screenshot sh=new Screenshot();
		String	name=driver.getTitle();
			sh.screenshot(driver, name);
			ExtentReport.logPass("Clicked on Next button to Navigated to create new customer page");
			
			//adding new customer here
			waitFor(20);
			sendKeys(first_name, firstName);
			waitFor(20);
			sendKeys(lastname, lastName);
			waitFor(20);
			sendKeys(email, mail);
			waitFor(20);
			click(preferd_method);
			waitFor(20);
			click(preferd_method_select);
			waitFor(20);
			sendKeys(emplyid, empNo);
			waitFor(20);
			click(country);
			waitFor(20);
			click(select_country);
			Thread.sleep(10);
			waitFor(20);
			click(state);
			waitFor(20);
			click(select_state);
			Thread.sleep(300);
			waitFor(20);
			click(city);
			waitFor(5);
			click(select_city);
			Thread.sleep(500);
			waitFor(20);
			click(Zip);
			waitFor(20);
			click(select_Zip);
			Thread.sleep(600);
			waitFor(20);
			sendKeys(street, "21 main street");
			Thread.sleep(600);
			waitFor(200);
			click(save_next);
			Thread.sleep(6000);
			name=driver.getTitle();
			sh.screenshot(driver, name);
			ExtentReport.logPass("Clicked on Next button to navigate Client Policy page");
			
			waitFor(200);
			selectByValue(select_Home_owner, "Home Owner");
			
			//Domicile Type Selection		
			click(Long_terms);
			clearAndSendKeys(Long_terms, "Long Term Assignment");				
			click(Long_Term_Radio);

			Thread.sleep(1000);
			waitFor(200);
			click(nextbutton);
			ExtentReport.logPass("Clicked on Next button to navigate Add Address page");
			
			//Add address page
			Thread.sleep(6000);
			name=driver.getTitle();
			sh.screenshot(driver, name);
			Reporter.log("", true);
			waitFor(20);
			click(departurecopy);
			waitFor(20);
			Thread.sleep(5000);
			click(Originationcopy);
			
			Thread.sleep(6000);
			click(destination_work_country);
			waitFor(20);
			click(usa);
			Thread.sleep(600);
			click(destination_work_state);
			waitFor(20);
			click(Florida);
			Thread.sleep(600);
			click(destination_work_City);
			waitFor(20);
			click(Bonita_Springs);
			Thread.sleep(1000);
			click(destination_work_Zip);
			waitFor(20);
			click(destination_zip);
			waitFor(20);
			Thread.sleep(1000);
			click(destination_work_Street);
			sendKeys(destination_work_Street_enter, "3 main Street");
			Thread.sleep(5000);
			waitFor(200);
			click(nextbutton);
			ExtentReport.logPass("Clicked on Next button to navigate Add dependents page");
			
			//Dependents page
			waitFor(200);
			Thread.sleep(1000);
			name=driver.getTitle();
			sh.screenshot(driver, name);
			click(add_dependent);
			Thread.sleep(1000);
			name=driver.getTitle();
			sh.screenshot(driver, name);
			waitFor(200);
			sendKeys(first_name, spouseName);
			waitFor(200);
			sendKeys(lastname, lastName);
			waitFor(200);
			sendKeys(email, spouseEmail);
			waitFor(200);
			Thread.sleep(5000);
			waitFor(200);
			selectByValue(relation, "Spouse");
			Thread.sleep(1000);
			waitFor(20);
			click(date_of_birth);
			waitFor(20);
			sendKeys(date_of_birth, "Aug 14, 1970");
			waitFor(20);
			selectByValue(empids, "No");
			Thread.sleep(1000);
			click(mailing_country);
			waitFor(20);
			click(Mailing_morries_country);
			Thread.sleep(1000);
			click(mailing_city);
			waitFor(20);
			click(mailing_borries_city);
			Thread.sleep(1000);
			click(maling_zip_click);
			waitFor(20);
			click(maling_zip_select);
			Thread.sleep(3000);
			sendKeys(mailing_street, "2 main street");
			Thread.sleep(1000);
			
			click(nextbutton);
			waitFor(200);			
			ExtentReport.logPass("Clicked on Next button to navigate Move Details page");
			name=driver.getTitle();
			sh.screenshot(driver, name);
			//Authorization page
			click(authorization_Scope_bussines);
			waitFor(20);
			click(full_authorization);
			waitFor(20);

			sendKeys(estimated_Move_Start_date, "Aug 4, 2023");
			name=driver.getTitle();
			sh.screenshot(driver, name);
			waitFor(20);

			sendKeys(estimated_Move_end_date, "Oct 5, 2023");
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
			Thread.sleep(3000);
				
			click(save_nextbutton);
			ExtentReport.logPass("Clicked on Save and Next button to save and navigate review authorization page");
			Thread.sleep(5000);
			name=driver.getTitle();
			sh.screenshot(driver, name);
			waitFor(20000);
			click(submit_authorization);
			ExtentReport.logPass("Clicked on Submit Authorization button to complete authorization and nvaigate to home page");
			Thread.sleep(20000);
			name=driver.getTitle();
			sh.screenshot(driver, name);
			
		} catch (Exception e) {
			ExtentReport.logFail("taskCostReleasesInAtlantaTEST failed - " + e.getMessage());
			throw new FrameworkException(e.getMessage());	
		}   
	}	
		
}

