package com.ddfw.punj.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.ddfw.punj.utils.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseTest {

	public static WebDriver driver;
	public static Properties prop;
	public static ExtentReports rep = ExtentManager.getInstance();
	public static ExtentTest test;

	/// *****************************Generic Functions**************
	/// ************************************************************

	public void init() {
		// init the property file
		if (BaseTest.prop == null) {
			BaseTest.prop = new Properties();
			// System.out.println(System.getProperty("user.dir"));
			try {
				FileInputStream fs = new FileInputStream(
						System.getProperty("user.dir") + "//src//test//resources//Projectconfig.properties");
				BaseTest.prop.load(fs);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public static WebDriver fn_LaunchBrowser(String browsername) {

		if (browsername == "CH") {
			System.setProperty("webdriver.chrome.driver", BaseTest.prop.getProperty("chromedriver_exe"));// "C:\\Web_Drivers\\chromedriver.exe");
			BaseTest.driver = new ChromeDriver();
		} else if (browsername == "FF") {
			BaseTest.driver = new FirefoxDriver();
		} else if (browsername == "IE") {
			System.setProperty("webdriver.ie.driver", BaseTest.prop.getProperty("iedriver_exe")); // "C:\\Web_Drivers\\IEDriverServer.exe");
			BaseTest.driver = new InternetExplorerDriver();
		}
		BaseTest.driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		BaseTest.driver.manage().window().maximize();
		return BaseTest.driver;
	}

	// *******************************fn_OpenURL*****************************************
	public static void fn_OpenURL(String Keyurl) {

		BaseTest.driver.get(BaseTest.prop.getProperty(Keyurl));
		BaseTest.driver.manage().window().maximize();

	}

	// *******************************fn_navigate*****************************************//
	public static void fn_navigate(String appKey) {
		System.out.println("hallo");
		BaseTest.driver.navigate().to(BaseTest.prop.getProperty(appKey));
		BaseTest.driver.manage().window().maximize();
	}

	// *******************************fn_click*****************************************//
	public static void fn_click(String locatorKey) {
		BaseTest.getElement(locatorKey).click();
	}

	// *******************************fn_type*****************************************
	public static void fn_type(String locatorKey, String inputData) {
		BaseTest.getElement(locatorKey).sendKeys(inputData);
	}

	// ***************************************find Element and return
	// it***************************************

	public static WebElement getElement(String locatorKey) {
		WebElement e = null;
		try {
			if (locatorKey.endsWith("_id")) {
				e = BaseTest.driver.findElement(By.id(BaseTest.prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_xPath")) {
				e = BaseTest.driver.findElement(By.xpath(BaseTest.prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_name")) {
				e = BaseTest.driver.findElement(By.name(BaseTest.prop.getProperty(locatorKey)));
			} else {
				BaseTest.reportFailure("Locator not correct- " + locatorKey);
				Assert.fail("Failed the test" + locatorKey);
			}
		} catch (Exception ex) {
			// fail the test and report the error
			BaseTest.reportFailure(ex.getMessage());
			ex.printStackTrace();
			Assert.fail("Failed the test" + ex.getMessage());
		}
		return e;

	}

	public boolean isElementPresent(String locatorKey) {
		List<WebElement> elementList = null;

		if (locatorKey.endsWith("_id")) {
			elementList = BaseTest.driver.findElements(By.id(BaseTest.prop.getProperty(locatorKey)));
		} else if (locatorKey.endsWith("_xPath")) {
			elementList = BaseTest.driver.findElements(By.xpath(BaseTest.prop.getProperty(locatorKey)));
		} else if (locatorKey.endsWith("_name")) {
			elementList = BaseTest.driver.findElements(By.name(BaseTest.prop.getProperty(locatorKey)));
		} else {
			BaseTest.reportFailure("Locator not correct- " + locatorKey);
			Assert.fail("Failed the test" + locatorKey);
		}
		if (elementList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	////////////////////////////////////////////// Drop
	////////////////////////////////////////////// downs////////////////////////////////////////////////////
	// select the dropdown using "select by visible text", so pass VisibleText as
	////////////////////////////////////////////// 'Yellow' to funtion
	public static void fn_Select(WebElement WE, String VisibleText) {
		Select selObj = new Select(WE);
		selObj.selectByVisibleText(VisibleText);
	}

	// select the dropdown using "select by index", so pass IndexValue as '2'
	public static void fn_Select(WebElement WE, int IndexValue) {
		Select selObj = new Select(WE);
		selObj.selectByIndex(IndexValue);
	}

	// select the dropdown using "select by value", so pass Value as 'thirdcolor'
	public static void fn_Selectbyval(WebElement WE, String Value) {
		Select selObj = new Select(WE);
		selObj.selectByValue(Value);
	}

	/// ******************************************Common Validation
	/// Function*************************************************///
	/// **************************************************************************************************************************////
	public boolean verifyTitle() {

		return false;

	}

	public boolean isElementPresent() {
		return false;

	}

	public boolean verifyText(String locatorKey, String expectedText) {

		String actualText = BaseTest.getElement(locatorKey).getText().trim();
		expectedText = BaseTest.prop.getProperty(expectedText);
		if (actualText.equals(expectedText)) {
			return true;
		} else {
			return false;
		}

	}

	/// ******************************************Common Reporting
	/// Function*************************************************///

	public void reportPass(String msg) {
		BaseTest.test.log(LogStatus.PASS, msg);
		BaseTest.takeScreenShot();

	}

	public static void reportFailure(String msg) {
		BaseTest.test.log(LogStatus.FAIL, msg);
		BaseTest.takeScreenShot();
		Assert.fail(msg);
	}

	public static void takeScreenShot() {
		// fileName of the screenshot
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// take screenshot
		File srcFile = ((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.FILE);
		try {
			// get the dynamic folder name
			FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir") + "\\Screenshot" + screenshotFile));
			// put screenshot file in reports
			BaseTest.test.log(LogStatus.INFO, "Screenshot-> "
					+ BaseTest.test.addScreenCapture(System.getProperty("user.dir") + "\\Screenshot" + screenshotFile));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
