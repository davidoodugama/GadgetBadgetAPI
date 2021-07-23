package model;

import java.sql.Connection;
import java.sql.PreparedStatement;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import connection.Conn;

public class Admin {

	Conn con = new Conn();

	public String getProject(String proName, String doclinks, String description, String projectType, String status) {

		String output = "";

		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database";
			}

			String query = "insert into pendingprojects(`adminId`,`proName`,`doclinks`,`description`,`projectType`,`status`,`receivedDate`)"
					+ "values (?,?,?,?,?,?,current_timestamp())";
			
//*************************************************	Sending Mail to Admin******************************************************	
			sendMailToAdmin();
			
			PreparedStatement preparedStmt = conn.prepareStatement(query);

//************************************************* Inserting values to DB*****************************************************
			
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, proName);
			preparedStmt.setString(3, doclinks);
			preparedStmt.setString(4, description);
			preparedStmt.setString(5, projectType);
			preparedStmt.setString(6, status);
			preparedStmt.execute();

			conn.close();

			output = "Inserted successfully";

		} catch (Exception e) {
			output = "Error while inserting";
			System.out.println(e.getMessage());
		}
		return output;
	}
	public String sendApprov(String projectName, String status) {

		String output = "";

		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database";
			}

			String query = "UPDATE pendingprojects SET status=? WHERE proName =?";

			PreparedStatement preparedStmt = conn.prepareStatement(query);

//************************************************ Updating admin table*********************************************************

			preparedStmt.setString(1, status);
			preparedStmt.setString(2, projectName);
			preparedStmt.execute();
			
//************************************************Sending msg to Projects*********************************************************
			
			sendProdetails(status,projectName);
			
			conn.close();
			output = "Pending Projects Table Updated successfully";

		} catch (Exception e) {
			output = "Error while inserting";
			System.out.println(e.getMessage());
		}
		return output;
	}

// ************************************* Sending Disapprove or approval msg to projects ***********************************
	
	public String sendProdetails(String status,String proname) {

		
		
		Client client = Client.create();
		String url = "http://localhost:8081/ProposalProjectService/ProjectService/Project/proname";
		WebResource resource = client.resource(url);
		String input = "{\"projectname\":\"" + proname + "\",\"status\":\"" + status + "\"}";
		ClientResponse response = resource.type("application/json").post(ClientResponse.class, input);
		String output = response.getEntity(String.class);
		return output;
	}
	
// *********************************************** Calling Mail service ****************************************************
	
	public String sendMailToAdmin() {
		
		String mail = "davidoodugama1999@gmail.com";
		String subject = "New Project Received to approval";
		String body = "You have received a Project and waiting for your response for the approval";
		
		Client client = Client.create();
		String url = "http://localhost:8081/MailService/Mail_Service/email/custom";
		WebResource resource = client.resource(url);
		String input = "{\"recepient\":\"" + mail + " \",\"subject\":\"" + subject + " \",\"messageBody\":\"" + body + "\"}";
		ClientResponse response = resource.type("application/json").post(ClientResponse.class, input);
		String output = response.getEntity(String.class);
		return output;
	}
	
}
