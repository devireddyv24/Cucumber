package com.weichertwm.qa.framework;

import java.awt.RenderingHints.Key;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class PageObject {
	protected WrappedWebDriver driver;

	public PageObject(WebDriver pDriver) {
		driver = (WrappedWebDriver) pDriver;
	}
	
	
	/**
	 * Method: isElementPresent Description: Method is used to check element present
	 * or not
	 * 
	 * @param findBy
	 * @return - true/false
	 * @throws Exception
	 */
	public boolean isElementPresent(By findBy) throws Exception {
		boolean elementPresent = false;
		List<WebElement> elements = null;
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		try {
			elements = this.getElements(findBy);
			if (elements.size() >= 1)
				elementPresent = true;
		} catch (Exception e) {
			elementPresent = false;
		} finally {
			long lngTimeout = Long.parseLong(Config.getEnvDetails("browser.timeoutmilli"));
			driver.manage().timeouts().implicitlyWait(lngTimeout, TimeUnit.MILLISECONDS);
		}
		return elementPresent;
	}

	/**
	 * Method: getElement Description: Method is used to get the Web element from
	 * web page
	 * 
	 * @param findBy
	 * @return - WebElement
	 * @throws Exception
	 */
	public WebElement getElement(By findBy) throws Exception {
		WebElement element = null;
		element = driver.findElement(findBy);
		if (element != null) {
			Log.info(findBy + " found successfully");
			this.highlightElement(element);
		} else {
			Log.error(findBy + " not found");
		}
		return element;
	}

	/**
	 * Method: getElements Description: Method is used to get the Web elements from
	 * web page
	 * 
	 * @param findBy
	 * @return - List<WebElement>
	 * @throws Exception
	 */
	public List<WebElement> getElements(By findBy) throws Exception {
		List<WebElement> elements = driver.findElements(findBy);
		return elements;
	}

	/**
	 * Method: highlightElement Description: Method is used to highlight the Web
	 * element
	 * 
	 * @param WebElement
	 * @throws Exception
	 */
	private void highlightElement(WebElement element) throws Exception {
		if (Config.getEnvDetails("element.highlight").equalsIgnoreCase("true")) {
			String style = element.getCssValue("background-color");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
					element);
			Thread.sleep(500);
			js.executeScript("arguments[0].setAttribute('style','border: solid 1px " + style + "');", element);
		}
	}

	/**
	 * Method: sendKeys Description: Method is used to enter the input text in Web
	 * element
	 * 
	 * @param By
	 * @param CharSequence
	 * @throws Exception
	 */
	public void sendKeys(By findBy, String keys) throws Exception {
		WebElement element = this.getElement(findBy);
		element.sendKeys(keys);
		Log.info(keys + " entered successfully in " + findBy.toString());
		// ExtentReport.logInfo(keys+" entered successfully in "+ findBy.toString());
	}

	public void clearAndSendKeys(By findBy, String keys) throws Exception {
		WebElement element = this.getElement(findBy);
		element.clear();
		element.sendKeys(keys);
		Log.info(keys + " entered successfully in " + findBy.toString());
		// ExtentReport.logInfo(keys+" entered successfully in "+ findBy.toString());
	}

	/**
	 * Method: sendKeysAndTab Description: Method is used to enter the input text
	 * and tab out from Web element
	 * 
	 * @param By
	 * @param CharSequence
	 * @throws Exception
	 */
	public void sendKeysAndTab(By findBy, String keys) throws Exception {
		WebElement element = this.getElement(findBy);
		element.clear();
		element.sendKeys(keys);
		element.sendKeys(Keys.TAB);
		Log.info(keys + " entered successfully and tab out from " + findBy.toString());
	}

	/**
	 * Method: click Description: Method is used to click on Web element
	 * 
	 * @param By
	 * @throws Exception
	 */
	public void click(By findBy) throws Exception {
		WebElement element = this.getElement(findBy);
		try {
			element.click();
			Log.info("Clicked successfully on element - " + findBy.toString());
		} catch (Exception e) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			Log.info("Clicked using Javascript successfully on element - " + findBy.toString());
		}
	}

	public void jsClick(By findBy) throws Exception {
		WebElement element = this.getElement(findBy);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
		Log.info("Clicked using Javascript successfully on element - " + findBy.toString());
	}

	public void doubleClick(By findBy) throws Exception {
		WebElement element = this.getElement(findBy);
		Actions action = new Actions(driver);
		action.doubleClick(element).build().perform();
		Log.info("Double click on element - " + findBy.toString());
	}

	/**
	 * Method: mouseHover Description: Method is used to mouse hover given element
	 * 
	 * @param By
	 * @throws Exception
	 */
	public void mouseHover(By findBy) throws Exception {
		WebElement element = this.getElement(findBy);
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		Log.info("Mouse hovering on element - " + findBy.toString());
	}

	/**
	 * Method: selectDropdownValue Description: Method is used to select the drop
	 * down value
	 * 
	 * @param By
	 * @param dropdownValue
	 * @throws Exception
	 */
	public void selectDropdownValue(By findBy, String dropdownValue) throws Exception {
		this.click(findBy);
		waitFor(2000);
		WebElement ddValue = null;
		By ddValueWithoutSpan = By
				.xpath("//div[@id='oj-listbox-drop']/ul/li/div[contains(./text(),'" + dropdownValue + "')]");
		By ddValueWithSpan = By.xpath("//div[@id='oj-listbox-drop']/ul/li//span[text()='" + dropdownValue + "']");
		By ddValueWithOjOption = By
				.xpath("//div[@id='oj-listbox-drop']/ul/li/div/oj-option[text()='" + dropdownValue + "']");
		if (isElementPresent(ddValueWithoutSpan))
			ddValue = this.getElement(ddValueWithoutSpan);
		else if (isElementPresent(ddValueWithSpan))
			ddValue = this.getElement(ddValueWithSpan);
		else if (isElementPresent(ddValueWithOjOption))
			ddValue = this.getElement(ddValueWithOjOption);
		else {
			String strFailedMsg = "Value '" + dropdownValue + "' not found in dropdown";
			ExtentReport.logFail(strFailedMsg);
			throw new FrameworkException(strFailedMsg);
		}
		highlightElement(ddValue);
		ddValue.click();
		Log.info("select " + dropdownValue + " value on element - " + findBy.toString());
	}

	/**
	 * Method: multiSelectListbox Description: Method is used to select multiple
	 * values in multi select listbox
	 * 
	 * @param By
	 * @param strMultipleValues
	 * @throws Exception
	 */
	public void multiSelectListbox(By findBy, String strMultipleValues) throws Exception {
		String[] strValues = null;
		strValues = strMultipleValues.split(",");
		for (String strVale : strValues) {
			this.click(findBy);
			WebElement ddValue = this
					.getElement(By.xpath("//div[@id='oj-listbox-drop']//div[contains(./text(),'" + strVale + "')]"));
			ddValue.click();
		}
		Log.info(strMultipleValues + " values selected on multiselect element - " + findBy.toString());
	}

	/**
	 * Method: clickCheckbox Description: Method is used to click on checkbox
	 * 
	 * @param By
	 * @param blnFlag
	 * @throws Exception
	 */
	public void clickCheckbox(By findBy, boolean blnFlag) throws Exception {
		WebElement checkbox = this.getElement(findBy);
		String strClassProperty = checkbox.getAttribute("class");
		String strValue = checkbox.getAttribute("checked");
		if (blnFlag) {
			if (strClassProperty.contains("oj-selected") || (strValue != null && strValue.equalsIgnoreCase("true"))) {
				Log.info(findBy.toString() + " checkbox already selected");
			} else {
				checkbox.click();
				Log.info(findBy + " checkbox selected");
			}
		} else {
			if (strClassProperty.contains("oj-selected") || (strValue != null && strValue.equalsIgnoreCase("true"))) {
				checkbox.click();
				Log.info(findBy.toString() + " checkbox unselected");
			} else {
				Log.info(findBy + " checkbox already unselected");
			}
		}
	}

	/**
	 * Method: getText Description: Method is used to get the innerHTML text from
	 * web element
	 * 
	 * @param By
	 * @return String
	 * @throws Exception
	 */
	public String getText(By findBy) throws Exception {
		String strText = "";
		WebElement element = this.getElement(findBy);
		if (element != null) {
			strText = element.getText();
			Log.info("Get the text from element " + findBy.toString() + " is:" + strText);
		}
		return strText;
	}

	/**
	 * Method: getAttribute Description: Method is used to get the attribute value
	 * for web element
	 * 
	 * @param By
	 * @param attributeName
	 * @return String
	 * @throws Exception
	 */
	public String getAttribute(By findBy, String attributeName) throws Exception {
		String strAttributeValue = this.getElement(findBy).getAttribute(attributeName);
		Log.info("property " + attributeName + " value is:" + strAttributeValue);
		return strAttributeValue;
	}

	/**
	 * Method: waitFor Description: Method to pause the execution for the specified
	 * time period
	 * 
	 * @param milliSeconds
	 *            The wait time in milliseconds
	 **/
	public void waitFor(long milliSeconds) throws Exception {
		Thread.sleep(milliSeconds);
	}

	/**
	 * Method: waitUntilPageReadyStateComplete Description: Method to wait until the
	 * page readyState equals 'complete'
	 * 
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 **/
	public void waitUntilPageReadyStateComplete(long timeOutInSeconds) {
		ExpectedCondition<Boolean> pageReadyStateComplete = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		(new WebDriverWait(driver, timeOutInSeconds)).until(pageReadyStateComplete);
		Log.info("Page loaded successfully..");
	}

	/**
	 * Method: waitForElementPresent Description: Method to wait until the specified
	 * element is located
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 **/
	public void waitForElementPresent(By findBy, long timeOutInSeconds) throws Exception {
		(new WebDriverWait(driver, timeOutInSeconds)).until(ExpectedConditions.presenceOfElementLocated(findBy));
	}

	/**
	 * Method: waitUntilElementVisible Description: Method to wait until the
	 * specified element is visible
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 */
	public void waitUntilElementVisible(By findBy, long timeOutInSeconds) throws Exception {
		(new WebDriverWait(driver, timeOutInSeconds)).until(ExpectedConditions.visibilityOfElementLocated(findBy));
	}

	/**
	 * Method: waitUntilElementEnabled Description: Method to wait until the
	 * specified element is enabled
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 */
	public void waitUntilElementEnabled(By findBy, long timeOutInSeconds) throws Exception {
		(new WebDriverWait(driver, timeOutInSeconds)).until(ExpectedConditions.elementToBeClickable(findBy));
	}

	/**
	 * Method: waitUntilElementDisabled Description: Method to wait until the
	 * specified element is disabled
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 */
	public void waitUntilElementDisabled(By findBy, long timeOutInSeconds) throws Exception {
		(new WebDriverWait(driver, timeOutInSeconds))
				.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(findBy)));
	}

	/**
	 * Method: selectByVisibleText Description: Method to select the specified
	 * visible text from a listbox
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the listbox
	 * @param visibletext
	 *            The value to be selected within the listbox
	 */
	public void selectByVisibleText(By findBy, String visibleText) throws Exception {
		Select dropDownList = new Select(this.getElement(findBy));
		dropDownList.selectByVisibleText(visibleText);
	}

	/**
	 * Method: selectByValue Description: Method to select the specified value from
	 * a listbox
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the listbox
	 * @param value
	 *            The value to be selected within the listbox
	 */
	public void selectByValue(By findBy, String value) throws Exception {
		Select dropDownList = new Select(this.getElement(findBy));
		dropDownList.selectByValue(value);
	}

	/**
	 * Method: selectByIndex Description: Method to select the specified index value
	 * from a listbox
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the listbox
	 * @param index
	 *            The value to be selected within the listbox
	 */
	public void selectByIndex(By findBy, int index) throws Exception {
		Select dropDownList = new Select(this.getElement(findBy));
		dropDownList.selectByIndex(index);
	}

	/**
	 * Method: getAllOptionsInSelect Description: Method to get all options in
	 * select tag
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the listbox
	 * @return List<WebElement>
	 */
	public List<WebElement> getAllOptionsInSelect(By findBy) throws Exception {
		Select dropDownList = new Select(this.getElement(findBy));
		return dropDownList.getOptions();
	}

	/**
	 * Method: objectExists Description: Method to verify whether the specified
	 * object exists within the current page
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @return Boolean value indicating whether the specified object exists
	 */
	public Boolean objectExists(By findBy) throws Exception {
		return !driver.findElements(findBy).isEmpty();
	}

	/**
	 * Method: isTextPresent Description: Method to verify whether the specified
	 * text is present within the current page
	 * 
	 * @param textPattern
	 *            The text to be verified
	 * @return Boolean value indicating whether the specified text is present
	 */
	public Boolean isTextPresent(String textPattern) throws Exception {
		return driver.findElement(By.cssSelector("BODY")).getText().matches(textPattern);
	}

	/**
	 * Method: isAlertPresent Description: Method to check if an alert is present on
	 * the current page
	 * 
	 * @param timeOutInSeconds
	 *            The number of seconds to wait while checking for the alert
	 * @return Boolean value indicating whether an alert is present
	 */
	public Boolean isAlertPresent(long timeOutInSeconds) throws Exception {
		try {
			new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Method: isAlertPresent Description: Method to accept the alert on the current
	 * page
	 * 
	 * @throws Exception
	 */
	public void acceptAlert() throws Exception {
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	/**
	 * Method: clear Description: Method is used to clear text in text box for Web
	 * element
	 * 
	 * @param By
	 * @throws Exception
	 */
	public void clear(By findBy) throws Exception {
		WebElement element = this.getElement(findBy);
		element.clear();
		Log.info("Cleared existing text in textbox for element - " + findBy.toString());
	}

	/**
	 * Method is used to wati for element to be clickable.
	 * 
	 * @param findBy
	 * @param timeOutInSeconds
	 * @throws Exception
	 */
	public void waitForElementToBeClickable(By findBy, long timeOutInSeconds) throws Exception {
		(new WebDriverWait(driver, timeOutInSeconds)).until(ExpectedConditions.elementToBeClickable(findBy));
	}
	
	/**
	 * Method used to retrieve and return the drop-down selected value 
	 * @param findBy
	 * @return
	 */
	public String getSelectedValue(By findBy){
		String dropDownValue = null;
		WebElement element = driver.findElement(findBy);
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		boolean changeDropDown =  false;
		if(element.getCssValue("display").equalsIgnoreCase("none")){
			executor.executeScript("arguments[0].style.display='block';", element);
			element = driver.findElement(findBy);
			changeDropDown = true;
		}
		dropDownValue = new Select(element).getFirstSelectedOption().getText();
		if(changeDropDown)
			executor.executeScript("arguments[0].style.display='none';", element);

		return dropDownValue;
	}
	
	/**
	 * Method: waitForNumberOfElementsSize Description: Method to wait until the
	 * specified number of elements are available
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 * @param numberOfElements
	 *            Number of elements size
	 */

	public void waitForNumberOfElementsSize(By findBy, long timeOutInSeconds, int numberOfElements) {
		new WebDriverWait(driver, timeOutInSeconds)
				.until(ExpectedConditions.numberOfElementsToBe(findBy, numberOfElements));
	}

	/**
	 * Method: Method used to refresh the web page
	 */
	public void refreshPage() {
		driver.navigate().refresh();
	}
	public void keysclass(By findby,Keys enter) {
		driver.findElement(findby).sendKeys(Keys.ENTER);
	}
}
