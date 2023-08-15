package com.weichertwm.qa.testcases;

import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.TestCase;
import com.weichertwm.qa.framework.TestCategory;
import com.weichertwm.qa.pages.LoginPage;
import com.weichertwm.qa.pages.WCWM_Existing_Customer_Page;
import com.weichertwm.qa.pages.WCWM_New_Customer_Page;

public class WCWM_Customer_Operations extends TestCase {

	@Override
	@TestCategory(categories = "WCWM_Customer_Operations, WeichertWM")
	public void test() throws Exception {

		// Login page instance creation
		LoginPage loginPage = new LoginPage(driver);

		// WCWM Existing Customer page functionality page instance creation
		WCWM_Existing_Customer_Page wcwmCustomer = new WCWM_Existing_Customer_Page(driver);
		
		//  WCWM New Customer page functionality page instance creation
		WCWM_New_Customer_Page wcwmNewCustomer = new WCWM_New_Customer_Page(driver);

		// Below method will log into WeichertWM application
		Log.info("Calling Login method");
		loginPage.loginToApp();
		
		//Below method will call the existing customer
		Log.info("Operations on Existing customer");
		wcwmCustomer.ExistingCustomer();
		Thread.sleep(6000);
		
		//Below method will call the existing customer
		Log.info("Operations on New customer");
		wcwmNewCustomer.AddingNewCustomer();;
		Thread.sleep(6000);
		
	}
}
