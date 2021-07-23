package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import connection.Conn;

public class ProposalProject {

	Conn con = new Conn();

	/**
	 * Inserting values to Project table
	 * 
	 * 
	 ********************************************************************************************************
	 * ### Date Author Description
	 * -------------------------------------------------------------------------------------------------------
	 * 1 14-04-2021 David Created
	 * 
	 ********************************************************************************************************
	 */

	public String insertProject(String projectname, String doclinks, String description, String projectType) {

		String output = "";

		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database";
			}
			String query = "insert into proposalproject (`projectID`,`projectname`,`doclinks`,`description`,`projectType`,`status`)"
					+ "values (?,?,?,?,?,'Pending')";

			PreparedStatement preparedStmt = conn.prepareStatement(query);
			sendProdetails(projectname,doclinks,description,projectType,"Pending");
			
			getCurrentLoggedUserinfo();
			
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, projectname);
			preparedStmt.setString(3, doclinks);
			preparedStmt.setString(4, description);
			preparedStmt.setString(5, projectType);

			preparedStmt.execute();
			conn.close();
			
			
			
			output = "Inserted successfully";

		} catch (Exception e) {
			output = "Error while inserting";
			System.out.println(e.getMessage());
		}
		return output;
	}

	/**
	 * Reading all values from Project table
	 * 
	 * 
	 ********************************************************************************************************
	 * ### Date Author Description
	 * -------------------------------------------------------------------------------------------------------
	 * 1 14-04-2021 David Created
	 * 
	 ********************************************************************************************************
	 */

	public String readProjects() {
		String output = "";
		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database for reading";
			}

			output = "<table border ='1'>" + "<tr><th>project ID</th><th>Project Name</th><th>Document Links</th>"
					+ "<th>Description</th><th>Project Type</th><th>Project Type</th>";

			String query = "select * from proposalProject";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {

// ************************************************ Reading values from fund and assign them into variables ************************************
				
				String projectID = Integer.toString(rs.getInt("projectID"));
				String projectname = rs.getString("projectname");
				String doclinks = rs.getString("doclinks");
				String description = rs.getString("description");
				String projectType = rs.getString("projectType");
				String status = rs.getString("status");

// ************************************************ Presenting the values of the variables in a table format ************************************
				
				output += "<tr><td>" + projectID + "</td>";
				output += "<td>" + projectname + "</td>";
				output += "<td>" + doclinks + "</td>";
				output += "<td>" + description + "</td>";
				output += "<td>" + projectType + "</td>";
				output += "<td>" + status + "</td>";

			}
			conn.close();
			output += "</table>";

		} catch (Exception e) {
			output = "Error while reading the projects.";
			System.err.println(e.getMessage());
		}

		return output;
	}

	/**
	 * Reading values from Db by project ID
	 * 
	 * 
	 ********************************************************************************************************
	 * ### Date Author Description
	 * -------------------------------------------------------------------------------------------------------
	 * 1 14-04-2021 David Created
	 * 
	 ********************************************************************************************************
	 */

	public String readProjectById(String ID) {
		String output = "";

		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database for reading";
			}

			output = "<table border ='1'>" + "<tr><th>projectID</th><th>projectname</th><th>doclinks</th>"
					+ "<th>description</th><th>projectType</th><th>status</th>";

			String query = "select * from proposalproject where projectID = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, Integer.parseInt(ID));
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {

// ************************************************ Reading values from Project and assign them into variables****************************
				

				String proId = Integer.toString(rs.getInt("projectID"));
				String proName = rs.getString("projectname");
				String documentlinks = rs.getString("doclinks");
				String des = rs.getString("description");
				String proType = rs.getString("projectType");
				String status = rs.getString("status");

// ************************************************ Presenting the values of the// variables in a table format ****************************

				output += "<tr><td>" + proId + "</td>";
				output += "<td>" + proName + "</td>";
				output += "<td>" + documentlinks + "</td>";
				output += "<td>" + des + "</td>";
				output += "<td>" + proType + "</td>";
				output += "<td>" + status + "</td>";
			}
			conn.close();
			output += "</table>";

		} catch (Exception e) {
			output = "Error while reading the Projects.";
			System.err.println(e.getMessage());
		}

		return output;
	}

	/**
	 * Getting the approval from Admin
	 * 
	 * 
	 ********************************************************************************************************
	 * ### Date Author Description
	 * -------------------------------------------------------------------------------------------------------
	 * 1 14-04-2021 David Created
	 * 
	 ********************************************************************************************************
	 */

	public String getApproval(String proname, String msg) {
		String output = "";

//************************************************ Validating the Message that was received from Admin*************************************
		System.out.println(proname + msg);
		
		if (msg.equals("Approved") || msg.equals("Closed")) {
			
			System.out.println("Athule inne...."+proname + msg);
			try {
				Connection conn = con.connect();
				if (con == null) {
					return "Error while connecting to the database for updating";
				}
				System.out.println(msg + proname);
				String query = "UPDATE proposalproject SET status=? WHERE projectname=?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);

// ************************************************ Updating Status Column as Approved or Closed *******************************************
				
				preparedStmt.setString(1, msg);
				preparedStmt.setString(2, proname);
				preparedStmt.execute();
				conn.close();

				output = "Proposal Projects Status Updated successfully";

			} catch (Exception e) {
				output = "Error while updating the Project";
				System.err.println(e.getMessage());
			}

			System.out.println("END................");
			return output;
		}

//************************************************ Validating the Message that was received from Admin **************************************
		
		else if (!msg.equals("Approved") || !msg.equals("Closed")) {
			
			System.out.println("Athule inne...."+proname + msg);
			try {
				Connection conn = con.connect();
				if (con == null) {
					return "Error while connecting to the database for deleting.";
				}

				String query = "delete proposalproject, funds from proposalproject inner join funds WHERE proposalproject.projectID = (select proposalproject.projectID where projectname = ?)";

// ************************************************ Deleting values from projects and funds by ID ********************************************
				
				PreparedStatement preparedStmt = conn.prepareStatement(query);

				preparedStmt.setString(1, proname);
				preparedStmt.execute();
				conn.close();

				output = "Delete succesfully";

			} catch (Exception e) {
				output = "Error while deleting the Projects.";
				System.err.println(e.getMessage());
			}

			return output;
		} else {
			return "Error";
		}
	}

	/**
	 * Deleting the projects
	 * 
	 * 
	 ********************************************************************************************************
	 * ### Date Author Description
	 * -------------------------------------------------------------------------------------------------------
	 * 1 16-04-2021 David Created
	 * 
	 ********************************************************************************************************
	 */

	public String deleteProjects(String proname) {
		String output = "";
		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}

			String query = "delete proposalproject, funds from proposalproject inner join funds where proposalproject.projectID = funds.proID and proposalproject.projectname = ?";

			PreparedStatement preparedStmt = conn.prepareStatement(query);

			preparedStmt.setString(1, proname);
			preparedStmt.execute();
			conn.close();

			output = "Delete succesfully";

		} catch (Exception e) {
			output = "Error while deleting the Projects.";
			System.err.println(e.getMessage());
		}

		return output;
	}

