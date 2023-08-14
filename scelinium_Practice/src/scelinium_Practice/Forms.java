package scelinium_Practice;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import java.sql.Time;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Forms {
@Test(groups = "ft")
	public void main() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver d=new ChromeDriver();
		d.get("http://www.uitestpractice.com/");
		d.manage().window().maximize();
		d.findElement(By.xpath("//a[text()='Form']")).click();
		d.findElement(By.id("firstname")).sendKeys("vamshi",Keys.TAB,"Devireddy");
	WebElement e1=d.findElement(By.xpath("//*[@name='optradio']/../../label[3]"));
	e1.click();
WebElement e2=d.findElement(By.xpath("//*[@value='dance']"));
e2.click();
d.findElement(By.xpath("//*[@value='dance']/../../label[3]")).click();
d.findElement(By.xpath("//*[@value='dance']/../../label[4]")).click();
WebElement e3=d.findElement(By.id("sel1"));
e3.click();
Select s=new Select(e3);
s.selectByVisibleText("Bahrain");
d.findElement(By.id("datepicker")).click();
/*WebElement e4=d.findElement(By.xpath("//select[@class='ui-datepicker-year']"));
Select s1=new Select(e4);
s1.selectByValue("2000");
WebElement e5=d.findElement(By.xpath("//select[@class='ui-datepicker-month']"));
Select s2=new Select(e5);
s2.selectByValue("3");
d.findElement(By.xpath("//*[text()='24']")).click();*/
d.findElement(By.id("phonenumber")).sendKeys("7093778248");
WebElement el1=d.findElement(By.xpath("//select[@class='ui-datepicker-year']"));
Select sl=new Select(el1);
sl.selectByValue("1989");
//d.findElement(By.id("phonenumber")).sendKeys("7093778248",Keys.TAB,"vamshidhar",Keys.TAB,"devireddyv22@gmail.com",Keys.TAB,"My name is vamshi i am from amangal",Keys.TAB,"Vamshi@123",Keys.TAB,Keys.ENTER);

/*d.findElement(By.xpath("//*[text()='Select']")).click();
WebDriverWait wait=new WebDriverWait(d,Duration.ofSeconds(10));
wait.until(ExpectedConditions.elementToBeClickable(By.id("dropdownMenu1")));
d.findElement(By.id("dropdownMenu1")).click();
d.findElement(By.xpath("//*[@id='dropdownMenu1']/../ul//li[3]/a")).click();
WebElement e6= d.findElement(By.id("countriesMultiple"));
Select s3=new Select(e6);
s3.selectByIndex(3);
WebElement e7=d.findElement(By.xpath("//select[@id='countriesSingle']"));
Select s4=new Select(e7);
s4.selectByIndex(1);*/
	System.out.println("functional testing is run completed");
	}

}
