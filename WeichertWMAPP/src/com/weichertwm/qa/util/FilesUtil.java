package com.weichertwm.qa.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;

import com.weichertwm.qa.framework.ExtentReport;

public class FilesUtil {

	
	public static void createFolder(String folderPath) throws Exception {
		File directory = new File(folderPath);
		if (!directory.exists()) {
			if (directory.mkdir()) {
				ExtentReport.logInfo("[creatFolder]: "+folderPath + " Directory is created!");
			} else {
				ExtentReport.logInfo("[creatFolder]: "+folderPath + " Directory is not created!");
				throw new Exception("Unable to created the basic folder strucuture");
			}
		}
		else{
			ExtentReport.logInfo("[creatFolder]: "+folderPath + " Directory is already existed");
			ExtentReport.logInfo("[creatFolder]: Clean the files from Directory "+folderPath);
			File[] listOfFiles=directory.listFiles();
			if(listOfFiles.length>0)
		    {
				for(File file:listOfFiles)
				{
					file.delete();
				}
		    }
		}
	}
	public static List<String> getFiles(String folderPath)
	{
		List<String> filesList = new ArrayList<String>();		
		File directory=new File(folderPath);
	    File[] listOfFiles=directory.listFiles();
	    System.out.println("[getFiles]: No. of files found "+listOfFiles.length);
	    ExtentReport.logInfo("[getFiles]: No. of files found "+listOfFiles.length);
	    if(listOfFiles.length>0)
	    {
	    	for(int k=0;k<listOfFiles.length;k++)
		    {
		    	if (listOfFiles[k].isFile()) {
		    		filesList.add(listOfFiles[k].getName());
		          } else {
		        	  continue;
		          }
		    }
	    }
	    else{
	    	ExtentReport.logInfo("[getFiles]: No files found");	    	
	    }
	    return filesList;
	}
	
	public static void copyFileToDirectory(File source, File dest) throws Exception {
	    FileUtils.copyFileToDirectory(source, dest);
	}
	
	public static boolean fileRename(File oldName,File newName) throws Exception
	{
		boolean isFileRenamed = oldName.renameTo(newName);
		if(isFileRenamed)
		{
			ExtentReport.logPass("[fileRename]:"+newName.getName()+" file renamed successfully");
		}
		else
		{
			ExtentReport.logFail("[fileRename]: Failed to rename file "+newName.getName());
		}
	    return isFileRenamed;
	     
	}
	public static void unzipFiles(String directory) throws Exception{

		int numUnzippedFiles = 0;
		String filepath = "";
		File folder = new File(directory);
		File[] listOfFiles = null;
		if (folder.isFile()) {
			listOfFiles = new File[1];
			listOfFiles[0] = folder;
		}
		else {
			listOfFiles = folder.listFiles();
		}
		//Unzips all .zip files
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				filepath = listOfFiles[i].getParent()+ "\\" + listOfFiles[i].getName();
				String ext = "";
				int l = filepath.lastIndexOf('.');
				if (l > 0) {
					ext = filepath.substring(l + 1);
				}
				if (ext.equals("zip")) {
					
						ZipInputStream zis = new ZipInputStream(new FileInputStream(filepath));
						ZipEntry ze = zis.getNextEntry();
						while(ze != null) {
							String fileName = ze.getName();
							File newFile = new File(listOfFiles[i].getParent() + "\\" + fileName);
							newFile.getParentFile().mkdirs();
							//System.out.println("Unzipping File: " + newFile.getAbsoluteFile());
							BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));
							byte[] bytesIn = new byte[4096];
							int read = 0;
							while ((read = zis.read(bytesIn)) != -1) {
								bos.write(bytesIn, 0, read);
							}
							bos.close();
							zis.closeEntry();
							ze = zis.getNextEntry();
							numUnzippedFiles++;

							if ((numUnzippedFiles % 500) == 0) {
								System.out.print(".");
							}
						}
						zis.close();
						//boolean success = (new File(filepath)).delete();
				}
			}
		}

		if (numUnzippedFiles > 0) {
			System.out.println("Done!");
			System.out.println(numUnzippedFiles + " Files Successfully Unzipped.");
			ExtentReport.logPass(numUnzippedFiles + " Files Successfully Unzipped.");
		}
		else {
			System.out.println("No Files to Unzipped.");
			ExtentReport.logFail("No Files to Unzipped.");
		}		
	}
	public static boolean isFileExist(String folderPath,String fileName) {
		File file = new File(folderPath);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.getName().equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		return false;
	}
	public static void deleteFile(String filePath) {
		File file = new File(filePath); 
        if(file.exists()) { 
        	file.delete();
        	System.out.println("File '"+filePath+"' deleted successfully");
        } 
        else
            System.out.println("file '"+filePath+"' not exist"); 
	}
}
