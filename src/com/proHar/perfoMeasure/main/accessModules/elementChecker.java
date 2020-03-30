package com.proHar.perfoMeasure.main.accessModules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.proHar.perfoMeasure.main.ValueParser;

import java.util.HashMap;
import java.util.Iterator;

public class elementChecker {

	//public static void main(String args[]){
	public static void checkIfEnded(int appId, int pageID){

		String getDataFromResources = "select * from Resources where Application_ID="+ appId +" and Page_ID = "+ pageID;
		List<String> resources = ValueParser.resHolder;
		Map<Integer, String> rowmap = new HashMap<Integer, String>();
		int ID;

		// Application Name Insertion

		try(Connection connection = DriverManager.getConnection(AccessDataMigrationCredentials.databaseConnectionURL)){
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(getDataFromResources);

			while (result.next()) {
				int Resource_ID = result.getInt("Resource_ID");
				String Res_ElementName = result.getString("Res_ElementName");

				rowmap.put(Resource_ID, Res_ElementName);
			}

			// Check if the VALUE exists in the list from DB
			/*Iterator iter = rowmap.entrySet().iterator();
      while (iter.hasNext()) {
          Map.Entry entry = (Map.Entry) iter.next();
          int key = Integer.parseInt(entry.getKey().toString());
          String value = entry.getValue().toString();
          String[] temp;
          for (String s : resources) {
                    String listElements = s;
                    temp = s.split(",");
                    String appName = temp[3];
                    if (value.equalsIgnoreCase(appName)) {
                         ID = 0;
                         System.out.println("NOt ID:"+ID);
                    }else{
                         ID = key;
                         System.out.println("ID:"+ID);
                    }
              }

      }*/

			// get All listed values in MAP


		}catch (Exception e) {     }


	}



}
