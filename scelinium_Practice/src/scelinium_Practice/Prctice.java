package scelinium_Practice;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Prctice {
@Test(groups = "st")
	public void main() {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		WebDriver d=new ChromeDriver();
		d.get("http://www.uitestpractice.com/");
		d.manage().window().maximize();
		d.findElement(By.linkText("Switch to")).click();
	int i=d.findElements(By.tagName("iframe")).size();
		System.out.println(i);
	//	WebElement e=d.findElement(By.xpath("//iframe[@*='iframe_a']"));
		d.switchTo().frame(0);
System.out.println("System testing is runed completed");
	}

}
