package com.proHar.perfoMeasure.main.Rules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import com.proHar.perfoMeasure.main.accessModules.AccessDataMigrationCredentials;
import com.proHar.perfoMeasure.main.queries.QueriesLibrary;

public class DropReferencialConstrains {

	public static void dropConstrains(){
		String databaseURL = AccessDataMigrationCredentials.databaseConnectionURL;
		String sql = QueriesLibrary.deleteReferencialConstrains;
		try (Connection connection = DriverManager.getConnection(databaseURL)) {
			PreparedStatement preparedStatementes = connection.prepareStatement(sql);
			preparedStatementes.executeUpdate();             
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}