package com.proHar.perfoMeasure.main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.proHar.perfoMeasure.main.accessModules.AccessDataMigrationCredentials;
import com.proHar.perfoMeasure.main.accessModules.AccessDataMigrationUtils;
import com.proHar.perfoMeasure.main.accessModules.AccessUtils;
import com.proHar.perfoMeasure.main.databaseModules.AzureDataMigrationUtils;
import com.proHar.perfoMeasure.main.reporting.ListFileUtils;
import com.proHar.perfoMeasure.main.reporting.ReporterAgent;
import com.proHar.perfoMeasure.main.ssmsModules.SSMSDataMigrationCredentials;
import com.proHar.perfoMeasure.main.ssmsModules.SSMSDataMigrationUtils;
import com.proHar.perfoMeasure.main.ssmsModules.SSMSUtils;

/*
 * Performance Entry
 * */
public class App {
	//   public static void main(String[] args) { 

	public void Performer(WebDriver driver, String Test_Scenario_Name, String ProjectName, String mode) {
		// DirecTory Allocations
		String workingPath = System.getProperty("user.dir");
		String getUserName = System.getProperty("user.name");

		// Create Folder
		String workingFOLDERpath = workingPath + "\\Output";
		File targetPath = new File(workingFOLDERpath);
		if (!targetPath.exists()) {
			targetPath.mkdir();
		}


		JavascriptExecutor js = (JavascriptExecutor)driver;  
		String base = js.executeScript("return document.domain;").toString();

		/*
		 * Set TestCase Name and UserName
		 * on the TestCases table, with an unique ID
		 * */

		int testCaseID = AccessUtils.setTestCaseName(Test_Scenario_Name,ProjectName);
		int SSMStestCaseID = SSMSUtils.setTestCaseName(Test_Scenario_Name,ProjectName);

		/*

		 * Set ApplicationName with ID into Application_Name
		 *  table to check if that exists..!
		 *  If YES then return ID else INSERT Domain Name and return Current ID
		 * */
		int appname_id = AccessUtils.getApplicationID(base, testCaseID);
		int SSMSappname_id = SSMSUtils.getApplicationID(base, SSMStestCaseID);

		/*
		 * Set the Page Name with new ID,
		 * Fetch the Current PAGE NAME :: Collect it from VALUE PARSER class
		 * PASS the appname_id :: insert it as (Page_ID|Page_NAME|Application_ID)
		 * */
		String getCurrentURL = driver.getCurrentUrl();
		int pager_id = AccessUtils.getPagerId(getCurrentURL, appname_id, testCaseID);
		int SSMSpager_id = SSMSUtils.getPagerId(getCurrentURL, SSMSappname_id, SSMStestCaseID);

		// Performance Methods
		try {
			if (mode=="A") {
				ValueParser.ResourceAnalyser(driver, appname_id, pager_id, testCaseID);
				ValueParser.NavigationAnalyser(driver, appname_id, pager_id, testCaseID);
			} else if (mode=="S") {
				ValueParser.ResourceAnalyser(driver, SSMSappname_id, SSMSpager_id, SSMStestCaseID);
				ValueParser.NavigationAnalyser(driver, SSMSappname_id, SSMSpager_id, SSMStestCaseID);
			} else if(mode=="SA") {
				ValueParser.ResourceAnalyser(driver, appname_id, pager_id, testCaseID);
				ValueParser.NavigationAnalyser(driver, appname_id, pager_id, testCaseID);
			}
			
			
			
		} catch (InterruptedException e) {   
			e.printStackTrace();
		}

	}


	// Excel Database generation class
	public void ExcelAgent() throws InterruptedException {
		String workingPath = System.getProperty("user.dir");
		String workingFOLDERpath = workingPath + "\\Output";
		ReporterAgent ra = new ReporterAgent();
		String nav = getNAVlocation(workingFOLDERpath);
		String res = getRESlocation(workingFOLDERpath);
		ra.getReport(nav, res);
		Thread.sleep(3000);
		try {
			ListFileUtils.GenerateValueFromExcel(nav, res, workingFOLDERpath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Access Database generation class
	public void AccessAgent(/*String connectioString*/) throws InterruptedException {
		String connectionString = AccessDataMigrationCredentials.databaseConnectionURL;
		AccessDataMigrationUtils adm = new AccessDataMigrationUtils();
		try{
			adm.AccessDatabaseManagerAgent(connectionString);
		} catch (Exception e) {   
			e.printStackTrace();

		}
	}

	// Access Database generation class
	public void SSMSAgent(/*String connectioString*/) throws InterruptedException {
		Connection con = SSMSDataMigrationCredentials.getSSMSConnection();
		SSMSDataMigrationUtils sdm = new SSMSDataMigrationUtils();
		try{
			sdm.SSMSDatabaseManagerAgent(con);
		} catch (Exception e) {   
			e.printStackTrace();

		}
	}

	// get values at any time
	public List<String> getListedNavigationElementsNow(){

		List<String> storedNavValue = ValueParser.navHolder;
		return storedNavValue;
	}

	public List<String> getListedResourceElementsNow(){

		List<String> storedResValue = ValueParser.navHolder;
		return storedResValue;
	}

	// Azure Data Migration Class
	public void AzureAgent(String hostName, String database, String user, String password) {

		AzureDataMigrationUtils adm = new AzureDataMigrationUtils();
		try {
			adm.AzureDatabaseManagerAgent(hostName, database, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private static String getNAVlocation(String baseFolderPath) {
		String navigationPath = baseFolderPath + "\\navTemp.txt";
		File navFile = new File(navigationPath);
		createFile(navFile);
		return navigationPath;
	}

	private static String getRESlocation(String baseFolderPath) {

		String resourcesPath = baseFolderPath + "\\resTemp.txt";
		File resFile = new File(resourcesPath);
		createFile(resFile);
		return resourcesPath;
	}

	private static void createFile(File navFile) {
		try {
			navFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}