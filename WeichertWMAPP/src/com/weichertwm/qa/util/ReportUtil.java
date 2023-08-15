package com.weichertwm.qa.util;

import java.io.File;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.weichertwm.qa.framework.BuildParams;
import com.weichertwm.qa.framework.Config;
import com.weichertwm.qa.framework.FrameworkException;
import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.TestCase;

public class ReportUtil {

	static Connection connection = null;
	static ResultSet rs = null;
	public static Map<String, String> mapScriptWithStatus = new HashMap<String, String>();

	/**
	 * Method:getHostName Description:This method is used to return the current
	 * hostname
	 * 
	 * @return - String
	 * @throws Exception
	 */
	private static String getHostName() throws Exception {
		String strHostName = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			strHostName = addr.getHostName();
		} catch (UnknownHostException ex) {
			Log.error("Hostname can not be resolved");
		}
		return strHostName;
	}

	/**
	 * Method:senarioDurationCalculation Description:This method is used to
	 * calculate senarioDuration based on 2 times
	 * 
	 * @param strTime1
	 * @param strTime2
	 * @return String
	 * @throws Exception
	 */
	private static String senarioDurationCalculation(String strTime1, String strTime2) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = format.parse(strTime1);
		Date date2 = format.parse(strTime2);
		long lngDiff = date2.getTime() - date1.getTime();
		long lngDiffSeconds = lngDiff / 1000L % 60L;
		long lngDiffMinutes = lngDiff / 60000L % 60L;
		long lngDiffHours = lngDiff / 3600000L % 24L;
		return lngDiffHours + "h " + lngDiffMinutes + "m " + lngDiffSeconds + "s";
	}

	/**
	 * Method:getConnection Description:This method is used to connect the report DB
	 * 
	 * @throws Exception
	 */
	private static Connection getConnection() throws Exception {
		Connection connection = null;
		try {
			String conString = "jdbc:oracle:thin:@//" + Config.getEnvDetails("report.db.host") + ":"
					+ Config.getEnvDetails("report.db.port") + "/" + Config.getEnvDetails("report.db.sid");
			Log.info("Creating Connection with report db String " + conString);
			connection = DatabaseUtil.getDBConnection("oracle.jdbc.driver.OracleDriver", conString,
					Config.getEnvDetails("report.db.username"), Config.getEnvDetails("report.db.password"));
			if (connection != null)
				Log.info("Report Portal Database connected successfully");
		} catch (SQLException e) {
			Log.error("Connection Failed! Check output console");
			throw new FrameworkException("Report Portal Database connection failed");
		}
		return connection;
	}

	/**
	 * Method:getMaxTestId Description:This method is used to get the max test id
	 * value
	 * 
	 * @return integer
	 * @throws Exception
	 */
	private static int getMaxTestId() throws Exception {
		int intTestId = 0;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt=null;
		try {
			conn = ReportUtil.getConnection();
			String strQuery = "SELECT MAX(TESTID) as MAXTESTID FROM REPORTUSER.TESTNAME";
			stmt = conn.prepareStatement(strQuery);
			rs=stmt.executeQuery();
			if (rs.next()) {
				intTestId = rs.getInt(1);
			}
		} catch (SQLException e) {
			Log.error("Connection Failed! Check output console"+e.getMessage());
			conn.close();
		} finally {
			Log.info("Diconnecting all the connection objects");
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (Exception e1) {
				// ignore
			}
		}
		return intTestId;
	}

	/**
	 * Method:getMaxTestRunId Description:This method is used to get the max test
	 * run id value
	 * 
	 * @return integer
	 * @throws Exception
	 */
	private static int getMaxTestRunId() throws Exception {
		int intTestRunId = 0;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt=null;
		try {
			conn = ReportUtil.getConnection();
			String strQuery = "SELECT MAX(TESTRUNID) as MAXTESTRUNID FROM REPORTUSER.TESTRUNS";
			stmt = conn.prepareStatement(strQuery);
			rs=stmt.executeQuery();
			if (rs.next()) {
				intTestRunId = rs.getInt(1);
			}
		} catch (SQLException e) {
			Log.error("Connection Failed! Check output console");
			conn.close();
		} finally {
			Log.info("Diconnecting all the connection objects");
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (Exception e1) {
				// ignore
			}
		}
		return intTestRunId;
	}

	/**
	 * Method:readReportAndStoredIntoDb Description:This method is used to read the
	 * result report and stored details into report DB
	 * 
	 * @param strFilePath
	 * @param strSuiteName
	 * @throws Exception
	 */
	public static void readReportAndStoredIntoDb(String strFilePath, String strSuiteName) throws Exception {
		int intTotalTests = 0;
		int intTestsPassCount = 0;
		int intTestsFailCount = 0;
		String strExecutionTotalDuration = null;
		String strSuiteStatus = null;
		String strModuleName = null;
		String strTestName = null;
		String strModuleAndTest = null;
		String strTestStatus = null;
		String strAgent = null;
		String strSuiteEndTime = null;
		String strSenarionExeStartTime = null;
		String strNextEleStartTime = null;
		String strSenarionExeDuration = null;
		String strExecutionDate = null;
		String strBuild = null;
		//String strZephyrUpdate, strCycleName, Phases, tcsSearchTags = "";
		List<TestRun> scriptArray = new ArrayList<TestRun>();
		Document doc = Jsoup.parse(new File(strFilePath), "UTF-8");
		String strReportHtml = FileUtils.readFileToString(new File(strFilePath), "UTF-8");
		int intTestId = 0;
		int intMaxTestRunId = 0;
		try {
			String strReportDBInsert = BuildParams.REPORT_INTO_DB;
			if (strReportDBInsert.equalsIgnoreCase("Yes")) {
				intTestId = ReportUtil.getMaxTestId();
				intTestId = intTestId + 1;
				intMaxTestRunId = ReportUtil.getMaxTestRunId();
			}

			Element testCollectionNode = doc.getElementById("test-collection");
			// Elements testCollection = testCollectionNode.getElementsByTag("li");
			Elements testCollection = testCollectionNode.getElementsByClass("collection-item");
			intTotalTests = testCollection.size();
			strSuiteEndTime = doc.getElementsByClass("suite-ended-time").get(0).text();
			strAgent = ReportUtil.getHostName();
			strBuild = BuildParams.BUILD_NUMBER;
			if (StringUtils.isEmpty(strBuild))
				strBuild = "20.1";
			for (Element test : testCollection) {
				if (test.getElementsByClass("test-status").get(0).text().equalsIgnoreCase("pass"))
					intTestsPassCount++;
				else
					intTestsFailCount++;
				strModuleAndTest = test.getElementsByClass("test-name").get(0).text();
				strTestStatus = test.getElementsByClass("test-status").get(0).text();
				strSenarionExeDuration = test.getElementsByAttributeValue("title", "Time taken to finish").get(0)
						.text();
				if (strSenarionExeDuration.equals("")) {
					strSenarionExeStartTime = test.getElementsByAttributeValue("title", "Test started time").get(0)
							.text();
					strNextEleStartTime = test.nextElementSibling()
							.getElementsByAttributeValue("title", "Test started time").get(0).text();
					if (intTotalTests == 1) {
						strSenarionExeDuration = senarioDurationCalculation(strSenarionExeStartTime, strSuiteEndTime);
						strSenarionExeDuration = strSenarionExeDuration.replace("h ", "H : ").replace("m ", "M : ")
								.replace("s", "S");
					} else {
						strSenarionExeDuration = senarioDurationCalculation(strSenarionExeStartTime,
								strNextEleStartTime);
						strSenarionExeDuration = strSenarionExeDuration.replace("h ", "H : ").replace("m ", "M : ")
								.replace("s", "S");
					}
				} else {
					// strSenarionExeDuration = strSenarionExeDuration.split("\\+")[0];
					strSenarionExeDuration = strSenarionExeDuration.replace("h ", "H : ").replace("m ", "M : ")
							.replace("s+", "S +").replace("+", " : ");
				}

				intMaxTestRunId = intMaxTestRunId + 1;
				strModuleName = strModuleAndTest.split("_", 2)[0];
				strTestName = strModuleAndTest.split("_", 2)[1];
				TestRun scriptObj = new TestRun();
				scriptObj.setTestRunId(intMaxTestRunId);
				scriptObj.setTestId(intTestId);
				scriptObj.setScriptName(strTestName);
				scriptObj.setStatus(strTestStatus.toUpperCase());
				scriptObj.setDuration(strSenarionExeDuration);
				scriptObj.setModule(strModuleName.toUpperCase());
				scriptArray.add(scriptObj);
				mapScriptWithStatus.put(strTestName, strTestStatus);
			}
			scriptArray.sort(Comparator.comparing(TestRun::getModule).thenComparing(Comparator.comparing(TestRun::getScriptName)));
			Elements timeheads = doc.getElementsByClass("suite-total-time-overall");
			Elements spantags = timeheads.get(0).getElementsByTag("span");
			strExecutionTotalDuration = spantags.get(1).text().split("\\+")[0];
			strExecutionTotalDuration = strExecutionTotalDuration.replace("h ", "H : ").replace("m ", "M : ")
					.replace("s", "S");

			Elements starttimeheads = doc.getElementsByClass("suite-start-time");
			Elements datespantags = starttimeheads.get(0).getElementsByTag("span");
			strExecutionDate = datespantags.get(1).text().split(" ")[0];

			if (intTestsFailCount > 0)
				strSuiteStatus = "Failed";
			else
				strSuiteStatus = "Passed";

			TestName testrun = new TestName();
			testrun.setTestId(intTestId);
			testrun.setTestName(strSuiteName);
			testrun.setBuildNumber(strBuild);
			testrun.setTestDuration(strExecutionTotalDuration);
			testrun.setTestRunDate(strExecutionDate);
			testrun.setTestStatus(strSuiteStatus);
			testrun.setTotal(intTotalTests);
			testrun.setFailed(intTestsFailCount);
			testrun.setPassed(intTestsPassCount);
			testrun.setTestAgent(strAgent);
			testrun.setEnvHost(TestCase.envHost);
			testrun.setReportHtml(strReportHtml);

			if (strReportDBInsert.equalsIgnoreCase("Yes")) {
				ReportUtil.insertTests(testrun);
				ReportUtil.insertScripts(scriptArray);
			}
			/*strZephyrUpdate = BuildParams.ZEPHYR_UPDATE;
			Log.info("ZephyrUpdate:" + strZephyrUpdate);
			Log.info("ReportUtil.mapScriptWithStatus.size():" + ReportUtil.mapScriptWithStatus.size());

			if (ReportUtil.mapScriptWithStatus.size() > 0 && strZephyrUpdate.equalsIgnoreCase("YES")) {
				Log.info("Zephyr update start....");
				strCycleName = BuildParams.ZEPHYR_CYCLE_NAME;
				Log.info("CycleName:" + strCycleName);
				Phases = BuildParams.ZEPHYR_PHASES;
				Log.info("All Phases:" + Phases);
				tcsSearchTags = BuildParams.ZEPHYR_TCS_SEARCH_TAGS;
				Log.info("TC'S Search Tags:" + tcsSearchTags);
				ZephyrAPI zephyrAPI = new ZephyrAPI();
				zephyrAPI.updateZephyrTestCasesStatus(strCycleName, Phases, tcsSearchTags, mapScriptWithStatus);
			}*/
			String strSendMail = BuildParams.IS_SEND_MAIL;
			if (strSendMail.equalsIgnoreCase("Yes"))
				ReportUtil.sendEmail(strFilePath, testrun, scriptArray);
		} catch (SQLException e) {
			Log.error("[readReportAndStoredIntoDb]:" + e.getMessage());
		}
	}

	/**
	 * Method:insertTests Description:This method is used to insert suite details
	 * into report DB
	 * 
	 * @param testrun
	 *            - TestName pojo class instance
	 * @throws Exception
	 */
	private static void insertTests(TestName testrun) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ReportUtil.getConnection();
			Clob clob = conn.createClob();
			clob.setString(12, testrun.getReportHtml());
			String strQuery = "INSERT INTO REPORTUSER.TESTNAME(TESTID,TESTNAME,BUILD,DURATION,TESTDATE,EXECSTATUS,TOTALSCRIPTS,PASSED,FAILED,AGENT,ENVHOST,REPORT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(strQuery);
			pstmt.setInt(1, testrun.getTestId());
			pstmt.setString(2, testrun.getTestName());
			pstmt.setString(3, testrun.getBuildNumber());
			pstmt.setString(4, testrun.getTestDuration());
			pstmt.setString(5, testrun.getTestRunDate());
			pstmt.setString(6, testrun.getTestStatus());
			pstmt.setInt(7, testrun.getTotal());
			pstmt.setInt(8, testrun.getPassed());
			pstmt.setInt(9, testrun.getFailed());
			pstmt.setString(10, testrun.getTestAgent());
			pstmt.setString(11, testrun.getEnvHost());
			pstmt.setClob(12, clob);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			Log.error("[insertTests:] Tests insertion failed - "+e.getMessage());
			conn.close();
		} finally {
			Log.info("Diconnecting all the connection objects");
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e1) {
				// ignore
			}
		}
	}

	/**
	 * Method:insertScripts Description:This method is used to insert scripts
	 * details into report DB
	 * 
	 * @param testScriptList
	 *            - TestRun pojo class instance list
	 * @throws Exception
	 */
	private static void insertScripts(List<TestRun> testScriptList) throws Exception {
		int intNoOfScripts = 0;
		Connection conn = null;
		try {
			conn =	ReportUtil.getConnection();
			for (TestRun testScript : testScriptList) {
				PreparedStatement pstmt = null;
				String strQuery = "INSERT INTO REPORTUSER.TESTRUNS(TESTRUNID,TESTID,SCRIPTNAME,DURATION,STATUS,MODULE) VALUES (?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(strQuery);
				pstmt.setInt(1, testScript.getTestRunId());
				pstmt.setInt(2, testScript.getTestId());
				pstmt.setString(3, testScript.getScriptName());
				pstmt.setString(4, testScript.getDuration());
				pstmt.setString(5, testScript.getStatus());
				pstmt.setString(6, testScript.getModule());
				pstmt.executeUpdate();
				intNoOfScripts++;
				pstmt.close();
			}
			Log.info("No of Scripts are inserted into DB:" + intNoOfScripts);
		} catch (SQLException e) {
			Log.error("[insertScripts:] Scripts insertion failed - "+e.getMessage());
			conn.close();
		} finally {
			Log.info("Diconnecting all the connection objects");
			try {
				conn.close();
			} catch (Exception e1) {
				// ignore
			}
		}
	}

	/**
	 * Method:sendEmail Description:This method is used to send the Suite details
	 * email
	 * 
	 * @param testList
	 *            - TestName instance
	 * @throws Exception
	 */
	private static void sendEmail(String strReportFilePath, TestName testList, List<TestRun> testScriptList)
			throws Exception {
		String strMailTo = "";
		String strEmail = "";
		String[] toArray = null;
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName("internal-mail-router.oracle.com");
			email.setSmtpPort(25);
			email.setSocketConnectionTimeout(300000);
			email.setSocketTimeout(300000);
			email.setFrom("R&A-AUTOMATION_IN@ORACLE.COM");
			email.setSubject(
					"R&A2 Automation Report of " + testList.getTestName() + " on Build " + testList.getBuildNumber());
			// email.attach(new File(strReportFilePath));
			strMailTo = BuildParams.MAIL_TO;
			toArray = strMailTo.split("::");
			email.addTo(toArray);
			String strReportDBInsert = BuildParams.REPORT_INTO_DB;
			if (strReportDBInsert.equalsIgnoreCase("Yes")) {
				// email.setHtmlMsg(ReportUtil.prepareHtmlbody(testList));
				List<TestModule> lstModules = prepareModuleWisePojo(testScriptList);
				email.setHtmlMsg(ReportUtil.prepareHTMLBodyWithDbInsert(testList, testScriptList, lstModules));
			} else if (strReportDBInsert.equalsIgnoreCase("No")) {
				email.setHtmlMsg(ReportUtil.prepareHTMLBodyWithoutDbInsert(testList, testScriptList));
				// email.setHtmlMsg(ReportUtil.prepareHtmlbodyWithoutDBInsert(testList,
				// testScriptList));
			}

			else
				throw new FrameworkException("Please select Yes/No for REPORT_INTO_DB parameter");
			// Create the attachment
			/*
			 * EmailAttachment attachment = new EmailAttachment();
			 * attachment.setPath(strReportFilePath);
			 * attachment.setDisposition(EmailAttachment.ATTACHMENT);
			 * email.attach(attachment);
			 */
			strEmail = email.send();
			Log.info("Email Status:" + strEmail);
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Method:prepareHtmlbody Description:This method is used to prepare html body
	 * for email
	 * 
	 * @param entry
	 *            - TestName instance
	 * @return - String
	 * @throws Exception
	 */
	/*
	 * private static String prepareHtmlbody(TestName entry) throws Exception {
	 * String portalLink = Config.getEnvDetails("report.portal"); String htmltext =
	 * "<html>\n<head>\n" +
	 * "<style>\ntable, th, td { \nborder-collapse: collapse; \nborder: 1px solid black;\ntext-align: center; \npadding: 5px; \n}\nth { \nborder-collapse: collapse; \nborder: 1px solid black;\ntext-align: center; \npadding: 5px; \nbackground:#F78E7C; \ncolor:white; \n}\n</style> \n</head>\n"
	 * +
	 * "<body>\n<h2>Execution Summary:</h2>\n<table>\n<tbody>\n<tr>\n<th>SNo</th>\n<th>Suite</th>\n<th>Build</th>\n<th>Total Tests</th>\n<th>Passed</th>\n<th>Failed</th>\n<th>Duration</th>\n<th>Agent</th>\n<th>EnvHost</th>\n<th>Execution Date</th>\n<th>Report</th>\n</tr>\n"
	 * ; int sno = 1; htmltext = htmltext + "<tr> \n<td>" + sno + "</td><td>" +
	 * entry.getTestName() + "</td><td>" + entry.getBuildNumber() + "</td>"; if
	 * (entry.getTotal() > 0) { htmltext = htmltext + "<td><a href=\"" + portalLink
	 * + "/MOTControllerServlet?action=getStatusScriptDtls&testNo=" +
	 * entry.getTestId() + "&module=" + URLEncoder.encode(entry.getTestName(),
	 * "UTF-8") + "&hit=mail&status=all \"><font color=black><b>" + entry.getTotal()
	 * + "</b></font></a></td>"; } else { htmltext = htmltext +
	 * "<td><font color=black><b>" + entry.getTotal() + "</b></td>"; } if
	 * (entry.getPassed() > 0) { htmltext = htmltext + "<td><a href=\"" + portalLink
	 * + "/MOTControllerServlet?action=getStatusScriptDtls&testNo=" +
	 * entry.getTestId() + "&module=" + URLEncoder.encode(entry.getTestName(),
	 * "UTF-8") + "&hit=mail&status=passed \"><font color=green><b>" +
	 * entry.getPassed() + "</b></font></a></td>"; } else { htmltext = htmltext +
	 * "<td><font color=green><b>" + entry.getPassed() + "</b></td>"; } if
	 * (entry.getFailed() > 0) { htmltext = htmltext + "<td><a href=\"" + portalLink
	 * + "/MOTControllerServlet?action=getStatusScriptDtls&testNo=" +
	 * entry.getTestId() + "&module=" + URLEncoder.encode(entry.getTestName(),
	 * "UTF-8") + "&hit=mail&status=failed \"><font color=red><b>" +
	 * entry.getFailed() + "</b></font></a></td>"; } else { htmltext = htmltext +
	 * "<td><font color=red><b>" + entry.getFailed() + "</b></td>"; }
	 * 
	 * htmltext = htmltext + "<td>" + entry.getTestDuration() + "</td><td>" +
	 * entry.getTestAgent() + "</td><td>" + entry.getEnvHost() + "</td><td>" +
	 * entry.getTestRunDate() + "</td>";
	 * 
	 * htmltext = htmltext + "<td><a href=\"" + portalLink +
	 * "/MOTControllerServlet?action=reportView&testNo=" + entry.getTestId() +
	 * "\"><font color=blue><b>report.html</b></font></a></td></tr>";
	 * 
	 * htmltext = htmltext + "</body>\n</html>";
	 * 
	 * return htmltext; }
	 */

	private static String prepareHTMLBodyWithDbInsert(TestName entry, List<TestRun> testScriptList,
			List<TestModule> testModuleList) {

		String htmlBody = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
				+ "  <html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\r\n"
				+ "  <head>\r\n" + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\r\n"
				+ "  <meta name=\"format-detection\" content=\"date=no\"/>\r\n"
				+ "  <meta name=\"format-detection\" content=\"telephone=no\"/>\r\n" + "     <!--[if !mso]><!-- -->\r\n"
				+ "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n" + "     <!--<![endif]-->\r\n"
				+ "<html>\r\n" + "	<head>\r\n" + "	<style>\r\n" + "    #outlook a{\r\n" + "        padding:0;\r\n"
				+ "    }\r\n" + "    .ReadMsgBody{\r\n" + "        width:100%;\r\n" + "    }\r\n" + "    body{\r\n"
				+ "        width:100% !important;\r\n" + "        min-width:100%;\r\n"
				+ "        -webkit-text-size-adjust:100%;\r\n" + "        -ms-text-size-adjust:100%;\r\n"
				+ "        -webkit-font-smoothing: antialiased;\r\n" + "    }\r\n" + "    v*{\r\n"
				+ "        behavior:url(#default#VML);\r\n" + "        display:inline-block;\r\n" + "    }\r\n"
				+ "    .ExternalClass{\r\n" + "        width:100%;\r\n" + "    }\r\n" + "    td{\r\n"
				+ "        border-collapse: collapse!important;\r\n" + "    }\r\n" + "    .ExternalClass,\r\n"
				+ "    .ExternalClass p,\r\n" + "    .ExternalClass span,\r\n" + "    .ExternalClass font,\r\n"
				+ "    .ExternalClass td,\r\n" + "    .ExternalClass div{\r\n" + "        line-height:100%;\r\n"
				+ "    }\r\n" + "    a img{\r\n" + "        border:none;\r\n" + "    }\r\n" + "    a {\r\n"
				+ "        text-decoration:none !important;\r\n" + "    }\r\n" + "    img{\r\n"
				+ "        display:block;\r\n" + "        outline:none;\r\n" + "        text-decoration:none;\r\n"
				+ "        border:none; \r\n" + "         -ms-interpolation-mode: bicubic;\r\n" + "    }\r\n"
				+ "    table{\r\n" + "        border-spacing:0;\r\n"
				+ "        border-collapse: separate !important;\r\n" + "        mso-table-lspace:0pt;\r\n"
				+ "        mso-table-rspace:0pt;\r\n" + "    }\r\n" + "	.results-table {\r\n"
				+ "    	font-family: 'Open Sans', sans-serif;\r\n" + "		border-collapse: collapse;\r\n"
				+ "		font-size: 0.9em;\r\n" + "		min-width: 400px;\r\n" + "		overflow:auto;\r\n"
				+ "		box-shadow: 0 0 30px 0px;\r\n" + "	}\r\n" + "	.results-table  th {\r\n"
				+ "	  background-color: #0b7670;\r\n" + "	  color: #ffffff;\r\n" + "	  text-align: center;\r\n"
				+ "	  font-weight: bold;\r\n" + "	}\r\n" + "\r\n" + "	.results-table  th,\r\n"
				+ "	.results-table  td {\r\n" + "	  padding: 12px 15px;\r\n" + "	  text-align: center;\r\n"
				+ "	  border: 1px solid black;\r\n" + "	}\r\n" + "\r\n" + ".results-table  td {\r\n"
				+ "	  background-color: #f8f7f4;\r\n" + "	}" + "	.results-table  tbody tr {\r\n"
				+ "	  border-bottom: 1px solid #dddddd;\r\n" + "	}\r\n" + "\r\n"
				+ "	.results-table  tr:nth-of-type(even) {\r\n" + "	  background-color: #f3f3f3;\r\n" + "	}\r\n"
				+ "\r\n" + "	.results-table  tbody tr:last-of-type {\r\n"
				+ "	  border-bottom: 2px solid #009879;\r\n" + "	}\r\n" + "	\r\n" + "		\r\n"
				+ "     </style>\r\n" + "	</head>\r\n"
				+ "	<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly;\">\r\n"
				+ "		<h1 style=\"font-family: 'Open Sans', sans-serif;\" align=\"center\">Execution Details</h1>\r\n"
				+ "		<table align=\"center\" class=\"results-table\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
				+ "			<tbody>\r\n" + "				<tr><th>Suite Name</th><td>#suiteName#</td> </tr>\r\n"
				+ "				<tr><th>Total Test Cases</th><td>#totalTestCasesAnchorTag#</td> </tr>\r\n"
				+ "				<tr><th>Passed Test Cases</th><td>#passedTestCasesAnchorTag#</td></tr>\r\n"
				+ "				<tr><th>Failed Test Cases</th><td>#failedTestCasesAnchorTag#</td></tr>	\r\n"
				+ "				<tr><th>Skipped Test Cases</th><td>#skippedTestCasesAnchorTag#</td> </tr>\r\n"
				+ "				<tr><th>Total Execution Time</th><td><b>#totalExecutionTime#</b></td></tr>\r\n"
				+ "				<tr><th>Report</th><td>#reportAnchorTag#</td></tr>\r\n"
				+ "			</tbody>			\r\n" + "		</table>\r\n" + "		<br />		\r\n"
				+ "		<table align='center' class=\"results-table\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
				+ "			<tbody>\r\n" + "				<tr><th>RNA Portal</th><td>#portalHost#</td> </tr>\r\n"
				+ "				<tr><th>Build Number</th><td><b>#buildNumber#</b></td> </tr>\r\n"
				+ "				<tr><th>Organization</th><td>#organization#</td> </tr>		\r\n"
				+ "				<tr><th>API Account Username</th><td>#apiAccountUserName#</td> </tr>		\r\n"
				+ "				<tr><th>Database</th><td style=\"text-align:left;\">#database#</td> </tr>	\r\n"
				+ "			</tbody>			\r\n" + "		</table>\r\n" + "		<br />		\r\n"
				+ "		<table align='center' class=\"results-table\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
				+ "			<thead>\r\n" + "				<tr>\r\n" + "					<th>S.No.</th>\r\n"
				+ "					<th>Module</th>\r\n" + "					<th>Total Test Cases</th>\r\n"
				+ "					<th>Passed Test Cases</th>\r\n" + "					<th>Failed Test Cases</th>\r\n"
				+ "					<th>Skipped Test Cases</th>\r\n" + "					<th>Duration</th>\r\n"
				+ "				</tr>\r\n" + "			</thead>\r\n" + "	    		<tbody>\r\n"
				+ "				#dynamicText#" + "			</tbody>\r\n" + "		</table>\r\n" + "	</body>\r\n"
				+ "</html>";
		try {
			String dynamicText = "";
			String template = "<tr>\r\n" + "				   <td>#sno#</td>\r\n"
					+ "				   <td>#Module#</td>\r\n"
					+ "				   <td><b><font color=\"blue\">#ModuleTotalTC#</font></b></td>\r\n"
					+ "				   <td><b><font color=\"green\">#ModuleTotalPassedTC#</font></b></td></td>\r\n"
					+ "				   <td><b><font color=\"red\">#ModuleTotalFailedTC#</font></b></td>\r\n"
					+ "				   <td><b><font color=\"brown\">#ModuleTotalSkippedTC#</font></b></td>\r\n"
					+ "				   <td>#Duration#</td>\r\n" + "				</tr>";
			int sno = 1;
			int passedCount = 0;
			int failedCount = 0;
			int skippedCount = 0;
			for (TestModule testModule : testModuleList) {
				passedCount = passedCount + testModule.getPassed();
				failedCount = failedCount + testModule.getFailed();
				skippedCount = skippedCount + testModule.getSkipped();
				dynamicText = dynamicText + template.replace("#sno#", String.valueOf(sno))
						.replace("#Module#", testModule.getModule().toLowerCase())
						.replace("#ModuleTotalTC#", String.valueOf(testModule.getTotal()))
						.replace("#ModuleTotalPassedTC#", String.valueOf(testModule.getPassed()))
						.replace("#ModuleTotalFailedTC#", String.valueOf(testModule.getFailed()))
						.replace("#ModuleTotalSkippedTC#", String.valueOf(testModule.getSkipped()))
						.replace("#Duration#", testModule.getDuration());
				sno++;
			}
			String portalLink = Config.getEnvDetails("report.portal");
			String totalTestCasesTag = null;
			String passedTestCasesTag = null;
			String failedTestCasesTag = null;
			String skippedTestCasesTag = null;
			String reportTag = null;
			if (entry.getTotal() > 0) {
				totalTestCasesTag = "<a href=\"" + portalLink
						+ "/MOTControllerServlet?action=getStatusScriptDtls&testNo=" + entry.getTestId() + "&module="
						+ URLEncoder.encode(entry.getTestName(), "UTF-8")
						+ "&hit=mail&status=all \"><font color=\"blue\"><b><u>" + entry.getTotal()
						+ "</u></b></font></a>";
			} else {
				totalTestCasesTag = "<font color=\"blue\"><b>" + entry.getTotal() + "</b></font>";
			}
			if (entry.getPassed() > 0) {
				passedTestCasesTag = "<a href=\"" + portalLink
						+ "/MOTControllerServlet?action=getStatusScriptDtls&testNo=" + entry.getTestId() + "&module="
						+ URLEncoder.encode(entry.getTestName(), "UTF-8")
						+ "&hit=mail&status=passed \"><font color=\"green\"><b><u>" + entry.getPassed()
						+ "</u></b></font></a>";
			} else {
				passedTestCasesTag = "<font color=\"green\"><b>" + entry.getPassed() + "</b></font>";
			}
			if (entry.getFailed() > 0) {
				failedTestCasesTag = "<a href=\"" + portalLink
						+ "/MOTControllerServlet?action=getStatusScriptDtls&testNo=" + entry.getTestId() + "&module="
						+ URLEncoder.encode(entry.getTestName(), "UTF-8")
						+ "&hit=mail&status=failed \"><font color=\"red\"><b><u>" + entry.getFailed()
						+ "</u></b></font></a>";
			} else {
				failedTestCasesTag = "<font color=\"red\"><b>" + entry.getFailed() + "</b></font>";
			}
			if (skippedCount > 0) {
				skippedTestCasesTag = "<a href=\"" + portalLink
						+ "/MOTControllerServlet?action=getStatusScriptDtls&testNo=" + entry.getTestId() + "&module="
						+ URLEncoder.encode(entry.getTestName(), "UTF-8")
						+ "&hit=mail&status=skipped \"><font color=\"brown\"><b><u>" + skippedCount
						+ "</u></b></font></a>";
			} else {
				skippedTestCasesTag = "<font color=\"brown\"><b>" + skippedCount + "</b></font>";
			}
			reportTag = "<a href=\"" + portalLink + "/MOTControllerServlet?action=reportView&testNo="
					+ entry.getTestId() + "\"><font color=\"blue\"><b><u>report.html</u></b></font></a>";

			htmlBody = htmlBody.replace("#dynamicText#", dynamicText);
			htmlBody = htmlBody.replace("#suiteName#", entry.getTestName());
			htmlBody = htmlBody.replace("#totalTestCasesAnchorTag#", totalTestCasesTag);
			htmlBody = htmlBody.replace("#passedTestCasesAnchorTag#", passedTestCasesTag);
			htmlBody = htmlBody.replace("#failedTestCasesAnchorTag#", failedTestCasesTag);
			htmlBody = htmlBody.replace("#skippedTestCasesAnchorTag#", skippedTestCasesTag);
			htmlBody = htmlBody.replace("#reportAnchorTag#", reportTag);
			htmlBody = htmlBody.replace("#totalExecutionTime#", entry.getTestDuration());
			htmlBody = htmlBody.replace("#portalHost#", BuildParams.PORTAL_APP_URL);
			htmlBody = htmlBody.replace("#buildNumber#", BuildParams.BUILD_NUMBER);
			/*htmlBody = htmlBody.replace("#organization#", BuildParams.ORG_SHORT_NAME);
			htmlBody = htmlBody.replace("#apiAccountUserName#", BuildParams.APIACCOUNT_USER_NAME);
			htmlBody = htmlBody.replace("#database#", "Host:" + BuildParams.APP_DB_HOST + ",<br />Port:"
					+ BuildParams.APP_DB_PORT + ",<br />Service:" + BuildParams.APP_DB_SID);
*/
		} catch (Exception e) {

		}
		return htmlBody;
	}

	private static String concatTestExecTimes(String strTime1, String strTime2) throws Exception {
		strTime1 = strTime1.replace("H", "").replace("M", "").replace("S", "").replace("ms", "");
		strTime2 = strTime2.replace("H", "").replace("M", "").replace("S", "").replace("ms", "");

		int totalHours = Integer.parseInt(strTime1.split(":")[0].trim())
				+ Integer.parseInt(strTime2.split(":")[0].trim());
		int totalMinutes = Integer.parseInt(strTime1.split(":")[1].trim())
				+ Integer.parseInt(strTime2.split(":")[1].trim());
		int totalSeconds = Integer.parseInt(strTime1.split(":")[2].trim())
				+ Integer.parseInt(strTime2.split(":")[2].trim());
		int totalMilliSeconds = Integer.parseInt(strTime1.split(":")[3].trim())
				+ Integer.parseInt(strTime2.split(":")[3].trim());

		if (totalMilliSeconds >= 1000) {
			totalSeconds++;
			totalMilliSeconds = totalMilliSeconds % 1000;
		}
		if (totalSeconds >= 60) {
			totalMinutes++;
			totalSeconds = totalSeconds % 60;
		}
		if (totalMinutes >= 60) {
			totalHours++;
			totalMinutes = totalMinutes % 60;
		}
		String finalTime = totalHours + "H : " + totalMinutes + "M : " + totalSeconds + "S : " + totalMilliSeconds
				+ "ms";
		return finalTime;
	}

	private static List<TestModule> prepareModuleWisePojo(List<TestRun> testScriptList) throws Exception {
		List<TestModule> lstModules = new ArrayList<TestModule>();
		TestModule testModule = null;
		List<String> lstOnlyModules = new ArrayList<String>();

		for (TestRun testScript : testScriptList) {
			String moduleName = testScript.getModule();
			if (lstOnlyModules.size() == 0)
				lstOnlyModules.add(moduleName);
			else if (!lstOnlyModules.contains(moduleName))
				lstOnlyModules.add(moduleName);
		}
		for (String module : lstOnlyModules) {
			testModule = new TestModule();
			for (TestRun testScript : testScriptList) {
				if (testScript.getModule().equalsIgnoreCase(module)) {
					if (testModule.getModule() == null)
						testModule.setModule(module);
					if (testModule.getDuration() == null)
						testModule.setDuration(testScript.getDuration());
					else
						testModule.setDuration(concatTestExecTimes(testScript.getDuration(), testModule.getDuration()));
					if (testScript.getStatus().equalsIgnoreCase("pass")) {
						testModule.setPassed(testModule.getPassed() + 1);
						testModule.setTotal(testModule.getTotal() + 1);
					} else if (testScript.getStatus().equalsIgnoreCase("fail")) {
						testModule.setFailed(testModule.getFailed() + 1);
						testModule.setTotal(testModule.getTotal() + 1);
					} else if (testScript.getStatus().equalsIgnoreCase("skip")) {
						testModule.setSkipped(testModule.getSkipped() + 1);
						testModule.setTotal(testModule.getTotal() + 1);
					}
				}
			}
			lstModules.add(testModule);
		}
		return lstModules;
	}

	// Older method
	/*
	 * private static String prepareHtmlbodyWithoutDBInsert(TestName entry,
	 * List<TestRun> testScriptList) throws Exception { String htmltext =
	 * "<html>\n<head>\n" +
	 * "<style>\ntable, th, td { \nborder-collapse: collapse; \nborder: 1px solid black;\ntext-align: center; \npadding: 5px; \n}\nth { \nborder-collapse: collapse; \nborder: 1px solid black;\ntext-align: center; \npadding: 5px; \nbackground:#F78E7C; \ncolor:white; \n}\n</style> \n</head>\n"
	 * + "<body>\n<h2>Execution Details:</h2>\n" + "<table>\n<tbody>\n<tr>\n" +
	 * "<th>Suite : " + entry.getTestName() + "</th>\n" + "<th>Build : " +
	 * entry.getBuildNumber() + "</th>\n" + "<th>Duration : " +
	 * entry.getTestDuration() + "</th>\n</tr>\n" + "<tr>\n" + "<th>S.No.</th>\n" +
	 * "<th>Module</th>\n <th>Test Name</th>\n <th>Duration</th>\n <th>Status</th>\n"
	 * ; int sno = 1; for (TestRun testScript : testScriptList) { String
	 * strFontColor = null; if (testScript.getStatus().equalsIgnoreCase("pass"))
	 * strFontColor = "green"; else strFontColor = "red"; htmltext = htmltext +
	 * "<tr> \n<td>" + sno +
	 * "</td> <td style=\"text-align:left;\">"+testScript.getModule().toLowerCase()
	 * +"</td>    <td style=\"text-align:left;\">" + testScript.getScriptName() +
	 * "</td><td>"+testScript.getDuration()+"</td> <td><font color=" + strFontColor
	 * + ">" + testScript.getStatus().toUpperCase() + "</td>\n</tr>"; sno++; }
	 * 
	 * htmltext = htmltext + "</body>\n</html>";
	 * 
	 * return htmltext; }
	 */

	private static String prepareHTMLBodyWithoutDbInsert(TestName entry, List<TestRun> testScriptList) {

		String htmlBody = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
				+ "  <html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\r\n"
				+ "  <head>\r\n" + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\r\n"
				+ "  <meta name=\"format-detection\" content=\"date=no\"/>\r\n"
				+ "  <meta name=\"format-detection\" content=\"telephone=no\"/>\r\n" + "     <!--[if !mso]><!-- -->\r\n"
				+ "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n" + "     <!--<![endif]-->\r\n"
				+ "<html>\r\n" + "	<head>\r\n" + "	<style>\r\n" + "    #outlook a{\r\n" + "        padding:0;\r\n"
				+ "    }\r\n" + "    .ReadMsgBody{\r\n" + "        width:100%;\r\n" + "    }\r\n" + "    body{\r\n"
				+ "        width:100% !important;\r\n" + "        min-width:100%;\r\n"
				+ "        -webkit-text-size-adjust:100%;\r\n" + "        -ms-text-size-adjust:100%;\r\n"
				+ "        -webkit-font-smoothing: antialiased;\r\n" + "    }\r\n" + "    v*{\r\n"
				+ "        behavior:url(#default#VML);\r\n" + "        display:inline-block;\r\n" + "    }\r\n"
				+ "    .ExternalClass{\r\n" + "        width:100%;\r\n" + "    }\r\n" + "    td{\r\n"
				+ "        border-collapse: collapse!important;\r\n" + "    }\r\n" + "    .ExternalClass,\r\n"
				+ "    .ExternalClass p,\r\n" + "    .ExternalClass span,\r\n" + "    .ExternalClass font,\r\n"
				+ "    .ExternalClass td,\r\n" + "    .ExternalClass div{\r\n" + "        line-height:100%;\r\n"
				+ "    }\r\n" + "    a img{\r\n" + "        border:none;\r\n" + "    }\r\n" + "    a {\r\n"
				+ "        text-decoration:none !important;\r\n" + "    }\r\n" + "    img{\r\n"
				+ "        display:block;\r\n" + "        outline:none;\r\n" + "        text-decoration:none;\r\n"
				+ "        border:none; \r\n" + "         -ms-interpolation-mode: bicubic;\r\n" + "    }\r\n"
				+ "    table{\r\n" + "        border-spacing:0;\r\n"
				+ "        border-collapse: separate !important;\r\n" + "        mso-table-lspace:0pt;\r\n"
				+ "        mso-table-rspace:0pt;\r\n" + "    }\r\n" + "	.results-table {\r\n"
				+ "    	font-family: 'Open Sans', sans-serif;\r\n" + "		border-collapse: collapse;\r\n"
				+ "		font-size: 0.9em;\r\n" + "		min-width: 400px;\r\n" + "		overflow:auto;\r\n"
				+ "		box-shadow: 0 0 30px 0px;\r\n" + "	}\r\n" + "	.results-table  th {\r\n"
				+ "	  background-color: #0b7670;\r\n" + "	  color: #ffffff;\r\n" + "	  text-align: center;\r\n"
				+ "	  font-weight: bold;\r\n" + "	}\r\n" + "\r\n" + "	.results-table  th,\r\n"
				+ "	.results-table  td {\r\n" + "	  padding: 12px 15px;\r\n" + "	  text-align: center;\r\n"
				+ "	  border: 1px solid black;\r\n" + "	}\r\n" + "\r\n" + ".results-table  td {\r\n"
				+ "	  background-color: #f8f7f4;\r\n" + "	}" + "	.results-table  tbody tr {\r\n"
				+ "	  border-bottom: 1px solid #dddddd;\r\n" + "	}\r\n" + "\r\n"
				+ "	.results-table  tr:nth-of-type(even) {\r\n" + "	  background-color: #f3f3f3;\r\n" + "	}\r\n"
				+ "\r\n" + "	.results-table  tbody tr:last-of-type {\r\n"
				+ "	  border-bottom: 2px solid #009879;\r\n" + "	}\r\n" + "	\r\n" + "		\r\n"
				+ "     </style>\r\n" + "	</head>\r\n"
				+ "	<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly;\">\r\n"
				+ "		<h1 style=\"font-family: 'Open Sans', sans-serif;\" align=\"center\">Execution Details</h1>\r\n"
				+ "		<table align=\"center\" class=\"results-table\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
				+ "			<tbody>\r\n" + "				<tr><th>Suite Name</th><td>#suiteName#</td> </tr>\r\n"
				+ "				<tr><th>Total Testcases</th><td><b><font color=\"blue\">#totalTestCases#</font></b></td> </tr>\r\n"
				+ "				<tr><th>Passed Testcases</th><td><b><font color=\"green\">#passedTestCases#</font></b></td></tr>\r\n"
				+ "				<tr><th>Failed TestCases</th><td><b><font color=\"red\">#failedTestCases#</font></b></td></tr>	\r\n"
				+ "				<tr><th>Skipped TestCases</th><td><b><font color=\"brown\">#skippedTestCases#</font></b></td> </tr>\r\n"
				+ "				<tr><th>Total Execution Time</th><td><b>#totalExecutionTime#</b></td></tr>\r\n"
				+ "			</tbody>			\r\n" + "		</table>\r\n" + "		<br />		\r\n"
				+ "		<table align='center' class=\"results-table\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
				+ "			<tbody>\r\n" + "				<tr><th>RNA Portal</th><td>#portalHost#</td> </tr>\r\n"
				+ "				<tr><th>Build Number</th><td><b>#buildNumber#</b></td> </tr>\r\n"
				+ "				<tr><th>Organization</th><td>#organization#</td> </tr>		\r\n"
				+ "				<tr><th>API Account Username</th><td>#apiAccountUserName#</td> </tr>		\r\n"
				+ "				<tr><th>Database</th><td style=\"text-align:left;\">#database#</td> </tr>	\r\n"
				+ "			</tbody>			\r\n" + "		</table>\r\n" + "		<br />		\r\n"
				+ "		<table align='center' class=\"results-table\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
				+ "			<thead>\r\n" + "				<tr>\r\n" + "					<th>S.No.</th>\r\n"
				+ "					<th>Module</th>\r\n" + "					<th>Test Name</th>\r\n"
				+ "					<th>Duration</th>\r\n" + "					<th>Status</th>\r\n"
				+ "				</tr>\r\n" + "			</thead>\r\n" + "	    		<tbody>\r\n"
				+ "				#dynamicText#" + "			</tbody>\r\n" + "		</table>\r\n" + "	</body>\r\n"
				+ "</html>";
		try {
			String dynamicText = "";
			String template = "<tr>\r\n" + "				   <td>#sno#</td>\r\n"
					+ "				   <td>#Module#</td>\r\n"
					+ "				   <td style=\"text-align:left;\">#TestName# </td>\r\n"
					+ "				   <td>#Duration#</td>\r\n"
					+ "				   <td><b><font color=\"#strFontColor#\">#Status#</b></td>\r\n"
					+ "				</tr>";
			int sno = 1;
			int passedCount = 0;
			int failedCount = 0;
			int skippedCount = 0;
			for (TestRun testScript : testScriptList) {
				String strFontColor = null;
				if (testScript.getStatus().equalsIgnoreCase("pass")) {
					strFontColor = "green";
					passedCount++;
				} else if (testScript.getStatus().equalsIgnoreCase("fail")) {
					strFontColor = "red";
					failedCount++;
				} else {
					skippedCount++;
				}

				dynamicText = dynamicText + template.replace("#sno#", String.valueOf(sno))
						.replace("#Module#", testScript.getModule().toLowerCase())
						.replace("#TestName#", testScript.getScriptName())
						.replace("#Duration#", testScript.getDuration()).replace("#strFontColor#", strFontColor)
						.replace("#Status#", testScript.getStatus());
				sno++;
			}

			htmlBody = htmlBody.replace("#dynamicText#", dynamicText);
			htmlBody = htmlBody.replace("#suiteName#", entry.getTestName());
			htmlBody = htmlBody.replace("#totalTestCases#", String.valueOf(testScriptList.size()));
			htmlBody = htmlBody.replace("#passedTestCases#", String.valueOf(passedCount));
			htmlBody = htmlBody.replace("#failedTestCases#", String.valueOf(failedCount));
			htmlBody = htmlBody.replace("#skippedTestCases#", String.valueOf(skippedCount));
			htmlBody = htmlBody.replace("#totalExecutionTime#", entry.getTestDuration());
			htmlBody = htmlBody.replace("#portalHost#", BuildParams.PORTAL_APP_URL);
			htmlBody = htmlBody.replace("#buildNumber#", BuildParams.BUILD_NUMBER);
			/*htmlBody = htmlBody.replace("#organization#", BuildParams.ORG_SHORT_NAME);
			htmlBody = htmlBody.replace("#apiAccountUserName#", BuildParams.APIACCOUNT_USER_NAME);
			htmlBody = htmlBody.replace("#database#", "Host:" + BuildParams.APP_DB_HOST + ",<br />Port:"
					+ BuildParams.APP_DB_PORT + ",<br />Service:" + BuildParams.APP_DB_SID);
*/
		} catch (Exception e) {

		}

		return htmlBody;
	}

}
