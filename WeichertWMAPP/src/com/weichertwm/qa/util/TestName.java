package com.weichertwm.qa.util;

import java.util.List;

public class TestName {
	private int testId;
	private String testName;
	private String buildNumber;
	private String testDuration;
	private String testRunDate;
	private String testStatus;
	private int total;
	private int passed;
	private int failed;
	private String testAgent;
	private String envHost;
	private String reportHtml;
 
  private List<TestRun> scriptsList;
  
  public TestName() {}
  
  
  
  public String getTestName() {
    return testName;
  }
  
  public void setTestName(String testName) { this.testName = testName; }
  
  public String getTestAgent() {
    return testAgent;
  }
  
  public void setTestAgent(String testAgent) { this.testAgent = testAgent; }
  
  public String getTestStatus() {
    return testStatus;
  }
  
  public void setTestStatus(String testStatus) { this.testStatus = testStatus; }
  
  public String getBuildNumber() {
    return buildNumber;
  }
  
  public void setBuildNumber(String buildNumber) { this.buildNumber = buildNumber; }
  
  public String getTestRunDate() {
    return testRunDate;
  }
  
  public void setTestRunDate(String testRunDate) { this.testRunDate = testRunDate; }
  
  public int getTotal() {
    return total;
  }
  
  public void setTotal(int total) { this.total = total; }
  
  public int getPassed() {
    return passed;
  }
  
  public void setPassed(int passed) { this.passed = passed; }
  
  public int getFailed() {
    return failed;
  }
  
  public void setFailed(int failed) { this.failed = failed; }
  
  public List<TestRun> getScriptsList() {
    return scriptsList;
  }
  
  public void setScriptsList(List<TestRun> scriptsList) { this.scriptsList = scriptsList; }
  
  public String getTestDuration()
  {
    return testDuration;
  }
  
  public void setTestDuration(String testDuration) { this.testDuration = testDuration; }
  
	public int getTestId() {
		return testId;
	}
	
	public void setTestId(int testId) {
		this.testId = testId;
	}



	public String getEnvHost() {
		return envHost;
	}



	public void setEnvHost(String envHost) {
		this.envHost = envHost;
	}



	public String getReportHtml() {
		return reportHtml;
	}



	public void setReportHtml(String reportHtml) {
		this.reportHtml = reportHtml;
	}
}