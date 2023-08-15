package com.weichertwm.qa.testcases;

import com.weichertwm.qa.framework.CommonPage;
import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.TestCase;
import com.weichertwm.qa.framework.TestCategory;
import com.weichertwm.qa.pages.LoginPage;
import com.weichertwm.qa.pages.WCWM_Existing_Customer_Page;
import com.weichertwm.qa.pages.WCWM_New_Customer_Page;

public class WCWM_Adding_New_Customer extends TestCase {

	@Override
	@TestCategory(categories = "WCWM_Adding_New_Customer, WeichertWM")
	public void test() throws Exception {
		
		// Login page instance creation
		LoginPage loginPage = new LoginPage(driver);

		// CS task cost release page functionality page instance creation
		WCWM_New_Customer_Page wcwmCustomer = new WCWM_New_Customer_Page(driver);
		
		// Common functionality page instance creation
		CommonPage commonPage = new CommonPage(driver);

		// Below method will log into great gulf application
		Log.info("Login method");
		loginPage.loginToApp();
		wcwmCustomer.AddingNewCustomer();
		Thread.sleep(7000);
		
	}

}
