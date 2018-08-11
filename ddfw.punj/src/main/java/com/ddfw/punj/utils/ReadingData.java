package com.ddfw.punj.utils;

public class ReadingData {

	public static void main(String[] args) {
		Xls_Reader xls = new Xls_Reader("C:\\Users\\Dell\\Desktop\\Testdata.xlsx");
		int rows = xls.getRowCount("Data");
		System.out.println("The number of rows --" + rows);
		int cols = xls.getColumnCount("Data");
		System.out.println("The number of cols --" + cols);

	}

}
