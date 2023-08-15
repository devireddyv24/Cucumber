package com.weichertwm.qa.util;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.weichertwm.qa.framework.Config;
import com.weichertwm.qa.framework.Log;


public class SQLQueryReader {

	private static final HashMap<String, String> sqlQueryReader = new HashMap<String, String>();

	/**
	 * @description Used to load all SQL Queries from the SQLQueries.xml
	 */
	public synchronized static void loadAllSQLQueries() {
		try {
			
			Log.debug("[loadAllSQLQueries] Loading All SQL Queries to HashMap........");
			String sqlQueryXMLFile = System.getProperty("user.dir") + Config.getEnvDetails("sqlQueriesFile");
			File fXmlFile = new File(sqlQueryXMLFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			String rootElementName = doc.getDocumentElement().getNodeName();
			Log.debug("[sqlQueryReader] Root element :" + rootElementName);

			Element rootElement = doc.getDocumentElement();

			// Read all childNodes of Rootelement
			NodeList rootElementChilds = rootElement.getChildNodes();

			for (int temp = 0; temp < rootElementChilds.getLength(); temp++) {
				Node nNode = rootElementChilds.item(temp);
				if (!nNode.getNodeName().equalsIgnoreCase("#text")) {
					Log.debug("[sqlQueryReader] Current Child Element of  :" + doc.getDocumentElement().getNodeName() + " is "+ nNode.getNodeName());

					// Getting all child elements of Child Nodes
					NodeList nList = nNode.getChildNodes();
					for (int temp1 = 0; temp1 < nList.getLength(); temp1++) {
						Node childNode = nList.item(temp1);
						if (!childNode.getNodeName().equalsIgnoreCase("#text")) {
							
							//Verify the childNode is CDATA Encrypted
							if(childNode instanceof CharacterData)
								sqlQueryReader.put(nNode.getNodeName() + "." + ((Element) childNode).getAttribute("name"), ((CharacterData) childNode).getData());
							else
								sqlQueryReader.put(nNode.getNodeName() + "." + ((Element) childNode).getAttribute("name"),childNode.getTextContent());
						}
							
					}
				}
			}
			Log.debug("[sqlQueryReader] is successfully processed.  All sql queries are loaded.");
		} catch (Exception e) {
			Log.error("[sqlQueryReader] is failed to load.");
			e.printStackTrace();
		}
	}
	
	/**
	 * @description Used to read SQL Query.
	 * @param keyName -- keyName will be parentTag.requiredQuery
	 * @example reportMail.GetDayOfMonth
	 * @return
	 */
	public static String readSQLQuery(String keyName) {
		String retValue="";
		loadAllSQLQueries();
		try {
			if(sqlQueryReader.isEmpty()) {
				Log.error("[readSQLQuery] No Queries available");
			}
			if (sqlQueryReader.containsKey(keyName)) {
				retValue= sqlQueryReader.get(keyName);
				Log.debug("Key:"+keyName+" and Value:"+retValue);
			}
		} catch (Exception e) {
			Log.error("[readSQLQuery] Unable to get the Query for key:"+keyName);
		}
		return retValue;	
	} 
}