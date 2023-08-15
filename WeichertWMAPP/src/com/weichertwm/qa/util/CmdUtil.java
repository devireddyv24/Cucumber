package com.weichertwm.qa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.weichertwm.qa.framework.ExtentReport;

public class CmdUtil {

	public static int runCommand(String workingDirectory,String command) throws Exception
	{
		Runtime rt = Runtime.getRuntime();        
        //File direc = new File(workingDirectory);
        Process pr=null;
        //String command = "java -Xmx512M -Xms512M -jar postingServer.jar C:\\pabasers\\log_Org60528_Lv60561_Tr2505121797_defns.zip";          
        boolean flag = StringUtils.isEmpty(workingDirectory); //- Checks if a String is empty ("") or null.
        if(flag)
        {
        	pr = rt.exec(new String[]{"cmd", "/c", command});
        }else{
        	pr = rt.exec(new String[]{"cmd", "/c", "cd  " + workingDirectory + "  & " + command});
        }
        
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line=null;
        while((line=input.readLine()) != null) {
              System.out.println(line);
          }
        int exitVal = pr.waitFor();
        System.out.println("Exited with error code "+exitVal);      
        return exitVal;
	}
	private static boolean checkMapDrive(String mountDriveName)
	{
		boolean flag=false;
		File[] roots = File.listRoots();
		for(int i = 0; i < roots.length ; i++){
            //System.out.println("Drive:" + roots[i].toString());
            if(roots[i].toString().contains(mountDriveName))
            {
            	flag=true;
            }
        }
		return flag;
	}
	public static void mapNetworkDrive(String mountDriveName,String hostName,String mountSharedDrive,String username,String password) throws Exception
	{
		String command = "net use "+mountDriveName+": \\\\"+hostName+"\\"+mountSharedDrive+"$ /persistent:no /user:"+username+" "+password;
		if(CmdUtil.checkMapDrive(mountDriveName))
		{
			ExtentReport.logPass("Drive mounted already..");
		}
		else if(CmdUtil.runCommand("", command)==0)
		{
			if(CmdUtil.checkMapDrive(mountDriveName))
			{
				ExtentReport.logPass("Drive mounted successfully..");
			}
			else
			{
				ExtentReport.logFail("Drive mounted command failed");
			}
		}
	}
	public static void disconnectNetworkDrive(String mountDriveName) throws Exception
	{
		//net use Z: /d
		String command="net use "+mountDriveName+": /d";
		if(CmdUtil.checkMapDrive(mountDriveName))
		{
			if(CmdUtil.runCommand("", command)==0)
			{
				if(CmdUtil.checkMapDrive(mountDriveName))
				{
					ExtentReport.logPass("Drive disconnected successfully..");
				}
				else
				{
					ExtentReport.logFail("Drive disconnected command failed");
				}
			}
			else
			{
				ExtentReport.logPass("Drive disconnected already");
			}
		}
		
	}
	public static void runCommandRemotely(String psToolPath,String hostName,String username,String password,String workingDirectory,String command) throws Exception
	{
		String remoteCommand = "\"" + psToolPath + "\""+" \\\\"+hostName+" -u "+"\"" + username + "\""+" -p "+"\"" + password + "\""+" -w "+"\"" + workingDirectory + "\""+" "+command;
		if(CmdUtil.runCommand("", remoteCommand)==0)
		{
			ExtentReport.logPass(command+" executed successfully");
		}
		else
		{
			ExtentReport.logFail(command+" execution failed");
		}
	}
}
