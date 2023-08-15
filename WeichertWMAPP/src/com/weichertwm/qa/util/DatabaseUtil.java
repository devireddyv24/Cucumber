package com.weichertwm.qa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.weichertwm.qa.framework.ExtentReport;
import com.weichertwm.qa.framework.Log;
import com.weichertwm.qa.framework.Status;
import com.weichertwm.qa.util.Table;

public class DatabaseUtil {
	
	private static Connection connection = null;
	
	private static HashMap<String, String[]> mapDatabase = new HashMap<String, String[]>();
	private static HashMap<String, Connection> mapDBConnections = new HashMap<String, Connection>();
	
	public synchronized static Connection getDBConnection(String host, String port, String sid, String uname, String pwd) throws Exception {
		String conString = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
		Log.info("Creating Connection with db String "+conString);
		return getDBConnection("oracle.jdbc.driver.OracleDriver",conString, uname, pwd);
	}
	public synchronized static Connection getDBConnection(String host, String port, String sid, String uname, String pwd,String dbtype) throws Exception {
		Connection connection = null;
		if(dbtype.equalsIgnoreCase("oracle"))
		{
			//String conString = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
			String conString = "jdbc:oracle:thin:@//" + host + ":" + port + "/" + sid;
			Log.info("Creating Connection with db String "+conString);
			connection= getDBConnection("oracle.jdbc.driver.OracleDriver",conString, uname, pwd);
		}
		else if(dbtype.equalsIgnoreCase("mssql"))
		{
			String conString = "jdbc:jtds:sqlserver://" + host + ":" + port + "/" + sid;
			Log.info("Creating Connection with db String "+conString);
			connection= getDBConnection("net.sourceforge.jtds.jdbc.Driver",conString, uname, pwd);
		}	
		return connection;
	}
	/**
	 * This method is used to connect to database with the connection string arguments
	 * @param strClassName
	 * @param strJDBCURL
	 * @param strUsername
	 * @param strPassword
	 * @return new connection
	 * @throws Exception 
	 */
	public synchronized static Connection getDBConnection(String strClassName, String strJDBCURL, String strUsername, String strPassword) throws Exception {
		try {
			if (connection != null && connection.isValid(5)) {
				return connection;
			}
			if (!strJDBCURL.contains("timesten")) {
				Class.forName(strClassName);
				connection = DriverManager.getConnection(strJDBCURL, strUsername, strPassword);
			} else {
				
			}			
		} catch (SQLException e) {
			throw new Exception(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new Exception("Could not find class by name "+strClassName);
		}
		return connection;
	}
	
	/**
	 * This method is used to execute the query on the specified connection  
	 * @param strQueryString
	 * @param con
	 * @return result set
	 * @throws Exception
	 */
	public synchronized static ResultSet executeQuery(String strQueryString, Connection con) throws Exception {
		//ExtentReport.log("Executing query <pre>"+strQueryString+"</pre>",Status.INFO, null);
		Log.info("Executing query - "+strQueryString);
		ResultSet rs = null;
		Statement stmt;
		try {
			stmt = con.createStatement(
				    ResultSet.TYPE_SCROLL_INSENSITIVE,
				    ResultSet.CONCUR_READ_ONLY				   
				);
			rs = stmt.executeQuery(strQueryString);
		} catch (SQLException e) {
			Log.error(e.getMessage());
			con.close();
		}
		return rs;
	}
	/**
	 * This method will execute the query on specified data base (query string)
	 * @param strDatabaseId
	 * @param strQueryString
	 * @return table with complete physical table data
	 * @throws Exception
	 */
	public synchronized static Table executeQuery(String strDatabaseId, String strQueryString) throws Exception {
		connect(strDatabaseId);		
		ExtentReport.log("Executing query "+strQueryString+" on "+strDatabaseId,Status.INFO, null);
		ResultSet rsTemp = executeQuery(strQueryString,mapDBConnections.get(strDatabaseId));
		Log.info("Query execution was successful");
		Table tblTemp =  new Table(rsTemp);
		rsTemp.close();
		
		return tblTemp;
	}
	
	/**
	 * Store database details
	 * 
	 * @param strDatabaseId
	 * @param strClassName
	 * @param strJDBCURL
	 * @param strUsername
	 * @param strPassword
	 */
	public synchronized static void defineDatabase(String strDatabaseId, String strClassName, String strJDBCURL, String strUsername, String strPassword) {
		Log.trace("==> Entering defineDatabase");
		String[] arrDBDetails = new String[4];
		arrDBDetails[0] = strClassName;
		arrDBDetails[1] = strJDBCURL;
		arrDBDetails[2] = strUsername;
		arrDBDetails[3] = strPassword;
		
		mapDatabase.put(strDatabaseId, arrDBDetails);
		Log.trace("==> Exiting defineDatabase");
	}
	
	public synchronized static boolean connect(String strDatabaseId) throws Exception {
		if (!mapDatabase.containsKey(strDatabaseId)) {
			Log.error("Database by Id ["+strDatabaseId+"] is not defined");
			ExtentReport.log("Database by Id ["+strDatabaseId+"] is not defined", Status.FATAL, null);
			throw new Exception("Database by Id ["+strDatabaseId+"] is not defined");
		}
		
		//Do not create new connection if an existing connection already exists
		//_ or if the connection has been closed
		if (mapDBConnections.containsKey(strDatabaseId)) {
			if (!(mapDBConnections.get(strDatabaseId) == null 
					|| mapDBConnections.get(strDatabaseId).isClosed())) {
				return true;
			}
		} 
		
		String[] arrDetails = mapDatabase.get(strDatabaseId);
		Log.info("Establishing connection to database by Id ["+strDatabaseId+"]");
		ExtentReport.logInfo( "Establishing connection to database by Id ["+strDatabaseId+"]");
		Connection connDatabase =  getDBConnection(arrDetails[0], arrDetails[1], arrDetails[2], arrDetails[3]);
		mapDBConnections.put(strDatabaseId, connDatabase);
		return true;
	}
	
	/**
	 * <b>Description</b> Close Database Connection
	 *  	
	 * @throws         Exception   Exception
	 * 
	 */
	public synchronized static void closeDatabase() throws Exception {		
		connection.close();		
	}
	
	/**
	 * This method is used to just disconnect the database previously connected
	 * @param strDatabaseId
	 * @throws SQLException
	 */
	public synchronized static void disconnect(String strDatabaseId) throws SQLException {
		if (mapDBConnections.containsKey(strDatabaseId)) {
			if (!(mapDBConnections.get(strDatabaseId) == null 
					|| mapDBConnections.get(strDatabaseId).isClosed())) {
				mapDBConnections.get(strDatabaseId).close();
			}
		}
	}
	
	/**
	 * This method is used to update the db on specified connection
	 * 
	 * @param Query
	 * @param connDB
	 * @return 
	 * @throws Exception
	 */
	public synchronized static int updateQuery(String Query, Connection connDB) throws Exception {
		int updateCount = -1;
		
		try {
			Statement StmtCheck = null;
			StmtCheck = connDB.createStatement();
			Log.info("[updateSQLQry] Updating the records +" + Query);
			System.out.println("[updateSQLQry] Updating the records +" + Query);
			updateCount = StmtCheck.executeUpdate(Query);
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return updateCount;

	}

	public static int executeUpdate(String strDatabaseId, String strQueryString) throws Exception {
		connect(strDatabaseId);
		ExtentReport.log("Executing update statement "+strQueryString+" on "+strDatabaseId, Status.INFO, null);
		return updateQuery(strQueryString,mapDBConnections.get(strDatabaseId));
	}

	public static int executeInsert(String strDatabaseId, String strQueryString) throws Exception {
		return executeUpdate(strDatabaseId,strQueryString);
	}
	
}
