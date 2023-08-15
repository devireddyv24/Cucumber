package com.weichertwm.qa.util;

import java.util.ArrayList;
import java.util.Arrays;

public class Row {
	private ArrayList<String> columnNames;
	private ArrayList<String> columnValues;
	
	public Row() {
		setColumnNames(new ArrayList<String>());
		setColumnValues(new ArrayList<String>());
	}
	
	public Row(ArrayList<String> columnNames, ArrayList<String> columnValues) {
		setColumnNames(columnNames);
		setColumnValues(columnValues);
	}
	
	public Row(int columnCount) {
		columnNames = new ArrayList<String>(columnCount);
		columnValues = new ArrayList<String>(columnCount);
	}
	
	public Row(String[] columnNames) {
		this.columnNames = new ArrayList<String>(Arrays.asList(columnNames));
		this.columnValues = new ArrayList<String>(this.columnNames.size());
	}
	
	public String get(String columnName) {
		return getColumnValues().get(getColumnNames().indexOf(columnName));
		
	}
	
	public String get(int columnIndex) {
		return getColumnValues().get(columnIndex);
	}
	
	public void set(String columnName, String value) {
		this.columnValues.set(this.columnNames.indexOf(columnName), value);
	}
	
	public String getColumnName(int columnIndex) {
		return getColumnNames().get(columnIndex);
	}

	public void setColumnNames(ArrayList<String> columnNames) {
		this.columnNames = columnNames;
	}
	
	public void setColumnNames(String[] columnNames) {
		this.columnNames = new ArrayList<String>(Arrays.asList(columnNames));
	}

	public ArrayList<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnValues(ArrayList<String> columnValues) {
		this.columnValues = columnValues;
	}

	public ArrayList<String> getColumnValues() {
		return columnValues;
	}
	
	public void setColumnValue(int columnIndex, String value) {
		this.columnValues.set(columnIndex, value);
	}
	
	public void setColumnValue(String columnName, String value) {
		this.columnValues.set(this.columnNames.indexOf(columnName), value);
	}
	
	public String toString() {
		String value = "";
		for (String element : columnValues) {
			value = (value.isEmpty()) ? element : value + "," + element;
		}
		return value;
	}
}