package com.ddfw.punj.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.ddfw.punj.base.BaseTest;
import com.ddfw.punj.utils.DataUtil;
import com.ddfw.punj.utils.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class TestCaseA extends BaseTest {
	// ExtentReports rep = ExtentManager.getInstance(); Both lines(ExtentReports rep
	// and ExtentTest test are required in every file to run
	// so we are moving it to Base class so that all instance can get the same
	// ExtentTest test;
	public SoftAssert softassertion;
	String testCaseName = "TestA";

	@Test(dataProvider = "getData")
	public void testA(Hashtable<String, String> data) {
		BaseTest.test = BaseTest.rep.startTest("TestCaseA");
		BaseTest.test.log(LogStatus.INFO, "Starting the test");
		if (data.get("RunMode").equals("N")) {
			BaseTest.test.log(LogStatus.SKIP, "The test case is skipped as indicator is set to N in excel");
			throw new SkipException("The test case is skipped as indicator is set to N in excel");
		}

	}

	// @Override
	// @Override
	@BeforeMethod()
	public void initsoft() {
		this.softassertion = new SoftAssert();

	}

	@Test(priority = 1)
	public void TestA1() {
		System.out.println(System.getProperty("user.dir") + "\\Screenshot");
		BaseTest.test = BaseTest.rep.startTest("TestCaseA");
		BaseTest.test.log(LogStatus.INFO, "Launching the Browser");
		BaseTest.fn_LaunchBrowser("CH");
		BaseTest.test.log(LogStatus.INFO, "Browser launch is complete");
		BaseTest.fn_OpenURL("appURL");// ("https://accounts.google.com");
		BaseTest.fn_type("email_xPath", "sandeep.punj@gmail.com");
		BaseTest.test.log(LogStatus.INFO, "typed the email address");
		BaseTest.fn_click("NextButton_xPath");
		BaseTest.test.log(LogStatus.PASS, "Test case Passed");
		BaseTest.takeScreenShot();

		this.softassertion.assertEquals(this.verifyText("signin_xpath", "signinText"), true);

	}

	@Test(priority = 2, dependsOnMethods = { "TestA1" })
	public void TestA2() {
		System.out.println("A2");
		// this.test.log(LogStatus.FAIL, "Failed for Demo");
		BaseTest.test.log(LogStatus.PASS, "Dummy TestA2 Passed");

	}

	@Test(priority = 3, dependsOnMethods = { "TestA2", "TestA1" })
	public void TestA3() {
		System.out.println("A3");
		BaseTest.test.log(LogStatus.PASS, "Dummy TestA3 Passed");
		BaseTest.takeScreenShot();
	}

	@AfterMethod
	public void extentquit() {
		BaseTest.rep.endTest(BaseTest.test);
		BaseTest.rep.flush();
		// this.rep.close();
	}

	@DataProvider
	public Object[][] getData() {
		super.init();
		System.out.println(BaseTest.prop.getProperty("xlspath"));
		Xls_Reader xls = new Xls_Reader(BaseTest.prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, this.testCaseName);
	}

}
