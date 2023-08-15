package com.weichertwm.qa.util;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.weichertwm.qa.framework.Log;

public class Table {
	public static boolean isDebug = true;
	
	private int rowCount = -1;
	public int columnCount = -1;
	
	private ArrayList<String> columnNames;
	private ArrayList<Integer> columnTypes;
	private ArrayList<Integer> columnScale;
	private ArrayList <Row> rows;
	
	public Table() {
		setRowCount(0);
		columnCount = 0;
		columnNames = new ArrayList<String>();
		columnTypes = new ArrayList<Integer>();
		columnScale = new ArrayList<Integer>();
		setRows(new ArrayList <Row>());
	}

	public Table(int colCount) {
		setRowCount(0);
		columnCount = colCount;
		columnNames = new ArrayList<String>(colCount);
		columnTypes = new ArrayList<Integer>(colCount);
		columnScale = new ArrayList<Integer>();
		setRows(new ArrayList <Row>());
	}
	
	/**
	 * @reference			https://docs.oracle.com/javase/7/docs/api/constant-values.html#java.sql.Types.INTEGER
	 * @param rs
	 * @throws SQLException
	 */
	public Table(ResultSet rs) throws SQLException {
		this(rs.getMetaData().getColumnCount());
		//columnCount = rs.getMetaData().getColumnCount();

		//Log
		Log.trace("Found "+columnCount+" columns");

		//Load Column Names
		//Column Index starts with 1 and not 0
		for(int colCounter=1; colCounter <= columnCount; colCounter++) {
			//Get values
			String colName = rs.getMetaData().getColumnLabel(colCounter);
			Integer colType = rs.getMetaData().getColumnType(colCounter);
			Integer scale = rs.getMetaData().getScale(colCounter);

			//Log
			Log.trace(colName+"\t \t \t"+colType);

			//Add values
			columnNames.add(colName);
			columnTypes.add(colType);		
			columnScale.add(scale);
		}

		Log.trace("Loading rows to table");
		
		while(rs.next()) {
			Row row = new Row(getColumnNames());

			for (int colCounter = 1; colCounter <= getColumnCount(); colCounter++) {

				//String colName = tbl.getColumnName(colCounter-1);
				String colValue = "";

				//Add Column Name
				//row.getColumnNames().add(colName);

				//Add Column Value
				if (getColumnType(colCounter-1) == Types.LONGVARCHAR 
						|| getColumnType(colCounter-1) == Types.LONGNVARCHAR ) {
					colValue = getLargerString(rs,colCounter);
				} else if (getColumnType(colCounter-1) == Types.DECIMAL ||
						getColumnType(colCounter-1) == Types.NUMERIC) {
					colValue = String.valueOf(rs.getBigDecimal(colCounter));
				} else {
					colValue = rs.getString(colCounter);
				}

				Log.trace(getColumnName(colCounter-1) + "\t \t \t" + colValue);
				row.getColumnValues().add(colValue);
				//row.setColumnValue(colCounter-1, colValue);
			}

			//Increment row count
			if (row != null && row.getColumnValues().size() > 0) {
				getRows().add(row);
				setRowCount(getRowCount() + 1);
			} 
		}

		Log.trace("Column count : "+getColumnCount());
		Log.trace("Row count : "+getRowCount());
		Log.trace("End of table loader");
	}

	/**
	 * Not Implemented
	 * 
	 * @param filePath
	 */
	public Table(String filePath) {
		
	}
	
	public Table(List<String> stringContents) {
		this(stringContents, ",");
	}
	