//********************************************************** Updating Projects ********************************************************************
	
	public String updateProject(String ID, String projectname, String doclinks, String description,
			String projectType, String status) {
		String output = "";

		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database for updating";
			}
			String query = "UPDATE proposalproject SET projectname=? doclinks=? description=? projectType=? status = ? WHERE projectID =?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);

			preparedStmt.setString(1, projectname);
			preparedStmt.setString(2, doclinks);
			preparedStmt.setString(3, description);
			preparedStmt.setString(4, projectType);
			preparedStmt.setString(5, status);
			preparedStmt.setInt(6, Integer.parseInt(ID));
			preparedStmt.execute();
			conn.close();

			output = "Updated successfully";
		} catch (Exception e) {
			output = "Error while updating the Funds";
			System.err.println(e.getMessage());
		}
		return output;
	}

	// method to get the current logged user's info from accountService
	public String getCurrentLoggedUserinfo() {

		Client client = Client.create();

		WebResource webResource = client.resource("http://localhost:8081/UserAccounts/UserAccountService/User_logins");

		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

		String output = response.getEntity(String.class);

		return output;

	}
	
//************************************************* Sending Project details to Admin Method *****************************************************
	
	public String sendProdetails(String proName, String doclinks, String description, String projectType,
			String status) {

		Client client = Client.create();
		String url = "http://localhost:8081/AdminService/AdminService/Admin";
		WebResource resource = client.resource(url);
		String input = "{\"projectname\":\"" + proName + " \",\"doclinks\":\""
				+ doclinks + " \",\"description\":\"" + description + "\",\"projectType\":\"" + projectType + "\",\"status\":\"" + status + "\"}";
		ClientResponse response = resource.type("application/json").post(ClientResponse.class, input);
		String output = response.getEntity(String.class);
		return output;
	}

}
