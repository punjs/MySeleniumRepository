package com.ddfw.punj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Xls_Reader {
	public static String filename = System.getProperty("user.dir") + "\\src\\config\\resources\\TestData.xlsx";
	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	public Xls_Reader(String path) {

		this.path = path;
		try {
			File f = new File(path);
			if (f.exists() == false) {
				throw new FileNotFoundException(this.path + " does not exist");
			}
			this.fis = new FileInputStream(path);
			this.workbook = new XSSFWorkbook(this.fis);
			this.sheet = this.workbook.getSheetAt(0);
			this.fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// returns the row count in a sheet
	public int getRowCount(String sheetName) {
		int index = this.workbook.getSheetIndex(sheetName);
		if (index == -1) {
			return 0;
		} else {
			this.sheet = this.workbook.getSheetAt(index);
			int number = this.sheet.getLastRowNum() + 1;
			return number;
		}

	}

	// returns the data from a cell
	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			if (rowNum <= 0) {
				return "";
			}

			int index = this.workbook.getSheetIndex(sheetName);
			int col_Num = -1;
			if (index == -1) {
				return "";
			}

			this.sheet = this.workbook.getSheetAt(index);
			this.row = this.sheet.getRow(0);
			for (int i = 0; i < this.row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (this.row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
					col_Num = i;
				}
			}
			if (col_Num == -1) {
				return "";
			}

			this.sheet = this.workbook.getSheetAt(index);
			this.row = this.sheet.getRow(rowNum - 1);
			if (this.row == null) {
				return "";
			}
			this.cell = this.row.getCell(col_Num);

			if (this.cell == null) {
				return "";
			}
			// System.out.println(cell.getCellType());
			if (this.cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return this.cell.getStringCellValue();
			} else if ((this.cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
					|| (this.cell.getCellType() == Cell.CELL_TYPE_FORMULA)) {

				String cellText = String.valueOf(this.cell.getNumericCellValue());
				if (DateUtil.isCellDateFormatted(this.cell)) {
					// format in form of M/D/YY
					double d = this.cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(DateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;

					// System.out.println(cellText);

				}

				return cellText;
			} else if (this.cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return "";
			} else {
				return String.valueOf(this.cell.getBooleanCellValue());
			}

		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	// returns the data from a cell
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum <= 0) {
				return "";
			}

			int index = this.workbook.getSheetIndex(sheetName);

			if (index == -1) {
				return "";
			}

			this.sheet = this.workbook.getSheetAt(index);
			this.row = this.sheet.getRow(rowNum - 1);
			if (this.row == null) {
				return "";
			}
			this.cell = this.row.getCell(colNum);
			if (this.cell == null) {
				return "";
			}

			if (this.cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return this.cell.getStringCellValue();
			} else if ((this.cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
					|| (this.cell.getCellType() == Cell.CELL_TYPE_FORMULA)) {

				String cellText = String.valueOf(this.cell.getNumericCellValue());
				/*
				 * if (HSSFDateUtil.isCellDateFormatted(cell)) { // format in form of M/D/YY
				 * double d = cell.getNumericCellValue();
				 *
				 * Calendar cal =Calendar.getInstance();
				 * cal.setTime(HSSFDateUtil.getJavaDate(d)); cellText =
				 * (String.valueOf(cal.get(Calendar.YEAR))).substring(2); cellText =
				 * cal.get(Calendar.MONTH)+1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" +
				 * cellText;
				 *
				 * // System.out.println(cellText);
				 *
				 * }
				 *
				 */

				return cellText;
			} else if (this.cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return "";
			} else {
				return String.valueOf(this.cell.getBooleanCellValue());
			}
		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}

	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
		try {
			this.fis = new FileInputStream(this.path);
			this.workbook = new XSSFWorkbook(this.fis);

			if (rowNum <= 0) {
				return false;
			}

			int index = this.workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1) {
				return false;
			}

			this.sheet = this.workbook.getSheetAt(index);

			this.row = this.sheet.getRow(0);
			for (int i = 0; i < this.row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (this.row.getCell(i).getStringCellValue().trim().equals(colName)) {
					colNum = i;
				}
			}
			if (colNum == -1) {
				return false;
			}

			this.sheet.autoSizeColumn(colNum);
			this.row = this.sheet.getRow(rowNum - 1);
			if (this.row == null) {
				this.row = this.sheet.createRow(rowNum - 1);
			}

			this.cell = this.row.getCell(colNum);
			if (this.cell == null) {
				this.cell = this.row.createCell(colNum);
			}

			// cell style
			CellStyle cs = this.workbook.createCellStyle();
			cs.setWrapText(true);
			this.cell.setCellStyle(cs);
			this.cell.setCellValue(data);

			this.fileOut = new FileOutputStream(this.path);

			this.workbook.write(this.fileOut);

			this.fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url) {
		// System.out.println("setCellData setCellData******************");
		try {
			this.fis = new FileInputStream(this.path);
			this.workbook = new XSSFWorkbook(this.fis);

			if (rowNum <= 0) {
				return false;
			}

			int index = this.workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1) {
				return false;
			}

			this.sheet = this.workbook.getSheetAt(index);
			// System.out.println("A");
			this.row = this.sheet.getRow(0);
			for (int i = 0; i < this.row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (this.row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName)) {
					colNum = i;
				}
			}

			if (colNum == -1) {
				return false;
			}
			this.sheet.autoSizeColumn(colNum); // ashish
			this.row = this.sheet.getRow(rowNum - 1);
			if (this.row == null) {
				this.row = this.sheet.createRow(rowNum - 1);
			}

			this.cell = this.row.getCell(colNum);
			if (this.cell == null) {
				this.cell = this.row.createCell(colNum);
			}

			this.cell.setCellValue(data);
			// XSSFCreationHelper createHelper = this.workbook.getCreationHelper();

			// cell style for hyperlinks
			// by default hypelrinks are blue and underlined
			CellStyle hlink_style = this.workbook.createCellStyle();
			XSSFFont hlink_font = this.workbook.createFont();
			hlink_font.setUnderline(Font.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hlink_style.setFont(hlink_font);
			// hlink_style.setWrapText(true);

			// XSSFHyperlink link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
			// link.setAddress(url);
			// this.cell.setHyperlink(link);
			// this.cell.setCellStyle(hlink_style);

			this.fileOut = new FileOutputStream(this.path);
			this.workbook.write(this.fileOut);

			this.fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// returns true if sheet is created successfully else false
	public boolean addSheet(String sheetname) {

		FileOutputStream fileOut;
		try {
			this.workbook.createSheet(sheetname);
			fileOut = new FileOutputStream(this.path);
			this.workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// returns true if sheet is removed successfully else false if sheet does not
	// exist
	public boolean removeSheet(String sheetName) {
		int index = this.workbook.getSheetIndex(sheetName);
		if (index == -1) {
			return false;
		}

		FileOutputStream fileOut;
		try {
			this.workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(this.path);
			this.workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// returns true if column is created successfully
	public boolean addColumn(String sheetName, String colName) {
		// System.out.println("**************addColumn*********************");

		try {
			this.fis = new FileInputStream(this.path);
			this.workbook = new XSSFWorkbook(this.fis);
			int index = this.workbook.getSheetIndex(sheetName);
			if (index == -1) {
				return false;
			}

			XSSFCellStyle style = this.workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);

			this.sheet = this.workbook.getSheetAt(index);

			this.row = this.sheet.getRow(0);
			if (this.row == null) {
				this.row = this.sheet.createRow(0);
			}

			// cell = row.getCell();
			// if (cell == null)
			// System.out.println(row.getLastCellNum());
			if (this.row.getLastCellNum() == -1) {
				this.cell = this.row.createCell(0);
			} else {
				this.cell = this.row.createCell(this.row.getLastCellNum());
			}

			this.cell.setCellValue(colName);
			this.cell.setCellStyle(style);

			this.fileOut = new FileOutputStream(this.path);
			this.workbook.write(this.fileOut);
			this.fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	// removes a column and all the contents
	public boolean removeColumn(String sheetName, int colNum) {
		try {
			if (!this.isSheetExist(sheetName)) {
				return false;
			}
			this.fis = new FileInputStream(this.path);
			this.workbook = new XSSFWorkbook(this.fis);
			this.sheet = this.workbook.getSheet(sheetName);
			XSSFCellStyle style = this.workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			// XSSFCreationHelper createHelper = this.workbook.getCreationHelper();
			style.setFillPattern(CellStyle.NO_FILL);

			for (int i = 0; i < this.getRowCount(sheetName); i++) {
				this.row = this.sheet.getRow(i);
				if (this.row != null) {
					this.cell = this.row.getCell(colNum);
					if (this.cell != null) {
						this.cell.setCellStyle(style);
						this.row.removeCell(this.cell);
					}
				}
			}
			this.fileOut = new FileOutputStream(this.path);
			this.workbook.write(this.fileOut);
			this.fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	// find whether sheets exists
	public boolean isSheetExist(String sheetName) {
		int index = this.workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = this.workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	// returns number of columns in a sheet
	public int getColumnCount(String sheetName) {
		// check if sheet exists
		if (!this.isSheetExist(sheetName)) {
			return -1;
		}

		this.sheet = this.workbook.getSheet(sheetName);
		this.row = this.sheet.getRow(0);

		if (this.row == null) {
			return -1;
		}

		return this.row.getLastCellNum();

	}

	// String sheetName, String testCaseName,String keyword ,String URL,String
	// message
	public boolean addHyperLink(String sheetName, String screenShotColName, String testCaseName, int index, String url,
			String message) {
		// System.out.println("ADDING addHyperLink******************");

		url = url.replace('\\', '/');
		if (!this.isSheetExist(sheetName)) {
			return false;
		}

		this.sheet = this.workbook.getSheet(sheetName);

		for (int i = 2; i <= this.getRowCount(sheetName); i++) {
			if (this.getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)) {
				// System.out.println("**caught "+(i+index));
				this.setCellData(sheetName, screenShotColName, i + index, message, url);
				break;
			}
		}

		return true;
	}

	public int getCellRowNum(String sheetName, String colName, String cellValue) {

		for (int i = 2; i <= this.getRowCount(sheetName); i++) {
			if (this.getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
				return i;
			}
		}
		return -1;

	}

	// to run this on stand alone
	public static void main(String arg[]) throws IOException {

		System.out.println(Xls_Reader.filename);
		Xls_Reader datatable = null;

		datatable = new Xls_Reader(
				"C:\\Users\\Dell\\Workspace\\Selenium\\ddfw.punj\\src\\test\\resources\\Testdata.xlsx");
		for (int col = 0; col < datatable.getColumnCount("TC5"); col++) {
			System.out.println(datatable.getCellData("TC5", col, 1));
		}
	}

}
