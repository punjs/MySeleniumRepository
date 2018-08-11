package com.ddfw.punj.testcases;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.ddfw.punj.base.BaseTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestCaseB extends BaseTest {

	@Test(priority = 1)
	public void TestB1() {
		System.out.println("B1");
		BaseTest.test = BaseTest.rep.startTest("TestCaseB");
		BaseTest.test.log(LogStatus.PASS, "Passed");
	}

	@Test(priority = 2, dependsOnMethods = { "TestB1" })
	public void TestB2() {
		System.out.println("B2");
		BaseTest.test.log(LogStatus.PASS, "Passed");
	}

	@Test(priority = 3, dependsOnMethods = { "TestB2", "TestB1" })
	public void TestB3() {
		System.out.println("B3");
		BaseTest.test.log(LogStatus.PASS, "Passed");
	}

	@AfterMethod
	public void extentquit() {
		BaseTest.rep.endTest(BaseTest.test);
		BaseTest.rep.flush();
		// this.rep.close();
	}
}
