package com.weichertwm.qa.framework;


@SuppressWarnings("serial")
public class FrameworkException extends RuntimeException {
	/**
	 * The step name to be specified for the exception
	 */
	private String errorName = "Error";
	
	public FrameworkException(String errorDescription) {
		super(errorDescription);
		Log.error(errorDescription);
		errorName=errorDescription;
	}
	
	public String getError() {
		return errorName;
	}
}