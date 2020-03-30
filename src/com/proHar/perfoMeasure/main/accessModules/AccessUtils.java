package com.proHar.perfoMeasure.main.accessModules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class AccessUtils {

	/*
	 * Get TestCaseName with ID - TestCaseName and TestCase_System_UserName
	 * Pass TestCaseID to Application Name
	 * will be inserted :: Pk-TestScenerio(TestScenarioID) :: FK-Application_Name(TestScenarioID)
	 * */
	public static int setTestCaseName(String testScenarioName, String projectName){

		int holdID = 0;
		String databaseURL = AccessDataMigrationCredentials.databaseConnectionURL;
		String sql = "select * from TestScenario";
		String insertSQL = "insert into TestScenario (TestScenarioName, ProjectName) values (?,?);";

		try (Connection connection = DriverManager.getConnection(databaseURL)) {
			String ScenarioName = testScenarioName;
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			int nRowsInserted = 0;
			boolean flag = false;
			while (result.next()) {
				int id = result.getInt("TestScenarioID");
				String fullname = result.getString("TestScenarioName");
				if (fullname.equalsIgnoreCase(ScenarioName)) {
					holdID = id;
					flag = true;
				}                }

			if (flag == false) {
				PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
				preparedStatement.setString(1, testScenarioName);
				preparedStatement.setString(2, projectName);
				nRowsInserted += preparedStatement.executeUpdate();
				String sqlNew = "select * from TestScenario where TestScenarioName = '" + testScenarioName + "';";
				//                   System.out.println(sqlNew);
				ResultSet results = statement.executeQuery(sqlNew);
				while (results.next()) {
					int idNew = results.getInt("TestScenarioID");
					String ScenarioNames = results.getString("TestScenarioName");
					if (ScenarioNames.equalsIgnoreCase(ScenarioName)) {
						holdID = idNew;
					}
				}
			}              
		} catch (Exception e) {
			e.printStackTrace();
		}
		return holdID;
	}
	/*
	 * Random Token Generator for TestCases
	 * */

	public static String randomTokenGenerator(){
		// create instance of Random class
		Random rand = new Random();
		// Generate random integers in range 0 to 999
		int rand_int1 = rand.nextInt(10000);
		int rand_int2 = rand.nextInt(10000);
		// Chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "0123456789"
				+ "abcdefghijklmnopqrstuvxyz";
		int n =20;
		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index
			= (int)(AlphaNumericString.length()

					* Math.random());
			// add Character one by one in end of sb
			sb.append(AlphaNumericString
					.charAt(index));
		}
		String token = sb.toString();
		String generateString = rand_int1 + token + rand_int2;
		return generateString;
	}

	/*
	 * PUT Domain Name(Application_Name) :
	 *  Check If exists OR Insert value in ACCESS :: RETURN Application_ID
	 * */

	public static int getApplicationID(String getApplicationName, int ScenerioID){
		int holdID = 0;
		String databaseURL = AccessDataMigrationCredentials.databaseConnectionURL;
		String sql = "select * from Application_Name";
		String insertSQL = "insert into Application_Name (Application_NAME, TestScenarioID) values (?,?);";
		try (Connection connection = DriverManager.getConnection(databaseURL)) {
			String appName = getApplicationName;
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			//              int nRowsInserted = 0;
			boolean flag = false;
			while (result.next()) {
				int id = result.getInt("Application_ID");
				String fullname = result.getString("Application_NAME");

				if (fullname.equalsIgnoreCase(appName)) {
					holdID = id;
					flag = true;
				}
			}


			if (flag == false) {
				int nRowsInserted = 0;
				PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
				preparedStatement.setString(1, appName);
				preparedStatement.setInt(2, ScenerioID);
				nRowsInserted += preparedStatement.executeUpdate();
				String sqlNew = "select * from Application_Name where Application_NAME = '" + appName + "';";
				System.out.println(sqlNew);
				ResultSet results = statement.executeQuery(sqlNew);
				while (results.next()) {
					int idNew = results.getInt("Application_ID");
					String fullname = results.getString("Application_NAME");
					if (fullname.equalsIgnoreCase(appName)) {
						holdID = idNew;
					}
				}
			}              
		} catch (Exception e) {   
			e.printStackTrace();
		}

		return holdID;
	}

	/*
	 * Get Page Name with ID - CurrentURL and App_ID
	 * will be inserted :: Pk-Application_Name(Application_Id) :: FK-Page_Name(Application_ID)
	 * */

	public static int getPagerId(String pageName, int appID, int scenerioID){
		int holdID = 0;
		String databaseURL = AccessDataMigrationCredentials.databaseConnectionURL;
		String sql = "select * from Page_Name";
		String insertSQL = "insert into Page_Name (TestScenarioID, Application_ID, Page_NAME) values (?,?,?);";

		try (Connection connection = DriverManager.getConnection(databaseURL)) {
			String appName = pageName;
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			boolean flag = false;
			while (result.next()) {
				int id = result.getInt("Page_ID");
				String fullname = result.getString("Page_NAME");

				//                   String applicationID = result.getInt("Application_ID");
				if (fullname.equalsIgnoreCase(appName)) {
					holdID = id;
					flag = true;
				}
			}
			if (flag == false) {
				int nRowsInserted = 0;
				PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
				preparedStatement.setInt(1, scenerioID);
				preparedStatement.setInt(2, appID);
				preparedStatement.setString(3, pageName);
				nRowsInserted += preparedStatement.executeUpdate();
				String sqlNew = "select * from Page_Name where Page_NAME = '" + appName + "';";
				System.out.println(sqlNew);
				ResultSet results = statement.executeQuery(sqlNew);
				while (results.next()) {
					int id = results.getInt("Page_ID");
					String fullname = results.getString("Page_NAME");
					//                         String applicationID = results.getString("Application_ID");
					if (fullname.equalsIgnoreCase(appName)) {
						holdID = id;
						flag = true;
					}
				}
			}              
		} catch (Exception e) {   
			e.printStackTrace();
		}
		return holdID;
	}
}