	/**
	 * Comma separated
	 * @param stringContents
	 */
	public Table(List<String> stringContents, String separatorChar) {
		this(stringContents.get(0).split(separatorChar,-1).length);
		
		//Assuming that first line is header
		String[] aHeaders = stringContents.get(0).split(",");
		
		//Initialize new Table object with number of columns
		//Table tbl = new Table(aHeaders.length);
		Log.trace("Found "+columnCount+" columns");
		
		//Load Column Names
		//Column Index starts with 1 and not 0
		columnNames = new ArrayList<String>(Arrays.asList(aHeaders)); 

		Log.trace("Loading rows to table");
		for(int iRowCounter = 1; iRowCounter < stringContents.size(); iRowCounter++) {
			Row row = new Row(getColumnNames());
			String rowContents = stringContents.get(iRowCounter);
			Log.trace("Found row : ["+rowContents+"]");
			row.setColumnValues(new ArrayList<String>(Arrays.asList(rowContents.split(",",-1))));

			//Increment row count
			if (row != null && row.getColumnValues().size() > 0) {
				getRows().add(row);
				setRowCount(getRowCount() + 1);
			} 
		}
		
		Log.trace("Column count : "+getColumnCount());
		Log.trace("Row count : "+getRowCount());
	}
	

	/**
	 * http://www.java2s.com/Tutorial/Java/0340__Database/GetaLongTextColumnFieldfromaDatabase.htm
	 * 
	 * @param rs
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	private static String getLargerString(ResultSet rs, int columnIndex) throws SQLException {

		InputStream in = null;
		int BUFFER_SIZE = 1024;
		try {
			in = rs.getAsciiStream(columnIndex);
			if (in == null) {
				return "";
			}

			byte[] arr = new byte[BUFFER_SIZE];
			StringBuffer buffer = new StringBuffer();
			int numRead = in.read(arr);
			while (numRead != -1) {
				buffer.append(new String(arr, 0, numRead));
				numRead = in.read(arr);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * Get row by absolute index
	 * Index starts with 0
	 * 
	 * @param rowNum
	 * @return
	 */
	public Row getRow(int rowNum) {
		return this.getRows().get(rowNum);
	}
	
	/**
	 * Get Column Names in an Array
	 * 
	 * @return {@link Array} of Column Names
	 */
	public String[] getColumnNames() {
		return this.columnNames.toArray(new String[this.columnCount]);
	}
	
	/**
	 * Get Column Name by Column Index
	 * Index starts at 0
	 * 
	 * @param index
	 * @return
	 */
	public String getColumnName(int index) {
		return this.columnNames.get(index);
	}
	
	/**
	 * Get Column count 
	 * 
	 * @return
	 */
	public int getColumnCount() {
		return this.columnCount;
	}

	/**
	 * Set the Column Count, not to be used by end users
	 * 
	 * @param columnCount
	 */
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
	
	public void setColumnNameType(String columnName, Integer columnType) {
		this.columnNames.add(columnName);
		this.columnTypes.add(columnType);
	}
	
	public void setColumnNames(String[] columnNames) {
		this.columnNames = new ArrayList<String>(Arrays.asList(columnNames));
	}

	@Deprecated
	public void setColumnTypes(Integer[] columnTypes) {
		this.columnTypes = new ArrayList<Integer>(Arrays.asList(columnTypes));
	}

	public String[] getColumnTypes() {
		return this.columnTypes.toArray(new String[this.columnCount]);
	}

	public Integer getColumnType(String columnName) {
		int index = this.columnNames.contains(columnName) 
						? this.columnNames.indexOf(columnName)
								: this.columnNames.indexOf(columnName.toUpperCase());
		return this.columnTypes.get(index);
	}
	
	public Integer getColumnType(int index) {
		return this.columnTypes.get(index);
	}
		
	public void setColumnType(Integer type) {
		this.columnTypes.add(type);
	}
	
	public Integer getColumnScale(int index) {
		return this.columnScale.get(index);
	}
		

	public Integer getColumnScale(String column) {
		int index = this.columnNames.contains(column) 
				? this.columnNames.indexOf(column)
						: this.columnNames.indexOf(column.toUpperCase());
		return this.columnScale.get(index);
	}

	public void setColumnScale(Integer scale) {
		this.columnScale.add(scale);
	}
	
	/**
	 * Used for export to CSV format
	 * 
	 * @param rows
	 */
	public void setRows(ArrayList <Row> rows) {
		this.rows = rows;
	}

	public ArrayList <Row> getRows() {
		return this.rows;
	}

	public int getRowCount() {
		return this.rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	

}
