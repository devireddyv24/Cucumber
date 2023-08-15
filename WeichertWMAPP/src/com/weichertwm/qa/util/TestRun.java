package com.weichertwm.qa.util;

public class TestRun {
  private String scriptName;
  private int testId;
  private int testRunId;
  private String duration;
  private String status;
  private String module;
  
  public TestRun() {}
  
  public String getScriptName() { return scriptName; }
  
  public void setScriptName(String scriptName) {
    this.scriptName = scriptName;
  }
  
  public int getTestRunId() { return testRunId; }
  
  public void setTestRunId(int testRunId) {
    this.testRunId = testRunId;
  }
  
  public String getDuration() { return duration; }
  
  public void setDuration(String duration) {
    this.duration = duration;
  }
  
  public String getStatus() {
		return status;
  }

  public void setStatus(String status) {
	this.status = status;
  }

public int getTestId() {
	return testId;
}

public void setTestId(int testId) {
	this.testId = testId;
}

public String getModule() {
	return module;
}

public void setModule(String module) {
	this.module = module;
}

}