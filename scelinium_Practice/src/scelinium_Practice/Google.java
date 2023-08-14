package scelinium_Practice;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import java.util.List;

import org.checkerframework.checker.interning.qual.EqualsMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Google {
@Test(groups ="ft")
	public void main() throws InterruptedException {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		WebDriver d=new ChromeDriver();
		d.get("https://www.google.co.in");
		d.findElement(By.name("q")).sendKeys("Enhops");
		Thread.sleep(3000);
		List<WebElement> e=d.findElements(By.xpath("//div[@class='wM6W7d']/../../../../div[2]/div/div/ul/li"));
		for (WebElement web : e) {
			String s=web.getText();
			System.out.println(s);
			if(s.contains("Enhops Salary")) {
				web.click();
				break;
			}
			System.out.println("functional testing is run completed");
				
				
				
			
		}

	}

}
