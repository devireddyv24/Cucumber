package scelinium_Practice;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import java.awt.Desktop.Action;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Flifcart {
@Test(groups = "It")
	public  void main() {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		WebDriver d=new ChromeDriver();
		d.get("https://www.flipkart.com");
d.findElement(By.xpath("//*[text()='✕']")).click();
WebElement e1=d.findElement(By.xpath("//*[text()='Electronics']"));
Actions act=new Actions(d);
act.moveToElement(e1).perform();
WebElement e2=d.findElement(By.xpath("//*[text()=\"Gaming\"]"));
act.moveToElement(e2).perform();
d.findElement(By.xpath("//*[text()='Gamepads']")).click();
WebDriverWait wait=new WebDriverWait(d,Duration.ofSeconds(3));
System.out.println("integration testing is completed");
	}

}
