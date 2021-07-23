package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import connection.Conn;

public class Fund {

	Conn con = new Conn();
	private String currentFunds;

	private void setCurrentFunds(String currentFunds) {
		this.currentFunds = currentFunds;
	}
	ProposalProject pro = new ProposalProject();

	/**
	 * Inserting Funds to DB
	 * 
	 ********************************************************************************************************
	 *  ###   Date             Author       Description
	 *-------------------------------------------------------------------------------------------------------
	 *    1   14-04-2021       David        Created
	 *    
	 ********************************************************************************************************
	 */	
	public String insertFunds(String proID,String actualAmount) {

		String output = "";

		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database";
			}
			
			String query = "insert into funds (`fundID`,`proID`,`actualAmount`,`currentTotalFunds`,`createdDate`)"
					+ "values (?,?,?,?,current_timestamp())";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			
     //************************************************ Inserting values to DB ************************************
			preparedStmt.setInt(1, 0);
			preparedStmt.setInt(2, Integer.parseInt(proID));
			preparedStmt.setString(3, actualAmount);
			preparedStmt.setDouble(4, 0.00);
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
	 * Reading Funds from the DB
	 * 
	 ********************************************************************************************************
	 *  ###   Date             Author       Description
	 *-------------------------------------------------------------------------------------------------------
	 *    1   14-04-2021       David        Created
	 *    
	 ********************************************************************************************************
	 */
	public String readFunds() {
		String output = "";
		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database for reading";
			}

			output = "<table border ='1'>" + "<tr><th>Fund ID</th><th>Project ID</th><th>Actual Amount</th>"
					+ "<th>Current Total Funds</th><th>Created Date</th>";

			String query = "select * from funds";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				String fundID = Integer.toString(rs.getInt("fundID"));
				String projectID = rs.getString("proID");
				String actualAmount = rs.getString("actualAmount");
				String currentFunds = rs.getString("currentTotalFunds");
				String createdDate = rs.getString("createdDate");
				
//************************************************ Getting values from DB present it in a table like format ************************************
				
				output += "<tr><td>" + fundID + "</td>";
				output += "<td>" + projectID + "</td>";
				output += "<td>" + actualAmount + "</td>";
				output += "<td>" + currentFunds + "</td>";
				output += "<td>" + createdDate + "</td>";
			}
			conn.close();
			output += "</table>";

		} catch (Exception e) {
			output = "Error while reading the Funds.";
			System.err.println(e.getMessage());
		}

		return output;
	}
	/**
	 * Reading Funds from the DB by ID
	 * 
	 ********************************************************************************************************
	 *  ###   Date             Author       Description
	 *-------------------------------------------------------------------------------------------------------
	 *    1   14-04-2021       David        Created
	 *    
	 ********************************************************************************************************
	 */
	public String readFundById(String ID) {
		String output = "";


		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database for reading";
			}

			output = "<table border ='1'>" + "<tr><th>Fund ID</th><th>Project ID</th><th>Actual Amount</th>"
					+ "<th>Current Total Funds</th><th>Created Date</th><th>Project Name</th>"
					+ "<th>Document Links</th><th>Description</th><th>Project Type</th><th>Status</th>";

			
			String query = "select * from funds inner join proposalproject where proposalproject.projectID = funds.proID AND proposalproject.projectID = ?";

			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, Integer.parseInt(ID));
			ResultSet rs = preparedStmt.executeQuery();

			while (rs.next()) {
				
//************************************************ Reading values from fund and project tables and assign them into variables ************************************
				
				String fundID = Integer.toString(rs.getInt("fundID"));
				String projectID = rs.getString("proID");
				String actualAmount = rs.getString("actualAmount");
				String currentFunds = rs.getString("currentTotalFunds");
				String createdDate = rs.getString("createdDate");
				String proName = rs.getString("projectname");
				String doc = rs.getString("doclinks");
				String des = rs.getString("description");
				String proType = rs.getString("projectType");
				String status = rs.getString("status");
				setCurrentFunds(currentFunds);

//************************************************ Getting values from both funds and project tables and present it in a table like format ************************************
				
				output += "<tr><td>" + fundID + "</td>";
				output += "<td>" + projectID + "</td>";
				output += "<td>" + actualAmount + "</td>";
				output += "<td>" + currentFunds + "</td>";
				output += "<td>" + createdDate + "</td>";
				output += "<td>" + proName + "</td>";
				output += "<td>" + doc + "</td>";
				output += "<td>" + des + "</td>";
				output += "<td>" + proType + "</td>";
				output += "<td>" + status + "</td>";
			}
			conn.close();
			output += "</table>";

		} catch (Exception e) {
			output = "Error while reading the Funds.";
			System.err.println(e.getMessage());
		}

		return output;
	}
	/**
	 * Updating Funds and calculating current funds and updating in DB
	 * 
	 * 
	 ********************************************************************************************************
	 *  ###   Date             Author       Description
	 *-------------------------------------------------------------------------------------------------------
	 *    1   15-04-2021       David        Created
	 *    
	 ********************************************************************************************************
	 */
	public String updatePayment(String ID, String amount) {
		String output = "";

		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database for updating";
			}
			String query = "UPDATE funds SET currentTotalFunds=? WHERE fundID =?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			
			preparedStmt.setDouble(1, Double.parseDouble(TotalPayment(ID,amount)));//Sending the amount to TotalPayment method for calculations
			preparedStmt.setInt(2, Integer.parseInt(ID));
			preparedStmt.execute();
			conn.close();

			output = "Updated successfully";
		}
		catch (Exception e) {
			output = "Error while updating the Funds";
			System.err.println(e.getMessage());
		}
		return output;
	}
	
	/**
	 * Calculation Method
	 * 
	 * 
	 ********************************************************************************************************
	 *  ###   Date             Author       Description
	 *-------------------------------------------------------------------------------------------------------
	 *    1   17-04-2021       David        Created
	 *    
	 ********************************************************************************************************
	 */
	public String TotalPayment(String ID ,String amount) {
		String output = "";
		Double actualAmount;
		try {
			Connection conn = con.connect();
			if (con == null) {
				return "Error while connecting to the database for reading";
			}

			String query = "select * from funds inner join proposalproject where proposalproject.projectID = funds.proID AND funds.proID = ?";

			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, Integer.parseInt(ID));
			ResultSet rs = preparedStmt.executeQuery();
			
			while (rs.next()) {
				
//********************************************Getting values from the DB ****************************************************************//
				
				actualAmount = rs.getDouble("actualAmount");
				Double currAmount = rs.getDouble("currentTotalFunds");
				String projectID = rs.getString("proID");
				String pronme = rs.getString("projectname");
				
				Double total = currAmount + Double.parseDouble(amount);
				
//******************************************* Validations before storing in the DB ******************************************************//
				if(actualAmount>total) {
					return Double.toString(total);
					}
				else if(actualAmount<=total){
					System.out.println("Funds are closed");
					pro.getApproval(pronme, "Closed");
					return Double.toString(actualAmount);
				}
			}
			conn.close();
			
		} catch (Exception e) {
			output = "Error while reading the Funds.";
			System.err.println(e.getMessage());
		}
		
		return output;
	}

}
