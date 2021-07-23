package com.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import model.Fund;

/**
 * FundService
 * 
 ********************************************************************************************************
 * ### Date Author Description
 * -------------------------------------------------------------------------------------------------------
 * 1 15-04-2021 David Created
 * 
 ********************************************************************************************************
 */



@Path("/Funds")
public class FundService {

	Fund fund = new Fund();

//*****************************************************Insert Fund	Details*******************************************//

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String insertFunds(@FormParam("proID") String proID, @FormParam("actualAmount") String actualAmount) {
		String output = fund.insertFunds(proID, actualAmount);
		return output;
	}

// **************************************************************END***************************************************//

// *****************************************************Get All Fund Details*******************************************//
	
	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public String readFunds() {
		return fund.readFunds();
	}

// **************************************************************END***************************************************//

// *****************************************Get Fund Details and Projects details By ID********************************//

	@GET
	@Path("/{ID}")
	@Produces(MediaType.TEXT_HTML)
	public String readFundById(@PathParam("ID") String id) {

		String output = fund.readFundById(id);
		return output;
	}

// **************************************************************END***************************************************//

// **********************Update Funds and if the actual payment received updating Project Status***********************//
	
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePayment(String fundData) {
		JsonObject fundObject = new JsonParser().parse(fundData).getAsJsonObject();
		String fundID = fundObject.get("fundID").getAsString();
		String amount = fundObject.get("currentTotalFunds").getAsString();

		String output = fund.updatePayment(fundID, amount);
		return output;
	}

// **************************************************************END***************************************************//

	@POST
	@Path("/donate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String sendProDetais(String projectData) {

		System.out.println("safsfsf**************");	
		JsonObject proObj = new JsonParser().parse(projectData).getAsJsonObject();
		String ProID = proObj.get("projectID").getAsString();
		String Proname = proObj.get("projectname").getAsString();
		String amount = proObj.get("amount").getAsString();
			
		return sendDeatailsToPayment(ProID,Proname,amount);
	}

	// method to get the current logged user's info from accountService
	
public String sendDeatailsToPayment(String proID, String pname, String amount) {
		
		String currentUserDetails = getCurrentLoggedUserinfo();

		JsonObject userJSONobj = new JsonParser().parse(currentUserDetails).getAsJsonObject();
		
		String cname = userJSONobj.get("UserName").getAsString();
		String cusmail = userJSONobj.get("Email").getAsString();
		String cID = userJSONobj.get("UId").getAsString();
		Client client = Client.create();

		String url = "http://localhost:8081/PaymentService/Payment_Service/Payments/donate";
		WebResource resource = client.resource(url);
		
		String input = "{\"ProjectID\":\"" + proID + "\",\"projectName\":\"" + pname + "\",\"actualName\":\""
				+ amount + "\",\"customerid\":\"" + cID + "\", \"customerName\":\"" + cname
				+ "\",\"mail\":\"" + cusmail + "\"}";
		
		ClientResponse response = resource.type("application/json").post(ClientResponse.class, input);
		String output = response.getEntity(String.class);

		return output;
	}
	
	public String getCurrentLoggedUserinfo() {

		Client client = Client.create();

		WebResource webResource = client.resource("http://localhost:8081/UserAccounts/UserAccountService/User_logins");

		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

		String output = response.getEntity(String.class);

		return output;

	}
}
