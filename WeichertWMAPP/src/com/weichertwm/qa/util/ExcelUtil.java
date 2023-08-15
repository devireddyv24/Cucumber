package com.weichertwm.qa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.weichertwm.qa.framework.Config;
import com.weichertwm.qa.framework.ExtentReport;
import com.weichertwm.qa.framework.Log;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtil {

	public final static int testDataNoOfRows = 60000;
	public final int testDataColumncount = 999;
	public final static int testCaseColumn = 0;
	public final int testDataColumn = 0;
	
	
	public synchronized static int getRandomTextSize() {
		int legnth=3;
		try {
			String legnthInText=Config.getEnvDetails("randomnumberlength");
			if (legnthInText!=null) legnth= Integer.parseInt(legnthInText);
		}catch (Exception e) {
			//Falls here when there is error while reading size of converting the size
		}
		Log.info("Using random text size:"+legnth);
		return legnth;	
	}

	/**
	 * This method is used to get the sheet object by passing excel path and sheet
	 * name
	 * 
	 * This method returns data required for test case. A test must indicate
	 *  if it requires auto data population through this method. 
	 *  
	 * Method halts execution if it encounters error
	 * 
	 * @param excelFilePath
	 * @param sheetName
	 * @return sheet Object
	 * @throws IOException 
	 */
	public synchronized static Sheet getSheet(String excelFilePath, String sheetName) {
		Log.info("Getting sheet [" + sheetName + "] from excel file:" + excelFilePath);
		
		
		try {
			Workbook workBook;
			FileInputStream inputStream;
			excelFilePath = new File(".").getCanonicalPath() + excelFilePath;

			inputStream = new FileInputStream(Paths.get(excelFilePath).toFile());
			if (excelFilePath.endsWith("xlsx")) {
				Log.debug("Found XLSX file [" + excelFilePath + "], creating workbook object");
				workBook = new XSSFWorkbook(inputStream);
				Log.debug("Workbook was retrieved");
			} else if (excelFilePath.endsWith("xls")) {
				Log.debug("Found XLS file [" + excelFilePath + "], creating workbook object");
				workBook = new HSSFWorkbook(inputStream);
				Log.debug("Workbook was retrieved");
			} else {
				inputStream.close();
				throw new IllegalArgumentException("The specified file [" + excelFilePath + "] is not an Excel file");
			}
			
			Sheet sheet = workBook.getSheet(sheetName);
			workBook.close();
			inputStream.close();
			
			if (sheet != null && sheet.getSheetName().equalsIgnoreCase(sheetName)) {
				ExtentReport.logPass("Found required data sheet : "+sheetName);
			} else {
				throw new Exception("The specified worksheet [" + sheetName + "] was not retrieved, instead received ["+sheet.getSheetName()+"]");
			}
 			
			
			return sheet;
		} catch (FileNotFoundException fnfe) {
			Log.error("File does not exists at " + excelFilePath + " or sheet " + sheetName + " is not available");
			Log.catching(fnfe);
			
		} catch (Exception e) {
			Log.catching(e);
		}
		
		/*Log.printHaltMessage();
		System.exit(1);*/
		return null;
	}

	/**
	 * This method is used to find out the row number for required test case id
	 * 
	 * @param sheet
	 * @param testcaseID
	 * @return row number
	 */
	public synchronized static int findRow(Sheet sheet, String testcaseID) {
		Log.debug("Searching for test case [" + testcaseID + "] in sheet [" + sheet.getSheetName() + "]");
		int rowNum = -1;
		try {
			for (int row = 0; row < testDataNoOfRows; row++) {
				String xlTCName = getCellData(sheet, row, testCaseColumn);
				if (!xlTCName.toLowerCase().trim().equals(testcaseID))
					continue;
				rowNum = row;
				if(rowNum!=-1)
					break;
			}
		} catch (Exception e) {
			Log.error("Error in finding the test case for test case:" + testcaseID + " in sheet:" + sheet.getSheetName());
			Log.catching(e);
			/*Log.printHaltMessage();
			System.exit(1);*/
			rowNum = -1;
		}
		return rowNum;
	}

	/**
	 * This method is used to read the cell data from any type of cell
	 * 
	 * @param mySheet
	 * @param row
	 * @param col
	 * @return cell data
	 */
	public synchronized static String getCellData(Sheet mySheet, int row, int col) {
		String returnVal = "";
		try {
			DataFormatter fmt = new DataFormatter();
			Cell cell = mySheet.getRow(row).getCell(col);
			returnVal = fmt.formatCellValue(cell);

		} catch (Exception e) {
			returnVal = "";
		}
		return returnVal;
	}

	/**
	 * This method is used to read the row data from specified excel sheet place it
	 * into hashmap
	 * 
	 * @param mySheet
	 * @param row
	 * @return key values pairs of key and value
	 */
	public synchronized static HashMap<String, String> load(Sheet mySheet, int row) {
		Log.debug("Reading data from sheet:" + mySheet.getSheetName() + " and test case row:" + row);
		HashMap<String, String> record = new HashMap<String, String>();
		DataFormatter fmt = new DataFormatter();
		try {
			Row testCaseRow = mySheet.getRow(row);
			if (testCaseRow == null) {
				throw new Exception("No row is identified for row number:" + row);
			}
			Iterator<Cell> cellIterator = testCaseRow.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				String cellData = fmt.formatCellValue(cell);
				if (cellData.equals("") || cellData == null) {
					throw new Exception("Cell is empty or No Data in excel");
				}
				//Ven - Checking if cell type is formula				
				/*if(cell.getCellType()==cell.CELL_TYPE_FORMULA)
					cellData=cell.getStringCellValue();*/
				if(cell.getCellTypeEnum() == CellType.FORMULA)
					cellData=cell.getStringCellValue();
				String[] keyValueSplitData = cellData.split("=", 2);
				if (keyValueSplitData.length == 1) {
					Log.debug("test case:" + keyValueSplitData[0]);
					record.put("testCaseName", keyValueSplitData[0]);
				} else if (!(keyValueSplitData[1].contains("<common.") || keyValueSplitData[1].contains("<config.")
						|| keyValueSplitData[1].contains("<Common.") || keyValueSplitData[1].contains("<Config.")
						|| keyValueSplitData[1].contains("<rand text>")
						|| keyValueSplitData[1].contains("<rand number>")
						|| keyValueSplitData[1].contains("<rand alphanumaric>"))) {

					record.put(keyValueSplitData[0], keyValueSplitData[1]);
				} else {
					String randomData=getRandomData(keyValueSplitData[1]);
					record.put(keyValueSplitData[0], randomData);
				}
			}
			Log.debug("Succefully read the data from sheet:" + mySheet.getSheetName() + " and test case row:" + row);
		} catch (Exception e) {
			Log.error("Failed to read data sheet due to " + e.getMessage());
			Log.catching(e);
			//System.exit(1);
			Log.printHaltMessage();
		}
		return record;
	}

	/**
	 * This method is used to read the random data based on the value string
	 * specified in the excel file
	 * 
	 * @param keyToRead
	 * @return string
	 */
	public synchronized static String getRandomData(String keyToRead) {
		int randomNumberLength=getRandomTextSize();
		String retValue = "";
		try {
			if (keyToRead.contains("<common.") || keyToRead.contains("<Common.")) {
				String key = keyToRead.toLowerCase().replace("<common.", "").replace(">", "");
				retValue = getCommonData(key);
			} else if (keyToRead.contains("<config.") || keyToRead.contains("<Config.")) {
				String key = keyToRead.replace("<config.", "").replace(">", "");
				retValue = Config.getEnvDetails(key);
			} else if (keyToRead.contains("<rand text>")) {
				String randomUUIDString = RandomStringUtils.randomAlphabetic(randomNumberLength);
				retValue = keyToRead.replace("<rand text>", randomUUIDString);
			} else if (keyToRead.contains("<rand number>")) {
				String randomUUIDString = RandomStringUtils.randomNumeric(randomNumberLength);
				retValue = keyToRead.replace("<rand number>", randomUUIDString);
			} else if (keyToRead.contains("<rand alphanumaric>")) {
				String randomUUIDString = RandomStringUtils.randomAlphanumeric(randomNumberLength);
				retValue = keyToRead.replace("<rand alphanumaric>", randomUUIDString);
			}
		} catch (Exception e) {
			Log.error("Unable to generate random value by parsing attribute " + keyToRead);
			Log.catching(e);
		}
		return retValue;
	}

	/**
	 * This method is used to read data from common data sheet
	 * 
	 * @param keyToRead
	 * @return value for requested key
	 */
	public synchronized static String getCommonData(String keyToRead) {
		Log.info("Reading value for key:" + keyToRead + " from sheet :common");
		String cellData = "";
		try {
			String fileName = Config.getEnvDetails("datafilename");
			Sheet sheet = getSheet(fileName, "common");
			int rowNum = findRow(sheet, keyToRead);
			if (rowNum == -1) {
				throw new Exception("There is no such key with name " + keyToRead + " in the data sheet :common");
			}
			cellData = getCellData(sheet, rowNum, 1);
		} catch (Exception e) {
			Log.error("Fail to read the common data file " + e.getMessage());
			Log.catching(e);
		}
		return cellData;
	}

	/**
	 * 
	 * This method reads each row into a List
	 * Cell values of each row will be put into a hashmap
	 * Keys will be the first row in Excel
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static List<HashMap<String, String>> readDataFromExcel(String fileName) throws IOException {
		String rowTestData = "";
		ArrayList<HashMap<String, String>> excelRecords = new ArrayList<HashMap<String, String>>();
		ArrayList<String> tableHeaders = new ArrayList<String>();
		int rowCount = 0;
		InputStream ExcelFileToRead = new FileInputStream(fileName);
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		XSSFCell cell;
		Iterator<Row> rows = sheet.iterator();
		while (rows.hasNext()) {
			HashMap<String, String> records = new HashMap<String, String>();
			try {
				row = (XSSFRow) rows.next();
				Iterator<Cell> cells = row.cellIterator();
				int colCount = 0;
				while (cells.hasNext()) {
					cell = (XSSFCell) cells.next();
					rowTestData = readCell(cell);
					if (rowCount == 0) {
						tableHeaders.add(rowTestData);
					} else {
						records.put(tableHeaders.get(colCount), rowTestData);
					}
					rowTestData = "";
					colCount++;
				}
				if (!(rowCount == 0)) {
					excelRecords.add(records);
				}
				rowCount++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		wb.close();
		return excelRecords;
	}

	
	/**
	 * This method checks the cell type and returns the cell value as String
	 * 
	 * @param cell
	 * @return
	 * @throws IOException
	 */
	public static String readCell(XSSFCell cell) throws IOException {
		String cellData = "";
		
		if(cell.getCellTypeEnum() == CellType.STRING)
			cellData += cell.getStringCellValue();
		else if(cell.getCellTypeEnum() == CellType.NUMERIC)
			cellData += (int) cell.getNumericCellValue();
		else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
			cellData += cell.getBooleanCellValue();
		else if (cell.getCellTypeEnum() == CellType.FORMULA)
			cellData = cell.getCellFormula();
		else if (cell.getCellTypeEnum() == CellType.BLANK)
			cellData += cell.getBooleanCellValue();
		
		/*if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
			cellData += cell.getStringCellValue();
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			cellData += (int) cell.getNumericCellValue();
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
			cellData += cell.getBooleanCellValue();
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
			cellData = cell.getCellFormula();
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			cellData += cell.getBooleanCellValue();
		} else {
			// Utilities.log("Invalid");
		}*/

		return cellData;

	}
	
	//ven
		public static boolean writeDataToExcelCell( String strSheetName,String strTestcaseName,String strKey,String strData) throws IOException {
			String excelFilePath = Config.getEnvDetails("datafilename");  
			excelFilePath = new File(".").getCanonicalPath() + excelFilePath;
			Workbook workBook = null;
			Sheet sheet = null;;
			String strCellData="";
			int iDataCol=-1;
			try {
				FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
				workBook = new XSSFWorkbook(inputStream);			
				sheet= (XSSFSheet) workBook.getSheet(strSheetName);
				if(sheet==null) return false;			
				int iRowNum=findRow(sheet,strTestcaseName.toLowerCase().trim());
				if(iRowNum<0) return false;
				System.out.println("irowNum???/"+iRowNum);
				Row row=sheet.getRow(iRowNum);
				System.out.println("irowNum???/"+row.getPhysicalNumberOfCells());
				for(int iColNum=1;iColNum<row.getPhysicalNumberOfCells()-1;iColNum++) 
				{
					strCellData=getCellData(sheet, iRowNum, iColNum);
					System.out.println("strCellData"+strCellData+"strKey"+strKey);
					if(strCellData.contains(strKey)) 
					{
						iDataCol=iColNum;
						break;
					}				
				}
				System.out.println("iDataCol"+iDataCol);
				if(iDataCol>-1)
				setCellData(sheet, iRowNum, iDataCol, strKey+"="+strData);
				inputStream.close();
				FileOutputStream outputStream = new FileOutputStream(new File(excelFilePath));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(workBook);
				workBook.write(outputStream);			
				workBook.close();
				outputStream.close();
				System.out.println("save is done");

			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
			return true;
		}
		
	//ven
		public synchronized static Boolean  setCellData(Sheet mySheet, int irowNum, int icolNum,String cellData) {		
			try {
				Row row = null;
				Cell cell = null;
				if(icolNum<0 && irowNum<0)  return false;
				if((mySheet==null)) return false;
				row = mySheet.getRow(irowNum);
				if((row==null)) mySheet.createRow(irowNum);
				else { 
					cell=(XSSFCell) row.createCell(icolNum);							
					cell.setCellValue(cellData);
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;

		}

		public static boolean updateExcelFile(String filePath,String worksheetId, String rowId, String columnId, String value) throws Exception {
			boolean update = false;
			filePath = new File(".").getCanonicalPath() + filePath;
			if (filePath==null || filePath.isEmpty()){
				System.out.println("Invalid or null file path ["+filePath+"]");
			}

			if (Integer.parseInt(worksheetId) < 0 || Integer.parseInt(rowId) < 0 || Integer.parseInt(columnId) < 0){
				System.out.println("Invalid cell details ["+worksheetId+"]::["+rowId+"]:["+columnId+"] with ["+value+"]");
			}

			if (value==null || value.isEmpty()){
				System.out.println("Invalid or null value ["+value+"]");
			}

			try {
				FileInputStream file = new FileInputStream(filePath);
				System.out.println("Updating file ["+filePath+"] at location ["+worksheetId+"]::["+rowId+"]:["+columnId+"] with ["+value+"]");
				@SuppressWarnings("resource")
				HSSFWorkbook workbook = new HSSFWorkbook(file);
				HSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(worksheetId));
				Cell cell = sheet.getRow(Integer.parseInt(rowId)).getCell(Integer.parseInt(columnId));
				cell.setCellValue(value);
				file.close();
				FileOutputStream outFile =new FileOutputStream(new File(filePath));
				workbook.write(outFile);
				outFile.close();
				update = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Could not find file ["+filePath+"]");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Could not update file ["+filePath+"]");
			} catch (NullPointerException e){
				e.printStackTrace();
				System.out.println("Could not find cell ["+rowId+"]:["+columnId+"]");
			}
			return update;
		}
}
