package com.ddfw.punj.testcases;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.ddfw.punj.base.BaseTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestCaseC extends BaseTest {

	@Test(priority = 1)
	public void TestC1() {
		System.out.println("c1");
		BaseTest.test = BaseTest.rep.startTest("TestCaseC");
		BaseTest.test.log(LogStatus.PASS, "Passed");
	}

	@Test(priority = 2, dependsOnMethods = { "TestC1" })
	public void TestC2() {
		System.out.println("c2");
		BaseTest.test.log(LogStatus.PASS, "Passed");
	}

	@Test(priority = 3, dependsOnMethods = { "TestC1", "TestC2" })
	public void TestC3() {
		System.out.println("c3");
		BaseTest.test.log(LogStatus.PASS, "Passed");
	}

	@AfterMethod
	public void extentquit() {
		BaseTest.rep.endTest(BaseTest.test);
		BaseTest.rep.flush();
		// this.rep.close();
	}
}
