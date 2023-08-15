package com.weichertwm.qa.framework;

public enum Status {
	INFO,
	// Debug: for reporting debug related logs
	DEBUG,
	// DONE: for reporting action items and test data used
	DONE,
	// PASS: for reporting successful logs with screen shots
	PASS,
	// FAIL: for reporting failure logs with screen shots
	FAIL,
	// WARN: for reporting warning logs
	WARN,
	// BUSINESSSTEP: for reporting business steps
	BUSINESSSTEP,
	KEYWORD,
	FATAL
   
}
