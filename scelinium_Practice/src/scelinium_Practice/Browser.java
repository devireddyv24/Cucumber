package scelinium_Practice;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import java.awt.Dimension;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Browser {
@Test(groups = "It")
	public  void main() throws Exception {
		// TODO Auto-generated method stub
		WebDriver d;
		//System.setProperty("webdriver.chrome.driver","D:\\Java_programs\\practice_java\\src\\browsers\\chromedriver.exe");
		WebDriverManager.edgedriver().setup();
		d=new EdgeDriver();
		d.manage().timeouts().implicitlyWait(Duration.ofSeconds(500));
d.get("https://www.amazon.in/?&ext_vrnc=hi&tag=googhydrabk1-21&ref=pd_sl_19f32o2up6_e&adgrpid=64607481971&hvpone=&hvptwo=&hvadid=486382354470&hvpos=&hvnetw=g&hvrand=13017198067369917285&hvqmt=e&hvdev=c&hvdvcmdl=&hvlocint=&hvlocphy=9062177&hvtargid=kwd-297775402051&hydadcr=5650_2175716&gclid=EAIaIQobChMIh4WH6-eZ_AIVTjErCh3sPwopEAAYASAAEgLr8fD_BwE");
org.openqa.selenium.Dimension d1=new org.openqa.selenium.Dimension(50,60);
d.manage().window().setSize(d1);
d.findElement(By.id("twotabsearchtextbox")).sendKeys("mi");
d.findElement(By.xpath("//*[@id='nav-search-submit-button']")).click();
//Thread.sleep(3000);
d.findElement(By.xpath("//*[contains(text(),'Redmi 10A (Slate Grey, 4GB RAM, 64GB Storage) | 2 Ghz Octa Core Helio G25 | 5000 mAh Battery | Finger Print Sensor | Upto 5GB RAM with RAM Booster')]")).click();
String s=d.getWindowHandle();
Set<String> allid=d.getWindowHandles();
for (String s1 : allid) {
	if(!s.equals(s1)) {
		d.switchTo().window(s1);
	}
	
}
//Thread.sleep(3000);
//d.close();
//Thread.sleep(3000);
d.findElement(By.xpath("//*[@name='submit.add-to-cart']")).click();
//Thread.sleep(3000);
d.switchTo().window(s);
d.quit();
d.findElement(By.xpath("//*[@*='attach-sidesheet-checkout-button-announce']")).click();
System.out.println("Integration testing is run completed");
	}

}
