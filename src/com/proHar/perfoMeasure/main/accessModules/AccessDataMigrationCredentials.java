package com.proHar.perfoMeasure.main.accessModules;

public class AccessDataMigrationCredentials {

	private String databaseURL;
	public static String databaseConnectionURL = "jdbc:ucanaccess://E:/Jars/PerformanceDependencies/PerformanceDBs/Performance-1-Beta.accdb;showSchema=true";
	public String getDatabaseURL() {
		return databaseURL;
	}
	public void setDatabaseURL(String databaseURL) {
		this.databaseURL = databaseURL;
	}
}
