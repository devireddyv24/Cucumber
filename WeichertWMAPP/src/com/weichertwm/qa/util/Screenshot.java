package com.weichertwm.qa.util;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.google.common.io.Files;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.weichertwm.qa.framework.ExtentReport;

public class Screenshot {
	public static int i=0;
	public void screenshot(WebDriver driver,String name) {
		/*TakesScreenshot sh=(TakesScreenshot)driver;
		File src=sh.getScreenshotAs(OutputType.FILE);
		i++;
		File drs=new File("C:\\Users\\VamshidharReddyDevir\\Downloads\\WeichertWMAPP (1)\\WeichertWMAPP\\Screenshots\\"+name+i+".png");
		try {
			Files.copy(src, drs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String filePath = drs.toString();
		String path = "<img src='"+filePath+"'>"+name+i+"</img>";
		Reporter.log(path);
		ExtentTest test = ExtentReport.getTest();
		test.log(LogStatus.INFO, "Snapshot below: (" + filePath + ")" + test.addScreenCapture(filePath));*/
		//ExtentReport.takeScreenShot(driver);
		
		
		String screenshotpath=ExtentReport.takeScreenShot(driver);
		ExtentTest test = ExtentReport.getTest();
		test.log(LogStatus.INFO, "Snapshot below: (" + screenshotpath + ")" + test.addScreenCapture(screenshotpath));
		
	}

}
