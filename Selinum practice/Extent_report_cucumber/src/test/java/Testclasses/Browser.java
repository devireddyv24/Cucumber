package Testclasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Browser {
	public static WebDriver driver;
public static WebDriver broser() {
	 WebDriverManager.chromedriver().setup();
	 driver=new ChromeDriver();
	 return driver;
}
}
