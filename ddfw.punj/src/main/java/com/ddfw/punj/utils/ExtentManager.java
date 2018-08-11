package com.ddfw.punj.utils;

//http://relevantcodes.com/Tools/ExtentReports2/javadoc/index.html?com/relevantcodes/extentreports/ExtentReports.html

import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private static ExtentReports extent;
	static String reportPath = "C:\\Reports\\Reports.html";

	public static ExtentReports getInstance() {
		if (ExtentManager.extent == null) {
			Date d = new Date();
			String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".html";
			ExtentManager.extent = new ExtentReports("C:\\Reports\\" + fileName, true, DisplayOrder.NEWEST_FIRST);

			ExtentManager.extent.loadConfig(new File(System.getProperty("user.dir") + "//ReportsConfig.xml"));
			// optional
			ExtentManager.extent.addSystemInfo("Selenium Version", "3.12.0").addSystemInfo("Environment", "QA");
		}
		return ExtentManager.extent;
	}
}
