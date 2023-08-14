package scelinium_Practice;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Practice {
@Test(groups = "st")
	public void main() throws InterruptedException {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		WebDriver d=new ChromeDriver();
		d.get("http://www.uitestpractice.com/");
		d.manage().window().maximize();
		d.findElement(By.linkText("Switch to")).click();
		d.findElement(By.xpath("//*[@*='modal']")).click();
		//WebDriverWait wait=new WebDriverWait(d,Duration.ofSeconds(4000));
		//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[text()='Ok']")));
	WebElement e=d.findElement(By.xpath("//*[text()='Ok']"));
	Thread.sleep(3000);
	JavascriptExecutor j = (JavascriptExecutor) d;
	j.executeScript("arguments[0].click();", e);
	System.out.println("System testing run is completed");
	

	}

}
