package com.weichertwm.qa.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import com.weichertwm.qa.framework.Config;
import com.weichertwm.qa.framework.ExtentReport;

public class EmailUtil {
	public Message[] getAllEmailMessags(String host, String storeType, String username,  String password) throws  Exception 
	{
		Properties properties = new Properties();
		Session emailSession;
		Store store;
		Folder emailFolder;
		Message[] messages = null;
		try {

			properties.put("mail.pop3.host", host);
			properties.put("mail.pop3.port", "110");
			//properties.put("mail.pop3.starttls.enable", "true");
			
			// Email connection to the host
			System.out.println("[getAllEmailMessages]: Connecting to Email Server....");
			emailSession = Session.getDefaultInstance(properties);
			System.out.println("Email Session has been set");
			store = emailSession.getStore("pop3");
			System.out.println("Store has been set");
			try {
				System.out.println(host+" "+username+" "+password);
				store.connect(host, username, password);

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("[getAllEmailMessages]: Connected to Email Server Successfully. UserName: "+username+", Password: "+password);
			
			//Reads All Emails from Inbox
			emailFolder = store.getFolder("Inbox");
			System.out.println(emailFolder.getURLName());
			emailFolder.open(Folder.READ_ONLY);
			messages = emailFolder.getMessages();
			System.out.println("[getAllEmailMessages]: Retrieved all Email Messages");

		} catch (NullPointerException e) {  //catch NullPointerException
			ExtentReport.logFail("[getAllEmailMessages] Failed:" +e.getMessage()+"Incorrect data passed for Host,StoreType,Username,Password:"+host+","+storeType+","+username+","+password);
		}catch (NoSuchProviderException e) {
			ExtentReport.logFail("[getAllEmailMessages] Failed:" +e.getMessage());
		} catch (MessagingException e) {
			ExtentReport.logFail("[getAllEmailMessages] Failed:" +e.getMessage());
		} catch (Exception e) {
			ExtentReport.logFail("[getAllEmailMessages] Failed:" +e.getMessage());
		}
		if(messages.length!=0)
		{
			ExtentReport.logPass("[getAllEmailMessages] Passed: Found "+messages.length+" messages");
			return messages;
		}
		else
		{
			ExtentReport.logFail("[getAllEmailMessages] Failed: No Messages Found "+messages.length+" messages");
			return null;
		}
	}
	
	/**
	 * 
	 * @param messages
	 * @param from
	 * @return Messages[]
	 * @throws AbstractScriptException
	 * @throws Exception
	 * @example 
	 * 	getEmailsFilterByFrom(messages[],"example@oracle.com");
	 * 
	 */

	public Message[]  getEmailsFilterByFrom(Message[] messages, String from) throws  Exception
	{
		//check for not null - Fail if values are null
		if(messages.length==0 || from.length()==0)
		{
			ExtentReport.logFail("[getEmailsFilterByFrom] Failed: No messages found or Incorrect From Address");
		}		
		
		ArrayList<Message> tempMessages=new ArrayList<Message>();
//		int count=0;
		
		//Filter Email Messages by From Email Address
		if(messages.length!=0 && from.length()!=0)
		{
			for(int i=0;i<messages.length;i++)
			{
				if(((Address)messages[i].getFrom()[0]).toString().contains(from))
					tempMessages.add(messages[i]);
			}
		}
		else
		{
			ExtentReport.logFail("[getRequiredEmailsByFrom] Failed: Messages recieved are empty "+messages.length+" messages");
			return null;
		}
		
		//Fails if no emails filtered by from address found
		if(tempMessages.size()==0)
			ExtentReport.logFail("[getRequiredEmailsByFrom] Failed: No Messages Found "+tempMessages.size()+" messages");
		Message[] reqMessages = new Message[tempMessages.size()];
		for(int k=0;k<tempMessages.size();k++)
			reqMessages[k]=tempMessages.get(k);
		
		//Returns all Emails when Emails filtered by from address found
		if(reqMessages.length!=0)
		{
			ExtentReport.logPass("[getRequiredEmailsByFrom] Passed: Found "+reqMessages.length+" messages");
			return reqMessages;
		}
		else
		{
			ExtentReport.logFail("[getRequiredEmailsByFrom] Failed: No Messages Found "+reqMessages.length+" messages");
			return null;
		}
	}

	/**
	 * 
	 * @param messages
	 * @param subject
	 * @return Messages[]
	 * @throws AbstractScriptException
	 * @throws Exception
	 * @example 
	 * 	getEmailsByPartialSub(messages[],"myMicros.net");
	 * 
	 */
	
	public Message[] getEmailByPartialSub(Message[] messages, String subject) throws  Exception
	{
		//check for not null - Fail if values are null
		if(messages.length==0 || subject.length()==0)
		{
			ExtentReport.logFail("[getEmailsByPartialSub] Failed: No messages found or Incorrect Subject");
		}
		ArrayList<Message> tempMessages=new ArrayList<Message>();
//		int count=0;
		
		//Filter Email Messages Filter By Partial Subject
		if(messages.length!=0 && subject.length()!=0)
		{
			for(int i=0;i<messages.length;i++)
			{
				if(messages[i].getSubject().contains(subject))
					tempMessages.add(messages[i]);
			}
		}		
		else
		{
			ExtentReport.logFail("[getEmailsByPartialSub] Failed: Messages recieved are empty "+messages.length+" messages");
			return null;
		}
		
		//Fails if no emails filtered by Partial Subject found
		if(tempMessages.size()==0)
			ExtentReport.logFail("[getEmailsByPartialSub] Failed: No Messages Found "+tempMessages.size()+" messages");
		Message[] reqMessages = new Message[tempMessages.size()];
		for(int k=0;k<tempMessages.size();k++)
			reqMessages[k]=tempMessages.get(k);
		
		//Returns all Emails when Emails filtered by Partial Subject found
		if(reqMessages.length!=0)
		{
			ExtentReport.logPass("[getEmailsByPartialSub] Passed: Found "+reqMessages.length+" messages");
			return reqMessages;
		}
		else
		{
			ExtentReport.logFail("[getEmailsByPartialSub] Failed: No Messages Found "+reqMessages.length+" messages");
			return null;
		}
	}
	
	public Message[] getEmailByPartialReceivedDate(Message[] messages, String date) throws  Exception
	{
		//check for not null - Fail if values are null
		if(messages.length==0 || date.length()==0)
		{
			ExtentReport.logFail("[getEmailsByPartialSub] Failed: No messages found or Incorrect Subject");
		}
		ArrayList<Message> tempMessages=new ArrayList<Message>();
//		int count=0;
		
		//Filter Email Messages Filter By Partial Subject
		if(messages.length!=0 && date.length()!=0)
		{
			for(int i=0;i<messages.length;i++)
			{
				System.out.println(messages[i].getReceivedDate().toString());
				if(messages[i].getReceivedDate().toString().contains(date))
					tempMessages.add(messages[i]);
			}
		}		
		else
		{
			ExtentReport.logFail("[getEmailsByPartialSub] Failed: Messages recieved are empty "+messages.length+" messages");
			return null;
		}
		
		//Fails if no emails filtered by Partial Subject found
		if(tempMessages.size()==0)
			ExtentReport.logFail("[getEmailsByPartialSub] Failed: No Messages Found "+tempMessages.size()+" messages");
		Message[] reqMessages = new Message[tempMessages.size()];
		for(int k=0;k<tempMessages.size();k++)
			reqMessages[k]=tempMessages.get(k);
		
		//Returns all Emails when Emails filtered by Partial Subject found
		if(reqMessages.length!=0)
		{
			ExtentReport.logPass("[getEmailsByPartialSub] Passed: Found "+reqMessages.length+" messages");
			return reqMessages;
		}
		else
		{
			ExtentReport.logFail("[getEmailsByPartialSub] Failed: No Messages Found "+reqMessages.length+" messages");
			return null;
		}
	}

	/**
	 * 
	 * @param messages
	 * @param subject
	 * @return Messages[]
	 * @throws AbstractScriptException
	 * @throws Exception
	 * @example 
	 * 	getEmailsBySubExactMatch(messages[],"myMicros.net - Helpdesk");
	 * 
	 */
	
	public Message[]  getEmailBySubExactMatch(Message[] messages, String subject) throws  Exception
	{
		//check for not null - Fail if values are null
		if(messages.length==0 || subject.length()==0)
		{
			ExtentReport.logFail("[getEmailsBySubExactMatch] Failed: No messages found or Incorrect Subject");
		}
		ArrayList<Message> tempMessages=new ArrayList<Message>();
//		int count=0;
		
		//Filter Email Messages Filter By Exact Subject
		if(messages.length!=0 && subject.length()!=0)
		{
			for(int i=0;i<messages.length;i++)
			{
				if(messages[i].getSubject().equalsIgnoreCase(subject))
					tempMessages.add(messages[i]);
			}
		}		
		else
		{
			ExtentReport.logFail("[getEmailsBySubExactMatch] Failed: Messages recieved are empty "+messages.length+" messages");
			return null;
		}
		
		//Fails if no emails filtered by Exact Subject found
		if(tempMessages.size()==0)
			ExtentReport.logFail("[getEmailsBySubExactMatch] Failed: No Messages Found "+tempMessages.size()+" messages");
		Message[] reqMessages = new Message[tempMessages.size()];
		for(int k=0;k<tempMessages.size();k++)
			reqMessages[k]=tempMessages.get(k);
		
		//Returns all Emails when Emails filtered by Exact Subject found
		if(reqMessages.length!=0)
		{
			ExtentReport.logPass("[getEmailsBySubExactMatch] Passed: Found "+reqMessages.length+" messages");
			return reqMessages;
		}
		else
		{
			ExtentReport.logFail("[getEmailsBySubExactMatch] Failed: No Messages Found "+reqMessages.length+" messages");
			return null;
		}
	}

	/**
	 * 
	 * @param messages
	 * @return Message
	 * @throws AbstractScriptException
	 * @throws Exception
	 * @example 
	 * 	getLatestEmailMessage(messages[]);
	 * 
	 */
	
	public Message getLatstEmailMessage(Message[] messages) throws  Exception
	{
		
		//Retrieves the Latest Message 
		if(messages.length!=0)
		{
			int reqMsgNum=messages.length-1;
			System.out.println("[getLatestEmailMessage]: Latest Email Message Number "+reqMsgNum);
			Message reqMessage=messages[reqMsgNum];
			ExtentReport.logPass("[getLatestEmailMessage] Passed: Latest Email message found ");
			return reqMessage;
		}
		
		//Fails if no messages are passed
		else
		{
			ExtentReport.logFail("[getLatestEmailMessage] Failed: No messages found");
			return null;
		}
	}

	/**
	 * 
	 * @param message
	 * @return String
	 * @throws AbstractScriptException
	 * @throws Exception
	 * @example 
	 * 	getMessageBodyContent(message);
	 * 
	 */
	
	public String getMessgeBodyContent(Message message) throws  Exception
	{
		//Check for the nullity of passed message
		if(message==null)
		{
			ExtentReport.logFail("[getMessageBodyContent] Failed: Incorrect Data ");
		}
		String bodyContent="";
		
		//Retrieve Body of the Email Message is Multipart Type
		if(message.getContent() instanceof Multipart)
		{
			Multipart multipart = (Multipart) message.getContent();
			if(multipart.getCount()==0)
				ExtentReport.logFail("[getMessageBodyContent] Failed: No messages found");

			for (int j = 0; j < multipart.getCount(); j++) 
			{
				BodyPart bodyPart = multipart.getBodyPart(j);
				String disposition = bodyPart.getDisposition();
				
				//Check if the Email has an attachment
				if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) 
				{ 
					System.out.println("Mail have some attachment");
					DataHandler handler = bodyPart.getDataHandler();
					System.out.println("file name : " + handler.getName());                                 
				}
				
				// Read the Body of Email Message of Multipart Type 
				else 
				{ 
					bodyPart = ((Multipart) message.getContent()).getBodyPart(0);
					bodyContent = bodyPart.getContent().toString();        
				}
			}
		}
		// Read the Body of Email Message of plain Text Type
		else 
		{
			bodyContent=message.getContent().toString();
		}
		if(bodyContent.length()!=0)
		{
			ExtentReport.logPass("[getMessageBodyContent] Passed: Body Content is \n"+bodyContent);
			return bodyContent;
		}
		else
		{
			ExtentReport.logFail("[getMessageBodyContent] Failed: No messages found");
			return null;
		}
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param from
	 * @param subject
	 * @return Message
	 * @throws AbstractScriptException
	 * @throws Exception
	 * @example 
	 * 	getEmailMessage("mylabor_web","pasword1","example@oracle.com","");
	 * 	getEmailMessage("mylabor_web","pasword1","","myMicros.net - Helpdesk");
	 * 	getEmailMessage("mylabor_web","pasword1","example@oracle.com","myMicros.net - Helpdesk");
	 * 	getEmailMessage("mylabor_web","pasword1","","");
	 * 
	 */
	
	public Message getEmailMessage(String username, String password, String from, String subject ) throws  Exception
	{

		//Read values from the Environment.xml
		String host = Config.getEnvDetails("mail.hostName");
		String mailStoreType =Config.getEnvDetails("mail.storeType");
		
		//Check for not null values
		if(host.length()==0 || mailStoreType.length()==0 || username.length()==0 || password.length()==0)
			ExtentReport.logFail("[getEmailMessage] Failed: Incorrect values passed.");
		
		//Get All Email Messages 
		Message[] messages=getAllEmailMessags(host, mailStoreType, username, password);
		
		if(messages.length==0) {
			System.out.println("[getEmailMessage] Failed: NO Emails received.");
		}
		
		//Get Email Messages Filtered By From Address
		if(from.length()!=0)
		{
			messages =getEmailsFilterByFrom(messages, from);
			ExtentReport.logPass("[getEmailMessage] Passed: Filtered by 'From' - "+messages.length+" messages found");
		}
		
		//Get Email Messages Filtered By Email Subject
		if(subject.length()!=0)
		{
			messages =getEmailByPartialSub(messages, subject);
			ExtentReport.logPass("[getEmailMessage] Passed: Filtered by 'Subject' - "+messages.length+" messages found");
		}
		

		
		try {
			Message reqMessage=getLatstEmailMessage(messages);
			ExtentReport.logPass("[getEmailMessage] Passed:  Latest message retrieved");
			return reqMessage;
		} catch (NullPointerException e) {
			ExtentReport.logFail("[getEmailMessage] Failed: No messages found"+e.getMessage());
			return null;
		}catch (Exception e) {
			ExtentReport.logFail("[getEmailMessage] Failed: No messages found");
			return null;
		}
		
	}
	/**
	 * 
	 * @param username
	 * @param password
	 * @param from
	 * @param subject
	 * @return String
	 * @throws AbstractScriptException
	 * @throws Exception
	 * @example 
	 * 	getPasswordFromEmail("mylabor_web","pasword1","example@oracle.com","");
	 * 	getPasswordFromEmail("mylabor_web","pasword1","","myMicros.net - Helpdesk");
	 * 	getPasswordFromEmail("createPortalUser ","pasword1","example@oracle.com","myMicros.net - Helpdesk");
	 * 	getPasswordFromEmail("mylabor_web","pasword1","","");
	 * 
	 */
	
	public String getPaswordFromEmail(String username, String password,String from, String subject ) throws  Exception
	{
		//Check for null values
		if(from.length()==0 || subject.length()==0)
    	{
    		ExtentReport.logFail("[getPasswordFromEmail] Failed: Incorrect data Passed");
    	}
		String bodyContent="",reqBodyData = null;
		
		//Get the Email Body Content
		Message message=getEmailMessage(username, password,from, subject);
		bodyContent=getMessgeBodyContent(message);
		System.out.println("[getPasswordFromEmail]: body content received is \n"+bodyContent);
		
		//Check email body length
		if(bodyContent.length()==0)
			ExtentReport.logFail("[getPasswordFromEmail]: Failed - Empty Body ");
		
		//Retrieves password from the body content
		try {
			reqBodyData=bodyContent.substring(bodyContent.indexOf("account:")+8, bodyContent.indexOf("account:")+16);

		} catch (NullPointerException e) {
			ExtentReport.logFail("[getPasswordFromEmail]: Failed - "+e.getMessage()+"  - No Password present ");
		}
		
		if(password.length()!=0)
		{
			ExtentReport.logPass("[getPasswordFromEmail]: Passed - Password retrieved is "+password);
			return reqBodyData;
		}
		else
		{
			ExtentReport.logFail("[getPasswordFromEmail]: Failed - No Password present ");
			return null;
		}

	}

	public List<String> getMessgeBodyContentWithAttachment(Message message) throws  Exception
	{
		ArrayList<String> messageList=new ArrayList<String>();
		//Check for the nullity of passed message
		if(message==null)
		{
			ExtentReport.logFail("[getMessageBodyContent] Failed: Incorrect Data ");
		}
		String bodyContent="";
		
		//Retrieve Body of the Email Message is Multipart Type
		if(message.getContent() instanceof Multipart)
		{
			Multipart multipart = (Multipart) message.getContent();
			if(multipart.getCount()==0)
				ExtentReport.logFail("[getMessageBodyContent] Failed: No messages found");

			for (int j = 0; j < multipart.getCount(); j++) 
			{
				BodyPart bodyPart = multipart.getBodyPart(j);
				String disposition = bodyPart.getDisposition();
				messageList.add(0,bodyContent);
				//Check if the Email has an attachment
				if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) 
				{ 
					String fileName = bodyPart.getFileName(); 
					System.out.println("fileName = " + fileName); 
					InputStream inStream = bodyPart.getInputStream(); 					
					FileOutputStream outStream = new FileOutputStream(new File( "C:/DownloadedReports/" + fileName));
					messageList.set(1,new String("C:/DownloadedReports/"+fileName));
					byte[] tempBuffer = new byte[4096];// 4 KB 
//					int numRead; 
					while ((inStream.read(tempBuffer)) != -1) 
					{ 
						outStream.write(tempBuffer); 
					} 
					inStream.close(); 
					outStream.close(); 
					System.out.println("Mail have some attachment");
					DataHandler handler = bodyPart.getDataHandler();
					System.out.println("file name : " + handler.getName());                                 
				}
				
				// Read the Body of Email Message of Multipart Type 
				else 
				{ 
					bodyPart = ((Multipart) message.getContent()).getBodyPart(0);
					bodyContent = bodyPart.getContent().toString();
					messageList.set(0,bodyContent);
				}
			}
		}
		// Read the Body of Email Message of plain Text Type
		else 
		{
			bodyContent=message.getContent().toString();
			messageList.set(0,bodyContent);
		}
		if(bodyContent.length()!=0)
		{
			ExtentReport.logPass("[getMessageBodyContent] Passed: Body Content is \n"+bodyContent);
		}
		else
		{
			ExtentReport.logFail("[getMessageBodyContent] Failed: No messages found");
			return null;
		}
		return messageList;
	}
	
	
	public void DeleteAllEmailMessags(String host, String storeType, String username,  String password) throws  Exception 
	{
		Properties properties = new Properties();
		Session emailSession;
		Store store;
		Folder emailFolder;
		Message[] messages = null;
		try {

			properties.put("mail.pop3.host", host);
			properties.put("mail.pop3.port", "110");
			//properties.put("mail.pop3.starttls.enable", "true");
			
			// Email connection to the host
			System.out.println("[getAllEmailMessages]: Connecting to Email Server....");
			emailSession = Session.getDefaultInstance(properties);
			System.out.println("Email Session has been set");
			store = emailSession.getStore("pop3");
			System.out.println("Store has been set");
			try {
				System.out.println(host+" "+username+" "+password);
				store.connect(host, username, password);

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("[getAllEmailMessages]: Connected to Email Server Successfully. UserName: "+username+", Password: "+password);
			
			//Reads All Emails from Inbox
			emailFolder = store.getFolder("Inbox");
			System.out.println(emailFolder.getURLName());
			emailFolder.open(Folder.READ_ONLY);
			messages = emailFolder.getMessages();
			 for(Message msg:messages){
				 System.out.println("---------------------Message is "+getMessgeBodyContent(msg));
				 msg.setFlag(Flags.Flag.DELETED, true);
				 System.out.println("-------Deleted Flag Updated");
			 }
			System.out.println("[getAllEmailMessages]: Retrieved all Email Messages");
			boolean expunge = true;
			emailFolder.close(expunge);
		} catch (NullPointerException e) {  //catch NullPointerException
			ExtentReport.logFail("[getAllEmailMessages] Failed:" +e.getMessage()+"Incorrect data passed for Host,StoreType,Username,Password:"+host+","+storeType+","+username+","+password);
		}catch (NoSuchProviderException e) {
			ExtentReport.logFail("[getAllEmailMessages] Failed:" +e.getMessage());
		} catch (MessagingException e) {
			ExtentReport.logFail("[getAllEmailMessages] Failed:" +e.getMessage());
		} catch (Exception e) {
			ExtentReport.logFail("[getAllEmailMessages] Failed:" +e.getMessage());
		}
		if(messages.length==0)
		{
			ExtentReport.logPass("[getAllEmailMessages] Passed: Found "+messages.length+" messages");
		}
		else
		{
			ExtentReport.logFail("[getAllEmailMessages] Failed: No Messages Found "+messages.length+" messages");
		}
		
	}
	
}

